
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
 * Exception for int types with length argument greater than the largest value possible.
 */
public class IntLengthException extends TypeException {

    //~ Instance Fields ..............................................................................................................................

    private final String message;

    //~ Constructors .................................................................................................................................

    /** Exception constructor. */
    public IntLengthException() {
        message = "";
    }

    /** Exception constructor. */
    public IntLengthException(final String message) {
        this.message = message;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return message;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3008553490911921069L;
}
