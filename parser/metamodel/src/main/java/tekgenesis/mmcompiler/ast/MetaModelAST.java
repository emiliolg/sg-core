
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.ast;

import org.jetbrains.annotations.NotNull;

import tekgenesis.parser.ASTNode;

/**
 * The interface for the MetaModel Tree.
 */
public interface MetaModelAST extends ASTNode<MetaModelAST, MMToken> {

    //~ Methods ......................................................................................................................................

    @NotNull @Override MetaModelAST getEffectiveNode();
}
