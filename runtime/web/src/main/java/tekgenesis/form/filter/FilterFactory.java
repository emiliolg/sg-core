
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.filter;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Enumeration;

import static tekgenesis.form.filter.Filter.*;

/**
 * Factory methods for filters creation.
 */
public interface FilterFactory {

    //~ Methods ......................................................................................................................................

    /** Creates a boolean filter for the given boolean field. */
    BooleanFilter boolFilter(@NotNull final Enum<?> field, @NotNull final String title);

    /** Creates custom <F> options for the given <T> type. */
    <T, F> CustomOptions<T, F> customFilter(@NotNull final String title, @NotNull final Options<?, F> options);

    /** Creates a date filter for the given date field. */
    DateFilter dateFilter(@NotNull final Enum<?> field, @NotNull final String title);

    /** Creates a date-time filter for the given date-time field. */
    DateTimeFilter dateTimeFilter(@NotNull final Enum<?> field, @NotNull final String title);

    /** Creates a decimal filter for the given decimal field. */
    DecimalFilter decimalFilter(@NotNull final Enum<?> field, @NotNull final String title);

    /** Creates an entity filter for the given entity field. */
    <T> EntityFilter<T> entityFilter(@NotNull final Enum<?> field, @NotNull final Class<T> type, @NotNull final String title);

    /** Creates an enum filter for the given enum field. */
    <T extends Enum<T> & Enumeration<T, I>, I> EnumFilter<T, I> enumFilter(@NotNull final Enum<?> field, @NotNull final Class<T> type,
                                                                           @NotNull final String title);

    /** Creates an integer filter for the given integer field. */
    IntegerFilter intFilter(@NotNull final Enum<?> field, @NotNull final String title);

    /** Creates an real filter for the given real field. */
    RealFilter realFilter(@NotNull final Enum<?> field, @NotNull final String title);

    /** Creates a string filter for the given string field. */
    StringFilter strFilter(@NotNull final Enum<?> field, @NotNull final String title);
}
