
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.intellij;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Stack;
import tekgenesis.lexer.TokenType;
import tekgenesis.parser.ASTBuilder;
import tekgenesis.parser.Parser;
import tekgenesis.parser.ParserErrorListener;
import tekgenesis.parser.Position;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.parser.Position.OffsetPosition.ZERO;
import static tekgenesis.parser.Position.OffsetPosition.createPosition;

/**
 * An adapter from a {@link Parser } to an Intellij PsiParser.
 */
public abstract class ParserAdapter<T extends TokenType<T>> implements PsiParser, ASTBuilder<T>, ParserErrorListener {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private PsiBuilder                    builder;
    @NotNull private final Stack<PsiBuilder.Marker> markers;

    //~ Constructors .................................................................................................................................


    protected ParserAdapter() {
        builder = null;
        markers = Stack.createStack(INITIAL_CAPACITY);
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChild() {}
    @Override public final void advanceLexer() {
        getBuilder().advanceLexer();
    }

    @Override public final void beginNode() {
        markers.push(getBuilder().mark());
    }

    @Override public void dropAllNodes() {
        while (markers.size() > 1)
            dropNode();
    }

    @Override public final void dropNode() {
        markers.pop().drop();
    }

    @Override public final void dupBeginNode() {
        final PsiBuilder.Marker prev = markers.pop();
        markers.push(prev.precede());
        markers.push(prev);
    }

    @Override public void endAndPushNode(T token) {
        final PsiBuilder.Marker current = markers.pop();
        current.done(adapt(token));
        markers.push(current.precede());
    }

    @Override public final void endNode(T token) {
        markers.pop().done(adapt(token));
    }

    public boolean eof() {
        return builder == null || builder.eof();
    }

    @Override public void error(Position pos, String message) {
        getBuilder().error(message);
        assert log(message);
    }

    @NotNull @Override public final T lookAhead(int i) {
        return adapt(getBuilder().lookAhead(i));
    }

    @NotNull public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder psiBuilder) {
        builder = psiBuilder;
        builder.setDebugMode(true);
        final PsiBuilder.Marker mark = psiBuilder.mark();
        parseRoot();
        mark.done(root);
        return psiBuilder.getTreeBuilt();
    }

    @NotNull @Override public Position getCurrentPosition() {
        return builder == null ? ZERO : createPosition(builder.getCurrentOffset());
    }

    @NotNull @Override public String getCurrentText() {
        return notNull(builder != null ? builder.getTokenText() : "");
    }

    @Override public boolean isCompletion() {  // noinspection SpellCheckingInspection
        return getCurrentText().endsWith("IntellijIdeaRulezzz");
    }

    @Override public long getOffset() {
        return getBuilder().getCurrentOffset();
    }

    @NotNull @Override public T getTokenType() {
        return adapt(getBuilder().getTokenType());
    }

    protected abstract T adapt(@Nullable IElementType tokenType);
    protected abstract IElementType adapt(T token);
    protected abstract void parseRoot();

    @NotNull protected PsiBuilder getBuilder() {
        if (builder == null) throw new IllegalStateException("Null PsiBuilder");
        return builder;
    }

    private boolean log(String message) {
        System.out.println("message = " + message);
        return true;
    }

    //~ Static Fields ................................................................................................................................

    private static final int INITIAL_CAPACITY = 50;
}  // end class ParserAdapter
