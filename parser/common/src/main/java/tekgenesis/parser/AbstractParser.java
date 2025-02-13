
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.lexer.TokenType;

import static tekgenesis.parser.ParserCommonMessages.MSGS;

/**
 * A skeleton implementation of a Parser.
 *
 * @param  <T>  The type of tokens processed by the parser
 */
@SuppressWarnings("ClassWithTooManyMethods")
public abstract class AbstractParser<T extends TokenType<T>> implements Parser {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final ASTBuilder<T>  astBuilder;
    @NotNull private ParserErrorListener  errorListener;

    private long lastPosition;

    //~ Constructors .................................................................................................................................

    protected AbstractParser(@NotNull ASTBuilder<T> astBuilder, @NotNull ParserErrorListener listener) {
        this.astBuilder = astBuilder;
        lastPosition    = -1;
        errorListener   = listener;
    }

    //~ Methods ......................................................................................................................................

    /** Starts the construction of a new tree. */
    public void beginTree() {
        astBuilder.beginNode();
    }
    /** Discard the current token. */
    public final void discard() {
        astBuilder.discard();
    }

    /** Discard the current tree under construction. */
    public void dropTree() {
        astBuilder.dropNode();
    }

    /** Duplicate current tree under construction. */
    public void dupBeginTree() {
        astBuilder.dupBeginNode();
    }

    /** Finish the construction of the current tree and push it again. */
    public void endAndPushTree(T token) {
        astBuilder.endAndPushNode(token);
    }

    /** Finish the construction of the current tree. */
    public void endTree(T token) {
        astBuilder.endNode(token);
    }

    /** Get the current token. */
    public final T getCurrent() {
        return astBuilder.getTokenType();
    }

    /** Report Parser Errors. */
    public final void setErrorListener(@NotNull ParserErrorListener listener) {
        errorListener = listener;
    }

    @SafeVarargs protected final void advanceTo(T... tokens) {
        while (!eof() && !currentAnyOf(tokens))
            astBuilder.advanceLexer();
    }
    protected void advanceToEof() {
        while (!eof())
            astBuilder.advanceLexer();
    }

    /** Check given type matches ahead token. */
    protected boolean ahead(T tokenType, final int i) {
        return astBuilder.lookAhead(i) == tokenType;
    }

    protected void checkCompletion() {
        if (isCompletion()) discard();
    }

    protected final void consume() {
        astBuilder.addChild();
        astBuilder.advanceLexer();
    }

    /** Check given type matches current token. */
    protected final boolean current(T tokenType) {
        return getCurrent() == tokenType;
    }

