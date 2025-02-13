
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

/**
 * Field Accessor for dynamic Forms.
 */
public interface DynamicFormAccessor extends DynamicFormWidgetAccessor {

    //~ Methods ......................................................................................................................................

    /**
     * Set Combo options.
     *
     * @param  fieldName  fieldName
     * @param  values     value
     */
    void setOptions(@NotNull String fieldName, Seq<String> values);

    /**
     * @param   tableName  widget name
     *
     * @return  DynamicFormTable
     */
    DynamicFormTable getTable(@NotNull String tableName);
}
