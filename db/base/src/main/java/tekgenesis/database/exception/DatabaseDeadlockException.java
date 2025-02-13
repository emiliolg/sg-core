
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
public class DatabaseDeadlockException extends DatabaseException {

    //~ Constructors .................................................................................................................................

    DatabaseDeadlockException(Throwable cause) {
        super(cause, false);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1617878670107143016L;
}
