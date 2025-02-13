
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.task;

/**
 * Task Type.
 */
public enum TaskType {

    //~ Enum constants ...............................................................................................................................

    RUNNABLE, IMPORTER, CLUSTER_LIFECYCLE, NODE_LIFE_CYCLE, PROCESSOR, NODE_RUNNABLE;

    //~ Methods ......................................................................................................................................

    /** Returns true if the Task is an Scheduled one. */
    public boolean isScheduled() {
        return this == RUNNABLE || this == PROCESSOR;
    }
}
