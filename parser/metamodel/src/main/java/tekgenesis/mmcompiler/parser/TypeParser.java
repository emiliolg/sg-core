
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
 * Parser for Type definitions.
 */
class TypeParser extends MetaModelParser {

    //~ Constructors .................................................................................................................................

    /** Creates an Type Parser. */
    private TypeParser(@NotNull MetaModelParser parent) {
        super(parent);
    }

    //~ Methods ......................................................................................................................................

    public void parse() {
        parseLabeledId(false);
        if (discard(EQ)) {
            beginTree();
            if (current(IDENTIFIER)) parseType(true);
            else unexpectedAndAdvanceTo(COMMA, SEMICOLON);
            parseFieldOptions(MetaModelKind.TYPE, false);
            discardOrError(SEMICOLON);
            endTree(FIELD);
        }
        else {
            parseList(ARG_LIST, LEFT_PAREN, null, RIGHT_PAREN, new TypeArgParser(this));
            parseStructTypeOptions();
            parseList(LIST, LEFT_BRACE, null, RIGHT_BRACE, new TypeFieldParser(this, true, true, STRUCT_REF));
        }
        endTree(TYPE);
    }

    private void parseStructTypeOptions() {
        while (currentByTextAnyOf(EXTENDS, WIDGET)) {
            loopCheck();
            final MMToken token = getAnyOf(EXTENDS, WIDGET);
            switch (token) {
            case EXTENDS:
                discard(EXTENDS);
                parseValues(EXTENDS, COMMA, new StructRefParser());
                break;
            case WIDGET:
                parseQualifiedId(WIDGET, true);
                break;
            default:
                unexpectedAndAdvanceTo(LEFT_BRACE);
                break;
            }
        }
    }

    //~ Methods ......................................................................................................................................

    static void register() {
        registerDefinitionParser(MMToken.TYPE, TypeParser::new);
    }

    //~ Inner Classes ................................................................................................................................

    private class StructRefParser implements Parser {
        @Override public void parse() {
            parseQualifiedId(STRUCT_REF, false);
        }
    }

    /**
     * Parser for Arguments.
     */
    static class TypeArgParser extends MetaModelParser {
        public TypeArgParser(final MetaModelParser typeParser) {
            super(typeParser);
        }

        @Override public void parse() {
            beginTree();
            parseLabeledId(false);
            if (currentOrError(COLON)) parseFieldDefinition();
            else advanceTo(COLON, RIGHT_PAREN);
            endTree(FIELD);
        }

        private void parseFieldDefinition() {
            discard(COLON);
            if (current(IDENTIFIER)) parseType(true, true, STRUCT_REF);
            else unexpectedAndAdvanceTo(COMMA, RIGHT_BRACE);
            parseFieldOptions(MetaModelKind.TYPE, false);

            final boolean notSemicolon = !discard(SEMICOLON);
            parseFieldDocumentation();
            if (notSemicolon) currentOrError(RIGHT_PAREN);
        }
    }

    /**
     * Parser for fields.
     */
    static class TypeFieldParser extends MetaModelParser {
        private final boolean allowBasicTypeCardinality;

        private final boolean allowReferenceCardinality;
        private final MMToken refType;

        public TypeFieldParser(final MetaModelParser typeParser) {
            this(typeParser, true, true, TYPE_REF);
        }

        public TypeFieldParser(final MetaModelParser typeParser, boolean allowReferenceCardinality, boolean allowBasicTypeCardinality,
                               final MMToken refType) {
            super(typeParser);
            this.allowReferenceCardinality = allowReferenceCardinality;
            this.allowBasicTypeCardinality = allowBasicTypeCardinality;
            this.refType                   = refType;
        }

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

        private void parseFieldDefinition() {
            discard(COLON);
            if (current(IDENTIFIER)) parseType(allowReferenceCardinality, allowBasicTypeCardinality, refType);
            else unexpectedAndAdvanceTo(COMMA, SEMICOLON, RIGHT_BRACE);
            parseFieldOptions(MetaModelKind.TYPE, false);
            discardOrError(SEMICOLON);
            parseFieldDocumentation();
        }
    }  // end class TypeFieldParser
}  // end class TypeParser
