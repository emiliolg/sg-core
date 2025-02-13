
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
public class DatabaseExecutionCanceledException extends DatabaseException {

    //~ Constructors .................................................................................................................................

    DatabaseExecutionCanceledException(Throwable cause) {
        super(cause, false);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3268946017618302288L;
}
