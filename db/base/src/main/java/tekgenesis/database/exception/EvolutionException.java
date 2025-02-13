
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.exception;

import tekgenesis.common.util.VersionString;

import static java.lang.String.format;

/**
 * Thrown when an error raises during Schema Evolution.
 */
public class EvolutionException extends DatabaseException {

    //~ Constructors .................................................................................................................................

    /** Create an evolution exception. */
    public EvolutionException(final DatabaseException cause, final String schema, final VersionString version) {
        super(format("Exception while evolving '%s' to version '%s'", schema, version), cause, cause.mustLog());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -1238962935678150941L;
}
