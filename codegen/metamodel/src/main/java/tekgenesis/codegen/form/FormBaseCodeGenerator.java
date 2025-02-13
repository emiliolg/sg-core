
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.form;

import java.util.function.Supplier;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.codegen.impl.java.JavaElement;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.media.Mime;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.form.widget.ExportType;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;
import tekgenesis.type.ModelType;
import tekgenesis.type.Type;
import tekgenesis.type.permission.Permission;

import static java.lang.String.format;
import static java.util.Arrays.copyOf;

import static tekgenesis.codegen.CodeGeneratorConstants.*;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.codegen.form.DbObjectMethodsCodeGenerator.ACTION_DEFAULT;
import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.util.JavaReservedWords.VOID;
import static tekgenesis.metadata.form.model.FormConstants.FIND_METHOD_NAME;
import static tekgenesis.metadata.form.model.FormConstants.POPULATE_METHOD_NAME;
import static tekgenesis.type.permission.PredefinedPermission.isPredefined;

/**
 * Generate the Code for the xxFormBase class.
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "OverlyComplexClass", "ClassTooDeepInInheritanceTree" })
public class FormBaseCodeGenerator extends UiModelBaseCodeGenerator<Form> {

    //~ Constructors .................................................................................................................................

    /** Create a FormBaseCodeGenerator. */
    public FormBaseCodeGenerator(JavaCodeGenerator codeGenerator, @NotNull Form form, @NotNull Option<MetaModel> binding,
                                 ModelRepository repository) {
        super(codeGenerator, form, binding.castTo(DbObject.class).filter(ignored -> !form.isListing()), repository);
    }

    //~ Methods ......................................................................................................................................

    public void populate() {
        final String modelType = getFormBindingType();
        asAbstract().withSuperclass(generic(FORM_INSTANCE, modelType));

        // Generate db object methods such as create, update, delete, and so on.
        generateDbObjectMethods(model);

        // Generate primary key methods
        generateSetPrimaryKeyMethod();
        generateKeyAsStringMethod();

        // Generate populate method
        generatePopulateMethod(false);

        generateAbstractMethod(model.getOnLoadMethodName(), "Invoked when the form is loaded");
        generateAbstractActionMethod(model.getOnDisplayMethodName(), "Invoked when the form is displayed");
        generateAbstractMethod(model.getOnCancelMethodName(), "Invoked when the form is canceled");
        generateAbstractMethod(model.getOnScheduleMethodName(), "Invoked when scheduled interval is triggered", FORM_ACTION);

        // Fields orthogonal accessors
        generateMessageMethod();
        generateIsDefinedMethod();
        generateResetMethod();
        generateFocusMethod();
        generateLabelMethod();
        generateConfigureMethod();

        // Add copy to methods
        generateCopyToMethod();

        // Add createOrUpdate subform methods
        generateCreateOrUpdateSubforms();

        // Add createOrUpdate multiple methods
        generateCreateOrUpdateMultiples();

        // Generate widget methods
        traverse(model);

        if (!model.getParameters().isEmpty()) generateParametersMethod(generateParametersClass());

        addListenersMethod();

        if (!model.getPermissions().filter(p -> p != null && !isPredefined(p.name())).isEmpty()) createPermissionsEnum();

        super.populate();
    }  // end method populate

    @Override protected void populateSubform(Method method, String instance, Binding b) {
        String invoke = invoke(b.invokeWidgetGetter(this, instance), KEY_AS_STRING_METHOD);

        // Ensure null check if requires differ
        if (!b.field().isRequired() && b.widget().isRequired()) invoke = ensureNotNu11(this, invoke, b.getFieldName());

        method.statement(invoke("", CREATE_METHOD + capitalizeFirst(b.getWidgetName()), invoke));
    }

    @Override void generateEmptyPopulateMethod() {
        final Method method = method(POPULATE_METHOD_NAME, getFormBindingType());
        if (!model.getPrimaryKeyAsStrings().isEmpty()) addAbstractMethod(method.asAbstract());
        else method.asFinal().throwNew(extractImport("tekgenesis.form.exception.FormCannotBePopulatedException"), quoted(model.getFullName()));
        method.boxedNotNull().withComments(POPULATE_COMMENT);
    }

    @Override void generateExportOnClick(@NotNull final Widget button, @NotNull final String methodName) {
        final String     tableName = button.getButtonBoundId();
        final ExportType type      = button.getExportType();
        if (!hasMethod(methodName)) {
            final String         exportClassName = getTableExportClassName(type.getName(), tableName);
            final MultipleWidget tableWidget     = (MultipleWidget) model.getElement(tableName);

            if (!hasInner(exportClassName)) addInner(new ExportTableCodeGenerator(this, exportClassName, tableWidget, type));

            final Method method = method(methodName, extractImport(FORM_ACTION)).notNull().withComments(format("Called on exporting %s.", tableName));

            method.declare(extractImport(FORM_ACTION), RESULT, ACTION_DEFAULT);
            method.statement(
                invoke(
                    invoke(invoke(RESULT, "withDownload", classOf(exportClassName)),
                        "withFileName",
                        quoted("exported-" + tableName + type.getExtension())),
                    "withContentType",
                    extractStaticImport(createQName(Mime.class.getCanonicalName(), type.getMime().name()))));
            method.return_(RESULT);
        }
    }

    @Override void generatePopulateMethod(@NotNull ModelType binding, @NotNull Bindings bindings, boolean copyPk) {
        final Method method = method(POPULATE_METHOD_NAME, binding.getName());

        final Seq<Binding> widgets     = bindings.filter(b -> b.isNotPrimaryKey() && !b.isProtected());
        final boolean      noProtected = !bindings.exists(b -> b.isNotPrimaryKey() && b.isProtected());

        if (widgets.isEmpty() && noProtected) {
            // just return the find() result, with out creating a variable if no field was populated
            method.return_(invoke("", FIND_METHOD_NAME));
            method.boxedNotNull().withComments(POPULATE_COMMENT);
            return;
        }

        // call find method and assign it first
        final String instance = deCapitalizeFirst(binding.getName());
        method.declare(binding.getImplementationClassName(), instance, FIND_METHOD_NAME + "()");
        method.blankLine();

        generatePopulateStatements(method, instance, binding.getImplementationClassName(), copyPk);

        method.blankLine();
        method.return_(instance);
        method.boxedNotNull().withComments(POPULATE_COMMENT);
    }

    @Override Iterable<InnerTableBaseCodeGenerator> getTables() {
        return tables;
    }

    private void addListener(final String className, final String method, final String comment) {
        final Method add = method(method, VOID).asStatic()
                           .asPublic()
                           .withComments(comment)
                           .statement(invoke(LISTENERS_VAR, method, LISTENER_TYPE, LISTENER));

        add.arg(LISTENER_TYPE, FORM_LISTENER_TYPE).notNull();
        add.arg(LISTENER, generic(FORM_LISTENER, className)).notNull();
    }

    private void addListenersMethod() {
        final String className       = model.getImplementationClassName();
        final String formListenerMap = extractImport("tekgenesis.form.extension.FormListenerMap");
        field(LISTENERS_VAR, generic(formListenerMap, className)).notNull().asPrivate().asStatic().withValue(newGeneric(formListenerMap));

        addListener(className, ADD_LISTENER, "Register a form Listener");
        addListener(className, REMOVE_LISTENER, "Remove a form Listener");
    }

    private void addParameterMethods(ClassGenerator cg) {
        addParameterMethods(cg, model);
    }

    private void addParameterMethods(ClassGenerator cg, Iterable<Widget> widgets) {
        for (final Widget widget : widgets) {
            final String widgetName = widget.getName();
            if (model.getParameter(widgetName).isPresent() && !widgetName.startsWith("$")) {
                final ElementAccessorCodeGenerator accessorGenerator = createAccessorGenerator(widget);
                final String                       methodName        = "with" + capitalizeFirst(widgetName);
                accessorGenerator.parameterBody(cg.method(methodName, cg.getName()).notNull());

                if (widget.isEntity()) {
                    final Method keyMethod = cg.method(methodName + "Key", cg.getName()).notNull();
                    accessorGenerator.parameterBody(keyMethod);
                }
            }
            addParameterMethods(cg, widget);
        }
    }

    private void createPermissionsEnum() {
        innerEnum(PERMISSION, model.getPermissions().filter(p -> p != null && !isPredefined(p.name())).map(p -> p.getName().toUpperCase()))
            .withInterfaces(Permission.class);
    }

    private void generateKeyAsStringMethod() {
        final Seq<String> primaryKey = model.getPrimaryKeyAsStrings();
        if (!isEmpty(primaryKey)) {
            final StrBuilder s = new StrBuilder(EMPTY).startCollection(CAT + quoted(":") + CAT);
            s.append(CAT);
            for (final String widgetName : primaryKey) {
                final Widget widget             = model.getElement(widgetName);
                final Type   type               = widget.getType();
                final String typeImplementation = widget.getType().getImplementationClassName();

                final String methodInvocation;
                if (type.isDatabaseObject())
                    methodInvocation = widgetAndKeyRange(widgetName)._2() == 1
                                       ? invokeStringsEscapeCharOn(getterName(widgetName + capitalizeFirst(KEY_ARG), STRING) + "()", "':'")
                                       : getterName(widgetName + capitalizeFirst(KEY_ARG), STRING) + "()";
                else
                    methodInvocation = type.isString() && primaryKey.size() > 1
                                       ? invokeStringsEscapeCharOn(getterName(widgetName, STRING) + "()", "':'")
                                       : invoke("", getterName(widgetName, typeImplementation));

                s.appendElement(methodInvocation);
            }
            method(KEY_AS_STRING_METHOD, String.class).notNull().return_(s);
        }
    }

    private ClassGenerator generateParametersClass() {
        final ClassGenerator cg = innerClass(model.getImplementationClassName() + "Parameters").asStatic()
                                  .asFinal()
                                  .asPublic()
                                  .withSuperclass(generic("tekgenesis.form.FormParameters", model.getImplementationClassName()));
        addParameterMethods(cg);

        return cg;
    }

    private void generateParametersMethod(@NotNull final ClassGenerator paramsClass) {
        method(PARAM_VAR, paramsClass.getName()).notNull().return_(new_(paramsClass.getName())).asStatic();
    }

    private void generateSetPrimaryKeyMethod() {
        final Seq<Tuple<Widget, Integer>> keys = model.getPrimaryKeyAsStrings().map(this::widgetAndKeyRange);
        if (keys.isEmpty()) return;

        final JavaElement.Method m = method(SET_PRIMARY_KEY);
        m.arg(KEY_ARG, String.class).notNull();

        if (keys.size() == 1) setWidgetValueFromString(m, keys.getFirst().map(Tuple::first).get(), KEY_ARG, 1);
        else {
            final int size = sum(keys);
            extractStaticImport(QName.createQName(SPLIT_ARRAY_IMPORT));
            m.declare(STRING_ARRAY, PARTS, invoke("", SPLIT_TO_ARRAY, KEY_ARG, String.valueOf(size))).asFinal();
            int i = 0;
            for (final Tuple<Widget, Integer> t : keys) {
                final String value = joinKeys(i, t.second());
                i += t.second();
                if (t.second() > 1) {
                    extractStaticImport(QName.createQName(IMMUTABLE_LIST));
                    extractStaticImport(QName.createQName(JOIN_IMPORT));
                }
                setWidgetValueFromString(m, t.first(), value, t.second());
            }
        }
    }  // end method generateSetPrimaryKeyMethod

    /** Invoke the method to convert the value into the type passed as an argument. */
    private String invokeFind(String value, String type) {
        return invokeStatic(type, "find", value);
    }

    private String invokeStringsEscapeCharOn(String toBeEscaped, String escaping) {
        extractStaticImport(QName.createQName(ESCAPE_CHAR));
        return invoke("", ESCAPE_CHAR_ON, toBeEscaped, escaping);
    }

    private String joinKeys(int index, int range) {
        final String result;
        if (range == 1) result = PARTS + "[" + index + "]";
        else {
            final StrBuilder builder = new StrBuilder().startCollection(", ");
            for (int i = index; i < index + range; i++)
                builder.appendElement(PARTS + "[" + i + "]");
            result = builder.toString();
        }
        return result;
    }  // end method joinKeys

    private int sum(Seq<Tuple<Widget, Integer>> keys) {
        return keys.map(Tuple::second).foldLeft(0, (a, b) -> a + b);
    }

    @NotNull private Tuple<Widget, Integer> widgetAndKeyRange(final String value) {
        final Widget keyWidget = model.getElement(value);
        final int    keyLength = keyWidget.getType().isDatabaseObject()
                                 ? getRepository().getModel(keyWidget.getType().getImplementationClassName(), DbObject.class)
                                   .get()
                                   .primaryKeySimpleFields()
                                   .size()
                                 : 1;
        return tuple(keyWidget, keyLength);
    }

    private String getFormBindingType() {
        final Supplier<String> supplier = () -> model.getPrimaryKeyAsStrings().isEmpty() ? Void.class.getName() : Object.class.getName();
        return getBinding().map(MetaModel::getFullName).orElseGet(supplier);
    }

    private void setWidgetValueFromString(JavaElement.Method m, Widget w, String value, int size) {
        m.statement(
            invoke(F_VAR,
                "set",
                getEnumField(w.getName()),
                w.getType().isDatabaseObject()
                    ? invokeFind(size > 1 ? invoke("", JOIN, invokeListOf(value), "\':\'") : value, w.getType().getImplementationClassName())
                    : invokeConvertFromString(value, w.getType().getImplementationClassName())));
    }

    //~ Methods ......................................................................................................................................

    /** Get field enum name based on widget id. */
    public static String getFieldEnumName(String widgetName) {
        return widgetName.startsWith("$") ? widgetName : fromCamelCase(widgetName);
    }

    static boolean isMutableReference(@NotNull final DbObject entity, final String reverseReference) {
        return !entity.getPrimaryKey().exists(pk -> pk != null && equal(pk.getName(), reverseReference));
    }

    private static String getTableExportClassName(String type, String tableName) {
        return capitalizeFirst(tableName) + type + "Dump";
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String[] FORM_SUPPRESSED_WARNINGS = copyOf(COMMON_SUPPRESSED_WARNINGS, COMMON_SUPPRESSED_WARNINGS.length + 1);

    static {
        // noinspection DuplicateStringLiteralInspection
        FORM_SUPPRESSED_WARNINGS[FORM_SUPPRESSED_WARNINGS.length - 1] = quoted("LocalVariableHidesMemberVariable");
    }
}  // end class FormBaseCodeGenerator
