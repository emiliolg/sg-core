
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.jgroups.Address;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.jmx.notification.Notifier;
import tekgenesis.cluster.jmx.notification.Operation;
import tekgenesis.common.core.Tuple;
import tekgenesis.sg.g.NodeEntryTable;
import tekgenesis.task.TaskStatus;

import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.sg.g.TaskEntryTable.TASK_ENTRY;
import static tekgenesis.task.jmx.EndpointResolver.resolve;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * Cluster.
 */
public class Cluster implements ClusterMBean {

    //~ Instance Fields ..............................................................................................................................

    private final ClusterManager<Address> clusterManager;

    //~ Constructors .................................................................................................................................

    /**  */
    public Cluster(ClusterManager<Address> clusterManager) {
        this.clusterManager = clusterManager;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean areRunningTasks() {
        return selectFrom(TASK_ENTRY).where(TASK_ENTRY.STATUS.eq(TaskStatus.RUNNING)).count() > 0;
    }

    @Nullable @Override public Tuple<Boolean, String> checkDbCompatibility(int build, String branchName) {
        // UpgradeUtil.checkDbCompatibility(build, branchName);
        return null;
    }

    @Override public void restart() {
        final Address current             = clusterManager.getMember();
        RemoteMember  currentRemoteMember = null;
        for (final RemoteMember remoteMember : getMembers()) {
            if (current.equals(remoteMember.getAddress())) currentRemoteMember = remoteMember;
            else remoteMember.restart();
        }

        try {
            // Wait 1 sec before restart me
            Thread.sleep(1000);
        }
        catch (final InterruptedException e) {
            // ignore
        }
        if (currentRemoteMember != null) currentRemoteMember.restart();
    }

    @Override public void upgrade(int buildNumber, String branchName, boolean healthCheckEnable)
        throws Exception
    {
        Notifier.broadcastCall(Operation.UPDATE_VERSION, buildNumber, branchName, healthCheckEnable);
    }

    @Override public List<RemoteMember> getMembers() {
        final ArrayList<RemoteMember> remoteMembers = new ArrayList<>();

        for (final Address address : clusterManager.getMembersAddresses()) {
            final RemoteMember remoteMember = new RemoteMember(address, resolve(address));
            remoteMembers.add(remoteMember);
        }

        return remoteMembers;
    }

    @Override public String getName() {
        return clusterManager.getClusterName();
    }

    @Override public List<NodeStatus> getNodeStatus() {
        return invokeInTransaction(() -> selectFrom(NodeEntryTable.NODE_ENTRY).list().map(NodeStatus::create).toList());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -8908620773226065500L;
}  // end class Cluster
