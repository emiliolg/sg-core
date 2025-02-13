
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.process.pvm.execution.ExecutionListener;

/**
 * A Process Scope.
 */
public interface Scope extends ProcessElement {

    //~ Methods ......................................................................................................................................

    /** Returns true if the Scope contains the specified activity. */
    boolean contains(Activity activity);

    /** Create an activity with the specified Id. */
    Activity createActivity(@NotNull String id);

    /** Find an activity with the specified id. */
    Activity findActivity(String activityId);

    /** Find the scope of the next activity. */
    Activity findNextScope(Activity destination);

    /** The Activities included in the Scope. */
    List<? extends Activity> getActivities();

    /** Return al execution listeners for the given event. */
    List<ExecutionListener> getExecutionListeners(PvmEvent event);
}
