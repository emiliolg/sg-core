
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task.jmx;

import java.net.InetAddress;

import org.infinispan.Cache;
import org.jetbrains.annotations.NotNull;
import org.jgroups.Address;

import tekgenesis.cache.CacheType;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cluster.ClusterManager;
import tekgenesis.common.core.Constants;
import tekgenesis.common.jmx.JmxEndpoint;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Constants.JMX_INSTANCE_MAP;
import static tekgenesis.common.env.context.Context.getSingleton;
import static tekgenesis.common.jmx.JmxEndpoint.NULL;

/**
 * JmxUtil.
 */
public final class EndpointResolver {

    //~ Constructors .................................................................................................................................

    private EndpointResolver() {}

    //~ Methods ......................................................................................................................................

    /** get JmxConfiguration. */
    @SuppressWarnings("MagicNumber")
    public static JmxEndpoint resolve(@NotNull Address address) {
        final ClusterManager<Address> clusterManager = cast(getSingleton(ClusterManager.class));
        final Cache<Object, Integer>  jmxConfigMap   = getSingleton(InfinispanCacheManager.class).getCache(JMX_INSTANCE_MAP,
                CacheType.DEFAULT.withSyncReplication());

        Integer jmxPort = jmxConfigMap.get(address);

        if (jmxPort == null) jmxPort = 9500;  // The member is not register...trying with default port

        final InetAddress physicalAddress = clusterManager.getPhysicalAddress(address);
        return physicalAddress == null ? NULL : JmxEndpoint.create(physicalAddress, jmxPort, Constants.SG_SERVICENAME);
    }
}
