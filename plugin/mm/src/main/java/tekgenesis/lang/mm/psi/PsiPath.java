
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.psi.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMElementType;

import static com.intellij.psi.LiteralTextEscaper.createSimple;

/**
 * Represents a routing path. May contain dynamic parts.
 */
public class PsiPath extends MMCommonComposite implements PsiLiteral, PsiLanguageInjectionHost, ContributedReferenceHost {

    //~ Constructors .................................................................................................................................

    PsiPath(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public LiteralTextEscaper<PsiPath> createLiteralTextEscaper() {
        return createSimple(this);
    }

    @Override public PsiLanguageInjectionHost updateText(@NotNull String text) {
        return this;
    }

    @Override public boolean isValidHost() {
        return true;
    }

    @Nullable @Override public Object getValue() {
        return getFirstChild().getText();
    }
}
