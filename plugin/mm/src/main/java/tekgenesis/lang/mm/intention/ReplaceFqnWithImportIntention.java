
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.intention;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.jgoodies.common.base.Strings;
import com.siyeh.IntentionPowerPackBundle;
import com.siyeh.ipp.base.PsiElementPredicate;
import com.siyeh.ipp.psiutils.HighlightUtil;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.logging.Logger;
import tekgenesis.lang.mm.completion.Imports;
import tekgenesis.lang.mm.i18n.PluginMessages;
import tekgenesis.lang.mm.psi.*;

import static tekgenesis.lang.mm.completion.Imports.nameCanBeImported;
import static tekgenesis.lang.mm.style.MetaModelReferenceAdjuster.resolve;

/**
 * Intention for replacing fully qualified names with import.
 */
public class ReplaceFqnWithImportIntention extends Intention {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public ReplaceFqnWithImportIntention() {
        setText(PluginMessages.MSGS.replaceQualifiedNameWithImport());
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean startInWriteAction() {
        return true;
    }

    @Override protected void processIntention(@NotNull PsiElement element) {
        PsiMetaModelCodeReferenceElement reference = (PsiMetaModelCodeReferenceElement) element;

        // Process target
        PsiElement target = resolve(reference);
        if (!(target instanceof PsiMetaModel)) {
            PsiElement parent = reference.getParent();
            while (parent instanceof PsiJavaCodeReferenceElement) {
                reference = (PsiMetaModelCodeReferenceElement) parent;
                target    = resolve(reference);
                if (target instanceof PsiMetaModel) break;
                parent = parent.getParent();
            }
        }
        if (!(target instanceof PsiMetaModel)) return;

        final PsiMetaModel<?> mm  = (PsiMetaModel<?>) target;
        final String          fqn = mm.getFullName();
        if (Strings.isEmpty(fqn)) return;

        final MMFile file = PsiTreeUtil.getParentOfType(reference, MMFile.class);
        if (file == null) return;

        Imports.addMetaModelImport(file, mm);

        final String               fullyQualifiedText   = reference.getText();
        final QualificationRemover qualificationRemover = new QualificationRemover(fullyQualifiedText);
        file.accept(qualificationRemover);

        final Collection<PsiMetaModelCodeReferenceElement> shortened = qualificationRemover.getShortened();
        displayMessageAndHighlight(shortened, shortened.size());
    }

    @NotNull @Override PsiElementPredicate getElementPredicate() {
        return new FullyQualifiedNamePredicate();
    }

    private void displayMessageAndHighlight(Collection<PsiMetaModelCodeReferenceElement> shortened, int elementCount) {
        final String text;
        if (elementCount == 1) text = IntentionPowerPackBundle.message("1.fully.qualified.name.status.bar.escape.highlighting.message");
        else text = IntentionPowerPackBundle.message("multiple.fully.qualified.names.status.bar.escape.highlighting.message", elementCount);
        HighlightUtil.highlightElements(shortened, text);
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(ReplaceFqnWithImportIntention.class);

    //~ Inner Classes ................................................................................................................................

    private static class FullyQualifiedNamePredicate implements PsiElementPredicate {
        @Override public boolean satisfiedBy(PsiElement element) {
            if (!(element instanceof PsiMetaModelCodeReferenceElement)) return false;

            final PsiMetaModelCodeReferenceElement ref = (PsiMetaModelCodeReferenceElement) element;
            if (!ref.isQualified()) return false;

            @SuppressWarnings("unchecked")
            final PsiElement p = PsiTreeUtil.getParentOfType(element, PsiImport.class, PsiDomain.class);
            if (p != null) return false;

            final PsiElement qualifier = ref.getQualifier();
            if (!(qualifier instanceof PsiMetaModelCodeReferenceElement)) return false;

            final PsiReference reference = ref.getReference();
            if (reference == null) return false;

            final PsiElement target = reference.resolve();
            if (!(target instanceof PsiMetaModel)) return false;

            final PsiMetaModel<?> mm  = (PsiMetaModel<?>) target;
            final String          fqn = mm.getFullName();
            return !Strings.isEmpty(fqn) && nameCanBeImported(fqn, ref.getContainingFile().getQContext());
        }
    }

    private static class QualificationRemover extends MetaModelRecursiveVisitor {
        private final String                                 fqn;
        private final List<PsiMetaModelCodeReferenceElement> shortened = new ArrayList<>();

        QualificationRemover(String fqn) {
            this.fqn = fqn;
        }

        @Override
        @SuppressWarnings("MethodWithMultipleReturnPoints")
        public void visitReferenceElement(PsiMetaModelCodeReferenceElement reference) {
            super.visitReferenceElement(reference);

            final PsiElement parent = reference.getParent();
            if (parent instanceof PsiImport) return;

            final String text = reference.getText();
            if (!text.equals(fqn)) return;

            final PsiElement qualifier = reference.getQualifier();
            if (qualifier == null) return;

            final PsiElement name = reference.getReferenceNameElement();
            if (name == null) return;

            final PsiElement dot = name.getPrevSibling();
            if (dot == null || !".".equals(dot.getText())) return;

            delete(qualifier);
            delete(dot);

            shortened.add(reference);
        }

        public Collection<PsiMetaModelCodeReferenceElement> getShortened() {
            return Collections.unmodifiableCollection(shortened);
        }

        private void delete(PsiElement element) {
            try {
                element.delete();
            }
            catch (final IncorrectOperationException e) {
                logger.error(e);
            }
        }
    }  // end class QualificationRemover
}  // end class ReplaceFqnWithImportIntention
