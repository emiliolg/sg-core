
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metric.core;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metric.service.MetricService;

import static tekgenesis.metric.core.MetricNamespace.resolveSource;

/**
 * Time Metric. A time metrics is basically a histogram of the duration of a type of event and a
 * meter of the rate of its occurrence.
 */
public class TimeMetric implements Metric {

    //~ Instance Fields ..............................................................................................................................

    private Timer.Context context = null;

    private Option<Timer> timer = Option.empty();

    //~ Constructors .................................................................................................................................

    /** Time Metric constructor. */
    protected TimeMetric(@NotNull String namespace, @NotNull String source, @NotNull String name) {
        MetricService.getMetricRegistry().ifPresent((r) -> timer = Option.of(r.timer(MetricRegistry.name(namespace, resolveSource(source), name))));
    }

    //~ Methods ......................................................................................................................................

    @Override public TimeMetric start() {
        timer.ifPresent((t) -> context = t.time());
        return this;
    }

    @Override public void stop() {
        if (context != null) context.stop();
    }
}
