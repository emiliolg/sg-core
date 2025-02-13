
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.tools.test;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.app.properties.JmxServiceProps;
import tekgenesis.app.service.JmxService;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.impl.MemoryEnvironment;
import tekgenesis.service.ServiceProps;
import tekgenesis.task.TaskServiceProps;

import static tekgenesis.common.core.Constants.HTTP_LOCALHOST;
import static tekgenesis.common.core.Strings.deCapitalizeFirst;
import static tekgenesis.common.tools.test.StatementBuilder.create;

/**
 * Abstract Web App Test.
 */

public class ApplicationRule implements TestRule {

    //~ Methods ......................................................................................................................................

    @Override public Statement apply(final Statement statement, Description description) {
        return create(statement, this::after, this::before);
    }

    /** Return localhost server url with specified port of 8080. */
    public String getServerUrl() {
        return HTTP_LOCALHOST + getPort();
    }

    protected void after()
        throws Exception
    {
        // Only if the service was initialized
        WebApplication.stop();
        Context.getContext().clean();
    }

    protected void before()
        throws Exception
    {
        final Environment      environment  = createEnvironment();
        final TaskServiceProps serviceProps = new TaskServiceProps();
        configureTaskProperties(serviceProps);
        environment.put(serviceProps);

        final JmxServiceProps jmxServiceProps = new JmxServiceProps();
        configureJmxServiceProperties(jmxServiceProps);
        environment.put(deCapitalizeFirst(JmxService.SERVICE_NAME), jmxServiceProps);

        final ApplicationProps applicationProps = new ApplicationProps();
        configureAppProperties(applicationProps);
        environment.put(applicationProps);

        Context.getContext().setSingleton(Environment.class, environment);
        WebApplication.start(environment, "AppTest", Application.getProfile(), getPort(), true);
    }

    protected void configureAppProperties(final ApplicationProps prop) {
        prop.noCluster = true;
    }
    protected void configureJmxServiceProperties(final ServiceProps serviceProps) {
        serviceProps.enabled = false;
    }

    protected void configureTaskProperties(final TaskServiceProps serviceProps) {
        serviceProps.enabled = false;
    }

    protected Environment createEnvironment() {
        return new MemoryEnvironment();
    }

    protected int getPort() {
        try {
            return Integer.valueOf(System.getProperty("test.port", DEFAULT_PORT + ""));
        }
        catch (final Throwable t) {
            return DEFAULT_PORT;
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final int DEFAULT_PORT = 8080;
}  // end class ApplicationRule
