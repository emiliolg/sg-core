
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Response object wrapper, for sending exceptions to client side.
 */
@SuppressWarnings("FieldMayBeFinal")
public class Response<T extends Serializable> implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private boolean authenticated;

    private ResponseError error;

    private ResponseException exception;
    private T                 response;

    //~ Constructors .................................................................................................................................

    /** GWT Empty Constructor. */
    protected Response() {
        error     = null;
        exception = null;
        response  = null;
    }

    private Response(@Nullable ResponseException exception, @Nullable T response) {
        this.exception = exception;
        error          = null;
        this.response  = response;
        authenticated  = true;
    }

    private Response(@Nullable ResponseError error, @Nullable T response, boolean authenticated) {
        this.error         = error;
        exception          = null;
        this.response      = response;
        this.authenticated = authenticated;
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if response contains an error. */
    public boolean hasError() {
        return error != null;
    }

    /** Returns true if response contains an exception. */
    public boolean hasException() {
        return exception != null;
    }

    /** Returns true response is authenticated. */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /** Return response error. */
    public ResponseError getError() {
        return error;
    }

    /** Return response exception. */
    public ResponseException getException() {
        return exception;
    }

    /** Return original response. */
    public T getResponse() {
        return response;
    }

    //~ Methods ......................................................................................................................................

    /** Creates an error response. */
    public static <T extends Serializable> Response<T> error(@NotNull final ResponseError error) {
        return new Response<>(error, null, true);
    }

    /** Creates an exception response. */
    public static <T extends Serializable> Response<T> exception(@NotNull final ResponseException exception) {
        return new Response<>(exception, null);
    }

    /** Creates a simple response. */
    public static <T extends Serializable> Response<T> success(@NotNull final T response) {
        return new Response<>(null, response, true);
    }

    /** Creates an unthenticated error response. */
    public static <T extends Serializable> Response<T> unauthenticated(@NotNull final ResponseError error) {
        return new Response<>(error, null, false);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8995633027600760439L;
}  // end class Response
