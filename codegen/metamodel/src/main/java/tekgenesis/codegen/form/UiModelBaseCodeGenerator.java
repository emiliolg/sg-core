
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.MMCodeGenConstants;
import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.java.EnumGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.codegen.impl.java.JavaItemGenerator;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.entity.StructType;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.EntityReference;
import tekgenesis.type.ModelType;
import tekgenesis.type.Type;
import tekgenesis.type.permission.PredefinedPermission;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import static tekgenesis.codegen.CodeGeneratorConstants.*;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.codegen.form.DbObjectMethodsCodeGenerator.ACTION_DEFAULT;
import static tekgenesis.codegen.form.FormBaseCodeGenerator.getFieldEnumName;
import static tekgenesis.codegen.form.FormBaseCodeGenerator.isMutableReference;
import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.exists;
import static tekgenesis.common.core.Constants.INT;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.common.util.JavaReservedWords.NEW;
import static tekgenesis.common.util.JavaReservedWords.THROW;
import static tekgenesis.metadata.form.model.FormConstants.CREATE_OR_UPDATE_METHOD_NAME;
import static tekgenesis.metadata.form.model.FormConstants.POPULATE_METHOD_NAME;

/**
 * Generate code for {@link UiModel} base classes.
 */
abstract class UiModelBaseCodeGenerator<M extends UiModel> extends WidgetContainerClassGenerator implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull final M                                 model;
    @NotNull final List<InnerTableBaseCodeGenerator> tables;

    //~ Constructors .................................................................................................................................

    UiModelBaseCodeGenerator(@NotNull final JavaCodeGenerator cg, @NotNull final M model, @NotNull final Option<? extends ModelType> binding,
                             @NotNull final ModelRepository repository) {
        super(cg, model.getName() + BASE, collectBoundable(model, new ArrayList<>()), binding, repository);
        this.model = model;
        tables     = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public String getSourceName() {
        return model.getSourceName();
    }

    @Override protected void generateCodeForMultiple(MultipleWidget tableWidget) {
        final String rowElementName = getRowElementName(tableWidget.getName());
        final String enumField      = getEnumField(tableWidget.getName());

        // Get table method
        final String tableType = generic(FORM_TABLE, rowElementName);
        final Method table     = method(getterName(tableWidget.getName(), tableType), tableType).notNull().asFinal();
        table.withComments("Returns a " + link(tableType) + " instance to handle " + capitalizeFirst(tableWidget.getName()) + " manipulation");
        final String rowElementClass = classOf(rowElementName);
        table.return_("table(" + enumField + ", " + rowElementClass + ")");

        // Create table accessor if missing
        if (tables.isEmpty()) {
            final String generic = generic(FORM_TABLE, "T");
            final Method method  = method(MMCodeGenConstants.TABLE, generic).withGenerics("T").notNull().asFinal().asPackage();
            method.withComments("Method to allow table row class override");
            method.arg(FIELD, FormFieldRef.class).notNull();
            method.arg("rowClass", generic(Class.class, "T")).notNull();
            method.return_("f.table(field, rowClass, this)");
        }

        // Inner class
        final String                      fqn     = withPackage(model.getName() + "." + rowElementName);
        final Option<? extends ModelType> binding = getBindingForType(tableWidget.getType());
        final InnerTableBaseCodeGenerator inner   = addInner(new InnerTableBaseCodeGenerator(this, extractImport(fqn), tableWidget, binding));
        inner.generation();
        inner.asAbstract();
        tables.add(inner);

        if (tableWidget.getWidgetType() != WidgetType.TABLE) addOnChangeMethod(tableWidget, tableWidget.toString());

        generateListTableMethods(tableWidget, inner.getBindings());

        if (tableWidget.isLazyFetch()) generateLazyLoadMethod(tableWidget);

        if (isFiltered(tableWidget)) generateTableFilterClass(tableWidget, rowElementName);
    }

    @Override protected void populate() {
        withComments("Generated base class for " + kindAsString(model) + ": " + model.getName() + ".");
        withComments(MODIFICATION_WARNING_LINE_1);
        withComments(MODIFICATION_WARNING_LINE_2);
        suppressWarnings(EXTENDED_SUPPRESSED_WARNINGS);

        createUiModelAccessorField(this);

        // Add logger to base
        addLogger(model.getFullName());

        // Add actions method accessor
        addActionsMethod();

        // Create Field enumeration with descendant widgets
        createFieldsEnum();

        super.populate();
    }

    /*boolean findIfPrimaryIsBound(@NotNull final Option<DbObject> e) {
     *  return findIfPrimaryIsBound(e, "", Option.empty());
     *}*/

    /*String findIfPrimaryIsBound(@NotNull final Option<? extends ModelType> e, @NotNull final String given,
     *                          @NotNull final Option<MultipleWidget> table) {  //
     *  return e.castTo(DbObject.class).map(m -> m.getPrimaryKey().forAll(a -> FormCode.isBound(this, a, given, table))).orElse(false);
     *}*/

    @Override void generateCreateOrUpdateMultiples(@NotNull final ModelType modelType, @NotNull final Bindings b) {
        /* Currently only for entity bound multiples. */
        b.filter(Binding::isEntityMultiple).forEach(t -> {
            final Method method = method(CREATE_OR_UPDATE_METHOD_NAME + capitalizeFirst(t.getWidgetName()));

            final Attribute attribute = (Attribute) t.field();
            method.withComments(format("Updates external references to %s.", attribute));
            final Entity e = (Entity) t.getFinalType();

            final String  arg              = deCapitalizeFirst(modelType.getName());
            final boolean mutableReference = isMutableReference(e, attribute.getReverseReference());
            if (mutableReference || e.isInner()) method.arg(arg, modelType.getImplementationClassName()).notNull();

            final Widget   widget   = t.widget();
            final Bindings bindings = new Bindings(some(e), widget, attribute.getReverseReference());

            if (!bindings.isPrimaryKeyBound()) addAbstractMethod(method.asAbstract());
            else {
                if (e.isInner()) createOrUpdateInner(method, attribute, e, widget, arg);
                else if (e.hasDefaultPrimaryKey()) createOrUpdateSequence(method, attribute, e, widget, arg, bindings);
                else createOrUpdateMultiple(method, attribute, e, widget, mutableReference, arg, bindings);
            }
        });
    }  // end method generateCreateOrUpdateMultiples

    abstract Iterable<InnerTableBaseCodeGenerator> getTables();

    @Override String getThisClass() {
        return model.getImplementationClassName();
    }

    private void addActionsMethod() {
        method(ACTIONS_FIELD, ACTIONS_CLASS).asProtected()
            .notNull()
            .asFinal()
            .return_(ACTIONS_FIELD)
            .withComments("Returns utility methods to deal with {@link Action actions}.");
    }

    private void createFieldsEnum() {
        final ImmutableList.Builder<String> fieldsNames = ImmutableList.builder();
        addFields(model, fieldsNames);
        final EnumGenerator fields = innerEnum(FIELDS_ENUM, fieldsNames.build()).withInterfaces(FormFieldRef.class);

        fields.field("id", String.class).asFinal();
        fields.constructor().asPackage().assign("this.id", "id").arg("id", String.class).asFinal();
        fields.method("id", String.class).notNull().return_("id");
    }

    /** Generate createOrUpdate method for inner entities. */
    private void createOrUpdateInner(Method method, final Attribute attribute, final DbObject entity, Widget bind, String instance) {
        final String itemVar = deCapitalizeFirst(entity.getName());
        final String itName  = attribute.getName();

        final String inner     = invoke(instance, getterName(itName));
        final String newValues = invoke("", getterAttribute(bind, attribute));

        method.statement(invoke(inner, MERGE_METHOD, newValues, format("(%s, row) -> row.copyTo(%s)", itemVar, itemVar)));
    }

    /** Generate createOrUpdate method for multiple attributes. */
    private void createOrUpdateMultiple(Method method, final Attribute attribute, final DbObject entity, Widget bind, boolean mutableReference,
                                        final String arg, final Bindings bindings) {
        final String itemType       = entity.getImplementationClassName();
        final String rowElementName = getRowElementName(bind.getName());

        final List<String> keys = new LinkedList<>();
        for (final Attribute pk : entity.getPrimaryKey()) {
            if (equal(attribute.getReverseReference(), pk.getName())) keys.add(invoke("", KEY_AS_STRING_METHOD));
            else bindings.keys().getFirst(b -> pk.getName().equals(b.getFieldName())).ifPresent(b -> {
                if (b.widget().getType().isDatabaseObject()) keys.add(invoke("r", getterName(b.getWidgetName() + capitalizeFirst(KEY_ARG), STRING)));
                else keys.add(b.invokeWidgetGetter(this, "r"));
            });
        }

        final String instance = deCapitalizeFirst(entity.getName());
        method.startForEach(rowElementName, "r", invoke("", getterAttribute(bind, attribute)));
        final String findInstance = invokeStatic(itemType, FIND_METHOD, keys);
        method.declare(itemType, instance, findInstance);
        method.startIf(instance + EQ_NULL);
        final String newInstance = NEW + capitalizeFirst(instance);
        method.declare(itemType, newInstance, invokeStatic(itemType, CREATE_METHOD, keys));
        method.statement(invoke("r", COPY_TO_METHOD_NAME, newInstance));
        if (mutableReference) method.statement(invoke(newInstance, setterName(attribute.getReverseReference()), arg));
        method.statement(invoke(newInstance, INSERT_METHOD));
        method.startElse();
        method.statement(invoke("r", COPY_TO_METHOD_NAME, instance));
        if (mutableReference) method.statement(invoke(instance, setterName(attribute.getReverseReference()), arg));
        method.statement(invoke(instance, UPDATE_METHOD));
        method.endIf();
        method.endFor();
    }  // end method createOrUpdateMultiple

    /**
     * Generate createOrUpdate method for multiple attributes witch entity type has a default
     * sequence primary key.
     */
    private void createOrUpdateSequence(Method method, final Attribute attribute, final DbObject entity, final Widget bind, final String arg,
                                        final Bindings bindings) {
        final String itemType       = entity.getImplementationClassName();
        final String rowElementName = getRowElementName(bind.getName());

        final Attribute pk = entity.getPrimaryKey().getFirst().get();

        bindings.keys().getFirst().ifPresent(seqBing -> {
            final String pkGetter = seqBing.invokeWidgetGetter(this, "r");
            final String pkField  = pk.getName();

            method.startForEach(rowElementName, "r", invoke("", getterAttribute(bind, attribute)));
            method.declare(pk.getImplementationClassName(), pkField, pkGetter);

            final String instance = deCapitalizeFirst(entity.getName());
            method.startIf(pkField + EQ_NULL);
            method.declare(itemType, instance, invokeStatic(itemType, CREATE_METHOD));
            method.statement(invoke("r", COPY_TO_METHOD_NAME, instance));
            method.statement(invoke(instance, setterName(attribute.getReverseReference()), arg));
            method.statement(invoke(instance, INSERT_METHOD));
            method.startElse();
            final String findInstance = invokeStatic(itemType, FIND_METHOD, pkField);
            final String findNotNull  = ensureNotNu11(this, findInstance, extractName(itemType));
            method.declare(itemType, instance, findNotNull);
            method.statement(invoke("r", COPY_TO_METHOD_NAME, instance));
            method.statement(invoke(instance, setterName(attribute.getReverseReference()), arg));
            method.statement(invoke(instance, UPDATE_METHOD));
            method.endIf();
            method.endFor();
        });
    }
    private void generateLazyLoadMethod(Widget tableWidget) {
        final Argument offset = new Argument(OFFSET, INT);
        final Argument limit  = new Argument(LIMIT, INT);

        final Method lazy = method(tableWidget.getLazyFetchMethodName(), FORM_ACTION).withComments(
                format(INVOKED_WHEN_LAZY_REQUEST, tableWidget.getName()));
        lazy.arg(offset);
        lazy.arg(limit);

        getBindingForType(tableWidget.getType()).ifPresentOrElse(t -> {
                final String name      = t.getName();
                final String lambdaVar = name.toLowerCase().charAt(0) + "";
                lazy.statement(
                    invoke(invoke(invoke(invoke(invoke(name, LIST_METHOD), OFFSET, OFFSET), LIMIT, LIMIT), LIST_METHOD),
                        FOR_EACH,
                        lambdaVar + LAMBDA_OP + invoke(invoke(invoke(getterName(tableWidget.getName())), "add"), POPULATE_METHOD_NAME, lambdaVar)));
                lazy.return_(ACTION_DEFAULT);
            },
            lazy::asAbstract);
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private void generateListOnChange(MultipleWidget w, Bindings bindings, String rowClass, String type, String instance) {
        getListOnChange(w).ifPresent(method -> {
            if (bindings.isPrimaryKeyBound()) {
                method.startIf(invoke("forms", "hasPermission", extractImport(PredefinedPermission.class) + "." +
                        PredefinedPermission.CREATE.name()));

                final String tableType = generic(FORM_TABLE, rowClass);
                method.declare(rowClass, ROW_VAR, invoke(invoke("", getterName(w.getName(), tableType)), GET_CURRENT));

                final String keys = createKeyArguments(bindings, ROW_VAR);
                if (bindings.hasDefaultPrimaryKey()) {
                    method.declare(Integer.class, PRIMARY_KEY_METHOD, keys);
                    method.declare(type,
                        instance,
                        PRIMARY_KEY_METHOD + " != null ? " + ensureNotNu11(this, invoke(type, FIND_METHOD, PRIMARY_KEY_METHOD), keys) + " : " +
                        invoke(type, CREATE_METHOD));
                }
                else {
                    final String keyFields = createKeyFields(bindings);
                    method.statement(
                        "if (!" + invoke(ROW_VAR, IS_DEFINED, keyFields) + ") " + THROW + " " +
                        new_(IllegalArgumentException.class, quoted("Trying to use a null key.")));
                    method.declare(type, instance, invoke(type, FIND_OR_CREATE, invoke(ROW_VAR, KEY_AS_STRING_METHOD)));
                }

                method.statement(invoke(ROW_VAR, COPY_TO_METHOD_NAME, instance));

                method.statement(invoke(instance, PERSIST_METHOD));

                if (bindings.hasDefaultPrimaryKey()) {
                    final Binding pk     = bindings.keys().getFirst().get();
                    final String  getter = getterName(pk.getFieldName(), pk.field().getTypeAsString());
                    method.statement(invoke(ROW_VAR, setterName(pk.getWidgetName()), invoke(instance, getter)));
                }

                method.endIf();

                method.return_(ACTION_DEFAULT);
            }
            else addAbstractMethod(method.asAbstract());
        });
    }  // end method generateListOnChange

    private void generateListOnLoad(MultipleWidget w, String rowClass, String type, String instance) {
        final String onLoad = w.getOnLoadMethodName();
        if (isEmpty(onLoad)) return;

        final String tableType = generic(FORM_TABLE, rowClass);
        final Method method    = method(onLoad).withComments(format("Called on loading %s.", w));

        method.declare(tableType, MMCodeGenConstants.TABLE, getterName(w.getName(), tableType) + "()");
        method.statement(
            invoke(type, FOR_EACH, instance + LAMBDA_OP + (invoke(invoke(MMCodeGenConstants.TABLE, "add"), POPULATE_METHOD_NAME, instance))));
    }

    private void generateListTableMethods(MultipleWidget w, Bindings bindings) {
        final boolean hasOnChange = isNotEmpty(w.getOnChangeMethodName());
        final String  rowClass    = getRowElementName(w.getName());

        if (bindings.isDbObject()) {
            final EntityReference entityReference = (EntityReference) w.getType();
            final String          type            = extractImport(entityReference.getFullName());
            final String          instance        = CURRENT + type;

            // generate onLoad method, if present...
            generateListOnLoad(w, rowClass, type, instance);

            if (hasOnChange)
            // generate onChange method, if present...
            generateListOnChange(w, bindings, rowClass, type, instance);
        }
        else if (hasOnChange) {
            final Option<Method> listOnChange = getListOnChange(w);
            if (listOnChange.isPresent()) addAbstractMethod(listOnChange.get().asAbstract());
        }
    }

    private void generateTableFilterClass(@NotNull final MultipleWidget multiple, String rowElementName) {
        final String filterName = capitalizeFirst(multiple.getName());

        addInner(new FormFilterTableCodeGenerator(this, filterName, multiple, rowElementName));

        final String filterClass = FormFilterTableCodeGenerator.getFilterClass(filterName);
        field(fromCamelCase(rowElementName) + "_FILTERS", filterClass, new_(filterClass)).asPublic().asStatic().asFinal().notNull();
    }

    private Option<? extends ModelType> getBindingForType(@NotNull final Type type) {
        final String                      fqn = type.getImplementationClassName();
        final Option<? extends ModelType> db  = getRepository().getModel(fqn, DbObject.class);
        if (db.isPresent()) return db;
        return getRepository().getModel(fqn, StructType.class);
    }

    /** Return true if there's any widget filtering given multiple. */
    private boolean isFiltered(@NotNull final MultipleWidget multiple) {
        return exists(model.getDescendants(),
            widget -> widget != null && widget.getWidgetType().isMultiple() && multiple.getName().equals(widget.getFiltering()));
    }

    /**
     * Returns on_change method if it must be added, or none() if the method has been added due to
     * reuse from another field.
     */
    private Option<Method> getListOnChange(MultipleWidget w) {
        final String onChange = w.getOnChangeMethodName();
        return !hasAbstractMethod(onChange)
               ? some(method(onChange, extractImport(FORM_ACTION)).notNull().withComments(format("Called each time %s changes.", w)))
               : Option.empty();
    }

    //~ Methods ......................................................................................................................................

    static void createUiModelAccessorField(JavaItemGenerator<?> g) {
        // Add annotation since this is inserted using reflection
        g.field(F_VAR, UI_MODEL_ACCESSOR).asPrivate().notNull().suppressWarnings(FIELD_F_SUPPRESS_WARNINGS);
    }

    static String fieldToWidget(JavaItemGenerator<?> g, String target, String instance, Binding binding) {
        final String setter = binding.isSubform() ? CREATE_METHOD + capitalizeFirst(binding.getWidgetName()) : setterName(binding.getWidgetName());
        final String getter = binding.isSubform() ? KEY_AS_STRING_METHOD : getterName(binding.getFieldName(), binding.getImplementationClassName());

        String invoke = g.invoke(instance, getter);

        // ensure null check if requires differ
        if (!binding.field().isRequired() && binding.widget().isRequired()) invoke = ensureNotNu11(g, invoke, binding.getFieldName());

        return g.invoke(target, setter, invoke);
    }

    static String kindAsString(UiModel uim) {
        return uim.getMetaModelKind().name().toLowerCase();
    }

    static String getEnumField(final String name) {
        return FIELDS_ENUM + "." + getFieldEnumName(name);
    }

    private static void addFields(Iterable<Widget> widgets, ImmutableList.Builder<String> fieldsNames) {
        for (final Widget widget : widgets) {
            final String widgetName = widget.getName();
            fieldsNames.add(getFieldEnumName(widgetName) + "(" + quoted(widgetName) + ")");
            addFields(widget, fieldsNames);
        }
    }

    /**
     * Collect all widgets subject to be bound ("boundable") on the given scope. Exclude inner
     * scopes such as table descendants.
     */
    @NotNull private static Collection<Widget> collectBoundable(@NotNull final Iterable<Widget> model, @NotNull final List<Widget> result) {
        for (final Widget widget : model) {
            result.add(widget);
            if (!widget.getWidgetType().isMultiple()) collectBoundable(widget, result);
        }
        return result;
    }

    //~ Static Fields ................................................................................................................................

    private static final String INVOKED_WHEN_LAZY_REQUEST = "Invoked when %s lazy requests items.";
    static final String         POPULATE_COMMENT          = "Invoked when populating a form instance";

    private static final String[] FIELD_F_SUPPRESS_WARNINGS = { INSTANCE_VARIABLE_MAY_NOT_BE_INITIALIZED, UNUSED_DECLARATION, NULLABLE_PROBLEMS };

    @NonNls private static final String[] EXTENDED_SUPPRESSED_WARNINGS = ImmutableList.<String>build(builder -> {
                builder.addAll(asList(COMMON_SUPPRESSED_WARNINGS));
                builder.add(LOCAL_VARIABLE_HIDES_MEMBER_VARIABLE);
                builder.add(UNUSED_PARAMETERS);
                builder.add(EMPTY_METHOD_WARNING);
            }).toArray(new String[] {});
}  // end class UiModelBaseCodeGenerator
