
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect.exception;

/**
 * Generic Exception while introspecting.
 */
public class IntrospectorException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    /** Create the exception. */
    public IntrospectorException(final Exception e) {
        super(e);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -9040771706588327621L;
}
