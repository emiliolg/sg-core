
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.parser;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.field.FieldOption;
import tekgenesis.lexer.TokenKind;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.mmcompiler.parser.TypeParser.TypeFieldParser;
import tekgenesis.parser.ASTBuilder;
import tekgenesis.parser.AbstractParser;
import tekgenesis.parser.LoopException;
import tekgenesis.parser.Parser;
import tekgenesis.parser.ParserErrorListener;
import tekgenesis.type.MetaModelKind;
import tekgenesis.type.Modifier;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.expr.ExpressionFactory.isInterpolation;
import static tekgenesis.field.FieldOption.ON_SUGGEST;
import static tekgenesis.mmcompiler.ast.MMToken.*;
import static tekgenesis.parser.ParserCommonMessages.MSGS;

/**
 * Parser for the Entity Definition Language.
 */

@SuppressWarnings({ "ClassWithTooManyMethods", "OverlyComplexClass" })
public class MetaModelParser extends AbstractParser<MMToken> {

    //~ Constructors .................................................................................................................................

    MetaModelParser(MetaModelParser parent) {
        this(parent.getAstBuilder(), parent.getErrorListener());
    }

    /** Creates an Entity Parser. */
    public MetaModelParser(@NotNull ASTBuilder<MMToken> builder, ParserErrorListener listener) {
        super(builder, listener);
    }

    //~ Methods ......................................................................................................................................

    @Override public void parse() {
        try {
            parsePackage();
            parseImports();
            parseDefinitions();
        }
        catch (final LoopException l) {
            advanceToEof();
            dropAllTrees();
            error(l.getPosition(), MSGS.loop());
        }
    }

    /** Parses a qualified id as a reference tree with identifier leafs. */
    public String parseQualifiedId() {
        beginTree();
        return parseQualifiedId(new StringBuilder()).toString();
    }

    /** Match an ID, return the text value. */
    String matchIdOrError() {
        if (!currentOrError(IDENTIFIER)) return "";

        final String result = getCurrentText();
        consume();
        return result;
    }

    /** Parse an ExpressionAST. */
    void parseExpression() {
        ExpressionType.IF.parseExpression(this);
    }

    void parseExpressionElement() {
        final MMToken current = getCurrent();
        switch (current) {
        case IDENTIFIER:
            if (next(LEFT_PAREN)) {
                if (currentByText(FORBIDDEN)) parseForbidden();
                else if (currentByText(IS_UPDATE)) parseBooleanFunction(IS_UPDATE);
                else if (currentByText(IS_READ_ONLY)) parseBooleanFunction(IS_READ_ONLY);
                else parseInvoke();
            }
            else parseQualifiedId(FIELD_REF, false);
            break;
        case LEFT_PAREN:
            discard();
            parseExpression();
            discardOrError(RIGHT_PAREN);
            break;
        case STRING_LITERAL:
            if (isInterpolation(getCurrentText())) {
                beginTree();
                matchOrError(STRING_LITERAL);
                endTree(INTERPOLATION);
            }
            else consume();
            break;
        default:
            if (current.isLiteral()) consume();
            else unexpectedError();
            break;
        }
    }

    void parseFieldDocumentation() {
        if (!current(MMToken.DOCUMENTATION)) return;
        beginTree();
        consume();
        endTree(MMToken.DOCUMENTATION);
    }

    void parseFieldOptions(@NotNull MetaModelKind mmKind, boolean commaConsumed) {
        if (!commaConsumed && !discard(COMMA)) return;

        do {
            if (!parseFieldOption(mmKind)) unexpectedAndAdvanceTo(COMMA, SEMICOLON, RIGHT_BRACE);
        }
        while (discard(COMMA));
    }

