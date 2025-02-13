
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
 * Exception that is thrown when an optimistic locking occurs in the data-store caused by concurrent
 * access of the same data entry.
 */
public class OptimisticLockingException extends ProcessEngineException {

    //~ Constructors .................................................................................................................................

    /** Construct a OptimisticLockingException. */
    public OptimisticLockingException(String message) {
        super(message);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1L;
}
