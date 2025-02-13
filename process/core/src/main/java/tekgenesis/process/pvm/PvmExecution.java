
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An Execution in the process Engine.
 */
public interface PvmExecution {

    //~ Methods ......................................................................................................................................

    /** Send a signal. */
    void signal();

    // boolean hasVariable(String variableName);

    /** Send a signal. */
    void signal(@NotNull String signalName, @Nullable Object signalData);

    // Activity getActivity();
    // Object getVariable(String variableName);

    /** Gets a variable of the specified type by name. */

    <T> T getVariable(String variableName, Class<T> clazz);
    /** Set a variable by name. */

    void setVariable(String variableName, Object value);

    /** The full map of variables. */
    Map<String, Object> getVariables();
}
