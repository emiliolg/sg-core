
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx.util;

import java.net.InetAddress;
import java.util.List;

import org.infinispan.Cache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jgroups.Address;

import tekgenesis.cache.CacheType;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.jmx.metric.HttpMetric;
import tekgenesis.cluster.jmx.notification.Operation;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.env.Version;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.logging.Logger;

import static java.lang.String.format;

import static tekgenesis.cluster.jmx.notification.Notifier.call;
import static tekgenesis.common.core.Constants.JMX_INSTANCE_MAP;

/**
 * Member summary.
 */
public final class MemberInfo {

    //~ Instance Fields ..............................................................................................................................

    private final Address                address;
    private final InfinispanCacheManager cacheManager;

    private final ClusterManager<Address> clusterManager;

    private final Logger logger = Logger.getLogger(MemberInfo.class);
    private final String name;

    //~ Constructors .................................................................................................................................

    /** default constructor. */
    public MemberInfo(InfinispanCacheManager cacheManager, ClusterManager<Address> clusterManager, @NotNull Address address) {
        this.address        = address;
        name                = clusterManager.getMemberName(address);
        this.clusterManager = clusterManager;
        this.cacheManager   = cacheManager;
    }

    //~ Methods ......................................................................................................................................

    /** .* */
    public Address getAddress() {
        return address;
    }

    /** .* */
    public List<Version.ComponentInfo> getComponentsVersion() {
        final ImmutableList<Version.ComponentInfo> defaultValue = Colls.emptyList();
        final List<Version.ComponentInfo>          call         = call(getAddress(), Operation.COMPONENT_INFO, defaultValue);
        return call == null ? defaultValue : call;
    }

    /** return memory usage.* */
    public double getCpuUsage() {
        return call(getAddress(), Operation.CPU_USAGE, 0d);
    }

    /** @return  if jmx is enabled in the member */
    public boolean isJmxEnabled() {
        final Cache<Object, Integer> jmxConfigMap = cacheManager.getCache(JMX_INSTANCE_MAP, CacheType.DEFAULT.withSyncReplication());
        logger.info(format("Is Jmx enable in %s, %b", address.toString(), jmxConfigMap.get(address) != null));
        return jmxConfigMap.get(address) != null;
    }

    /** @return  InetSocketAddress */
    @Nullable public InetAddress getInetAddress() {
        return clusterManager.getPhysicalAddress(address);
    }

    /** @return  JmxConnection */
    public JmxEndpoint getJmxEndpoint() {
        return ClusterInfo.getInstance().getJmxConfiguration(address);
    }

    /** return memory usage.* */
    public MemoryUsageInfo getMemoryUsage() {
        return call(getAddress(), Operation.MEMORY_USAGE, new MemoryUsageInfo());
    }

    /** .* */
    public HttpMetric getMetrics() {
        return new HttpMetric(this);
    }

    /** Symbolic name.* */
    public String getName() {
        return name;
    }

    /** .* */
    public boolean isMaster() {
        return address.equals(clusterManager.getMaster());
    }

    /** .* */
    public Long getUptime() {
        return call(getAddress(), Operation.UPTIME, 0L);
    }
}  // end class MemberInfo
