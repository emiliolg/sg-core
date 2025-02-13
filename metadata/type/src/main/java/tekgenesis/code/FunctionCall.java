
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.code;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static tekgenesis.code.RefAccess.ensureBind;

/**
 * An operation representing a function Call.
 */
public class FunctionCall implements Code {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private transient Fun<?> function;

    @NotNull private final String name;

    //~ Constructors .................................................................................................................................

    /** Creates the code instruction to invoke a custom functions. */
    public FunctionCall(@NotNull String name) {
        this.name = name;
        function  = null;
    }

    //~ Methods ......................................................................................................................................

    @Override public void bind(Binder binder) {
        function = binder.bindFunction(name);
    }

    @Override public String toString() {
        return "fun " + name + "()";
    }

    @Override public Instruction getInstruction() {
        return Instruction.FUN;
    }

    /** Returns the function name. */
    @NotNull public String getName() {
        return name;
    }

    @NotNull Fun<?> getFunction() {
        return ensureBind(function);
    }
}  // end class FunctionCall
