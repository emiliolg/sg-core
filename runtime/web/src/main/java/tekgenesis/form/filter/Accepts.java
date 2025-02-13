
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

/**
 * Filter accepts predicate.
 */
@SuppressWarnings("WeakerAccess")
public interface Accepts<T, F> {

    //~ Methods ......................................................................................................................................

    /** True if element accepts given filter option. */
    boolean apply(@NotNull final T element, @NotNull final F option);
}
