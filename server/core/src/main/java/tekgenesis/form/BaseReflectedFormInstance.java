
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

import tekgenesis.metadata.form.InstanceReference;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.metadata.form.widget.Widget;

/**
 * Base form reflection classes. Instance to manipulate dynamic/user form classes.
 */
public abstract class BaseReflectedFormInstance extends FormInstance<Object> implements UiModelInstanceHandler {

    //~ Methods ......................................................................................................................................

    /** @return  Find result */
    public abstract Object find();

    /** Get Form title. */
    public abstract String title();

    /** Set current widget. */
    public abstract void setCurrentWidget(@NotNull FormFieldRef currentWidget);

    /** Returns form field ref of the given field. */
    public abstract FormFieldRef getEnumFieldRef(@NotNull Widget field);

    /** @return  Form extended or original Instance */
    public FormInstance<?> getExtendedInstance() {
        return getInstance();
    }

    /** @return  Form */
    public abstract Form getForm();

    /** Set form field value. */
    public abstract void setFormField(@NotNull final String field, @NotNull final Object value);

    /** @return  Form Instance */
    public abstract FormInstance<?> getInstance();

    /** Get Form model. */
    public abstract FormModel getModel();

    /** Set work item on model. */
    public void setWorkItemReference(@NotNull final InstanceReference reference) {
        getModel().setWorkItem(reference);
    }

    /** Call onLoad.* */
    abstract void onLoad();

    //~ Methods ......................................................................................................................................

    /**
     * Creator.
     *
     * @param   formModel  FormModel
     *
     * @return  ReflectedFormInstanceHandler
     */
    public static BaseReflectedFormInstance createInstanceHandler(@NotNull FormModel formModel) {
        if (formModel.metadata().isDynamic()) return DynamicReflectedFormInstance.create(formModel);
        else return ReflectedFormInstance.create(formModel);
    }

    /**
     * Creator.
     *
     * @param   form  FormInstance
     *
     * @return  ReflectedFormInstanceHandler
     */
    public static BaseReflectedFormInstance wrapInstanceHandler(@NotNull FormInstance<?> form) {
        if (form.isDynamic()) {
            final BaseReflectedFormInstance wrap = DynamicReflectedFormInstance.wrap(form);
            wrap.onLoad();
            return wrap;
        }
        else return new MeteredReflectedFormInstance(form);
    }
}
