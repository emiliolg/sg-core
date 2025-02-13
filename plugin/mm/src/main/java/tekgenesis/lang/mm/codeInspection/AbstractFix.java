
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.codeInspection;

import com.intellij.codeInsight.FileModificationService;
import com.intellij.codeInspection.LocalQuickFixOnPsiElement;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * Common Class For Inspection Fixes.
 */
abstract class AbstractFix<T extends PsiElement, U extends PsiElement> extends LocalQuickFixOnPsiElement {

    //~ Instance Fields ..............................................................................................................................

    private final String name;

    //~ Constructors .................................................................................................................................

    AbstractFix(String name, T e) {
        super(e);
        this.name = name;
    }
    AbstractFix(String name, T startElement, U endElement) {
        super(startElement, endElement);
        this.name = name;
    }

    //~ Methods ......................................................................................................................................

    /** Applies Inspection Fix. */
    public abstract void doApplyFix(Project project, T startElement, U endElement);

    @Override public void invoke(@NotNull Project project, @NotNull PsiFile psiFile, @NotNull PsiElement startElement,
                                 @NotNull PsiElement endElement) {
        if (startElement.isValid() && FileModificationService.getInstance().prepareFileForWrite(psiFile))
            doApplyFix(project, cast(startElement), cast(endElement));
    }

    @NotNull public String getFamilyName() {
        return MSGS.fixFamily();
    }

    @NotNull public String getText() {
        return name;
    }
}