    @SuppressWarnings({ "OverlyLongMethod", "OverlyComplexMethod" })
    void parseFieldOptionValue(@NotNull FieldOption option) {
        switch (option.getType()) {
        case CHECK_T:
            parseOptionalList(LIST, LEFT_PAREN, COMMA, RIGHT_PAREN, this::parseCheckElement);
            break;
        case LABELED_IDS_T:
            parseOptionalList(LIST, LEFT_PAREN, COMMA, RIGHT_PAREN, () -> parseLabeledId(false));
            break;
        case STRING_T:
            matchOrError(STRING_LITERAL);
            break;
        case IDENTIFIERS_T:
            parseIdentifierList();
            break;
        case IDENTIFIER_T:
            matchOrError(IDENTIFIER);
            break;
        case METHOD_T:
            if (option == ON_SUGGEST) parseInvoke(true);
            else {
                beginTree();
                matchOrError(IDENTIFIER);
                if (currentByText(WHEN)) {
                    discard();
                    parseExpression();
                }
                endTree(METHOD_REF);
            }

            break;
        case METAMODEL_REFERENCE_T:
            parseQualifiedId();
            break;
        case BOOLEAN_T:
            break;
        case STRING_EXPR_T:
        case UNSIGNED_EXPR_T:
        case VALUE_EXPR_T:
        case VALUE_ARRAY_EXPR_T:
        case GENERIC_EXPR_T:
        case STRING_ARRAY_EXPR_T:
            parseExpression();
            break;
        case STRING_EXPRS_T:
            if (!parseList(LIST, LEFT_PAREN, COMMA, RIGHT_PAREN, this::parseExpression)) parseExpression();
            break;
        case ASSIGNMENT_EXPRS:
            if (currentOrError(LEFT_PAREN)) parseList(ASSIGNMENT_LIST, LEFT_PAREN, COMMA, RIGHT_PAREN, this::parseAssignmentExpression);
            break;
        case BOOLEAN_EXPR_T:
            if (currentByText(WHEN)) {
                discard();
                parseExpression();
            }
            break;
        case UNSIGNED_T:
            parseInteger();
            break;
        case ENUM_T:
            matchOrError(IDENTIFIER);
            break;
        case TYPE_T:
            parseType(true);
            break;
        case FIELDS_T:
            parseList(LIST, LEFT_BRACE, null, RIGHT_BRACE, new TypeFieldParser(this));
            break;
        case AGGREGATE_T:
        case METHOD_REF_T:
            throw unreachable();
        }
    }  // end method parseFieldOptionValue

    void parseId(MMToken token) {
        parseId(token, false);
    }

    void parseId(MMToken token, boolean discardCurrent) {
        beginTree();
        if (discardCurrent) discard();
        matchIdOrError();
        endTree(token);
    }

    void parseLabeledId(boolean optionalId) {
        beginTree();
        final boolean notMatch = !match(IDENTIFIER);
        if (notMatch && !optionalId) {
            currentOrError(IDENTIFIER);
            discard();
        }
        match(STRING_LITERAL);
        endTree(LABELED_ID);
    }

    boolean parseOptionalInteger() {
        return matchAnyOf(HEX_INT, DEC_INT);
    }

    void parseOptionWithField(MMToken key, MMToken... extraTokens) {
        beginTree();
        discard();
        parseRef(FIELD_REF, extraTokens);
        endTree(key);
    }

    void parseOptionWithFields(MMToken key, MMToken... extraTokens) {
        parseOptionWithFieldsExpecting(key, FIELD_REF, extraTokens);
    }

    void parseOptionWithFieldsExpecting(MMToken key, MMToken refToken, MMToken... extraTokens) {
        beginTree();
        discard();
        parseRef(refToken, extraTokens);
        while (discard(COMMA))
            parseRef(refToken, extraTokens);
        endTree(key);
    }
    void parseOptionWithoutFields(MMToken key) {
        beginTree();
        discard();
        endTree(key);
    }

    void parseOptionWithRefsExpecting(MMToken key, MMToken refToken) {
        beginTree();
        discard();
        parseQualifiedId(refToken, false);
        while (discard(COMMA))
            parseQualifiedId(refToken, false);
        endTree(key);
    }

    void parseQualifiedId(MMToken token, boolean discardCurrent) {
        beginTree();
        if (discardCurrent) discard();
        parseQualifiedId();
        endTree(token);
    }

    void parseRef(MMToken key) {
        if (currentOrError(IDENTIFIER)) {
            beginTree();
            consume();
            endTree(key);
        }
    }

    void parseRef(MMToken key, MMToken... extraTokens) {
        final MMToken[] tokens;
        if (extraTokens.length > 0) {
            tokens    = new MMToken[extraTokens.length + 1];
            tokens[0] = IDENTIFIER;
            System.arraycopy(extraTokens, 0, tokens, 1, extraTokens.length);
        }
        else tokens = new MMToken[] { IDENTIFIER };
        if (currentAnyOfOrError(tokens)) {
            beginTree();
            consume();
            endTree(key);
        }
    }

    void parseSearchable() {
        beginTree();
        discard();

        if (currentByText(BY)) {
            discard();
            if (currentByText(DATABASE)) {
                discard();
                beginTree();
                endTree(DATABASE);
            }

            if (currentOrError(LEFT_BRACE)) parseList(LIST, LEFT_BRACE, SEMICOLON, RIGHT_BRACE, new SearchableFieldParser());
        }

        endTree(SEARCHABLE);
    }

    void parseType(boolean allowReferenceCardinality) {
        parseType(allowReferenceCardinality, false, TYPE_REF);
    }

