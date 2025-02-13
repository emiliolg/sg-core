
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.eventbus.test;

import org.jetbrains.annotations.Nullable;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import tekgenesis.eventbus.EventBus;
import tekgenesis.eventbus.EventBusProps;

import static tekgenesis.common.env.context.Context.getEnvironment;

/**
 * EventBus Rule.
 */
public abstract class EventBusRule implements TestRule {

    //~ Methods ......................................................................................................................................

    @Override public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override public void evaluate()
                throws Throwable
            {
                before();
                try {
                    base.evaluate();
                }
                catch (final Exception e) {
                    e.printStackTrace(System.err);
                }
                finally {
                    after();
                }
            }
        };
    }

    public abstract String getImplementation();
    @Nullable public abstract Object getProps();

    /** Override to tear down your specific external resource. */
    protected void after() {
        EventBus.shutdown();
    }

    /**
     * Override to set up your specific external resource.
     *
     * @throws  Throwable  if setup fails (which will disable {@code after}
     */
    protected void before()
        throws Throwable
    {
        final EventBusProps p = new EventBusProps();
        p.implementation = getImplementation();
        p.enabled        = true;

        getEnvironment().put(p);
        final Object props = getProps();
        if (props != null) getEnvironment().put(props);
    }
}  // end class EventBusRule
