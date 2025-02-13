
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

import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.CompositePsiElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.AbstractIterator;
import tekgenesis.common.collections.Seq;
import tekgenesis.lexer.TokenType;
import tekgenesis.parser.ASTNode;
import tekgenesis.parser.Position;

import static tekgenesis.common.collections.Colls.immutable;

/**
 * The base class for Composite Elements in the Psi tree.
 */
public abstract class CommonCompositeElement<N extends ASTNode<N, T>, E extends CommonElementType<T>, T extends TokenType<T>>
    extends CompositePsiElement implements CommonPsiElement<E, T>, ASTNode<N, T>
{

    //~ Instance Fields ..............................................................................................................................

    private final E elementType;

    //~ Constructors .................................................................................................................................

    /** Create a PsiElement. */
    protected CommonCompositeElement(E t) {
        super(t);
        elementType = t;
    }

    //~ Methods ......................................................................................................................................

    @Override public final Seq<N> children() {
        return Seq.createSeq(() -> immutable(iterator()));
    }

    @Override public final Seq<N> children(@NotNull T tokenType) {
        return Utils.children(this, tokenType);
    }

    @Override public final boolean hasType(T type) {
        return getType() == type;
    }

    @NotNull @Override public Iterator<N> iterator() {
        return new AbstractIterator<N>() {
            @Override protected boolean advance() {
                com.intellij.lang.ASTNode node = (com.intellij.lang.ASTNode) next;
                do {
                    node = node == null ? getFirstChildNode() : node.getTreeNext();
                }
                while (mustSkip(node));
                next = asNode(node);
                return next != null;
            }

            @SuppressWarnings("unchecked")
            private N asNode(com.intellij.lang.ASTNode node) {
                return (N) node;
            }

            private boolean mustSkip(com.intellij.lang.ASTNode node) {
                return node != null && (!(node instanceof ASTNode) || asNode(node).getType().isIgnorable());
            }
        };
    }

    @Override public String toString() {
        return elementType.toString();
    }

    // @Override public String toString() { return getName(); }

    @NotNull @Override public final N getChild(int number) {
        if (number >= 0) {
            int i = number;
            for (final N node : this) {
                if (i-- == 0) return node;
            }
        }
        return getEmptyNode();
    }

    public final boolean isWhiteSpace() {
        return getType().isWhiteSpace();
    }

    @NotNull @Override public E getElementType() {
        return elementType;
    }
    /** get Line Marker for this CommonComposite. */

    @NotNull public final Position getPosition() {
        return Position.OffsetPosition.createPosition(getTextOffset());
    }
    /** Gets PsiElement for the first previous comma found in the tree. */
    @Nullable public PsiElement getPreviousComma() {
        PsiElement prevSibling = getPrevSibling();
        while (prevSibling != null) {
            if (",".equals(prevSibling.getText())) return prevSibling;
            else prevSibling = prevSibling.getPrevSibling();
        }
        return null;
    }

    @NotNull public final T getType() {
        return getElementType().getTokenType();
    }
    public final boolean isEmpty() {
        return getType().isEmpty();
    }
}  // end class CommonCompositeElement
