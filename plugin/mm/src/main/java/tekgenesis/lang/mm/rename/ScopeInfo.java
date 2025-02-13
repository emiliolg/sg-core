
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.rename;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.QName;

import static tekgenesis.codegen.common.MMCodeGenConstants.BASE;
import static tekgenesis.codegen.common.MMCodeGenConstants.ROW_CLASS_SUFFIX;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.QName.qualify;
import static tekgenesis.common.core.Strings.capitalizeFirst;

/**
 * Scope info for renaming. Basically handles domains and qualifications for user and base classes
 * (root or multiple scopes).
 */
public class ScopeInfo {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final QName fqn;
    private final boolean        root;

    //~ Constructors .................................................................................................................................

    private ScopeInfo(@NotNull final QName fqn, boolean root) {
        this.fqn  = fqn;
        this.root = root;
    }

    //~ Methods ......................................................................................................................................

    /** Get root base class name. */
    public String getRootBaseClassName() {
        return getRootUserClassName() + BASE;
    }

    /** Get root user class name. */
    public String getRootUserClassName() {
        return root ? fqn.getFullName() : fqn.getQualification();
    }

    /** Get scoped base class name. */
    public String getScopedBaseClassName() {
        return root ? getRootBaseClassName() : qualify(fqn.getQualification() + BASE, fqn.getName() + BASE);
    }

    /** Get scoped user class name. */
    public String getScopedUserClassName() {
        return fqn.getFullName();
    }

    //~ Methods ......................................................................................................................................

    /** Create info for multiple inner scope. */
    public static ScopeInfo createScopeInfoForMultiple(@NotNull final QName root, @NotNull final String name) {
        return new ScopeInfo(createQName(root.getFullName(), capitalizeFirst(name + ROW_CLASS_SUFFIX)), false);
    }

    /** Create info for root scope. */
    public static ScopeInfo createScopeInfoForRoot(@NotNull final QName root) {
        return new ScopeInfo(root, true);
    }
}  // end class ScopeInfo
