
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metric.service;

import java.io.IOException;

import com.codahale.metrics.JvmAttributeGaugeSet;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.metric.MetricProps;
import tekgenesis.metric.reporter.Reporter;
import tekgenesis.metric.reporter.ReporterBuilder;
import tekgenesis.service.Service;
import tekgenesis.service.ServiceManager;

/**
 * Metric Service.
 */
public class MetricService extends Service {

    //~ Instance Fields ..............................................................................................................................

    private MetricRegistry metricRegistry = null;

    private Option<Reporter> reporter = null;

    //~ Constructors .................................................................................................................................

    /** default constructor. */
    public MetricService(ServiceManager serviceManager) {
        super(serviceManager, SERVICE_NAME, SERVICE_START_ORDER, MetricProps.class);
    }

    //~ Methods ......................................................................................................................................

    @Override public final MetricProps getProperties() {
        return (MetricProps) super.getProperties();
    }

    @Override protected void doShutdown() {
        reporter.ifPresent(Reporter::stop);
    }

    @Override protected void doStart()
        throws IOException
    {
        final MetricProps properties = getProperties();
        reporter = ReporterBuilder.build(properties.reporter);

        if (reporter.isPresent()) {
            metricRegistry = new MetricRegistry();
            reporter.get().start(this);
            builtInMetrics();
        }
        else logger.warning("Unable to found a reporter of type " + properties.reporter);
    }

    private void builtInMetrics() {
        metricRegistry.register("jvm", new JvmAttributeGaugeSet());
        metricRegistry.register("mem", new MemoryUsageGaugeSet());
        metricRegistry.register("gc", new GarbageCollectorMetricSet());
        // noinspection DuplicateStringLiteralInspection
        metricRegistry.register("thread", new ThreadStatesGaugeSet());
    }

    private MetricRegistry getRegistry() {
        return metricRegistry;
    }

    //~ Methods ......................................................................................................................................

    /** @return  MetricRegistry */
    public static Option<MetricRegistry> getMetricRegistry() {
        if (!Context.getContext().hasBinding(ServiceManager.class)) return Option.empty();
        final Option<MetricService> service = Context.getContext().getSingleton(ServiceManager.class).getService(MetricService.class);
        return service.map(MetricService::getRegistry);
    }

    //~ Static Fields ................................................................................................................................

    public static final String SERVICE_NAME        = MetricService.class.getSimpleName();
    private static final int   SERVICE_START_ORDER = 10;
}
