
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

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.*;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.GenericType;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.InvokerCommand;
import tekgenesis.common.invoker.PathResource;
import tekgenesis.common.media.MediaType;
import tekgenesis.common.service.Call;
import tekgenesis.common.util.Conversions;

import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.common.service.Method.GET;
import static tekgenesis.common.service.Method.POST;

/**
 * Generated remote service class for handler: ProductHandler. Don't modify this as this is an auto
 * generated class that's gets generated every time the meta model file is modified.
 */
@SuppressWarnings(
        {
            "DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods",
            "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable",
            "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor",
            "UnusedParameters"
        }
                 )
public class ProductHandlerRemote {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final HttpInvoker invoker;
    @NotNull private Option<Locale>    locale;

    //~ Constructors .................................................................................................................................

    /** Default service constructor specifying {@link HttpInvoker}. */
    public ProductHandlerRemote(@NotNull HttpInvoker invoker) {
        this.invoker = invoker;
        locale       = of(Context.getContext().getLocale());
    }

    //~ Methods ......................................................................................................................................

    /**
     * Return {@link InvokerCommand command} for remote 'post' invocation on path
     * "/products/body/empty".
     */
    @NotNull public InvokerCommand<String> bodyEmpty() {
        final Call call = Routes.bodyEmpty();
        return resource(call.getUrl()).invoke(POST, String.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'post' invocation on path
     * "/products/body/multiple".
     */
    @NotNull public InvokerCommand<String> bodyMultiple(@NotNull Seq<Product> body) {
        final Call call = Routes.bodyMultiple();
        return resource(call.getUrl()).invoke(POST, String.class, body).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'post' invocation on path
     * "/products/body/simple".
     */
    @NotNull public InvokerCommand<String> bodySimple(@NotNull String body) {
        final Call            call     = Routes.bodySimple();
        final PathResource<?> resource = resource(call.getUrl());
        resource.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        return resource.invoke(POST, String.class, body).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/products/parameters/conversions".
     */
    @NotNull public InvokerCommand<String> conversions(int a, double b, @NotNull BigDecimal c, @NotNull DateOnly d, @NotNull DateTime e,
                                                       @NotNull String f, boolean g, @NotNull State h) {
        final Call            call     = Routes.conversions();
        final PathResource<?> resource = resource(call.getUrl());
        resource.param("a", Conversions.toString(Integers.checkSignedLength("a", a, false, 9)));
        resource.param("b", Conversions.toString(Reals.checkSigned("b", b, false)));
        resource.param("c", Conversions.toString(Decimals.scaleAndCheck("c", c, false, 6, 4)));
        resource.param("d", Conversions.toString(d));
        resource.param("e", Conversions.toString(e));
        resource.param("f", Strings.truncate(f, 25));
        resource.param("g", Conversions.toString(g));
        resource.param("h", Conversions.toString(h));
        return resource.invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/products/$id/details".
     */
    @NotNull public InvokerCommand<Product> details(@NotNull String id) {
        final Call call = Routes.details(id);
        return resource(call.getUrl()).invoke(GET, Product.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/products/failure".
     */
    @NotNull public InvokerCommand<String> failure() {
        final Call call = Routes.failure();
        return resource(call.getUrl()).invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'post' invocation on path
     * "/products/failure".
     */
    @NotNull public InvokerCommand<String> failure(@NotNull Product body) {
        final Call call = Routes.failure();
        return resource(call.getUrl()).invoke(POST, String.class, body).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/products/failureWithMessage".
     */
    @NotNull public InvokerCommand<Integer> failureWithMessage(int method) {
        final Call            call     = Routes.failureWithMessage();
        final PathResource<?> resource = resource(call.getUrl());
        resource.param("method", Conversions.toString(Integers.checkSignedLength("method", method, false, 3)));
        return resource.invoke(GET, Integer.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/products/failureWithMessageStr".
     */
    @NotNull public InvokerCommand<String> failureWithMessageStr(int method) {
        final Call            call     = Routes.failureWithMessageStr();
        final PathResource<?> resource = resource(call.getUrl());
        resource.param("method", Conversions.toString(Integers.checkSignedLength("method", method, false, 3)));
        return resource.invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/products/featured".
     */
    @NotNull public InvokerCommand<Seq<Product>> featured() {
        final Call call = Routes.featured();
        return resource(call.getUrl()).invoke(GET, new GenericType<Seq<Product>>() {}).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/products/headers".
     */
    @NotNull public InvokerCommand<String> headers(@NotNull String contentType, @NotNull String charset) {
        final Call            call     = Routes.headers();
        final PathResource<?> resource = resource(call.getUrl());
        resource.param("contentType", Strings.truncate(contentType, 255));
        resource.param("charset", Strings.truncate(charset, 255));
        return resource.invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/products/cookies/inbound".
     */
    @NotNull public InvokerCommand<String> inboundCookies() {
        final Call call = Routes.inboundCookies();
        return resource(call.getUrl()).invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/products/list/$list".
     */
    @NotNull public InvokerCommand<ProductList> list(@NotNull String list) {
        final Call call = Routes.list(list);
        return resource(call.getUrl()).invoke(GET, ProductList.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/products/localized/message".
     */
    @NotNull public InvokerCommand<String> localizedMessage() {
        final Call call = Routes.localizedMessage();
        return resource(call.getUrl()).invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/products/parameters/multiples".
     */
    @NotNull public InvokerCommand<String> multiples(@NotNull Seq<Integer> a, @NotNull Seq<Double> b, @NotNull Seq<BigDecimal> c,
                                                     @NotNull Seq<DateOnly> d, @NotNull Seq<DateTime> e, @NotNull Seq<String> f,
                                                     @NotNull Seq<Boolean> g, @NotNull Seq<State> h) {
        final Call            call     = Routes.multiples();
        final PathResource<?> resource = resource(call.getUrl());
        resource.param("a", Conversions.toString(Integers.checkSignedLength("a", a, false, 9)));
        if (b != null) resource.param("b", Conversions.toString(Reals.checkSigned("b", b, false)));
        resource.param("c", Conversions.toString(Decimals.scaleAndCheck("c", c, false, 6, 4)));
        resource.param("d", Conversions.toString(d));
        resource.param("e", Conversions.toString(e));
        resource.param("f", Conversions.toString(Strings.truncate(f, 25)));
        resource.param("g", Conversions.toString(g));
        resource.param("h", Conversions.toString(h));
        return resource.invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path
     * "/products/parameters/$id".
     */
    @NotNull public InvokerCommand<String> parameters(int id, @NotNull String from, @Nullable String to, int step) {
        final Call            call     = Routes.parameters(id);
        final PathResource<?> resource = resource(call.getUrl());
        resource.param("from", Strings.truncate(from, 255));
        if (to != null) resource.param("to", Strings.truncate(to, 255));
        resource.param("step", Conversions.toString(Integers.checkSignedLength("step", step, false, 9)));
        return resource.invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'post' invocation on path
     * "/products/$id/related".
     */
    @NotNull public InvokerCommand<Product> related(@NotNull String id, @NotNull Product body) {
        final Call call = Routes.related(id);
        return resource(call.getUrl()).invoke(POST, Product.class, body).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'get' invocation on path "/products/$id".
     */
    @NotNull public InvokerCommand<Product> show(@NotNull String id) {
        final Call call = Routes.show(id);
        return resource(call.getUrl()).invoke(GET, Product.class).withInvocationKey(call.getKey());
    }

    /**
     * Return {@link InvokerCommand command} for remote 'post' invocation on path "/products/$id".
     */
    @NotNull public InvokerCommand<?> update(@NotNull String id, @NotNull Product body) {
        final Call call = Routes.update(id);
        return resource(call.getUrl()).invoke(POST, body).withInvocationKey(call.getKey());
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
        /** Reverse route for "/products/body/empty" {@link ProductHandler#bodyEmpty()}. */
        @NotNull public static Call bodyEmpty() {
            return new Call(POST, "/products/body/empty", "ProductsBodyEmpty");
        }

        /** Reverse route for "/products/body/multiple" {@link ProductHandler#bodyMultiple(Seq)}. */
        @NotNull public static Call bodyMultiple() {
            return new Call(POST, "/products/body/multiple", "ProductsBodyMultiple");
        }

        /** Reverse route for "/products/body/simple" {@link ProductHandler#bodySimple(String)}. */
        @NotNull public static Call bodySimple() {
            return new Call(POST, "/products/body/simple", "ProductsBodySimple");
        }

        /**
         * Reverse route for "/products/parameters/conversions"
         * {@link ProductHandler#conversions(int, double, BigDecimal, DateOnly, DateTime, String, boolean, State)}.
         */
        @NotNull public static Call conversions() {
            return new Call(GET, "/products/parameters/conversions", "ProductsParametersConversions");
        }

        /** Reverse route for "/products/$id/details" {@link ProductHandler#details(String)}. */
        @NotNull public static Call details(@NotNull String id) {
            return new Call(GET, String.format("/products/%s/details", Strings.truncate(id, 255)), "ProductsIdDetails");
        }

        /** Reverse route for "/products/failure" {@link ProductHandler#failure()}. */
        @NotNull public static Call failure() {
            return new Call(GET, "/products/failure", "ProductsFailure");
        }

        /**
         * Reverse route for "/products/failureWithMessage"
         * {@link ProductHandler#failureWithMessage(int)}.
         */
        @NotNull public static Call failureWithMessage() {
            return new Call(GET, "/products/failureWithMessage", "ProductsFailureWithMessage");
        }

        /**
         * Reverse route for "/products/failureWithMessageStr"
         * {@link ProductHandler#failureWithMessageStr(int)}.
         */
        @NotNull public static Call failureWithMessageStr() {
            return new Call(GET, "/products/failureWithMessageStr", "ProductsFailureWithMessageStr");
        }

        /** Reverse route for "/products/featured" {@link ProductHandler#featured()}. */
        @NotNull public static Call featured() {
            return new Call(GET, "/products/featured", "ProductsFeatured");
        }

        /** Reverse route for "/products/headers" {@link ProductHandler#headers(String, String)}. */
        @NotNull public static Call headers() {
            return new Call(GET, "/products/headers", "ProductsHeaders");
        }

        /**
         * Reverse route for "/products/cookies/inbound" {@link ProductHandler#inboundCookies()}.
         */
        @NotNull public static Call inboundCookies() {
            return new Call(GET, "/products/cookies/inbound", "ProductsCookiesInbound");
        }

        /** Reverse route for "/products/list/$list" {@link ProductHandler#list(String)}. */
        @NotNull public static Call list(@NotNull String list) {
            return new Call(GET, String.format("/products/list/%s", Strings.truncate(list, 255)), "ProductsListList");
        }

        /**
         * Reverse route for "/products/localized/message" {@link ProductHandler#localizedMessage()}.
         */
        @NotNull public static Call localizedMessage() {
            return new Call(GET, "/products/localized/message", "ProductsLocalizedMessage");
        }

        /**
         * Reverse route for "/products/parameters/multiples"
         * {@link ProductHandler#multiples(Seq, Seq, Seq, Seq, Seq, Seq, Seq, Seq)}.
         */
        @NotNull public static Call multiples() {
            return new Call(GET, "/products/parameters/multiples", "ProductsParametersMultiples");
        }

        /**
         * Reverse route for "/products/cookies/outbound" {@link ProductHandler#outboundCookies()}.
         */
        @NotNull public static Call outboundCookies() {
            return new Call(GET, "/products/cookies/outbound", "ProductsCookiesOutbound");
        }

        /**
         * Reverse route for "/products/parameters/$id"
         * {@link ProductHandler#parameters(int, String, String, int)}.
         */
        @NotNull public static Call parameters(int id) {
            return new Call(GET, String.format("/products/parameters/%s", Integers.checkSignedLength("id", id, true, 9)), "ProductsParametersId");
        }

        /** Reverse route for "/products/redirect/$code" {@link ProductHandler#redirection(int)}. */
        @NotNull public static Call redirection(int code) {
            return new Call(GET, String.format("/products/redirect/%s", Integers.checkSignedLength("code", code, true, 9)), "ProductsRedirectCode");
        }

        /**
         * Reverse route for "/products/$id/related" {@link ProductHandler#related(String, Product)}.
         */
        @NotNull public static Call related(@NotNull String id) {
            return new Call(POST, String.format("/products/%s/related", Strings.truncate(id, 255)), "ProductsIdRelated");
        }

        /** Reverse route for "/products/$id" {@link ProductHandler#show(String)}. */
        @NotNull public static Call show(@NotNull String id) {
            return new Call(GET, String.format("/products/%s", Strings.truncate(id, 255)), "ProductsId");
        }

        /** Reverse route for "/products/$id" {@link ProductHandler#update(String, Product)}. */
        @NotNull public static Call update(@NotNull String id) {
            return new Call(POST, String.format("/products/%s", Strings.truncate(id, 255)), "ProductsId");
        }
    }  // end class Routes
}  // end class ProductHandlerRemote
