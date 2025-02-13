
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.parser;

import org.jetbrains.annotations.NotNull;

import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.parser.Parser;

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Parser for Link definitions.
 */
class LinkParser extends MetaModelParser {

    //~ Constructors .................................................................................................................................

    /** Creates a Link Parser. */
    private LinkParser(@NotNull MetaModelParser parent) {
        super(parent);
    }

    //~ Methods ......................................................................................................................................

    public void parse() {
        parseLabeledId(false);
        discardOrError(EQ);

        final MMToken token = getAnyOf(STRING_LITERAL, IDENTIFIER);

        switch (token) {
        case STRING_LITERAL:
            match(STRING_LITERAL);
            break;
        case IDENTIFIER:
            parseQualifiedId(FORM_REF, false);
            parseList(LIST, LEFT_PAREN, COMMA, RIGHT_PAREN, new AssignmentParser());
            break;
        default:
            unexpectedAndAdvanceTo(SEMICOLON, RIGHT_BRACE);
        }

        discardOrError(SEMICOLON);
        endTree(LINK);
    }

    //~ Methods ......................................................................................................................................

    static void register() {
        registerDefinitionParser(LINK, LinkParser::new);
    }

    //~ Inner Classes ................................................................................................................................

    private final class AssignmentParser implements Parser {
        @Override public void parse() {
            beginTree();
            parseRef(FIELD_REF);
            discardOrError(EQ);
            parseExpression();
            endTree(ASSIGNMENT);
        }
    }  // end AssignmentParser
}  // end class LinkParser
