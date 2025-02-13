
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanInfo;
import javax.management.ObjectName;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.RemoteCluster;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxInvoker;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.common.jmx.MBeanResourceImpl;
import tekgenesis.database.DatabaseConfig;
import tekgenesis.database.hikari.HikariDatabaseConfig;
import tekgenesis.form.Action;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.env.context.Context.getEnvironment;
import static tekgenesis.console.ConsoleConstants.*;
import static tekgenesis.task.jmx.JmxConstants.HIKARI_MBEAN;
import static tekgenesis.task.jmx.JmxConstants.HIKARI_MBEANS_QUERY;

/**
 * User class for Form: DatabaseForm
 */
public class DatabaseForm extends DatabaseFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void onLoad() {
        loadStatistics(DEFAULT);
        final RemoteCluster cluster = Context.getSingleton(Clusters.class).getActiveCluster().get();
        loadHikariPools(cluster);
        loadDatabaseInfo();
    }

    @NotNull @Override public Action selectPool() {
        if (isDefined(DatabaseFormBase.Field.HIKARI_POOLS)) {
            final String polls = getHikariPools();
            loadStatistics(polls);
        }
        return actions.getDefault();
    }

    @NotNull private String beanName(final ObjectName value) {
        final String canonicalName = value.getCanonicalName();

        final int    idx  = canonicalName.indexOf("(");
        final String name = canonicalName.substring(idx + 1, canonicalName.length() - 1);

        return notEmpty(name, DEFAULT);
    }

    private void loadDatabaseInfo() {
        final DatabaseConfig databaseConfig = getEnvironment().get(HikariDatabaseConfig.class);
        setDbDriver(databaseConfig.driverClass);
        setDbType(databaseConfig.type.toString());
        setDbUrl(databaseConfig.jdbcUrl);
        setEnforceVersion(databaseConfig.enforceVersion);
        setSessionCacheSize(databaseConfig.sessionCacheSize);
    }

    private void loadHikariPools(@NotNull RemoteCluster cluster) {
        final Set<String> uniquePools = new HashSet<>();

        final List<RemoteMember> members = cluster.getMembers();
        for (final RemoteMember member : members)
            uniquePools.addAll(
                map(JmxInvokerImpl.invoker(member.getJmxEndpoint()).mbean("").queryMBean(HIKARI_MBEANS_QUERY), this::beanName).toList());

        setHikariPoolsOptions(uniquePools);
    }

    private void loadStatistics(@NotNull String name) {
        final RemoteCluster cluster = Context.getSingleton(Clusters.class).getActiveCluster().get();

        final List<RemoteMember> members = cluster.getMembers();
        getConnectionsTab().clear();

        for (final RemoteMember member : members) {
            final JmxEndpoint jmxConfiguration = member.getJmxEndpoint();
            if (jmxConfiguration.isAvailable()) {
                final String mbeanName = getMbeanName(name);

                final JmxInvoker        jmxInvoker = JmxInvokerImpl.invoker(jmxConfiguration);
                final MBeanResourceImpl mbean      = jmxInvoker.mbean(mbeanName);
                final MBeanInfo         mbeanInfo  = mbean.getInfo();

                if (mbeanInfo != null) {
                    final AttributeList attributesValue = mbean.getAttributesValue(ATTRS);

                    final ImmutableList<Attribute> list = immutable(attributesValue.asList());

                    final InetAddress       inetAddress = member.getJmxEndpoint().getAddress();
                    final String            hostName    = inetAddress == null ? "N/A" : inetAddress.getHostName();
                    final ConnectionsTabRow row         = getConnectionsTab().add();

                    setTotalFor(list, ACTIVE_CONNECTIONS, row::setTotalActiveConnections);
                    setTotalFor(list, TOTAL_CONNECTIONS, row::setTotalConnections);
                    setTotalFor(list, IDLE_CONNECTIONS, row::setTotalIdleConnections);
                    setTotalFor(list, THREAD_WAITING_CONNECTIONS, row::setTotalWaitingConnections);

                    row.setConNode(hostName);
                }
            }
        }
    }  // end method loadStatistics

    @NotNull private String getMbeanName(@NotNull String name) {
        return HIKARI_MBEAN + (name.equals(DEFAULT) ? "" : name);
    }

    private void setTotalFor(ImmutableList<Attribute> list, String name, Consumer<Integer> setTotal) {  //
        list.filter(p -> name.equals(p.getName())).map(attribute -> (Integer) attribute.getValue()).getFirst().ifPresent(setTotal);
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String[] ATTRS = { "ActiveConnections", "IdleConnections", "TotalConnections", "ThreadAwaitingConnection" };

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String DEFAULT = "Default";

    //~ Inner Classes ................................................................................................................................

    public class ConnectionsTabRow extends ConnectionsTabRowBase {}
}  // end class DatabaseForm
