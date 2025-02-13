
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

import tekgenesis.form.Action;
import tekgenesis.persistence.Sql;
import tekgenesis.sg.ClusterConf;
import tekgenesis.sg.ClusterStatus;

import static tekgenesis.sg.g.ClusterConfTable.CLUSTER_CONF;

/**
 * User class for Form: ClusterListForm
 */
public class ClusterListForm extends ClusterListFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action createNew() {
        return actions.navigate(ClusterForm.class);
    }

    /** Invoked when the form is loaded. */
    @Override public void init() {
        setLoading(true);
        getClusters().clear();
        ClusterConf.forEach(c -> getClusters().add().populate(c));
        setLoading(false);
    }

    //~ Inner Classes ................................................................................................................................

    public class ClustersRow extends ClustersRowBase {
        public void populate(@NotNull final ClusterConf d) {
            setName(d.getName());
            setSelectCluster(d.getStatus() == ClusterStatus.ACTIVE);
        }

        @NotNull @Override public Action selectCurrentCluster() {
            if (isLoading()) return actions.getDefault();
            final ClustersRow current        = getClusters().getCurrent();
            final ClusterConf currentCluster = Sql.selectFrom(CLUSTER_CONF).where(CLUSTER_CONF.STATUS.eq(ClusterStatus.ACTIVE)).get();
            if (currentCluster != null) {
                currentCluster.setStatus(ClusterStatus.DEACTIVE);
                currentCluster.persist();
            }

            final ClusterConf selectedCluster = ClusterConf.find(current.getName());
            if (selectedCluster == null) return actions.getError().withMessage("Selected Cluster not found");
            selectedCluster.setStatus(ClusterStatus.ACTIVE);
            selectedCluster.persist();
            init();
            return actions.getDefault();
        }
    }
}  // end class ClusterListForm
