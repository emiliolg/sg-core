
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.parser;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.BaseSeq;
import tekgenesis.common.collections.ImmutableIterator;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.lexer.TokenType;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.collections.Colls.immutable;

/**
 * Default implementation of a {@link ASTNode}.
 */
public abstract class AbstractASTNode<N extends ASTNode<N, T>, T extends TokenType<T>> extends BaseSeq<N> implements ASTNode<N, T> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final ImmutableList<N> children;
    @NotNull private final Position         position;
    @NotNull private final String           text;
    @NotNull private final T                type;

    //~ Constructors .................................................................................................................................

    protected AbstractASTNode(@NotNull T type, @NotNull String text, @NotNull Position position) {
        this.type     = type;
        this.position = position;
        this.text     = text;
        children      = emptyList();
    }

    protected AbstractASTNode(@NotNull T type, @NotNull String text, @NotNull Position position, @NotNull List<N> children) {
        this.type     = type;
        this.position = position;
        this.text     = text;
        this.children = immutable(children);
    }

    //~ Methods ......................................................................................................................................

    @Override public final Seq<N> children() {
        return children;
    }

    @Override public final Seq<N> children(@NotNull final T t) {
        return Utils.children(this, t);
    }

    @Override public boolean hasType(T token) {
        return equal(getType(), token);
    }

    @NotNull @Override public ImmutableIterator<N> iterator() {
        return children.iterator();
    }

    @Override public String toString() {
        return getText();
    }

    @NotNull @Override public N getChild(int n) {
        return n < 0 || n >= children.size() ? getEmptyNode() : children.get(n);
    }

    @Override public boolean isWhiteSpace() {
        return type.isWhiteSpace();
    }

    @NotNull @Override public ASTNode<N, T> getEffectiveNode() {
        return this;
    }

    @NotNull @Override public Position getPosition() {
        return position;
    }

    @NotNull @Override public String getText() {
        return text;
    }

    @NotNull @Override public T getType() {
        return type;
    }
    @Override public boolean isEmpty() {
        return getType().isEmpty();
    }
}  // end class AbstractASTNode
