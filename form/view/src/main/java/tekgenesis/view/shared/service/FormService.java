
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.serializer.GwtSerializationWhiteList;
import tekgenesis.view.client.service.Feedback;
import tekgenesis.view.shared.feedback.FeedbackEventData;
import tekgenesis.view.shared.response.BootResponse;
import tekgenesis.view.shared.response.FavoriteResponse;
import tekgenesis.view.shared.response.FormModelResponse;
import tekgenesis.view.shared.response.MailValidationResponse;
import tekgenesis.view.shared.response.MenuResponse;
import tekgenesis.view.shared.response.OrgUnitsResponse;
import tekgenesis.view.shared.response.Response;
import tekgenesis.view.shared.response.SwipeFetchResponse;

/**
 * GWT Form Service.
 */
@RemoteServiceRelativePath("form")
public interface FormService extends RemoteService {

    //~ Methods ......................................................................................................................................

    GwtSerializationWhiteList _whiteList(GwtSerializationWhiteList o);
    void addFavorite(@NotNull String formLink, int newIdx);

    /** Boot application. */
    Response<BootResponse> boot();
    Response<FormModelResponse> cancelForm(@NotNull final SourceWidget widget, @NotNull final FormModelResponse response);
    Response<FormModelResponse> deleteForm(@NotNull final SourceWidget widget, @NotNull final FormModelResponse response);
    Response<FormModelResponse> deprecateForm(final boolean deprecate, @NotNull final FormModelResponse response);
    Response<SwipeFetchResponse> fetchSwipeModels(@NotNull final FormModelResponse response, @NotNull final List<Integer> fetchIndexes,
                                                  @NotNull final String loaderClassName);
    @SuppressWarnings("EmptyMethod")
    void keepAlive();
    Response<FormModelResponse> lazyFetchSync(@NotNull final SourceWidget widgetId, int offset, int limit, @NotNull final FormModelResponse model);
    Response<FormModelResponse> loadFormInstance(@NotNull final String formId, @Nullable final String pk, @Nullable final String parameters);
    Response<FormModelResponse> onAbstractSync(@NotNull final SourceWidget delegate, @NotNull final SourceWidget source,
                                               @NotNull final FormModelResponse model);
    Response<FormModelResponse> onBlurSync(@NotNull final SourceWidget widgetId, @NotNull final FormModelResponse model);
    Response<FormModelResponse> onChangeSync(@NotNull final List<SourceWidget> widgetId, @NotNull final FormModelResponse model);
    Response<FormModelResponse> onClickSync(@NotNull final SourceWidget widgetId, boolean feedback, @NotNull final FormModelResponse model);
    Response<FormModelResponse> onDisplaySync(@NotNull final FormModelResponse model);
    Response<FeedbackEventData> onFutureCancellation(@NotNull final String uuid);
    Response<FeedbackEventData> onFutureSync(@NotNull final String uuid);
    Response<FormModelResponse> onLoadSync(@NotNull SourceWidget widgetId, @NotNull FormModelResponse request, @Nullable String pk);
    Response<FormModelResponse> onMethodInvocation(final String method, @NotNull final FormModelResponse response);
    Response<FormModelResponse> onNewLocationSync(@NotNull final SourceWidget source, final FormModelResponse model, double lat, double lng);
    Response<FormModelResponse> onNewSync(@NotNull SourceWidget widgetId, @NotNull FormModelResponse model, @NotNull String text);
    Response<FormModelResponse> onRowChangeSync(@NotNull SourceWidget widgetId, @NotNull FormModelResponse model);
    Response<FormModelResponse> onRowSelectedSync(@NotNull SourceWidget widgetId, @NotNull FormModelResponse model);
    Response<FormModelResponse> ping();
    void postFeedback(@NotNull final Feedback feedback);
    Response<FormModelResponse> postForm(@NotNull SourceWidget widget, @NotNull FormModelResponse response, @NotNull String action);
    void removeFavorite(int idx);
    void swapFavorite(int idxA, int idxB);
    Response<MailValidationResponse> validateEmail(String mail, boolean checkAddress);
    Response<FavoriteResponse> getFavorites();
    Response<MenuResponse> getMenu(@Nullable String fqn);
    void setOrgUnit(@NotNull final String ouName);
    Response<OrgUnitsResponse> getOrgUnits();

    //~ Inner Classes ................................................................................................................................

    /**
     * Utility/Convenience class. Use FormReaderService.App.getInstance() to access static instance
     * of {@link FormServiceAsync}
     */
    class App {
        private App() {}

        /** Returns the instance. */
        public static synchronized FormServiceAsync getInstance() {
            return ourInstance;
        }

        private static final FormServiceAsync ourInstance = GWT.create(FormService.class);
    }
}  // end interface FormService
