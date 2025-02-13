
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
 * Exclusion violation exception.
 */
@SuppressWarnings("WeakerAccess")
public class ExclusionViolationException extends DatabaseIntegrityViolationException {

    //~ Constructors .................................................................................................................................

    ExclusionViolationException(Throwable cause) {
        super(cause);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2479131556193129491L;
}
