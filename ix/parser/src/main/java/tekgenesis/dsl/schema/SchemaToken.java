
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.dsl.schema;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.common.util.JavaReservedWords;
import tekgenesis.lexer.Lexer;
import tekgenesis.lexer.TokenKind;
import tekgenesis.lexer.TokenType;
import tekgenesis.lexer.Tokens;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.lexer.TokenKind.*;

/**
 * Token for the Schema DSL.
 */
public enum SchemaToken implements TokenType<SchemaToken> {

    //~ Enum constants ...............................................................................................................................

    // EMPTY Imaginary token
    EOF_TOKEN(EMPTY),

    // Illegal Character

    BAD_CHAR(TokenKind.BAD_CHAR),

    // WhiteSpace

    WHITE_SPACE(TokenKind.WHITE_SPACE),

    // Comments
    COMMENT(TokenKind.COMMENT), LINE_COMMENT(TokenKind.COMMENT),

    // Documentation
    DOCUMENTATION(TokenKind.DOCUMENTATION),

    // Numbers
    HEX_INT(NUMBER), HEX_LONG(NUMBER), OCT_INT(NUMBER), OCT_LONG(NUMBER), DEC_INT(NUMBER), DEC_LONG(NUMBER), FIXED_POINT_DECIMAL(NUMBER),
    FLOAT_LITERAL(NUMBER, JavaReservedWords.FLOAT), DOUBLE_LITERAL(NUMBER, Constants.DOUBLE),

    // String
    STRING_LITERAL(TokenKind.STRING),

    // Identifier

    IDENTIFIER(TokenKind.IDENTIFIER),

    // Operators

    PLUS('+'), MINUS('-'), SLASH('/'), ASTERISK('*'), PERCENTAGE('%'), EQ('='), GT('>'), LT('<'), AMPERSAND('&'), BAR('|'), COLON(':'),
    SEMICOLON(';'), DOT('.'), COMMA(','), LEFT_BRACE('{'), RIGHT_BRACE('}'), LEFT_BRACKET('['), RIGHT_BRACKET(']'), LEFT_PAREN('('), RIGHT_PAREN(')'),
    QUESTION('?'),

    EQ_EQ('=', '='), NE('!', '='), GE('>', '='), LE('<', '='), BAR_BAR('|', '|'), DOLLAR('$'),

    // Reserved Words
    ADD(KEYWORD), ALL(KEYWORD), ALTER(KEYWORD), AND(KEYWORD), ASCENDING(KEYWORD), ASC(ASCENDING), BETWEEN(KEYWORD), BIGINT(KEYWORD), BINARY(KEYWORD),
    BIT(KEYWORD), BLOB(KEYWORD), BOOLEAN(KEYWORD), BOOL(BOOLEAN), BY(KEYWORD), CHARACTER(KEYWORD), CHAR(CHARACTER), CHECK(KEYWORD), COLLATE(KEYWORD),
    CREATE(KEYWORD), DATE(KEYWORD), DECIMAL(KEYWORD), DEC(DECIMAL), DEFAULT(KEYWORD), DELETE(KEYWORD), DESCENDING(KEYWORD), DESC(DESCENDING),
    DESCRIPTION(KEYWORD), DESCR(DESCRIPTION), DIGIT(KEYWORD), DOUBLE(KEYWORD), FALSE(KEYWORD), FLOAT(KEYWORD), GRANT(KEYWORD), GROUP(KEYWORD),
    HOUR(KEYWORD), IN(KEYWORD), INDEX(KEYWORD), INSERT(KEYWORD), INTEGER(KEYWORD), INT(INTEGER), IS(KEYWORD), KEY(KEYWORD), LANGUAGE(KEYWORD),
    LIKE(KEYWORD), MANIP(KEYWORD), MASK(KEYWORD), NOT(KEYWORD), NULL(KEYWORD), NUMERIC(KEYWORD), NUM(NUMERIC), ON(KEYWORD), OR(KEYWORD),
    OPTION(KEYWORD), PRECISION(KEYWORD), PRIMARY(KEYWORD), PRIVILEGES(KEYWORD), PUBLIC(KEYWORD), REAL(KEYWORD), SCHEMA(KEYWORD), SELECT(KEYWORD),
    SMALLINT(KEYWORD), STRING(KEYWORD), STR(STRING), TABLE(KEYWORD), TEMPORARY(KEYWORD), TEMP(TEMPORARY), TEXT(KEYWORD), TIME(KEYWORD),
    TINYINT(KEYWORD), TO(KEYWORD), TODAY(KEYWORD), TRUE(KEYWORD), UNIQUE(KEYWORD), UPDATE(KEYWORD), USE(KEYWORD), VARCHAR(KEYWORD), VARYING(KEYWORD),
    WITH(KEYWORD),

