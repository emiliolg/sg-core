
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metric.reporter;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.graphite.Graphite;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.context.Context;
import tekgenesis.metric.service.MetricService;

/**
 * Graphite Reporter base on dropwizard Metrics.
 */
class GraphiteReporter extends Reporter {

    //~ Instance Fields ..............................................................................................................................

    private com.codahale.metrics.graphite.GraphiteReporter reporter = null;

    //~ Methods ......................................................................................................................................

    @Override public void start(@NotNull MetricService service) {
        final GraphiteProps graphiteProps = Context.getEnvironment().get(GraphiteProps.class);

        final Graphite graphite = new Graphite(new InetSocketAddress(graphiteProps.host, graphiteProps.port));

        reporter = com.codahale.metrics.graphite.GraphiteReporter.forRegistry(MetricService.getMetricRegistry().get())
                   .prefixedWith(getPrefix())
                   .convertRatesTo(TimeUnit.SECONDS)
                   .convertDurationsTo(TimeUnit.MILLISECONDS)
                   .filter(MetricFilter.ALL)
                   .build(graphite);
        reporter.start(graphiteProps.publishRate, TimeUnit.SECONDS);
    }

    @Override public void stop() {
        reporter.stop();
    }
}
