
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
 * Exception thrown when an option is duplicated in the same field.
 */
public class DuplicateOptionException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Exception constructor. */
    public DuplicateOptionException(String option) {
        super("Adding duplicate option '" + option + "'", "");
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3048393441342980605L;
}
