
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.parser;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Stack;
import tekgenesis.lexer.Lexer;
import tekgenesis.lexer.TokenType;

import static tekgenesis.common.collections.Colls.mkString;

/**
 * A base implementation of the {@link ASTBuilder} interface.
 */
public abstract class AbstractASTBuilder<N extends ASTNode<N, T>, T extends TokenType<T>> implements ASTBuilder<T>, ParserErrorListener {

    //~ Instance Fields ..............................................................................................................................

    private int       current;
    private final int last;

    @NotNull private final List<Diagnostic> messages;
    private final boolean                   skipWhiteSpace;
    @NotNull private final String           sourceName;

    @NotNull private final Stack<NodeBuilder<N>> stack;
    @NotNull private final List<N>               tokens;

    //~ Constructors .................................................................................................................................

    protected AbstractASTBuilder(@NotNull Lexer<T> lexer, @NotNull String sourceName, boolean skipWhiteSpace) {
        this.skipWhiteSpace = skipWhiteSpace;
        stack               = Stack.createStack();
        tokens              = slurpTokens(lexer);
        current             = -1;
        messages            = new ArrayList<>();
        last                = tokens.size() - 1;
        this.sourceName     = sourceName;
        advanceLexer();
    }

    //~ Methods ......................................................................................................................................

    public void addChild() {
        addChild(getCurrent());
    }

    @Override public void advanceLexer() {
        while (current++ < last) {
            final N n = getCurrent();
            if (!n.isWhiteSpace()) return;
        }
    }

    @Override public void beginNode() {
        //
        stack.push(new NodeBuilder<>(getCurrentPosition()));
        // printStack("begin");
    }

    @Override public void discard() {
        advanceLexer();
    }

    @Override public void dropAllNodes() {
        while (stack.size() > 1)
            dropNode();
    }

    @Override public void dropNode() {
        final NodeBuilder<N> nb  = stack.pop();
        final NodeBuilder<N> top = stack.peek();
        for (final N n : nb.nodes)
            top.add(n);
            // printStack("drop");
    }

    @Override public void dupBeginNode() {
        final NodeBuilder<N> nb = stack.pop();
        stack.push(new NodeBuilder<>(nb.position));
        stack.push(nb);
        // printStack("dup");
    }

    @Override public void endAndPushNode(T token) {
        final N              n  = buildAST(token, token.getDescription(""));
        final NodeBuilder<N> nb = new NodeBuilder<>(n.getPosition());
        nb.add(n);
        stack.push(nb);
    }

    @Override public void endNode(T token) {
        addChild(buildAST(token, token.getDescription("")));
    }

    @Override public boolean eof() {
        return current >= last;
    }

    @Override public final void error(Position position, String message) {
        messages.add(position.createMessage(message));
    }

    @NotNull @Override public T lookAhead(final int position) {
        int i = position;
        int j = current;
        if (skipWhiteSpace) j += i;
        else {
            while (i > 0 && j < last) {
                if (!tokens.get(j).isWhiteSpace()) i--;
                j++;
            }
        }
        return j < last ? tokens.get(j).getType() : getCurrent().getEmptyNode().getType();
    }  // end method lookAhead

    @NotNull public Position getCurrentPosition() {
        return getCurrent().getPosition();
    }

    @NotNull public String getCurrentText() {
        return getCurrent().getText();
    }

    /** Get the List of Errors. */
    @NotNull public List<Diagnostic> getMessages() {
        return messages;
    }

    @Override public boolean isCompletion() {
        return false;
    }

    @Override public long getOffset() {
        return getCurrentPosition().getOffset();
    }

    /** Get the name of the source being processed. */
    @NotNull public String getSourceName() {
        return sourceName;
    }

    @NotNull @Override public T getTokenType() {
        return getCurrent().getType();
    }

    protected N buildAST(T token, String text) {
        final NodeBuilder<N> n = stack.pop();
        return createNode(token, text, n.position, n.nodes);
    }

    protected abstract N createNode(T token, String text, Position position);
    protected abstract N createNode(T token, String text, Position position, List<N> children);

    private void addChild(N n) {
        stack.peek().add(n);
    }

    private List<N> slurpTokens(Lexer<T> l) {
        final List<N> result = new ArrayList<>();
        while (true) {
            final T token = l.getCurrentToken();
            if (!skipWhiteSpace || !token.isWhiteSpace()) result.add(createNode(token, l.getCurrentTokenText().toString(), l.getCurrentPosition()));
            if (token.isEmpty()) return result;
            l.advance();
        }
    }

    private N getCurrent() {
        return tokens.get(current);
    }

    //~ Inner Classes ................................................................................................................................

    static class NodeBuilder<N> {
        private final List<N>  nodes;
        private final Position position;

        private NodeBuilder(Position position) {
            this.position = position;
            nodes         = new ArrayList<>(2);
        }

        @Override public String toString() {
            return mkString(nodes);
        }

        private void add(N n) {
            nodes.add(n);
        }
    }
}
