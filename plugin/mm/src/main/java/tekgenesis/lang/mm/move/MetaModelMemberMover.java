
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.move;

import com.intellij.codeInsight.editorActions.moveUpDown.LineMover;
import com.intellij.codeInsight.editorActions.moveUpDown.LineRange;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.lang.mm.psi.PsiMetaModelMember;
import tekgenesis.mmcompiler.ast.MMToken;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;

/**
 * MetaModel member mover. To move fields, and metamodels as blocks.
 */
@SuppressWarnings("OverlyLongMethod")
public class MetaModelMemberMover extends LineMover {

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("OverlyComplexMethod")
    public boolean checkAvailable(@NotNull final Editor editor, @NotNull final PsiFile file, @NotNull final MoveInfo info, final boolean down) {
        if (!(file instanceof MMFile)) return false;

        if (!super.checkAvailable(editor, file, info, down)) return false;

        final LineRange                    oldRange = info.toMove;
        final Pair<PsiElement, PsiElement> psiRange = getElementRange(editor, file, oldRange);
        if (psiRange == null) return false;

        final PsiMetaModelMember firstMember = getParentOfType(psiRange.getFirst(), PsiMetaModelMember.class, false);
        final PsiMetaModelMember lastMember  = getParentOfType(psiRange.getSecond(), PsiMetaModelMember.class, false);
        if (firstMember == null || lastMember == null) return false;

        final LineRange range;

        if (firstMember == lastMember) {
            range = memberRange(firstMember, editor, oldRange);
            if (range == null) return false;
            range.firstElement = range.lastElement = firstMember;
        }
        else {
            final PsiElement parent = PsiTreeUtil.findCommonParent(firstMember, lastMember);
            if (parent == null) return false;

            final Pair<PsiElement, PsiElement> combinedRange = getElementRange(parent, firstMember, lastMember);
            if (combinedRange == null) return false;
            final LineRange lineRange1 = memberRange(combinedRange.getFirst(), editor, oldRange);
            if (lineRange1 == null) return false;
            final LineRange lineRange2 = memberRange(combinedRange.getSecond(), editor, oldRange);
            if (lineRange2 == null) return false;
            range              = new LineRange(lineRange1.startLine, lineRange2.endLine);
            range.firstElement = combinedRange.getFirst();
            range.lastElement  = combinedRange.getSecond();
        }

        final Document document = editor.getDocument();

        PsiElement sibling = down ? range.lastElement.getNextSibling() : range.firstElement.getPrevSibling();
        if (sibling == null) return false;
        sibling = firstNonWhiteOrCommentElement(sibling, down);

        info.toMove = range;

        final boolean areWeMovingMetaModel = range.firstElement instanceof PsiMetaModel;

        if (areWeMovingMetaModel && !(sibling instanceof PsiMetaModel)) info.toMove2 = null;

        if (sibling != null && info.toMove2 != null) {
            if ((down && MMToken.RIGHT_BRACE.getText().equals(sibling.getText())) ||
                (!down && MMToken.LEFT_BRACE.getText().equals(sibling.getText()))) info.toMove2 = null;
            else info.toMove2 = new LineRange(sibling, sibling, document);
        }

        return true;
    }  // end method checkAvailable

    //~ Methods ......................................................................................................................................

    @Nullable private static PsiElement firstNonWhiteOrCommentElement(PsiElement element, final boolean lookRight)
    {
        PsiElement result = element;
        while (result instanceof PsiWhiteSpace || result instanceof PsiComment)
            result = lookRight ? result.getNextSibling() : result.getPrevSibling();
        return result;
    }

    private static LineRange memberRange(@NotNull PsiElement member, Editor editor, LineRange lineRange) {
        final TextRange textRange = member.getTextRange();
        if (editor.getDocument().getTextLength() < textRange.getEndOffset()) return null;
        final int startLine = editor.offsetToLogicalPosition(textRange.getStartOffset()).line;
        final int endLine   = editor.offsetToLogicalPosition(textRange.getEndOffset()).line + 1;
        return new LineRange(startLine, endLine);
    }
}  // end class MetaModelMemberMover
