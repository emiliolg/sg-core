
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm;

import org.jetbrains.annotations.NotNull;

/**
 * A process definition.
 */
public interface ProcessDefinition extends ReadOnlyProcessDefinition {

    //~ Methods ......................................................................................................................................

    /** Create a new {@link ProcessInstance}. */
    ProcessInstance createProcessInstance();

    /** creates a process instance using the provided activity as initial. */
    ProcessInstance createProcessInstanceForInitial(@NotNull Activity first);

    /** Find an activity with the specified id. */
    Activity findActivity(String activityId);

    /** Get the process deployment id. */
    String getDeploymentId();
}
