
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.jetbrains.annotations.Nullable;

/**
 * Used to provide feedback on long executions.
 */
public interface ExecutionFeedback {

    //~ Methods ......................................................................................................................................

    /** Provide progress feedback. */
    ExecutionFeedback step(@Nullable String msg);

    /** Provide progress feedback. */
    ExecutionFeedback step(int percentage, @Nullable String msg);

    /** Return true if other part cancelled the execution. */
    boolean isCanceled();
}
