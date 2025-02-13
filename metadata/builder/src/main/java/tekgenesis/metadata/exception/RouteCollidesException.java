
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
 * Colliding route exception.
 */
public class RouteCollidesException extends BuilderException {

    //~ Constructors .................................................................................................................................

    RouteCollidesException(String path, String handler) {
        super("Route '" + path + "' is already defined in scope", handler);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929214042014L;
}
