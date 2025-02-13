
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
 * Unique constraint violation exception.
 */
@SuppressWarnings("WeakerAccess")
public class UniqueViolationException extends DatabaseIntegrityViolationException {

    //~ Constructors .................................................................................................................................

    UniqueViolationException(Throwable cause) {
        super(cause);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 2932525725749062403L;
}
