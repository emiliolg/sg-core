
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service.html;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.junit.BeforeClass;
import org.junit.Test;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.impl.MemoryEnvironment;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.service.Forwarder;
import tekgenesis.service.RequestForwarder;
import tekgenesis.service.ServiceTests;

import static tekgenesis.service.ServiceTests.createMockGetRequest;
import static tekgenesis.service.ServiceTests.createProduct;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class XHtmlTemplateTest {

    //~ Instance Fields ..............................................................................................................................

    private final Views views = new Views(builder);

    //~ Methods ......................................................................................................................................

    @Test public void testCdnResources()
        throws IOException
    {
        testXHtmlForPath(views.suicdnresources());
    }

    @Test public void testSiteHome()
        throws IOException
    {
        testXHtmlForPath(views.siteHome());
    }

    @Test public void testSiteProduct()
        throws IOException
    {
        testXHtmlForPath(views.siteProduct());
    }

    @Test public void testSuiIncludeParameterAndContent()
        throws IOException
    {
        final Html.WithMetadata html = (Html.WithMetadata) views.suiinclude("Salvador", views.suiincludereferencedcontent("Salvador"));

        html.metadata("og:title", "Salvador betting page");
        html.metadata("og:image", "img/image.jpeg");
        html.metadata("og:image:width", "200");
        html.metadata("og:image:height", "200");
        html.metadata("og:image", "img/image2.jpeg");
        html.metadata("og:image:width", "200");
        html.metadata("og:image:height", "200");

        testXHtmlForPath(html);
    }

    @Test public void testSuiMessagesHtml()
        throws IOException
    {
        testXHtmlForPath(views.suimessages());
    }

    @Test public void testSuiParamsHtml()
        throws IOException
    {
        final Html html = views.suiparams("Value 'a'", "Value 'b'", createProduct());
        testXHtmlForPath(html);
    }

    @Test public void testSuiViewAsTagHtml()
        throws IOException
    {
        testXHtmlForPath(views.suiviewastag());
    }

    @Test public void testSuiViewHtml()
        throws IOException
    {
        testXHtmlForPath(views.suiview());
    }

    private void testXHtmlForPath(Html html)
        throws IOException
    {
        final File             resourcesDir = getResourcesDir();
        final File             inputFile    = new File(resourcesDir, html.key());
        final Tests.GoldenTest test         = Tests.goldenCreate(inputFile, "target/server/core/test-output");

        setupEnvironment(resourcesDir, test);

        final FileOutputStream out = new FileOutputStream(test.getOutputFile());

        final OutputStreamWriter writer = new OutputStreamWriter(out);
        ((HtmlInstance.Xhtml) html).render(writer, XHtmlTemplateTest::createForwarder);
        writer.flush();
        writer.close();

        test.check();
    }

    private File getResourcesDir() {
        return new File("server/core/src/test/resources");
    }

    //~ Methods ......................................................................................................................................

    @BeforeClass public static void before() {
        Context.getContext().setSingleton(HtmlBuilder.class, builder);
    }

    static Forwarder createForwarder() {
        return new RequestForwarder(ServiceTests.createDispatcher(), createMockGetRequest());
    }

    static void setupEnvironment(File resourcesDir, Tests.GoldenTest test) {
        Context.bind(Environment.class, MemoryEnvironment.class);

        /*if (!context.hasBinding(InfinispanCacheManager.class))
         *  context.setSingleton(InfinispanCacheManager.class, new InfinispanCacheManager());*/
        final ApplicationProps applicationProps = new ApplicationProps();
        applicationProps.resourceSrcDir = resourcesDir.getAbsolutePath();
        applicationProps.resourceOutDir = test.getOutputFile().getParentFile().getAbsolutePath();
        applicationProps.cdnHost        = "http://cdnd.epi.ninja";
        Context.getEnvironment().put(applicationProps);
    }

    //~ Static Fields ................................................................................................................................

    private static final HtmlBuilderImpl builder = new HtmlBuilderImpl();
}  // end class XHtmlTemplateTest
