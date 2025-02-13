
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.transaction;

/**
 * Exception thrown when a leak is detected.
 */
public class TransactionUnavailableException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    TransactionUnavailableException() {
        super("Not Current Transaction Exception");
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -7603302380603524184L;
}
