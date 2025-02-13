
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
import java.nio.charset.Charset;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.media.Mime;
import tekgenesis.common.service.Call;
import tekgenesis.common.service.HeaderNames;
import tekgenesis.common.service.Status;
import tekgenesis.common.service.cookie.Cookie;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;

import static java.lang.String.format;

import static tekgenesis.common.core.Constants.TEKGENESIS_DOMAIN;
import static tekgenesis.common.core.Times.SECONDS_HOUR;
import static tekgenesis.sales.basic.State.ACTIVE;
import static tekgenesis.sales.basic.service.Products.*;

/**
 * User class for Handler: ProductHandler
 */
@SuppressWarnings({ "MagicNumber", "DuplicateStringLiteralInspection" })
public class ProductHandler extends ProductHandlerBase {

    //~ Constructors .................................................................................................................................

    ProductHandler(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    /** Invoked for route "/products/body/empty". */
    @NotNull @Override public Result<String> bodyEmpty() {
        return ok("empty");
    }

    /** Invoked for route "/products/body/multiple". */
    @NotNull @Override public Result<String> bodyMultiple(@NotNull Seq<Product> body) {
        return ok(String.valueOf(body.size()));
    }

    /** Invoked for route "/products/body/simple". */
    @NotNull @Override public Result<String> bodySimple(@NotNull final String body) {
        return ok(body);
    }

    /** Invoked for route "/products/parameters/conversions". */
    @NotNull @Override public Result<String> conversions(int a, double b, @NotNull BigDecimal c, @NotNull DateOnly d, @NotNull DateTime e,
                                                         @NotNull String f, boolean g, @NotNull State h) {
        return ok(format("a:'%s', b:'%s', c:'%s', d:'%s', e:'%s', f:'%s', g:'%s', h:'%s'", a, b, c, d, e, f, g, h));
    }

    /** Invoked for route "/products/$id/details". */
    @NotNull @Override public Result<Product> details(@NotNull String id) {
        return ok(phone(id));
    }

    /** Invoked for route "/products/failure". */
    @NotNull @Override public Result<String> failure() {
        return badRequest("Failure!");
    }

    /** Invoked for route "/products/failure". */
    @NotNull @Override public Result<String> failure(@NotNull Product body) {
        return badRequest("Failure!");
    }

    /** Invoked for route "/products/failure". */
    @NotNull @Override public Result<Integer> failureWithMessage(int method) {
        return getResult(method);
    }

    /** Invoked for route "/products/failure". */
    @NotNull @Override public Result<String> failureWithMessageStr(int method) {
        return getResult(method);
    }

    /** Invoked for route "/products/featured". */
    @NotNull @Override public Result<Seq<Product>> featured() {
        final Seq<Product> products = createProductSequence();
        return ok(products);
    }

    @NotNull @Override public Result<String> headers(@NotNull String contentType, @NotNull String charset) {
        return ok("<abc>á</abc>").withContentType(Mime.fromMimeString(contentType), Charset.forName(charset));
    }

    /** Invoked for route "/products/cookies/inbound". */
    @NotNull @Override public Result<String> inboundCookies() {
        final Seq<Cookie> cookies = req.getCookies();
        return ok(cookies.map(Cookie::getValue).mkString("[", ",", "]"));
    }

    /** Invoked for route "/products/list/$list". */
    @NotNull @Override public Result<ProductList> list(@NotNull String list) {
        return ok(Products.createProductList(list));
    }

    /** Invoked for route "/products/localized/message". */
    @NotNull @Override public Result<String> localizedMessage() {
        return ok(ACTIVE.label());
    }

    /** Invoked for route "/products/parameters/multiples". */
    @NotNull @Override public Result<String> multiples(@NotNull Seq<Integer> a, @NotNull Seq<Double> b, @NotNull Seq<BigDecimal> c,
                                                       @NotNull Seq<DateOnly> d, @NotNull Seq<DateTime> e, @NotNull Seq<String> f,
                                                       @NotNull Seq<Boolean> g, @NotNull Seq<State> h) {
        return ok(
            format("a:'%s', b:'%s', c:'%s', d:'%s', e:'%s', f:'%s', g:'%s', h:'%s'",
                a.mkString("|"),
                b.mkString("|"),
                c.mkString("|"),
                d.mkString("|"),
                e.mkString("|"),
                f.mkString("|"),
                g.mkString("|"),
                h.mkString("|")));
    }

    /** Invoked for route "/products/cookies/outbound". */
    @NotNull @Override public Result<Void> outboundCookies() {
        final Result<Void> result = ok();
        result.withCookie("SESSION", "uuid")
            .withDomain(TEKGENESIS_DOMAIN)
            .withPath("/path")
            .withMaxAge(SECONDS_HOUR)
            .withSecure(true)
            .withHttpOnly(true);
        result.withCookie("NODE", "xyz");
        return result;
    }

    /** Invoked for route "/products/parameters/$id". */
    @NotNull @Override public Result<String> parameters(int id, @NotNull String from, @Nullable String to, int step) {
        return ok(format("Id %s from %s to %s step %s", id, from, to, step));
    }

    @NotNull @Override public Result<Void> redirection(int code) {
        final Call call = Routes.details("ipad");
        switch (code) {
        case 301:
            return withTokenHeader(movedPermanently(call));
        case 302:
            return withTokenHeader(found(call));
        case 303:
            return withTokenHeader(redirect(call));
        case 304:
            return withTokenHeader(notModified(call));
        default:
            return withTokenHeader(found(call));
        }
    }

    @NotNull @Override public Result<Product> related(@NotNull String id, @NotNull Product body) {
        return ok(body);
    }

    /** Invoked for route "/products/$id". */
    @NotNull @Override public Result<Product> show(@NotNull String id) {
        return ok(tv(id));
    }

    @NotNull @Override public Result<Void> update(@NotNull String id, @NotNull Product product) {
        product.setProductId(id);
        return ok();
    }

    private <T> Result<T> withTokenHeader(@NotNull Result<T> result) {
        return result.withHeader(HeaderNames.TEK_APP_TOKEN, "HMpRgckBMeo6ipblp4ETCAA12JF2JzU+MfrjtY/Rp5w=");
    }

    private <T> Result<T> getResult(int method) {
        switch (method) {
        // cannot use Status variables in a switch
        case 400:
            return badRequest(Status.BAD_REQUEST.name());
        case 401:
            return unauthorized(Status.UNAUTHORIZED.name());
        case 403:
            return forbidden(Status.FORBIDDEN.name());
        case 404:
            return notFound(Status.NOT_FOUND.name());
        case 500:
            return internalServerError(Status.INTERNAL_SERVER_ERROR.name());
        case 501:
            return notImplemented(Status.NOT_IMPLEMENTED.name());
        default:
            return badRequest();
        }
    }
}  // end class ProductHandler
