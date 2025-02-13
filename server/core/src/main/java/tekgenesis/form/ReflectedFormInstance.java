
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.lang.reflect.Modifier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.shiro.AuthorizationUtils;
import tekgenesis.check.CheckMsg;
import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.core.Lazy;
import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.service.Parameters;
import tekgenesis.common.util.Reflection;
import tekgenesis.common.util.Reflection.Instance;
import tekgenesis.field.ModelField;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.form.exprs.ServerExpressions;
import tekgenesis.form.extension.FormExtension;
import tekgenesis.form.extension.FormExtensionRegistry;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.metadata.form.InstanceReference;
import tekgenesis.metadata.form.LocalWidget;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.*;
import tekgenesis.model.KeyMap;
import tekgenesis.persistence.DeprecableInstance;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.type.permission.PredefinedPermission;

import static tekgenesis.authorization.shiro.AuthorizationUtils.checkPermission;
import static tekgenesis.codegen.CodeGeneratorConstants.DEFINE_METHOD;
import static tekgenesis.codegen.common.MMCodeGenConstants.F_VAR;
import static tekgenesis.codegen.common.MMCodeGenConstants.LISTENERS_VAR;
import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Constants.CANNOT_CREATE_CLASS;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.common.util.Reflection.construct;
import static tekgenesis.common.util.Reflection.invokeDeclared;
import static tekgenesis.form.FormsImpl.createUserFormInstance;
import static tekgenesis.form.ModelPopulate.createFormModel;
import static tekgenesis.form.ModelPopulate.modelPopulate;
import static tekgenesis.form.etl.FieldAccessors.deserializeWidget;
import static tekgenesis.form.exprs.ServerExpressions.updateModelTimestamp;
import static tekgenesis.form.extension.FormListenerType.*;
import static tekgenesis.metadata.form.model.FormConstants.FIND_METHOD_NAME;
import static tekgenesis.metadata.form.widget.WidgetType.TABLE;
import static tekgenesis.metadata.form.widget.WidgetType.TOGGLE_BUTTON;
import static tekgenesis.type.permission.PredefinedPermission.*;

