
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
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.jetbrains.annotations.NotNull;
import org.jgroups.Address;

import tekgenesis.cluster.jmx.service.RemoteService;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.Version;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.common.util.Reflection;
import tekgenesis.service.ServiceStatus;
import tekgenesis.task.jmx.JmxConstants;

import static java.lang.String.format;

import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.task.jmx.JmxConstants.*;

/**
 * Member Instance Wrapper class over the MBean calls.
 */
public class RemoteMember implements MemberMBean {

    //~ Instance Fields ..............................................................................................................................

    @JsonIgnore private final Address     address;
    @JsonIgnore private final JmxEndpoint conn;
    private final String                  name;

    //~ Constructors .................................................................................................................................

    /**  */
    public RemoteMember(String name) {
        this.name = name;
        conn      = null;
        address   = null;
    }

    /**  */
    public RemoteMember(Address a, JmxEndpoint c) {
        address = a;
        conn    = c;
        name    = a.toString();
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean areTaskRunning() {
        // noinspection DuplicateStringLiteralInspection
        return JmxInvokerImpl.invoker(conn).mbean(NODE).invoke("areTaskRunning", EMPTY_SIGNATURE, EMPTY_ARGS);
    }

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public List<Tuple<String, String>> dumpCacheContent(@NotNull String cacheName) {
        return JmxInvokerImpl.invoker(conn)
               .mbean(NODE)
               .invoke("dumpCacheContent", new String[] { String.class.getName() }, new Object[] { cacheName });
    }

    @Override public void restart() {
        JmxInvokerImpl.invoker(conn).mbean(NODE).invoke(RESTART, EMPTY_SIGNATURE, EMPTY_ARGS);
    }

    @Override public void safeMode(boolean enabled) {
        final String[] signature = { boolean.class.getName() };
        final Object[] params    = { enabled };
        JmxInvokerImpl.invoker(conn).mbean(NODE).invoke(SAFE_MODE, signature, params);
    }

    @Override public void update(int buildNumber, String branchName, boolean healthCheck) {
        final String[] signature = { int.class.getName(), String.class.getName(), boolean.class.getName() };
        final Object[] params    = { buildNumber, branchName, healthCheck };
        JmxInvokerImpl.invoker(conn).mbean(NODE).invoke(UPGRADE, signature, params);
    }

    @Override public <T> void updateProperty(String scope, Class<T> clazz, String field, String value) {
        JmxInvokerImpl.invoker(conn)
            .mbean(NODE)
            .invoke(JmxConstants.UPDATE_PROPERTY,
                new String[] { String.class.getName(), Class.class.getName(), String.class.getName(), String.class.getName() },
                new Object[] { scope, clazz, field, value });
    }

    public Address getAddress() {
        return address;
    }

    /** @return  the list of service that are enabled */
    public List<String> getAvailableServices() {
        final Map<String, ServiceStatus> servicesStatus = getServicesStatus();
        return Colls.filter(servicesStatus.keySet(), s -> servicesStatus.get(s) != ServiceStatus.DISABLED).toList();
    }

    @Override public List<Version.ComponentInfo> getComponents() {
        return JmxInvokerImpl.invoker(conn).mbean(NODE).getAttribute("Components");
    }

    /** @return  the list of components */
    public Seq<String> getComponentVersionList() {
        return map(getComponents(), v -> format("%s %s-#%s", v.getComponent(), v.getBranch(), v.getBuild()));
    }

    /** Jmx Connection. */
    public JmxEndpoint getJmxEndpoint() {
        return conn;
    }

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Metric getMetrics() {
        return JmxInvokerImpl.invoker(conn).mbean(NODE).getAttribute("Metrics");
    }

    @Override public String getName() {
        return name;
    }

    @Override public Map<String, String> getProperties() {
        // noinspection DuplicateStringLiteralInspection
        return JmxInvokerImpl.invoker(conn).mbean(NODE).getAttribute("Properties");
    }

    /** Service instance over the member. */
    public RemoteService getService(String service) {
        return new RemoteService(service, getJmxEndpoint());
    }

    /** Service instance over the member. */
    public <T extends RemoteService> T getService(Class<T> serviceType, String service) {
        return Reflection.construct(serviceType, service, getJmxEndpoint());
    }

    @Override public Map<String, ServiceStatus> getServicesStatus() {
        return JmxInvokerImpl.invoker(conn).mbean(NODE).getAttribute("ServicesStatus");
    }

    @Override public NodeStatus getStatus() {
        return JmxInvokerImpl.invoker(conn).mbean(NODE).getAttribute("Status");
    }

    @Override public long getUptime() {
        return JmxInvokerImpl.invoker(conn).mbean(NODE).getAttribute("Uptime");
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -1456497315341080762L;
}  // end class RemoteMember
