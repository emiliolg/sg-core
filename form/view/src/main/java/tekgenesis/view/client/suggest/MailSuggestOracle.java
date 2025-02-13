
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

import tekgenesis.metadata.form.OnSuggestParams;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.view.client.ui.MailFieldUI;
import tekgenesis.view.shared.response.SuggestResponse;
import tekgenesis.view.shared.response.SyncFormResponse;
import tekgenesis.view.shared.service.IndexService;

import static tekgenesis.common.Predefined.cast;

public class MailSuggestOracle extends KeySuggestOracle {

    //~ Instance Fields ..............................................................................................................................

    private SyncFormResponse sync = null;

    //~ Constructors .................................................................................................................................

    public MailSuggestOracle(MailFieldUI mailField) {
        super(mailField, false);
    }

    //~ Methods ......................................................................................................................................

    @Override public void requestSuggestions(Request request, Callback callback) {
        final OnSuggestParams params = new OnSuggestParams(request.getQuery(), getOnSuggestArg(), false);

        final MailFieldUI  mailField = getMailFieldUI();
        final SourceWidget source    = getSourceWidget();
        if (mailField.hasOnSuggest())
            IndexService.App.getInstance().getUserSuggestions(getFormFqn(), source, params, new ItemSuggestCallback(request, callback, getNextSeq()));
        else if (mailField.hasOnSuggestSync())
            IndexService.App.getInstance().getUserSuggestionsSync(params, source, sync, new ItemSuggestCallback(request, callback, getNextSeq()));
        else super.requestSuggestions(request, callback);
    }

    /** Sets the form model. */
    public void setFormModel(FormModel formModel) {
        sync = new SyncFormResponse(formModel);
    }

    private MailFieldUI getMailFieldUI() {
        return (MailFieldUI) getUi();
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

    protected class ItemSuggestCallback implements AsyncCallback<SuggestResponse> {
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

                for (final Object key : response.getKeys()) {
                    final String value         = cast(key);
                    final String displayString = response.getSuggestionsDescription().get(key);
                    final String toString      = response.getSuggestionsToString().get(key);
                    suggestions.add(new ItemSuggestion(value, displayString, toString));
                }

                resp.setSuggestions(suggestions);
                resp.setMoreSuggestions(response.hasMore());

                onSuggestionsReady(callback, request, resp);
            }
        }
    }
}  // end class MailSuggestOracle
