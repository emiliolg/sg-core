
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.MMElementType;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;

/**
 * A psi role permission node.
 */
public class PsiRolePermission extends ElementWithReferences {

    //~ Constructors .................................................................................................................................

    /** Creates a psi role permission node. */
    PsiRolePermission(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    /** Resolve role permission associated form. */
    public Option<PsiForm> resolveForm() {
        final PsiElement   fqn       = getParent().getParent().getFirstChild();
        final PsiReference reference = fqn.getReference();
        if (reference != null) {
            final PsiElement resolved = reference.resolve();
            if (resolved instanceof PsiForm) {
                final PsiForm form = (PsiForm) resolved;
                return some(form);
            }
        }
        return empty();
    }
}
