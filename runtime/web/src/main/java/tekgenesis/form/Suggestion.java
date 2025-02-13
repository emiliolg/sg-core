
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

import static tekgenesis.common.collections.Colls.listOf;

/**
 * Represents a suggest box suggestion.
 */
public interface Suggestion {

    //~ Methods ......................................................................................................................................

    /**
     * Strings to be used in suggest widget list. If contains only one String, one column will be
     * used. If multiple each String will be represented in a separate column.
     */
    Seq<String> getDisplayString();

    /** String that act as the key of this suggestion. */
    String getKey();

    /** String to be used as a replacement if this suggestion gets chosen from the list. */
    String getReplacementString();

    //~ Methods ......................................................................................................................................

    /** Creates a Suggestion with unique value. */
    static Suggestion create(@NotNull final String value) {
        return new SuggestionImpl(value, listOf(value), value);
    }

    /** Creates a Suggestion with key-value. */
    static Suggestion create(@NotNull final String key, @NotNull final String value) {
        return new SuggestionImpl(key, listOf(value), value);
    }

    /** Creates a Suggestion with the given values. */
    static Suggestion create(@NotNull final String key, @NotNull final Seq<String> display, @NotNull final String replacement) {
        return new SuggestionImpl(key, display, replacement);
    }
}
