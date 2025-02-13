
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.psi.tree.TokenSet;

import tekgenesis.lang.mm.MMElementType;

/**
 * Node representing an element with a list of references to another elements.
 */
public class ReferencesList extends MMCommonComposite {

    //~ Constructors .................................................................................................................................

    /** Create a PsiElement. */
    ReferencesList(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    /*@Override public void annotate(AnnotationHolder holder) {
     *  final PsiFieldReference[] children = getFields();
     *  for (final PsiFieldReference child : children) {
     *      for (FieldReference reference : child.getReferences()) {
     *          if (reference.resolve() == null)
     *              holder.createErrorAnnotation((PsiElement) child, PluginMessages.MSGS.cannotResolveField())
     *                      .setHighlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL);
     *      }
     *  }
     *}*/

    /** Returns all the references. */
    public PsiFieldReference[] getFields() {
        return getChildrenAsPsiElements(TokenSet.create(MMElementType.FIELD_REF), PsiFieldReference[]::new);
    }
}
