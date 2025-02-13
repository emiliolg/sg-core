
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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

import org.jetbrains.annotations.NotNull;

import tekgenesis.index.QueryMode;
import tekgenesis.metadata.form.OnSuggestParams;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.type.assignment.AssignmentType;
import tekgenesis.view.client.ui.SuggestUI;
import tekgenesis.view.shared.response.SuggestResponse;
import tekgenesis.view.shared.response.SyncFormResponse;
import tekgenesis.view.shared.service.IndexService;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.collections.Colls.toList;
import static tekgenesis.common.util.JavaReservedWords.FALSE;
import static tekgenesis.md.MdConstants.DEPRECATED_FIELD;
import static tekgenesis.metadata.form.widget.WidgetType.SUGGEST_BOX;
import static tekgenesis.metadata.form.widget.WidgetType.TAGS_SUGGEST_BOX;
import static tekgenesis.type.assignment.AssignmentType.assignment;

/**
 * Item suggest prototype.
 */
public class ItemSuggestOracle extends TekSuggestOracle {

    //~ Instance Fields ..............................................................................................................................

    private boolean addDeprecated = false;
    private String  fqn;

    private boolean handleDeprecated = false;

    private final QueryMode queryMode;

    private SyncFormResponse sync = null;

    //~ Constructors .................................................................................................................................

    /** Creates an ItemSuggestOracle given the full qualified name of the entity to search. */
    public ItemSuggestOracle(@NotNull SuggestUI suggest, boolean hasNewMethod, String fqn) {
        super(suggest, hasNewMethod);
        this.fqn  = fqn;
        queryMode = suggest.getModel().getQueryMode();
    }

    //~ Methods ......................................................................................................................................

    @Override public void requestDefaultSuggestions(Request request, Callback callback) {
        setLastSeq(getNextSeq());

        request.setQuery("");
        requestSuggestions(request, callback);
    }

    public void requestSuggestions(Request req, Callback callback) {
        final OnSuggestParams params  = new OnSuggestParams(req.getQuery(), getOnSuggestArg(), addDeprecated);
        final SuggestUI       suggest = getSuggestUi();
        final SourceWidget    source  = getSourceWidget();
        if (suggest.hasOnSuggest())
            IndexService.App.getInstance().getUserSuggestions(getFormFqn(), source, params, new ItemSuggestCallback(req, callback, getNextSeq()));
        else if (suggest.hasOnSuggestSync())
            IndexService.App.getInstance().getUserSuggestionsSync(params, source, sync, new ItemSuggestCallback(req, callback, getNextSeq()));
        else requestIndexSuggestions(req, callback);
    }

    /** User wants to specifically search for deprecated instances. */
    public void setAddDeprecated(boolean addDeprecated) {
        this.addDeprecated = addDeprecated;
    }

    /** Set entity fqn. */
    public void setEntityToSearchFqn(String fullName) {
        fqn = fullName;
    }

    /** Sets the form model. */
    public void setFormModel(FormModel formModel) {
        sync = new SyncFormResponse(formModel);
    }

    /** Logged user can handle deprecated instances or not (based on permission). */
    public void setHandleDeprecated(boolean handleDeprecated) {
        this.handleDeprecated = handleDeprecated;
    }

    public boolean isDisplayStringHTML() {
        return true;
    }

    @NotNull private List<AssignmentType> addDeprecatedFalse(@NotNull final List<AssignmentType> filter) {
        final List<AssignmentType> result = new ArrayList<>(filter);
        result.add(assignment(DEPRECATED_FIELD, listOf(FALSE), true, true));
        return result;
    }

    private void requestIndexSuggestions(Request req, Callback callback) {
        final SuggestUI suggest = getSuggestUi();
        if (suggest.getModel().getType().isString()) return;
        List<AssignmentType> filter = toList(suggest.getFilterExpression());

        // if suggest is a SuggestBox and is bounded to a deprecable entity, filter must be switched to deprecated false.
        if (suggest.isBoundedToDeprecable() && (isSuggestBox() || !handleDeprecated)) filter = addDeprecatedFalse(filter);

        // if user is able to handle deprecated instances and widget searches for deprecable.
        if (handleDeprecated && suggest.canSearchDeprecable() && !addDeprecated)
        // add deprecated filter only if widget can search for deprecable and user wants to search for deprecable.
        filter = addDeprecatedFalse(filter);

        IndexService.App.getInstance().getIndexSuggestions(req, filter, queryMode, fqn, new ItemSuggestCallback(req, callback, getNextSeq()));
    }  // end method requestIndexSuggestions

    @NotNull private SuggestUI getSuggestUi() {
        return (SuggestUI) getUi();
    }

    private boolean isSuggestBox() {
        final SuggestUI suggest = getSuggestUi();
        return suggest.getModel().getWidgetType() == SUGGEST_BOX || suggest.getModel().getWidgetType() == TAGS_SUGGEST_BOX;
    }

    //~ Methods ......................................................................................................................................

    private static int lastSeq() {
        return lastSeq;
    }

    private static void setLastSeq(int seq) {
        lastSeq = seq;
    }

    private static int getNextSeq() {
        return ++seqNumber;
    }

    //~ Static Fields ................................................................................................................................

    private static int seqNumber = 0;

    private static int lastSeq = 0;

    //~ Inner Classes ................................................................................................................................

    class ItemSuggestCallback implements AsyncCallback<SuggestResponse> {
        private final SuggestOracle.Callback callback;

        private final SuggestOracle.Request request;
        private final int                   seq;

        public ItemSuggestCallback(Request request, Callback callback, int i) {
            this.request  = request;
            this.callback = callback;
            seq           = i;
        }

        public void onFailure(Throwable error) {}

        public void onSuccess(SuggestResponse response) {
            if (seq >= lastSeq()) {
                setLastSeq(seq);

                final SuggestOracle.Response resp        = new SuggestOracle.Response();
                final List<ItemSuggestion>   suggestions = new ArrayList<>();

                final SuggestUI suggest = getSuggestUi();
                for (final Object key : response.getKeys()) {
                    final String value         = suggest.getInputHandler().toString(key);
                    final String displayString = response.getSuggestionsDescription().get(key);  // May contain HTML.
                    final String toString      = response.getSuggestionsToString().get(key);
                    suggestions.add(new ItemSuggestion(value, displayString, toString));
                }

                resp.setSuggestions(suggestions);
                resp.setMoreSuggestions(response.hasMore());

                onSuggestionsReady(callback, request, resp);
            }
        }
    }
}  // end class ItemSuggestOracle
