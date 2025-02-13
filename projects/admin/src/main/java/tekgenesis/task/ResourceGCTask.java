
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.context.Context;
import tekgenesis.database.Databases;
import tekgenesis.persistence.resource.ResourcesGC;

/**
 * User class for Task: ResourceGCTask
 */
public class ResourceGCTask extends ResourceGCTaskBase {

    //~ Constructors .................................................................................................................................

    protected ResourceGCTask(@NotNull final ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    /** Cleaning resources. */
    @NotNull public Status run() {
        new ResourcesGC(Context.getEnvironment(), Databases.openDefault()).purge();
        return Status.ok();
    }
}
