
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

import org.jetbrains.annotations.NotNull;

import tekgenesis.app.SuiGenerisInstance;
import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.impl.MemoryEnvironment;
import tekgenesis.common.env.logging.LogConfig;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.util.Files;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Constants.INDEX;

/**
 * SuiGeneris application tools. TODO mgutierrez what?
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "WeakerAccess" })
public class Application {

    //~ Constructors .................................................................................................................................

    private Application() {}

    //~ Methods ......................................................................................................................................

    /** Rebuild lucene indexes immediatly instead of waiting for scheduler to run. */
    public static void rebuildIndex() {
        // if (indexWriter == null || indexConsumer == null)
        // throw new IllegalStateException("Indexes were not initialized. Start the application before rebuilding indexes");

        // indexWriter.close();

        final File indexDir = getIndexDirectory();
        Files.remove(indexDir);

        // noinspection ResultOfMethodCallIgnored
        indexDir.mkdirs();

        // indexConsumer.checkIndex();
    }

    /** Startup the SuiGeneris application. */
    public static void start(@NotNull String applicationName, boolean resetDb) {
        start(applicationName, getProfile(), resetDb);
    }

    /** Startup the SuiGeneris application. */
    @SuppressWarnings("MagicNumber")
    public static void start(@NotNull final String applicationName, @NotNull final PropertiesProfile profile, final boolean resetDb) {
        // Just in case it is running....
        stop();

        // Set the run dir property before initializing the application as it is used by the configure method
        final String runDir = ApplicationUtils.setupRunDir(applicationName);

        // Configure the application
        final Environment env = new MemoryEnvironment();
        app = new SuiGenerisInstance(env, false);

        // Setup profile properties
        profile.setup(env, resetDb, runDir);

        // Remove existing application files
        Files.remove(new File(runDir));

        // Startup the application in test mode, so consumers are not initialized automatically. By this way,
        // we can retain index writer and consumer for future operations (like rebuilding indexes)
        app.startup();

        // initConsumers();

        SecurityUtils.getSession().authenticate("admin", "password");

        // Re-bridging JUL, since shiro seems to break it.
        LogConfig.bridgeJul();
    }

    /** Shutdown the SuiGeneris application. */
    public static void stop() {
        if (app != null) app.shutdown();

        // if (indexWriter != null) indexWriter.close();
        // indexWriter   = null;
        // indexConsumer = null;
    }

    /** Returns the System Property profile configuration. */
    @NotNull public static PropertiesProfile getProfile() {
        final String      profile           = System.getProperty("test.databases");
        PropertiesProfile propertiesProfile = null;
        if (!isEmpty(profile)) {
            if ("oracle".equalsIgnoreCase(profile)) propertiesProfile = PropertiesProfile.TEST_ORACLE;
            else if ("postgres".equalsIgnoreCase(profile)) propertiesProfile = PropertiesProfile.TEST_POSTGRES;
            else if ("none".equalsIgnoreCase(profile)) propertiesProfile = PropertiesProfile.NONE;
        }

        return propertiesProfile == null ? PropertiesProfile.TEST_HSQLDB : propertiesProfile;
    }

    private static File getIndexDirectory() {
        final ApplicationProps applicationProps = Context.getProperties(ApplicationProps.class);
        return new File(applicationProps.runDir, INDEX);
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("RedundantFieldInitialization")
    private static SuiGenerisInstance app = null;

    // @SuppressWarnings("RedundantFieldInitialization")
    // private static EntityIndexWriter indexWriter   = null;
    // @SuppressWarnings("RedundantFieldInitialization")
    // private static IndexConsumer     indexConsumer = null;
}  // end class Application
