
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
 * Success/Fail Meter Metric.
 */
public class StatusMeterMetric implements Metric {

    //~ Instance Fields ..............................................................................................................................

    private final MeterMetric failedMeter;
    private final MeterMetric successMeter;

    //~ Constructors .................................................................................................................................

    /**
     * Creates a new StatusMeterMetric for the specified namespace and name.
     *
     * @param  namespace  the namespace
     * @param  name       the metric name
     */
    public StatusMeterMetric(@NotNull String namespace, @NotNull String name) {
        // noinspection DuplicateStringLiteralInspection
        successMeter = MetricsFactory.meter(namespace, name, "status.succeeded");
        // noinspection DuplicateStringLiteralInspection
        failedMeter = MetricsFactory.meter(namespace, name, "status.failed");
    }

    //~ Methods ......................................................................................................................................

    /**
     * Marks the occurrence of an event. It can be a successful or failure mark according to the
     * success argument.
     *
     * @param  success  whether the mark should be a success or failure mark
     */
    public void mark(boolean success) {
        if (success) succeeded();
        else failed();
    }

    /** Mark as failed. */
    private void failed() {
        failedMeter.mark();
    }

    /** Mark as success. */
    private void succeeded() {
        successMeter.mark();
    }
}  // end class StatusMeterMetric
