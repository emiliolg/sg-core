
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
 * Restrict ViolationException.
 */
@SuppressWarnings("WeakerAccess")
public class RestrictViolationException extends DatabaseIntegrityViolationException {

    //~ Constructors .................................................................................................................................

    RestrictViolationException(Throwable cause) {
        super(cause);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -7361234708119431202L;
}
