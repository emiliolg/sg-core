
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.intention;

import com.intellij.codeInsight.FileModificationService;
import com.intellij.codeInsight.intention.BaseElementAtCaretIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.siyeh.ipp.base.PsiElementPredicate;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMLanguage;

/**
 * Base abstract class for intentions.
 */
abstract class Intention extends BaseElementAtCaretIntentionAction {

    //~ Instance Fields ..............................................................................................................................

    private final PsiElementPredicate predicate;

    //~ Constructors .................................................................................................................................

    Intention() {
        // noinspection AbstractMethodCallInConstructor
        predicate = getElementPredicate();
    }

    //~ Methods ......................................................................................................................................

    @Override public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        if (prepareForWriting() && !FileModificationService.getInstance().preparePsiElementsForWrite(element)) return;
        final PsiElement matchingElement = findMatchingElement(element, editor);

        if (matchingElement == null) return;
        processIntention(editor, matchingElement);
    }

    @Override public boolean startInWriteAction() {
        return false;
    }

    @Override public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        return findMatchingElement(element, editor) != null;
    }

    @Nls @NotNull @Override public String getFamilyName() {
        return getText();
    }

    protected boolean prepareForWriting() {
        return true;
    }

    protected abstract void processIntention(@NotNull PsiElement element);

    protected void processIntention(Editor editor, @NotNull PsiElement element) {
        processIntention(element);
    }

    @Nullable PsiElement findMatchingElement(@Nullable PsiElement element, Editor editor) {
        PsiElement current = element;
        while (current != null) {
            if (!MMLanguage.INSTANCE.equals(current.getLanguage())) break;
            if (predicate.satisfiedBy(current)) return current;
            current = current.getParent();
            if (current instanceof PsiFile) break;
        }
        return null;
    }

    @NotNull abstract PsiElementPredicate getElementPredicate();
}  // end class Intention