    /**
     * @param  allowReferenceCardinality  Allow cardinality on reference types (e.g.: entities,
     *                                    enums)
     * @param  allowTypeCardinality       Allow cardinality on basic types (e.g.: String, Int)
     * @param  refType                    Type of reference node
     */
    void parseType(boolean allowReferenceCardinality, boolean allowTypeCardinality, MMToken refType) {
        beginTree();

        final String typeName = parseQualifiedId();
        final Type   t        = getTypeFromString(typeName);

        final boolean reference = t.isNull();
        if (reference) {
            dupBeginTree();
            endTree(refType);
        }

        parseParameters(t.getParametersCount());

        if (reference) {
            if (allowReferenceCardinality) match(ASTERISK);
            if (currentByText(USING)) {
                discard();
                matchOrError(IDENTIFIER);
            }
        }
        else if (allowTypeCardinality) match(ASTERISK);

        endTree(TYPE_NODE);
    }

    /** Parse a basic type based on an String. May be overridden to allow type extensions. */
    @NotNull Type getTypeFromString(String typeName) {
        return Types.fromString(typeName);
    }

    /** Consumes any of the tokens or use the first token as the default. */
    private void checkType(MMToken... tokens) {
        beginTree();
        getAnyOf(tokens);
        for (final MMToken token : tokens) {
            if (currentByText(token)) {
                consume();
                break;
            }
        }
        endTree(OPTION);
    }

    @Nullable private Parser createDefinitionParser() {
        Factory factory = null;
        boolean inTree  = false;
        boolean doc     = false;
        while (!eof()) {
            if (!inTree && !doc && current(DOCUMENTATION)) {
                beginTree();

                beginTree();
                consume();
                endTree(DOCUMENTATION);

                doc = true;
                if (notValidAfterDocumentation()) error(getAstBuilder().getCurrentPosition(), MSGS.unexpectedAfterDoc(getCurrentText()));
            }
            else {
                final String txt = textFromIdOrKeyword();
                if (txt.isEmpty()) break;
                final Modifier mod = Modifier.fromId(txt);
                if (mod == null) {
                    factory = definitionParsers.get(txt);
                    break;
                }

                if (!inTree) {
                    if (!doc) beginTree();
                    beginTree();
                    inTree = true;
                }
                consume();
            }
        }
        if (factory == null) {
            if (inTree) {
                dropTree();
                dropTree();
            }
            else if (doc) dropTree();
            return null;
        }

        if (inTree) endTree(MODIFIERS);
        else if (!doc) beginTree();

        return factory.create(this);
    }

    private boolean notValidAfterDocumentation() {
        final String txt = textFromIdOrKeyword();
        return txt.isEmpty() || Modifier.fromId(txt) == null && definitionParsers.get(txt) == null;
    }

    private void parseAssignmentExpression() {
        beginTree();
        parseRef(FILTER_REF);
        if (currentAnyOfOrError(EQ, NE)) {
            final MMToken sign = getCurrent();
            discard();

            beginTree();
            if (!parseList(INNER_ASSIGNMENT_LIST, LEFT_PAREN, COMMA, RIGHT_PAREN, this::parseExpression)) parseExpression();
            endTree(sign);

            if (currentByText(WHEN)) {
                discard();
                parseExpression();
            }
        }
        endTree(ASSIGNMENT_FIELD);
    }

    /**
     * Creates the isUpdate or isReadOnly node expression. The method checks for an identifier, a
     * left parenthesis and a right parenthesis.
     */
    private void parseBooleanFunction(@NotNull MMToken func) {
        beginTree();
        discardOrError(IDENTIFIER);
        discardOrError(LEFT_PAREN);
        discardOrError(RIGHT_PAREN);
        endTree(func);
    }

    private void parseCheckElement() {
        beginTree();
        parseExpression();
        discardOrError(COLON);
        // inline or popup
        checkType(POPUP, INLINE);
        // check type
        checkType(ERROR, WARNING, INFO);
        // check msg
        matchOrError(STRING_LITERAL);
        endTree(CHECK);
    }

    private boolean parseDefinition() {
        final Parser parser = createDefinitionParser();

        if (parser == null) return false;
        beginTree();
        discard();
        endTree(MMToken.MODEL);
        parser.parse();
        return true;
    }

    private void parseDefinitions() {
        boolean recovered = true;
        while (!eof()) {
            loopCheck();
            if (parseDefinition()) recovered = true;
            else {
                if (recovered) {
                    unexpectedError();
                    recovered = false;
                }
                discard();
            }
        }
    }  // end method parseDefinitions

