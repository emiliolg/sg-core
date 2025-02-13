
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.psi.PsiElementVisitor;

/**
 * MetaModel language psi element visitor. To be extended to all elements!
 */
public class MetaModelElementVisitor extends PsiElementVisitor {

    //~ Methods ......................................................................................................................................

    /** Visit MetaModel element. */
    public void visitMetaModel(PsiMetaModel<?> model) {
        visitElement(model);
    }

    /** Visit reference element. */
    public void visitReferenceElement(PsiMetaModelCodeReferenceElement reference) {
        visitElement(reference);
    }
}
