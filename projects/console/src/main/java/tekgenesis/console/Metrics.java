
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cluster.Clusters;
import tekgenesis.common.env.context.Context;
import tekgenesis.form.Action;
import tekgenesis.sg.ClusterConf;

/**
 * User class for Form: Metrics
 */
public class Metrics extends MetricsBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        createDb();
    }

    @NotNull @Override public Action selectTab() {
        final ClusterConf selected = Context.getSingleton(Clusters.class).getSelectedClusterConf();
        if (selected != null) setClusterName(selected.getName());

        final int metrics = getMetrics();

        if (metrics == 0) {
            final DatabaseMetrics db = getDb();
            if (db != null) db.onLoad();
        }

        return actions.getDefault();
    }
}
