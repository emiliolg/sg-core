
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

import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.service.Call;
import tekgenesis.common.service.Status;
import tekgenesis.common.service.etl.MessageConverter;
import tekgenesis.common.service.server.Request;

/**
 * Handler Instance defines a series of handy result methods and exposes request.
 */
@SuppressWarnings({ "InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems" })
public abstract class HandlerInstance implements LoggableInstance {

    //~ Instance Fields ..............................................................................................................................

    /** Service request. */
    @NotNull protected final Request req;

    /** Converters for handling request payload and response content. */
    @NotNull private final List<MessageConverter<?>> converters;

    /** Results factory. */
    @NotNull private final Results results;

    //~ Constructors .................................................................................................................................

    protected HandlerInstance(@NotNull Factory factory) {
        req     = factory.request();
        results = factory.results();

        // Supply default converters for instance
        converters = factory.converters();
    }

    //~ Methods ......................................................................................................................................

    /** Return an accepted result. */
    protected <T> Result<T> accepted() {
        return results.accepted();
    }

    /** Return an accepted result. */
    protected <T> Result<T> accepted(@NotNull T value) {
        return results.accepted(value);
    }

    /** Return a bad request result. */
    protected <T> Result<T> badRequest() {
        return results.badRequest();
    }

    /** Return a bad request result with given value. */
    protected <T> Result<T> badRequest(@NotNull Object value) {
        return results.badRequest(value);
    }

    /** Return a created result. */
    protected <T> Result<T> created() {
        return results.created();
    }

    /** Return a created result. */
    protected <T> Result<T> created(@NotNull T value) {
        return results.created(value);
    }

    /** Return a forbidden result. */
    protected <T> Result<T> forbidden() {
        return results.forbidden();
    }

    /** Return a forbidden result with given value. */
    protected <T> Result<T> forbidden(@NotNull Object value) {
        return results.forbidden(value);
    }

    /** Return a forward result for handlers routing. */
    protected <T> Result<T> forward(@NotNull Call call) {
        return results.forward(call);
    }

    /** Return a raw forward result skipping handlers routing. */
    protected <T> Result<T> forward(@NotNull String url) {
        return results.forward(url);
    }

    /** Return a redirect result. */
    protected <T> Result<T> found(@NotNull Call call) {
        return results.found(call);
    }

    /** Return an internal server error result. */
    protected <T> Result<T> internalServerError() {
        return results.internalServerError();
    }

    /** Return an internal server error result with given value. */
    protected <T> Result<T> internalServerError(@NotNull Object value) {
        return results.internalServerError(value);
    }

    /** Return a method not allowed result. */
    protected <T> Result<T> methodNotAllowed() {
        return results.methodNotAllowed();
    }

    /** Return a moved permanently result. */
    protected <T> Result<T> movedPermanently(@NotNull final Call call) {
        return results.movedPermanently(call);
    }

    /** Return a no content result. */
    protected <T> Result<T> noContent() {
        return results.noContent();
    }

    /** Return a not acceptable result. */
    protected <T> Result<T> notAcceptable() {
        return results.notAcceptable();
    }

    /** Return a not acceptable result with given value. */
    protected <T> Result<T> notAcceptable(@NotNull Object value) {
        return results.notAcceptable(value);
    }

    /** Return a not found result. */
    protected <T> Result<T> notFound() {
        return results.notFound();
    }

    /** Return a not found result with given value. */
    protected <T> Result<T> notFound(@NotNull Object value) {
        return results.notFound(value);
    }

    /** Return a not implemented result. */
    protected <T> Result<T> notImplemented() {
        return results.notImplemented();
    }

    /** Return a not implemented result with given value. */
    protected <T> Result<T> notImplemented(@NotNull Object value) {
        return results.notImplemented(value);
    }

    /** Return a redirect result. */
    protected <T> Result<T> notModified(@NotNull Call call) {
        return results.notModified(call);
    }

    /** Return an ok result. */
    protected <T> Result<T> ok() {
        return results.ok();
    }

    /** Return an ok result. */
    protected <T> Result<T> ok(@NotNull T value) {
        return results.ok(value);
    }

    /** Return a precondition failed result. */
    protected <T> Result<T> preconditionFailed() {
        return results.preconditionFailed();
    }

    /** Return a precondition failed result with given value. */
    protected <T> Result<T> preconditionFailed(@NotNull Object value) {
        return results.preconditionFailed(value);
    }

    /** Return a redirect result. */
    protected <T> Result<T> redirect(@NotNull final Call call) {
        return results.redirect(call);
    }

    /** Return an accepted result. */
    protected <T> Result<T> status(final Status status) {
        return results.status(status);
    }

    /** Return an accepted result. */
    protected <T> Result<T> status(final Status status, @NotNull T value) {
        return results.status(status, value);
    }

    /** Return an unauthorized result. */
    protected <T> Result<T> unauthorized() {
        return results.unauthorized();
    }

    /** Return an unauthorized result with given value. */
    protected <T> Result<T> unauthorized(@NotNull Object value) {
        return results.unauthorized(value);
    }

    /** Add given {@link MessageConverter converter} for handling results. */
    protected final void withConverter(@NotNull MessageConverter<?> converter) {
        converters.add(0, converter);
    }

    /** Return handler converters. */
    @NotNull List<MessageConverter<?>> getConverters() {
        return converters;
    }
}  // end class HandlerInstance
