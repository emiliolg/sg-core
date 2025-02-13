package tekgenesis.sales.basic.service;

import java.math.BigDecimal;
import tekgenesis.common.service.Call;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.service.Factory;
import tekgenesis.service.HandlerInstance;
import tekgenesis.common.core.Integers;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.service.Result;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Strings;
import static tekgenesis.common.service.Method.GET;
import static tekgenesis.common.service.Method.POST;

/** 
 * Generated base class for handler: ProductHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class ProductHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    ProductHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/products/$id" */
    @NotNull public abstract Result<Product> show(@NotNull String id);

    /** Invoked for route "/products/$id" */
    @NotNull public abstract Result<Void> update(@NotNull String id, @NotNull Product body);

    /** Invoked for route "/products/$id/details" */
    @NotNull public abstract Result<Product> details(@NotNull String id);

    /** Invoked for route "/products/featured" */
    @NotNull public abstract Result<Seq<Product>> featured();

    /** Invoked for route "/products/list/$list" */
    @NotNull public abstract Result<ProductList> list(@NotNull String list);

    /** Invoked for route "/products/headers" */
    @NotNull public abstract Result<String> headers(@NotNull String contentType, @NotNull String charset);

    /** Invoked for route "/products/redirect/$code" */
    @NotNull public abstract Result<Void> redirection(int code);

    /** Invoked for route "/products/failure" */
    @NotNull public abstract Result<String> failure();

    /** Invoked for route "/products/failure" */
    @NotNull public abstract Result<String> failure(@NotNull Product body);

    /** Invoked for route "/products/failureWithMessage" */
    @NotNull public abstract Result<Integer> failureWithMessage(int method);

    /** Invoked for route "/products/failureWithMessageStr" */
    @NotNull public abstract Result<String> failureWithMessageStr(int method);

    /** Invoked for route "/products/$id/related" */
    @NotNull public abstract Result<Product> related(@NotNull String id, @NotNull Product body);

    /** Invoked for route "/products/body/multiple" */
    @NotNull public abstract Result<String> bodyMultiple(@NotNull Seq<Product> body);

    /** Invoked for route "/products/body/simple" */
    @NotNull public abstract Result<String> bodySimple(@NotNull String body);

    /** Invoked for route "/products/body/empty" */
    @NotNull public abstract Result<String> bodyEmpty();

    /** Invoked for route "/products/parameters/$id" */
    @NotNull public abstract Result<String> parameters(int id, @NotNull String from, @Nullable String to, int step);

    /** Invoked for route "/products/parameters/multiples" */
    @NotNull public abstract Result<String> multiples(@NotNull Seq<Integer> a, @NotNull Seq<Double> b, @NotNull Seq<BigDecimal> c, @NotNull Seq<DateOnly> d, @NotNull Seq<DateTime> e, @NotNull Seq<String> f, @NotNull Seq<Boolean> g, @NotNull Seq<State> h);

    /** Invoked for route "/products/parameters/conversions" */
    @NotNull public abstract Result<String> conversions(int a, double b, @NotNull BigDecimal c, @NotNull DateOnly d, @NotNull DateTime e, @NotNull String f, boolean g, @NotNull State h);

    /** Invoked for route "/products/localized/message" */
    @NotNull public abstract Result<String> localizedMessage();

    /** Invoked for route "/products/cookies/outbound" */
    @NotNull public abstract Result<Void> outboundCookies();

    /** Invoked for route "/products/cookies/inbound" */
    @NotNull public abstract Result<String> inboundCookies();

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(ProductHandler.class);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/products/$id" {@link ProductHandler#show(String)} */
        @NotNull public static Call show(@NotNull String id) {
            return new Call(GET, String.format("/products/%s", Strings.truncate(id, 255)), "ProductsId");
        }

        /** Reverse route for "/products/$id" {@link ProductHandler#update(String, Product)} */
        @NotNull public static Call update(@NotNull String id) {
            return new Call(POST, String.format("/products/%s", Strings.truncate(id, 255)), "ProductsId");
        }

        /** Reverse route for "/products/$id/details" {@link ProductHandler#details(String)} */
        @NotNull public static Call details(@NotNull String id) {
            return new Call(GET, String.format("/products/%s/details", Strings.truncate(id, 255)), "ProductsIdDetails");
        }

        /** Reverse route for "/products/featured" {@link ProductHandler#featured()} */
        @NotNull public static Call featured() {
            return new Call(GET, "/products/featured", "ProductsFeatured");
        }

        /** Reverse route for "/products/list/$list" {@link ProductHandler#list(String)} */
        @NotNull public static Call list(@NotNull String list) {
            return new Call(GET, String.format("/products/list/%s", Strings.truncate(list, 255)), "ProductsListList");
        }

        /** Reverse route for "/products/headers" {@link ProductHandler#headers(String, String)} */
        @NotNull public static Call headers() {
            return new Call(GET, "/products/headers", "ProductsHeaders");
        }

        /** Reverse route for "/products/redirect/$code" {@link ProductHandler#redirection(int)} */
        @NotNull public static Call redirection(int code) {
            return new Call(GET, String.format("/products/redirect/%s", Integers.checkSignedLength("code", code, true, 9)), "ProductsRedirectCode");
        }

        /** Reverse route for "/products/failure" {@link ProductHandler#failure()} */
        @NotNull public static Call failure() {
            return new Call(GET, "/products/failure", "ProductsFailure");
        }

        /** Reverse route for "/products/failureWithMessage" {@link ProductHandler#failureWithMessage(int)} */
        @NotNull public static Call failureWithMessage() {
            return new Call(GET, "/products/failureWithMessage", "ProductsFailureWithMessage");
        }

        /** Reverse route for "/products/failureWithMessageStr" {@link ProductHandler#failureWithMessageStr(int)} */
        @NotNull public static Call failureWithMessageStr() {
            return new Call(GET, "/products/failureWithMessageStr", "ProductsFailureWithMessageStr");
        }

        /** Reverse route for "/products/$id/related" {@link ProductHandler#related(String, Product)} */
        @NotNull public static Call related(@NotNull String id) {
            return new Call(POST, String.format("/products/%s/related", Strings.truncate(id, 255)), "ProductsIdRelated");
        }

        /** Reverse route for "/products/body/multiple" {@link ProductHandler#bodyMultiple(Seq)} */
        @NotNull public static Call bodyMultiple() {
            return new Call(POST, "/products/body/multiple", "ProductsBodyMultiple");
        }

        /** Reverse route for "/products/body/simple" {@link ProductHandler#bodySimple(String)} */
        @NotNull public static Call bodySimple() {
            return new Call(POST, "/products/body/simple", "ProductsBodySimple");
        }

        /** Reverse route for "/products/body/empty" {@link ProductHandler#bodyEmpty()} */
        @NotNull public static Call bodyEmpty() {
            return new Call(POST, "/products/body/empty", "ProductsBodyEmpty");
        }

        /** Reverse route for "/products/parameters/$id" {@link ProductHandler#parameters(int, String, String, int)} */
        @NotNull public static Call parameters(int id) {
            return new Call(GET, String.format("/products/parameters/%s", Integers.checkSignedLength("id", id, true, 9)), "ProductsParametersId");
        }

        /** Reverse route for "/products/parameters/multiples" {@link ProductHandler#multiples(Seq, Seq, Seq, Seq, Seq, Seq, Seq, Seq)} */
        @NotNull public static Call multiples() {
            return new Call(GET, "/products/parameters/multiples", "ProductsParametersMultiples");
        }

        /** Reverse route for "/products/parameters/conversions" {@link ProductHandler#conversions(int, double, BigDecimal, DateOnly, DateTime, String, boolean, State)} */
        @NotNull public static Call conversions() {
            return new Call(GET, "/products/parameters/conversions", "ProductsParametersConversions");
        }

        /** Reverse route for "/products/localized/message" {@link ProductHandler#localizedMessage()} */
        @NotNull public static Call localizedMessage() {
            return new Call(GET, "/products/localized/message", "ProductsLocalizedMessage");
        }

        /** Reverse route for "/products/cookies/outbound" {@link ProductHandler#outboundCookies()} */
        @NotNull public static Call outboundCookies() {
            return new Call(GET, "/products/cookies/outbound", "ProductsCookiesOutbound");
        }

        /** Reverse route for "/products/cookies/inbound" {@link ProductHandler#inboundCookies()} */
        @NotNull public static Call inboundCookies() {
            return new Call(GET, "/products/cookies/inbound", "ProductsCookiesInbound");
        }

    }
}
