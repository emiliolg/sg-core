
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.logging.Logger;
import tekgenesis.common.service.Status;
import tekgenesis.metric.core.MeterMetric;
import tekgenesis.metric.core.MetricsFactory;
import tekgenesis.metric.core.TimeMetric;

import static tekgenesis.common.collections.Colls.set;
import static tekgenesis.metric.core.MetricSources.HANDLER;

/**
 * Http Metric by status Code.
 */
class HttpMetric extends TimeMetric {

    //~ Instance Fields ..............................................................................................................................

    private final Map<Status, MeterMetric> statusMap = new HashMap<>();

    //~ Constructors .................................................................................................................................

    /** constructor. */
    HttpMetric(@NotNull String handler, String path) {
        super(HANDLER, handler, path);
        set(Status.values()).forEach((s) -> statusMap.put(s, MetricsFactory.meter(HANDLER, handler + path, "response." + s.code())));
    }

    //~ Methods ......................................................................................................................................

    public void mark(Status status) {
        final MeterMetric meterMetric = statusMap.get(status);
        if (meterMetric == null) logger.error("Unable to store metric for http code " + status);
        else meterMetric.mark();
    }

    //~ Static Fields ................................................................................................................................

    public static final Logger logger = Logger.getLogger(HttpMetric.class);
}
