
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic.service;

import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Strings;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.InvokerCommand;
import tekgenesis.common.invoker.PathResource;
import tekgenesis.common.service.Call;

import static tekgenesis.common.core.Option.option;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.service.Method.GET;
import static tekgenesis.common.service.Method.POST;

/**
 * Generated remote service class for handler: ExceptionHandler. Don't modify this as this is an
 * auto generated class that's gets generated every time the meta model file is modified.
 */
@SuppressWarnings(
        {
            "DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ClassWithTooManyMethods",
            "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable",
            "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor",
            "UnusedParameters"
        }
                 )
public class ExceptionHandlerRemote {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final HttpInvoker invoker;
    @NotNull private Option<Locale>    locale;

    //~ Constructors .................................................................................................................................

    /** Default service constructor specifying {@link HttpInvoker}. */
    public ExceptionHandlerRemote(@NotNull HttpInvoker invoker) {
        this.invoker = invoker;
        locale       = some(Context.getContext().getLocale());
    }

    //~ Methods ......................................................................................................................................

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/exceptions/badRequestException".
     */
    @NotNull public InvokerCommand<String> exception() {
        final Call call = Routes.exception();
        return resource(call.getUrl()).invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'post' invocation on path
     * "/exceptions/badRequestExceptionForVoid".
     */
    @NotNull public InvokerCommand<?> exceptionForVoid() {
        final Call call = Routes.exceptionForVoid();
        return resource(call.getUrl()).invoke(POST).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/exceptions/badRequestExceptionWithLabel".
     */
    @NotNull public InvokerCommand<String> exceptionWithMsg(@NotNull String msg) {
        final Call            call     = Routes.exceptionWithMsg();
        final PathResource<?> resource = resource(call.getUrl());
        resource.param("msg", Strings.truncate(msg, 255));
        return resource.invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/exceptions/badRequest".
     */
    @NotNull public InvokerCommand<String> returnBadRequest() {
        final Call call = Routes.returnBadRequest();
        return resource(call.getUrl()).invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/exceptions/badRequestWithObject".
     */
    @NotNull public InvokerCommand<String> returnBadRequestWithObject() {
        final Call call = Routes.returnBadRequestWithObject();
        return resource(call.getUrl()).invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/exceptions/badRequestSubroutineIntInvalidException".
     */
    @NotNull public InvokerCommand<Integer> subroutineIntInvalidException() {
        final Call call = Routes.subroutineIntInvalidException();
        return resource(call.getUrl()).invoke(GET, Integer.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/exceptions/badRequestIntSubroutineValidException".
     */
    @NotNull public InvokerCommand<Integer> subroutineIntValidException() {
        final Call call = Routes.subroutineIntValidException();
        return resource(call.getUrl()).invoke(GET, Integer.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/exceptions/badRequestSubroutineInvalidException".
     */
    @NotNull public InvokerCommand<String> subroutineInvalidException() {
        final Call call = Routes.subroutineInvalidException();
        return resource(call.getUrl()).invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/exceptions/badRequestSubroutineValidException".
     */
    @NotNull public InvokerCommand<String> subroutineValidException() {
        final Call call = Routes.subroutineValidException();
        return resource(call.getUrl()).invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /** Set locale to be used on invocations. */
    public void setLocale(@Nullable Locale l) {
        locale = option(l);
    }

    @NotNull protected PathResource<?> resource(@NotNull String path) {
        final PathResource<?> result = invoker.resource(path);
        if (locale.isPresent()) result.acceptLanguage(locale.get());
        return result;
    }

    //~ Inner Classes ................................................................................................................................

    public static final class Routes {
        /** Reverse route for "/exceptions/badRequestException". */
        @NotNull public static Call exception() {
            return new Call(GET, "/exceptions/badRequestException", "ExceptionsBadRequestException");
        }

        /** Reverse route for "/exceptions/badRequestExceptionForVoid". */
        @NotNull public static Call exceptionForVoid() {
            return new Call(GET, "/exceptions/badRequestExceptionForVoid", "ExceptionsBadRequestExceptionForVoid");
        }

        /** Reverse route for "/exceptions/badRequestExceptionWithLabel". */
        @NotNull public static Call exceptionWithMsg() {
            return new Call(GET, "/exceptions/badRequestExceptionWithLabel", "ExceptionsBadRequestExceptionWithLabel");
        }

        /** Reverse route for "/exceptions/badRequest". */
        @NotNull public static Call returnBadRequest() {
            return new Call(GET, "/exceptions/badRequest", "ExceptionsBadRequest");
        }

        /** Reverse route for "/exceptions/badRequestWithObject". */
        @NotNull public static Call returnBadRequestWithObject() {
            return new Call(GET, "/exceptions/badRequestWithObject", "ExceptionsBadRequestWithObject");
        }

        /** Reverse route for "/exceptions/badRequestSubroutineIntInvalidException". */
        @NotNull public static Call subroutineIntInvalidException() {
            return new Call(GET, "/exceptions/badRequestSubroutineIntInvalidException", "ExceptionsBadRequestSubroutineIntInvalidException");
        }

        /** Reverse route for "/exceptions/badRequestIntSubroutineValidException". */
        @NotNull public static Call subroutineIntValidException() {
            return new Call(GET, "/exceptions/badRequestIntSubroutineValidException", "ExceptionsBadRequestIntSubroutineValidException");
        }

        /** Reverse route for "/exceptions/badRequestSubroutineInvalidException". */
        @NotNull public static Call subroutineInvalidException() {
            return new Call(GET, "/exceptions/badRequestSubroutineInvalidException", "ExceptionsBadRequestSubroutineInvalidException");
        }

        /** Reverse route for "/exceptions/badRequestSubroutineValidException". */
        @NotNull public static Call subroutineValidException() {
            return new Call(GET, "/exceptions/badRequestSubroutineValidException", "ExceptionsBadRequestSubroutineValidException");
        }
    }  // end class Routes
}  // end class ExceptionHandlerRemote
