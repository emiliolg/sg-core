
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

import tekgenesis.common.core.DateTime;
import tekgenesis.database.Databases;

import static tekgenesis.database.DbConstants.SCHEMA_SG;

/**
 * User class for Task: CleaningTask
 */
public class CleaningTask extends CleaningTaskBase {

    //~ Constructors .................................................................................................................................

    private CleaningTask(@NotNull ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Status run() {
        Databases.openDefault()
            .sqlStatement("delete from %s.DELETED_INSTANCES where TS < ?", SCHEMA_SG)
            .onArgs(DateTime.current().addDays(-2))
            .execute();

        return Status.ok();
    }
}
