
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import tekgenesis.app.SuiGenerisInstance;
import tekgenesis.app.env.DatabaseEnvironment;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;

/**
 * Server context that runs on application server startup.
 */
@SuppressWarnings("WeakerAccess")
public class ApplicationContextListener implements ServletContextListener {

    //~ Instance Fields ..............................................................................................................................

    private SuiGenerisInstance app    = null;
    private final Logger       logger = Logger.getLogger(ApplicationContextListener.class);

    //~ Methods ......................................................................................................................................

    @Override public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (app != null) app.shutdown();
    }

    @Override public void contextInitialized(final ServletContextEvent event) {
        try {
            Context.bind(Environment.class, DatabaseEnvironment.class);
            app = Context.getEnvironment() != null ? new SuiGenerisInstance(Context.getEnvironment(), true) : new SuiGenerisInstance(true);
            app.withAllServices();
            app.startup();
        }
        catch (final Throwable e) {
            logger.error(e);
            throw e;
        }
    }
}