    @SafeVarargs protected final boolean currentAnyOf(T... tokenType) {
        if (eof()) return false;
        for (final T t : tokenType) {
            if (getCurrent() == t) return true;
        }
        return false;
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    protected final boolean currentAnyOfOrError(T... tokenType) {
        for (final T t : tokenType) {
            if (getCurrent() == t) return true;
        }
        error(MSGS.expected(MSGS.anyOf(ImmutableList.fromArray(tokenType).toString()), getCurrentDescription()));
        return false;
    }

    /** Check given type matches current token or match by text. */
    protected final boolean currentByText(T tokenType) {
        final T current = getCurrent();
        return current == tokenType || current.getKind().isIdentifier() && getCurrentText().equals(tokenType.getText());
    }

    @SafeVarargs protected final boolean currentByTextAnyOf(T... tokenType) {
        if (eof()) return false;
        for (final T t : tokenType) {
            final T current = getCurrent();
            if (current == t || current.getKind().isIdentifier() && getCurrentText().equals(t.getText())) return true;
        }
        return false;
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    protected final boolean currentByTextAnyOfOrError(T... tokenType) {
        if (currentByTextAnyOf(tokenType)) return true;

        error(MSGS.expected(MSGS.anyOf(ImmutableList.fromArray(tokenType).toString()), getCurrentDescription()));
        return false;
    }

    @SuppressWarnings("UnusedReturnValue")
    protected boolean currentByTextOrError(T tokenType) {
        if (currentByText(tokenType)) return true;

        error(MSGS.expected(tokenType.getDescription(""), getCurrentDescription()));
        // Try to sync with next token
        if (!aheadByText(tokenType, 1)) return false;
        discard();
        return true;
    }

    protected boolean currentOrError(T tokenType) {
        if (current(tokenType)) return true;

        error(MSGS.expected(tokenType.getDescription(""), getCurrentDescription()));
        // Try to sync with next token
        if (!ahead(tokenType, 1)) return false;
        discard();
        return true;
    }

    protected final boolean discard(T tokenType) {
        if (current(tokenType)) {
            discard();
            return true;
        }
        return false;
    }

    protected void discardOrError(T tokenType) {
        final boolean b = currentOrError(tokenType);
        if (b) discard();
    }

    /** Discard all pending trees under construction. */
    protected void dropAllTrees() {
        astBuilder.dropAllNodes();
    }

    protected final boolean eof() {
        return astBuilder.eof();
    }

    protected void error(Position position, String msg) {
        errorListener.error(position, msg);
    }

    protected T lookAhead(final int i) {
        return astBuilder.lookAhead(i);
    }

    protected void loopCheck() {
        final long current = getOffset();
        if (current == lastPosition) throw new LoopException(astBuilder.getCurrentPosition());
        lastPosition = current;
    }

    protected final boolean match(T tokenType) {
        final boolean b = current(tokenType);
        if (b) consume();
        return b;
    }

    @SafeVarargs protected final boolean matchAnyOf(T... tokens) {
        for (final T token : tokens) {
            if (current(token)) {
                consume();
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("UnusedReturnValue")
    protected boolean matchOrError(T tokenType) {
        final boolean b = currentOrError(tokenType);
        if (b) consume();
        return b;
    }

    protected boolean next(T token) {
        return astBuilder.lookAhead(1) == token;
    }

    /** Check not end of file and given type does not match current token. */
    protected final boolean notCurrent(T tokenType) {
        return !eof() && getCurrent() != tokenType;
    }

    @SafeVarargs protected final boolean notCurrentAnyOf(T... tokenType) {
        if (eof()) return false;
        for (final T t : tokenType) {
            if (getCurrent() == t) return false;
        }
        return true;
    }

    /**
     * Parse a List of Elements delimited by the specified tokens.
     *
     * @param  listNode       The type of tree to build
     * @param  leftToken      The left delimiter
     * @param  sepToken       The separator token (It can be null)
     * @param  rightToken     The right delimiter
     * @param  elementParser  The procedure to invoke to parse the elements of the list
     */
    protected final boolean parseList(@NotNull T listNode, @NotNull T leftToken, @Nullable T sepToken, @NotNull T rightToken,
                                      @NotNull Parser elementParser) {
        final boolean b = current(leftToken);
        if (b) {
            beginTree();
            discardOrError(leftToken);
            while (notCurrent(rightToken)) {
                loopCheck();
                elementParser.parse();
                if (notCurrent(rightToken)) {
                    if (sepToken != null) {
                        currentOrError(sepToken);
                        discard();
                    }
                }
            }
            discardOrError(rightToken);
            endTree(listNode);
        }
        return b;
    }
    /** Parse a List of Elements delimited by the specified tokens OR a simple Element. */
    protected final void parseOptionalList(@NotNull T listNode, @NotNull T leftToken, @Nullable T sepToken, @NotNull T rightToken,
                                           @NotNull Parser elementParser) {
        if (!parseList(listNode, leftToken, sepToken, rightToken, elementParser)) {
            beginTree();
            elementParser.parse();
            endTree(listNode);
        }
    }

    protected void parseSimpleTree(T treeRoot, T content) {
        if (current(treeRoot)) {
            beginTree();
            discard(treeRoot);
            matchOrError(content);
            endTree(treeRoot);
        }
    }

    @SafeVarargs protected final void parseSimpleTree(T treeRoot, T... contentAlternatives) {
        if (current(treeRoot)) {
            beginTree();
            discard(treeRoot);
            if (!matchAnyOf(contentAlternatives)) unexpectedToken();
            endTree(treeRoot);
        }
    }

    /**
     * Parse one or more values with a separator token.
     *
     * @param  valuesNode     The type of tree to build
     * @param  sepToken       The separator token (It can be null)
     * @param  elementParser  The procedure to invoke to parse the elements of the list
     */
    protected final void parseValues(T valuesNode, T sepToken, Parser elementParser) {
        beginTree();
        elementParser.parse();
        while (discard(sepToken)) {
            loopCheck();
            elementParser.parse();
        }
        endTree(valuesNode);
    }

    @SafeVarargs protected final void unexpectedAndAdvanceTo(T... tokens) {
        unexpectedError();
        advanceTo(tokens);
    }

    protected void unexpectedError() {
        beginTree();
        endTree(getCurrent());
        error(astBuilder.getCurrentPosition(), MSGS.unexpected(getCurrentDescription()));
    }

    protected final void unexpectedToken() {
        unexpectedError();
        discard();
    }

    @SafeVarargs protected final T getAnyOf(T... tokens) {
        for (final T token : tokens) {
            if (currentByText(token)) return token;
        }
        return getCurrent();
    }

    @NotNull protected ASTBuilder<T> getAstBuilder() {
        return astBuilder;
    }

    @NotNull protected final String getCurrentText() {
        return astBuilder.getCurrentText();
    }

    /** Return the Error Listener. */
    @NotNull protected ParserErrorListener getErrorListener() {
        return errorListener;
    }

    protected boolean isCompletion() {
        return astBuilder.isCompletion();
    }

    /** Check given type matches ahead token. */
    private boolean aheadByText(T tokenType, final int i) {
        final T ahead = astBuilder.lookAhead(i);
        return ahead == tokenType || ahead.getKind().isIdentifier() && ahead.getText().equals(tokenType.getText());
    }

    private void error(String msg) {
        error(astBuilder.getCurrentPosition(), msg);
    }

    private String getCurrentDescription() {
        return getCurrent().getDescription(getCurrentText());
    }

    private long getOffset() {
        return astBuilder.getOffset();
    }
}  // end class AbstractParser