    // Imaginary tokens --> Node labels
    LIST(NODE), UNIT(NODE), TABLE_FIELD(NODE), TABLE_FIELDS(NODE), TABLE_INDICES(NODE), TABLE_INDEX(NODE), TYPE(NODE), FIELD_OPTIONS(NODE),
    NOT_NULL(NODE), RANGE(NODE), INDEX_FIELD(NODE), INDEX_FIELDS(NODE), EXPRESSION(NODE), NOT_BETWEEN(NODE), NOT_IN(NODE), IN_VALUE(NODE),
    IN_TABLE(NODE), NOT_IN_TABLE(NODE), IN_TABLE_KEYS(NODE), IN_TABLE_DESCRIPTIONS(NODE), FILE(NODE);

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final TokenKind   kind;
    @NotNull private final SchemaToken target;

    @NotNull private final String text;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("WeakerAccess")
    SchemaToken(@NotNull SchemaToken target) {
        this(target.kind, "", target);
    }

    @SuppressWarnings("WeakerAccess")
    SchemaToken(@NotNull TokenKind kind) {
        this(kind, "", null);
    }

    @SuppressWarnings("WeakerAccess")
    SchemaToken(char... cs) {
        this(OPERATOR, new String(cs), null);
    }

    @SuppressWarnings("WeakerAccess")
    SchemaToken(@NotNull TokenKind kind, @NotNull String text) {
        this(kind, text, null);
    }

    @SuppressWarnings("WeakerAccess")
    SchemaToken(@NotNull TokenKind kind, String text, @Nullable SchemaToken target) {
        this.kind   = kind;
        this.text   = isNotEmpty(text) ? text : kind == KEYWORD ? name().toLowerCase() : name();
        this.target = notNull(target, this);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public SchemaToken target() {
        return target;
    }

    @NotNull @Override public String getDescription(String s) {
        return kind.description(isNotEmpty(s) ? s : text);
    }

    @Override public boolean isIgnorable() {
        return getKind().isIgnorable() || kind == TokenKind.DOCUMENTATION;
    }

    @Override public boolean isWhiteSpace() {
        return kind.isWhiteSpace() || kind == TokenKind.DOCUMENTATION;
    }

    @NotNull @Override public TokenKind getKind() {
        return kind;
    }
    @NotNull @Override public String getText() {
        return text;
    }

    @Override public boolean isEmpty() {
        return this == EOF_TOKEN;
    }

    //~ Methods ......................................................................................................................................

    /** Creates a lexer for this set of tokens. */
    public static Lexer<SchemaToken> lexer() {
        return tokens.lexer();
    }

    /** Returns the tokens object associated to this Token Class. */
    public static Tokens<SchemaToken> tokens() {
        return tokens;
    }

    //~ Static Fields ................................................................................................................................

    private static final Tokens<SchemaToken> tokens = new Tokens<SchemaToken>(SchemaToken.class) {
            @Override public SchemaToken getHexInt() {
                return HEX_INT;
            }
            @Override public SchemaToken getHexLong() {
                return HEX_LONG;
            }
            @Override public SchemaToken getOctInt() {
                return OCT_INT;
            }
            @Override public SchemaToken getOctLong() {
                return OCT_LONG;
            }
            @NotNull @Override public SchemaToken getDecInt() {
                return DEC_INT;
            }
            @Override public SchemaToken getDecLong() {
                return DEC_LONG;
            }
            @Override public SchemaToken getFixedDecimal() {
                return FIXED_POINT_DECIMAL;
            }
            @Override public SchemaToken getFloat() {
                return FLOAT_LITERAL;
            }
            @NotNull @Override public SchemaToken getDouble() {
                return DOUBLE_LITERAL;
            }
            @Override public SchemaToken getLineComment() {
                return LINE_COMMENT;
            }
        };
}
