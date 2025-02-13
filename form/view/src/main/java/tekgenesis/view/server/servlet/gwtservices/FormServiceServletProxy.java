
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet.gwtservices;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.apache.shiro.ShiroException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.security.PermissionException;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.exception.SuiGenerisException;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.service.HeaderNames;
import tekgenesis.form.ExecutionFeedback;
import tekgenesis.form.exception.UserOperationException;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.serializer.GwtSerializationWhiteList;
import tekgenesis.view.client.service.Feedback;
import tekgenesis.view.shared.feedback.FeedbackEventData;
import tekgenesis.view.shared.response.*;
import tekgenesis.view.shared.service.FormService;

import static java.lang.Boolean.getBoolean;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.core.Constants.*;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.view.server.servlet.concurrent.FutureExecution.attempt;
import static tekgenesis.view.server.servlet.concurrent.FutureExecution.await;
import static tekgenesis.view.server.servlet.concurrent.FutureExecution.cancel;
import static tekgenesis.view.server.servlet.concurrent.FutureExecution.feedback;
import static tekgenesis.view.shared.response.Response.error;
import static tekgenesis.view.shared.response.Response.exception;
import static tekgenesis.view.shared.response.Response.success;
import static tekgenesis.view.shared.response.Response.unauthenticated;
import static tekgenesis.view.shared.response.ResponseError.deepest;

