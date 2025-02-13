
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.io.IOException;
import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.media.MediaType;
import tekgenesis.common.service.Method;
import tekgenesis.common.service.Status;
import tekgenesis.common.service.server.Request;
import tekgenesis.sales.basic.service.*;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.json.JsonMapping.shared;
import static tekgenesis.service.HandlerTests.initialize;

@SuppressWarnings({ "JavaDoc", "MagicNumber" })
public class HandlerTest {

    //~ Instance Fields ..............................................................................................................................

    @Rule public final MockResponseRule response = new MockResponseRule();

    //~ Methods ......................................................................................................................................

    @Test public void testException()
        throws IOException
    {
        final ExceptionHandler handler = initialize(ExceptionHandler.class, createGetRequest());

        final Result<String> result = handler.exceptionWithMsg("message");

        assertThat(result.getStatus()).isEqualTo(Status.BAD_REQUEST);

        assertThat(HandlerTests.hasException(result, HandlerError.QUOTE_MESSAGE, "message")).isTrue();

        final Result<String> resultWithoutArg = handler.exception();

        assertThat(resultWithoutArg.getStatus()).isEqualTo(Status.BAD_REQUEST);

        assertThat(HandlerTests.hasException(resultWithoutArg, HandlerError.ERROR_MESSAGE)).isTrue();
    }

    @Test public void testNotFound()
        throws IOException
    {
        final ProductHandler products = initialize(ProductHandler.class, createGetRequest());

        final Result<?> result = products.failureWithMessage(Status.NOT_FOUND.code());

        response.handle(result);

        assertThat(response.getStatus()).isEqualTo(Status.NOT_FOUND);
    }

    @Test public void testOkResult()
        throws IOException
    {
        final ProductHandler products = initialize(ProductHandler.class, createGetRequest());

        final Result<?> result = products.show("1");

        response.handle(result);

        assertThat(response.getStatus()).isEqualTo(Status.OK);

        final Product product = response.getBodyAsJson(Product.class);

        assertThat(product.getProductId()).isEqualTo("1");
    }

    @Test public void testOkResultWithBasicTypes() {
        final ReturnTypesHandler types = initialize(ReturnTypesHandler.class, createGetRequest());

        assertPrimitiveResult(types.returnBoolean(), "true", Types.booleanType());
        assertPrimitiveResult(types.returnReal(), "0.99", Types.realType());
        assertPrimitiveResult(types.returnDecimal(), "0.99", Types.decimalType(10, 2));
        assertPrimitiveResult(types.returnInt(), "1", Types.intType());
        assertPrimitiveResult(types.returnDateTime(), "2014-01-31T09:47:00Z", Types.dateTimeType());
        assertPrimitiveResult(types.returnDate(), "2014-01-31", Types.dateType());
    }

    @Test public void testOkResultWithComplexType()
        throws IOException
    {
        final ProductHandler products = initialize(ProductHandler.class, createGetRequest());

        final Result<?> result = products.list("1");

        response.handle(result);

        assertThat(response.getStatus()).isEqualTo(Status.OK);
        final ProductList list = response.getBodyAsJson(ProductList.class);
        assertThat(list.getProducts().size()).isEqualTo(3);
    }

    @Test public void testOkResultWithMultipleType()
        throws IOException
    {
        final ProductHandler products = initialize(ProductHandler.class, createGetRequest());

        final Result<?> result = products.featured();

        response.handle(result);

        assertThat(response.getStatus()).isEqualTo(Status.OK);
        final Seq<Product> list = response.getBodyAsJsonSeq(Product.class);
        assertThat(list.size()).isEqualTo(3);
        assertThat(list.iterator().next().getProductId()).isEqualTo("1");
    }

    @Test public void testOkResultWithPost()
        throws IOException
    {
        final ProductHandler products = initialize(ProductHandler.class, createRequest(Method.POST, createProductAsContent("0")));

        final Result<Void> result = products.update("1", createProduct("0"));

        response.handle(result);
        assertThat(response.getStatus()).isEqualTo(Status.OK);
    }

    private <T> void assertPrimitiveResult(Result<T> result, String value, Type type) {
        ((ResultImpl<?>) result).withResultType(type);
        response.handle(result);
        assertThat(response.getStatus()).isEqualTo(Status.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.TEXT_PLAIN);
        assertThat(response.getBodyAsString()).isEqualTo(value);
    }

    private Product createProduct(String productId) {
        final Product product = new Product();
        product.setProductId(productId);
        product.setModel("Sony 32");
        product.setPrice(new BigDecimal(499.99));
        return product;
    }

    private byte[] createProductAsContent(@NotNull String productId)
        throws IOException
    {
        final Product product = createProduct(productId);
        return shared().writeValueAsBytes(product);
    }

    //~ Methods ......................................................................................................................................

    private static Request createGetRequest() {
        return createRequest(Method.GET);
    }

    private static Request createRequest(@NotNull Method method) {
        return new MockServerRequest(method);
    }

    private static Request createRequest(@NotNull Method method, @NotNull byte[] content) {
        return new MockServerRequest(method, content);
    }
}  // end class ServiceHandlerTest
