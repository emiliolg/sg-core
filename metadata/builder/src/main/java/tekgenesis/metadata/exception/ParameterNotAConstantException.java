
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

/**
 * Exception to throw when a parameter assignation is not a constant.
 */
public class ParameterNotAConstantException extends BuilderErrorException {

    //~ Instance Fields ..............................................................................................................................

    private final String model;

    //~ Constructors .................................................................................................................................

    /** Constructor for exception. */
    public ParameterNotAConstantException(String expression, String model) {
        super("Parameter value '" + expression + "' should be a constant!");
        this.model = model;
    }

    //~ Methods ......................................................................................................................................

    @Override public BuilderError getBuilderError() {
        return BuilderErrors.parameterNotAConstant(getMessage(), model);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1139016270504081995L;
}
