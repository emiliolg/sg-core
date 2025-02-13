
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
 * Filter Labels provider. Return a string representation of the given <F> filter option.
 */
public interface Labels<F> {

    //~ Instance Fields ..............................................................................................................................

    Labels<Object> DEFAULT = String::valueOf;

    //~ Methods ......................................................................................................................................

    /** Returns the label value for the given filter option. */
    String value(@NotNull final F option);
}
