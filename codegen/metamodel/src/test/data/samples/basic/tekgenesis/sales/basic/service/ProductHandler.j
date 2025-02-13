package tekgenesis.sales.basic.service;

import java.math.BigDecimal;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.service.Factory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.service.Result;
import tekgenesis.common.collections.Seq;

/** User class for Handler: ProductHandler */
public class ProductHandler
    extends ProductHandlerBase
{

    //~ Constructors .............................................................................................................

    ProductHandler(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/products/$id" */
    @Override @NotNull public Result<Product> show(@NotNull String id) { return notImplemented(); }

    /** Invoked for route "/products/$id" */
    @Override @NotNull public Result<Void> update(@NotNull String id, @NotNull Product body) { return notImplemented(); }

    /** Invoked for route "/products/$id/details" */
    @Override @NotNull public Result<Product> details(@NotNull String id) { return notImplemented(); }

    /** Invoked for route "/products/featured" */
    @Override @NotNull public Result<Seq<Product>> featured() { return notImplemented(); }

    /** Invoked for route "/products/list/$list" */
    @Override @NotNull public Result<ProductList> list(@NotNull String list) { return notImplemented(); }

    /** Invoked for route "/products/headers" */
    @Override @NotNull public Result<String> headers(@NotNull String contentType, @NotNull String charset) { return notImplemented(); }

    /** Invoked for route "/products/redirect/$code" */
    @Override @NotNull public Result<Void> redirection(int code) { return notImplemented(); }

    /** Invoked for route "/products/failure" */
    @Override @NotNull public Result<String> failure() { return notImplemented(); }

    /** Invoked for route "/products/failureWithMessage" */
    @Override @NotNull public Result<Integer> failureWithMessage(int method) { return notImplemented(); }

    /** Invoked for route "/products/failureWithMessageStr" */
    @Override @NotNull public Result<String> failureWithMessageStr(int method) { return notImplemented(); }

    /** Invoked for route "/products/$id/related" */
    @Override @NotNull public Result<Product> related(@NotNull String id, @NotNull Product body) { return notImplemented(); }

    /** Invoked for route "/products/body/multiple" */
    @Override @NotNull public Result<String> bodyMultiple(@NotNull Seq<Product> body) { return notImplemented(); }

    /** Invoked for route "/products/body/simple" */
    @Override @NotNull public Result<String> bodySimple(@NotNull String body) { return notImplemented(); }

    /** Invoked for route "/products/body/empty" */
    @Override @NotNull public Result<String> bodyEmpty() { return notImplemented(); }

    /** Invoked for route "/products/parameters/$id" */
    @Override @NotNull public Result<String> parameters(int id, @NotNull String from, @Nullable String to, int step) { return notImplemented(); }

    /** Invoked for route "/products/parameters/multiples" */
    @Override @NotNull public Result<String> multiples(@NotNull Seq<Integer> a, @NotNull Seq<Double> b, @NotNull Seq<BigDecimal> c, @NotNull Seq<DateOnly> d, @NotNull Seq<DateTime> e, @NotNull Seq<String> f, @NotNull Seq<Boolean> g, @NotNull Seq<State> h) { return notImplemented(); }

    /** Invoked for route "/products/parameters/conversions" */
    @Override @NotNull public Result<String> conversions(int a, double b, @NotNull BigDecimal c, @NotNull DateOnly d, @NotNull DateTime e, @NotNull String f, boolean g, @NotNull State h) { return notImplemented(); }

    /** Invoked for route "/products/localized/message" */
    @Override @NotNull public Result<String> localizedMessage() { return notImplemented(); }

    /** Invoked for route "/products/cookies/outbound" */
    @Override @NotNull public Result<Void> outboundCookies() { return notImplemented(); }

    /** Invoked for route "/products/cookies/inbound" */
    @Override @NotNull public Result<String> inboundCookies() { return notImplemented(); }

}
