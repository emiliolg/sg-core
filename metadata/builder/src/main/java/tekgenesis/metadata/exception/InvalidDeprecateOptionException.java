
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
 * Exception to throw when a Widget does not supports a given feature.
 */
public class InvalidDeprecateOptionException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new exception. */
    public InvalidDeprecateOptionException(String modelName) {
        super("Cannot add deprecate action in a form bounded to a non deprecable entity", modelName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 5891111230559287268L;
}
