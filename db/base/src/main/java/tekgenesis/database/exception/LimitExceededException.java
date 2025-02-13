
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.exception;

/**
 * Exceptions of DatabaseHandling.
 */
public class LimitExceededException extends DatabaseException {

    //~ Constructors .................................................................................................................................

    /** Create a LimitExceeded Exception with an specified message. */
    public LimitExceededException(String message) {
        super(message, true);
    }

    LimitExceededException(Throwable cause) {
        super(cause, true);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5260336826478469836L;
}
