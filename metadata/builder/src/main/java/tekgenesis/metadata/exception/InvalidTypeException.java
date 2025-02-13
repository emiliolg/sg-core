
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
 * Exception to throw when a Widget does not supports a given feature.
 */
public class InvalidTypeException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Invalid type for model. */
    public InvalidTypeException(String modelName, Type type) {
        super(String.format("Invalid type '%s' for option in '%s'", type, modelName), modelName);
    }

    /** Invalid type for field. */
    public InvalidTypeException(String modelName, String fieldName, Type type) {
        super(String.format("Invalid type '%s' for field '%s'", type, fieldName), modelName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1104650367563651239L;
}
