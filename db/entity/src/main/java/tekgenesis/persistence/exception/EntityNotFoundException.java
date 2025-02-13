
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.exception;

import static java.lang.String.format;

/**
 * Exceptions handling resources.
 */
public class EntityNotFoundException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    private EntityNotFoundException(String msg) {
        super(msg);
    }

    //~ Methods ......................................................................................................................................

    /** Create Exception. */
    public static EntityNotFoundException notFound(String entityName, String key) {
        return new EntityNotFoundException(format("Entity '%s' key '%s' not found ", entityName, key));
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3535551991354520659L;
}
