
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.shiro.AuthorizationUtils;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.security.PermissionException;
import tekgenesis.common.service.Parameters;
import tekgenesis.form.exprs.ServerExpressions;
import tekgenesis.form.filter.Filter;
import tekgenesis.form.filter.FormFilters;
import tekgenesis.metadata.form.InstanceReference;
import tekgenesis.metadata.form.exprs.ValidationExpressionsEvaluator;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.WidgetDefModel;
import tekgenesis.metadata.form.widget.*;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.type.permission.Permission;
import tekgenesis.workflow.WorkItemInstance;
import tekgenesis.workflow.WorkItemReferences;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.form.ModelPopulate.modelPopulate;
import static tekgenesis.form.extension.FormExtensionRegistry.getLocalizedForm;
import static tekgenesis.form.extension.FormExtensionRegistry.getLocalizedWidgetDef;

/**
 * Utility methods to deal with Forms.
 */
public class FormsImpl implements Forms {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private FormFieldRef currentWidget;

    @NotNull private final ReflectedFormInstance instance;

    //~ Constructors .................................................................................................................................

    FormsImpl(@NotNull final ReflectedFormInstance instance) {
        this.instance = instance;
        currentWidget = null;
    }

    //~ Methods ......................................................................................................................................

    /** Mark given form as update. Useful for 'singleton' instances. */
    @Override public void asUpdate() {
        getModel().asUpdate();
    }

    /** Check permission for current user and current form. */
    @Override public void checkPermission(@NotNull Permission permission)
        throws PermissionException
    {
        AuthorizationUtils.checkPermission(getModel().metadata(), permission);
    }

    /** Check permission for current user and selected form. */
    @Override public void checkPermission(@NotNull Class<? extends FormInstance<?>> form, @NotNull Permission permission)
        throws PermissionException
    {
        checkPermission(createQName(form.getPackage().getName(), form.getSimpleName()), permission);
    }

    /** Check permission for current user and the form identified by the {@link QName}. */
    @Override public void checkPermission(@NotNull QName name, @NotNull Permission permission)
        throws PermissionException
    {
        AuthorizationUtils.checkPermission(name, permission);
    }

    @Override public void filter(@NotNull Seq<? extends Filter> filters) {
        final Form           form    = instance.getForm();
        final MultipleWidget filter  = getMultipleFilter(form);
        final MultipleWidget results = (MultipleWidget) form.getElement(filter.getFiltering());
        final Seq<Filter>    fs      = cast(filters);
        FormFilters.populateFilters(instance.getInstance(), filter, results, fs);
    }

    /** Check permission for current user and the current form. */
    @Override public boolean hasPermission(@NotNull Permission permission) {
        return hasPermission(createQName(getModel().getFormFullName()), permission);
    }

    /** Check permission for current user and selected form. */
    @Override public boolean hasPermission(@NotNull Class<? extends FormInstance<?>> form, @NotNull Permission permission) {
        return hasPermission(createQName(form.getPackage().getName(), form.getSimpleName()), permission);
    }

    /** Check permission for current user and the form identified by the {@link QName}. */
    @Override public boolean hasPermission(@NotNull QName name, @NotNull Permission permission) {
        return AuthorizationUtils.hasPermission(name, permission);
    }

    /** Initialize a given Form class. */
    @NotNull @Override public <T extends FormInstance<?>> T initialize(@NotNull Class<T> clazz) {
        return initialize(clazz, null, null);
    }

    /** Initializes an instance of a given form, with a given primary key. */
    @NotNull @Override public <T extends FormInstance<?>> T initialize(@NotNull final Class<T> clazz, @Nullable final String pk) {
        return initialize(clazz, pk, null);
    }

    @NotNull @Override public <T extends FormInstance<?>> T initialize(@NotNull final Class<T> clazz, @Nullable final FormParameters<T> params) {
        return initialize(clazz, null, params);
    }

    @NotNull @Override public <T extends FormInstance<?>> T initialize(@NotNull final Class<T> clazz, @Nullable final String pk,
                                                                       @Nullable final FormParameters<T> params) {
        return init(clazz, pk, params).first();
    }

    @Override public <T extends EntityInstance<T, K>, K> void lockingTimestamp(@NotNull T updatedInstance) {
        ServerExpressions.updateModelTimestamp(getModel(), updatedInstance);
    }

    /** Mark given form as read-only. */
    @Override public void readOnly(boolean readOnly) {
        getModel().setReadOnly(readOnly);
    }

    /** Validate a given Form instance. */
    @NotNull @Override public Validations validate() {
        final ValidationsImpl validations = new ValidationsImpl();
        ValidationExpressionsEvaluator.validate(getModel(), validations);
        return validations;
    }

    @NotNull @Override public FormFieldRef getCurrentWidget() {
        return cast(ensureNotNull(currentWidget, "No current widget defined."));
    }

    @NotNull @Override public <T extends Enum<T>> T getCurrentWidgetEnum() {
        return cast(instance.getEnumField(instance.getWidget(getCurrentWidget())));
    }

    /** Returns true if form is on update. */
    @Override public boolean isUpdate() {
        return getModel().isUpdate();
    }

