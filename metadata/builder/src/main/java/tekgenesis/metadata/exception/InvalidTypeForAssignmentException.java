
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
 * Exception to throw when a type of a parameter assignation is invalid.
 */
public class InvalidTypeForAssignmentException extends BuilderErrorException {

    //~ Instance Fields ..............................................................................................................................

    private final String model;

    //~ Constructors .................................................................................................................................

    /** Constructor for exception. */
    public InvalidTypeForAssignmentException(String parameter, Type fieldType, Type expressionType, String model) {
        super(String.format("Parameter '%s' expected type '%s' but type '%s' found.", parameter, fieldType, expressionType));
        this.model = model;
    }

    //~ Methods ......................................................................................................................................

    @Override public BuilderError getBuilderError() {
        return BuilderErrors.invalidTypeForParameter(getMessage(), model);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1139016270504921995L;
}
