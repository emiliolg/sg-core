
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service.html;

import java.io.*;

import org.jetbrains.annotations.NotNull;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Resource;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.media.Mime;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.persistence.resource.DbResourceHandler;

import static tekgenesis.common.core.Constants.UTF8;
import static tekgenesis.service.ServiceTests.createProduct;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection" })
public class XHtmlResourceTemplateTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public String dbName = null;
    private DbResourceHandler              rh     = null;

    @Rule public DbRule db = new DbRule(DbRule.SG, DbRule.AUTHORIZATION, DbRule.BASIC_TEST, DbRule.BASIC, DbRule.SHOWCASE) {
            @Override protected void before() {
                createDatabase(dbName);
                rh = new DbResourceHandler(env, database);
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void testSiteHome()
        throws IOException
    {
        final String   file     = "site/home.xhtml";
        final Resource resource = createResourceForFile(file);
        testXHtmlForResource(file, resource);
    }

    @Test public void testSiteProduct()
        throws IOException
    {
        final String   file     = "site/product.xhtml";
        final Resource resource = createResourceForFile(file);
        testXHtmlForResource(file, resource);
    }

    @Test public void testSuiIncludeParameterAndContent()
        throws IOException
    {
        final Resource resource = createResourceForFile("sui-include.xhtml");
        final byte[]   text     = "<span>Hello include from resource!</span>".getBytes(UTF8);

        final Resource include = invokeInTransaction(() ->  //
                     rh.create().upload("include", Mime.APPLICATION_XHTML_XML.getMime(), new ByteArrayInputStream(text)));

        final Html              span = builder.html(include).build();
        final Html.WithMetadata html = builder.html(resource).str("title", "From resource!").html("content", span).build();

        html.metadata("og:some", "metadata");

        testXHtmlForResource("sui-include-as-resource.xhtml", html);
    }

    @Test public void testSuiParamsHtml()
        throws IOException
    {
        final Resource resource = createResourceForFile("sui-params.xhtml");
        final Html     html     = builder.html(resource).str("a", "Value 'a'").str("b", "Value 'b'").struct("c", createProduct()).build();
        testXHtmlForResource("sui-params-as-resource.xhtml", html);
    }

    @Test public void testSuiViewAsTagHtml()
        throws IOException
    {
        final String   file     = "sui-view-as-tag.xhtml";
        final Resource resource = createResourceForFile(file);
        testXHtmlForResource(file, resource);
    }

    @Test public void testSuiViewHtml()
        throws IOException
    {
        final String   file     = "sui-view.xhtml";
        final Resource resource = createResourceForFile(file);
        testXHtmlForResource(file, resource);
    }

    private Resource createResourceForFile(@NotNull String file) {  //
        return invokeInTransaction(() -> rh.create().upload(new File(html_files_directory, file)));
    }

    private void testXHtmlForResource(String golden, Resource resource)
        throws IOException
    {
        final Html html = builder.html(resource).build();
        testXHtmlForResource(golden, html);
    }

    private void testXHtmlForResource(String golden, Html html)
        throws IOException
    {
        final Tests.GoldenTest test = Tests.goldenCreate(new File(html_files_directory, golden), "target/server/core/test-output");

        XHtmlTemplateTest.setupEnvironment(html_files_directory, test);

        final FileOutputStream out = new FileOutputStream(test.getOutputFile());

        final OutputStreamWriter writer = new OutputStreamWriter(out);
        ((HtmlInstance.Xhtml) html).render(writer, XHtmlTemplateTest::createForwarder);
        writer.flush();
        writer.close();

        test.check();
    }

    //~ Methods ......................................................................................................................................

    @BeforeClass public static void before() {
        Context.getContext().setSingleton(HtmlBuilder.class, builder);
    }

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    //~ Static Fields ................................................................................................................................

    private static final HtmlBuilderImpl builder = new HtmlBuilderImpl();

    public static final File html_files_directory = new File("server/core/src/test/resources/html");
}  // end class XHtmlResourceTemplateTest
