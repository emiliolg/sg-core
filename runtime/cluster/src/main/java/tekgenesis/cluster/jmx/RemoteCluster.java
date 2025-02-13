
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Tuple;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxInvokerImpl;

import static tekgenesis.task.jmx.JmxConstants.*;

/**
 * Cluster Instance Wrapper class over the MBean calls.
 */
public class RemoteCluster implements ClusterMBean {

    //~ Instance Fields ..............................................................................................................................

    private JmxEndpoint conn = null;

    //~ Constructors .................................................................................................................................

    /**  */
    public RemoteCluster(@NotNull final JmxEndpoint conn) {
        this.conn = conn;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean areRunningTasks() {
        // noinspection DuplicateStringLiteralInspection
        return JmxInvokerImpl.invoker(conn).mbean(CLUSTER).getAttribute("TaskRunning");
    }

    @Override public Tuple<Boolean, String> checkDbCompatibility(int build, String branchName) {
        return JmxInvokerImpl.invoker(conn)
               .mbean(CLUSTER)
               .invoke("checkDbCompatibility", new String[] { int.class.getName(), String.class.getName() }, new Object[] { build, branchName });
    }

    @Override public void restart() {
        JmxInvokerImpl.invoker(conn).mbean(CLUSTER).invoke(RESTART, EMPTY_SIGNATURE, EMPTY_ARGS);
    }

    @Override public void upgrade(int buildNumber, String branchName, boolean healthCheck) {
        JmxInvokerImpl.invoker(conn)
            .mbean(CLUSTER)
            .invoke(UPGRADE,
                new String[] { int.class.getName(), String.class.getName(), boolean.class.getName() },
                new Object[] { buildNumber, branchName, healthCheck });
    }

    @Override public List<RemoteMember> getMembers() {
        return JmxInvokerImpl.invoker(conn).mbean(CLUSTER).getAttribute(GET_MEMBERS);
    }

    @Override public String getName() {
        return JmxInvokerImpl.invoker(conn).mbean(CLUSTER).getAttribute("Name");
    }

    @Override public List<NodeStatus> getNodeStatus() {
        return JmxInvokerImpl.invoker(conn).mbean(CLUSTER).getAttribute("NodeStatus");
    }

    //~ Static Fields ................................................................................................................................

    private static final long     serialVersionUID = -7262771994196830159L;
    private static final String[] EMPTY_SIGNATURE  = {};
    private static final Object[] EMPTY_ARGS       = {};
}  // end class RemoteCluster
