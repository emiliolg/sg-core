
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
 * Not null violation exception.
 */
@SuppressWarnings("WeakerAccess")
public class NotNullViolationException extends DatabaseIntegrityViolationException {

    //~ Constructors .................................................................................................................................

    NotNullViolationException(Throwable cause) {
        super(cause);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2560675675118699129L;
}
