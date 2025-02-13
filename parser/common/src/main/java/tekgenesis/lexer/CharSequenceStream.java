
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

import static tekgenesis.parser.Position.LineColumnPosition.createPosition;

/**
 * This class implements the {@link CharStream} class based on a {@link CharSequence}.
 */
public class CharSequenceStream extends CharStream implements CharSequence {

    //~ Instance Fields ..............................................................................................................................

    /** The index of the character relative to the beginning of the line 0. */
    private int column;

    private final CharSequence data;
    private final int          endOffset;

    /** line number 1..n within the input. */
    private int line = 1;

    /** startOffset..endOffset-1 index into string of next char. */
    private int p;

    private final String sourceName;
    private final int    startOffset;
    /** Spaces for tab. */
    private int tabSpaces = 4;

    //~ Constructors .................................................................................................................................

    /** Create an instance of the class based on the empty String. */
    public CharSequenceStream() {
        this("", "");
    }

    /** Create an instance of the class based on a {@link CharSequence}. */
    public CharSequenceStream(String sourceName, CharSequence input) {
        this(sourceName, input, 0, input.length());
    }

    /** Create an instance of the class based on a section of a {@link CharSequence}. */
    public CharSequenceStream(String sourceName, CharSequence input, int startOffset, int endOffset) {
        data             = input;
        this.startOffset = startOffset;
        this.endOffset   = endOffset;
        p                = startOffset;
        tabSpaces        = 4;
        this.sourceName  = sourceName;
    }

    //~ Methods ......................................................................................................................................

    @Override public char charAt(int index) {
        assert index >= startOffset && index < endOffset;
        return data.charAt(index);
    }

    public void consume() {
        if (p < endOffset) {
            column++;
            final char c = data.charAt(p);
            if (c == '\n') {
                line++;
                column = 0;
            }
            else if (c == '\t') {
                while (column % tabSpaces != 1)
                    column++;
            }
            p++;
        }
    }

    public int currentChar() {
        return lookAhead(0);
    }

    /**
     * Return the current input symbol index startOffset..endOffset where n indicates the last
     * symbol has been read. The index is the index of char to be returned from LA(1).
     */
    public int index() {
        return p;
    }

    @Override public int length() {
        return endOffset - startOffset;
    }

    public int lookAhead(int i) {
        assert i >= 0;
        return (p + i) < endOffset ? data.charAt(p + i) : CharStream.EOF_CHAR;
    }

    @Override public CharSequence subSequence(int start, int end) {
        assert start >= startOffset && end <= endOffset && start <= end;
        return data.subSequence(start, end);
    }

    @NotNull public String toString() {
        return data.subSequence(startOffset, endOffset).toString();
    }

    public Position getPosition() {
        return createPosition(sourceName, line, column);
    }

    @Override public String getSourceName() {
        return sourceName;
    }
}  // end class CharSequenceStream
