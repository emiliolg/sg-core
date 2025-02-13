
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.psi.ContributedReferenceHost;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiLiteral;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMElementType;

/**
 * Psi element for interpolation strings.
 */
public class PsiInterpolation extends MMCommonComposite implements PsiLiteral, PsiLanguageInjectionHost, ContributedReferenceHost {

    //~ Constructors .................................................................................................................................

    PsiInterpolation(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
        return LiteralTextEscaper.createSimple(this);
    }

    @Override public PsiLanguageInjectionHost updateText(@NotNull String s) {
        return this;
    }

    @Override public boolean isValidHost() {
        return true;
    }

    @Nullable @Override public Object getValue() {
        return getFirstChild().getText();
    }
}
