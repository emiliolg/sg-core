
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

import tekgenesis.lexer.TokenType;

/**
 * A AST builder creates Abstract Syntax tress based on a simple interface that should be called
 * from the Parser while consuming Tokens from the Lexer.
 *
 * @param  <T>  The type of input symbols (tokens) returned by the Lexer
 */
public interface ASTBuilder<T extends TokenType<T>> {

    //~ Methods ......................................................................................................................................

    /** Adds the current input token to the sub-tree under construction. */
    void addChild();

    /** Advance the Lexer to the next position. */
    void advanceLexer();
    /**
     * Instruct the Builder to start the building of a new tree node Every call to addChild will
     * place a new child under the current sub-tree.
     */
    void beginNode();
    /** discard selected token and advance. */
    void discard();

    /** Instruct the Builder to drop all pending nodes. */
    void dropAllNodes();

    /** Instruct the builder to drop the current node being built. */
    void dropNode();

    /**
     * Instruct the builder to start the building of a new tree at the beginning of the last started
     * node. All the children add to the previous node will be placed under the new one
     */
    void dupBeginNode();

    /** Works as {@link #endNode} but pushes the current tree back. */
    void endAndPushNode(T token);

    /**
     * Instruct the Builder to end the building of the current sub-tree. The provided token will be
     * used as the root of the constructed sub-tree
     */
    void endNode(T token);

    /** Returns true if the token source is exhausted. */
    boolean eof();

    /** A token <code>n</code> positions ahead. */
    @NotNull T lookAhead(int n);

    /** Ge the position of the current. */
    @NotNull Position getCurrentPosition();

    /** Get the text of the current token. */
    @NotNull String getCurrentText();

    /** Returns true if the node is an imaginary completion token (Usually inserted by the IDE). */
    boolean isCompletion();

    /** The offset in the input (considered as a sequence of characters) of the current token. */
    long getOffset();

    /** The token type of the current token. */
    @NotNull T getTokenType();
}  // end interface ASTBuilder
