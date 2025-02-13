
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.codeInsight.intention.AbstractIntentionAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;

import static com.siyeh.ig.fixes.SerialVersionUIDBuilder.computeDefaultSUID;

import static tekgenesis.common.util.JavaReservedWords.FINAL;
import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * Create missing multiple class intention.
 */
class CreateMissingInnerClassIntention extends AbstractIntentionAction {

    //~ Instance Fields ..............................................................................................................................

    private final PsiClass clazz;
    private final String   innerClassName;
    private boolean        isFinal;
    private boolean        isStatic;
    private final String   superClassName;
    private boolean        withSerialUID;

    //~ Constructors .................................................................................................................................

    public CreateMissingInnerClassIntention(@NotNull final PsiClass clazz, @NotNull final String innerClassName, @NotNull String superClassName) {
        this.clazz          = clazz;
        this.innerClassName = innerClassName;
        this.superClassName = superClassName;
    }

    //~ Methods ......................................................................................................................................

    public CreateMissingInnerClassIntention asFinal() {
        isFinal = true;
        return this;
    }

    public CreateMissingInnerClassIntention asStatic() {
        isStatic = true;
        return this;
    }

    @Override public void invoke(@NotNull final Project project, final Editor editor, PsiFile psiFile)
        throws IncorrectOperationException
    {
        //J-
        ApplicationManager.getApplication().runWriteAction(() ->
            {
                final PsiElementFactory elementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
                final String classText = String.format("public %s%sclass %s extends %s {}",
                        isStatic ? "static " : "", isFinal ? FINAL + " " : "", innerClassName, superClassName);
                final PsiClass created = elementFactory.createClassFromText(classText, clazz);
                final PsiClass[] inners = created.getAllInnerClasses();
                assert inners.length == 1;
                if (withSerialUID)
                {
                    final long     serialVersionUID = computeDefaultSUID(clazz);
                    final PsiField field = elementFactory.createFieldFromText("private static final long serialVersionUID = " + serialVersionUID + "L;", created);
                    inners[0].add(field);
                }
                clazz.add(inners[0]);
            });
        //J+
    }

    public CreateMissingInnerClassIntention withSerialUID(boolean b) {
        withSerialUID = b;
        return this;
    }

    @NotNull @Override public String getText() {
        return MSGS.createMissingInnerClass(innerClassName, clazz.getQualifiedName());
    }

    //~ Methods ......................................................................................................................................

    @NotNull public static CreateMissingInnerClassIntention create(@NotNull final PsiClass clazz, @NotNull final String innerClassName,
                                                                   @NotNull String superClassName) {
        return new CreateMissingInnerClassIntention(clazz, innerClassName, superClassName);
    }
}  // end class CreateMissingInnerClassIntention
