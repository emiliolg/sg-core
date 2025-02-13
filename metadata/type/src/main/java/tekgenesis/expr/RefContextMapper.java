
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import org.jetbrains.annotations.NotNull;

import tekgenesis.type.Type;

/**
 * This Maps a reference to a new context.
 */
public abstract class RefContextMapper implements RefTypeSolver, RefPermissionSolver {

    //~ Instance Fields ..............................................................................................................................

    private final RefTypeSolver refResolver;

    //~ Constructors .................................................................................................................................

    protected RefContextMapper(final RefTypeSolver refResolver) {
        this.refResolver = refResolver;
    }

    //~ Methods ......................................................................................................................................

    /** Maps the reference to the new context. */
    @NotNull public abstract String doMap(@NotNull String referenceName);

    @NotNull @Override public Type doResolve(@NotNull final String referenceName, final boolean isColumn) {
        return refResolver.doResolve(doMap(referenceName), isColumn);
    }

    @Override public boolean hasPermission(@NotNull String permissionName) {
        return refResolver instanceof RefPermissionSolver && ((RefPermissionSolver) refResolver).hasPermission(permissionName);
    }

    //~ Methods ......................................................................................................................................

    /** Do not re-map, just resolve references. */
    public static RefContextMapper none(final RefTypeSolver refResolver) {
        return new None(refResolver);
    }

    //~ Inner Classes ................................................................................................................................

    private static class None extends RefContextMapper {
        public None(final RefTypeSolver refResolver) {
            super(refResolver);
        }

        @NotNull @Override public String doMap(@NotNull final String referenceName) {
            return referenceName;
        }
    }
}  // end class RefContextMapper
