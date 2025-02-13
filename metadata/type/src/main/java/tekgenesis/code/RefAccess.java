
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

import static tekgenesis.common.Predefined.*;

/**
 * An operation representing the access to a value in RefType.
 */
public class RefAccess implements Code, Comparable<RefAccess> {

    //~ Instance Fields ..............................................................................................................................

    private final boolean col;

    @NotNull private final String name;

    @Nullable private transient BoundRef<?> ref;

    //~ Constructors .................................................................................................................................

    /** Creates the code instruction to invoke a custom functions. */
    public RefAccess(@NotNull String name) {
        this(name, false);
    }

    /** Creates the code instruction to invoke a custom functions. */
    public RefAccess(@NotNull String name, boolean isCol) {
        this.name = name;
        col       = isCol;
        ref       = null;
    }

    //~ Methods ......................................................................................................................................

    @Override public void bind(Binder binder) {
        ref = binder.bindRef(name, col);
    }

    @Override
    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    public int compareTo(@NotNull RefAccess refAccess) {
        return getName().compareTo(refAccess.getName());
    }

    @Override public boolean equals(Object o) {
        return o instanceof RefAccess && equal(((RefAccess) o).name, name) && equal(((RefAccess) o).col, col);
    }

    @Override public int hashCode() {
        return hashCodeAll(name, col);
    }

    @Override public String toString() {
        return "ref(" + name + ")";
    }

    @Override public Instruction getInstruction() {
        return Instruction.REF;
    }

    /** True if it references a column. */
    public boolean isCol() {
        return col;
    }

    /** Get Ref name. */
    @NotNull public String getName() {
        return name;
    }

    @NotNull BoundRef<?> getRef() {
        return ensureBind(ref);
    }

    //~ Methods ......................................................................................................................................

    @NotNull static <T> T ensureBind(@Nullable final T function) {
        return ensureNotNull(function, "bind method should be called before executing");
    }
}  // end class RefAccess
