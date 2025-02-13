
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
 * A Context that can be used to store data for the current transaction.
 */
public interface TransactionContext {

    //~ Instance Fields ..............................................................................................................................

    @SuppressWarnings("unused")
    TransactionContext EMPTY_CONTEXT = new TransactionContext() {};

    //~ Methods ......................................................................................................................................

    /** Get Current Context. */
    static <T extends TransactionContext> T getCurrent(TransactionListener<T> l) {
        return Transaction.getTransactionManager().getOrCreateTransaction().getContext(l);
    }
}
