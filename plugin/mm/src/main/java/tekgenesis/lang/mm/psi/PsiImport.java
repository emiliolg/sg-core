
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.i18n.PluginMessages;

import static tekgenesis.lang.mm.psi.MetaModelReferences.getReferenceText;

/**
 * The package identifier.
 */
public class PsiImport extends ElementWithReferences {

    //~ Constructors .................................................................................................................................

    /** Creates an Identifier. */
    PsiImport(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Override public void annotate(AnnotationHolder holder) {
        super.annotate(holder);
        if (isNotInjected()) {
            final PsiMetaModelCodeReferenceElement element = getImportReference();
            if (element != null) {
                final PsiReference reference = element.getReference();
                if (reference != null) {
                    final PsiElement resolve = reference.resolve();
                    if (resolve == null)
                        holder.createAnnotation(HighlightSeverity.ERROR, element.getTextRange(), PluginMessages.MSGS.cannotResolveImport());
                }
            }
        }
    }

    /** Return import reference text. */
    public String getImportReferenceText() {
        final PsiMetaModelCodeReferenceElement reference = getImportReference();
        return reference != null ? getReferenceText(reference) : null;
    }

    @Override public PsiReference getReference() {
        final PsiMetaModelCodeReferenceElement reference = getImportReference();
        return reference != null ? reference.getReference() : null;
    }

    @Nullable private PsiMetaModelCodeReferenceElement getImportReference() {
        final ASTNode node = findChildByType(MMElementType.REFERENCE);
        return node != null ? ((PsiMetaModelCodeReferenceElement) node.getPsi()) : null;
    }
}  // end class PsiImport
