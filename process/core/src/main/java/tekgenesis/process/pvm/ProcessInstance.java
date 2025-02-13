
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

import tekgenesis.process.pvm.execution.DelegateExecution;
import tekgenesis.process.pvm.execution.InterpretableExecution;

/**
 * A Process Instance being executed.
 */
public interface ProcessInstance extends PvmExecution, DelegateExecution {

    //~ Methods ......................................................................................................................................

    /** Deletes this and related process instances. */
    void deleteCascade(String deleteReason);

    /** Returns the list of Active Activities. */
    List<String> findActiveActivityIds();

    /** Searches for an execution positioned in the given activity. */
    PvmExecution findExecution(String activityId);

    /** Start the execution of an instance. */
    void start();

    /** Returns true if the instance is ended. */
    boolean isEnded();

    /** Returns the list of execution for this ProcessInstance. */
    List<? extends InterpretableExecution> getExecutions();
}
