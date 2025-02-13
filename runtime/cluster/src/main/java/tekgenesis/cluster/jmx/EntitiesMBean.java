
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

/**
 * EntitiesMBean.
 */
public interface EntitiesMBean extends Serializable {

    //~ Methods ......................................................................................................................................

    /** Clear entity's cache. */
    void clearCache(String entityFqn);

    /** Rebuild entity' index. */
    void rebuildIndex(String entityFqn);

    /**  */
    List<String> getEntities();
}
