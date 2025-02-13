
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic.service;

import java.math.BigDecimal;
import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.InvokerCommand;
import tekgenesis.common.invoker.PathResource;
import tekgenesis.common.service.Call;
import tekgenesis.service.html.Html;

import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.common.service.Method.GET;

/**
 * Generated remote service class for handler: ReturnTypesHandler. Don't modify this as this is an
 * auto generated class that's gets generated every time the meta model file is modified.
 */
@SuppressWarnings(
        {
            "DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods",
            "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable",
            "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor",
            "UnusedParameters"
        }
                 )
public class ReturnTypesHandlerRemote {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final HttpInvoker invoker;
    @NotNull private Option<Locale>    locale;

    //~ Constructors .................................................................................................................................

    /** Default service constructor specifying {@link HttpInvoker}. */
    public ReturnTypesHandlerRemote(@NotNull HttpInvoker invoker) {
        this.invoker = invoker;
        locale       = of(Context.getContext().getLocale());
    }

    //~ Methods ......................................................................................................................................

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/return/Any". */
    @NotNull public InvokerCommand<byte[]> returnAny() {
        final Call call = Routes.returnAny();
        return resource(call.getUrl()).invoke(GET, byte[].class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path "/return/Boolean".
     */
    @NotNull public InvokerCommand<Boolean> returnBoolean() {
        final Call call = Routes.returnBoolean();
        return resource(call.getUrl()).invoke(GET, Boolean.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/return/Date". */
    @NotNull public InvokerCommand<DateOnly> returnDate() {
        final Call call = Routes.returnDate();
        return resource(call.getUrl()).invoke(GET, DateOnly.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path "/return/DateTime".
     */
    @NotNull public InvokerCommand<DateTime> returnDateTime() {
        final Call call = Routes.returnDateTime();
        return resource(call.getUrl()).invoke(GET, DateTime.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path "/return/Decimal".
     */
    @NotNull public InvokerCommand<BigDecimal> returnDecimal() {
        final Call call = Routes.returnDecimal();
        return resource(call.getUrl()).invoke(GET, BigDecimal.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/return/Enum". */
    @NotNull public InvokerCommand<State> returnEnum() {
        final Call call = Routes.returnEnum();
        return resource(call.getUrl()).invoke(GET, State.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/return/Html". */
    @NotNull public InvokerCommand<Html> returnHtml() {
        final Call call = Routes.returnHtml();
        return resource(call.getUrl()).invoke(GET, Html.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/return/Int". */
    @NotNull public InvokerCommand<Integer> returnInt() {
        final Call call = Routes.returnInt();
        return resource(call.getUrl()).invoke(GET, Integer.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/return/Real". */
    @NotNull public InvokerCommand<Double> returnReal() {
        final Call call = Routes.returnReal();
        return resource(call.getUrl()).invoke(GET, Double.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/return/Type". */
    @NotNull public InvokerCommand<Product> returnType() {
        final Call call = Routes.returnType();
        return resource(call.getUrl()).invoke(GET, Product.class).withInvocationKey(call.getKey());
    }

    /** Set locale to be used on invocations. */
    public void setLocale(@Nullable Locale l) {
        locale = ofNullable(l);
    }

    @NotNull protected PathResource<?> resource(@NotNull String path) {
        final PathResource<?> result = invoker.resource(path);
        if (locale.isPresent()) result.acceptLanguage(locale.get());
        return result;
    }

    //~ Inner Classes ................................................................................................................................

    public static final class Routes {
        /** Reverse route for "/return/Any" {@link ReturnTypesHandler#returnAny()}. */
        @NotNull public static Call returnAny() {
            return new Call(GET, "/return/Any", "ReturnAny");
        }

        /** Reverse route for "/return/Boolean" {@link ReturnTypesHandler#returnBoolean()}. */
        @NotNull public static Call returnBoolean() {
            return new Call(GET, "/return/Boolean", "ReturnBoolean");
        }

        /** Reverse route for "/return/Date" {@link ReturnTypesHandler#returnDate()}. */
        @NotNull public static Call returnDate() {
            return new Call(GET, "/return/Date", "ReturnDate");
        }

        /** Reverse route for "/return/DateTime" {@link ReturnTypesHandler#returnDateTime()}. */
        @NotNull public static Call returnDateTime() {
            return new Call(GET, "/return/DateTime", "ReturnDateTime");
        }

        /** Reverse route for "/return/Decimal" {@link ReturnTypesHandler#returnDecimal()}. */
        @NotNull public static Call returnDecimal() {
            return new Call(GET, "/return/Decimal", "ReturnDecimal");
        }

        /** Reverse route for "/return/Enum" {@link ReturnTypesHandler#returnEnum()}. */
        @NotNull public static Call returnEnum() {
            return new Call(GET, "/return/Enum", "ReturnEnum");
        }

        /** Reverse route for "/return/Html" {@link ReturnTypesHandler#returnHtml()}. */
        @NotNull public static Call returnHtml() {
            return new Call(GET, "/return/Html", "ReturnHtml");
        }

        /** Reverse route for "/return/Int" {@link ReturnTypesHandler#returnInt()}. */
        @NotNull public static Call returnInt() {
            return new Call(GET, "/return/Int", "ReturnInt");
        }

        /** Reverse route for "/return/Real" {@link ReturnTypesHandler#returnReal()}. */
        @NotNull public static Call returnReal() {
            return new Call(GET, "/return/Real", "ReturnReal");
        }

        /** Reverse route for "/return/Resource" {@link ReturnTypesHandler#returnResource()}. */
        @NotNull public static Call returnResource() {
            return new Call(GET, "/return/Resource", "ReturnResource");
        }

        /** Reverse route for "/return/Type" {@link ReturnTypesHandler#returnType()}. */
        @NotNull public static Call returnType() {
            return new Call(GET, "/return/Type", "ReturnType");
        }

        /** Reverse route for "/return/Void" {@link ReturnTypesHandler#returnVoid()}. */
        @NotNull public static Call returnVoid() {
            return new Call(GET, "/return/Void", "ReturnVoid");
        }
    }  // end class Routes
}  // end class ReturnTypesHandlerRemote
