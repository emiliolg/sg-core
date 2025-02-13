
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.formatter.MMBlock;

import static com.intellij.formatting.FormattingModelProvider.createFormattingModelForPsiFile;

/**
 * Created by IntelliJ IDEA. User: diego Date: 2/9/12 Time: 10:52 AM To change this template use
 * File | Settings | File Templates.
 */
@SuppressWarnings("WeakerAccess")
public class EntityFormattingBuilder implements FormattingModelBuilder {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
        return createFormattingModelForPsiFile(element.getContainingFile(), new MMBlock(element.getNode(), settings), settings);
    }

    @Override public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        return null;
    }
}
