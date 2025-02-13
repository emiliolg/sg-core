
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.exception;

import static java.lang.String.format;

/**
 * Exception throw when required type does not match actual type in ResultSet.
 */
public class InvalidValueException extends DatabaseUnspecifiedException {

    //~ Constructors .................................................................................................................................

    /** Create the Exception. */
    public InvalidValueException(int row, int column, Object actualValue, Class<?> requiredType) {
        super(
            format("Invalid Value exception affecting row: %d, column: %d expected type: '%s' actual value: '%s'",
                row,
                column,
                requiredType,
                actualValue));
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4279725489886615624L;
}
