
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

import tekgenesis.common.collections.AbstractIterator;
import tekgenesis.mmcompiler.ast.MetaModelAST;

/**
 * Iterator for MMCommonComposites.
 */
public class MMIterator extends AbstractIterator<MetaModelAST> {

    //~ Instance Fields ..............................................................................................................................

    private final ASTNode tree;

    //~ Constructors .................................................................................................................................

    /** Constructor for MMIterator. */
    public MMIterator(ASTNode tree) {
        this.tree = tree;
    }

    //~ Methods ......................................................................................................................................

    protected boolean advance() {
        next = (MetaModelAST) ((ASTNode) next).getTreeNext();
        return next != null;
    }

    @Override protected boolean start() {
        next = (MetaModelAST) tree.getFirstChildNode();
        return next != null;
    }
}
