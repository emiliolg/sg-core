
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.parser;

import static tekgenesis.common.Predefined.notNull;

/**
 * Defines the Position (line, column) in an input text.
 */
public interface Position {

    //~ Methods ......................................................................................................................................

    /** Decorates the Position with an error message. */
    Diagnostic createMessage(String msg);

    /** Return an Offset that must always be different and ascending inside a file. */
    long getOffset();

    //~ Inner Classes ................................................................................................................................

    class LineColumnPosition implements Position {
        private final int    column;
        private final int    line;
        private final String sourceName;

        /** Creates a new Position object. */
        private LineColumnPosition(String sourceName, int line, int column) {
            this.sourceName = notNull(sourceName);
            this.line       = line;
            this.column     = column;
        }

        @Override public Diagnostic createMessage(final String msg) {
            return new Diagnostic.Default(this, msg) {
                @Override public String getFormattedMessage() {
                    return sourceName + ":" + line + ": " + msg;
                }
            };
        }

        @Override public boolean equals(Object o) {
            if (o instanceof LineColumnPosition) {
                final LineColumnPosition that = (LineColumnPosition) o;
                return line == that.line && column == that.column && sourceName.equals(that.sourceName);
            }
            return false;
        }

        @Override public int hashCode() {
            return (31 * line + column) * 31 + sourceName.hashCode();
        }

        @Override public String toString() {
            return "(" + line + ", " + column + ")";
        }

        /** Get column. */
        public int getColumn() {
            return column;
        }

        /** Get line. */
        public int getLine() {
            return line;
        }

        public long getOffset() {
            return line << SALT | column;
        }

        /** Get source name. */
        public String getSourceName() {
            return sourceName;
        }

        /** Creates a new Position object from a line and column. */
        public static Position createPosition(String sourceName, int line, int column) {
            return new LineColumnPosition(sourceName, line, column);
        }

        private static final long SALT = 20;

        public static final Position ZERO = createPosition("", 0, 0);
    }  // end class LineColumnPosition

    class OffsetPosition implements Position {
        private final long offset;

        /** Creates a new Position object. */
        private OffsetPosition(long offset) {
            this.offset = offset;
        }

        @Override public Diagnostic createMessage(String msg) {
            return new Diagnostic.Default(this, msg);
        }

        @Override public boolean equals(Object o) {
            return o instanceof OffsetPosition && offset == ((OffsetPosition) o).offset;
        }

        @Override public int hashCode() {
            return (int) offset;
        }

        @Override public String toString() {
            return "(" + offset + ")";
        }

        public long getOffset() {
            return offset;
        }

        /** Creates a new Position object from a line and column. */
        public static Position createPosition(long offset) {
            return new OffsetPosition(offset);
        }

        public static final Position ZERO = createPosition(0);
    }
}  // end class Position
