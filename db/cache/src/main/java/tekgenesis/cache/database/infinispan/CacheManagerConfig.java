
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache.database.infinispan;

import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;

/**
 * CacheManagerConfig.
 */
public abstract class CacheManagerConfig {

    //~ Methods ......................................................................................................................................

    /** Get Cache Configuration. */
    public GlobalConfiguration getConfiguration() {
        final GlobalConfigurationBuilder builder = new GlobalConfigurationBuilder();
        return build(builder).globalJmxStatistics().enable().allowDuplicateDomains(true).build();
    }

    /** Returns true if the CacheManager is local. */

    public boolean isLocal() {
        return true;
    }
    abstract GlobalConfigurationBuilder build(GlobalConfigurationBuilder builder);

    //~ Inner Classes ................................................................................................................................

    public static class NoCluster extends CacheManagerConfig {
        @Override GlobalConfigurationBuilder build(GlobalConfigurationBuilder builder) {
            return builder.nonClusteredDefault();
        }
    }
}