/**
 * Wrapped Reflection.Instance to manipulate user form classes.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class ReflectedFormInstance extends BaseReflectedFormInstance {

    //~ Instance Fields ..............................................................................................................................

    private final Lazy<UiModelAccessorImpl> accessor;
    private final Form                      form;
    private final Instance                  instance;

    //~ Constructors .................................................................................................................................

    /** Create {@link ReflectedFormInstance} wrapping existing {@link FormInstance}. */
    ReflectedFormInstance(@NotNull final FormInstance<?> object) {
        instance = Instance.wrap(object);
        accessor = instantiate();
        form     = getModel().metadata();
    }

    /** Create new {@link ReflectedFormInstance}. */
    ReflectedFormInstance(@NotNull final Class<? extends FormInstance<?>> clazz, @NotNull final Form form) {
        instance = Instance.wrap(construct(clazz));
        setInstanceFields();
        this.form = form;
        accessor  = instantiate();
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action cancel() {
        callListeners(BEFORE_CANCEL);
        onCancel();
        final Action result = getExtendedInstance().cancel();
        callListeners(AFTER_CANCEL);
        return result;
    }

    @NotNull @Override public Action create() {
        check(CREATE);
        callListeners(BEFORE_CREATE);
        callListeners(BEFORE_PERSIST);
        final Action result = getExtendedInstance().create();
        callListeners(AFTER_CREATE);
        callListeners(AFTER_PERSIST);
        return result;
    }

    @NotNull @Override public Action delete() {
        check(DELETE);
        callListeners(BEFORE_DELETE);
        final Action delete = getExtendedInstance().delete();
        callListeners(AFTER_DELETE);
        return delete;
    }

    /** Effectively changes the deprecation status of an instance. */
    @NotNull public Action deprecate(boolean deprecate) {
        check(HANDLE_DEPRECATED);
        callListeners(BEFORE_DEPRECATE);
        final Action action = getExtendedInstance().deprecate(deprecate);
        if (!action.isError()) getModel().setDeprecated(deprecate);
        callListeners(AFTER_DEPRECATE);
        return action;
    }

    /** Invoke find method on instance if exists. Returns the form associated main Entity. */
    @Nullable public Object find() {
        return instance.invoke(FIND_METHOD_NAME);
    }

    public <W extends WidgetInstance<?>> W invokeDefine(String name) {
        return ensureNotNull(invokeDeclared(instance.getInstance(), DEFINE_METHOD + capitalizeFirst(name)));
    }

    @NotNull @Override public String keyAsString() {
        return getInstance().keyAsString();
    }

    @NotNull @Override public Object populate() {
        final Object populate = getExtendedInstance().populate();
        if (populate instanceof DeprecableInstance) populateDeprecated(getModel(), (DeprecableInstance<?, ?>) populate);
        return populate;
    }

    @Override public ReflectedFormInstance root() {
        return this;
    }

    @Override public String title() {
        return getUiModelAccessor().title();
    }

    @NotNull @Override public Action update() {
        check(UPDATE);
        callListeners(BEFORE_UPDATE);
        callListeners(BEFORE_PERSIST);
        final Action result = getExtendedInstance().update();
        callListeners(AFTER_UPDATE);
        callListeners(AFTER_PERSIST);
        return result;
    }

    @NotNull @Override public Instance wrapped() {
        return instance;
    }

    @Override public void setCurrentWidget(Widget field) {
        setCurrentWidget(getEnumFieldRef(field));
    }

    @Override public void setCurrentWidget(@NotNull FormFieldRef field) {
        ((FormsImpl) getInstance().forms).setCurrentWidget(field);
    }

    /** Returns form field ref of the given field. */
    public FormFieldRef getEnumFieldRef(@NotNull Widget field) {
        return !getForm().isExtended() || field.isOriginalField() ? getEnumField(field)
                                                                  : new FormFieldRef.Dynamic(field.getName(), field.getOrdinal());
    }

    /** Returns the FormBase created in this FormInstance. */
    @Override public FormInstance<?> getExtendedInstance() {
        final FormInstance<?>             i         = getInstance();
        final Option<FormExtension<?, ?>> extension = FormExtensionRegistry.getInstance().getExtension(form, i, getUiModelAccessor());
        if (extension.isEmpty()) return i;
        setInstanceFields(extension.get());
        return extension.get();
    }

    /** Get Form meta model. */
    public Form getForm() {
        return form;
    }

    /** Set given ModelField on Form. */
    public void setFormField(@NotNull final String field, @NotNull final Object value) {
        if (!form.containsElement(field)) throw new IllegalStateException("Form field '" + field + "' not found on Form '" + form.getName() + "'");

        final Widget widget = form.getElement(field);

        final Option<MultipleWidget> tableWidget = widget.getMultiple();

        if (tableWidget.isPresent()) LOGGER.warning("Missing row context to sync field " + field);
        // final TableWidget   table    = tableWidget.get();
        // final TableInstance instance = getTableInstance(table);
        // instance.setField(widget, value);
        else getUiModelAccessor().set(widget, value);
    }

    /** Returns underlying {@link FormInstance instance}. */
    public FormInstance<?> getInstance() {
        return cast(instance.getInstance());
    }

    /** Get underlying {@link FormModel model}. */
    public FormModel getModel() {
        return (FormModel) getUiModelAccessor().container();
    }

    @Override public void setPrimaryKey(@NotNull String pk) {
        setInstancePrimaryKey(this, pk);
    }

    @Override public void setSlot(@NotNull Widget field, @Nullable Object value) {
        getUiModelAccessor().set(field, value);
    }

    /** Invoke on_loads methods if any. */
    void onLoad() {
        callListeners(BEFORE_LOAD);

        // todo remove on_load field in favor of default FormInstance.load() method
        final String onLoad = form.getOnLoadMethodName();
        if (isNotEmpty(onLoad)) instance.invoke(onLoad);
        else getExtendedInstance().load();

        for (final Widget widget : form.getUiModelElements(TABLE)) {
            final String tableOnLoad = widget.getOnLoadMethodName();
            if (isNotEmpty(tableOnLoad)) instance.invoke(tableOnLoad);
        }

        callListeners(AFTER_LOAD);
    }

    /** Test. Set the given multiple current slot. */
    void setCurrent(FormFieldRef field, int slot) {
        final Widget widget = getWidget(field);
        if (widget.getWidgetType().isMultiple()) {
            final MultipleModel multiple = getModel().getMultiple((MultipleWidget) widget);
            if (multiple.size() >= slot) getModel().updateRow(new LocalWidget(widget.getName(), slot));
            else throw new IndexOutOfBoundsException("Attempting to set current index #" + slot + " having #" + multiple.size() + " slots.");
        }
        else throw new IllegalStateException("Attempting to set current to a not multiple widget.");
    }

    /** Test. Get the associated message text for a given field. */
    @NotNull String getFieldMessage(@NotNull final FormFieldRef field) {
        final Widget widget = getWidget(field);
        return getFieldMessage(getModel(), widget);
    }

    /** Test. Get the associated message text for a given field. */
    @NotNull String getFieldMessage(@NotNull final FormFieldRef field, int row) {
        final Widget widget = getWidget(field);
        return getFieldMessage(getRowModel(row, widget), widget);
    }

    /** Test. Get the options for a given field. */
    @NotNull KeyMap getFieldOptions(@NotNull final FormFieldRef field) {
        final Widget widget = getWidget(field);
        return getModel().getOptions(widget);
    }

    /** Test. Get the options for a given field. */
    @NotNull KeyMap getFieldOptions(@NotNull final FormFieldRef field, int row) {
        final Widget widget = getWidget(field);
        return getRowModel(row, widget).getOptions(widget);
    }

    /** Set Form model. */
    ReflectedFormInstance setModel(@NotNull final FormModel model) {
        instance.setPrivateField(F_VAR, new UiModelAccessorImpl(this, this, model));
        return this;
    }

    RowModel getRowModel(int row, Widget widget) {
        final MultipleWidget multiple = widget.getMultiple().getOrFail("Expecting a field inside a multiple");
        return getModel().getMultiple(multiple).getRow(row);
    }

    Widget getWidget(FormFieldRef field) {
        return getForm().getWidgetByOrdinal(field.ordinal());
    }

    private void callListeners(final FormListenerType type) {
        final Class<FormInstance<?>>           clazz     = cast(getInstance().getClass());
        final FormListenerMap<FormInstance<?>> listeners = Reflection.getPrivateStaticField(clazz, LISTENERS_VAR);
        listeners.apply(type, getInstance());
    }

    private void check(@NotNull final PredefinedPermission permission) {
        checkPermission(getModel().metadata(), permission);
    }

    private Lazy<UiModelAccessorImpl> instantiate() {
        return new Lazy<>(() -> instance.getPrivateField(F_VAR));
    }

    /** Invoke on_cancel method if any. */
    private void onCancel() {
        final String onCancel = form.getOnCancelMethodName();
        if (isNotEmpty(onCancel)) instance.invoke(onCancel);
    }

    private void populateDeprecated(FormModel model, DeprecableInstance<?, ?> deprecableInstance) {
        final boolean deprecated = deprecableInstance.isDeprecated();
        if (deprecated) {
            check(HANDLE_DEPRECATED);
            model.setDeprecated(true);
            for (final Widget button : getForm().getUiModelElements(TOGGLE_BUTTON)) {
                if (button.getToggleButtonType() == ToggleButtonType.DEPRECATE) model.set(button, true);
            }
        }
    }

    @NotNull private String getFieldMessage(Model model, Widget widget) {
        final CheckMsg msg = model.getFieldMsg(widget);
        return msg != null ? msg.getText() : "";
    }

    /** Set instance helper object fields. */
    private void setInstanceFields() {
        setInstanceFields(getInstance());
    }

    @SuppressWarnings("ConstantConditions")
    private void setInstanceFields(final FormInstance<?> formInstance) {
        if (formInstance.actions == null) {
            formInstance.actions = ActionsImpl.getInstance();
            formInstance.context = new ApplicationContextImpl();
            formInstance.forms   = new FormsImpl(this);
        }
    }

    @NotNull private UiModelAccessorImpl getUiModelAccessor() {
        return accessor.get();
    }

    //~ Methods ......................................................................................................................................

    /** Create a FormInstance given the Form metadata and model. */
    public static ReflectedFormInstance create(@NotNull final FormModel model) {
        final Form                         form                  = model.metadata();
        final MeteredReflectedFormInstance reflectedFormInstance = new MeteredReflectedFormInstance(getClass(form), form);
        return reflectedFormInstance.setModel(model);
    }

    /**
     * Instantiate, bind, compute, set primary key, load, and populate a FormInstance with set
     * permissions.
     */
    public static BaseReflectedFormInstance createFormInstance(@NotNull final Form form, @Nullable final String pk) {
        return createFormInstance(form, pk, null);
    }

    /**
     * Instantiate, bind, compute, parameterize, set primary key, load, and populate a FormInstance
     * with set permissions.
     */
    public static BaseReflectedFormInstance createFormInstance(@NotNull final Form form, @Nullable final String pk,
                                                               @Nullable final MultiMap<String, String> parameters) {
        return createFormInstance(form, pk, parameters, null);
    }

    /**
     * Instantiate, bind, compute, parameterize, set primary key, set instance reference, load, and
     * populate a FormInstance with set permissions.
     */
    public static BaseReflectedFormInstance createFormInstance(@NotNull final Form form, @Nullable final String pk,
                                                               @Nullable final MultiMap<String, String> parameters,
                                                               @Nullable final InstanceReference reference) {
        return initialize(parameterize(instantiate(form, pk), parameters), pk, reference);
    }

    /** Create a {@link ReflectedFormInstance} wrapping the given {@link FormInstance}. */
    public static ReflectedFormInstance wrap(@NotNull final FormInstance<?> instance) {
        return new ReflectedFormInstance(instance);
    }

    /** Return given Form class. */
    @NotNull public static Class<FormInstance<?>> getClass(@NotNull final Form form) {
        return getClass(form.getImplementationClassFullName());
    }

    /** Return given Form class. */
    @NotNull public static Class<FormInstance<?>> getClass(@NotNull final String formClass) {
        final Class<FormInstance<?>> clazz;
        try {
            // Initialize the user form java class.
            clazz = cast(Class.forName(formClass, true, Thread.currentThread().getContextClassLoader()));
        }
        catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(CANNOT_CREATE_CLASS + formClass, e);
        }

        return clazz;
    }

    static void checkPublicNotStatic(Class<?> clazz) {
        checkPublic(clazz);
        if (Modifier.isStatic(clazz.getModifiers())) throw new IllegalArgumentException(clazz.getName() + " should not be static");
    }

    static void checkPublicStatic(Class<?> clazz) {
        checkPublic(clazz);
        if (!Modifier.isStatic(clazz.getModifiers())) throw new IllegalArgumentException(clazz.getName() + " should be static");
    }

    private static void checkPublic(Class<?> clazz) {
        if (!Modifier.isPublic(clazz.getModifiers())) throw new IllegalArgumentException(clazz.getName() + " should be public");
    }

    /** Set primary key, load, and populate given FormInstance. */
    private static <T extends EntityInstance<T, K>, K> BaseReflectedFormInstance initialize(@NotNull final BaseReflectedFormInstance formInstance,
                                                                                            @Nullable final String                   pk,
                                                                                            @Nullable final InstanceReference        reference) {
        if (reference != null) formInstance.setWorkItemReference(reference);
        if (isNotEmpty(pk)) setInstancePrimaryKey(formInstance, pk);
        formInstance.onLoad();
        if (isNotEmpty(pk)) {
            final Object    instance = formInstance.populate();
            final FormModel model    = formInstance.getModel();
            updateModelTimestamp(model, instance);
            if (instance instanceof EntityInstance) {
                final T entityInstance = cast(instance);
                if (entityInstance.metadata().getUpdateTimeField() != null) model.setDescribeBy(entityInstance.describe().mkString(" "));
            }
        }
        return formInstance;
    }

    /** Instantiate, bind, and compute a FormInstance with set permissions. */
    private static BaseReflectedFormInstance instantiate(@NotNull final Form form, @Nullable final String pk) {
        // This doesn't check against the model since there is still no model.
        AuthorizationUtils.checkPermission(form, READ);

        final FormModel model = createFormModel(form);
        if (isNotEmpty(pk)) model.asUpdate();
        final BaseReflectedFormInstance instance = createUserFormInstance(model);
        ServerExpressions.bindAndCompute(instance);
        modelPopulate(form, model);
        return instance;
    }

    /** Parametrize given form instance with parameter map. */
    private static BaseReflectedFormInstance parameterize(@NotNull final BaseReflectedFormInstance instance,
                                                          @Nullable MultiMap<String, String>       parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            final Form f = instance.getForm();
            for (final ModelField parameter : f.getParameters()) {
                final String name = parameter.getName();
                if (parameters.containsKey(name)) deserializeWidget(f.getElement(name), instance, parameters.get(name));
            }
            instance.getModel().setParameters(Parameters.mapToQueryString(parameters));
        }
        return instance;
    }

    private static void setInstancePrimaryKey(@NotNull BaseReflectedFormInstance instance, @NotNull String pk) {
        try {
            instance.getModel().asUpdate();  // Mark as update
            instance.getModel().setPrimaryKey(pk);
            instance.getInstance().setPrimaryKey(pk);
        }
        catch (final Exception ignore) {
            throw new EntityInstanceNotFoundException(instance.title(), pk);
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger LOGGER = Logger.getLogger(ReflectedFormInstance.class);
}  // end class ReflectedFormInstance
