
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Resource;

/**
 * Accessor for Dynamic Forms.
 */
public interface DynamicFormWidgetAccessor {

    //~ Methods ......................................................................................................................................

    /**
     * @param   fieldName  field name
     *
     * @return  Boolean
     */
    Boolean is(@NotNull String fieldName);

    /**
     * @param   fieldName  field name
     *
     * @return  DateOnly
     */
    DateOnly getDateOnly(@NotNull String fieldName);

    /**
     * @param   fieldName  field name
     *
     * @return  DateTime
     */
    DateTime getDateTime(@NotNull String fieldName);

    /**
     * @param   fieldName  field name
     *
     * @return  BigDecimal
     */
    BigDecimal getDecimal(@NotNull String fieldName);

    /**
     * Set Value to the specific field.
     *
     * @param  fieldName  Field name
     * @param  value      value
     */
    void setFieldValue(@NotNull String fieldName, @Nullable Object value);

    /**
     * @param   fieldName  field name
     *
     * @return  Integer
     */
    Integer getInt(@NotNull String fieldName);

    /**
     * @param   columnName  field name
     *
     * @return  Object
     */
    Object getObject(String columnName);

    /**
     * @param   columnName  field name
     *
     * @return  Resource
     */
    Resource getResource(String columnName);

    /**
     * @param   fieldName  field name
     *
     * @return  String
     */
    String getString(@NotNull String fieldName);
}  // end interface DynamicFormWidgetAccessor
