
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

import tekgenesis.common.core.Tuple;

/**
 * CacheMBean.
 */
public interface CachesMBean extends Serializable {

    //~ Methods ......................................................................................................................................

    /** Clear cache. */
    void clearCache(String cacheName);

    /** Get List of defined cache. */
    List<Tuple<String, String>> getEntityCaches();

    /** Get List of defined cache. */
    List<Tuple<String, String>> getUserCaches();

    /** Returns a values from an specific cache. */
    <K, V> V getValue(String cacheName, K key);

    /**
     * Returns all values from an specific cache in this node. There is not warranty that getValues
     * returns all values in cluster.
     */
    <V> List<V> getValues(String cacheName);
}
