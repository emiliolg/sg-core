
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import java.io.Serializable;
import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Enumeration;
import tekgenesis.index.SearchableExpr.FieldExpr;
import tekgenesis.persistence.EntityInstance;

import static tekgenesis.common.collections.Colls.listOf;

/**
 * Represents a search field inside a searchable block.
 */
public interface SearchableField<V> extends Serializable {

    //~ Instance Fields ..............................................................................................................................

    long serialVersionUID = 2250424764528549568L;

    //~ Methods ......................................................................................................................................

    /** This field's value should be the passed value. */
    FieldExpr eq(@NotNull final V value);

    /** This field's value should be any of the values inside the iterable. */
    FieldExpr in(@NotNull final Seq<V> value);

    /** This field's value should be any of the values inside the iterable. */
    @SuppressWarnings("unchecked")
    default FieldExpr in(@NotNull final V first, V... rest) {
        return in(listOf(first, rest));
    }

    /** This field's value should not be the passed value. */
    default FieldExpr ne(@NotNull final V value) {
        return eq(value).not();
    }

    /** This field's value should not be any of the values inside the iterable. */
    default FieldExpr notIn(@NotNull final Seq<V> value) {
        return in(value).not();
    }

    /** This field's value should not be any of the values inside the iterable. */
    @SuppressWarnings("unchecked")
    default FieldExpr notIn(@NotNull final V first, V... rest) {
        return notIn(listOf(first, rest));
    }

    //~ Inner Interfaces .............................................................................................................................

    interface Bool extends SearchableField<Boolean> {}

    interface Date extends SearchableField<DateOnly> {}

    interface Decimal extends SearchableField<BigDecimal> {}

    interface DTime extends SearchableField<DateTime> {}

    interface Ent<T extends EntityInstance<T, ?>> extends SearchableField<T> {
        long serialVersionUID = 6550422364528543568L;

        /**
         * Return entity field as primitive type string. Commonly used to compare with keyAsString.
         */
        Str primitive();
    }

    interface Enum<E extends java.lang.Enum<E> & Enumeration<E, ?>> extends SearchableField<E> {}

    interface Int extends SearchableField<Integer> {}

    interface LongFld extends SearchableField<Long> {}

    interface Many<C> extends SearchableField<C> {}

    interface ManyEnt<C extends EntityInstance<?, ?>> extends SearchableField<C> {
        long serialVersionUID = 1250424764528549543L;

        /**
         * Return entity field as primitive type string. Commonly used to compare with keyAsString.
         */
        Many<String> primitive();
    }

    interface Real extends SearchableField<Double> {}

    interface Str extends SearchableField<String> {}
}  // end interface SearchableField
