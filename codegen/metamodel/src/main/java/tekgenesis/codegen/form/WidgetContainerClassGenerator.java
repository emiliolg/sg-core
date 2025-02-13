
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
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.check.CheckType;
import tekgenesis.codegen.common.AbstractMethods;
import tekgenesis.codegen.common.MMCodeGenConstants;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.codegen.impl.java.JavaItemGenerator;
import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Decimals;
import tekgenesis.common.core.Option;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.StructType;
import tekgenesis.metadata.form.widget.ButtonType;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetDef;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.model.KeyMap;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.ArrayType;
import tekgenesis.type.DecimalType;
import tekgenesis.type.ModelType;
import tekgenesis.type.Type;

import static java.lang.String.format;

import static tekgenesis.codegen.CodeGeneratorConstants.*;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.codegen.form.FormBaseCodeGenerator.getEnumField;
import static tekgenesis.codegen.type.StructTypeCodeGenerator.FROM_JSON;
import static tekgenesis.codegen.type.StructTypeCodeGenerator.TO_JSON;
import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Constants.*;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.common.util.JavaReservedWords.NEW;
import static tekgenesis.common.util.JavaReservedWords.THIS;
import static tekgenesis.common.util.JavaReservedWords.VOID;
import static tekgenesis.field.FieldOption.ON_CLICK;
import static tekgenesis.metadata.form.model.FormConstants.*;
import static tekgenesis.metadata.form.model.FormConstants.WIDGET;
import static tekgenesis.metadata.form.widget.ButtonType.EXPORT;
import static tekgenesis.metadata.form.widget.ButtonType.REMOVE_ROW;
import static tekgenesis.metadata.form.widget.WidgetType.*;
import static tekgenesis.metadata.form.widget.WidgetTypes.hasOptions;
import static tekgenesis.metadata.form.widget.WidgetTypes.supports;
import static tekgenesis.type.Kind.DECIMAL;
import static tekgenesis.type.Types.elementType;

