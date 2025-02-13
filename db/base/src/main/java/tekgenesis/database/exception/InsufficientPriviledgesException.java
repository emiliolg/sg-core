
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.exception;

/**
 * Insufficient Priviledges Exception.
 */
@SuppressWarnings("WeakerAccess")
public class InsufficientPriviledgesException extends BadGrammarException {

    //~ Constructors .................................................................................................................................

    InsufficientPriviledgesException(Throwable cause) {
        super(cause);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 717462347021527040L;
}
