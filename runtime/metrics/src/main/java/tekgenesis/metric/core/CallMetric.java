
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metric.core;

import org.jetbrains.annotations.NotNull;

/**
 * Call Time Meter and Status Metric.
 */
public class CallMetric extends StatusMeterMetric {

    //~ Instance Fields ..............................................................................................................................

    private final TimeMetric timer;

    //~ Constructors .................................................................................................................................

    /** constructor. */
    CallMetric(@NotNull String namespace, @NotNull String target, @NotNull String name) {
        super(namespace, target);
        timer = MetricsFactory.time(namespace, target, name);
    }

    //~ Methods ......................................................................................................................................

    @Override public CallMetric start() {
        timer.start();
        return this;
    }

    @Override public void stop() {
        timer.stop();
    }
}