    /** Turns on/off a permission over the current form. */
    @Override public void setPermission(@NotNull String permission, boolean status) {
        getModel().setPermission(permission, status);
    }

    @Override public boolean isCurrentWidget(@NotNull FormFieldRef candidate) {
        return currentWidget != null && candidate.id().equals(currentWidget.id());
    }

    @NotNull @Override public Option<WorkItemInstance<?, ?, ?, ?, ?, ?>> getWorkItem() {
        return option(instance.getModel().getWorkItem()).map(WorkItemReferences::createWorkInstance);
    }

    /** Is form marked as read-only. */
    @Override public boolean isReadOnly() {
        return getModel().isReadOnly();
    }

    /** Set current widget. */
    void setCurrentWidget(@Nullable FormFieldRef currentWidget) {
        this.currentWidget = currentWidget;
    }

    private boolean isMultiple(final Widget w) {
        return w != null && w.getWidgetType().isMultiple() && !w.getFiltering().isEmpty();
    }

    private FormModel getModel() {
        return instance.getModel();
    }

    private MultipleWidget getMultipleFilter(Form form) {
        return (MultipleWidget) Colls.first(form.getDescendants(), this::isMultiple)
               .getOrFail("Form does not have a multiple widget with a valid 'filtering' option.");
    }

    //~ Methods ......................................................................................................................................

    /** Wraps the given model as a widget definition instance of the given class. */
    public static <W extends WidgetInstance<?>> void bindWidgetInstance(@NotNull ReflectedFormInstance root, @NotNull final String fqn,
                                                                        @NotNull W instance, @NotNull WidgetDefModel model, boolean compute) {
        final WidgetDef                  definition = getLocalizedWidgetDef(fqn);
        final ReflectedWidgetDefInstance result     = new ReflectedWidgetDefInstance(root, instance).setModel(model.init(definition));
        if (compute) ServerExpressions.bindAndCompute(result);
        else ServerExpressions.bindListener(result);
        modelPopulate(definition, model);
    }

    /** Initialize the user form java class. */
    public static BaseReflectedFormInstance createUserFormInstance(@NotNull final FormModel model) {
        return BaseReflectedFormInstance.createInstanceHandler(model);
    }

    /**
     * Initialize a form instance of a given class and returns the instance and its associated
     * model.
     */
    public static <T extends FormInstance<?>> T init(@NotNull final Class<T> clazz) {
        return init(clazz, null).first();
    }

    /** Wraps the given model as a form instance of the given class. */
    static <T extends FormInstance<?>> T formInstance(@NotNull Class<T> clazz, @NotNull FormModel model) {
        final Form                         form   = getLocalizedForm(clazz.getName(), model.getPk(), model.getParameters());
        final MeteredReflectedFormInstance result = new MeteredReflectedFormInstance(clazz, form);
        result.setModel(model.init(form));
        ServerExpressions.bindListener(result);
        return cast(result.getInstance());
    }

    /**
     * Initialize a form instance of a given class and returns the instance and its associated
     * model.
     */
    static <T extends FormInstance<?>> Tuple<T, FormModel> init(@NotNull final Class<T> clazz, @Nullable final String pk) {
        return init(clazz, pk, null);
    }

    static <T extends FormInstance<?>> Tuple<T, FormModel> init(Class<T> clazz, @Nullable String pk, @Nullable FormParameters<T> params) {
        final BaseReflectedFormInstance instance = initializeInstance(clazz, pk, null, params != null ? params.asMap() : null);
        final T                         object   = cast(instance.getInstance());
        return tuple(object, instance.getModel());
    }

    /** Initializes an instance of a given form. */
    @NotNull static <T extends FormInstance<?>> BaseReflectedFormInstance initializeInstance(@NotNull final Class<T> clazz) {
        return initializeInstance(clazz, null);
    }

    /** Initializes an instance of a given form, with a given primary key. */
    @NotNull static <T extends FormInstance<?>> BaseReflectedFormInstance initializeInstance(@NotNull final Class<T> clazz,
                                                                                             @Nullable final String  pk) {
        return initializeInstance(clazz, pk, null);
    }

    /** Initializes an instance of a given form, with a given primary key. */
    @NotNull static <T extends FormInstance<?>> BaseReflectedFormInstance initializeInstance(@NotNull final Class<T> clazz, @Nullable final String pk,
                                                                                             @Nullable final InstanceReference reference) {
        return initializeInstance(clazz, pk, reference, null);
    }

    /** Initializes an instance of a given form, with a given primary key and map of parameters. */
    @NotNull static <T extends FormInstance<?>> BaseReflectedFormInstance initializeInstance(@NotNull final Class<T> clazz, @Nullable final String pk,
                                                                                             @Nullable final InstanceReference reference,
                                                                                             @Nullable final MultiMap<String, String> params) {
        final Form form = getLocalizedForm(clazz.getName(), pk, params == null ? null : Parameters.mapToQueryString(params));
        return ReflectedFormInstance.createFormInstance(form, pk, params, reference);
    }
}  // end class FormsImpl
