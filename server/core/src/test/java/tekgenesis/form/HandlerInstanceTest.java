
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.service.HeaderNames;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.FormRule;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.routing.Route;
import tekgenesis.metadata.routing.Routes;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.repository.ModelRepository;
import tekgenesis.sales.basic.Category;
import tekgenesis.sales.basic.Customer;
import tekgenesis.sales.basic.DocType;
import tekgenesis.sales.basic.Sex;
import tekgenesis.sales.basic.g.CategoryBase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.sales.basic.CustomerForm.INVALID_DOCUMENT;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Handler test.
 */

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class HandlerInstanceTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    public DbRule db = new DbRule(DbRule.SG, DbRule.AUTHORIZATION, DbRule.BASIC) {
            @Override protected void before() {
                createDatabase(dbName);
                final ModelRepositoryLoader loader = new ModelRepositoryLoader(Thread.currentThread().getContextClassLoader());
                Context.getContext().setSingleton(ModelRepository.class, loader.build());
            }
        };

    @Rule public TestRule chain = db.around(new FormRule());

    final String customerJson = "{" +
                                "\n  \"documentType\" : \"DNI\"," +
                                "\n  \"documentId\" : 30887538," +
                                "\n  \"firstName\" : \"John\"," +
                                "\n  \"lastName\" : \"Doe\"," +
                                "\n  \"nickname\" : \"JDoe\"," +
                                "\n  \"sex\" : \"M\"" +
                                "\n}";

    private HandlerImpl<FormInstance<?>> handler  = null;
    private MockHttpServletRequest       req      = new MockHttpServletRequest();
    private MockHttpServletResponse      resp     = new MockHttpServletResponse();
    private DefaultHandlerInstance       instance = createDefaultHandlerInstance(req, resp);

    //~ Methods ......................................................................................................................................

    @Test public void testHandleDelete() {
        final Customer customer = createCustomer();

        http("tekgenesis.sales.basic.CustomerForm/" + customer.keyAsString());

        assertThat(getCustomersSize()).isEqualTo(1);

        runInTransaction(() -> {
            try {
                instance.handleDelete(handler);
            }
            catch (final IOException e) {
                fail(e.getMessage());
            }
        });

        assertThat(getCustomersSize()).isZero();
    }

    @Test public void testHandleGet()
        throws IOException
    {
        final Customer customer = createCustomer();

        http("tekgenesis.sales.basic.CustomerForm/" + customer.keyAsString());

        req.addHeader(JsonConfiguration.X_PRETTY_PRINT, "true");

        runInTransaction(() -> {
            try {
                instance.handleGet(handler);
            }
            catch (final IOException e) {
                fail(e.getMessage());
            }
        });

        assertThat(resp.getContentAsString()).isEqualTo(customerJson);
    }

    @Test public void testHandleHeadAndOptions()
        throws IOException
    {
        http("tekgenesis.sales.basic.CustomerForm");

        try {
            instance.handleHead(handler);
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        }
        catch (final IllegalStateException e) {
            assertThat(e).hasMessage("To be implemented!");
        }

        try {
            instance.handleOptions(handler);
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        }
        catch (final IllegalStateException e) {
            assertThat(e).hasMessage("To be implemented!");
        }
    }

    @Test public void testHandlePostCreate()
        throws IOException
    {
        http("tekgenesis.sales.basic.CustomerForm");

        req.addHeader(HeaderNames.X_FIELDS, "nickname,sex");

        req.setContent(customerJson.getBytes(Charset.defaultCharset()));

        assertThat(getCustomersSize()).isZero();

        instance.handlePost(handler);

        assertThat(getCustomersSize()).isEqualTo(1);

        assertThat(resp.getStatus()).isEqualTo(HttpURLConnection.HTTP_CREATED);
        assertThat(resp.getHeader(HandlerInstance.LOCATION)).isEqualTo("/tekgenesis.sales.basic.CustomerForm/DNI:30887538:M");

        final String nicknameJson = "{\"nickname\":\"JDoe\",\"sex\":\"M\"}";
        assertThat(resp.getContentAsString()).isEqualTo(nicknameJson);
    }

    @Test public void testHandlePostCreateWithError()
        throws IOException
    {
        http("tekgenesis.sales.basic.CustomerForm");

        final String invalid = "{" +
                               "\n  \"documentType\" : \"DNI\"," +
                               "\n  \"documentId\" : 10142297," +
                               "\n  \"firstName\" : \"John\"," +
                               "\n  \"lastName\" : \"Doe\"," +
                               "\n  \"nickname\" : \"JDoe\"," +
                               "\n  \"sex\" : \"M\"" +
                               "\n}";

        req.setContent(invalid.getBytes(Charset.defaultCharset()));

        assertThat(getCustomersSize()).isZero();

        instance.handlePost(handler);

        assertThat(getCustomersSize()).isZero();

        assertThat(resp.getStatus()).isEqualTo(HttpURLConnection.HTTP_INTERNAL_ERROR);
        assertThat(resp.getContentAsString()).isEqualToIgnoringCase(INVALID_DOCUMENT);
    }

    @Test public void testHandlePostUpdate() {
        runInTransaction(() -> {
            final Customer customer = createCustomer();

            http("tekgenesis.sales.basic.CustomerForm/" + customer.keyAsString());

            req.setContent("{\"firstName\" : \"Salvatore\"}".getBytes(Charset.defaultCharset()));

            final Customer john = Customer.find(customer.keyAsString());
            if (john != null) assertThat(john.getFirstName()).isEqualTo("John");
            else fail("Should have found John");

            try {
                instance.handlePost(handler);
            }
            catch (final IOException e) {
                fail(e.getMessage());
            }

            final Customer salvatore = Customer.find(customer.keyAsString());
            if (salvatore != null) assertThat(salvatore.getFirstName()).isEqualTo("Salvatore");
            else fail("Should have found Salvatore");

            assertThat(resp.getStatus()).isEqualTo(HttpURLConnection.HTTP_ACCEPTED);
            assertThat(resp.getHeader(HandlerInstance.LOCATION)).isEqualTo("/tekgenesis.sales.basic.CustomerForm/DNI:30887538:M");
        });
    }

    @Test public void testParametersWithDefaultValues()
        throws UnsupportedEncodingException
    {
        final Seq<Category> all = createCategories();

        http("tekgenesis.sales.basic.CategoriesService");

        runInTransaction(() -> {
            try {
                instance.handleGet(handler);
            }
            catch (final IOException e) {
                fail(e.getMessage());
            }
        });

        final String expected = "{" +
                                "\"categories\":" + all.take(3).map(HandlerInstanceTest::categoryAsJson).mkString("[", ",", "],") + "\"next\":4" +
                                "}";

        assertThat(resp.getContentAsString()).isEqualTo(expected);
    }

    @Test public void testParametersWithSpecifiedValues()
        throws IOException
    {
        final Seq<Category> all = createCategories();

        http("tekgenesis.sales.basic.CategoriesService");

        req.setQueryString("from=2&limit=4");

        runInTransaction(() -> {
            try {
                instance.handleGet(handler);
            }
            catch (final IOException e) {
                fail(e.getMessage());
            }
        });

        final String expected = "{" +
                                "\"categories\":" + all.drop(1).take(4).map(HandlerInstanceTest::categoryAsJson).mkString("[", ",", "],") +
                                "\"next\":6" +
                                "}";

        assertThat(resp.getContentAsString()).isEqualTo(expected);
    }

    private Seq<Category> createCategories() {
        return invokeInTransaction(() ->
                listOf(createCategory(1, "Tv"),
                    createCategory(2, "Audio"),
                    createCategory(3, "Celulares"),
                    createCategory(4, "Heladeras"),
                    createCategory(5, "Cocina"),
                    createCategory(6, "Gaming"),
                    createCategory(7, "Lavado")));
    }

    private Category createCategory(Integer id, String name) {
        return CategoryBase.create(id).setName(name).setDescr(name).insert();
    }

    private Customer createCustomer() {
        return invokeInTransaction(() ->
                Customer.create(DocType.DNI, new BigDecimal(30887538L), Sex.M)
                        .setFirstName("John")
                        .setLastName("Doe")
                        .setNickname("JDoe")
                        .insert());
    }

    private DefaultHandlerInstance createDefaultHandlerInstance(final MockHttpServletRequest request, final MockHttpServletResponse response) {
        return new DefaultHandlerInstance(request, response);
    }

    private HandlerImpl<FormInstance<?>> createHandler(String path) {
        final Route<Form> route = Routes.route(path);
        assertThat(route.isDefined()).isTrue();
        return new HandlerImpl<>(route.getTarget(), route.getPath(), route.getKey());
    }

    private void http(String path) {
        handler  = createHandler(path);
        req      = new MockHttpServletRequest();
        resp     = new MockHttpServletResponse();
        instance = createDefaultHandlerInstance(req, resp);
    }

    private long getCustomersSize() {
        return invokeInTransaction(() -> Customer.list().count());
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    @NotNull private static String categoryAsJson(final Category category) {
        return "{" +
               "\"id\":" + category.getIdKey() + "," +
               "\"name\":\"" + category.getName() + "\"," +
               "\"descr\":\"" + category.getDescr() + "\"" +
               "}";
    }
}  // end class HandlerInstanceTest
