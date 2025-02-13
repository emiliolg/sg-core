
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.parser;

import java.io.PrintWriter;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.lexer.TokenType;

import static java.lang.Integer.parseInt;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Strings.spaces;

/**
 * A Node of the Abstract Syntax Tree.
 */
public interface ASTNode<N extends ASTNode<N, T>, T extends TokenType<T>> extends Iterable<N> {

    //~ Methods ......................................................................................................................................

    /** Return all children of this node. */
    Seq<N> children();

    /** Return all children of this node with the specified Token Type. */
    Seq<N> children(@NotNull T t);

    /** Returns true if the node has the specified type. */
    boolean hasType(T type);

    /**
     * Returns an {@link Option} to the Child in the specified position. Returns
     * {@link Option#empty()} } if there si no child with that index
     */
    @NotNull N getChild(int n);

    /** Returns <code>true</code> if the node is considered whitespace. */
    boolean isWhiteSpace();

    /** Returns the effective node. Used for wrapping nodes. */
    @NotNull ASTNode<N, T> getEffectiveNode();

    /** Get the Empty for the AST. */
    N getEmptyNode();

    /** Returns the position in the input. */
    @NotNull Position getPosition();

    /** Returns the text representation of the node. */
    @NotNull String getText();
    /** Returns the type of the Node. */
    @NotNull T getType();

    /** Return true if the child is the Empty Node. */
    boolean isEmpty();

    //~ Inner Classes ................................................................................................................................

    /**
     * Some handy utils yo manage Nodes.
     */
    class Utils {
        private Utils() {}

        /** Return a List (Lisp like) representation of the AST. */
        public static <N extends ASTNode<N, T>, T extends TokenType<T>> String asLispList(@NotNull ASTNode<N, T> node) {
            final Seq<N> children = node.children();
            final String text     = node.getText();
            if (children.isEmpty()) return text;
            else {
                final StringBuilder builder = new StringBuilder();
                builder.append("(").append(text);
                for (final N child : children) {
                    builder.append(" ");
                    builder.append(asLispList(child));
                }
                builder.append(")");
                return builder.toString();
            }
        }

        /** Verify the type of the Node. */
        public static <N extends ASTNode<N, T>, T extends TokenType<T>> void assertType(@NotNull ASTNode<N, T> node, T type) {
            if (!node.hasType(type)) throw new IllegalArgumentException(INVALID_TOKEN_TYPE + node.getType() + ", must be: " + type);
        }

        /** Return all children of this node with the specified Token Type. */
        public static <N extends ASTNode<N, T>, T extends TokenType<T>> Seq<N> children(@NotNull ASTNode<N, T> node, @NotNull final T t) {
            return node.children().filter(e -> e != null && e.hasType(t));
        }

        /** Return the children of the node as Strings. */
        public static Seq<String> childrenAsStrings(ASTNode<?, ?> node) {
            final Seq<ASTNode<?, ?>>              children = cast(node.children());
            final Function<ASTNode<?, ?>, String> getText  = ASTNode::getText;
            return children.map(getText);
        }

        /** Return the first atom (Node without children) of the specified tree. */

        public static <N extends ASTNode<N, T>, T extends TokenType<T>> N firstAtom(N node) {
            N n     = node;
            N child = n.getChild(0);
            while (!child.isEmpty()) {
                n     = child;
                child = n.getChild(0);
            }
            return n;
        }

        /** Print the Node as a Tree, one Node per line, indented. */
        public static <N extends ASTNode<N, T>, T extends TokenType<T>> void printTree(@NotNull ASTNode<N, T> node, PrintWriter out) {
            printTree(node, out, 0);
            out.flush();
        }

        /** Get the nth child as an int. */
        public static <N extends ASTNode<N, T>, T extends TokenType<T>> int getChildAsInt(@NotNull ASTNode<N, T> t, int child) {
            final N p = t.getChild(child);

            return p.getType().isEmpty() ? 0 : parseInt(p.getText());
        }
        private static <N extends ASTNode<N, T>, T extends TokenType<T>> void printTree(@NotNull ASTNode<N, T> node, PrintWriter out, int indent) {
            out.printf("%7s%s %s: %s\n", node.getPosition(), spaces(indent), node.getType(), node.getText());
            for (final N child : node.children())
                printTree(child, out, indent + 4);
        }

        public static final String INVALID_TOKEN_TYPE = "Invalid token type: ";
    }  // end class Utils
}
