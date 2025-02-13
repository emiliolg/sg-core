
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import static java.lang.String.format;

/**
 * Exception to throw when a path collision is detected.
 */
public class RaisingANonExceptionEnum extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new invalid Raising a non exception enum exception. */
    public RaisingANonExceptionEnum(String enumName, String modelName) {
        super(format("%s must be an exception enum to be risen", enumName), modelName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3608700670647945112L;
}