/**
 * Proxy of {@link FormServiceServlet} to handle authorization and exceptions.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public abstract class FormServiceServletProxy extends RemoteServiceServlet implements FormService {

    //~ Instance Fields ..............................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private final ApplicationProps.PathFilter pathFilter = Context.getProperties("forms", ApplicationProps.PathFilter.class);

    //~ Methods ......................................................................................................................................

    @Override public GwtSerializationWhiteList _whiteList(final GwtSerializationWhiteList o) {
        return o;
    }

    @Override public void addFavorite(@NotNull String formLink, int newIdx) {
        handleAddFavorite(formLink, newIdx);
    }

    @Override public Response<BootResponse> boot() {
        return success(handleBoot());
    }

    @Override public Response<FormModelResponse> cancelForm(@NotNull final SourceWidget widget, @NotNull final FormModelResponse request) {
        return invoke(() -> handleCancelForm(widget, request));
    }

    @Override public Response<FormModelResponse> deleteForm(@NotNull final SourceWidget widget, @NotNull final FormModelResponse request) {
        return invoke(() -> handleDeleteForm(widget, request));
    }

    @Override public Response<FormModelResponse> deprecateForm(final boolean deprecate, @NotNull final FormModelResponse request) {
        return invoke(() -> handleDeprecateForm(deprecate, request));
    }

    @Override public Response<SwipeFetchResponse> fetchSwipeModels(@NotNull final FormModelResponse response,
                                                                   @NotNull final List<Integer>     fetchIndexes,
                                                                   @NotNull final String            loaderClassName) {
        return success(handleFetchSwipeModels(response, fetchIndexes, loaderClassName));
    }

    @Override
    @SuppressWarnings("EmptyMethod")
    public void keepAlive() {
        // Do nothing, its just a ping to reset session timeout.
    }

    @Override public Response<FormModelResponse> lazyFetchSync(@NotNull final SourceWidget source, int offset, int limit,
                                                               @NotNull final FormModelResponse request) {
        return invoke(() -> handleLazyFetchSync(source, offset, limit, request));
    }

    @Override public Response<FormModelResponse> loadFormInstance(@NotNull final String formId, @Nullable final String pk,
                                                                  @Nullable final String parameters) {
        return invoke(() -> handleLoadFormInstance(formId, pk, parameters));
    }

    @Override public Response<FormModelResponse> onAbstractSync(@NotNull final SourceWidget delegate, @NotNull final SourceWidget source,
                                                                @NotNull final FormModelResponse request) {
        return invoke(() -> handleOnAbstractSync(delegate, source, request));
    }

    @Override public Response<FormModelResponse> onBlurSync(@NotNull final SourceWidget source, @NotNull final FormModelResponse request) {
        return invoke(() -> handleOnBlurSync(source, request));
    }

    @Override public Response<FormModelResponse> onChangeSync(@NotNull final List<SourceWidget> sources, @NotNull final FormModelResponse request) {
        return invoke(() -> handleOnChangeSync(sources, request));
    }

    @Override public Response<FormModelResponse> onClickSync(@NotNull final SourceWidget source, boolean feedback,
                                                             @NotNull final FormModelResponse request) {
        final Option<ExecutionFeedback> execution = feedback(feedback);
        return invoke(await(() -> handleOnClickSync(source, request, execution), execution));
    }

    @Override public Response<FormModelResponse> onDisplaySync(@NotNull final FormModelResponse request) {
        return invoke(() -> handleOnDisplaySync(request));
    }

    @Override public Response<FeedbackEventData> onFutureCancellation(@NotNull String uuid) {
        return success(cancel(uuid));
    }

    @Override public Response<FeedbackEventData> onFutureSync(@NotNull final String uuid) {
        return success(attempt(uuid));
    }

    @Override public Response<FormModelResponse> onLoadSync(@NotNull final SourceWidget source, @NotNull final FormModelResponse request,
                                                            @Nullable final String pk) {
        return invoke(() -> handleLoadSync(source, pk, request));
    }

    @Override public Response<FormModelResponse> onMethodInvocation(@NotNull final String method, @NotNull final FormModelResponse request) {
        return invoke(() -> handleOnMethodInvocation(method, request));
    }

    @Override public Response<FormModelResponse> onNewLocationSync(@NotNull final SourceWidget source, @NotNull final FormModelResponse request,
                                                                   double lat, double lng) {
        return invoke(() -> handleOnNewLocaBlurSync(source, request, lat, lng));
    }

    @Override public Response<FormModelResponse> onNewSync(@NotNull final SourceWidget source, @NotNull final FormModelResponse request,
                                                           @NotNull final String text) {
        return invoke(() -> handleOnNewSync(source, text, request));
    }

    @Override public Response<FormModelResponse> onRowChangeSync(@NotNull final SourceWidget source, @NotNull final FormModelResponse request) {
        return invoke(() -> handleOnRowChangeSync(source, request));
    }

    @Override public Response<FormModelResponse> onRowSelectedSync(@NotNull final SourceWidget source, @NotNull final FormModelResponse request) {
        return invoke(() -> handleOnRowSelectedSync(source, request));
    }

    @Nullable @Override public Response<FormModelResponse> ping() {
        if (!SecurityUtils.getSession().isAuthenticated()) return unauthenticated(ResponseError.EMPTY);
        return null;
    }

    @Override public void postFeedback(@NotNull Feedback feedback) {
        handleFeedback(feedback);
    }

    @Override public Response<FormModelResponse> postForm(@NotNull final SourceWidget widget, @NotNull final FormModelResponse request,
                                                          @NotNull final String action) {
        return invoke(() -> handlePostForm(widget, request, action));
    }

    @Override public void removeFavorite(int idx) {
        handleRemoveFavorite(idx);
    }

    @Override public void swapFavorite(int a, int b) {
        handleSwapFavorites(a, b);
    }

    @Override public Response<MailValidationResponse> validateEmail(String mail, boolean checkAddress) {
        return success(handleValidateEmail(mail, checkAddress));
    }

    @Override public Response<FavoriteResponse> getFavorites() {
        return success(invokeInTransaction(this::handleGetFavorites));
    }

    @Override public Response<MenuResponse> getMenu(@Nullable String fqn) {
        return success(handleGetMenu(fqn));
    }

    @Override public void setOrgUnit(@NotNull final String ouName) {
        handleSetOrgUnit(ouName);
    }

    @Override public Response<OrgUnitsResponse> getOrgUnits() {
        return success(handleGetOrgUnits());
    }

    protected abstract void handleFeedback(@NotNull final Feedback feedback);
    abstract void handleAddFavorite(@NotNull String formLink, int newIdx);
    abstract BootResponse handleBoot();
    abstract FormModelResponse handleCancelForm(@NotNull final SourceWidget widget, FormModelResponse response);
    abstract FormModelResponse handleDeleteForm(@NotNull final SourceWidget widget, FormModelResponse response);
    abstract FormModelResponse handleDeprecateForm(boolean deprecate, FormModelResponse response);
    abstract SwipeFetchResponse handleFetchSwipeModels(FormModelResponse response, List<Integer> fetchIndexes, String loaderClassName);
    abstract FavoriteResponse handleGetFavorites();
    abstract MenuResponse handleGetMenu(@Nullable final String menuFqn);
    abstract OrgUnitsResponse handleGetOrgUnits();
    abstract FormModelResponse handleLazyFetchSync(final SourceWidget source, int offset, int limit, FormModelResponse response);
    abstract FormModelResponse handleLoadFormInstance(String formId, @Nullable String pk, @Nullable String parameters);
    abstract FormModelResponse handleLoadSync(@NotNull final SourceWidget source, @Nullable final String pk,
                                              @NotNull final FormModelResponse response);
    abstract FormModelResponse handleOnAbstractSync(final SourceWidget delegate, final SourceWidget source, FormModelResponse response);
    abstract FormModelResponse handleOnBlurSync(final SourceWidget source, FormModelResponse response);
    abstract FormModelResponse handleOnChangeSync(final List<SourceWidget> sources, FormModelResponse response);
    abstract FormModelResponse handleOnClickSync(final SourceWidget source, FormModelResponse response, Option<ExecutionFeedback> feedback);
    abstract FormModelResponse handleOnDisplaySync(@NotNull FormModelResponse response);
    abstract FormModelResponse handleOnMethodInvocation(@NotNull final String method, @NotNull final FormModelResponse response);
    abstract FormModelResponse handleOnNewLocaBlurSync(SourceWidget source, FormModelResponse response, double lat, double lng);
    abstract FormModelResponse handleOnNewSync(final SourceWidget source, final String text, FormModelResponse response);
    abstract FormModelResponse handleOnRowChangeSync(@NotNull SourceWidget source, @NotNull FormModelResponse response);
    abstract FormModelResponse handleOnRowSelectedSync(@NotNull SourceWidget source, @NotNull FormModelResponse response);
    abstract FormModelResponse handlePostForm(@NotNull SourceWidget source, @NotNull FormModelResponse response, @NotNull String action);
    abstract void handleRemoveFavorite(int idx);
    abstract void handleSetOrgUnit(@NotNull final String ouName);
    abstract void handleSwapFavorites(int a, int b);
    abstract MailValidationResponse handleValidateEmail(String mail, boolean checkAddress);

    Response<FormModelResponse> invoke(@NotNull final ServerMethod response) {
        if (!checkFilter()) return error(ResponseError.EMPTY);

        // Temporary!!!! TO BE REMOVED (lucas)
        if (isNotEmpty((String) getServletContext().getAttribute(CURRENT_TEST_ID)))
            SecurityUtils.getSession().authenticate(SHIRO_ADMIN_USER, SHIRO_ADMIN_PASS);

        return invokeInTransaction(() -> doInvoke(response));
    }

    private boolean checkFilter() {
        if (!isEmpty(pathFilter.allowed)) {
            for (final String allowedPattern : pathFilter.allowed.split(",")) {
                final HttpServletRequest request         = getThreadLocalRequest();
                final String             clientIpAddress = notEmpty(request.getHeader(HeaderNames.X_FORWARD_FOR), request.getRemoteAddr());
                if (clientIpAddress.matches(allowedPattern)) return true;
            }
            return false;
        }
        return true;
    }

    //~ Methods ......................................................................................................................................

    @SuppressWarnings({ "ThrowableResultOfMethodCallIgnored", "MethodWithMultipleReturnPoints" })
    static Response<FormModelResponse> doInvoke(ServerMethod response) {
        try {
            return success(response.exec());
        }
        catch (final ShiroException e) {
            return unauthenticated(ResponseError.EMPTY);
        }
        catch (final PermissionException e) {
            return handlePermissionException(e);
        }
        catch (final UserOperationException e) {
            return handleException(e, getBoolean(SUIGEN_DEVMODE));
        }
        catch (final SuiGenerisException e) {
            return handleSuiGenerisException(e);
        }
        catch (final Exception e) {
            final Throwable deepest = deepest(e);
            if (deepest instanceof SuiGenerisException) return handleSuiGenerisException((SuiGenerisException) deepest);
            else if (deepest instanceof PermissionException) return handlePermissionException((PermissionException) deepest);
            else if (deepest instanceof UserOperationException) return handleException(e, getBoolean(SUIGEN_DEVMODE));
            else {
                final Option<InvocationTargetException> targetException = anyInvocationTargetExceptionIn(e);
                if (targetException.isPresent() && targetException.get().getCause() != null)
                    return handleException(targetException.get().getCause(), true);
                else return handleException(e, true);
            }
        }
    }  // end method doInvoke

    /**
     * Eliminates InvocationTargetExceptions from stacks. Lots of exceptions are wrapped in
     * InvocationTarget ones, just removing them
     */
    @NotNull private static Option<InvocationTargetException> anyInvocationTargetExceptionIn(@NotNull final Exception e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
            if (cause instanceof InvocationTargetException) return some((InvocationTargetException) cause);
        }
        return Option.empty();
    }

    /** Log and pretty print exception. */
    private static Response<FormModelResponse> handleException(Throwable e, boolean logs) {
        if (logs) logger.error(e);

        final ResponseError fail = new ResponseError(e, getBoolean(SUIGEN_DEVMODE));
        return error(fail.withClass(SourcePreviewer.preview(fail, System.getProperty(SUIGEN_SOURCES))));
    }

    private static Response<FormModelResponse> handlePermissionException(PermissionException e) {
        if (SecurityUtils.getSession().getPrincipal().isSystem()) {
            logger.info("Returning unauthenticated response for system user.");
            return unauthenticated(ResponseError.EMPTY);
        }

        logger.info(e.getMessage());
        return exception(new ResponseException(e.getMessage()));
    }

    private static Response<FormModelResponse> handleSuiGenerisException(SuiGenerisException e) {
        logger.debug(e);

        return exception(new ResponseException(e.getMessage()));
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger           = getLogger(FormServiceServletProxy.class);
    private static final long   serialVersionUID = 2380163346876072962L;
}  // end class FormServiceServletProxy
