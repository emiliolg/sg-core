
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.view.client.FormBox;
import tekgenesis.view.shared.feedback.FeedbackEventData;
import tekgenesis.view.shared.logging.LoggingUtils;
import tekgenesis.view.shared.response.FormModelResponse;
import tekgenesis.view.shared.response.Response;
import tekgenesis.view.shared.response.SwipeFetchResponse;
import tekgenesis.view.shared.service.FormService;
import tekgenesis.view.shared.service.FormServiceAsync;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.metadata.form.model.FormConstants.CURRENT_FORM_ID;
import static tekgenesis.view.client.Application.messages;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.service.BaseAsyncCallback.resolveMessage;

/**
 * Wraps the FormService to add feedback.
 */
public class ClientFormService {

    //~ Instance Fields ..............................................................................................................................

    private final FormBox formBox;

    private final Set<SourceWidget> processing = new HashSet<>();
    private final FormServiceAsync  service;

    //~ Constructors .................................................................................................................................

    /** Constructs the service. */
    public ClientFormService(FormBox formBox) {
        this.formBox = formBox;
        service      = FormService.App.getInstance();
    }

    //~ Methods ......................................................................................................................................

    /** Cancels a Form. */
    public void cancelForm(@NotNull final SourceWidget source, final FormModelResponse response, final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.canceling(), formBox);
        LoggingUtils.logCancel(logger, response.getSync().getFqn(), true);
        service.cancelForm(source, response, new AsyncCallbackImpl(callback, MSGS.canceled(), formBox));
    }

    public void clearProcessing() {
        processing.clear();
    }

    /** Deletes a Form. */
    public void deleteForm(@NotNull final SourceWidget source, final FormModelResponse response, final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.deleting(), formBox);
        LoggingUtils.logDelete(logger, response.getSync().getFqn(), true);
        service.deleteForm(source, response, new AsyncCallbackImpl(callback, MSGS.deleted(), formBox));
    }

    /** Change the deprecation status of an instance. */
    public void deprecateForm(final boolean deprecate, final FormModelResponse response, final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.processing(), formBox);
        LoggingUtils.logDeprecate(logger, deprecate, response.getSync().getFqn(), true);
        service.deprecateForm(deprecate, response, new AsyncCallbackImpl(callback, formBox));
    }

    /** Fetches more form models from the server. */
    public void fetchSwipeModels(final FormModelResponse model, List<Integer> fetchIndexes, String loaderClassName,
                                 final DefaultCallback<SwipeFetchResponse> callback) {
        messages().info(MSGS.fetching(), formBox);
        LoggingUtils.logFetch(logger, fetchIndexes, loaderClassName, true);
        service.fetchSwipeModels(model, fetchIndexes, loaderClassName, new BaseCallback<SwipeFetchResponse>(callback, formBox, true) {
                @Override public void onSuccess(Response<SwipeFetchResponse> result) {
                    super.callback.onSuccess(result.getResponse());
                    messages().hide(super.formBox);
                }
            });
    }

    public void lazyFetchSync(final SourceWidget source, int offset, int limit, final FormModelResponse model,
                              final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.processing(), formBox);
        LoggingUtils.logLazyFetch(logger, source, model.getSync().getFqn(), true);
        service.lazyFetchSync(source, offset, limit, model, new AsyncCallbackImpl(callback, formBox));
    }

    /** Retrieves a form instance from a formId and instance primary keys. */
    public void loadForm(@NotNull final String formId, @Nullable final String pk, @Nullable final String parameters,
                         @NotNull final DefaultCallback<FormModelResponse> callback) {
        if (formBox.isBoundedByHistory()) messages().info(MSGS.loading(), formBox);
        LoggingUtils.logLoad(logger, formId, pk, parameters, true);
        final RequestBuilder requestBuilder = service.loadFormInstance(formId, pk, parameters, new AsyncCallbackImpl(callback, formBox));
        requestBuilder.setHeader(CURRENT_FORM_ID, formId);
        try {
            requestBuilder.send();
        }
        catch (final RequestException e) {
            messages().error(MSGS.oops() + " " + resolveMessage(e), formBox);
            logger.error(e);
        }
    }

    /** Requests html using the url. */

    //J-
    public void loadHtml(@NotNull final String url, @NotNull final Consumer<String> callback) {
        try {
            new RequestBuilder(RequestBuilder.GET, url).sendRequest(null, new RequestCallback() {
                                                                        @Override public void onResponseReceived(
                                                                            Request                             request,
                                                                            com.google.gwt.http.client.Response response) {
                                                                            callback.accept(response.getText());
                                                                        }

                                                                        @Override public void onError(Request   request,
                                                                                                      Throwable e) {
                                                                            messages().error(MSGS.oops() + " " +
                                                                                            resolveMessage(e),
                                                                                    formBox);
                                                                        }
                                                                    });
        }
        catch (final RequestException e) {
            messages().error(MSGS.oops() + " " + resolveMessage(e), formBox);
        }
    }
    //J+

    /** Server side method invocation. */
    public void methodInvocation(final String method, final FormModelResponse response, final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.processing(), formBox);
        LoggingUtils.logOnMethodInvocation(logger, method, response.getSync().getFqn(), true);
        service.onMethodInvocation(method, response, new AsyncCallbackImpl(callback, formBox));
    }

    /** Syncs the form model after executing an abstract invocation for a triggering widget. */
    public void onAbstractSync(final SourceWidget delegate, final SourceWidget source, final FormModelResponse model,
                               final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.processing(), formBox);
        LoggingUtils.logAbstractMethod(logger, delegate, source, model.getSync().getFqn(), true);
        service.onAbstractSync(delegate, source, model, new AsyncCallbackImpl(callback, formBox));
    }

    /** Syncs the form model after executing the onBlur method of the triggered widget. */
    public void onBlurSync(final SourceWidget source, final FormModelResponse model, final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.processing(), formBox);
        LoggingUtils.logOnBlur(logger, source, model.getSync().getFqn(), true);
        service.onBlurSync(source, model, new AsyncCallbackImpl(callback, formBox));
    }

    /** Syncs the form model after executing all the on change methods of the triggered widget. */
    public void onChangeSync(final List<SourceWidget> sources, final FormModelResponse model, final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.processing(), formBox);
        LoggingUtils.logOnChange(logger, sources, model.getSync().getFqn(), true);
        service.onChangeSync(sources, model, new AsyncCallbackImpl(callback, formBox));
    }

    /** Syncs the form model after executing the onClick method of the triggered widget. */
    public void onClickSync(final SourceWidget source, boolean feedback, final FormModelResponse model,
                            final DefaultCallback<FormModelResponse> callback) {
        if (!processing.contains(source)) {
            processing.add(source);
            messages().info(MSGS.processing(), formBox);
            LoggingUtils.logOnClick(logger, source, model.getSync().getFqn(), true);
            service.onClickSync(source, feedback, model, new AsyncCallbackImpl(callback, formBox));
        }
    }

    /** Syncs the form model after executing the onDisplay method of the triggered widget. */
    public void onDisplaySync(final FormModelResponse model, final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.processing(), formBox);
        LoggingUtils.logOnDisplay(logger, SourceWidget.NONE, model.getSync().getFqn(), true);
        service.onDisplaySync(model, new AsyncCallbackImpl(callback, formBox));
    }

    /** Syncs the form model after waiting the onClick execution. */
    public void onFutureCancellation(final String uuid, final DefaultCallback<FeedbackEventData> cb) {
        service.onFutureCancellation(uuid, new BaseCallback<FeedbackEventData>(cb, formBox, true) {
                @Override public void onSuccess(Response<FeedbackEventData> result) {
                    callback.onSuccess(result.getResponse());
                }
            });
    }

    /** Maybe syncs the form model after checking for future pending execution. */
    public void onFutureSync(final String uuid, final DefaultCallback<FeedbackEventData> cb) {
        service.onFutureSync(uuid, new BaseCallback<FeedbackEventData>(cb, formBox, true) {
                @Override public void onSuccess(Response<FeedbackEventData> result) {
                    callback.onSuccess(result.getResponse());
                }
            });
    }

    /** Load next model after "link form" was clicked. */
    public void onLoadSync(@NotNull final SourceWidget source, @NotNull final FormModelResponse model, @Nullable final String pk,
                           @NotNull final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.processing(), formBox);
        LoggingUtils.logLoadSync(logger, source, model.getSync().getFqn(), pk, true);
        service.onLoadSync(source, model, pk, new AsyncCallbackImpl(callback, formBox));
    }

    /** Syncs the form model after executing the onBlur method of the triggered widget. */
    public void onNewLocationSync(final SourceWidget source, double lat, double lng, final FormModelResponse model,
                                  final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.processing(), formBox);
        LoggingUtils.logOnNewLocation(logger, source, model.getSync().getFqn(), true);
        service.onNewLocationSync(source, model, lat, lng, new AsyncCallbackImpl(callback, formBox));
    }

    /** Syncs the form model after executing the onNew method of the triggered widget. */
    public void onNewSync(final SourceWidget source, final FormModelResponse model, String text, final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.processing(), formBox);
        LoggingUtils.logOnClick(logger, source, model.getSync().getFqn(), true);
        service.onNewSync(source, model, text, new AsyncCallbackImpl(callback, formBox));
    }

    /** Syncs the form model after executing the row onChange method of the triggered widget. */
    public void onRowChangeSync(final SourceWidget source, final FormModelResponse model, final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.processing(), formBox);
        LoggingUtils.logOnRowChange(logger, listOf(source), model.getSync().getFqn(), true);
        service.onRowChangeSync(source, model, new AsyncCallbackImpl(callback, formBox));
    }

    /** Syncs the form model after executing the row onSelect method of the triggered widget. */
    public void onRowSelectedSync(final SourceWidget source, final FormModelResponse model, final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.processing(), formBox);
        LoggingUtils.logOnClick(logger, source, model.getSync().getFqn(), true);
        service.onRowSelectedSync(source, model, new AsyncCallbackImpl(callback, formBox));
    }

    /** Submits a Form. */
    public void postForm(final SourceWidget widget, final FormModelResponse response, final String action,
                         final DefaultCallback<FormModelResponse> callback) {
        messages().info(MSGS.saving(), formBox);
        LoggingUtils.logSubmit(logger, response.getSync().getFqn(), action, true);
        service.postForm(widget, response, action, new AsyncCallbackImpl(callback, MSGS.saved(), formBox));
    }

    public void removeProcessing(@NotNull SourceWidget s) {
        processing.remove(s);
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(ClientFormService.class);
}  // end class ClientFormService
