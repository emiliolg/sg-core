
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache.database.infinispan;

import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.configuration.global.TransportConfigurationBuilder;
import org.infinispan.remoting.transport.jgroups.JGroupsTransport;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.JGroupClusterManager;

/**
 * ClusterCacheManagerConfig.
 */
public class ClusterCacheManagerConfig extends CacheManagerConfig {

    //~ Instance Fields ..............................................................................................................................

    private final JGroupClusterManager cluster;

    private final String clusterName;

    //~ Constructors .................................................................................................................................

    /**  */
    public ClusterCacheManagerConfig(String name, ClusterManager<?> cluster) {
        clusterName  = name;
        this.cluster = (JGroupClusterManager) cluster;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean isLocal() {
        return false;
    }

    @Override GlobalConfigurationBuilder build(GlobalConfigurationBuilder builder) {
        final GlobalConfigurationBuilder    configurationBuilder = builder.clusteredDefault();
        final TransportConfigurationBuilder transport            = configurationBuilder.transport();
        transport.clusterName(clusterName);
        try {
            transport.transport(new JGroupsTransport((cluster).getChannel()));
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return configurationBuilder;
    }
}
