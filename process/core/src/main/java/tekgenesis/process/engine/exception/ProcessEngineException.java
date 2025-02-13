
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.engine.exception;

/**
 * Runtime exception that is the superclass of all Process Engine exceptions.
 */
public class ProcessEngineException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    /** Create ProcessEngine Exception. */
    public ProcessEngineException(String message) {
        super(message);
    }

    /** Create ProcessEngine Exception. */
    public ProcessEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8023369937942870100L;
}
