
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
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
 * Prototype for suggest.
 */
@RemoteServiceRelativePath("index")
@SuppressWarnings("DuplicateStringLiteralInspection")
public interface IndexService extends RemoteService {

    //~ Methods ......................................................................................................................................

    GwtSerializationWhiteList _whiteList(GwtSerializationWhiteList o);
    @SuppressWarnings("NonSerializableServiceParameters")
    SuggestResponse getIndexSuggestions(SuggestOracle.Request req, List<AssignmentType> filter, QueryMode mode, String fqn);
    @SuppressWarnings("NonSerializableServiceParameters")
    SuggestResponse getUserSuggestions(@NotNull final String formFqn, @NotNull final SourceWidget widget, @NotNull final OnSuggestParams params);
    @SuppressWarnings("NonSerializableServiceParameters")
    SuggestResponse getUserSuggestionsSync(@NotNull final OnSuggestParams params, @NotNull final SourceWidget widget,
                                           @NotNull final SyncFormResponse response);

    //~ Inner Classes ................................................................................................................................

    class App {
        private App() {}

        /** Returns the instance. */
        public static synchronized IndexServiceAsync getInstance() {
            return ourInstance;
        }

        private static final IndexServiceAsync ourInstance = GWT.create(IndexService.class);
    }
}
