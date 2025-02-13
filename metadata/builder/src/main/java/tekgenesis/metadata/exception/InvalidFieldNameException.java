
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
 * Duplicate field exception.
 */
public class InvalidFieldNameException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new invalid fieldName exception. */
    public InvalidFieldNameException(String fieldName, String modelName) {
        super(String.format("Invalid field name  '%s'", fieldName), modelName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 6033581522532077453L;
}
