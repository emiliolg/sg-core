
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.repository;

/**
 * This Exception represents the main exception for the repository operations.
 */
@SuppressWarnings("WeakerAccess")
public class RepositoryException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    /** Creates a repository Exception with an init cause. */
    public RepositoryException(String message) {
        super(message);
    }

    /** Creates a repository Exception with a cause exception. */
    public RepositoryException(Throwable cause) {
        super(cause);
    }

    /** Creates a repository Exception with a cause message and cause exception. */
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -6617704003559594114L;
}
