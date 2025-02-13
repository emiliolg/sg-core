
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
import static tekgenesis.common.Predefined.*;

/**
 * An operation representing the access to a value in Forbidden expression.
 */
public class ForbiddenAccess implements Code, Comparable<ForbiddenAccess> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String name;

    @Nullable private transient BoundRef<Boolean> permissionRef;

    //~ Constructors .................................................................................................................................

    /** Creates the code instruction to invoke a custom functions. */
    public ForbiddenAccess(@NotNull String name) {
        this.name     = name;
        permissionRef = null;
    }

    //~ Methods ......................................................................................................................................

    @Override public void bind(Binder binder) {
        permissionRef = binder.bindPermissionRef(name);
    }

    @Override
    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    public int compareTo(@NotNull ForbiddenAccess refAccess) {
        return getName().compareTo(refAccess.getName());
    }

    @Override public boolean equals(Object o) {
        return o instanceof ForbiddenAccess && equal(((ForbiddenAccess) o).name, name);
    }

    @Override public int hashCode() {
        return hashCodeAll(name);
    }

    @Override public String toString() {
        return "forbidden(" + name + ")";
    }

    @Override public Instruction getInstruction() {
        return Instruction.FORBIDDEN;
    }

    /** Get permission Ref name. */
    @NotNull public String getName() {
        return name;
    }

    @NotNull BoundRef<Boolean> getRef() {
        return ensureBind(permissionRef);
    }
}  // end class ForbiddenAccess
