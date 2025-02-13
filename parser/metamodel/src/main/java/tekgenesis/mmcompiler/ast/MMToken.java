
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.ast;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.util.JavaReservedWords;
import tekgenesis.expr.BinaryExpression;
import tekgenesis.expr.UnaryExpression;
import tekgenesis.field.FieldOption;
import tekgenesis.lexer.Lexer;
import tekgenesis.lexer.TokenKind;
import tekgenesis.lexer.TokenKindType;
import tekgenesis.lexer.TokenType;
import tekgenesis.lexer.Tokens;
import tekgenesis.parser.Highlight;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Maps.enumMap;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.lexer.TokenKind.*;
import static tekgenesis.parser.Highlight.*;

/**
 * Token for the Entity and Forms DSL.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public enum MMToken implements TokenType<MMToken> {

    //~ Enum constants ...............................................................................................................................

    // IGNORABLE tokens used in ParserAdapter
    IGNORABLE(TokenKind.IGNORABLE),

    // EMPTY Imaginary token
    EMPTY_TOKEN(EMPTY),

    // Illegal Character

    BAD_CHAR(TokenKind.BAD_CHAR),

    // WhiteSpace

    WHITE_SPACE(TokenKind.WHITE_SPACE),

    // Comments
    COMMENT(TokenKind.COMMENT), LINE_COMMENT(TokenKind.COMMENT),

    // Documentation
    DOCUMENTATION(TokenKind.DOCUMENTATION),

    // Numbers
    HEX_INT(NUMBER), DEC_INT(NUMBER), FIXED_POINT_DECIMAL(NUMBER), DOUBLE_LITERAL(NUMBER, JavaReservedWords.FLOAT),

    // String
    STRING_LITERAL(TokenKind.STRING),

    // Identifier

    IDENTIFIER(TokenKind.IDENTIFIER),

    // Operators

    PLUS('+'), MINUS('-'), SLASH('/'), ASTERISK('*'), EQ('='), GT('>'), LT('<'), COLON(':'), SEMICOLON(';'), DOT('.'), COMMA(','), LEFT_BRACE('{'),
    RIGHT_BRACE('}'), LEFT_BRACKET('['), RIGHT_BRACKET(']'), LEFT_PAREN('('), RIGHT_PAREN(')'), QUESTION('?'), DOLLAR('$'), OR('|', '|'),
    AND('&', '&'), NOT('!'), IF('?'), FORBIDDEN(OPERATOR, "forbidden"), IS_UPDATE(OPERATOR, "isUpdate"), IS_READ_ONLY(OPERATOR, "isReadOnly"),

    EQ_EQ('=', '='), NE('!', '='), GE('>', '='), LE('<', '='),

    // Reserved Words

    // Reserved Words <= JavaReservedWords
    PACKAGE(KEYWORD), IMPORT(KEYWORD), ENUM(KEYWORD), NULL(KEYWORD), TRUE(KEYWORD), FALSE(KEYWORD), DEFAULT(KEYWORD), PROTECTED(KEYWORD),
    EXTENDS(KEYWORD), IMPLEMENTS(KEYWORD),

    // Contextual Keywords (Not reserved)
    FORM(CTX_KEYWORD), WIDGET(CTX_KEYWORD), ENTITY(CTX_KEYWORD), OF(CTX_KEYWORD), CASE(CTX_KEYWORD), DESCRIBED_BY(CTX_KEYWORD), IMAGE(CTX_KEYWORD),
    SEARCHABLE(CTX_KEYWORD), FILTER(CTX_KEYWORD), INDEX(CTX_KEYWORD), UNIQUE(CTX_KEYWORD), PRIMARY_KEY(CTX_KEYWORD), DATABASE(CTX_KEYWORD),
    PARAMETERS(CTX_KEYWORD), PERMISSIONS(CTX_KEYWORD), ERROR(CTX_KEYWORD), WARNING(CTX_KEYWORD), INFO(CTX_KEYWORD), INLINE(CTX_KEYWORD),
    POPUP(CTX_KEYWORD), WHEN(CTX_KEYWORD), USING(CTX_KEYWORD), BY(CTX_KEYWORD), TYPE(CTX_KEYWORD), ON_LOAD(CTX_KEYWORD), ON_DISPLAY(CTX_KEYWORD),
    ON_ROUTE(CTX_KEYWORD), ON_CANCEL(CTX_KEYWORD), ON_SCHEDULE(CTX_KEYWORD), WITH(CTX_KEYWORD), LISTING(CTX_KEYWORD), HANDLER(CTX_KEYWORD),
    ROLE(CTX_KEYWORD), MENU(CTX_KEYWORD), LINK(CTX_KEYWORD), SCHEMA(CTX_KEYWORD), DEPRECABLE(CTX_KEYWORD), AUDITABLE(CTX_KEYWORD),
    REMOTABLE(CTX_KEYWORD), OPTIMISTIC(CTX_KEYWORD), BATCH_SIZE(CTX_KEYWORD), VIEW(CTX_KEYWORD), UPDATABLE(CTX_KEYWORD), CACHE(CTX_KEYWORD),
    TABLE(CTX_KEYWORD), ALL(CTX_KEYWORD), ISOLATED(CTX_KEYWORD), TASK(CTX_KEYWORD), SCHEDULE(CTX_KEYWORD), PATTERN(CTX_KEYWORD),
    TRANSACTION(CTX_KEYWORD), NODE(CTX_KEYWORD), CLUSTER(CTX_KEYWORD), EXCLUSION_GROUP(CTX_KEYWORD), LIFECYCLE(CTX_KEYWORD), PROCESSOR(CTX_KEYWORD),
    RUNNABLE(CTX_KEYWORD), IMPORTER(CTX_KEYWORD), EACH(CTX_KEYWORD), NONE(CTX_KEYWORD), DEPENDS_ON(CTX_KEYWORD), SERVER(CTX_KEYWORD),
    CLIENT(CTX_KEYWORD), AS(CTX_KEYWORD), BODY(CTX_KEYWORD), UNRESTRICTED(CTX_KEYWORD), SECURE_BY(CTX_KEYWORD), RAISE(CTX_KEYWORD),
    INTERNAL(CTX_KEYWORD), NOTIFY(CTX_KEYWORD), PROJECT(CTX_KEYWORD), AFTER(CTX_KEYWORD),
    // Literals

    // Imaginary tokens --> TokenKind.NODE labels
    LABELED_ID(TokenKind.NODE), ASSIGNMENT_LIST(TokenKind.NODE), INNER_ASSIGNMENT_LIST(TokenKind.NODE), LIST(TokenKind.NODE),
    ARG_LIST(TokenKind.NODE), FIELD(TokenKind.NODE), ENUM_FIELD(TokenKind.NODE), ASSIGNMENT(TokenKind.NODE), ASSIGNMENT_FIELD(TokenKind.NODE),
    AGGREGATE(TokenKind.NODE), ENUM_VALUE(TokenKind.NODE), FILE(TokenKind.NODE), FIELD_REF(TokenKind.NODE.withHighlight(FIELD_REF_H)),
    FILTER_REF(TokenKind.NODE.withHighlight(FIELD_REF_H)), PERMISSION_REF(TokenKind.NODE.withHighlight(REFERENCE_H)), WIDGET_FIELD(TokenKind.NODE),
    INVOKE(TokenKind.NODE), UNARY_MINUS(TokenKind.NODE), FILE_TYPE(TokenKind.NODE, true), CHECK_TYPE(TokenKind.NODE, true),
    BUTTON_TYPE(TokenKind.NODE, true), TOGGLE_BUTTON_TYPE(TokenKind.NODE, true), TAB_TYPE(TokenKind.NODE, true), POPOVER_TYPE(TokenKind.NODE, true),
    MAIL_VALIDATION_TYPE(TokenKind.NODE, true), RATING_TYPE(TokenKind.NODE, true), MAP_TYPE(TokenKind.NODE, true), DATE_TYPE(TokenKind.NODE, true),
    MASK(TokenKind.NODE, true), QUERY_MODE(TokenKind.NODE, true), ICON(TokenKind.NODE, true), EXPORT_TYPE(TokenKind.NODE, true),
    HTTP_METHOD(TokenKind.NODE, true), MENU_ELEMENT(TokenKind.NODE), ROLE_ELEMENT(TokenKind.NODE), ROLE_PERMISSION(TokenKind.NODE),
    ROUTE(TokenKind.NODE), TASK_PHASE(TokenKind.NODE), PATH(TokenKind.NODE), PART(TokenKind.NODE), CLASS(TokenKind.NODE),
    INTERPOLATION(TokenKind.NODE),

    // TokenKind.NODEs with Special Highlight
    REFERENCE(TokenKind.NODE.withHighlight(REFERENCE_H)), TYPE_REF(TokenKind.NODE), ENTITY_REF(TokenKind.NODE.withHighlight(KEYWORD_H)),
    TYPE_NODE(TokenKind.NODE), WIDGET_TYPE(TokenKind.NODE.withHighlight(WIDGET_H)), MODIFIERS(TokenKind.NODE.withHighlight(KEYWORD_H)),
    OPTION(TokenKind.NODE.withHighlight(OPTION_H)), CHECK(TokenKind.NODE.withHighlight(KEYWORD_H)), MODEL(TokenKind.NODE.withHighlight(KEYWORD_H)),
    ENUM_REF(TokenKind.NODE.withHighlight(KEYWORD_H)), ENUM_FIELD_REF(TokenKind.NODE.withHighlight(REFERENCE_H)), STRUCT_REF(TokenKind.NODE),
    FORM_REF(TokenKind.NODE, true), WIDGET_DEF_REF(TokenKind.NODE, true), METHOD_REF(TokenKind.NODE.withHighlight(REFERENCE_H), true),
    DATAOBJECT_REF(TokenKind.NODE.withHighlight(KEYWORD_H)), TASK_REF(TokenKind.NODE.withHighlight(REFERENCE_H)),
    PARAMETER(TokenKind.NODE.withHighlight(REFERENCE_H)),

    // Fake TokenKind.NODEs for completion
    MODEL_OPTIONS(TokenKind.NODE), AFTER_SEARCHABLE(TokenKind.NODE), TASK_TYPE(TokenKind.NODE);

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Highlight highlight;
    @NotNull private final TokenKind kind;
    @NotNull private final String    text;
    private final boolean            wrapper;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("WeakerAccess")  // Clashes with 'private' in enum constructor is redundant.
    MMToken(@NotNull TokenKindType kind) {
        this(kind, "", false);
    }

    @SuppressWarnings("WeakerAccess")  // Clashes with 'private' in enum constructor is redundant.
    MMToken(char... cs) {
        this(OPERATOR, new String(cs), false);
    }

    @SuppressWarnings("WeakerAccess")  // Clashes with 'private' in enum constructor is redundant.
    MMToken(@NotNull TokenKindType kind, boolean wrapper) {
        this(kind, "", wrapper);
    }

    @SuppressWarnings("WeakerAccess")  // Clashes with 'private' in enum constructor is redundant.
    MMToken(@NotNull TokenKindType kind, String text) {
        this(kind, text, false);
    }

    @SuppressWarnings("WeakerAccess")  // Clashes with 'private' in enum constructor is redundant.
    MMToken(@NotNull TokenKindType kind, String text, boolean wrapper) {
        this.kind    = kind.getTokenKind();
        this.text    = isNotEmpty(text) ? text : kind == KEYWORD || kind == CTX_KEYWORD ? name().toLowerCase() : name();
        highlight    = kind.getHighlight();
        this.wrapper = wrapper;
    }

    //~ Methods ......................................................................................................................................

    /**
     * For Tokens that represent Binary Operations return the {@link BinaryExpression.Operator } it
     * will return null otherwise.
     */
    @Nullable public BinaryExpression.Operator binaryOperator() {
        return binaryOp.get(this);
    }

    @NotNull @Override public MMToken target() {
        return this;
    }
    /**
     * For Tokens that represent Unary Operations return the {@link UnaryExpression.Operator } it
     * will return null otherwise.
     */
    @Nullable public UnaryExpression.Operator unaryOperator() {
        return unaryOp.get(this);
    }

    /** Returns true if the item is a Keyword. */
    public boolean isKeyword() {
        return kind == KEYWORD;
    }

    @NotNull @Override public String getDescription(String s) {
        return kind.description(isNotEmpty(s) ? s : text);
    }

    @Override public boolean isIgnorable() {
        return getKind().isIgnorable();
    }

    @Override public boolean isWhiteSpace() {
        return kind.isWhiteSpace();
    }

    /** Return Highlight for token. */
    @NotNull public Highlight getHighlight() {
        return highlight;
    }

    @NotNull @Override public TokenKind getKind() {
        return kind;
    }

    /** Returns true if the token is considered a LITERAL (A constant). */
    public boolean isLiteral() {
        return kind == NUMBER || LITERALS.contains(this);
    }

    /** Returns true if node is a wrapper. */
    public boolean isWrapper() {
        return wrapper;
    }

    @NotNull @Override public String getText() {
        return text;
    }

    @Override public boolean isEmpty() {
        return this == EMPTY_TOKEN;
    }

    //~ Methods ......................................................................................................................................

    /** Creates a lexer for this set of tokens. */
    public static Lexer<MMToken> lexer() {
        return tokens.lexer();
    }

    /** Return an specific MMToken for a given option or null. */
    @NotNull public static Option<MMToken> nodeFor(@NotNull final FieldOption option) {
        return ofNullable(nodeForOption.get(option));
    }

    /** Returns the tokens object associated to this Token Class. */
    public static Tokens<MMToken> tokens() {
        return tokens;
    }

    //J-

    private static final Tokens<MMToken> tokens =
        new Tokens<MMToken>(MMToken.class) {
            @Override          public MMToken getHexInt()          { return HEX_INT; }
            @NotNull @Override public MMToken getDecInt()          { return DEC_INT; }
            @Override          public MMToken getFixedDecimal()    { return FIXED_POINT_DECIMAL; }
            @NotNull @Override public MMToken getDouble()          { return DOUBLE_LITERAL; }
            @Override          public MMToken getLineComment()     { return LINE_COMMENT; }
            //
            // Even if an identifier is not a reserved word for MM but it is for Java reduce it to the reserved word null
            // This is to avoid problems when interacting with Java
            //
            @NotNull @Override public MMToken findKeyword(@NotNull String name) {
                final MMToken value = keywordMap.get(name);
                return value != null ? value : JavaReservedWords.isReserved(name) ? NULL : IDENTIFIER;
            }
        };

    private static final Map<FieldOption, MMToken> nodeForOption =
            enumMap(tuple(FieldOption.SUBFORM_ID, FORM_REF),
                    tuple(FieldOption.ON_NEW_FORM, FORM_REF),
                    tuple(FieldOption.LINK_FORM, FORM_REF),
                    tuple(FieldOption.FORM, FORM_REF),
                    tuple(FieldOption.WIDGET_DEF, WIDGET_DEF_REF),
                    tuple(FieldOption.TASK, TASK_REF),
                    tuple(FieldOption.ON_NEW, METHOD_REF),
                    tuple(FieldOption.ON_SUGGEST, METHOD_REF),
                    tuple(FieldOption.ON_SUGGEST_SYNC, METHOD_REF),
                    tuple(FieldOption.ON_CLICK, METHOD_REF),
                    tuple(FieldOption.ON_SELECTION, METHOD_REF),
                    tuple(FieldOption.ON_CHANGE, METHOD_REF),
                    tuple(FieldOption.ON_BLUR, METHOD_REF),
                    tuple(FieldOption.ON_NEW_LOCATION, METHOD_REF),
                    tuple(FieldOption.ON_UI_CHANGE, METHOD_REF),
                    tuple(FieldOption.ON_LOAD, METHOD_REF),
                    tuple(FieldOption.ON_DISPLAY, METHOD_REF),
                    tuple(FieldOption.ON_INIT, METHOD_REF),
                    tuple(FieldOption.ON_STOP, METHOD_REF),
                    tuple(FieldOption.ON_CANCEL, METHOD_REF),
                    tuple(FieldOption.ON_SCHEDULE, METHOD_REF),
                    tuple(FieldOption.HANDLER, HANDLER),
                    tuple(FieldOption.FILE_TYPE, FILE_TYPE),
                    tuple(FieldOption.CHECK_TYPE, CHECK_TYPE),
                    tuple(FieldOption.MASK, MASK),
                    tuple(FieldOption.ICON, ICON),
                    tuple(FieldOption.QUERY_MODE, QUERY_MODE),
                    tuple(FieldOption.EXPORT_TYPE, EXPORT_TYPE),
                    tuple(FieldOption.ICON_SELECTED, ICON),
                    tuple(FieldOption.METHOD,HTTP_METHOD),
                    tuple(FieldOption.BUTTON_TYPE, BUTTON_TYPE),
                    tuple(FieldOption.TOGGLE_BUTTON_TYPE, TOGGLE_BUTTON_TYPE),
                    tuple(FieldOption.DATE_TYPE, DATE_TYPE),
                    tuple(FieldOption.TAB_TYPE, TAB_TYPE),
                    tuple(FieldOption.POPOVER_TYPE, POPOVER_TYPE),
                    tuple(FieldOption.MAIL_VALIDATION_TYPE, MAIL_VALIDATION_TYPE),
                    tuple(FieldOption.RATING_TYPE, RATING_TYPE),
                    tuple(FieldOption.MAP_TYPE, MAP_TYPE));

    private static final EnumMap<MMToken, BinaryExpression.Operator> binaryOp =
            enumMap(tuple(OR, BinaryExpression.Operator.OR),
                    tuple(AND, BinaryExpression.Operator.AND),
                    tuple(EQ_EQ, BinaryExpression.Operator.EQ),
                    tuple(NE, BinaryExpression.Operator.NE),
                    tuple(GE, BinaryExpression.Operator.GE),
                    tuple(GT, BinaryExpression.Operator.GT),
                    tuple(LE, BinaryExpression.Operator.LE),
                    tuple(LT, BinaryExpression.Operator.LT),
                    tuple(PLUS, BinaryExpression.Operator.ADD),
                    tuple(MINUS, BinaryExpression.Operator.SUB),
                    tuple(ASTERISK, BinaryExpression.Operator.MUL),
                    tuple(SLASH, BinaryExpression.Operator.DIV));

    private static final EnumMap<MMToken, UnaryExpression.Operator> unaryOp =
            enumMap(tuple(NOT, UnaryExpression.Operator.NOT),
                    tuple(UNARY_MINUS, UnaryExpression.Operator.MINUS),
                    tuple(PLUS, UnaryExpression.Operator.PLUS));

    private static final EnumSet<MMToken> LITERALS  = EnumSet.of(NULL, STRING_LITERAL, TRUE, FALSE);

    //J+
}
