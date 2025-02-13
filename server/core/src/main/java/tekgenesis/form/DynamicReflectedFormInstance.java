
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

import tekgenesis.common.core.Tuple;
import tekgenesis.common.util.Reflection;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.type.permission.PredefinedPermission;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.form.FormUtils.buildDynamicFormMetaModel;
import static tekgenesis.form.ModelPopulate.createFormModel;
import static tekgenesis.form.ModelPopulate.modelPopulate;

/**
 * Wrapped Reflection.Instance to manipulate dynamic form classes.
 */
class DynamicReflectedFormInstance extends BaseReflectedFormInstance {

    //~ Instance Fields ..............................................................................................................................

    private final DynamicFormAccessor dynamicForm;

    private final FormModel           formModel;
    private final DynamicFormInstance instance;

    //~ Constructors .................................................................................................................................

    private DynamicReflectedFormInstance(FormModel formModel) {
        this.formModel = formModel;

        final Tuple<String, String> formInfo = FormUtils.getFormInfo(formModel.getFormFullName());

        instance = new DynamicFormInstance(formInfo.first(), formInfo.second());
        instance.setActions(ActionsImpl.getInstance());
        instance.setContext(ApplicationContextImpl.getInstance());
        final Form form = buildDynamicFormMetaModel(instance);
        setPermissions(this.formModel);
        dynamicForm = DynamicFormAccesorImpl.create(form, formModel);
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private DynamicReflectedFormInstance(@NotNull FormInstance<?> formInstance) {
        final Object className = Reflection.Instance.wrap(formInstance).getPrivateField("instance");
        final String param     = Reflection.Instance.wrap(formInstance).getPrivateField("param");

        instance = new DynamicFormInstance(className.getClass().getCanonicalName(), param);
        instance.setActions(ActionsImpl.getInstance());
        instance.setContext(ApplicationContextImpl.getInstance());
        final Form form = buildDynamicFormMetaModel(instance);
        formModel = createFormModel(form);
        modelPopulate(form, formModel);
        setPermissions(formModel);
        dynamicForm = DynamicFormAccesorImpl.create(form, formModel);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action cancel() {
        return instance.cancel(dynamicForm);
    }

    @NotNull @Override public Action create() {
        return instance.create(dynamicForm);
    }

    @NotNull @Override public Action delete() {
        return instance.delete(dynamicForm);
    }

    @NotNull @Override public Action deprecate(boolean status) {
        throw unreachable();
    }

    @Override public Object find() {
        throw unreachable();
    }

    @Override public <W extends WidgetInstance<?>> W invokeDefine(String name) {
        throw unreachable();
    }

    @Override public <T> T invokeUserMethod(@NotNull final String method, @NotNull final Object... args) {
        return cast(instance.handleClick(dynamicForm, method));
    }

    @NotNull @Override public String keyAsString() {
        throw unreachable();
    }

    @NotNull @Override public Object populate() {
        instance.populate(dynamicForm);
        return "";
    }

    @Override public ReflectedFormInstance root() {
        throw unreachable();
    }

    @Override public String title() {
        return formModel.metadata().getLabel();
    }

    @NotNull @Override public Action update() {
        return instance.update(dynamicForm);
    }

    @NotNull @Override public Reflection.Instance wrapped() {
        throw unreachable();
    }

    @Override public void setCurrentWidget(Widget widget) {}

    @Override public void setCurrentWidget(@NotNull FormFieldRef currentWidget) {}

    @Nullable @Override public FormFieldRef getEnumFieldRef(@NotNull final Widget field) {
        return null;
    }

    @Override public void setField(@NotNull Widget field, @Nullable Object value) {
        throw unreachable();
    }

    @Override public Form getForm() {
        return formModel.metadata();
    }

    @Override public void setFormField(@NotNull String field, @NotNull Object value) {
        throw unreachable();
    }

    public FormInstance<?> getInstance() {
        return instance;
    }

    @Override public FormModel getModel() {
        return formModel;
    }

    @Override public void setPrimaryKey(@NotNull String pk) {
        instance.setPrimaryKey(dynamicForm, pk);
    }

    @Override public void setSlot(@NotNull Widget field, @Nullable Object value) {
        throw unreachable();
    }

    @Override void onLoad() {
        instance.onLoad(dynamicForm);
    }

    private void setPermissions(@NotNull FormModel model) {
        for (final PredefinedPermission predefined : PredefinedPermission.values())
            model.setPermission(predefined.getName(), true);
    }

    //~ Methods ......................................................................................................................................

    /** Create a FormInstance given the Form metadata and model. */
    public static BaseReflectedFormInstance create(@NotNull final FormModel model) {
        return new DynamicReflectedFormInstance(model);
    }

    /** Create a FormInstance wrapping the given FormBase. */
    public static BaseReflectedFormInstance wrap(@NotNull final FormInstance<?> instance) {
        return new DynamicReflectedFormInstance(instance);
    }
}  // end class ReflectedDynamicReflectedFormInstance
