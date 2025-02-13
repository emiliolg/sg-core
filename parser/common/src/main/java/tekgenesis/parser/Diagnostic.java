
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.parser;

/**
 * Collected Parser Errors.
 */
public interface Diagnostic {

    //~ Methods ......................................................................................................................................

    /** Get a formatted version of the message. */
    String getFormattedMessage();

    /** Get the plain message. */
    String getMessage();

    /** Get the position in the input where this message occurs. */
    Position getPosition();

    //~ Inner Classes ................................................................................................................................

    class Default implements Diagnostic {
        final String   message;
        final Position position;

        Default(Position position, String message) {
            this.position = position;
            this.message  = message;
        }

        @Override public String toString() {
            return getFormattedMessage();
        }

        @Override public String getFormattedMessage() {
            return message;
        }

        @Override public String getMessage() {
            return message;
        }

        @Override public Position getPosition() {
            return position;
        }
    }
}  // end interface Diagnostic
