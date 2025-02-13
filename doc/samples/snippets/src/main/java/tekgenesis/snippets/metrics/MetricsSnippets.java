
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.snippets.metrics;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metric.core.CallMetric;
import tekgenesis.metric.core.MeterMetric;
import tekgenesis.metric.core.MetricsFactory;
import tekgenesis.metric.core.TimeMetric;

/**
 * Sample snippets for metrics.
 */
public class MetricsSnippets {

    //~ Constructors .................................................................................................................................

    private MetricsSnippets() {}

    //~ Methods ......................................................................................................................................

    void testMetrics() {
        final String myMethod    = "myMethod";
        final String target      = "myTarget";
        final String myNamespace = "MyNamespace";

        // #meter
        try(MeterMetric meterMetric = MetricsFactory.meter(myNamespace, target, myMethod)) {
            // Do anything you want to record as a metric
            meterMetric.mark();
        }
        // #meter
        // #time
        try(TimeMetric timeMetric = MetricsFactory.time(myNamespace, target, myMethod).start()) {
            // Do anything you want to record as a metric
            doSomething(timeMetric);
        }
        // #time
        // #time2
        final TimeMetric timeMetric = MetricsFactory.time(myNamespace, target, myMethod);
        timeMetric.start();
        // ...
        timeMetric.stop();
        // #time2
        final Class<?> targetClass = getClass();
        // #call
        final CallMetric callMetric = MetricsFactory.call(myNamespace, targetClass, myMethod).start();

        try {
            // ....
            callMetric.mark(true);
        }
        catch (final Exception e) {
            callMetric.mark(false);
        }
        finally {
            callMetric.stop();
        }
        // #call
    }

    private void doSomething(@NotNull TimeMetric timeMetric) {
        System.err.println(timeMetric);
    }
}
