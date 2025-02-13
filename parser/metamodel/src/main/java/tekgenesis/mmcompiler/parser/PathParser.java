
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

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Parser for anonymous paths definitions. Path parser is injected on handler's string literal
 * paths.
 */
class PathParser extends MetaModelParser {

    //~ Constructors .................................................................................................................................

    /** Creates a Handler Parser. */
    private PathParser(@NotNull MetaModelParser parent) {
        super(parent);
    }

    //~ Methods ......................................................................................................................................

    public void parse() {
        if (discard(COLON)) {
            do {
                discardOrError(SLASH);
                if (!current(EMPTY_TOKEN)) parsePart();
            }
            while (current(SLASH));
        }
        else advanceTo(SEMICOLON);

        discard(SEMICOLON);
        endTree(PATH);
    }

    private void consumePart() {
        beginTree();
        consume();
        endTree(PART);
    }

    private void parsePart() {
        while (!currentAnyOf(SLASH, SEMICOLON) && !eof()) {
            loopCheck();
            if (current(IDENTIFIER) || getCurrent().isLiteral() || getCurrent().isKeyword()) consumePart();
            else if (discard(DOLLAR)) {
                parseId(PARAMETER);
                if (discard(COLON)) parseType(false);
                else discard(ASTERISK);
            }
            else if (currentAnyOf(MINUS, DOT)) discard();
            else unexpectedAndAdvanceTo(SLASH, SEMICOLON);
        }
    }

    //~ Methods ......................................................................................................................................

    static void register() {
        registerDefinitionParser(MMToken.PATH, PathParser::new);
    }
}  // end class PathParser
