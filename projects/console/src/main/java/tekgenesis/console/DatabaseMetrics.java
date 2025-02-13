
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanInfo;
import javax.management.ObjectName;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.form.Action;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.task.jmx.JmxConstants.HIKARI_MBEAN;
import static tekgenesis.task.jmx.JmxConstants.HIKARI_MBEANS_QUERY;

/**
 * User class for Form: DatabaseMetrics
 */
public class DatabaseMetrics extends DatabaseMetricsBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void onLoad() {
        loadStatistics(DEFAULT);
        loadPools();
    }

    /**
     * Invoked when combo_box(boneCPPools) value changes Invoked when button(refreshPools) is
     * clicked.
     */
    @NotNull @Override public Action selectPool() {
        if (isDefined(Field.POOLS)) {
            final String pools = getPools();
            loadStatistics(pools);
        }
        return actions.getDefault();
    }

    private void loadPools() {
        final Set<String>        uniquePools   = new HashSet<>();
        final List<RemoteMember> remoteMembers = Context.getSingleton(Clusters.class).getActiveCluster().get().getMembers();

        for (final RemoteMember remoteMember : remoteMembers) {
            final JmxEndpoint         conn        = remoteMember.getJmxEndpoint();
            final HashSet<ObjectName> mBeansNames = JmxInvokerImpl.invoker(conn).mbean("").queryMBean(HIKARI_MBEANS_QUERY);
            final Seq<String>         pools       = map(mBeansNames,
                    value -> {
                        final String canonicalName = value.getCanonicalName();

                        return notEmpty(canonicalName, DEFAULT);
                    });
            uniquePools.addAll(pools.toList());
        }

        setPoolsOptions(uniquePools);
    }

    private void loadStatistics(@NotNull String name) {
        final List<RemoteMember> remoteMembers = Context.getSingleton(Clusters.class).getActiveCluster().get().getMembers();
        getConnectionsTab().clear();

        for (final RemoteMember remoteMember : remoteMembers) {
            final JmxEndpoint jmxConfiguration = remoteMember.getJmxEndpoint();
            if (jmxConfiguration.isAvailable()) {
                final String    mbeanName = HIKARI_MBEAN + (name.equals(DEFAULT) ? "" : name);
                final MBeanInfo mbeanInfo = JmxInvokerImpl.invoker(jmxConfiguration).mbean(mbeanName).getInfo();

                if (mbeanInfo != null) {
                    final AttributeList   attributesValue = JmxInvokerImpl.invoker(jmxConfiguration).mbean(mbeanName).getAttributesValue(ATTRS);
                    final List<Attribute> attributes      = attributesValue.asList();

                    final String            hostName          = remoteMember.getName();
                    final ConnectionsTabRow connectionsTabRow = getConnectionsTab().add();
                    connectionsTabRow.setConNode(hostName);
                    connectionsTabRow.populate(attributes);
                }
            }
        }
    }  // end method loadStatistics

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String[] ATTRS = { "ActiveConnections", "IdleConnections", "TotalConnections", "ThreadAwaitingConnection" };

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String DEFAULT = "Default";

    //~ Inner Classes ................................................................................................................................

    public class ConnectionsTabRow extends ConnectionsTabRowBase {
        /**  */
        public void populate(List<Attribute> list) {
            setTotalConnections((Integer) list.get(0).getValue());
            setTotalActiveConnections((Integer) list.get(0).getValue());
            setTotalIdleConnections((Integer) list.get(1).getValue());
            setTotalWaitingConnections((Integer) list.get(2).getValue());
        }
    }
}  // end class DatabaseMetrics
