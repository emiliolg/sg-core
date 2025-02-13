
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

import tekgenesis.common.collections.Seq;

/**
 * Filter options provider.
 */
public interface Options<T, F> {

    //~ Methods ......................................................................................................................................

    /** Returns a sequence of filter options given a set of elements. */
    Seq<F> values(@NotNull final Seq<T> elements);
}
