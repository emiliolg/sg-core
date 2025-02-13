
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
 * Suggestion implementation.
 */
public final class SuggestionImpl implements Suggestion {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Seq<String> displayString;

    @NotNull private final String key;
    @NotNull private final String replacementString;

    //~ Constructors .................................................................................................................................

    SuggestionImpl(@NotNull final String k, @NotNull final Seq<String> display, @NotNull final String replacement) {
        key               = k;
        displayString     = display;
        replacementString = replacement;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Seq<String> getDisplayString() {
        return displayString;
    }

    @NotNull @Override public String getKey() {
        return key;
    }

    @NotNull @Override public String getReplacementString() {
        return replacementString;
    }
}
