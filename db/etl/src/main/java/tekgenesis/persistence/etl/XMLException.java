
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

/**
 * Wrapper for XML Processing Exceptions.
 */
@SuppressWarnings("WeakerAccess")
public class XMLException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    /** Create an XMLException. */
    public XMLException(Exception e) {
        super(e);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8679541279965775084L;
}
