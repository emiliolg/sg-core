
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.test;

import org.jetbrains.annotations.NotNull;

import tekgenesis.database.Databases;
import tekgenesis.task.ScheduledTask;
import tekgenesis.task.Status;

/**
 * User class for Task: SlowSqlTask
 */
public class SlowSqlTask extends SlowSqlTaskBase {

    //~ Constructors .................................................................................................................................

    private SlowSqlTask(@NotNull ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Status run() {
        System.out.println("Runnin Slow Sql task");
        Databases.openDefault().asSystem().nativeSql("BEGIN\nDBMS_LOCK.sleep(300);\nEND;").execute();
        return Status.ok();
    }
}
