
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.suggest;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.IntIntTuple;
import tekgenesis.view.client.ui.WidgetUI;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Strings.findSubstring;
import static tekgenesis.common.core.Strings.stripAccents;
import static tekgenesis.metadata.form.model.FormConstants.CLOSE_STRONG_TAG;
import static tekgenesis.metadata.form.model.FormConstants.STRONG_TAG;

/**
 * TekSuggestOracle.
 */
@SuppressWarnings("NonJREEmulationClassesInClientCode")  // GWT, also not true.
public class KeySuggestOracle extends TekSuggestOracle {

    //~ Instance Fields ..............................................................................................................................

    private final List<ItemSuggestion> options;

    //~ Constructors .................................................................................................................................

    /** Creates a KeySuggestOracle. */
    public KeySuggestOracle() {
        this(null, false);
    }

    /** Creates a KeySuggestOracle. */
    public KeySuggestOracle(@Nullable final WidgetUI ui, final boolean hasNewMethod) {
        super(ui, hasNewMethod);
        options = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    /** Adds a suggestion. */
    public void addSuggestion(ItemSuggestion suggestion) {
        options.add(suggestion);
    }

    /** Clears options. */
    public void clear() {
        options.clear();
    }

    @Override public void requestDefaultSuggestions(Request request, Callback callback) {
        requestSuggestions(request, callback);
    }

    @Override public void requestSuggestions(Request request, Callback callback) {
        final String               query      = stripAccents(request.getQuery());
        final List<ItemSuggestion> candidates = new ArrayList<>();

        if (isEmpty(query)) candidates.addAll(options);
        else {
            // first we add 'startWith' candidates...
            for (final ItemSuggestion suggestion : options) {
                final String replacementString = stripAccents(suggestion.getReplacementString());
                if (replacementString.toLowerCase().startsWith(query.toLowerCase())) {
                    suggestion.setDisplayString(suggestion.getReplacementString());
                    candidates.add(formatSuggestion(query, suggestion));
                }
            }

            // ... then we add 'contains' candidates only if they were not added first.
            // In both cases candidate's format is: part1<strong>query</strong>part2.
            for (final ItemSuggestion suggestion : options) {
                final String replacementString = stripAccents(suggestion.getReplacementString());
                if (!candidates.contains(suggestion) && replacementString.toLowerCase().contains(query.toLowerCase())) {
                    suggestion.setDisplayString(suggestion.getReplacementString());
                    candidates.add(formatSuggestion(query, suggestion));
                }
            }
        }

        // we finally call the callback with the candidates list.
        onSuggestionsReady(callback, request, new Response(candidates));
    }  // end method requestSuggestions

    @Override public boolean isDisplayStringHTML() {
        return true;
    }

    /** Returns this suggeste options. */
    public List<ItemSuggestion> getOptions() {
        return options;
    }

    //~ Methods ......................................................................................................................................

    /** Formats a suggestion. */
    static ItemSuggestion formatSuggestion(final String query, final ItemSuggestion suggestion) {
        String str = suggestion.getDisplayString();

        // First, the image part.
        String imgPart = "";
        if (str.contains("<img")) {
            final int begin = str.indexOf("<img");
            final int end   = str.indexOf("/>");
            imgPart = str.substring(begin, end + 2);
            str     = str.substring(0, begin) + str.substring(end + 2);
        }

        // Then split it by tab char and make the tds.
        final StringBuilder accum = new StringBuilder();
        final String[]      parts = str.split("\\t");
        for (final String part : parts) {
            final IntIntTuple idx = findSubstring(part, query);
            if (idx != null) {
                final String part1      = part.substring(0, idx.first());
                final String strongPart = part.substring(idx.first(), idx.second());
                final String part2      = part.substring(idx.second(), part.length());

                accum.append(part1);
                if (!strongPart.isEmpty()) {
                    accum.append(STRONG_TAG);
                    accum.append(strongPart);
                    accum.append(CLOSE_STRONG_TAG);
                }
                accum.append(part2);
            }
            else accum.append(part);
            accum.append('\t');
        }

        suggestion.setDisplayString(imgPart + accum.toString());

        return suggestion;
    }
}  // end class KeySuggestOracle
