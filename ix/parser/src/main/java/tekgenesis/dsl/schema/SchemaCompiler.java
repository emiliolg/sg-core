
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.dsl.schema;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lexer.FileStream;
import tekgenesis.lexer.Lexer;
import tekgenesis.parser.AbstractASTBuilder;
import tekgenesis.parser.ParserErrorListener;
import tekgenesis.parser.Position;

/**
 * User: emilio Date: 26/11/11 Time: 19:02
 */
public class SchemaCompiler extends AbstractASTBuilder<SchemaAST, SchemaToken> implements ParserErrorListener {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final SchemaParser parser;

    //~ Constructors .................................................................................................................................

    private SchemaCompiler(File file, @NotNull Lexer<SchemaToken> lexer, boolean skipWhiteSpace) {
        super(lexer, file.getPath(), skipWhiteSpace);
        parser = new SchemaParser(this, this);
    }

    //~ Methods ......................................................................................................................................

    /** Parse the schema. */
    public SchemaAST parse() {
        beginNode();
        parser.parse();
        return buildAST(SchemaToken.UNIT, "");
    }

    @Override protected SchemaAST createNode(SchemaToken token, String text, Position position) {
        return new SchemaAST(token, text, position);
    }

    @Override protected final SchemaAST createNode(SchemaToken token, String text, Position position, List<SchemaAST> children) {
        return new SchemaAST(token, text, position, children);
    }

    //~ Methods ......................................................................................................................................

    /** Creates a compiler for the Schema file. */
    public static SchemaCompiler createSchemaCompiler(@NotNull File file) {
        try {
            final Lexer<SchemaToken> lexer = SchemaToken.lexer();
            lexer.resetStream(new FileStream(file));
            return new SchemaCompiler(file, lexer, true);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}  // end class SchemaCompiler
