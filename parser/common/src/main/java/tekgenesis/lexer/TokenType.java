
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lexer;

import org.jetbrains.annotations.NotNull;

/**
 * An interface that must be implemented by a Token class.
 */
public interface TokenType<T extends TokenType<T>> {

    //~ Methods ......................................................................................................................................

    /** If the token is an alias then return the one I'm aliasing. (If not it will return this) */
    @NotNull T target();

    /** Return a description of the token. */
    @NotNull String getDescription(String tokenText);

    /** Returns true if the token should be ignored when processing the AST for building. */
    boolean isIgnorable();

    /** Return true if the token is a white space one. */
    boolean isWhiteSpace();

    /** Return the {@link TokenKind} of this Token. */
    @NotNull TokenKind getKind();

    /** Return a textual representation of the token. */
    @NotNull String getText();

    /** Return true if the token represents the <code>Empty</code> token. */
    boolean isEmpty();
}
