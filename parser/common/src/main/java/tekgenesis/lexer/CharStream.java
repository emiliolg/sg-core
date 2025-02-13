
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lexer;

import tekgenesis.parser.Position;

/**
 * A source of characters for a {@link Lexer}.
 */
abstract class CharStream implements CharSequence {

    //~ Methods ......................................................................................................................................

    /** Consume and advance to the next character. */
    public abstract void consume();

    /** The current character in the input stream. */
    public abstract int currentChar();

    /** The absolute index (0..n) of the current character. */
    public abstract int index();

    /**
     * Return the character <code>i</code> characters ahead of the current position, or
     * {@link CharStream#EOF_CHAR} if the EOF is reached.
     */
    public abstract int lookAhead(int i);

    /**
     * Match the current character with the argument provided. If the match is successful true is
     * returned and the character is consumed. Otherwise false is returned and the stream is not
     * advanced.
     */
    public final boolean match(int c) {
        final boolean b = c == currentChar();
        if (b) consume();
        return b;
    }

    /**
     * Similar to {@link #match(int) } but match every character in sequence.
     *
     * <p>If the match is successful true is returned and all the characters are consumed. Otherwise
     * false is returned and the stream is not advanced.</p>
     */
    public final boolean match(int... chars) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != lookAhead(i)) return false;
        }

        for (int i = chars.length; i > 0; i--)
            consume();
        return true;
    }

    /**
     * Similar to {@link #match(int) } but tries to match with any of the characters provided.
     *
     * <p>If the match is successful true is returned and the character is consumed. Otherwise false
     * is returned and the stream is not advanced.</p>
     */
    public final boolean matchAny(int... chars) {
        final int current = currentChar();
        for (final int c : chars) {
            if (c == current) {
                consume();
                return true;
            }
        }
        return false;
    }

    /** The {@link Position} of the current character. */
    public abstract Position getPosition();

    /** Return the name of the source (Usually a file name, or similar). */
    public abstract String getSourceName();

    //~ Static Fields ................................................................................................................................

    public static final int EOF_CHAR = -1;
}  // end class CharStream
