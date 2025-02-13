
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.impl.source.resolve.ResolveCache.AbstractResolver;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.RecursiveTreeElementWalkingVisitor;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.tree.TokenSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMElementType;
import tekgenesis.type.Types;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.lang.mm.MMElementType.REFERENCE;

/**
 * Utility class to deal with MetaModel References.
 */
public class MetaModelReferences {

    //~ Constructors .................................................................................................................................

    private MetaModelReferences() {}

    //~ Methods ......................................................................................................................................

    /** Resolve reference through cache. */
    @Nullable public static PsiElement resolveWithCaching(@NotNull final PsiMetaModelCodeReference                                  reference,
                                                          @NotNull final AbstractResolver<PsiMetaModelCodeReference, ResolveResult> resolver) {
        final PsiMetaModelCodeReferenceElement element = reference.getElement();
        final MMFile                           file    = element.getContainingFile();
        final ResolveResult                    result  = ResolveCache.getInstance(file.getProject())
                                                         .resolveWithCaching(reference, resolver, false, false);
        return result != null ? result.getElement() : null;
    }

    /** Return true if given reference is not a basic type. */
    public static boolean isNotBasicType(PsiMetaModelCodeReferenceElement element) {
        return element.getQualifier() != null || Types.fromString(notNull(element.getReferenceName())).isNull();
    }

    /** Return if given reference element is name part. */
    public static boolean isReferenceName(PsiMetaModelCodeReferenceElement element) {
        return element.getParent().getNode().getElementType() != REFERENCE;
    }

    /** Return reference kind for given PsiMetaModelCodeReferenceElement. */
    public static MetaModelReferenceKind getReferenceKind(PsiMetaModelCodeReferenceElement ref) {
        PsiElement parent = ref.getParent();
        while (parent != null && parent.getNode().getElementType() == REFERENCE)
            parent = parent.getParent();
        if (parent instanceof PsiDomain) return MetaModelReferenceKind.PACKAGE_NAME_KIND;
        if (parent instanceof PsiImport) return MetaModelReferenceKind.CLASS_FQ_NAME_KIND;
        return MetaModelReferenceKind.CLASS_NAME_KIND;
    }

    /** Get reference entire text. */
    @NotNull public static String getReferenceText(@NotNull PsiMetaModelCodeReferenceElement ref) {
        final StringBuilder buffer = new StringBuilder();

        ((TreeElement) ref.getNode()).acceptTree(new RecursiveTreeElementWalkingVisitor() {
                @Override public void visitLeaf(LeafElement leaf) {
                    if (!REF_FILTER.contains(leaf.getElementType())) buffer.append(leaf.getText());
                }

                @Override public void visitComposite(CompositeElement composite) {
                    if (!REF_FILTER.contains(composite.getElementType())) super.visitComposite(composite);
                }
            });

        return buffer.toString();
    }

    //~ Static Fields ................................................................................................................................

    private static final TokenSet REF_FILTER = TokenSet.orSet(MMElementType.COMMENTS, MMElementType.WHITE_SPACES);
}  // end class MetaModelReferences