@SuppressWarnings({ "ClassWithTooManyMethods", "OverlyComplexClass" })
abstract class WidgetContainerClassGenerator extends ClassGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final AbstractMethods             ab         = new AbstractMethods(this);
    private final Option<? extends ModelType> binding;
    private final Bindings                    bindings;
    private final ModelRepository             repository;

    //~ Constructors .................................................................................................................................

    /** Used to create inner. */
    WidgetContainerClassGenerator(WidgetContainerClassGenerator cg, String name, Iterable<Widget> widgets, Option<? extends ModelType> binding) {
        super(cg, name);
        this.binding = binding;
        repository   = cg.getRepository();
        bindings     = new Bindings(binding, widgets);
    }

    /** Used from FormBaseCodeGenerator. */
    WidgetContainerClassGenerator(JavaCodeGenerator cg, String name, Iterable<Widget> widgets, Option<? extends ModelType> binding,
                                  ModelRepository repository) {
        super(cg, name);
        this.binding    = binding;
        this.repository = repository;
        bindings        = new Bindings(binding, widgets);
    }

    //~ Methods ......................................................................................................................................

    @SuppressWarnings("WeakerAccess")
    public ElementAccessorCodeGenerator createAccessorGenerator(final Widget widget) {
        if (widget.getType() instanceof StructType) return new StructTypeAccessorCodeGenerator(widget);
        if (widget.getType().getKind() == DECIMAL) return new DecimalAccessorCodeGenerator(widget);
        if (widget.getType().isEnum()) return new EnumAccessorCodeGenerator(widget);
        if (widget.getType().isTime()) return new DateAccessorCodeGenerator(widget);
        if (widget.getType().isDatabaseObject()) return new ReferenceAccessorCodeGenerator(widget);
        if (widget.isMultiple()) return new ArrayAccessorCodeGenerator(widget);
        return new ElementAccessorCodeGenerator(widget);
    }

    void addAbstractMethod(Method method) {
        ab.add(method.getName(), method);
    }

    void addOnChangeMethod(Widget w, String cause) {
        generateAbstractActionMethod(w.getOnChangeMethodName(), format("Invoked when %s value changes", cause));
    }

    void addOptionSetter(ElementAccessorCodeGenerator g) {
        g.addOptions(this);
    }

    String createKeyArguments(@NotNull Bindings b, @NotNull String scope) {
        return createKeyArguments(b, scope, ", ");
    }

    String createKeyArguments(@NotNull Bindings b, @NotNull String scope, @NotNull String separator) {
        return b.keys().map(k -> {
                final String getter = k.getFinalType().isDatabaseObject() ? getterName(k.getWidgetName() + capitalizeFirst(KEY_ARG),
                        Constants.STRING)
                                                                          : getterName(k.getWidgetName(), k.getImplementationClassName());
                return invoke(scope, getter);
            }).mkString(separator);
    }

    String createKeyFields() {
        return createKeyFields(bindings);
    }

    String createKeyFields(@NotNull final Bindings b) {
        final Seq<String> keys = b.keys().map(Binding::getWidgetName);
        return keys.map(UiModelBaseCodeGenerator::getEnumField).mkString(", ");
    }

    void delegateRemoveRowExecution(Method m, String table) {
        // Delegate execution to table's current row 'remove' method
        m.return_(invoke(invoke(invoke(getterName(table, "")), GET_CURRENT), REMOVE_METHOD));
    }

    Option<Method> generateAbstractActionMethod(final String methodName, final String comment) {
        return ab.generateAbstractMethod(methodName, comment, extractImport(FORM_ACTION));
    }

    void generateAbstractMethod(final String methodName, final String comment) {
        generateAbstractMethod(methodName, comment, VOID);
    }

    Option<Method> generateAbstractMethod(String methodName, String comment, String type) {
        return ab.generateAbstractMethod(methodName, comment, type);
    }

    void generateCodeForMultiple(MultipleWidget widget) {}

    /** Generate configure(Enum) method. */
    void generateConfigureMethod() {
        final Method m = method(CONFIGURATION_METHOD_NAME, "T").withGenerics("T extends " + extractImport(WIDGET_CONFIGURATION))
                         .asProtected()
                         .notNull();
        m.withComments("Returns a typed configuration for a given field.");
        m.arg(FIELD, FIELDS_ENUM).notNull();
        m.return_(invoke(F_VAR, CONFIG_METHOD_NAME, FIELD));
    }

    /** Generate copyTo method. */
    final void generateCopyToMethod() {
        binding.filter(this::isUpdatable).ifPresent(this::generateCopyToMethod);
    }

    /** Generate multiples createOrUpdate methods. */
    final void generateCreateOrUpdateMultiples() {
        binding.ifPresent(b -> generateCreateOrUpdateMultiples(b, bindings));
    }

    void generateCreateOrUpdateMultiples(@NotNull final ModelType modelType, @NotNull final Bindings b) {}

    /** Generate subforms createOrUpdate methods. */
    final void generateCreateOrUpdateSubforms() {
        binding.ifPresent(this::generateCreateOrUpdateSubforms);
    }

    void generateDbObjectMethods(Form form) {
        binding.castTo(DbObject.class).ifPresent(model -> {
            // Generate db objects methods
            new DbObjectMethodsCodeGenerator(this, model, bindings).generate();
        });
    }

    void generateEmptyPopulateMethod() {}

    void generateExportOnClick(Widget button, String methodName) {}

    void generateFocusMethod() {
        final Method focus = method(FOCUS, VOID).asProtected().notNull();
        focus.arg(FIELD, FIELDS_ENUM).notNull();
        focus.statement("f.focus(" + FIELD + ")");
        focus.withComments("Focuses given field.");
    }

    void generateIsDefinedMethod() {
        final Method defined = getter(DEFINED, Boolean.class).asPublic().notNull();
        defined.arg(FIELDS, FIELDS_ENUM + "...").notNull();
        defined.return_("f.defined(" + FIELDS + ")");
        defined.withComments("Returns true if the field value is not null.");
    }

    void generateLabelMethod() {
        final Method focus = method(MMCodeGenConstants.LABEL, String.class).asProtected().notNull();
        focus.arg(FIELD, FIELDS_ENUM).notNull();
        focus.statement("return f.label(" + FIELD + ")");
        focus.withComments("Returns the label of the given field.");
    }

    void generateMessageMethod() {
        @SuppressWarnings("DuplicateStringLiteralInspection")
        final Method m = method("message", extractImport(FORM_MESSAGE)).asPublic().notNull();
        m.arg(FIELD, FIELDS_ENUM).notNull();
        m.arg("msg", String.class).notNull();
        m.return_("f.msg(" + FIELD + ", msg)");
        m.withComments("Associates a new message to the specified field and returns it for any further configuration.");
    }

    void generateOptionsMethod() {
        final Method m = method(Constants.OPTIONS, VOID).asProtected().notNull();
        m.arg(FIELD, FIELDS_ENUM).notNull();
        m.arg(Constants.OPTIONS, KeyMap.class).notNull();
        m.statement("f." + "opts" + "(" + FIELD + ", " + Constants.OPTIONS + ")");
        m.withComments("Sets options to the given field.");
    }

    /** Generate populate method. */
    final void generatePopulateMethod(boolean copyPk) {
        binding.ifPresentOrElse(b -> generatePopulateMethod(b, bindings, copyPk), this::generateEmptyPopulateMethod);
    }

    void generatePopulateMethod(@NotNull final ModelType modelType, @NotNull final Bindings b, boolean copyPk) {
        final String instance = deCapitalizeFirst(modelType.getName());
        final Method method   = method(POPULATE_METHOD_NAME);
        method.withComments(POPULATE_FIELD_VALUES + instance + " " + INSTANCE + ".");
        method.arg(instance, modelType.getImplementationClassName()).notNull();

        generatePopulateStatements(method, instance, modelType.getImplementationClassName(), copyPk);
    }

    void generatePopulateStatements(@NotNull final Method method, @NotNull final String instance, @NotNull final String instanceType,
                                    boolean copyPk) {
        final Predicate<Binding> pks          = b -> copyPk || b.isNotPrimaryKey();
        final Seq<Binding>       widgets      = bindings.filter(pks).filter(Binding::isNotProtected);
        final boolean            anyProtected = bindings.filter(pks).exists(Binding::isProtected);

        final Seq<Binding> widgetsToPopulate = widgets.filter(b -> b.isNotCalculated() && b.isNotFieldMultiple());

        modelToUiStatements(method, instance, widgetsToPopulate.filter(b -> b.isNotSubform() && b.isNotWidgetDef()));

        widgetsToPopulate.filter(Binding::isWidgetDef).forEach(widgetDef -> {
            method.blankLine();
            populateWidgetDef(method, instance, widgetDef);
        });

        widgetsToPopulate.filter(Binding::isSubform).forEach(subform -> {
            method.blankLine();
            populateSubform(method, instance, subform);
        });

        widgets.filter(Binding::isFieldMultiple).forEach(multiple -> {
            method.blankLine();
            populateMultiple(method, instance, multiple);
        });

        if (anyProtected) {
            method.blankLine();
            method.statement(invoke("", POPULATE_PROTECTED, instance));

            /* Ensure populate protected method is defined. */
            generateAbstractMethod(POPULATE_PROTECTED,
                protectedMethodJavadoc(POPULATE_METHOD_NAME, instanceType),
                new Argument(instance, extractImport(instanceType)).notNull());
        }
    }

    void generateResetMethod() {
        final Method reset = method(RESET, VOID).asProtected().notNull();
        reset.arg(FIELDS, FIELDS_ENUM + "...").notNull();
        reset.statement("f.reset(" + FIELDS + ")");
        reset.withComments("Resets the given fields.");
    }

    boolean hasAbstractMethod(@NotNull final String methodName) {
        return ab.has(methodName);
    }

    void populateSubform(Method method, String instance, Binding b) {}

    @SuppressWarnings("OverlyLongMethod")
    void traverse(Iterable<Widget> widgets) {
        for (final Widget w : widgets) {
            final WidgetType widgetType = w.getWidgetType();
            final ButtonType buttonType = w.getButtonType();

            if (supports(widgetType, ON_CLICK) && widgetNotHasStandardOnClick(widgetType, buttonType)) generateOnClickActionMethod(w);

            switch (widgetType) {
            case BUTTON:
                if (buttonType == REMOVE_ROW && isNotEmpty(w.getOnClickMethodName()))
                    generateRemoveRowMethod(w.getButtonBoundId(), w.getOnClickMethodName());
                if (buttonType == EXPORT) generateExportOnClick(w, w.getOnClickMethodName());
                break;
            case CHART:
                generateAbstractChartOnClickMethod(w.getOnClickMethodName(), format(INVOKED_WHEN_IS_CLICKED, w));
                generateCodeForMultiple((MultipleWidget) w);
                break;
            case TABLE:
                generateAbstractActionMethod(w.getOnSelectionMethodName(), format(INVOKED_WHEN_IS_CLICKED, w));
                generateCodeForMultiple((MultipleWidget) w);
                break;
            case SECTION:
            case MAP:
                addOnNewLocationMethod(w, w.getName());
                generateCodeForMultiple((MultipleWidget) w);
                break;
            case SEARCH_BOX:
                generateSuggestBoxMethods(w);
                break;
            case TAGS_SUGGEST_BOX:
            case SUGGEST_BOX:
                generateSuggestBoxMethods(w);
                generateValueWidgetMethods(w);
                break;
            case SUBFORM:
                generateSubformMethods(w);
                break;
            case WIDGET:
                generateWidgetDefMethods(w);
                break;
            case VERTICAL:
            case HORIZONTAL:
                if (w.isCollapsible()) generateValueWidgetMethods(w);
                traverse(w);
                break;
            default:
                if (w.hasValue()) generateValueWidgetMethods(w);
                traverse(w);
            }
        }
    }  // end method traverse

    @NotNull Iterable<Method> getAbstractMethods() {
        return ab.methods();
    }

    Option<? extends ModelType> getBinding() {
        return binding;
    }

    Bindings getBindings() {
        return bindings;
    }

    boolean isPrimaryKeyBound() {
        return bindings.isPrimaryKeyBound();
    }

    ModelRepository getRepository() {
        return repository;
    }

    abstract String getThisClass();

    /** Return true if binding exists and it's not a view nor a type. */
    boolean isEntity() {
        return binding.castTo(DbObject.class).map(db -> !db.isView()).orElse(false);
    }

    private void addOnBlurMethod(Widget w, String cause) {
        generateAbstractActionMethod(w.getOnBlurMethodName(), format("Invoked when %s blurs (widget or its child loses focus)", cause));
    }

    private Option<Method> addOnClickMethod(Widget w, String cause) {
        return generateAbstractActionMethod(w.getOnClickMethodName(), format(INVOKED_WHEN_IS_CLICKED, cause));
    }

    private void addOnNewLocationMethod(Widget w, String cause) {
        generateAbstractActionMethod(w.getOnNewLocationMethodName(), format("Invoked when user clicks on location button on map %s", cause))
            .ifPresent(m -> {
                m.arg("lat", DOUBLE);
                m.arg("lng", DOUBLE);
            });
    }

    private void addOnUiChangeMethod(Widget w, String cause) {
        generateAbstractActionMethod(w.getOnUiChangeMethodName(), format("Invoked when %s value ui changes", cause));
    }

    private void collectAbstractInvocations(Iterable<Widget> widgets, Widget trigger) {  //
        widgets.forEach(widget -> {
            if (widget.isAbstractInvocation()) {
                final String cause  = widget + " on " + trigger;
                final String change = widget.getOnChangeMethodName();
                if (isNotEmpty(change)) addOnChangeMethod(widget, cause);
                final String ui = widget.getOnUiChangeMethodName();
                if (isNotEmpty(ui)) addOnUiChangeMethod(widget, cause);
                final String blur = widget.getOnBlurMethodName();
                if (isNotEmpty(blur)) addOnBlurMethod(widget, cause);
                final String click = widget.getOnClickMethodName();
                if (isNotEmpty(click)) addOnClickMethod(widget, cause);
            }
            collectAbstractInvocations(widget, trigger);
        });
    }

    private void copyToMultiple(Method method, String instance, Binding b) {
        final String rowElementClass = getRowElementName(b.getWidgetName());
        final String elementsGetter  = getterName(extractName(b.getFieldName()), "");

        final String widgetGetter = b.invokeWidgetGetter(this, "");
        final String consumer     = rowElementClass + "::" + COPY_TO_METHOD_NAME;
        final String supplier     = extractImport(getElementClassName(b)) + "::" + NEW;
        method.statement(invoke(widgetGetter, MERGE_INTO_METHOD_NAME, invoke(instance, elementsGetter), consumer, supplier));
    }

    private void copyToWidgetDef(Method method, String instance, Binding b) {
        final String fieldGet  = b.invokeFieldGetter(this, instance);
        final String widgetGet = b.invokeWidgetGetter(this, "");

        if (b.widget().isRequired()) method.statement(invoke(widgetGet, COPY_TO_METHOD_NAME, fieldGet));
        else repository.getModel(createQName(b.getImplementationClassName())).ifPresent(m -> {
            final String copyToReference = extractImport(b.widget().getWidgetDefinitionFqn()) + "::" + COPY_TO_METHOD_NAME;final String deleteReference =
                instance + "::" + b.fieldSetter();if (m instanceof DbObject)
                method.statement(invoke(widgetGet, COPY_TO_METHOD_NAME, fieldGet, copyToReference, deleteReference));if (m instanceof StructType) {
                final String createReference = extractImport(b.getImplementationClassName()) + "::" + NEW;method.statement(
                    invoke(widgetGet, COPY_TO_METHOD_NAME, fieldGet, copyToReference, deleteReference, createReference));}
        });
    }  // end method copyToWidgetDef

    private void createSubformsGetterAndCreate(Widget subform) {
        final String formName      = extractImport(subform.getSubformFqn());
        final String formClassName = classOf(formName);

        final String createMethodName = CREATE_METHOD + capitalizeFirst(subform.getName());
        method(createMethodName, formName).notNull()                     //
        .withComments("Create and set a new " + formName + " instance")  //
        .return_("f.init(" + getEnumField(subform.getName()) + ", " + formClassName + ")");

        final String createWithPKMethodName = CREATE_METHOD + capitalizeFirst(subform.getName());
        final Method method                 = method(createWithPKMethodName, formName).notNull()  //
                                              .withComments("Create and populates set a new " + formName + " instance with a pk")  //
                                              .return_(invoke("f", "init", getEnumField(subform.getName()), formClassName, KEY_ARG));
        method.arg(KEY_ARG, String.class).notNull();

        getter(subform.getName(), formName)                                       //
        .withComments("Get the " + formName + " if defined, or null otherwise.")  //
        .withComments("@see #" + createMethodName)                                //
        .return_("f.subform(" + getEnumField(subform.getName()) + ", " + formClassName + ")");
    }

    private void generateAbstractChartOnClickMethod(final String methodName, final String comment) {
        final Option<Method> chartOnClick = generateAbstractActionMethod(methodName, comment);
        for (final Method m : chartOnClick) {
            if (!m.hasArgument(FIELD)) m.arg(FIELD, FIELDS_ENUM).notNull();
        }
    }

    private void generateAbstractMethod(final String methodName, final String comment, Argument... args) {
        generateAbstractMethod(methodName, comment, VOID, args);
    }

    private Option<Method> generateAbstractMethod(String methodName, String comment, String type, Argument... args) {
        return ab.generateAbstractMethod(methodName, comment, type, args);
    }

    private void generateAccessors(final Widget widget) {
        // skip auto generated widget ids
        if (!widget.hasGeneratedName()) {
            // add a method for each form elem in the correct parent (the table inner class if its a row, or the main form class)
            final ElementAccessorCodeGenerator g = createAccessorGenerator(widget);
            g.addGetter();

            final WidgetType widgetType = widget.getWidgetType();
            // Do not generate setter for is widgets && for message entity widget
            if (widget.getIsExpression().isNull() && widgetType != BREADCRUMB && !isMessageEntityWidget(widget)) {
                g.addSetter();
                if (widgetType == SUGGEST_BOX && widget.getType().isString()) g.suggestBoxSetter();
            }

            if (hasOptions(widgetType) || widgetType == MAIL_FIELD) addOptionSetter(g);
        }
    }

    private void generateCopyToMethod(@NotNull final ModelType modelType) {
        final String instance = deCapitalizeFirst(modelType.getName());
        final Method method   = method(COPY_TO_METHOD_NAME);
        method.arg(instance, modelType.getImplementationClassName()).notNull();
        method.withComments("Copies field values to given " + instance + " " + INSTANCE + ".");

        generateCopyToStatements(method, instance, modelType.getImplementationClassName());
    }

    private void generateCopyToStatements(@NotNull final Method method, @NotNull final String instance, @NotNull final String instanceType) {
        final Seq<Binding> widgets = bindings.filter(b -> b.isNotPrimaryKey() && !b.isReadOnly());

        final Seq<Binding> copyToWidgets = widgets.filter(b -> !b.isProtected() && !b.isArray());

        uiToModelStatements(method, instance, copyToWidgets.filter(Binding::isNotWidgetDef));

        copyToWidgets.filter(Binding::isWidgetDef).forEach(widgetDef -> {
            method.blankLine();
            copyToWidgetDef(method, instance, widgetDef);
        });

        widgets.filter(Binding::isArray).forEach(multiple -> {
            method.blankLine();  // Array types allow modifications
            copyToMultiple(method, instance, multiple);
        });

        /* If there is a protected field, generate a copy to protected method. */
        if (widgets.exists(Binding::isProtected)) {
            method.blankLine();
            method.statement(invoke("", COPY_TO_PROTECTED, instance));

            /* Ensure populate protected method is defined. */
            generateAbstractMethod(COPY_TO_PROTECTED,
                protectedMethodJavadoc(COPY_TO_METHOD_NAME, instanceType),
                new Argument(instance, extractImport(instanceType)).notNull());
        }
    }

    private void generateCreateOrUpdateSubforms(@NotNull final ModelType modelType) {
        bindings.filter(Binding::isSubform).forEach(b -> {
            final String name = b.getWidgetName();

            final Method method = method(CREATE_OR_UPDATE_METHOD_NAME + capitalizeFirst(name));
            method.withComments(format("Updates %s reference from subform %s.", b.getFieldName(), name));

            final String arg = deCapitalizeFirst(modelType.getName());
            method.arg(arg, modelType.getImplementationClassName()).notNull();
            method.arg(Constants.UPDATE, Boolean.class).notNull();

            final String subformClass = b.widget().getSubformFqn();
            method.declare(subformClass, name, invoke("", getterName(name, subformClass)));
            method.startIf(name + " != null");
            method.startIf(Constants.UPDATE).statement(invoke(name, UPDATE_METHOD));
            method.startElse().statement(invoke(name, CREATE_METHOD)).endIf();
            method.statement(invoke(arg, b.fieldSetter(), invoke(name, FIND_METHOD)));
            method.endIf();
        });
    }

    private void generateDefineMethod(Widget widget, String type, String definition) {
        final String defineMethodName = DEFINE_METHOD + capitalizeFirst(widget.getName());
        final Method method           = method(defineMethodName, type).notNull()
                                        .withComments("Define " + link(type, INSTANCE) + " to be used during interaction.")
                                        .asPackage();

        repository.getModel(createQName(definition), WidgetDef.class).ifPresent(w -> {
            if (w.isAbstract()) method.asAbstract();
            else method.return_(new_(definition));
        });
    }

    private void generateOnClickActionMethod(Widget w) {
        if (!w.isAbstractInvocation()) {
            for (final Method method : addOnClickMethod(w, w.toString())) {
                if (w.isFeedback()) method.arg(FEEDBACK, "tekgenesis.form.ExecutionFeedback").notNull();
            }
        }
    }

    private void generateOnSuggestSync(Widget w) {
        final Type   t               = w.getType().isArray() ? ((ArrayType) w.getType()).getElementType() : w.getType();
        final String returnClassName = t.getImplementationClassName();
        final String methodName      = w.getOnSuggestSyncMethodName();

        if (!methodName.isEmpty() && !hasAbstractMethod(methodName)) {
            final String returnType = generic(Iterable.class, t.isString() ? extractImport(SUGGESTION_CLASS) : returnClassName);

            final Option<Method> onSuggestSync = generateAbstractMethod(methodName, format(ON_SUGGEST_JAVADOC, w), extractImport(returnType));

            for (final Method m : onSuggestSync) {
                m.arg(QUERY_METHOD, String.class);
                m.notNull();
                final String returnNew = generic(ArrayList.class, t.isString() ? extractImport(SUGGESTION_CLASS) : returnClassName);
                m.return_(new_(returnNew));
            }
        }
    }

    private void generateRemoveRowMethod(String table, String methodName) {
        if (!hasMethod(methodName)) {
            final Method m = method(methodName, extractImport(FORM_ACTION)).notNull().withComments(format("Called on removing %s row.", table));
            delegateRemoveRowExecution(m, table);
        }
    }

    private void generateSubformMethods(Widget subform) {
        createSubformsGetterAndCreate(subform);
        implementSubformAbstractInvocations(subform);
    }

    private void generateSuggestBoxMethods(Widget w) {
        generateOnSuggestSync(w);

        final Option<Method> onNew = generateAbstractActionMethod(w.getOnNewMethodName(),
                format("Invoked when the 'Create new' options of the %s is clicked", w));

        // Avoid multiple on_new generate multiple arguments!
        for (final Method m : onNew) {
            boolean add = true;
            for (final Argument arg : m.getArguments()) {
                if (TEXT.equals(arg.getName())) {
                    add = false;
                    break;
                }
            }
            if (add) m.arg(TEXT, String.class);
        }
    }

    private void generateValueWidgetMethods(Widget w) {
        if (!w.isAbstractInvocation()) {
            addOnChangeMethod(w, w.toString());
            addOnBlurMethod(w, w.toString());
            addOnUiChangeMethod(w, w.toString());
        }
        generateAccessors(w);
    }

    private void generateWidgetDefMethods(Widget widget) {
        final String definition = widget.getWidgetDefinitionFqn();
        final String name       = extractImport(definition);

        generateDefineMethod(widget, name, definition);

        final String type   = widget.isRequired() ? name : generic(extractImport(OPTIONAL_WIDGET), name);
        final String method = widget.isRequired() ? WIDGET : "optionalWidget";
        getter(widget.getName(), type).withComments("Get widget " + link(name) + ".")  //
        .return_(invoke(F_VAR, method, getEnumField(widget.getName()), classOf(name))).notNull();

        addOnChangeMethod(widget, widget.toString());
        addOnUiChangeMethod(widget, widget.toString());
    }

    private void implementSubformAbstractInvocations(Widget widget) {
        repository.getModel(widget.getSubformFqn(), Form.class).ifPresent(f -> collectAbstractInvocations(f, widget));
    }

    private void modelToUiStatements(@NotNull final Method method, @NotNull final String instance, @NotNull final Seq<Binding> widgets) {
        final String invocation = widgets.foldLeft("",
                (previous, t) -> {
                    String invoke = t.invokeFieldGetter(this, instance);

                    // ensure null check if requires differ
                    if (!t.field().isRequired() && t.widget().isRequired()) invoke = ensureNotNu11(this, invoke, t.getFieldName());

                    return invoke(previous, t.widgetSetter(), true, invoke);
                });
        if (!invocation.isEmpty()) method.statement(invocation, true);
    }

    private void populateMultiple(final Method method, final String instance, final Binding b) {
        final String rowElementClass = getRowElementName(b.getWidgetName());
        final String elementsGetter  = getterName(extractName(b.getFieldName()), "");

        final String widgetGetter = b.invokeWidgetGetter(this, "");
        final String consumer     = rowElementClass + "::" + POPULATE_METHOD_NAME;
        method.statement(invoke(widgetGetter, POPULATE_METHOD_NAME, invoke(instance, elementsGetter), consumer));
    }

    private void populateWidgetDef(Method method, String instance, Binding b) {
        final String fieldGet  = b.invokeFieldGetter(this, instance);
        final String widgetGet = b.invokeWidgetGetter(this, "");

        if (b.widget().isRequired()) method.statement(invoke(widgetGet, POPULATE_METHOD_NAME, fieldGet));
        else {
            final String copyToReference = extractImport(b.widget().getWidgetDefinitionFqn()) + "::" + POPULATE_METHOD_NAME;
            method.statement(invoke(widgetGet, POPULATE_METHOD_NAME, fieldGet, copyToReference));
        }
    }  // end method populateWidgetDef

    @NotNull private String protectedMethodJavadoc(String method, @NotNull String instanceType) {
        return "Invoked from {@link #" + method + "(" + extractImport(instanceType) + ") method} to handle protected fields.";
    }

    private String referenceThisType(String var) {
        return extractName(getThisClass()).equals(getName()) ? var : cast(getThisClass(), var);
    }

    private void uiToModelStatements(@NotNull final Method method, @NotNull final String instance, @NotNull Seq<Binding> widgets) {
        final String invocation = widgets.foldLeft("",
                (previous, binding) -> {
                    final TypeField field    = binding.field();
                    final boolean   multiple = field.isMultiple();

                    if ((!multiple && !binding.isSubform() && !field.isSynthesized()) || (multiple && field.getType().isEnum())) {
                        String invoke = binding.invokeWidgetGetter(this, "");

                        if (multiple) invoke = seqToEnumSet(invoke, field instanceof Attribute ? ((Attribute) field).getElementClassName() : "");

                        // ensure null check if requires differ
                        if (!binding.widget().isRequired() && field.isRequired()) invoke = ensureNotNu11(this, invoke, binding.getWidgetName());

                        return previous.isEmpty() ? invoke(instance, setterName(field.getName()), invoke)
                                                  : invoke(previous, setterName(field.getName()), true, invoke);
                    }

                    return previous;
                });

        if (!invocation.isEmpty()) method.statement(invocation, true);
    }

    private boolean widgetNotHasStandardOnClick(WidgetType widgetType, ButtonType buttonType) {
        return widgetType != CHART && buttonType != REMOVE_ROW && buttonType != EXPORT;
    }

    private boolean isUpdatable(ModelType b) {
        return !(b instanceof DbObject) || ((DbObject) b).isUpdatable();
    }

    private String getElementClassName(Binding b) {
        final Type type = b.getFinalType();
        return Predefined.notNull(elementType(type), type).getImplementationClassName();
    }

    private boolean isMessageEntityWidget(Widget widget) {
        return equal(widget.getWidgetType(), MESSAGE) && equal(widget.getMsgType(), CheckType.ENTITY);
    }

    //~ Methods ......................................................................................................................................

    static String ensureNotNu11(final JavaItemGenerator<?> generator, final String invoke, final String fieldError) {
        return generator.invokeStatic(PREDEFINED_CLASS, ENSURE_NOT_NULL, invoke, quoted("'" + fieldError + NOT_FOUND));
    }

    static String getterAttribute(@NotNull final Widget widget, @NotNull final TypeField field) {
        final String type = field.getImplementationClassName();
        return getterName(widget.getName(), type);
    }

    @SuppressWarnings("WeakerAccess")
    static String getRowElementName(String name) {
        return capitalizeFirst(name) + "Row";
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String POPULATE_FIELD_VALUES = "Populate field values with given ";
    private static final String         POPULATE_PROTECTED    = "populateProtectedFields";

    @NonNls static final String ON_SUGGEST_JAVADOC = "Invoked when the user type something on %s to create suggest list";

    @NonNls
    @SuppressWarnings("WeakerAccess")
    public static final String         STRING = String.class.getSimpleName();
    private static final String        LONG   = Long.class.getSimpleName();

    private static final String INVOKED_WHEN_IS_CLICKED = "Invoked when %s is clicked";

    //~ Inner Classes ................................................................................................................................

    /**
     * Create ElementAccessorCodeGenerator for attribute name and type.
     */

    private class ArrayAccessorCodeGenerator extends ElementAccessorCodeGenerator {
        private ArrayAccessorCodeGenerator(final Widget widget) {
            super(widget);
        }

        @Override void getterBody(@NotNull Method getter) {
            getter.return_(invoke(F_VAR, "getArray", getEnumField(name), classOf(innerTypeName)));
        }

        @Override void parameterBody(@NotNull Method m) {
            m.arg(name, generic(Iterable.class, isArrayParameterKey(widget.getType(), m) ? STRING : innerTypeName)).required(true);
            m.return_(invoke("", "putAll", getEnumField(name), name));
        }

        @Override void setterBody(@NotNull Method setter) {
            setter.arg(name, generic(Iterable.class, innerTypeName)).required(true);
            setter.statement(invoke(F_VAR, "setArray", getEnumField(name), name));
        }

        boolean isGetterNotNull() {
            return true;
        }

        private boolean isArrayParameterKey(@NotNull Type t, Method m) {
            return t.isArray() && ((ArrayType) t).getElementType().isEntity() && isDatabaseObjectKeyMethod(m);
        }
    }

    private class DateAccessorCodeGenerator extends ElementAccessorCodeGenerator {
        private DateAccessorCodeGenerator(final Widget widget) {
            super(widget);
        }

        @Override void getterBody(@NotNull final Method getter) {
            final String t = extractImport(typeName);
            if (isGetterNotNull()) getter.return_(invoke(t, FROM_MILLISECONDS, modelGet(LONG)));
            else getter.declare(Long.class, "val", modelGet(LONG)).return_("val == null ? null : " + t + ".fromMilliseconds(val)");
        }
    }

    private class DecimalAccessorCodeGenerator extends ElementAccessorCodeGenerator {
        private DecimalAccessorCodeGenerator(final Widget widget) {
            super(widget);
        }

        @Override void parameterBody(@NotNull Method m) {
            m.return_(invoke("", "put", getEnumField(name), setMethodAndGetScaled(m)));
        }

        @Override void setterBody(@NotNull Method setter) {
            setter.statement(invoke(F_VAR, "set", getEnumField(name), setMethodAndGetScaled(setter)));
        }

        private String setMethodAndGetScaled(@NotNull Method setter) {
            setter.arg(name, typeName).required(widget.isRequired());
            final DecimalType decimal       = (DecimalType) widget.getType();
            final Boolean     signedDecimal = widget.isSigned();
            return invokeStatic(Decimals.class,
                SCALE_AND_CHECK,
                quoted(name),
                name,
                signedDecimal.toString(),
                String.valueOf(decimal.getPrecision()),
                String.valueOf(decimal.getDecimals()));
        }
    }

    class ElementAccessorCodeGenerator {
        final String       innerTypeName;
        final String       name;
        final String       typeName;
        final Widget       widget;
        private final Type innerType;

        ElementAccessorCodeGenerator(final Widget widget) {
            this.widget   = widget;
            name          = widget.getName();
            typeName      = widget.getType().getImplementationClassName();
            innerType     = widget.getType().isArray() ? ((ArrayType) widget.getType()).getElementType() : widget.getType();
            innerTypeName = innerType.getImplementationClassName();
        }

        public Method suggestBoxSetter() {
            return generateSetter(s -> {
                s.arg(name, SUGGESTION_CLASS).required(true);
                s.statement(invoke(F_VAR, "set", getEnumField(name), name));
            });
        }

        final void addGetter() {
            getterBody(getter(name, typeName).required(isGetterNotNull()).withComments(format("Returns the value of the %s.", widget.toString())));
        }

        final void addOptions(ClassGenerator cg) {
            final String optionsMethod = widget.getWidgetType() == TREE_VIEW ? "optsTree" : "opts";

            // Default addOptions for everyone but Dynamics
            if (widget.getWidgetType() != DYNAMIC) addOptions(cg, optionsMethod, false);

            // Specific addOptions with comparator only for Trees
            if (widget.getWidgetType() == TREE_VIEW) addOptions(cg, optionsMethod, true);

            // addOptions with KeyMap for everyone but Mail_fields
            else if (widget.getWidgetType() != MAIL_FIELD) addKeyMapOptions(cg);
        }

        final Method addSetter() {
            return generateSetter(this::setterBody);
        }

        void getterBody(@NotNull final Method getter) {
            getter.return_(modelGet(typeName));
        }

        final String modelGet(String t) {
            return invoke(F_VAR, "get", getEnumField(name), classOf(t));
        }

        void parameterBody(@NotNull final Method m) {
            m.arg(name, typeName).required(widget.isRequired());
            m.return_(invoke("", "put", getEnumField(name), name));
        }

        void setterBody(@NotNull final Method setter) {
            setter.arg(name, typeName).required(widget.isRequired());
            setter.statement(invoke(F_VAR, "set", getEnumField(name), name));
        }

        boolean isDatabaseObjectKeyMethod(@NotNull Method m) {
            return m.getName().endsWith(capitalizeFirst(widget.getName()) + "Key");
        }

        boolean isGetterNotNull() {
            return widget.isRequired();
        }

        private void addKeyMapOptions(@NotNull final ClassGenerator cg) {
            final String optsType = cg.extractImport(KeyMap.class);

            final Method m = cg.setter(name + OPTIONS);
            m.withComments(format("Sets the options of the %s with the given KeyMap.", widget));
            m.arg(ITEMS, optsType).notNull();

            m.statement(invoke(F_VAR, "opts", getEnumField(widget.getName()), ITEMS));
        }

        private void addOptions(@NotNull final ClassGenerator cg, @NotNull final String optsMethod, boolean withComparator) {
            // If class is final (enum, entity, etc) do not add the wildcard extends
            final String type = innerType.isDatabaseObject() ? wildcardExtends(innerTypeName) : innerTypeName;

            final String optsType = cg.generic(Iterable.class, type);

            final Method m = cg.setter(name + OPTIONS);
            m.withComments(format("Sets the options of the %s.", widget));
            m.arg(ITEMS, optsType).notNull();

            if (withComparator) {
                m.arg(COMPARATOR, cg.generic(Comparator.class, wildcardSuper(innerTypeName))).notNull();
                m.statement(invoke(F_VAR, optsMethod, getEnumField(widget.getName()), ITEMS, COMPARATOR));
            }
            else m.statement(invoke(F_VAR, optsMethod, getEnumField(widget.getName()), ITEMS));
        }

        @NotNull private Method generateSetter(Consumer<Method> body) {
            final Method s = setter(name, getThisClass()).withComments(format(SETTER_COMMENT, widget.toString()));
            s.notNull();
            body.accept(s);
            s.return_(referenceThisType(THIS));
            return s;
        }

        private static final String OPTIONS = "Options";
    }  // end class ElementAccessorCodeGenerator

    private class EnumAccessorCodeGenerator extends ElementAccessorCodeGenerator {
        private EnumAccessorCodeGenerator(final Widget widget) {
            super(widget);
        }

        @Override void getterBody(@NotNull final Method getter) {
            final String t = extractImport(typeName);
            if (isGetterNotNull()) getter.return_(invoke(t, VALUE_OF, modelGet(STRING)));
            else getter.declare(String.class, "val", modelGet(STRING)).return_("val == null ? null : " + t + ".valueOf(val)");
        }
    }

    private class ReferenceAccessorCodeGenerator extends ElementAccessorCodeGenerator {
        private ReferenceAccessorCodeGenerator(final Widget widget) {
            super(widget);
        }

        @Override void getterBody(@NotNull final Method getter) {
            final Method keyGetter = getter(name + "Key", String.class).required(isGetterNotNull()).return_(modelGet(STRING));
            keyGetter.withComments(format("Returns the key value of the %s.", widget.toString()));

            if (isGetterNotNull())
                getter.return_(
                    invokeStatic(PREDEFINED_CLASS,
                        ENSURE_NOT_NULL,
                        invokeStatic(typeName, FIND_METHOD, invoke("", keyGetter.getName())),
                        quoted("'" + name + NOT_FOUND)));
            else
                getter.declare(String.class, KEY_ARG, invoke("", keyGetter.getName()))
                    .return_(KEY_ARG + " == null ? null : " + invokeStatic(typeName, FIND_METHOD, KEY_ARG));
        }

        @Override void parameterBody(@NotNull Method m) {
            m.arg(name, isDatabaseObjectKeyMethod(m) ? STRING : typeName).required(widget.isRequired());
            m.return_(invoke("", "put", getEnumField(name), name));
        }
    }

    private class StructTypeAccessorCodeGenerator extends ElementAccessorCodeGenerator {
        private StructTypeAccessorCodeGenerator(final Widget widget) {
            super(widget);
        }

        @Override void getterBody(@NotNull final Method getter) {
            getter.return_(invokeStatic(JsonMapping.class, FROM_JSON, modelGet(STRING), extractImport(classOf(typeName))));
        }

        @Override void parameterBody(@NotNull Method m) {
            m.arg(name, typeName).required(widget.isRequired());
            m.return_(invoke("", "put", getEnumField(name), invoke(name, TO_JSON)));
        }

        @Override void setterBody(@NotNull Method setter) {
            setter.arg(name, typeName).required(widget.isRequired());
            setter.statement(invoke(F_VAR, "set", getEnumField(name), invoke(name, TO_JSON)));
        }
    }
}  // end class WidgetContainerClassGenerator
