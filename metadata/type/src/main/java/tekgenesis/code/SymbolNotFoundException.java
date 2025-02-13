
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.code;

import static tekgenesis.common.core.Constants.NOT_FOUND;
import static tekgenesis.common.core.Constants.SYMBOL;

/**
 * Thrown when a Symbol is not found in an Expression.
 */
public class SymbolNotFoundException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    /** Default contructor. */
    public SymbolNotFoundException() {}

    /** Creates the Exception. */
    public SymbolNotFoundException(String name) {
        super(SYMBOL + name + NOT_FOUND);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4919884398935185704L;
}
