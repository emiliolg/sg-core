
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm.execution;

import java.util.Map;

/**
 * Define an Scope with Variables.
 */
// Todo try to remove bulk methods also favor typed methods
public interface VariableScope {

    //~ Methods ......................................................................................................................................

    /** Returns true if the specified variable has a value. */
    boolean hasVariable(String variableName);

    /** Get the value of a variable as an Object. */
    Object getVariable(String variableName);

    /** Get the value of a variable of the specified type. */
    <T> T getVariable(String variableName, Class<T> clazz);

    /** Sets the value of a variable. */
    void setVariable(String variableName, Object value);

    /** Get a local variable. */
    Object getVariableLocal(String variableName);

    /** Sets a local variable value. */
    Object setVariableLocal(String variableName, Object value);

    /** Get the local variables in the scope. */
    Map<String, Object> getVariablesLocal();

    /** Initialize the local variables. */
    @SuppressWarnings("EmptyMethod")
    void setVariablesLocal(Map<String, ?> variables);
}