    private boolean parseFieldOption(MetaModelKind mmKind) {
        final FieldOption option = FieldOption.fromId(textFromIdOrKeyword(), mmKind);

        if (option == null) return false;

        beginTree();
        consume();

        final Option<MMToken> wrapper = MMToken.nodeFor(option);
        wrapper.ifPresent(w -> beginTree());
        parseFieldOptionValue(option);
        wrapper.ifPresent(this::endTree);

        endTree(OPTION);

        return true;
    }  // end method parseFieldOption

    private void parseForbidden() {
        beginTree();
        discardOrError(IDENTIFIER);  // discard 'forbidden' identifier
        discardOrError(LEFT_PAREN);
        parseRef(PERMISSION_REF);    // permission reference
        discardOrError(RIGHT_PAREN);
        endTree(FORBIDDEN);
    }

    private void parseIdentifierList() {
        if (!match(IDENTIFIER)) parseList(LIST, LEFT_PAREN, COMMA, RIGHT_PAREN, this::matchIdOrError);
    }

    private void parseImports() {
        while (current(IMPORT)) {
            beginTree();
            discard(IMPORT);
            final String fqn = parseQualifiedId();
            if (fqn.isEmpty()) advanceTo(SEMICOLON);
            discardOrError(SEMICOLON);
            endTree(IMPORT);
        }
    }

    private void parseInteger() {
        parseOptionalInteger();
    }

    private void parseInvoke() {
        parseInvoke(false);
    }

    private void parseInvoke(boolean optionalArguments) {
        beginTree();
        matchOrError(IDENTIFIER);
        if (current(LEFT_PAREN) || !optionalArguments) {
            discardOrError(LEFT_PAREN);
            if (!current(RIGHT_PAREN)) {
                parseExpression();
                while (discard(COMMA))
                    parseExpression();
            }
            discardOrError(RIGHT_PAREN);
        }
        endTree(INVOKE);
    }

    private void parsePackage() {
        if (currentOrError(PACKAGE)) {
            beginTree();
            discard(PACKAGE);
            parseQualifiedId();
            endTree(PACKAGE);
            if (currentByText(SCHEMA)) {
                beginTree();
                discard();
                matchIdOrError();
                endTree(SCHEMA);
            }
            discardOrError(SEMICOLON);
        }
    }

    private void parseParameters(int parametersCount) {
        if (current(LEFT_PAREN)) {
            final boolean hasParameters = parametersCount > 0;
            if (hasParameters) beginTree();
            else unexpectedError();

            discard();
            parseInteger();

            int n = 0;
            while (current(COMMA)) {
                if (++n == parametersCount) unexpectedError();
                discard(COMMA);
                parseInteger();
            }
            discardOrError(RIGHT_PAREN);
            if (hasParameters) endTree(LIST);
        }
    }

    private StringBuilder parseQualifiedId(@NotNull final StringBuilder text) {
        final String identifier = matchIdOrError();
        text.append(identifier);

        if (current(DOT)) dupBeginTree();
        endTree(REFERENCE);

        if (identifier.isEmpty()) return new StringBuilder();

        if (discard(DOT)) {
            text.append('.');
            parseQualifiedId(text);
        }
        return text;
    }

    private String textFromIdOrKeyword() {
        final TokenKind kind = getCurrent().getKind();
        return kind.isIdentifier() || kind.isKeyword() ? getCurrentText() : "";
    }

    //~ Methods ......................................................................................................................................

    static void registerDefinitionParser(MMToken tokenId, Factory factory) {
        definitionParsers.put(tokenId.getText(), factory);
    }

    //~ Static Fields ................................................................................................................................

    private static final Map<String, Factory> definitionParsers = new HashMap<>();

    static {
        EntityParser.register();
        HandlerParser.register();
        WidgetDefParser.register();
        InterpolationParser.register();
        EnumParser.register();
        TypeParser.register();
        FormParser.register();
        RoleParser.register();
        LinkParser.register();
        PathParser.register();
        CaseParser.register();
        MenuParser.register();
        ViewParser.register();
        // noinspection StaticInitializerReferencesSubClass
        TaskParser.register();
    }

    //~ Inner Interfaces .............................................................................................................................

    interface Factory {
        Parser create(MetaModelParser parent);
    }

    //~ Inner Classes ................................................................................................................................

    private class SearchableFieldParser implements Parser {
        @Override public void parse() {
            beginTree();

            if (ahead(COLON, 1)) {
                match(IDENTIFIER);
                discardOrError(COLON);
            }

            if (currentOrError(IDENTIFIER)) {
                parseRef(FIELD_REF);

                if (discard(COMMA)) parseFieldOptions(MetaModelKind.SEARCHABLE, true);
            }

            endTree(FIELD);
        }
    }
}  // end class MetaModelParser
