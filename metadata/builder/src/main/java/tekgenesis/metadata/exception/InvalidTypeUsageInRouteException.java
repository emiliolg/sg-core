
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
public class InvalidTypeUsageInRouteException extends BuilderErrorException {

    //~ Instance Fields ..............................................................................................................................

    private final String model;

    //~ Constructors .................................................................................................................................

    /** Constructor for exception. */
    private InvalidTypeUsageInRouteException(String type, String route, String model) {
        super(String.format("Local type '%s' used in route %s. Local types are not allowed.", type, route));
        this.model = model;
    }

    //~ Methods ......................................................................................................................................

    @Override public BuilderError getBuilderError() {
        return BuilderErrors.createError(getMessage(), model);
    }

    //~ Methods ......................................................................................................................................

    /** Factory method for InvalidTypeForStructFieldException. */
    public static InvalidTypeUsageInRouteException invalidTypeUsageInRouteException(String type, String route, String model) {
        return new InvalidTypeUsageInRouteException(type, route, model);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1139016270504921995L;
}
