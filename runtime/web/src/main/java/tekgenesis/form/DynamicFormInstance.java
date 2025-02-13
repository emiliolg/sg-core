
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

import tekgenesis.common.core.QName;
import tekgenesis.common.util.Reflection;
import tekgenesis.field.FieldOption;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.metadata.common.ModelLinkerImpl;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormBuilder;
import tekgenesis.metadata.form.widget.FormBuilderPredefined;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.Predefined.unreachable;

/**
 * DynamicFormInstance.
 */
public class DynamicFormInstance extends FormInstance<Void> {

    //~ Instance Fields ..............................................................................................................................

    private final DynamicInstance  instance;
    @Nullable private final String param;

    //~ Constructors .................................................................................................................................

    /**
     * @param  clazz  Class
     * @param  param  dymamic form parameter
     */
    public DynamicFormInstance(Class<? extends DynamicInstance> clazz, @Nullable String param) {
        this.param = param;
        instance   = Reflection.construct(clazz);
    }

    /**
     * @param  fqn    Form FQN
     * @param  param  dymamic form parameter
     */
    public DynamicFormInstance(@NotNull String fqn, @Nullable String param) {
        this(Reflection.findClass(fqn, () -> new EntityInstanceNotFoundException(fqn)), param);
    }

    //~ Methods ......................................................................................................................................

    /**
     * Build Form.
     *
     * @param   builder     FormBuilder
     * @param   repository  ModelRepository
     *
     * @return  Form
     */
    public Form build(FormBuilder builder, ModelRepository repository)
        throws BuilderException
    {
        instance.build(builder, param);
        builder.with(FieldOption.DYNAMIC_FORM);
        final Form result = builder.build();
        ModelLinkerImpl.linkForm(repository, result);
        return result;
    }

    @NotNull @Override public Action cancel() {
        throw unreachable();
    }
    /** Invoked when canceling a form instance. */
    public Action cancel(@NotNull DynamicFormAccessor dynamicForm) {
        return instance.cancel(dynamicForm, param);
    }

    @NotNull @Override public Action create() {
        throw unreachable();
    }

    /** Invoked when creating a form instance. */
    public Action create(@NotNull DynamicFormAccessor dynamicForm) {
        return instance.create(dynamicForm, param);
    }

    @NotNull @Override public Action delete() {
        throw unreachable();
    }
    /** Invoked when deleting a form instance. */
    public Action delete(@NotNull DynamicFormAccessor dynamicForm) {
        return instance.delete(dynamicForm, param);
    }

    @NotNull @Override public Action deprecate(boolean status) {
        throw unreachable();
    }

    /** Invoked when on_click is executed. */
    @NotNull public Action handleClick(@NotNull DynamicFormAccessor dynamicForm, @NotNull String methodName) {
        return instance.handleClick(dynamicForm, methodName);
    }

    @NotNull @Override public String keyAsString() {
        return getParam();
    }

    /**
     * OnLoad form.
     *
     * @param  dynamicForm  DynamicForm
     */
    public void onLoad(@NotNull DynamicFormAccessor dynamicForm) {
        instance.populate(dynamicForm, getParam());
    }

    @NotNull @Override public Void populate() {
        throw unreachable();
    }

    /** Invoked when populating a form instance. */
    public void populate(@NotNull DynamicFormAccessor dynamicForm) {
        instance.populate(dynamicForm, getParam());
    }

    /**
     * Initialize FormBuilder.
     *
     * @param   repository  ModelRepository
     *
     * @return  FormBuilder
     */
    public FormBuilder resolve(@NotNull ModelRepository repository) {
        final String p = param == null ? "" : ":" + param;

        final QName       fqn     = QName.createQName(instance.getClass());
        final FormBuilder builder = FormBuilderPredefined.form("", fqn.getQualification(), fqn.getName() + p);
        builder.withRepository(repository);
        return builder;
    }

    @NotNull @Override public Action update() {
        throw unreachable();
    }

    /** Invoked when updating a form instance. */
    public Action update(@NotNull DynamicFormAccessor dynamicForm) {
        return instance.update(dynamicForm, getParam());
    }

    /** Set Actions. */
    public void setActions(@NotNull Actions actions) {
        instance.actions = actions;
    }

    /** Set Actions. */
    public boolean isDynamic() {
        return true;
    }

    /**
     * Set context.
     *
     * @param  context  ApplicationContext
     */
    public void setContext(@NotNull ApplicationContext context) {
        instance.context = context;
    }

    @Override public void setPrimaryKey(@NotNull String pk) {
        throw unreachable();
    }

    /** Set the Form primary key. */
    public void setPrimaryKey(@NotNull DynamicFormAccessor dynamicForm, @NotNull String pk) {
        instance.populate(dynamicForm, pk);
    }

    private String getParam() {
        return notNull(param, "");
    }
}  // end class DynamicFormInstance
