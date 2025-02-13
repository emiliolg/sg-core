
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
 * Check violation exception.
 */
@SuppressWarnings("WeakerAccess")
public class CheckViolationException extends DatabaseIntegrityViolationException {

    //~ Constructors .................................................................................................................................

    CheckViolationException(Throwable cause) {
        super(cause);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2059061283747716015L;
}
