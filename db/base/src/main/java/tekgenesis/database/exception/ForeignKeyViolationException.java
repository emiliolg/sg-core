
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
 * Foreign key violation exception.
 */
@SuppressWarnings("WeakerAccess")
public class ForeignKeyViolationException extends DatabaseIntegrityViolationException {

    //~ Constructors .................................................................................................................................

    ForeignKeyViolationException(Throwable cause) {
        super(cause);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3161386246777758196L;
}
