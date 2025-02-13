
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
 * Exception to throw when an invalid reference is found.
 */
public class InvalidReferenceException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a InvalidReferenceException. */
    public InvalidReferenceException(String reference) {
        super("Invalid reference '" + reference + "'", reference);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -4714024816463220557L;
}
