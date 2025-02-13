
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

import tekgenesis.common.util.Files;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Constants.APPLICATION_RUN_DIR;

class ApplicationUtils {

    //~ Constructors .................................................................................................................................

    private ApplicationUtils() {}

    //~ Methods ......................................................................................................................................

    /**
     * Setup the install dir system property, only if not already set.
     *
     * @return  the current system property if set. Otherwise, the recently created one
     */
    @NotNull public static String setupInstallDir() {
        String result = System.getProperty(SUIGEN_INSTALLDIR);
        if (isEmpty(result)) {
            // Use the default sandbox run dir
            result = "target/server/app/install/suigeneris";
            File installDir = new File(result);
            if (!installDir.exists()) {
                installDir = new File("../../distribution");
                result     = installDir.getAbsolutePath();
            }

            // TODO Check if there is a way of setting it in the context instead of a system property
            System.getProperties().put(SUIGEN_INSTALLDIR, installDir.getAbsolutePath());
        }
        return result;
    }

    /**
     * Setup the run dir system property.
     *
     * @param   applicationName  the name of the application
     *
     * @return  the run dir being set
     */
    @NotNull public static String setupRunDir(@NotNull final String applicationName) {
        // Get the run dir. If an application name is provided, it will use it to create an
        // specific application dir. Otherwise, it will use the default sandbox run dir
        final File runDir = new File("target/application-run/" + applicationName);
        Files.remove(runDir);

        // We cannot set this property programatically and we must use a system property instead
        final String result = runDir.getAbsolutePath();
        System.getProperties().put(APPLICATION_RUN_DIR, result);

        return result;
    }

    //~ Static Fields ................................................................................................................................

    private static final String SUIGEN_INSTALLDIR = "suigen.install.dir";
}  // end class ApplicationUtils
