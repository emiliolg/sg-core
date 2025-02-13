
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
 * Interface for all Errors that originate in Builder.
 */
public abstract class BuilderErrorException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    protected BuilderErrorException() {}

    protected BuilderErrorException(String message) {
        super(message);
    }

    //~ Methods ......................................................................................................................................

    /** Return the exception as BuilderError. */
    public abstract BuilderError getBuilderError();

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2947070430961459581L;
}
