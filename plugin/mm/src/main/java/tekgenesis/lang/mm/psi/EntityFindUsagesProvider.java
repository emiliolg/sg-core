
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.find.impl.HelpID;
import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;

import org.jetbrains.annotations.NotNull;

import tekgenesis.intellij.LexerAdapter;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.MMLanguage;
import tekgenesis.mmcompiler.ast.MMToken;

/**
 * @author  cdr
 */
@SuppressWarnings("WeakerAccess")
public class EntityFindUsagesProvider implements FindUsagesProvider {

    //~ Methods ......................................................................................................................................

    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof PsiNamedElement;
    }

    @NotNull public String getDescriptiveName(@NotNull PsiElement element) {
        final String name = ((PsiNamedElement) element).getName();
        return name != null ? name : element.getText();
    }

    public String getHelpId(@NotNull PsiElement psiElement) {
        return HelpID.FIND_IN_PROJECT;
    }

    @NotNull public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        return getDescriptiveName(element);
    }

    @NotNull public String getType(@NotNull PsiElement element) {
        if (element instanceof PsiEntity) return MMLanguage.ID;
        return "";
    }

    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(new LexerAdapter<>(MMToken.lexer(), MMElementType::forToken),
            TokenSet.create(MMElementType.IDENTIFIER),
            MMElementType.COMMENTS,
            MMElementType.LITERALS);
    }
}
