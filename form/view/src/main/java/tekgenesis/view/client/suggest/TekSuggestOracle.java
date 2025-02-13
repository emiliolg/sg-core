
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.suggest;

import java.util.Collection;

import com.google.gwt.user.client.ui.SuggestOracle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.view.client.ui.BaseWidgetUI;
import tekgenesis.view.client.ui.WidgetUI;

import static com.google.gwt.user.client.ui.TekSuggestBox.createExtraSuggestion;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * TekSuggestOracle.
 */
public abstract class TekSuggestOracle extends SuggestOracle {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private String  formFqn;
    private final boolean    hasNewMethod;
    @Nullable private Object onSuggestArg;

    @NotNull private final Option<BaseWidgetUI> ui;

    //~ Constructors .................................................................................................................................

    /** Default Constructor. */
    TekSuggestOracle(@Nullable final BaseWidgetUI ui, boolean hasNewMethod) {
        this.ui           = ofNullable(ui);
        this.hasNewMethod = hasNewMethod;
        formFqn           = "";
        onSuggestArg      = null;
    }

    //~ Methods ......................................................................................................................................

    /** Sets the form fqn. */
    public void setFormFqn(@NotNull final String formFqn) {
        this.formFqn = formFqn;
    }

    /** Sets on suggest expression result that will be passed to the user when searching. */
    public void setOnSuggestArg(@Nullable final Object result) {
        onSuggestArg = result;
    }

    void onSuggestionsReady(Callback callback, Request request, Response response) {
        final Collection<Suggestion> suggestions = cast(response.getSuggestions());

        for (final Suggestion suggestion : suggestions)
            KeySuggestOracle.formatSuggestion(request.getQuery(), (ItemSuggestion) suggestion);
        if (hasNewMethod) suggestions.add(createExtraSuggestion(MSGS.createNew()));

        callback.onSuggestionsReady(request, response);
    }

    /** Returns the form fqn. */
    @NotNull String getFormFqn() {
        return formFqn;
    }

    /** Returns on suggest expression result that will be passed to the user when searching. */
    @Nullable Object getOnSuggestArg() {
        return onSuggestArg;
    }

    /** Returns the source widget. */
    @NotNull SourceWidget getSourceWidget() {
        return getUi().toSourceWidget();
    }

    /** Return suggest oracle {@link WidgetUI ui holder}. */
    @NotNull BaseWidgetUI getUi() {
        return ui.getOrFail("Suggest oracle without ui backend.");
    }
}  // end class TekSuggestOracle
