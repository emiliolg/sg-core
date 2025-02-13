
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.selenium;

import java.io.File;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.service.DriverService;

import tekgenesis.app.properties.JmxServiceProps;
import tekgenesis.app.service.JmxService;
import tekgenesis.common.env.impl.MemoryEnvironment;

import static tekgenesis.common.core.Strings.deCapitalizeFirst;
import static tekgenesis.common.tools.test.StatementBuilder.create;

@SuppressWarnings("JavaDoc")
public class SeleniumRule implements TestRule {

    //~ Instance Fields ..............................................................................................................................

    protected MemoryEnvironment env     = new MemoryEnvironment();
    private DriverService       service = null;

    //~ Constructors .................................................................................................................................

    public SeleniumRule() {}

    //~ Methods ......................................................................................................................................

    @Override public Statement apply(final Statement statement, Description description) {
        return create(statement, this::after, this::before);
    }

    /** Returns a chrome driver service already initialized. */
    public DriverService getService() {
        return service;
    }

    protected void before()
        throws Exception
    {
        resetEnv();
        SeleniumWebApplication.start("CatalogSeleniumTest", env);
        final String hubUrl = System.getProperty(SeleniumDriverRule.SELENIUM_HUB_URL, "");
        if (hubUrl.isEmpty()) {
            service = new ChromeDriverService.Builder().usingDriverExecutable(new File(System.getProperty("user.home"), "/chromedriver"))
                      .usingAnyFreePort()
                      .build();
            service.start();
        }
    }

    protected void resetEnv() {
        env = new MemoryEnvironment();
        final JmxServiceProps props = new JmxServiceProps();
        props.enabled = false;
        env.put(deCapitalizeFirst(JmxService.SERVICE_NAME), props);
    }

    private void after()
        throws Exception
    {
        // Only if the service was initialized
        SeleniumWebApplication.stop();
        if (service != null) service.stop();
        env.dispose();
    }
}  // end class SeleniumRule
