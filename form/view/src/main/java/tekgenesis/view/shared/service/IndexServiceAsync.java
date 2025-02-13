
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

import org.jetbrains.annotations.NotNull;

import tekgenesis.index.QueryMode;
import tekgenesis.metadata.form.OnSuggestParams;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.serializer.GwtSerializationWhiteList;
import tekgenesis.type.assignment.AssignmentType;
import tekgenesis.view.shared.response.SuggestResponse;
import tekgenesis.view.shared.response.SyncFormResponse;

/**
 * Index service interface.
 */
public interface IndexServiceAsync {

    //~ Methods ......................................................................................................................................

    /** Hack to add classes to the GWT serialization white list. */
    void _whiteList(GwtSerializationWhiteList o, AsyncCallback<GwtSerializationWhiteList> async);

    /** Returns a Suggestion response. */
    void getIndexSuggestions(SuggestOracle.Request req, List<AssignmentType> filter, QueryMode mode, String fqn,
                             AsyncCallback<SuggestResponse> callback);

    /** Returns a list of suggestions returned by the user. */
    void getUserSuggestions(@NotNull final String formFqn, @NotNull final SourceWidget widget, @NotNull final OnSuggestParams params,
                            AsyncCallback<SuggestResponse> async);

    /** Returns a list of suggestions returned by the user. Performs a sync. */
    void getUserSuggestionsSync(@NotNull final OnSuggestParams params, @NotNull final SourceWidget widget, @NotNull final SyncFormResponse response,
                                AsyncCallback<SuggestResponse> async);
}
