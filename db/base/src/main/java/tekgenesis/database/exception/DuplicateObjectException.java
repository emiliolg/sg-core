
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
 * Duplicate object exception.
 */
@SuppressWarnings("WeakerAccess")
public class DuplicateObjectException extends BadGrammarException {

    //~ Constructors .................................................................................................................................

    DuplicateObjectException(Throwable cause) {
        super(cause);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 7428077602306369292L;
}
