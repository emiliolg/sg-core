
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.surround;

import com.intellij.codeInsight.CodeInsightUtilCore;
import com.intellij.lang.surroundWith.Surrounder;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.surround.LoggerSurroundDescriptor.LoggerMode;

import static com.intellij.psi.CommonClassNames.JAVA_LANG_STRING;
import static com.intellij.psi.CommonClassNames.JAVA_LANG_THROWABLE;
import static com.intellij.psi.util.InheritanceUtil.isInheritor;

import static tekgenesis.common.core.Constants.JAVA_CLASS_EXT;

/**
 * Surrounder to log in java with tek Logger class.
 */
public class LoggerSurrounder implements Surrounder {

    //~ Instance Fields ..............................................................................................................................

    private final LoggerMode mode;

    //~ Constructors .................................................................................................................................

    /** Constructor for surrounder. */
    public LoggerSurrounder(LoggerMode mode) {
        this.mode = mode;
    }

    //~ Methods ......................................................................................................................................

    @Nullable @Override public TextRange surroundElements(@NotNull final Project project, @NotNull Editor editor, @NotNull PsiElement[] elements)
        throws IncorrectOperationException
    {
        final PsiElement element = CodeInsightUtilCore.forcePsiPostprocessAndRestoreElement(elements[0]);

        final Document document   = editor.getDocument();
        final int      textOffset = element.getTextOffset();
        final int      textLength = element.getTextLength();

        String oldText = document.getText(new TextRange(textOffset, textOffset + textLength));
        if (oldText.charAt(oldText.length() - 1) == ';') oldText = oldText.substring(0, oldText.length() - 1);
        document.replaceString(textOffset, textOffset + textLength, "logger." + mode.getMethod() + "(" + oldText + ");");

        importLogger(element, project, document);

        return element.getTextRange();
    }

    @Override public boolean isApplicable(@NotNull PsiElement[] elements) {
        final PsiElement element = elements[0];
        return IS_THROWABLE_OR_STRING.value(element) || element instanceof PsiExpressionStatement || element instanceof PsiLiteralExpression;
    }

    @Override public String getTemplateDescription() {
        return "log" + mode.getLog();
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private void importLogger(PsiElement element, final Project project, final Document document) {
        final PsiJavaFileImpl file     = (PsiJavaFileImpl) element.getContainingFile();
        final PsiClass        psiClass = file.getClasses()[0];
        if (psiClass.findFieldByName("logger", true) == null) {
            final String fieldSource = String.format("public static final Logger logger = Logger.getLogger(%s%s);",
                    psiClass.getName(),
                    JAVA_CLASS_EXT);

            final PsiElementFactory elementFactory = PsiElementFactory.SERVICE.getInstance(project);
            final PsiField          newField       = elementFactory.createFieldFromText(fieldSource, null);
            final PsiClass          importClass    = JavaPsiFacade.getInstance(project)
                                                     .findClass("tekgenesis.common.logging.Logger", GlobalSearchScope.allScope(project));

            PsiDocumentManager.getInstance(project).commitDocument(document);
            psiClass.add(newField);
            file.importClass(importClass);
            PsiDocumentManager.getInstance(project).commitDocument(document);
        }
    }

    //~ Static Fields ................................................................................................................................

    public static final Condition<PsiElement> IS_THROWABLE_OR_STRING = element -> {
                                                                           if (!(element instanceof PsiExpression)) return false;
                                                                           final PsiType type = ((PsiExpression) element).getType();
                                                                           return type != null && isInheritor(type, JAVA_LANG_THROWABLE) ||
                                                                                  isInheritor(type, JAVA_LANG_STRING);
                                                                       };
}  // end class LoggerSurrounder
