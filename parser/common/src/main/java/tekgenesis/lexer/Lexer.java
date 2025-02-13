
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

import tekgenesis.parser.Position;

/**
 * A lexer is recognizer that draws input symbols from a character stream. Specific lexer grammars
 * are implementations of this interface this object.
 *
 * @param  <T>  The type of input symbols (tokens) returned by the Lexer
 */
public interface Lexer<T extends TokenType<T>> {

    //~ Methods ......................................................................................................................................

    /** Advance the lexer to the next token. */
    void advance();

    /** Reset the lexer to operate with this new Stream. */
    Lexer<T> resetStream(CharStream cs);

    /** Get the Position (Line, Column) the current token. */
    @NotNull Position getCurrentPosition();

    /** get the current token in the input stream. */
    @NotNull T getCurrentToken();

    /** Get the index of the end position if the current token. */
    int getCurrentTokenEnd();

    /** Get the index of the start position if the current token. */
    int getCurrentTokenStart();

    /** Get the current Token String. */
    CharSequence getCurrentTokenText();

    /** Ge the current state of the Lexer (0 = default state). */
    int getState();

    /** Set the current state of the Lexer (0 = default state). */
    void setState(int initialState);
}
