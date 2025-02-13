
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import tekgenesis.type.Type;

/**
 * Exception to throw when a struct field doesn't allow a determined type in its fields.
 */
public class InvalidTypeForStructFieldException extends BuilderErrorException {

    //~ Instance Fields ..............................................................................................................................

    private final String model;

    //~ Constructors .................................................................................................................................

    /** Constructor for exception. */
    private InvalidTypeForStructFieldException(String fieldName, Type fieldType, String model) {
        super(String.format("Invalid type '%s' for a field '%s'. Entities are not allowed", fieldType, fieldName));
        this.model = model;
    }

    //~ Methods ......................................................................................................................................

    @Override public BuilderError getBuilderError() {
        return BuilderErrors.createError(getMessage(), model);
    }

    //~ Methods ......................................................................................................................................

    /** Factory method for InvalidTypeForStructFieldException. */
    public static InvalidTypeForStructFieldException invalidTypeForStructFieldException(String fieldName, Type fieldType, String model) {
        return new InvalidTypeForStructFieldException(fieldName, fieldType, model);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1139016270504921995L;
}
