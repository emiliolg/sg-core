
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
import java.math.BigDecimal;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.env.security.Session;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.database.DatabaseConstants;
import tekgenesis.samples.service.FamousQuotes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import static tekgenesis.service.ServiceTests.createProduct;
import static tekgenesis.service.html.XHtmlTemplateTest.setupEnvironment;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class MustacheTemplateTest {

    //~ Instance Fields ..............................................................................................................................

    @Rule public DbRule db = new DbRule(DbRule.AUTHORIZATION) {
            @Override protected void before() {
                createDatabase(DatabaseConstants.MEM);
            }
        };

    private final Views views = new Views(builder);

    //~ Methods ......................................................................................................................................

    @Test public void testCdnResources()
        throws IOException
    {
        testForPath(views.mustacheSuicdnresources());
    }

    @Test public void testi18nHtml()
        throws IOException
    {
        testForPath(views.mustacheSuii18n(FamousQuotes.DALAI));
    }

    @Test public void testInvalidMustache()
        throws IOException
    {
        try {
            testForPath(views.mustacheSuiinvalid());
            failBecauseExceptionWasNotThrown(HtmlTemplateException.class);
        }
        catch (final HtmlTemplateException e) {
            assertThat(e.getMessage()).isEqualTo("No method or field with name 'doesNotExist' on line 7 file /html/mustache/sui-invalid.mustache");
        }
    }

    @Test public void testMdAndJson()
        throws IOException
    {
        testForPath(views.mustacheSuimdandjson(createProduct()));
    }

    @Test public void testParameters()
        throws IOException
    {
        testForPath(views.mustacheSuiparameters("ñandú", 2, new BigDecimal("1.1234"), true, ImmutableList.of("one", "two", "three")));
    }

    @Test public void testSuiNullParametersHtml()
        throws IOException
    {
        // todo support optional method param
        testForPath(views.mustacheSuinullparameters("", 0, Colls.emptyList()));
    }

    @Test public void testSuiParenttemplate()
        throws IOException
    {
        testForPath(views.mustacheSuiparenttemplate(views.valueOfMustacheSource("<h3>Child Included from parameter Template</h3>").build()));
    }

    @Test public void testTags()
        throws IOException
    {
        testForPath(views.mustacheSuitags());
    }

    private void testForPath(Html html)
        throws IOException
    {
        final Session session = SecurityUtils.getSession();
        session.logout();
        session.authenticate("admin", "password");

        final File             resourcesDir = getResourcesDir();
        final File             inputFile    = new File(resourcesDir, html.key());
        final Tests.GoldenTest test         = Tests.goldenCreate(inputFile, "target/server/core/test-output");

        setupEnvironment(resourcesDir, test);

        final FileOutputStream out = new FileOutputStream(test.getOutputFile());

        final OutputStreamWriter writer = new OutputStreamWriter(out);
        ((HtmlInstance.Mustache) html).render(writer, XHtmlTemplateTest::createForwarder);
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

    //~ Static Fields ................................................................................................................................

    private static final HtmlBuilderImpl builder = new HtmlBuilderImpl();
}  // end class MustacheTemplateTest
