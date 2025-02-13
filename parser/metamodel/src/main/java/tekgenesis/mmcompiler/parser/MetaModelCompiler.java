
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.parser;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lexer.CharSequenceStream;
import tekgenesis.lexer.Lexer;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.mmcompiler.ast.MetaModelASTImpl;
import tekgenesis.parser.AbstractASTBuilder;
import tekgenesis.parser.Position;

/**
 * User: emilio Date: 26/11/11 Time: 19:02
 */
public class MetaModelCompiler extends AbstractASTBuilder<MetaModelAST, MMToken> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull final MetaModelParser parser;
    @Nullable private MetaModelAST rootNode;

    //~ Constructors .................................................................................................................................

    /** Create a compiler for the specified {@link}File}. */
    public MetaModelCompiler(@NotNull CharSequenceStream stream, String path) {
        this(MMToken.lexer().resetStream(stream), path);
    }

    MetaModelCompiler(@NotNull Lexer<MMToken> lexer, @NotNull String sourceName) {
        super(lexer, sourceName, true);
        parser   = new MetaModelParser(this, this);
        rootNode = null;
    }

    //~ Methods ......................................................................................................................................

    /** Get the root node for the AST, parsing the file if necessary. */
    @NotNull public MetaModelAST getAST() {
        if (rootNode == null) {
            beginNode();
            parse();
            rootNode = buildAST(MMToken.FILE, "");
        }
        return rootNode;
    }

    @Override protected final MetaModelAST createNode(MMToken token, String text, Position position) {
        return new MetaModelASTImpl(token, text, position);
    }

    @Override protected final MetaModelAST createNode(MMToken token, String text, Position position, List<MetaModelAST> children) {
        return new MetaModelASTImpl(token, text, position, children);
    }

    void parse() {
        parser.parse();
    }
}  // end class MetaModelCompiler
