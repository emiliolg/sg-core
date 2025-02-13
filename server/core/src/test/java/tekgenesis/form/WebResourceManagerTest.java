
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.common.core.Times;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.impl.MemoryEnvironment;
import tekgenesis.common.tools.test.LrgTests;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.common.util.Files;
import tekgenesis.service.ServiceProps;
import tekgenesis.task.TaskService;

import static tekgenesis.common.core.Constants.SUIGEN_DEVMODE;
import static tekgenesis.common.core.Strings.deCapitalizeFirst;
import static tekgenesis.common.tools.test.Tests.assertEquals;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection" })
public class WebResourceManagerTest {

    //~ Methods ......................................................................................................................................

    @Category(LrgTests.class)
    public void testHtmlResourcesManagerListening()
        throws IOException
    {
        final File resources = directory("target/server/core/test-resources");
        final File output    = directory("target/server/core/test-output");

        Files.copyDirectory(new File("server/core/src/test/resources/html_resources"), resources);

        final File siteResources = new File(resources, "js/site");
        final File siteOutput    = new File(output, "js/site");

        final File beforeJs       = new File(siteResources, "before.js");
        final File goldenBeforeJs = new File(siteResources, "before.golden");
        final File outputBeforeJs = new File(siteOutput, "before.js");

        final File afterJs       = new File(siteResources, "after.js");
        final File goldenAfterJs = new File(siteResources, "after.golden");

        setupEnvironment(resources, output);

        final Boolean devMode = Boolean.getBoolean(SUIGEN_DEVMODE);
        System.setProperty(SUIGEN_DEVMODE, String.valueOf(false));

        WebResourceManager.start();

        Tests.checkDiff(outputBeforeJs, goldenBeforeJs);

        Files.copy(new FileInputStream(afterJs), new FileOutputStream(beforeJs));

        for (int retries = 0; retries < 8 && !assertEquals(outputBeforeJs, goldenAfterJs); retries++) {
            try {
                Thread.sleep(5 * Times.MILLIS_SECOND);
            }
            catch (final InterruptedException ignored) {}
        }

        Tests.checkDiff(outputBeforeJs, goldenAfterJs);

        WebResourceManager.stop();

        System.setProperty(SUIGEN_DEVMODE, String.valueOf(devMode));
    }  // end method testHtmlResourcesManagerListening

    @Test public void testHtmlResourcesManagerStartup()
        throws IOException
    {
        final File resources    = directory("target/server/core/test-resources");
        final File resourcesDev = directory("target/server/core/test-output/devgolden");
        final File output       = directory("target/server/core/test-output");

        Files.copyDirectory(new File("server/core/src/test/resources/html_resources"), resources);

        final File             javascriptJs = new File(resources, "javascript.js");
        final Tests.GoldenTest jsTest       = Tests.goldenCreate(javascriptJs, output.getAbsolutePath());
        final Tests.GoldenTest jsDevTest    = Tests.goldenCreate("javascript.js", output.getAbsolutePath(), resourcesDev.getAbsolutePath());

        final File             styleLess    = new File(resources, "style.less");
        final Tests.GoldenTest styleTest    = Tests.goldenCreate(styleLess, output.getAbsolutePath());
        final Tests.GoldenTest styleDevTest = Tests.goldenCreate("style.less", output.getAbsolutePath(), resourcesDev.getAbsolutePath());

        final File             beforeJs      = new File(new File(resources, "js/site"), "before.js");
        final Tests.GoldenTest beforeTest    = Tests.goldenCreate(beforeJs, new File(output, "js/site").getAbsolutePath());
        final Tests.GoldenTest beforeDevTest = Tests.goldenCreate("before.js",
                new File(output, "js/site").getAbsolutePath(),
                resourcesDev.getAbsolutePath());

        setupEnvironment(resources, output);

        final Boolean devMode = Boolean.getBoolean(SUIGEN_DEVMODE);
        System.setProperty("application.lifecycleTaskEnabled", String.valueOf(false));
        System.setProperty("taskService.taskservice.enabled", String.valueOf(false));

        System.setProperty(SUIGEN_DEVMODE, String.valueOf(false));
        WebResourceManager.start();
        jsTest.check();
        styleTest.check();
        beforeTest.check();
        WebResourceManager.stop();

        System.setProperty(SUIGEN_DEVMODE, String.valueOf(true));
        WebResourceManager.start();
        jsDevTest.check();
        styleDevTest.check();
        beforeDevTest.check();
        WebResourceManager.stop();

        System.setProperty(SUIGEN_DEVMODE, String.valueOf(devMode));
    }  // end method testHtmlResourcesManagerStartup

    private File directory(@NotNull final String pathname) {
        final File directory = new File(pathname);
        Files.remove(directory);
        return directory;
    }

    private void setupEnvironment(@NotNull final File resources, @NotNull final File output) {
        Context.bind(Environment.class, MemoryEnvironment.class);
        final ApplicationProps applicationProps = new ApplicationProps();
        applicationProps.lifecycleTaskEnabled = false;

        applicationProps.resourceSrcDir = resources.getAbsolutePath();
        applicationProps.resourceOutDir = output.getAbsolutePath();

        final ServiceProps serviceProps = new ServiceProps();
        serviceProps.enabled = false;

        Context.getEnvironment().put(deCapitalizeFirst(TaskService.SERVICE_NAME), serviceProps);
        Context.getEnvironment().put(applicationProps);
    }
}  // end class WebResourceManagerTest
