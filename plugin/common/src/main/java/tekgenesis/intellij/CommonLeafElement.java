
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.intellij;

import java.util.Iterator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.impl.source.tree.LeafPsiElement;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.lexer.TokenType;
import tekgenesis.parser.ASTNode;
import tekgenesis.parser.Position;

/**
 * The base class for Leaf Elements in the Psi tree.
 */
public abstract class CommonLeafElement<N extends ASTNode<N, T>, E extends CommonElementType<T>, T extends TokenType<T>> extends LeafPsiElement
    implements CommonPsiElement<E, T>, ASTNode<N, T>
{

    //~ Instance Fields ..............................................................................................................................

    private final E elementType;

    //~ Constructors .................................................................................................................................

    /** Creates the element. */
    protected CommonLeafElement(E type, CharSequence text) {
        super(type, text);
        elementType = type;
    }

    //~ Methods ......................................................................................................................................

    @Override public void annotate(AnnotationHolder holder) {}

    @Override public Seq<N> children() {
        return Colls.emptyIterable();
    }

    @Override public Seq<N> children(@NotNull T mmToken) {
        return Colls.emptyIterable();
    }

    @Override public boolean hasType(T type) {
        return getType() == type;
    }

    @NotNull @Override public Iterator<N> iterator() {
        return Colls.emptyIterator();
    }

    @Override public String toString() {
        return "CommonLeafElement{" +
               "elementType=" + elementType + '}';
    }

    @NotNull @Override public N getChild(int n) {
        return getEmptyNode();
    }

    public boolean isWhiteSpace() {
        return getType().isWhiteSpace();
    }

    @NotNull @Override public ASTNode<N, T> getEffectiveNode() {
        return this;
    }

    @NotNull @Override public E getElementType() {
        return elementType;
    }

    @NotNull public Position getPosition() {
        return Position.OffsetPosition.createPosition(getTextOffset());
    }

    @NotNull public T getType() {
        return getElementType().getTokenType();
    }

    public boolean isEmpty() {
        return getType().isEmpty();
    }
}  // end class CommonLeafElement
