
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import tekgenesis.common.core.Enumeration;
import tekgenesis.index.SearchableFieldImpl.*;
import tekgenesis.persistence.EntityInstance;

/**
 * Factory for searchable fields.
 */
public interface SearchableFields {

    //~ Methods ......................................................................................................................................

    /** Boolean field. */
    BoolField boolField(String id, String name);

    /** Date field. */
    DateField dateField(String id, String name);

    /** DateTime field. */
    DateTimeField dateTimeField(String id, String name);

    /** Decimal field. */
    DecimalField decimalField(String id, String name);

    /** Entity field. */
    <T extends EntityInstance<T, K>, K> SearchableFieldImpl.EntityField<T, K> entityField(String id, String name, Class<T> entityClass);

    /** Enum field. */
    <E extends Enum<E> & Enumeration<E, ?>> EnumField<E> enumField(String id, String name, Class<E> enumClass);

    /** Int field. */
    IntField intField(String id, String name);

    /** Long field. */
    LongField longField(String id, String name);

    /** Many entities field. Represents the field as a multiple. */
    <C extends EntityInstance<?, ?>> ManyEntField<C> manyEntField(SearchableField<C> field);

    /** Many field. Represents the field as a multiple. */
    <C> ManyField<C> manyField(SearchableField<C> field);

    /** Real field. */
    RealField realField(String id, String name);

    /** String field. */
    StrField strField(String id, String name);
}  // end interface SearchableFields
