
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metric.reporter;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.common.core.Constants;
import tekgenesis.common.env.context.Context;
import tekgenesis.metric.MetricProps;
import tekgenesis.metric.service.MetricService;

/**
 * Reporter base class.
 */
public abstract class Reporter {

    //~ Methods ......................................................................................................................................

    /** start. */
    public abstract void start(@NotNull MetricService service);
    /** Stop. */
    public abstract void stop();

    /** Returns the metric property configuration. */
    public MetricProps getProps() {
        return Context.getEnvironment().get(MetricProps.class);
    }

    String getPrefix() {
        String hostname = Constants.LOCALHOST;
        if (Context.getContext().hasBinding(ClusterManager.class)) hostname = Context.getContext().getSingleton(ClusterManager.class).getMemberName();
        return getProps().domain + "." + hostname.replace(".", "_").replace(" ", "_");
    }
}
