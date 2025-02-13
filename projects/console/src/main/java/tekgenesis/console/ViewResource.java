
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.common.env.context.Context;
import tekgenesis.persistence.ResourceHandler;

/**
 * User class for Form: ViewResource
 */
public class ViewResource extends ViewResourceBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Object populate() {
        final ResourceHandler resourceHandler = Context.getSingleton(ResourceHandler.class);

        final Option<Resource> resource = resourceHandler.findResource(getResource());
        if (resource.isPresent()) setResources(resource.toList());
        return "";
    }
}
