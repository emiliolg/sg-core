
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
import tekgenesis.mmcompiler.parser.TypeParser.TypeFieldParser;
import tekgenesis.parser.Parser;

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Parser for Enum definitions.
 */
class EnumParser extends MetaModelParser {

    //~ Constructors .................................................................................................................................

    /** Creates an Entity Parser. */
    EnumParser(@NotNull MetaModelParser parent) {
        super(parent);
    }

    //~ Methods ......................................................................................................................................

    @Override public void parse() {
        parseLabeledId(false);
        parseEnumOptions();
        parseList(LIST, LEFT_BRACE, null, RIGHT_BRACE, this::parseEnumValue);
        endTree(ENUM);
    }

    private void parseEnumOptions() {
        while (notCurrent(LEFT_BRACE)) {
            loopCheck();
            final MMToken token = getAnyOf(FORM, WITH, DEFAULT, PRIMARY_KEY, INDEX);
            switch (token) {
            case FORM:
                parseQualifiedId(FORM_REF, false);
                break;

            case DEFAULT:
                beginTree();
                discard();
                parseRef(ENUM_FIELD_REF);
                endTree(DEFAULT);
                break;

            case PRIMARY_KEY:
                beginTree();
                discard();
                parseRef(FIELD_REF);
                endTree(PRIMARY_KEY);
                break;

            case INDEX:
                beginTree();
                discard();
                parseRef(FIELD_REF);
                endTree(INDEX);
                break;

            case WITH:
                discard();
                parseList(WITH, LEFT_BRACE, null, RIGHT_BRACE, new TypeFieldParser(this));
                break;

            case IMPLEMENTS:
                discard();
                parseValues(IMPLEMENTS, COMMA, new JavaRefParser());
                break;

            default:
                unexpectedAndAdvanceTo(LEFT_BRACE);
                break;
            }
        }
    }  // end method parseEnumOptions

    private void parseEnumValue() {
        if (current(IDENTIFIER)) {
            beginTree();
            parseId(ENUM_VALUE);
            if (discard(COLON)) {
                matchOrError(STRING_LITERAL);
                beginTree();
                while (discard(COMMA))
                    parseExpression();
                endTree(LIST);
            }
            discardOrError(SEMICOLON);
            parseFieldDocumentation();
            endTree(ENUM_FIELD);
        }
        else unexpectedToken();
    }

    //~ Methods ......................................................................................................................................

    static void register() {
        registerDefinitionParser(MMToken.ENUM, EnumParser::new);
    }

    //~ Inner Classes ................................................................................................................................

    private class JavaRefParser implements Parser {
        @Override public void parse() {
            parseQualifiedId(CLASS, false);
        }
    }
}
