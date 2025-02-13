
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import static java.lang.String.format;

import static tekgenesis.metadata.exception.BuilderErrors.createError;

/**
 * PermissionNotDeclaredException.
 */
public class PermissionNotDeclaredException extends BuilderErrorException {

    //~ Instance Fields ..............................................................................................................................

    private final String model;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public PermissionNotDeclaredException(String permission, String model) {
        super(format("Permission '%s' not declared on form '%s'", permission, model));
        this.model = model;
    }

    //~ Methods ......................................................................................................................................

    @Override public BuilderError getBuilderError() {
        return createError(getMessage(), model);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 2012037284330072016L;
}
