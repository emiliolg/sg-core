
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.dsl.schema;

import java.util.List;

import tekgenesis.parser.AbstractASTNode;
import tekgenesis.parser.Position;

import static tekgenesis.parser.Position.LineColumnPosition.*;

/**
 * This class implements an AST node for the Schema parser.
 */
public class SchemaAST extends AbstractASTNode<SchemaAST, SchemaToken> {

    //~ Constructors .................................................................................................................................

    /**
     * Creates an instance of the Schema AST Node.
     *
     * @param  type      The Token Type for the Node
     * @param  text      The text for the node
     * @param  position  The position in the input
     */
    public SchemaAST(SchemaToken type, String text, Position position) {
        super(type, text, position);
    }
    /**
     * Creates an instance of the Schema AST Node.
     *
     * @param  type      The Token Type for the Node
     * @param  text      The text for the node
     * @param  position  The position in the input
     * @param  children  The node's childrenBuilders
     */
    public SchemaAST(SchemaToken type, String text, Position position, List<SchemaAST> children) {
        super(type, text, position, children);
    }

    //~ Methods ......................................................................................................................................

    @Override public SchemaAST getEmptyNode() {
        return EMPTY_NODE;
    }

    //~ Static Fields ................................................................................................................................

    private static final SchemaAST EMPTY_NODE = new SchemaAST(SchemaToken.EOF_TOKEN, "", ZERO);
}
