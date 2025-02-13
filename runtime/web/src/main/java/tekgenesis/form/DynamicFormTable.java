
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import tekgenesis.common.core.Option;

/**
 * Table from a Dynamic Form.
 */
public interface DynamicFormTable {

    //~ Methods ......................................................................................................................................

    /**
     * Add new row.
     *
     * @return  DynamicFormWidgetAccessor
     */
    DynamicFormWidgetAccessor add();

    /** Clear table model. */
    void clear();

    /**
     * Remove a row.
     *
     * @param  row  row position
     */
    void removeRow(int row);

    /** @return  Size of the table */
    int size();

    /** @return  An option of DynamicFormWidgetAccessor */
    Option<DynamicFormWidgetAccessor> getCurrentRow();
}
