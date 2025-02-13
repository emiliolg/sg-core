
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import org.jetbrains.annotations.NotNull;

/**
 * Presentation for Psi.
 */
class PsiModelFieldPresentation extends MMCommonCompositePresentation {

    //~ Constructors .................................................................................................................................

    PsiModelFieldPresentation(MMCommonComposite commonComposite) {
        super(commonComposite);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public String getLocationString() {
        return "(in " + commonComposite.getDomain() + ")";
    }
}
