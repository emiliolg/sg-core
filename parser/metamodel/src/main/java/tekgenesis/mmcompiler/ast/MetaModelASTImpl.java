
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.ast;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.parser.AbstractASTNode;
import tekgenesis.parser.Position;

import static tekgenesis.parser.Position.LineColumnPosition.*;

/**
 * This class implements an AST node for the Entity and Form parsers.
 */
public class MetaModelASTImpl extends AbstractASTNode<MetaModelAST, MMToken> implements MetaModelAST {

    //~ Constructors .................................................................................................................................

    /**
     * Creates an instance of the Entity AST Node.
     *
     * @param  type      The Token Type for the Node
     * @param  text      The text for the node
     * @param  position  The position in the input
     */
    public MetaModelASTImpl(MMToken type, String text, Position position) {
        super(type, text, position);
    }

    /**
     * Creates an instance of the Entity AST Node.
     *
     * @param  type      The Token Type for the Node
     * @param  text      The text for the node
     * @param  position  The position in the input
     * @param  children  The node's children
     */
    public MetaModelASTImpl(MMToken type, String text, Position position, List<MetaModelAST> children) {
        super(type, text, position, children);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public MetaModelAST getEffectiveNode() {
        return getType().isWrapper() ? getFirst().orElse(this) : this;
    }

    @Override public MetaModelAST getEmptyNode() {
        return EMPTY_NODE;
    }

    //~ Static Fields ................................................................................................................................

    private static final MetaModelAST EMPTY_NODE = new MetaModelASTImpl(MMToken.EMPTY_TOKEN, "", ZERO);
}  // end class MetaModelASTImpl
