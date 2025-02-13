
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.g.InvalidatedSessionTable;
import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.env.context.Context;
import tekgenesis.persistence.Sql;
import tekgenesis.task.ScheduledTask;
import tekgenesis.task.Status;

/**
 * User class for Task: CleanSessions
 */
public class CleanSessions extends CleanSessionsBase {

    //~ Constructors .................................................................................................................................

    private CleanSessions(@NotNull ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Status run() {
        final int maxSessionTimeout = Context.getProperties(ShiroProps.class).maxSessionTimeout;
        Sql.deleteFrom(InvalidatedSessionTable.INVALIDATED_SESSION)
            .where(InvalidatedSessionTable.INVALIDATED_SESSION.EXPIRED.lt(DateTime.current().addSeconds(-maxSessionTimeout)))
            .execute();
        return Status.ok();
    }
}
