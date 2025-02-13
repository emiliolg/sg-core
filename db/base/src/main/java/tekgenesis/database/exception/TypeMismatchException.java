
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
 * Exception throw when required type does not match actual type in ResultSet.
 */
public class TypeMismatchException extends DatabaseUnspecifiedException {

    //~ Constructors .................................................................................................................................

    /** Create the Exception. */
    public TypeMismatchException(int row, int column, Class<?> actualType, Class<?> requiredType) {
        super(
            "Type mismatch affecting row: " + row + ", column: " + column + " expected type: '" + requiredType + "' actual type: '" + actualType +
            "'");
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4279725489886615624L;
}
