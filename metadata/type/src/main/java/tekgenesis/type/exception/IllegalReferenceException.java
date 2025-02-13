
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type.exception;

/**
 * IllegalReferenceException.
 */
public class IllegalReferenceException extends TypeException {

    //~ Instance Fields ..............................................................................................................................

    private final String msg;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public IllegalReferenceException() {
        msg = "No message";
    }

    /** Constructor with message. */
    public IllegalReferenceException(String msg) {
        this.msg = msg;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return msg;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -6398514185693428576L;
}
