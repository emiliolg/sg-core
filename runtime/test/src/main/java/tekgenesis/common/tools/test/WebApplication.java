
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.tools.test;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.logging.LogConfig;
import tekgenesis.common.util.Files;

/**
 * Utility that starts a Sui Generis application with an embedded Jetty Server.
 */
@SuppressWarnings({ "UnnecessaryFullyQualifiedName", "WeakerAccess" })
public class WebApplication {

    //~ Constructors .................................................................................................................................

    private WebApplication() {}

    //~ Methods ......................................................................................................................................

    /** Main method. */
    @SuppressWarnings({ "OverlyLongMethod", "MagicNumber" })
    public static void start(Environment env, @NotNull final String applicationName, @NotNull final PropertiesProfile profile, final int port,
                             final boolean resetDb)
        throws Exception
    {
        // Just in case it is running....
        stop();

        // Set the run dir property before initializing the application as it is used by the configure method
        final String runDir = ApplicationUtils.setupRunDir(applicationName);

        // Initialize logger (and avoid jetty default logs)
        LogConfig.start();

        profile.setup(env, resetDb, runDir);

        // Set the install dir needed to run the web app (if not already set)
        final String installDir = ApplicationUtils.setupInstallDir();

        // Cleanup run dir
        Files.remove(new File(runDir));

        // noinspection SpellCheckingInspection
        final String webAppDir = installDir + "/webapp";
        System.setProperty(Constants.WEBAPP_DIR, webAppDir);

        // Setup the web context
        final WebAppContext context = new WebAppContext();
        context.setWar(webAppDir);
        context.setContextPath("/");

        // Enable Jetty to use normal JavaSE class-loading priority, and gives priority to the parent/system class-loader.
        // This avoids the issues of multiple versions of a class within a web-app
        // Ref: http://wiki.eclipse.org/Jetty/Reference/Jetty_Classloading
        context.setParentLoaderPriority(true);

        final File webAppDirFile = new File(webAppDir);
        appendExtraJars(context, webAppDirFile);

        // Now configure the server and start it
        server = new Server(port);
        server.setHandler(context);
        server.start();

        // Just in case
        LogConfig.bridgeJul();

        // Add a shutdown hook so we can stop the server when the application finishes
        // This does not always work and stop() method should be called from outside to stop the server (eg: in tests)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    stop();
                }
                catch (final Exception e) {
                    e.printStackTrace(System.err);
                }
            }));

        // Do not wait until all threads finish. We want to keep going as this will be used for testing purposes.
        // The join method must be used if we want to keep the server alive until all threads finish.
        // server.join();
    }  // end method start

    /** Shutdown the SuiGeneris application and the http server. */
    public static void stop()
        throws Exception
    {
        if (server != null) {
            server.stop();
            LogConfig.stop();
            server = null;
        }
    }

    @SuppressWarnings("Duplicates")
    private static void appendExtraJars(@NotNull final WebAppContext context, @NotNull final File webAppDir) {
        // Append possible extra jars in the webapp/lib folder (eg: ojdbc.jar)

        final File   libDir    = new File(webAppDir, "/lib");
        final File[] extraJars = libDir.listFiles((dir, name) -> name.endsWith(".jar"));

        final StringBuilder builder = new StringBuilder();
        if (extraJars != null) {
            boolean notFirst = !builder.toString().isEmpty();
            for (final File extraJar : extraJars) {
                if (notFirst) builder.append(';');
                else notFirst = true;
                builder.append(extraJar.getAbsolutePath());
            }
        }

        context.setExtraClasspath(builder.toString());
    }

    //~ Static Fields ................................................................................................................................

    @Nullable private static Server server = null;
}  // end class WebApplication
