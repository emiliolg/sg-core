
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import javax.swing.*;

import com.intellij.navigation.ItemPresentation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMFileType;

/**
 * Presentation for Psi.
 */
class MMCommonCompositePresentation implements ItemPresentation {

    //~ Instance Fields ..............................................................................................................................

    final MMCommonComposite commonComposite;

    //~ Constructors .................................................................................................................................

    MMCommonCompositePresentation(MMCommonComposite commonComposite) {
        this.commonComposite = commonComposite;
    }

    //~ Methods ......................................................................................................................................

    @Nullable @Override public Icon getIcon(boolean b) {
        return MMFileType.getIconFor(commonComposite);
    }

    @NotNull @Override public String getLocationString() {
        return "(" + commonComposite.getDomain() + ")";
    }

    @Nullable @Override public String getPresentableText() {
        return commonComposite.getName();
    }
}
