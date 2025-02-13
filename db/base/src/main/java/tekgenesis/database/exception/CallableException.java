
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
public class CallableException extends DatabaseException {

    //~ Constructors .................................................................................................................................

    /** Create a Callable Exception. */
    public CallableException(String msg) {
        super(msg, true);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3521996357416118415L;
}
