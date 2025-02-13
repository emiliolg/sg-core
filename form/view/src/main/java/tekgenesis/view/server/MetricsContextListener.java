
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilterContextListener;

import org.jetbrains.annotations.Nullable;

import tekgenesis.metric.service.MetricService;

/**
 * Context listener that stars and configures Metrics.
 */
public class MetricsContextListener extends InstrumentedFilterContextListener {

    //~ Constructors .................................................................................................................................

    /** Configure. */
    public MetricsContextListener() {}

    //~ Methods ......................................................................................................................................

    @Nullable @Override protected MetricRegistry getMetricRegistry() {
        return MetricService.getMetricRegistry().getOrNull();
    }
}
