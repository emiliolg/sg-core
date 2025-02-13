
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
import tekgenesis.type.MetaModelKind;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Parser for Handler definitions.
 */
class HandlerParser extends MetaModelParser {

    //~ Constructors .................................................................................................................................

    /** Creates a Handler Parser. */
    private HandlerParser(@NotNull MetaModelParser parent) {
        super(parent);
    }

    //~ Methods ......................................................................................................................................

    public void parse() {
        parseLabeledId(false);
        parseHandlerOptions();
        parseHandlerRoutes();
        endTree(HANDLER);
    }

    @NotNull @Override Type getTypeFromString(String typeName) {
        return Types.extendedFromString(typeName, super.getTypeFromString(typeName));
    }

    private void parseHandlerOptions() {
        while (notCurrentAnyOf(LEFT_BRACE, SEMICOLON)) {
            loopCheck();
            final MMToken token = getAnyOf(ON_ROUTE, FORM, UNRESTRICTED, SECURE_BY, RAISE);
            switch (token) {
            case FORM:
                parseQualifiedId(FORM_REF, true);
                break;
            case ON_ROUTE:
                parseOnRoute(token);
                break;
            case SECURE_BY:
                parseId(token, true);
                break;
            case UNRESTRICTED:
                parseOptionWithoutFields(UNRESTRICTED);
                break;
            case RAISE:
                parseQualifiedId(ENUM_REF, true);
                break;
            default:
                unexpectedAndAdvanceTo(LEFT_BRACE);
                break;
            }
        }
    }

    private void parseHandlerRoutes() {
        if (!parseList(LIST, LEFT_BRACE, null, RIGHT_BRACE, new RouteParser())) discardOrError(SEMICOLON);
    }

    private void parseOnRoute(MMToken token) {
        beginTree();
        discard();
        match(STRING_LITERAL);
        endTree(token);
    }

    //~ Methods ......................................................................................................................................

    static void register() {
        registerDefinitionParser(MMToken.HANDLER, HandlerParser::new);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Handler route parser.
     */
    private class RouteParser implements Parser {
        @Override public void parse() {
            beginTree();

            // Parse path
            parsePath();
            discardOrError(COLON);

            // Parse type, method and options
            if (current(IDENTIFIER)) parseTypeMethodAndOptions();
            else {
                unexpectedAndAdvanceTo(SEMICOLON);
                discard(SEMICOLON);
            }

            endTree(ROUTE);
        }

        private void parsePath() {
            beginTree();
            matchOrError(STRING_LITERAL);
            endTree(PATH);
        }

        private void parseTypeMethodAndOptions() {
            parseType(true);

            discardOrError(COMMA);

            if (current(IDENTIFIER)) parseId(METHOD_REF);
            else unexpectedAndAdvanceTo(COMMA, SEMICOLON, RIGHT_BRACE);

            if (discard(COMMA)) parseFieldOptions(MetaModelKind.HANDLER, true);

            discardOrError(SEMICOLON);
        }
    }  // end class RouteParser
}  // end class HandlerParser
