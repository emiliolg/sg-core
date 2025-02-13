
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
@SuppressWarnings("WeakerAccess")
public class DatabaseUnspecifiedException extends DatabaseException {

    //~ Constructors .................................................................................................................................

    /** Constructor for the exception. */
    public DatabaseUnspecifiedException(Throwable cause) {
        super(cause, true);
    }

    DatabaseUnspecifiedException(String message) {
        super(message, true);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -1921771037378429991L;
}
