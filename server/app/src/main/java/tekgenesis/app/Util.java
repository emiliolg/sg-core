
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Tuple3;
import tekgenesis.common.logging.Logger;

import static java.lang.String.valueOf;

import static tekgenesis.common.core.Constants.APPLICATION_RUN_DIR;
import static tekgenesis.common.core.Tuple.tuple;

/**
 * Util class.
 */
public class Util {

    //~ Constructors .................................................................................................................................

    private Util() {}

    //~ Methods ......................................................................................................................................

    static void handleUncaughtExceptions(final Logger logger) {
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
            logger.error(e);
            System.err.println("SuiGeneris command failed!");
            System.exit(1);
        });
    }

    static void setPropertiesOrExit(final File webAppDir, final String runDir, final int port, final boolean resetDb, @Nullable final String safeMode,
                                    final String sgProperties, int httpsPort) {
        try {
            setProperties(webAppDir, runDir, port, resetDb, safeMode, sgProperties, httpsPort);
        }
        catch (final Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static Tuple3<String, String, String> findVersion(final File dir) {
        try {
            final Properties properties = new Properties();
            properties.load(new FileReader(new File(dir.getParentFile(), Constants.VERSION_PROPERTIES)));
            return tuple(properties.getProperty(Constants.BUILD_VERSION),
                properties.getProperty(Constants.BUILD_NUMBER),
                properties.getProperty(Constants.BRANCH));
        }
        catch (final IOException e) {
            return tuple("", "", "");
        }
    }

    private static void setProperties(final File webAppDir, final String runDir, final int port, final boolean resetDb, final String safeMode,
                                      final String sgProperties, int httpsPort)
        throws IOException
    {
        final Tuple3<String, String, String> build = findVersion(webAppDir);
        System.out.println("Starting Sui Generis version: " + build.first() + "  build: " + build.second());

        final Properties ps = System.getProperties();
        ps.put(Constants.WEBAPP_DIR, webAppDir.getCanonicalPath());
        ps.put(BIN_DIR, new File(webAppDir.getParent(), Constants.BIN).getCanonicalPath());
        ps.put(APPLICATION_RUN_DIR, runDir);
        ps.put(Constants.PORT_OPT, valueOf(port));
        ps.put(RESET_DB, String.valueOf(resetDb));
        // noinspection DuplicateStringLiteralInspection
        ps.put("product.version", build.first());
        ps.put(Constants.SUIGENERIS_VERSION, build.first());
        ps.put(Constants.SUIGENERIS_BUILD, build.second());
        ps.put(Constants.SUIGENERIS_BRANCH, build.third());
        if (httpsPort != 0) ps.put(Constants.HTTPS_PORT, String.valueOf(httpsPort));
        if (Predefined.isNotEmpty(safeMode))
        // noinspection DuplicateStringLiteralInspection
        ps.put("server.safeMode", safeMode);
        ps.put("java.net.preferIPv4Stack", "true");
        if (!sgProperties.isEmpty()) ps.put(SG_PROPERTIES, sgProperties);
    }

    //~ Static Fields ................................................................................................................................

    private static final String BIN_DIR       = Constants.BIN_DIR;
    private static final String RESET_DB      = Constants.APPLICATION_RESET_DB;
    private static final String SG_PROPERTIES = Constants.SUIGEN_PROPS;
}  // end class Util
