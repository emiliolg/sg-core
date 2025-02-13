
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.exception;

/**
 * Exceptions handling resources.
 */
public class ResourceException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    /** Create a ResourceException. */
    private ResourceException(String msg) {
        super(msg);
    }

    /** Create a ResourceException. */
    private ResourceException(String msg, Throwable cause) {
        super(msg, cause);
    }

    //~ Methods ......................................................................................................................................

    /** Create Exception. */
    public static ResourceException errorLoading(String resource, Throwable cause) {
        return new ResourceException("Error loading " + resource, cause);
    }

    /** Create Exception. */
    public static ResourceException notFound(String resource) {
        return new ResourceException("Resource not found " + resource);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3535551991354520659L;
}
