
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
 * Exception thrown when a dynamic part has an invalid type.
 */
public class InvalidTypeForDynamicPartException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Constructor used when the Widget does not have a type. */
    public InvalidTypeForDynamicPartException(String msg, String handler) {
        super(msg, handler);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929219842014L;
}
