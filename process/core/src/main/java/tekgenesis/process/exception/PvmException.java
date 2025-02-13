
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.exception;

/**
 * Building Exceptions.
 */
public class PvmException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    /** Creates a building Exception. */
    public PvmException(String msg) {
        super(msg);
    }

    /** Creates a building Exception. */
    public PvmException(String msg, Throwable cause) {
        super(msg, cause);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -1032214332031677201L;
}
