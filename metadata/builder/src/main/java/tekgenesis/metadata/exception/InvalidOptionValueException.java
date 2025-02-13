
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
public class InvalidOptionValueException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new exception. */
    public InvalidOptionValueException(String modelName, String option, String value) {
        super(String.format("Invalid value '%s' for option '%s'", value, option), modelName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929254380781L;
}
