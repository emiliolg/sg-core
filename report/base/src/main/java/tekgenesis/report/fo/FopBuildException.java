
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo;

/**
 * FopBuildException.
 */
public class FopBuildException extends Exception {

    //~ Constructors .................................................................................................................................

    /** Exception with message. */
    public FopBuildException(String message) {
        super(message);
    }

    /** Exception with base cause. */
    public FopBuildException(Throwable cause) {
        super(cause);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 467303549500257017L;
}
