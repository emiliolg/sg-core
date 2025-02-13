
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.mmcompiler.ast.MetaModelAST;

import static com.intellij.psi.tree.TokenSet.create;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.lang.mm.MMElementType.IDENTIFIER;
import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;
import static tekgenesis.lang.mm.psi.PsiUtils.getPathToSourceRoot;

/**
 * The package identifier.
 */
public class PsiDomain extends MMCommonComposite {

    //~ Constructors .................................................................................................................................

    /** Creates an Identifier. */
    PsiDomain(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Override public void annotate(AnnotationHolder holder) {
        super.annotate(holder);
        if (isNotInjected()) {
            final PsiMetaModelCodeReferenceElement reference = getReferenceChild();
            if (reference == null) {
                holder.createErrorAnnotation(getNode(), MSGS.missingPackage());
                return;
            }

            final String path   = getPathToSourceRoot(getContainingFile());
            final String domain = getDomain();

            if (!Predefined.isEmpty(path) && !equal(path, domain)) {
                final TextRange range = reference.getTextRange();
                holder.createErrorAnnotation(range, MSGS.packageNameNotCorrespondToPath(domain, path))
                    .registerFix(new SetPackageNameFix(range, path));
            }
        }
    }

    @NotNull @Override public String getDomain() {
        return getDomainName();
    }

    /** Get the package name. */
    public String getDomainName() {
        final PsiMetaModelCodeReferenceElement r = getReferenceChild();
        return r != null ? MetaModelReferences.getReferenceText(r) : "";
    }

    @Nullable private PsiElement getFirstPackagePart() {
        return (PsiElement) getChildren(create(IDENTIFIER))[0];
    }

    @Nullable private PsiElement getLastPackagePart() {
        return getLastChild();
    }

    private PsiMetaModelCodeReferenceElement getReferenceChild() {
        final MetaModelAST child = getChild(0);
        return child instanceof PsiMetaModelCodeReferenceElement ? (PsiMetaModelCodeReferenceElement) child : null;
    }

    //~ Inner Classes ................................................................................................................................

    private static class SetPackageNameFix implements IntentionAction {
        @NotNull private final String    path;
        @NotNull private final TextRange range;

        public SetPackageNameFix(@NotNull final TextRange range, @NotNull final String path) {
            this.range = range;
            this.path  = path;
        }

        @Override public void invoke(@NotNull Project project, final Editor editor, PsiFile file)
            throws IncorrectOperationException
        {
            new WriteCommandAction.Simple<Object>(project, file) {
                    @Override protected void run()
                        throws Throwable
                    {
                        editor.getDocument().replaceString(range.getStartOffset(), range.getEndOffset(), path);
                    }
                }.execute();
        }

        @Override public boolean startInWriteAction() {
            return true;
        }

        @Override public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
            return true;
        }

        @NotNull @Override public String getFamilyName() {
            return getText();
        }

        @NotNull @Override public String getText() {
            return MSGS.setPackageName(path);
        }
    }
}  // end class PsiDomain
