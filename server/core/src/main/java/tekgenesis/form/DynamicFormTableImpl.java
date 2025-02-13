
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

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.LocalWidget;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;

import static tekgenesis.common.core.Option.ofNullable;

/**
 * Table from a Dynamic Form.
 */
public class DynamicFormTableImpl implements DynamicFormTable {

    //~ Instance Fields ..............................................................................................................................

    private final FormModel formModel;

    private final MultipleModel model;

    //~ Constructors .................................................................................................................................

    /**
     * Default constructor.
     *
     * @param  model      MultipleModel
     * @param  formModel  FormModel
     */
    public DynamicFormTableImpl(@NotNull MultipleModel model, FormModel formModel) {
        this.model     = model;
        this.formModel = formModel;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Add new row.
     *
     * @return  DynamicFormTableRow
     */
    public DynamicFormWidgetAccessor add() {
        final RowModel rowModel = model.addRow();
        return new DynamicFormWidgetAccessorImpl<>(formModel.metadata(), rowModel);
    }

    /** Clear table model. */
    public void clear() {
        model.clear();
    }

    /**
     * Remove a row.
     *
     * @param  row  row position
     */
    public void removeRow(int row) {
        model.removeRow(row);
    }

    /** @return  Size of the table */
    public int size() {
        return model.size();
    }

    /** @return  An option of DynamicFormTableRow */
    public Option<DynamicFormWidgetAccessor> getCurrentRow() {
        final Option<Integer> row = ofNullable(formModel.getCurrentItem()).flatMap(LocalWidget::getItem);
        // Not checking if current item matches multiple :S
        return row.map(r -> new DynamicFormWidgetAccessorImpl<>(formModel.metadata(), model.getRow(r)));
    }
}  // end class DynamicFormTableImpl
