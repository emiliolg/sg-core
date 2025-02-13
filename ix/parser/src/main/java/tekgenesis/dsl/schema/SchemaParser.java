
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.dsl.schema;

import java.util.EnumSet;

import org.jetbrains.annotations.NotNull;

import tekgenesis.parser.ASTBuilder;
import tekgenesis.parser.AbstractParser;
import tekgenesis.parser.LoopException;
import tekgenesis.parser.ParserErrorListener;

import static tekgenesis.dsl.schema.SchemaToken.*;
import static tekgenesis.parser.ParserCommonMessages.MSGS;

/**
 * User: emilio Date: 22/11/11 Time: 13:35
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class SchemaParser extends AbstractParser<SchemaToken> {

    //~ Constructors .................................................................................................................................

    /** Ideafic .sc file parser. */
    public SchemaParser(@NotNull ASTBuilder<SchemaToken> schemaTokenASTBuilder, ParserErrorListener listener) {
        super(schemaTokenASTBuilder, listener);
    }

    //~ Methods ......................................................................................................................................

    @Override public void parse() {
        try {
            while (!eof()) {
                loopCheck();
                parseStatement();
            }
        }
        catch (final LoopException l) {
            error(l.getPosition(), MSGS.loop());
        }
    }

    private void parseBetween(boolean between) {
        beginTree();
        if (!between) matchOrError(NOT);
        matchOrError(BETWEEN);
        parseValue();
        matchOrError(AND);
        parseValue();
        endTree(between ? BETWEEN : NOT_BETWEEN);
    }

    private void parseCheck() {
        beginTree();
        discardOrError(LEFT_PAREN);
        // parseExpression
        discardOrError(RIGHT_PAREN);
        endTree(EXPRESSION);
    }

    private void parseDefault() {
        beginTree();
        discard(DEFAULT);
        if (discard(LEFT_PAREN)) discardOrError(RIGHT_PAREN);
        else if (match(DOLLAR)) matchOrError(IDENTIFIER);
        else parseValue();
        endTree(DEFAULT);
    }

    private void parseDimension() {
        if (current(LEFT_BRACKET)) {
            discard();
            parseInteger();
            matchOrError(RIGHT_BRACKET);
        }
    }

    private void parseField() {
        beginTree();
        match(IDENTIFIER);
        parseType();
        beginTree();
        while (notCurrentAnyOf(COMMA, RIGHT_BRACE)) {
            loopCheck();
            parseOption();
        }
        endTree(FIELD_OPTIONS);
        discard(COMMA);
        endTree(TABLE_FIELD);
    }

    private void parseFields() {
        if (current(LEFT_BRACE)) {
            beginTree();
            discardOrError(LEFT_BRACE);
            while (current(IDENTIFIER)) {
                loopCheck();
                parseField();
            }
            discardOrError(RIGHT_BRACE);
            endTree(TABLE_FIELDS);
        }
    }

    private void parseGrant() {
        beginTree();
        discard(GRANT);
        parsePrivileges();
        parseGrantTarget();
        parseGrantUsers();
        parseGrantOption();
        discardOrError(SEMICOLON);
        endTree(GRANT);
    }

    private void parseGrantOption() {
        if (discard(WITH)) {
            matchOrError(GRANT);
            discardOrError(OPTION);
        }
    }

    private void parseGrantTarget() {
        // todo complete other options
        discardOrError(ON);
        if (match(SCHEMA)) match(IDENTIFIER);
        else matchAnyOf(ASTERISK, IDENTIFIER);
    }

    private void parseGrantUsers() {
        // todo complete other options
        discardOrError(TO);
        matchOrError(PUBLIC);
    }

    private void parseIn(boolean in) {
        if (next(LEFT_PAREN)) parseInValues(in);
        else parseInTable(in);
    }

    private void parseIndexField() {
        beginTree();
        matchOrError(IDENTIFIER);

        beginTree();
        if (current(LEFT_PAREN)) parseParameters();
        endTree(RANGE);
        while (currentAnyOf(NOT, ASCENDING, DESCENDING)) {
            loopCheck();
            final SchemaToken token = getCurrent();
            if (token == SchemaToken.NOT) parseNotNull();
            else match(token);
        }
        endTree(INDEX_FIELD);
    }

    private void parseIndexFields() {
        beginTree();
        final boolean     braces = current(LEFT_BRACE);
        final SchemaToken end    = braces ? RIGHT_BRACE : RIGHT_PAREN;

        discardOrError(braces ? LEFT_BRACE : LEFT_PAREN);
        do {
            loopCheck();

            parseIndexField();
            discard(COMMA);
        }
        while (notCurrent(end));
        discardOrError(end);
        endTree(INDEX_FIELDS);
    }

    private void parseIndices() {
        beginTree();
        while (currentAnyOf(PRIMARY, UNIQUE, INDEX)) {
            loopCheck();

            beginTree();
            final SchemaToken token = getCurrent();
            discard(token);

            if (token == SchemaToken.PRIMARY) discardOrError(KEY);
            else if (token == SchemaToken.UNIQUE) {
                discardOrError(INDEX);
                matchOrError(IDENTIFIER);
            }
            else matchOrError(IDENTIFIER);
            parseIndexFields();
            parseDimension();
            endTree(token);

            discard(COMMA);
        }
        endTree(TABLE_INDICES);
    }

    private void parseInKeyFields() {
        if (current(LEFT_PAREN)) {
            beginTree();
            discard(LEFT_PAREN);
            while (notCurrent(RIGHT_PAREN)) {
                loopCheck();

                // TODO ID. ID
                if (!match(IDENTIFIER)) {
                    if (!match(DOLLAR)) parseValue();
                    else matchOrError(IDENTIFIER);
                }
                discard(COMMA);
            }
            discard(RIGHT_PAREN);
            endTree(IN_TABLE_KEYS);
        }
    }

    private void parseInTable(boolean in) {
        beginTree();
        discardOrError(IN);
        matchOrError(IDENTIFIER);
        parseSimpleTree(BY, IDENTIFIER);
        parseInKeyFields();
        if (current(COLON)) parseInTableDescriptions();
        // To change body of created methods use File | Settings | File Templates.
        endTree(in ? IN_TABLE : NOT_IN_TABLE);
    }

    private void parseInTableDescriptions() {
        discard(COLON);
        beginTree();
        if (!match(IDENTIFIER)) {
            discardOrError(LEFT_PAREN);
            while (match(IDENTIFIER)) {
                loopCheck();

                discard(COMMA);
            }
            discardOrError(RIGHT_PAREN);
        }
        endTree(IN_TABLE_DESCRIPTIONS);
    }

    private void parseInteger() {
        if (!matchAnyOf(OCT_INT, HEX_INT, DEC_INT)) unexpectedToken();
    }

    private void parseInValues(boolean in) {
        beginTree();

        discardOrError(IN);
        discardOrError(LEFT_PAREN);
        while (isLiteral(getCurrent())) {
            loopCheck();

            beginTree();
            consume();
            if (discard(COLON)) match(STRING_LITERAL);
            endTree(IN_VALUE);
            discard(COMMA);
        }
        matchOrError(RIGHT_PAREN);
        endTree(in ? IN : NOT_IN);
    }

    private void parseNotNull() {
        beginTree();
        discard();
        discardOrError(NULL);
        endTree(NOT_NULL);
    }

    private void parseNotOptions() {
        if (next(NULL)) parseNotNull();
        else if (next(IN)) {
            discardOrError(NOT);
            parseIn(false);
        }
        else System.out.println("oops ");
    }

    @SuppressWarnings("OverlyLongMethod")
    private void parseOption() {
        switch (getCurrent()) {
        case DEFAULT:
            parseDefault();
            break;
        case DESCRIPTION:
            parseSimpleTree(DESCRIPTION, STRING_LITERAL);
            break;
        case NOT:
            parseNotOptions();
            break;
        case CHECK:
            if (next(DIGIT)) {
                discard();
                parseSimpleTree(DIGIT, STRING_LITERAL);
            }
            else parseCheck();
            break;
        case COLLATE:
            // parseCollate();
            break;
        case MASK:
            if (current(MASK)) {
                beginTree();
                discard(MASK);
                if (match(DOLLAR)) matchOrError(IDENTIFIER);
                else if (!match(STRING_LITERAL)) unexpectedToken();
                endTree(MASK);
            }
            break;
        case PRIMARY:
            match(PRIMARY);
            discardOrError(KEY);
            break;
        case IN:
            parseIn(true);
            break;
        case BETWEEN:
            parseBetween(true);
            break;
        case LT:
        case LE:
        case GE:
        case GT:
        case EQ:
        case NE:
        case EQ_EQ:
        case LIKE:
            parseRelationalOp();
            break;

        default:
            unexpectedAndAdvanceTo(COMMA, RIGHT_BRACE);
            break;
        }
    }  // end method parseOption

    private void parseParameters() {
        discard(LEFT_PAREN);
        parseInteger();
        while (current(COMMA)) {
            loopCheck();

            discard(COMMA);
            parseInteger();
        }
        discardOrError(RIGHT_PAREN);
    }

    private void parsePrivileges() {
        beginTree();
        while (currentAnyOf(INSERT, ADD, DELETE, UPDATE, SELECT, ALTER, USE, TEMP, MANIP, ALL)) {
            if (match(ALL)) discard(PRIVILEGES);
            else consume();
            discard(COMMA);
        }
        endTree(LIST);
    }

    private void parseRelationalOp() {
        final SchemaToken t = getCurrent();
        beginTree();
        consume();
        parseValue();
        endTree(t);
    }

    private void parseSchema() {
        beginTree();
        discard(CREATE);
        discard(SCHEMA);
        matchOrError(IDENTIFIER);
        parseSimpleTree(DESCRIPTION, STRING_LITERAL);
        parseSimpleTree(LANGUAGE, STRING_LITERAL);
        discardOrError(SEMICOLON);
        endTree(SCHEMA);
    }

    private void parseStatement() {
        switch (getCurrent()) {
        case SCHEMA:
            parseSchema();
            break;
        case TABLE:
            parseTable();
            break;
        case GRANT:
            parseGrant();
            break;
        case CREATE:
            if (next(SCHEMA)) parseSchema();
            else if (next(TABLE)) parseTable();
            break;
        default:
            unexpectedAndAdvanceTo(SEMICOLON);
            discard(SEMICOLON);
            break;
        }
    }

    private void parseTable() {
        beginTree();
        discard(CREATE);
        discard(TABLE);
        matchOrError(IDENTIFIER);
        parseSimpleTree(DESCRIPTION, STRING_LITERAL);
        parseFields();
        parseIndices();
        discardOrError(SEMICOLON);
        endTree(TABLE);
    }

    private void parseType() {
        if (current(LEFT_BRACKET)) {
            discard();
            advanceTo(RIGHT_BRACKET);
            discard();
        }

        if (currentAnyOf(NUMERIC, CHARACTER, DATE, TIME, BOOLEAN, FLOAT, DOUBLE, INTEGER)) {
            final SchemaToken type = getCurrent();
            beginTree();
            discard(type);
            if (current(LEFT_PAREN)) parseParameters();
            endTree(type);
        }
        else unexpectedAndAdvanceTo(COMMA, RIGHT_BRACE);
    }

    private void parseValue() {
        if (isLiteral(getCurrent())) consume();
        else if (current(MINUS) && numberLiterals.contains(lookAhead(1))) {
            // todo pack
            consume();
            consume();
        }
        else unexpectedToken();
    }

    private boolean isLiteral(final SchemaToken token) {
        return literals.contains(token);
    }

    //~ Static Fields ................................................................................................................................

    private static final EnumSet<SchemaToken> numberLiterals = EnumSet.of(OCT_INT,
            HEX_INT,
            DEC_INT,
            OCT_LONG,
            HEX_LONG,
            DEC_LONG,
            FIXED_POINT_DECIMAL,
            FLOAT_LITERAL,
            DOUBLE_LITERAL);
    private static final EnumSet<SchemaToken> literals       = EnumSet.of(NULL, STRING_LITERAL, TRUE, FALSE, TODAY, HOUR);

    static {
        literals.addAll(numberLiterals);
    }
}  // end class SchemaParser
