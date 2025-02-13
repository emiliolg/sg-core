
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import java.io.Serializable;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import tekgenesis.model.KeyMap;

/**
 * A SuggestResponse.
 */
public class SuggestResponse implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private boolean defaultSuggestions;
    private boolean hasMore;

    @SuppressWarnings("GwtInconsistentSerializableClass")  // it's fine IDEA!
    private KeyMap suggestionsDescription = null;

    @SuppressWarnings("GwtInconsistentSerializableClass")  // it's fine IDEA!
    private KeyMap suggestionsToString = null;

    //~ Constructors .................................................................................................................................

    /** Default constructor for GWT serialization. */
    public SuggestResponse() {
        this(KeyMap.create(), KeyMap.create());
    }

    /**
     * Creates a SuggestResponse with a KeyMap of suggestions. By default (and for backward
     * compatibility) suggestion response are not default ones.
     */
    public SuggestResponse(KeyMap descriptions, KeyMap toStrings) {
        suggestionsDescription = descriptions;
        suggestionsToString    = toStrings;
        defaultSuggestions     = false;
        hasMore                = true;
    }

    //~ Methods ......................................................................................................................................

    /** Adds a suggestion. */
    public void addSuggestion(@NotNull final Object key, @NotNull final String description, @NotNull final String toString) {
        suggestionsDescription.put(key, description);
        suggestionsToString.put(key, toString);
    }

    /** Returns true if there are more entities besides the suggested ones. */
    public boolean hasMore() {
        return hasMore;
    }

    /**
     * Sets this suggestion response as a one containing default suggestions (SuggestBoxes renders
     * them differently).
     */
    public void setDefaultSuggestions(boolean defaultSuggestions) {
        this.defaultSuggestions = defaultSuggestions;
    }

    /** Set this to true if there are more suggestions. */
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    /** Returns keys. */
    public Set<Object> getKeys() {
        return suggestionsDescription.keySet();
    }

    /** Tells whether this response contains default suggestions. */
    public boolean isDefaultSuggestions() {
        return defaultSuggestions;
    }

    /** Returns the suggestions descriptions contained in this response. */
    public KeyMap getSuggestionsDescription() {
        return suggestionsDescription;
    }

    /** Returns the suggestions toStrings contained in this response. */
    public KeyMap getSuggestionsToString() {
        return suggestionsToString;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -4582455384568516739L;
}  // end class SuggestResponse
