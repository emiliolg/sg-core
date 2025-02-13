
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;

import tekgenesis.mmcompiler.ast.MMToken;

import static tekgenesis.lang.mm.MMElementType.forToken;
import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * BraceMatcher for MM.
 */
public class MMBraceMatcher implements PairedBraceMatcher {

    //~ Methods ......................................................................................................................................

    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return openingBraceOffset;
    }

    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lBraceType, IElementType contextType) {
        return true;
    }
    public BracePair[] getPairs() {
        return bracePairs;
    }

    //~ Methods ......................................................................................................................................

    private static BracePair pair(MMToken a, MMToken b, boolean structural) {
        return new BracePair(forToken(a), forToken(b), structural);
    }

    //~ Static Fields ................................................................................................................................

    private static final BracePair[] bracePairs = {
        pair(LEFT_BRACE, RIGHT_BRACE, true),
        pair(LEFT_BRACKET, RIGHT_BRACKET, false),
        pair(LEFT_PAREN, RIGHT_PAREN, false)
    };
}
