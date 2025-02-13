
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metric.reporter;

import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metric.service.MetricService;

/**
 * Jmx reporter.
 */
public class JmxReporter extends Reporter {

    //~ Instance Fields ..............................................................................................................................

    private com.codahale.metrics.JmxReporter reporter = null;

    //~ Methods ......................................................................................................................................

    @Override public void start(@NotNull MetricService service) {
        reporter = com.codahale.metrics.JmxReporter.forRegistry(MetricService.getMetricRegistry().get())
                   .inDomain(getPrefix())
                   .convertRatesTo(TimeUnit.SECONDS)
                   .convertDurationsTo(TimeUnit.MILLISECONDS)
                   .build();

        reporter.start();
    }

    @Override public void stop() {
        reporter.stop();
    }
}
