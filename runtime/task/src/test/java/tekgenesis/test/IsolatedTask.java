
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.task.ScheduledTask;
import tekgenesis.task.Status;
import tekgenesis.transaction.Transaction;

/**
 * User class for Task: IsolatedTask
 */
public class IsolatedTask extends IsolatedTaskBase<IsolatedTask.IsolatedStatus> {

    //~ Instance Fields ..............................................................................................................................

    public int                            processed = 0;
    private int                           id;
    private ImmutableList<IsolatedStatus> statuses  = null;

    //~ Constructors .................................................................................................................................

    protected IsolatedTask(@NotNull final ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @Nullable @Override public Seq<IsolatedStatus> enumerate() {
        return Colls.immutable(statuses);
    }

    public void init(ImmutableList<IsolatedStatus> list, int i) {
        statuses = list;
        id       = i;
    }

    @Nullable @Override public Status process(@NotNull IsolatedStatus o) {
        final Isolated isolated = Isolated.find(id);
        assert isolated != null;
        isolated.setCount(isolated.getCount() + 1);
        isolated.persist();

        switch (o) {
        case EXCEPTION:
            throw new IllegalStateException("End With Exception");
        case ABORT:
            return Status.abort("Abort");
        case OK:
            processed++;
            return Status.ok();
        case DONE:
            processed++;
            return Status.done();
        case FAIL:
            return Status.error("Error");
        case TX_FORCE_COMMIT:
            processed++;
            Transaction.getCurrent().get().commit();
            return Status.done();
        case TX_FORCE_ROLLBACK:
            processed++;
            Transaction.getCurrent().get().rollback();
            return Status.done();
        }
        return Status.ok();
    }

    //~ Enums ........................................................................................................................................

    public enum IsolatedStatus { OK, FAIL, EXCEPTION, ABORT, DONE, TX_FORCE_COMMIT, TX_FORCE_ROLLBACK }
}  // end class IsolatedTask
