
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

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Parser for Enum definitions.
 */
class EntityParser extends DatabaseObjectParser {

    //~ Constructors .................................................................................................................................

    /** Creates an Entity Parser. */
    private EntityParser(@NotNull MetaModelParser parent) {
        super(parent);
    }

    //~ Methods ......................................................................................................................................

    public void parse() {
        parseLabeledId(false);
        parseEntityOptions(false);
        parseList(LIST, LEFT_BRACE, null, RIGHT_BRACE, new EntityFieldParser());
        endTree(ENTITY);
    }

    @SuppressWarnings("OverlyLongMethod")
    private void parseEntityOptions(boolean embedded) {
        while (notCurrent(LEFT_BRACE)) {
            loopCheck();
            final MMToken token = getAnyOf(PRIMARY_KEY,
                    DESCRIBED_BY,
                    IMAGE,
                    SEARCHABLE,
                    UNIQUE,
                    INDEX,
                    FORM,
                    DEPRECABLE,
                    AUDITABLE,
                    REMOTABLE,
                    OPTIMISTIC,
                    CACHE,
                    TABLE);
            switch (token) {
            case PRIMARY_KEY:
                if (embedded) unexpectedError();
                parseOptionWithFields(PRIMARY_KEY);
                break;
            case DESCRIBED_BY:
                parseOptionWithFields(DESCRIBED_BY);
                break;
            case IMAGE:
                parseOptionWithField(IMAGE);
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
            case FORM:
                parseQualifiedId(FORM_REF, true);
                break;
            case DEPRECABLE:
                parseOptionWithoutFields(DEPRECABLE);
                break;
            case AUDITABLE:
                parseOptionWithoutFields(AUDITABLE);
                break;
            case REMOTABLE:
                parseOptionWithoutFields(REMOTABLE);
                break;
            case OPTIMISTIC:
                parseOptionWithoutFields(OPTIMISTIC);
                break;
            case CACHE:
                parseCache();
                break;
            case TABLE:
                parseTableName();
                break;
            default:
                unexpectedAndAdvanceTo(LEFT_BRACE);
                break;
            }
        }
    }  // end method parseEntityOptions

    //~ Methods ......................................................................................................................................

    static void register() {
        registerDefinitionParser(MMToken.ENTITY, EntityParser::new);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Parser for EntityFields.
     */
    private class EntityFieldParser implements Parser {
        @Override public void parse() {
            beginTree();
            parseLabeledId(false);
            if (currentOrError(COLON)) parseFieldDefinition();
            else {
                advanceTo(SEMICOLON, RIGHT_BRACE);
                discard(SEMICOLON);
            }
            endTree(FIELD);
        }

        private void parseEmbeddedEntity() {
            beginTree();
            if (current(DOCUMENTATION)) {
                beginTree();
                consume();
                endTree(DOCUMENTATION);
                if (!currentByText(ENTITY)) unexpectedAndAdvanceTo(COMMA, SEMICOLON, RIGHT_BRACE);
            }
            discard();
            beginTree();
            matchOrError(IDENTIFIER);
            match(ASTERISK);
            match(STRING_LITERAL);
            endTree(LABELED_ID);
            parseEntityOptions(true);
            parseList(LIST, LEFT_BRACE, null, RIGHT_BRACE, new EntityFieldParser());
            endTree(ENTITY);
        }

        private void parseEmbeddedEnum() {
            beginTree();
            discard();
            parseEnum();
        }

        private void parseEnum() {
            new EnumParser(EntityParser.this).parse();
        }

        private void parseFieldDefinition() {
            discard(COLON);

            if (current(ENUM)) parseEmbeddedEnum();
            else if (current(DOCUMENTATION) || current(IDENTIFIER)) {
                if (current(DOCUMENTATION) || currentByText(ENTITY)) parseEmbeddedEntity();
                else parseType(true, true, TYPE_REF);
            }
            else unexpectedAndAdvanceTo(COMMA, SEMICOLON, RIGHT_BRACE);

            parseFieldOptions(MetaModelKind.ENTITY, false);

            discardOrError(SEMICOLON);

            parseFieldDocumentation();
        }  // end method parseFieldDefinition
    }  // end class EntityFieldParser
}  // end class EntityParser
