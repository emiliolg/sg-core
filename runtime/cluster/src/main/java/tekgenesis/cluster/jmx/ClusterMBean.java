
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx;

import java.io.Serializable;
import java.util.List;

import tekgenesis.common.core.Tuple;

/**
 * ClusterMBean.
 */
public interface ClusterMBean extends Serializable {

    //~ Methods ......................................................................................................................................

    /** return true if there are running tasks. */
    boolean areRunningTasks();

    /** Verify check db compatibility. */
    Tuple<Boolean, String> checkDbCompatibility(int build, String branchName);

    /** Restart all nodes. */
    void restart();

    /** Update version. */
    void upgrade(int buildNumber, String branchName, boolean healthCheckEnable)
        throws Exception;

    /** List of members. */
    List<RemoteMember> getMembers();

    /** Cluster name. */
    String getName();

    /** @return  the list of UpgradeStatus for all the available nodes */
    List<NodeStatus> getNodeStatus();
}
