
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
 * Invalid Name Exception.
 */
@SuppressWarnings("WeakerAccess")
public class InvalidNameException extends BadGrammarException {

    //~ Constructors .................................................................................................................................

    InvalidNameException(Throwable cause) {
        super(cause);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2286424213941744729L;
}
