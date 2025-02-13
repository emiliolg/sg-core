
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metric.core;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metric.service.MetricService;

import static tekgenesis.metric.core.MetricNamespace.resolveSource;

/**
 * Meter Metric. Meters measure the rate of the events in a few different ways
 */
public class MeterMetric implements Metric {

    //~ Instance Fields ..............................................................................................................................

    private Option<Meter> meter = Option.empty();

    //~ Constructors .................................................................................................................................

    /** Time Metric constructor. */
    MeterMetric(@NotNull String namespace, @NotNull String source, @NotNull String method) {
        MetricService.getMetricRegistry().ifPresent((r) -> meter = Option.of(r.meter(MetricRegistry.name(namespace, resolveSource(source), method))));
    }

    //~ Methods ......................................................................................................................................

    /** Mark the occurrence of an event. */
    public void mark() {
        meter.ifPresent(Meter::mark);
    }

    @Override public MeterMetric start() {
        return this;
    }
}
