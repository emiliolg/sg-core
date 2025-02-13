
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
 * Exception to throw when a form parameter assignation is not specified in the form parameters.
 */
public class ParameterNotSpecifiedException extends BuilderErrorException {

    //~ Instance Fields ..............................................................................................................................

    private final String link;

    //~ Constructors .................................................................................................................................

    /** Constructor for exception. */
    public ParameterNotSpecifiedException(String parameterName, String formName, String link) {
        super("Parameter '" + parameterName + "' not specified on form '" + formName + "'");
        this.link = link;
    }

    //~ Methods ......................................................................................................................................

    @Override public BuilderError getBuilderError() {
        return BuilderErrors.invalidMenuItemFormParameter(getMessage(), link);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1139016270531071992L;
}
