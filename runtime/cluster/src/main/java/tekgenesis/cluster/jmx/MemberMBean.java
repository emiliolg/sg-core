
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
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jgroups.Address;

import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.Version;
import tekgenesis.service.ServiceStatus;

/**
 * NodeMBean.
 */
public interface MemberMBean extends Serializable {

    //~ Methods ......................................................................................................................................

    /** return true if there are task running on the node. */
    boolean areTaskRunning();

    /** @return  dump cache content */
    List<Tuple<String, String>> dumpCacheContent(@NotNull String cacheName);

    /** restart node. */
    void restart();

    /** Enabled/Disable safeMode. */
    void safeMode(boolean enabled);

    /** Update version. */
    void update(int buildNumber, String branchName, boolean healthCheck);

    /** Update property. */
    <T> void updateProperty(String scope, Class<T> clazz, String field, String value);

    /** Node's address. */
    Address getAddress();

    /** Component's list version. */
    List<Version.ComponentInfo> getComponents();

    /** Node Metrics. */
    Metric getMetrics();
    /** Node's name. */
    String getName();

    /** Get Properties. */
    Map<String, String> getProperties();

    /** Services status. */
    Map<String, ServiceStatus> getServicesStatus();

    /** @return  Upgrade status */
    NodeStatus getStatus();

    /** Node uptime. */
    long getUptime();
}  // end interface MemberMBean
