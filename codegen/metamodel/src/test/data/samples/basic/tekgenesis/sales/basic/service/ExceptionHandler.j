package tekgenesis.sales.basic.service;

import tekgenesis.service.Factory;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;

/** User class for Handler: ExceptionHandler */
public class ExceptionHandler
    extends ExceptionHandlerBase
{

    //~ Constructors .............................................................................................................

    ExceptionHandler(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/exceptions/badRequest" */
    @Override @NotNull public Result<String> returnBadRequest() { return notImplemented(); }

    /** Invoked for route "/exceptions/badRequestWithObject" */
    @Override @NotNull public Result<String> returnBadRequestWithObject() { return notImplemented(); }

    /** Invoked for route "/exceptions/badRequestException" */
    @Override @NotNull public Result<String> exception() { return notImplemented(); }

    /** Invoked for route "/exceptions/badRequestExceptionForVoid" */
    @Override @NotNull public Result<Void> exceptionForVoid() { return notImplemented(); }

    /** Invoked for route "/exceptions/badRequestExceptionWithLabel" */
    @Override @NotNull public Result<String> exceptionWithMsg(@NotNull String msg) { return notImplemented(); }

    /** Invoked for route "/exceptions/badRequestSubroutineValidException" */
    @Override @NotNull public Result<String> subroutineValidException() { return notImplemented(); }

    /** Invoked for route "/exceptions/badRequestIntSubroutineValidException" */
    @Override @NotNull public Result<Integer> subroutineIntValidException() { return notImplemented(); }

    /** Invoked for route "/exceptions/badRequestSubroutineInvalidException" */
    @Override @NotNull public Result<String> subroutineInvalidException() { return notImplemented(); }

    /** Invoked for route "/exceptions/badRequestSubroutineIntInvalidException" */
    @Override @NotNull public Result<Integer> subroutineIntInvalidException() { return notImplemented(); }

}
