
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

import tekgenesis.common.collections.Seq;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.model.KeyMap;

/**
 * Field Accessor for dynamic Forms.
 */
public final class DynamicFormAccesorImpl extends DynamicFormWidgetAccessorImpl<FormModel> implements DynamicFormAccessor {

    //~ Constructors .................................................................................................................................

    private DynamicFormAccesorImpl(@NotNull Form form, @NotNull FormModel model) {
        super(form, model);
    }

    //~ Methods ......................................................................................................................................

    /**
     * Set Combo options.
     *
     * @param  fieldName  fieldName
     * @param  values     value
     */
    public void setOptions(@NotNull String fieldName, Seq<String> values) {
        getModel().setOptions(getForm().getElement(fieldName), KeyMap.fromValues(values));
    }

    /**
     * @param   tableName  widget name
     *
     * @return  DynamicFormTable
     */
    public DynamicFormTableImpl getTable(@NotNull String tableName) {
        final MultipleWidget multipleWidget = (MultipleWidget) getForm().getElement(tableName);
        final MultipleModel  modelMultiple  = getModel().getMultiple(multipleWidget);
        return new DynamicFormTableImpl(modelMultiple, getModel());
    }

    //~ Methods ......................................................................................................................................

    /**
     * Creator.
     *
     * @param   form   Form
     * @param   model  FormModel
     *
     * @return  DynamicForm
     */
    public static DynamicFormAccessor create(@NotNull Form form, @NotNull FormModel model) {
        return new DynamicFormAccesorImpl(form, model);
    }
}  // end class DynamicFormAccesorImpl
