
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.EnumException;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.service.Call;
import tekgenesis.common.service.Status;

/**
 * Internal utility class to handle Results.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public interface Results {

    //~ Methods ......................................................................................................................................

    /** Return an accepted result. */
    <T> Result<T> accepted();

    /** Return an accepted result. */
    <T> Result<T> accepted(@NotNull T value);

    /** Return a bad request result. */
    <T> Result<T> badRequest();

    /** Return a bad request result with given value. */
    <T> Result<T> badRequest(@NotNull Object value);

    /** Return a created result. */
    <T> Result<T> created();

    /** Return a created result. */
    <T> Result<T> created(@NotNull T value);

    /** Raise an exception result with given enum value, under default bad request status. */
    <T, E extends Enum<E> & Enumeration<E, ?> & EnumException<?>> Result<T> exception(@NotNull E error);

    /**
     * Raise an exception result with given enum value formatted with arguments, under default bad
     * request status.
     */
    <T, E extends Enum<E> & Enumeration<E, ?> & EnumException<?>> Result<T> exception(@NotNull E error, Object... args);

    /** Raise an exception result with given enum value, under given {@link Status status}. */
    <T, E extends Enum<E> & Enumeration<E, ?> & EnumException<?>> Result<T> exception(@NotNull Status status, @NotNull E error);

    /**
     * Raise an exception result with given enum value formatted with arguments, under given
     * {@link Status status}.
     */
    <T, E extends Enum<E> & Enumeration<E, ?> & EnumException<?>> Result<T> exception(@NotNull Status status, @NotNull E error, Object... args);

    /** Return a forbidden result. */
    <T> Result<T> forbidden();

    /** Return a forbidden result with given value. */
    <T> Result<T> forbidden(@NotNull Object value);

    /** Return a forward result for handlers routing. */
    <T> Result<T> forward(@NotNull Call call);

    /** Return a raw forward result skipping handlers routing. */
    <T> Result<T> forward(@NotNull String url);

    /** Return a found result. */
    <T> Result<T> found(@NotNull Call call);

    /** Return a result for specified status. */
    <T> Result<T> fromStatus(@NotNull Status status);

    /** Return a result for specified status. */
    <T> Result<T> fromStatus(@NotNull Status status, @NotNull T value);

    /** Return a result for specified redirection status. */
    <T> Result<T> fromStatus(@NotNull Status redirection, @NotNull Call call);

    /** Return an internal server error result. */
    <T> Result<T> internalServerError();

    /** Return an internal server error result with given value. */
    <T> Result<T> internalServerError(@NotNull Object value);

    /** Return a found redirect to login result. */
    <T> Result<T> login();

    /** Return a found redirect to login result including success redirection parameter. */
    <T> Result<T> login(@NotNull String redirection);

    /** Return a found redirect to login result including success redirection parameter. */
    <T> Result<T> login(@NotNull Call redirection);

    /** Return a method not allowed result. */
    <T> Result<T> methodNotAllowed();

    /** Return a moved permanently result. */
    <T> Result<T> movedPermanently(@NotNull Call call);

    /** Return a no content result. */
    <T> Result<T> noContent();

    /** Return a not acceptable result. */
    <T> Result<T> notAcceptable();

    /** Return a not acceptable result with given value. */
    <T> Result<T> notAcceptable(@NotNull Object value);

    /** Return a not found result. */
    <T> Result<T> notFound();

    /** Return a not found result with given value. */
    <T> Result<T> notFound(@NotNull Object value);

    /** Return a not implemented result. */
    <T> Result<T> notImplemented();

    /** Return a not implemented result with given value. */
    <T> Result<T> notImplemented(@NotNull Object value);

    /** Return a not modified result. */
    <T> Result<T> notModified(@NotNull Call call);

    /** Return an ok result. */
    <T> Result<T> ok();

    /** Return an ok result. */
    <T> Result<T> ok(@NotNull T value);

    /** Return a precondition failed result. */
    <T> Result<T> preconditionFailed();

    /** Return a precondition failed result. */
    <T> Result<T> preconditionFailed(@NotNull Object value);

    /** Return a redirect result. */
    <T> Result<T> redirect(@NotNull Call call);

    /** Return with a defined status code. */
    <T> Result<T> status(Status status);

    /** Return with a defined status code. */
    <T> Result<T> status(Status status, @NotNull T value);

    /** Return a temporary redirect result. */
    <T> Result<T> temporaryRedirect(@NotNull Call call);

    /** Return an unauthorized result. */
    <T> Result<T> unauthorized();

    /** Return an unauthorized result with given value. */
    <T> Result<T> unauthorized(@NotNull Object value);
}  // end interface Results
