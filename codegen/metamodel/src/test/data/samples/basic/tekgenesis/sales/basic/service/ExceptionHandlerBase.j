package tekgenesis.sales.basic.service;

import tekgenesis.common.service.Call;
import tekgenesis.service.Factory;
import tekgenesis.service.HandlerInstance;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;
import tekgenesis.service.Results;
import tekgenesis.common.service.Status;
import static tekgenesis.common.service.Method.GET;
import static tekgenesis.common.service.Method.POST;

/** 
 * Generated base class for handler: ExceptionHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class ExceptionHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @NotNull private final Results results;

    //~ Constructors .............................................................................................................

    ExceptionHandlerBase(@NotNull Factory factory) {
        super(factory);
        results = factory.results();
    }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/exceptions/badRequest" */
    @NotNull public abstract Result<String> returnBadRequest();

    /** Invoked for route "/exceptions/badRequestWithObject" */
    @NotNull public abstract Result<String> returnBadRequestWithObject();

    /** Invoked for route "/exceptions/badRequestException" */
    @NotNull public abstract Result<String> exception();

    /** Invoked for route "/exceptions/badRequestExceptionForVoid" */
    @NotNull public abstract Result<Void> exceptionForVoid();

    /** Invoked for route "/exceptions/badRequestExceptionWithLabel" */
    @NotNull public abstract Result<String> exceptionWithMsg(@NotNull String msg);

    /** Invoked for route "/exceptions/badRequestSubroutineValidException" */
    @NotNull public abstract Result<String> subroutineValidException();

    /** Invoked for route "/exceptions/badRequestIntSubroutineValidException" */
    @NotNull public abstract Result<Integer> subroutineIntValidException();

    /** Invoked for route "/exceptions/badRequestSubroutineInvalidException" */
    @NotNull public abstract Result<String> subroutineInvalidException();

    /** Invoked for route "/exceptions/badRequestSubroutineIntInvalidException" */
    @NotNull public abstract Result<Integer> subroutineIntInvalidException();

    /** Raise an application exception result with given HandlerError value, under default bad request {@link Status status}. */
    @NotNull protected <T> Result<T> exception(@NotNull HandlerError error) { return results.exception(error); }

    /** Raise an application exception result with given HandlerError value, formatted with arguments, under default bad request {@link Status status}. */
    @NotNull protected <T> Result<T> exception(@NotNull HandlerError error, @NotNull Object... args) { return results.exception(error, args); }

    /** Raise an application exception result under specified {@link Status status} with given HandlerError value. */
    @NotNull protected <T> Result<T> exception(@NotNull Status status, @NotNull HandlerError error) { return results.exception(status, error); }

    /** Raise an application exception result under specified {@link Status status} with given HandlerError value formatted with arguments. */
    @NotNull protected <T> Result<T> exception(@NotNull Status status, @NotNull HandlerError error, @NotNull Object... args) { return results.exception(status, error, args); }

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(ExceptionHandler.class);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/exceptions/badRequest" {@link ExceptionHandler#returnBadRequest()} */
        @NotNull public static Call returnBadRequest() {
            return new Call(GET, "/exceptions/badRequest", "ExceptionsBadRequest");
        }

        /** Reverse route for "/exceptions/badRequestWithObject" {@link ExceptionHandler#returnBadRequestWithObject()} */
        @NotNull public static Call returnBadRequestWithObject() {
            return new Call(GET, "/exceptions/badRequestWithObject", "ExceptionsBadRequestWithObject");
        }

        /** Reverse route for "/exceptions/badRequestException" {@link ExceptionHandler#exception()} */
        @NotNull public static Call exception() {
            return new Call(GET, "/exceptions/badRequestException", "ExceptionsBadRequestException");
        }

        /** Reverse route for "/exceptions/badRequestExceptionForVoid" {@link ExceptionHandler#exceptionForVoid()} */
        @NotNull public static Call exceptionForVoid() {
            return new Call(POST, "/exceptions/badRequestExceptionForVoid", "ExceptionsBadRequestExceptionForVoid");
        }

        /** Reverse route for "/exceptions/badRequestExceptionWithLabel" {@link ExceptionHandler#exceptionWithMsg(String)} */
        @NotNull public static Call exceptionWithMsg() {
            return new Call(GET, "/exceptions/badRequestExceptionWithLabel", "ExceptionsBadRequestExceptionWithLabel");
        }

        /** Reverse route for "/exceptions/badRequestSubroutineValidException" {@link ExceptionHandler#subroutineValidException()} */
        @NotNull public static Call subroutineValidException() {
            return new Call(GET, "/exceptions/badRequestSubroutineValidException", "ExceptionsBadRequestSubroutineValidException");
        }

        /** Reverse route for "/exceptions/badRequestIntSubroutineValidException" {@link ExceptionHandler#subroutineIntValidException()} */
        @NotNull public static Call subroutineIntValidException() {
            return new Call(GET, "/exceptions/badRequestIntSubroutineValidException", "ExceptionsBadRequestIntSubroutineValidException");
        }

        /** Reverse route for "/exceptions/badRequestSubroutineInvalidException" {@link ExceptionHandler#subroutineInvalidException()} */
        @NotNull public static Call subroutineInvalidException() {
            return new Call(GET, "/exceptions/badRequestSubroutineInvalidException", "ExceptionsBadRequestSubroutineInvalidException");
        }

        /** Reverse route for "/exceptions/badRequestSubroutineIntInvalidException" {@link ExceptionHandler#subroutineIntInvalidException()} */
        @NotNull public static Call subroutineIntInvalidException() {
            return new Call(GET, "/exceptions/badRequestSubroutineIntInvalidException", "ExceptionsBadRequestSubroutineIntInvalidException");
        }

    }
}
