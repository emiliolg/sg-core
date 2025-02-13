
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.pom.Navigatable;

import org.jetbrains.annotations.NotNull;

import tekgenesis.intellij.CommonLeafElement;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.documentationProvider.Documentable;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.mmcompiler.ast.MetaModelAST;

import static tekgenesis.lang.mm.documentationProvider.DocumentationManager.findDoc;

/**
 * Common PsiElement for composite nodes.
 */
public class MMLeafElement extends CommonLeafElement<MetaModelAST, MMElementType, MMToken> implements MetaModelAST, Navigatable, Documentable {

    //~ Instance Fields ..............................................................................................................................

    private String posibleCompletion;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    MMLeafElement(MMElementType t, CharSequence txt) {
        super(t, txt);
        posibleCompletion = "";
    }

    //~ Methods ......................................................................................................................................

    @Override public String getDocumentation() {
        final String text = posibleCompletion != null && posibleCompletion.isEmpty() ? getText() : posibleCompletion;  // uses
        // posibleCompletion in
        // cases like
        // lookUpItems, because
        // psi.getText() is
        // still incomplete
        return findDoc(text);
    }

    @NotNull @Override public MetaModelAST getEffectiveNode() {
        return this;
    }

    /** gets empty node. */
    public MMLeafElement getEmptyNode() {
        return EMPTY;
    }

    /** used in Documentation. */
    public void setPosibleCompletion(String posibleCompletion) {
        this.posibleCompletion = posibleCompletion;
    }

    //~ Static Fields ................................................................................................................................

    /** The Empty (Invalid) Leaf Element. */
    public static final MMLeafElement EMPTY = new MMLeafElement(MMElementType.EMPTY, "");
}  // end class MMLeafElement
