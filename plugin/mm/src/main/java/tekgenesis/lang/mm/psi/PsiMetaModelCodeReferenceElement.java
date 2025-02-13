
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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.SharedPsiElementImplUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.MMElementType;

import static com.intellij.psi.impl.source.SourceTreeToPsiMap.treeElementToPsi;

import static tekgenesis.common.Predefined.option;

/**
 * Represents a reference element found in MetaModel code (either an identifier or a sequence of
 * identifiers separated by periods).
 */
public interface PsiMetaModelCodeReferenceElement extends MetaModelPsiElement, ASTNode {

    //~ Methods ......................................................................................................................................

    /** Returns true if reference element contains qualifier. */
    default boolean isQualified() {
        return getQualifier() != null;
    }

    /**
     * Returns the qualifier of the reference (the element representing the content up to the last
     * period), or null if the reference is not qualified.
     */
    @Nullable default PsiElement getQualifier() {
        return treeElementToPsi(findChildByType(MMElementType.REFERENCE));
    }

    /**
     * Returns the text of the reference not including its qualifier, or null if the reference
     * element is incomplete.
     */
    @Nullable default String getReferenceName() {
        return getReferenceNameNode().map(ASTNode::getText).getOrNull();
    }

    /**
     * Returns the element representing the name of the referenced element, or null if the reference
     * is incomplete.
     */
    @Nullable default PsiElement getReferenceNameElement() {
        return getReferenceNameNode().map(ASTNode::getPsi).getOrNull();
    }

    /** Returns the non-qualified text node of the reference. */
    @NotNull default Option<ASTNode> getReferenceNameNode() {
        return option(findChildByType(MMElementType.IDENTIFIER));
    }
    @NotNull @Override default PsiReference[] getReferences() {
        return SharedPsiElementImplUtil.getReferences(this);
    }
}  // end interface PsiMetaModelCodeReferenceElement
