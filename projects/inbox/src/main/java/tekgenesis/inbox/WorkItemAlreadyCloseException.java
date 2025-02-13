
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.inbox;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.exception.SuiGenerisException;
import tekgenesis.workflow.WorkItemInstance;

/**
 * Indicates a navigation was attempted to an already closed work item.
 */
@SuppressWarnings("WeakerAccess")
public class WorkItemAlreadyCloseException extends SuiGenerisException {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final WorkItemInstance<?, ?, ?, ?, ?, ?> instance;

    //~ Constructors .................................................................................................................................

    WorkItemAlreadyCloseException(@NotNull final WorkItemInstance<?, ?, ?, ?, ?, ?> instance) {
        this.instance = instance;
    }

    //~ Methods ......................................................................................................................................

    /** Return work item. */
    @NotNull public WorkItemInstance<?, ?, ?, ?, ?, ?> getInstance() {
        return instance;
    }

    @Override public String getMessage() {
        return Messages.TASK_ALREADY_CLOSE.label();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 5353821804065420850L;
}
