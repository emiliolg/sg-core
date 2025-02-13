
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
 * Database exception thrown when a result set did not have the correct column count, for example
 * when expecting a single column but getting 0 or more than 1 columns.
 */
public class ResultSetColumnCountException extends DatabaseUnspecifiedException {

    //~ Constructors .................................................................................................................................

    /**
     * Constructor for IncorrectResultSetColumnCountException.
     *
     * @param  expectedCount  the expected column count
     * @param  actualCount    the actual column count
     */
    public ResultSetColumnCountException(int expectedCount, int actualCount) {
        super("Incorrect column count: expected " + expectedCount + ", actual " + actualCount);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8867446570217903934L;
}
