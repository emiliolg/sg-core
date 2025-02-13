
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.selenium;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.impl.MemoryEnvironment;
import tekgenesis.common.tools.test.Application;
import tekgenesis.common.tools.test.PropertiesProfile;
import tekgenesis.common.tools.test.WebApplication;

import static tekgenesis.common.core.Constants.HTTP_LOCALHOST;

/**
 * Utility class that starts a web application by using a selenium configuration.
 */
@SuppressWarnings({ "MagicNumber", "DuplicateStringLiteralInspection", "WeakerAccess" })
public class SeleniumWebApplication {

    //~ Constructors .................................................................................................................................

    private SeleniumWebApplication() {}

    //~ Methods ......................................................................................................................................

    /** Start SuiGeneris instance and web server. */
    public static void start(@NotNull final String applicationName)
        throws Exception
    {
        start(applicationName, Application.getProfile(), new MemoryEnvironment());
    }

    /** Start SuiGeneris instance and web server with environment. */

    public static void start(@NotNull final String applicationName, Environment env)
        throws Exception
    {
        start(applicationName, Application.getProfile(), env);
    }

    /** Start SuiGeneris instance with profile and environment. */

    public static void start(@NotNull final String applicationName, @NotNull final PropertiesProfile profile, Environment env)
        throws Exception
    {
        // final ApplicationProps applicationProps = env.get(ApplicationProps.class);
        // applicationProps.noCluster = true;
        // env.put(applicationProps);
        Context.getContext().setSingleton(Environment.class, env);
        final int port = Integer.parseInt(System.getProperty(PORT_PROP, String.valueOf(PORT)));
        loadDefaultSeleniumSystemProperties(port);
        if (!Boolean.getBoolean(USE_EXTERNAL_SERVER)) WebApplication.start(env, applicationName, profile, port, true);
    }

    /** Stop SuiGeneris instance and web server. */
    public static void stop()
        throws Exception
    {
        if (!Boolean.getBoolean(USE_EXTERNAL_SERVER)) WebApplication.stop();
        Context.getContext().clean();
    }

    private static String buildURL(final int port) {
        return HTTP_LOCALHOST + port;
    }

    private static void loadDefaultSeleniumSystemProperties(final int port) {
        // Set some system properties needed from AbstractSeleniumTest (if not already set)

        System.setProperty(PORT_PROP, String.valueOf(port));

        if (System.getProperty(BASE_URL_PROP) == null) System.setProperty(BASE_URL_PROP, buildURL(port));

        if (System.getProperty(USER_PROP) == null) System.setProperty(USER_PROP, USER);

        if (System.getProperty(PASSWORD_PROP) == null) System.setProperty(PASSWORD_PROP, PASSWORD);

        System.setProperty(Constants.APPLICATION_RESET_DB, "true");
        System.setProperty(Constants.SUIGEN_DEVMODE, "true");
        System.setProperty(Constants.WAIT_FOR_INDEX, "true");
        System.setProperty("application.seedDB", "true");
        System.setProperty("application.noCluster", "true");
    }

    //~ Static Fields ................................................................................................................................

    private static final int    PORT     = 8024;
    private static final String USER     = "admin";
    private static final String PASSWORD = "password";

    private static final String BASE_URL_PROP       = "suigen.selenium.baseUrl";
    private static final String USE_EXTERNAL_SERVER = "suigen.selenium.externalServer";
    private static final String USER_PROP           = "suigen.selenium.user";
    private static final String PASSWORD_PROP       = "suigen.selenium.password";
    private static final String PORT_PROP           = "suigen.selenium.port";
}  // end class SeleniumWebApplication
