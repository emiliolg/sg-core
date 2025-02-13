
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

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
 * GWT Async Form Service.
 */
public interface FormServiceAsync {

    //~ Methods ......................................................................................................................................

    /** Hack to add classes to the GWT serialization white list. */
    @SuppressWarnings("UnusedDeclaration")  // used to generate a gwt serialization white list.
    void _whiteList(GwtSerializationWhiteList o, AsyncCallback<GwtSerializationWhiteList> async);

    /** Add User Favorite. */
    void addFavorite(@NotNull String formLink, int newIdx, @NotNull AsyncCallback<Void> async);

    /** Get the menus for the toolbar. */
    void boot(@NotNull AsyncCallback<Response<BootResponse>> async);

    /** Cancels a Form. */
    void cancelForm(@NotNull final SourceWidget widget, @NotNull final FormModelResponse response,
                    @NotNull final AsyncCallback<Response<FormModelResponse>> async);

    /** Deletes a Form. */
    void deleteForm(@NotNull final SourceWidget widget, @NotNull final FormModelResponse response,
                    @NotNull final AsyncCallback<Response<FormModelResponse>> async);

    /** Changes deprecation status of an instance. */
    void deprecateForm(final boolean deprecate, @NotNull final FormModelResponse response,
                       @NotNull final AsyncCallback<Response<FormModelResponse>> async);

    /** Fetches more models to be displayed in a Swiper. */
    void fetchSwipeModels(@NotNull final FormModelResponse response, @NotNull final List<Integer> fetchIndexes, @NotNull final String loaderClassName,
                          @NotNull final AsyncCallback<Response<SwipeFetchResponse>> async);

    /** Posts feedback. */
    void keepAlive(@NotNull AsyncCallback<Void> async);

    /** Syncs the form model after executing a lazyFetch method of the triggered widget. */
    void lazyFetchSync(@NotNull final SourceWidget source, int offset, int limit, final FormModelResponse model,
                       @NotNull final AsyncCallback<Response<FormModelResponse>> async);

    /** Retrieves a form instance from a formId and instance primary keys. */
    RequestBuilder loadFormInstance(@NotNull final String formId, @Nullable final String pk, @Nullable final String parameters,
                                    @NotNull final AsyncCallback<Response<FormModelResponse>> async);

    /** Syncs the form model after executing an abstract invocation for a triggering widget. */
    void onAbstractSync(@NotNull final SourceWidget delegate, @NotNull final SourceWidget source, final FormModelResponse model,
                        @NotNull final AsyncCallback<Response<FormModelResponse>> async);

    /** Syncs the form model after executing the onBlur method of the triggered widget. */
    void onBlurSync(@NotNull final SourceWidget source, final FormModelResponse model,
                    @NotNull final AsyncCallback<Response<FormModelResponse>> async);

    /** Syncs the form model after executing all the on change methods of the triggered widget. */
    void onChangeSync(@NotNull final List<SourceWidget> widgetIds, @NotNull final FormModelResponse model,
                      @NotNull final AsyncCallback<Response<FormModelResponse>> async);

    /** Syncs the form model after executing the onClick method of the triggered widget. */
    void onClickSync(@NotNull final SourceWidget source, boolean feedback, final FormModelResponse model,
                     @NotNull final AsyncCallback<Response<FormModelResponse>> async);

    /** Syncs the form model after executing the onDisplay method of the triggered widget. */
    void onDisplaySync(final FormModelResponse model, @NotNull final AsyncCallback<Response<FormModelResponse>> async);

    /** Syncs the form model after waiting the onClick execution. */
    void onFutureCancellation(@NotNull final String uuid, @NotNull final AsyncCallback<Response<FeedbackEventData>> async);

    /** Maybe syncs the form model after checking for future pending execution. */
    void onFutureSync(@NotNull final String uuid, @NotNull final AsyncCallback<Response<FeedbackEventData>> async);

    /** Navigates to a form instance from a formId and instance primary keys. */
    void onLoadSync(@NotNull final SourceWidget source, @NotNull FormModelResponse request, @Nullable String pk,
                    AsyncCallback<Response<FormModelResponse>> async);

    /** Server side asynchronous method invocation. */
    void onMethodInvocation(final String method, @NotNull final FormModelResponse response,
                            @NotNull final AsyncCallback<Response<FormModelResponse>> async);

    /** Syncs the form model after executing the onBlur method of the triggered widget. */
    void onNewLocationSync(@NotNull final SourceWidget source, final FormModelResponse model, double lat, double lng,
                           @NotNull final AsyncCallback<Response<FormModelResponse>> async);

    /** Syncs the form model after executing the onNew method of the triggered suggestBox. */
    void onNewSync(@NotNull SourceWidget source, @NotNull FormModelResponse response, @NotNull String text,
                   AsyncCallback<Response<FormModelResponse>> async);

    /** Syncs the form model after executing on row change method of the triggered widget. */
    void onRowChangeSync(@NotNull SourceWidget source, @NotNull FormModelResponse model, AsyncCallback<Response<FormModelResponse>> async);

    /** Syncs the form model after executing on row selected method of the triggered widget. */
    void onRowSelectedSync(@NotNull SourceWidget source, @NotNull FormModelResponse model, AsyncCallback<Response<FormModelResponse>> async);

    /** Ping for login. feedback. */
    void ping(@NotNull AsyncCallback<Response<FormModelResponse>> async);

    /** Posts feedback. */
    void postFeedback(@NotNull final Feedback feedback, @NotNull AsyncCallback<Void> async);

    /** Submits a Form. */
    void postForm(@NotNull final SourceWidget widget, @NotNull final FormModelResponse response, @NotNull final String action,
                  @NotNull final AsyncCallback<Response<FormModelResponse>> async);

    /** Remove User Favorite. */
    void removeFavorite(int idx, @NotNull AsyncCallback<Void> async);

    /** Swap User Favorite. */
    void swapFavorite(int idxA, int idxB, @NotNull AsyncCallback<Void> async);

    /** Validates wheter the mail's domain and address exist or not. */
    void validateEmail(String mail, boolean checkAddress, AsyncCallback<Response<MailValidationResponse>> async);

    /** Get the Org Units for the OU changer. */
    void getFavorites(@NotNull AsyncCallback<Response<FavoriteResponse>> async);

    /** Get the menus for the toolbar. */
    void getMenu(@Nullable String fqn, @NotNull AsyncCallback<Response<MenuResponse>> async);

    /** Changes the Org Unit in the Context. */
    void setOrgUnit(@NotNull final String ouName, @NotNull AsyncCallback<Void> async);

    /** Get the Org Units for the OU changer. */
    void getOrgUnits(@NotNull AsyncCallback<Response<OrgUnitsResponse>> async);
}  // end interface FormServiceAsync
