
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.style;

import java.util.ArrayList;
import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.codeStyle.ReferenceAdjuster;
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.psi.MetaModelReferenceKind;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.lang.mm.psi.PsiMetaModelCodeReferenceElement;

import static tekgenesis.lang.mm.completion.Imports.addMetaModelImport;
import static tekgenesis.lang.mm.psi.MetaModelReferenceKind.CLASS_NAME_KIND;
import static tekgenesis.lang.mm.psi.MetaModelReferences.getReferenceKind;

/**
 * Reference adjuster for MetaModel language.
 */
public class MetaModelReferenceAdjuster implements ReferenceAdjuster {

    //~ Methods ......................................................................................................................................

    @Override public ASTNode process(@NotNull ASTNode element, boolean addImports, boolean incompleteCode, Project project) {
        return process(element, addImports, incompleteCode, false, false);
    }

    @Override public ASTNode process(@NotNull ASTNode node, boolean addImports, boolean incompleteCode, boolean useFqInJavadoc, boolean useFqInCode) {
        final IElementType type = node.getElementType();

        if (type == MMElementType.REFERENCE) {
            final PsiMetaModelCodeReferenceElement ref  = (PsiMetaModelCodeReferenceElement) node.getPsi();
            final MetaModelReferenceKind           kind = getReferenceKind(ref);

            if (kind == CLASS_NAME_KIND) {
                final boolean isShort = !ref.isQualified();
                if (isShort) return node;

                final PsiElement element = incompleteCode ? null : resolve(ref);

                if (element instanceof PsiMetaModel) {
                    final PsiMetaModel<?> mm        = (PsiMetaModel<?>) element;
                    final int             oldLength = node.getTextLength();
                    makeShortReference(ref, mm);
                    if (node.getTextLength() == oldLength) {
                        final PsiElement qualifier = ref.getQualifier();
                        if (qualifier instanceof PsiMetaModelCodeReferenceElement && resolve(qualifier) instanceof PsiMetaModel)
                            process(qualifier.getNode(), addImports, false, useFqInJavadoc, useFqInCode);
                    }
                    return node;
                }
            }
        }

        for (ASTNode child = node.getFirstChildNode(); child != null; child = child.getTreeNext())
            // noinspection AssignmentToForLoopParameter
            child = process(child, addImports, incompleteCode, useFqInJavadoc, useFqInCode);

        return node;
    }  // end method process

    @Override public void processRange(@NotNull ASTNode element, int startOffset, int endOffset, Project project) {
        processRange(element, startOffset, endOffset, false, false);
    }

    @Override public void processRange(@NotNull ASTNode element, int startOffset, int endOffset, boolean useFqInJavadoc, boolean useFqInCode) {
        final List<ASTNode> array = new ArrayList<>();
        addReferencesInRange(array, element, startOffset, endOffset);
        for (final ASTNode ref : array) {
            if (ref.getPsi().isValid()) process(ref, true, true, useFqInJavadoc, useFqInCode);
        }
    }

    //~ Methods ......................................................................................................................................

    /** Simple element reference resolution. */
    public static PsiElement resolve(PsiElement ref) {
        final PsiReference reference = ref.getReference();
        return reference != null ? reference.resolve() : null;
    }

    private static void addReferencesInRange(@NotNull List<ASTNode> collector, ASTNode parent, int startOffset, int endOffset) {
        if (parent.getElementType() == MMElementType.REFERENCE) {
            collector.add(parent);
            return;
        }
        addReferencesInRangeForComposite(collector, parent, startOffset, endOffset);
    }

    private static void addReferencesInRangeForComposite(@NotNull List<ASTNode> collector, ASTNode parent, int startOffset, int endOffset) {
        int offset = 0;
        for (ASTNode child = parent.getFirstChildNode(); child != null; child = child.getTreeNext()) {
            final int length = child.getTextLength();
            if (startOffset <= offset + length && offset <= endOffset) {
                final IElementType type = child.getElementType();
                if (type == MMElementType.REFERENCE) collector.add(child);
                else addReferencesInRangeForComposite(collector, child, startOffset - offset, endOffset - offset);
            }
            offset += length;
        }
    }

    private static void makeShortReference(@NotNull PsiMetaModelCodeReferenceElement ref, @NotNull PsiMetaModel<?> mm) {
        final boolean addedImport = addMetaModelImport(ref.getContainingFile(), mm);
        if (addedImport) replaceReferenceWithShort(ref);
    }

    private static void removeQualification(@NotNull PsiMetaModelCodeReferenceElement reference) {
        final PsiElement q = reference.getQualifier();
        if (q != null) {
            final ASTNode qualifier = q.getNode();
            final ASTNode first     = qualifier.getFirstChildNode();
            final boolean marked    = first instanceof TreeElement && CodeEditUtil.isMarkedToReformatBefore((TreeElement) first);
            ((CompositeElement) reference.getNode()).deleteChildInternal(qualifier);
            if (marked) {
                final ASTNode node = reference.getFirstChildNode();
                if (node != null) CodeEditUtil.markToReformatBefore(node, true);
            }
        }
    }

    private static void replaceReferenceWithShort(PsiMetaModelCodeReferenceElement reference) {
        final ASTNode node = reference.getNode();
        assert node != null;
        removeQualification(reference);
    }
}  // end class MetaModelReferenceAdjuster
