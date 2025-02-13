
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;

/**
 * Handler for Resources.
 */
public interface ResourceHandler {

    //~ Methods ......................................................................................................................................

    /** Create a resource factory. */
    Resource.Factory create();

    /** Create a resource factory with the given uuid. */
    Resource.Factory create(String uuid);

    /** Find the Content with the specified sha. */
    Option<Resource.Content> findContent(String sha);

    /** Find the resource with the specified uuid. */
    Option<Resource> findResource(String uuid);
}
