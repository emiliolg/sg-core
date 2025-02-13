
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.parser;

import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.parser.Parser;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Parser for view definition.
 */
class ViewParser extends DatabaseObjectParser {

    //~ Instance Fields ..............................................................................................................................

    private boolean asView;

    //~ Constructors .................................................................................................................................

    private ViewParser(MetaModelParser parent) {
        super(parent);
    }

    //~ Methods ......................................................................................................................................

    public void parse() {
        parseLabeledId(false);
        parseViewOptions();
        parseViewElements();
        endTree(VIEW);
    }

    private void parseAs() {
        beginTree();
        consume();
        match(STRING_LITERAL);
        endTree(AS);
        asView = true;
    }

    private void parseBatchSize() {
        beginTree();
        discard();
        parseOptionalInteger();
        endTree(BATCH_SIZE);
    }

    private void parseViewElements() {
        if (!parseList(LIST, LEFT_BRACE, null, RIGHT_BRACE, new ViewElementParser())) discardOrError(SEMICOLON);
    }

    private void parseViewOptions() {
        currentByTextAnyOfOrError(OF, AS);
        while (notCurrentAnyOf(LEFT_BRACE)) {
            loopCheck();
            final MMToken token = getAnyOf(OF,
                    DESCRIBED_BY,
                    IMAGE,
                    SEARCHABLE,
                    UPDATABLE,
                    INDEX,
                    UNIQUE,
                    AS,
                    PRIMARY_KEY,
                    REMOTABLE,
                    OPTIMISTIC,
                    BATCH_SIZE);
            switch (token) {
            case OF:
                parseOptionWithRefsExpecting(OF, DATAOBJECT_REF);
                break;
            case PRIMARY_KEY:
                parseOptionWithFields(PRIMARY_KEY);
                break;
            case DESCRIBED_BY:
                parseOptionWithFields(DESCRIBED_BY);
                break;
            case IMAGE:
                parseOptionWithField(IMAGE);
                break;
            case UPDATABLE:
                parseOptionWithoutFields(UPDATABLE);
                break;
            case BATCH_SIZE:
                parseBatchSize();
                break;
            case REMOTABLE:
                parseOptionWithoutFields(REMOTABLE);
                break;
            case OPTIMISTIC:
                parseOptionWithoutFields(OPTIMISTIC);
                break;
            case SEARCHABLE:
                parseSearchable();
                break;
            case UNIQUE:
                parseIndex(UNIQUE);
                break;
            case INDEX:
                parseIndex(INDEX);
                break;
            case AS:
                parseAs();
                break;
            default:
                unexpectedAndAdvanceTo(LEFT_BRACE);
                break;
            }
        }
    }  // end method parseViewOptions

    //~ Methods ......................................................................................................................................

    static void register() {
        registerDefinitionParser(MMToken.VIEW, ViewParser::new);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Parser for View Elements.
     */
    private final class ViewElementParser implements Parser {
        @Override public void parse() {
            beginTree();
            parseLabeledId(false);
            if (currentOrError(COLON)) {
                if (asView) parseFieldDefinition(false);
                else {
                    discard(COLON);
                    if (current(IDENTIFIER)) {
                        if (lookAhead(1) == MMToken.COMMA || lookAhead(2) == MMToken.COMMA) parseFieldDefinition(true);
                        else {
                            parseId(FIELD_REF);
                            discardOrError(SEMICOLON);
                        }
                    }
                    else unexpectedAndAdvanceTo(SEMICOLON, RIGHT_BRACE);
                }
            }
            else {
                advanceTo(SEMICOLON, RIGHT_BRACE);
                discard(SEMICOLON);
            }
            endTree(FIELD);
        }

        private void parseFieldDefinition(boolean allowTypeCardinality) {
            discard(COLON);

            if (current(IDENTIFIER)) parseType(true, allowTypeCardinality, TYPE_REF);
            else unexpectedAndAdvanceTo(COMMA, SEMICOLON, RIGHT_BRACE);

            parseFieldOptions(MetaModelKind.ENTITY, false);

            discardOrError(SEMICOLON);
        }
    }  // end class ViewElementParser
}  // end class ViewParser
