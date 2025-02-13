
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
 * Exception to throw when a reference can not be resolved.
 */
public class InvalidMultipleTypeException extends BuilderErrorException {

    //~ Instance Fields ..............................................................................................................................

    private final String modelName;

    //~ Constructors .................................................................................................................................

    /** Invalid multiple type. */
    public InvalidMultipleTypeException(String modelName) {
        this.modelName = modelName;
    }

    //~ Methods ......................................................................................................................................

    /** Return Sub Form Id not found BuilderError. */
    @Override public BuilderError getBuilderError() {
        return BuilderErrors.invalidMultipleType(modelName);
    }
    /** Get ModelName. */
    public String getModelName() {
        return modelName;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1139016270591905599L;
}
