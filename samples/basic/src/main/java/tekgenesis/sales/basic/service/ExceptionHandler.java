
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic.service;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.exception.ApplicationException;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;

import static tekgenesis.sales.basic.service.HandlerError.ERROR_MESSAGE;
import static tekgenesis.sales.basic.service.HandlerError.QUOTE_MESSAGE;

/**
 * User class for Handler: ExceptionHandler
 */
public class ExceptionHandler extends ExceptionHandlerBase {

    //~ Constructors .................................................................................................................................

    ExceptionHandler(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    /** Invoked for route "/exceptions/badRequestException". */
    @NotNull @Override public Result<String> exception() {
        return exception(ERROR_MESSAGE);
    }

    /** Invoked for route "/exceptions/badRequestExceptionForVoid". */
    @NotNull @Override public Result<Void> exceptionForVoid() {
        return exception(ERROR_MESSAGE);
    }

    /** Invoked for route "/exceptions/badRequestExceptionWithLabel". */
    @NotNull @Override public Result<String> exceptionWithMsg(@NotNull String msg) {
        return exception(QUOTE_MESSAGE, msg);
    }

    /** Invoked for route "/exceptions/badRequest". */
    @NotNull @Override public Result<String> returnBadRequest() {
        return badRequest();
    }

    /** Invoked for route "/exceptions/badRequestWithObject". */
    @NotNull @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Result<String> returnBadRequestWithObject() {
        return badRequest("Ooooops!");
    }

    @NotNull @Override public Result<Integer> subroutineIntInvalidException() {
        throw new InvalidApplicationException();
    }

    /** Invoked for route "/exceptions/badRequestIntSubroutineValidException". */
    @NotNull @Override public Result<Integer> subroutineIntValidException() {
        throw ERROR_MESSAGE.exception();
    }

    /** Invoked for route "/exceptions/badRequestSubroutineInvalidException". */
    @NotNull @Override public Result<String> subroutineInvalidException() {
        throw new InvalidApplicationException();
    }

    /** Invoked for route "/exceptions/badRequestSubroutineValidException". */
    @NotNull @Override public Result<String> subroutineValidException() {
        throw ERROR_MESSAGE.exception();
    }

    //~ Inner Classes ................................................................................................................................

    private class InvalidApplicationException extends ApplicationException {
        private InvalidApplicationException() {
            super(State.ACTIVE);  // Any other enumeration (not matching expected HandlerError enum)
        }

        private static final long serialVersionUID = 4963438433284040770L;
    }
}  // end class ExceptionHandler
