
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
 * Exception to throw when a path incompatibility is detected.
 */
public class IncompatiblePathException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new invalid path exception. */
    public IncompatiblePathException(String msg, String path, String modelName) {
        super(String.format("Incompatible route '%s'. " + msg, path), modelName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929254380781L;
}
