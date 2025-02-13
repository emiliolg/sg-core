
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.EnumException;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.service.ApplicationExceptionResult;
import tekgenesis.common.service.Call;
import tekgenesis.common.service.Status;
import tekgenesis.common.service.etl.MessageConverter;
import tekgenesis.common.service.server.Response;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Constants.REDIRECTION;
import static tekgenesis.common.service.Status.*;
import static tekgenesis.security.shiro.web.URLConstants.LOGIN_URI;
import static tekgenesis.service.ResultImpl.EmptyResult.*;

/**
 * Results factory class.
 */
public class ResultFactory implements Results {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final List<MessageConverter<?>> converters;
    @NotNull private final Response                  response;

    //~ Constructors .................................................................................................................................

    ResultFactory(@NotNull final Response response, @NotNull final List<MessageConverter<?>> converters) {
        this.response   = response;
        this.converters = converters;
    }

    //~ Methods ......................................................................................................................................

    @Override public <T> Result<T> accepted() {
        return empty(ACCEPTED);
    }

    @Override public <T> Result<T> accepted(@NotNull T value) {
        return some(ACCEPTED, value);
    }

    @Override public <T> Result<T> badRequest() {
        return empty(BAD_REQUEST);
    }

    @Override public <T> Result<T> badRequest(@NotNull Object value) {
        return some(BAD_REQUEST, value);
    }

    @Override public <T> Result<T> created() {
        return empty(CREATED);
    }

    @Override public <T> Result<T> created(@NotNull T value) {
        return some(CREATED, value);
    }

    /** Raise an exception result with given enum value, under default bad request status. */
    @Override public <T, E extends Enum<E> & Enumeration<E, ?> & EnumException<?>> Result<T> exception(@NotNull E error) {
        return raise(BAD_REQUEST, error, error.label());
    }

    /**
     * Raise an exception result with given enum value formatted with arguments, under default bad
     * request status.
     */
    @Override public <T, E extends Enum<E> & Enumeration<E, ?> & EnumException<?>> Result<T> exception(@NotNull E error, Object... args) {
        return raise(BAD_REQUEST, error, error.label(args));
    }

    /** Raise an exception result with given enum value, under given {@link Status status}. */
    @Override public <T, E extends Enum<E> & Enumeration<E, ?> & EnumException<?>> Result<T> exception(@NotNull Status status, @NotNull E error) {
        return raise(status, error, error.label());
    }

    /**
     * Raise an exception result with given enum value formatted with arguments, under given
     * {@link Status status}.
     */
    @Override public <T, E extends Enum<E> & Enumeration<E, ?> & EnumException<?>> Result<T> exception(@NotNull Status status, @NotNull E error,
                                                                                                       Object... args) {
        return raise(status, error, error.label(args));
    }

    @Override public <T> Result<T> forbidden() {
        return empty(FORBIDDEN);
    }

    @Override public <T> Result<T> forbidden(@NotNull Object value) {
        return some(FORBIDDEN, value);
    }

    @Override public <T> Result<T> forward(@NotNull Call call) {
        return forwarding(call.getUrl(), true);
    }

    @Override public <T> Result<T> forward(@NotNull String url) {
        return forwarding(url, false);
    }

    @Override public <T> Result<T> found(@NotNull Call call) {
        return redirection(FOUND, call.getUrl());
    }

    @Override public <T> Result<T> fromStatus(@NotNull Status status) {
        return empty(status);
    }

    @Override public <T> Result<T> fromStatus(@NotNull Status status, @NotNull T value) {
        return some(status, value);
    }

    @Override public <T> Result<T> fromStatus(@NotNull Status redirection, @NotNull Call call) {
        return redirection(redirection, call.getUrl());
    }

    @Override public <T> Result<T> internalServerError() {
        return empty(INTERNAL_SERVER_ERROR);
    }

    @Override public <T> Result<T> internalServerError(@NotNull Object value) {
        return some(INTERNAL_SERVER_ERROR, value);
    }

    @Override public <T> Result<T> login() {
        return sgLogin("");
    }

    @Override public <T> Result<T> login(@NotNull String redirection) {
        return sgLogin(redirection);
    }

    @Override public <T> Result<T> login(@NotNull Call redirection) {
        return sgLogin(redirection.getUrl());
    }

    @Override public <T> Result<T> methodNotAllowed() {
        return empty(METHOD_NOT_ALLOWED);
    }

    @Override public <T> Result<T> movedPermanently(@NotNull Call call) {
        return redirection(MOVED_PERMANENTLY, call.getUrl());
    }

    @Override public <T> Result<T> noContent() {
        return empty(NO_CONTENT);
    }

    @Override public <T> Result<T> notAcceptable() {
        return empty(NOT_ACCEPTABLE);
    }

    @Override public <T> Result<T> notAcceptable(@NotNull Object value) {
        return some(NOT_ACCEPTABLE, value);
    }

    @Override public <T> Result<T> notFound() {
        return empty(NOT_FOUND);
    }

    @Override public <T> Result<T> notFound(@NotNull Object value) {
        return some(NOT_FOUND, value);
    }

    @Override public <T> Result<T> notImplemented() {
        return empty(NOT_IMPLEMENTED);
    }

    @Override public <T> Result<T> notImplemented(@NotNull Object value) {
        return some(NOT_IMPLEMENTED, value);
    }

    @Override public <T> Result<T> notModified(@NotNull Call call) {
        return redirection(NOT_MODIFIED, call.getUrl());
    }

    @Override public <T> Result<T> ok() {
        return empty(OK);
    }

    @Override public <T> Result<T> ok(@NotNull T content) {
        return some(OK, content);
    }

    @Override public <T> Result<T> preconditionFailed() {
        return empty(PRECONDITION_FAILED);
    }

    @Override public <T> Result<T> preconditionFailed(@NotNull Object value) {
        return some(PRECONDITION_FAILED, value);
    }

    @Override public <T> Result<T> redirect(@NotNull Call call) {
        return redirection(SEE_OTHER, call.getUrl());
    }

    @Override public <T> Result<T> status(Status status) {
        return empty(status);
    }

    @Override public <T> Result<T> status(Status status, @NotNull T value) {
        return some(status, value);
    }

    @Override public <T> Result<T> temporaryRedirect(@NotNull Call call) {
        return redirection(TEMPORARY_REDIRECT, call.getUrl());
    }

    @Override public <T> Result<T> unauthorized() {
        return empty(UNAUTHORIZED);
    }

    @Override public <T> Result<T> unauthorized(@NotNull Object value) {
        return some(UNAUTHORIZED, value);
    }

    private <T> Result<T> empty(@NotNull Status status) {
        return cast(new EmptyResult(status, response));
    }

    private <T> Result<T> forwarding(@NotNull String url, boolean routing) {
        return cast(new ForwardResult(response, url, routing));
    }

    private <T> Result<T> raise(@NotNull Status status, @NotNull Enum<?> error, String label) {
        return cast(new ObjectResult(status, response, converters, new ApplicationExceptionResult(error, label)));
    }

    private <T> Result<T> redirection(@NotNull Status status, @NotNull String url) {
        return cast(new RedirectResult(status, response, url));
    }

    private <T> Result<T> sgLogin(@NotNull String redirection) {
        final String uri = LOGIN_URI + (isEmpty(redirection) ? "" : "?" + REDIRECTION + "=" + redirection);
        return cast(new RedirectResult(FOUND, response, uri));
    }

    private <T> Result<T> some(@NotNull Status status, @NotNull Object content) {
        return cast(new ObjectResult(status, response, converters, content));
    }
}  // end class ResultFactory
