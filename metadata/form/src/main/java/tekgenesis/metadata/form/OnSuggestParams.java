
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;

import static tekgenesis.common.core.Option.option;

/**
 * Creates an on suggest param.
 */
public class OnSuggestParams {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private final Object arg;
    private final boolean          deprecated;

    @NotNull private final String query;

    //~ Constructors .................................................................................................................................

    /** Create the source of the event. */
    public OnSuggestParams(@NotNull final String query, @Nullable final Object arg, boolean deprecated) {
        this.query      = query;
        this.arg        = arg;
        this.deprecated = deprecated;
    }

    //~ Methods ......................................................................................................................................

    /** Returns an option an argument or null if there isn't any. */
    @Nullable public Object getArg() {
        return arg;
    }

    /** Returns true if deprecated flag was activated on form. */
    public boolean isDeprecated() {
        return deprecated;
    }

    /** Returns an option representing an argument. */
    @NotNull public Option<Object> getOptionArg() {
        return option(arg);
    }

    /** Returns search query. */
    @NotNull public String getQuery() {
        return query;
    }
}  // end class OnSuggestParams
