
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
 * Database Exception Wrapper class.
 */
public abstract class DatabaseException extends RuntimeException {

    //~ Instance Fields ..............................................................................................................................

    private final boolean mustLog;
    @SuppressWarnings("NonFinalFieldOfException")
    private String        statement = null;

    //~ Constructors .................................................................................................................................

    DatabaseException(String message) {
        this(message, false);
    }

    DatabaseException(Throwable cause, boolean mustLog) {
        super(cause);
        this.mustLog = mustLog;
    }

    DatabaseException(String message, boolean mustLog) {
        super(message);
        this.mustLog = mustLog;
    }

    DatabaseException(final String msg, final Throwable cause, final boolean mustLog) {
        super(msg, cause);
        this.mustLog = mustLog;
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if the exception must be logged. */
    public boolean mustLog() {
        return mustLog;
    }

    @Override public String getMessage() {
        final String message = super.getMessage();
        return statement == null || statement.isEmpty() ? message : "\nWhile trying to execute:\n" + statement + "\n" + message;
    }

    /** Set the Statement causing the problem. */
    public void setStatement(final String statement) {
        this.statement = statement;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -6321442903710049224L;
}  // end class DatabaseException
