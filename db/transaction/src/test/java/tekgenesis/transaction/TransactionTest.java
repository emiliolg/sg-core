
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.transaction;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.transaction.TransactionListener.Operation.BEGIN;
import static tekgenesis.transaction.TransactionListener.Operation.COMMIT;

/**
 * Simple Transaction Tests.
 */
public class TransactionTest {

    //~ Instance Fields ..............................................................................................................................

    private int                          counter;
    private final JDBCTransactionManager tm = new JDBCTransactionManager();

    //~ Methods ......................................................................................................................................

    public void incrementCounter(TransactionListener<TransactionContext> listener) {
        tm.runInTransaction(t -> {
            t.getContext(listener);
            counter++;
        });
    }

    @Test public void invokeNestedTransaction() {
        counter = 0;
        tm.runInTransaction(t -> {
            counter++;
            t.runAfter(this::modifyCounter);
            final int n = tm.invokeInNestedTransaction(tn -> {
                    assertThat(counter).isEqualTo(1);
                    counter++;
                    tn.runAfter(this::modifyCounter);
                    return counter;
                });
            assertThat(n).isEqualTo(2);
            assertThat(counter).isEqualTo(3);
            t.abort();
        });
        assertThat(counter).isEqualTo(2);
    }
    @Test public void runAfterCommiting() {
        counter = 0;
        tm.runInTransaction(t -> {
            counter++;
            t.runAfter(this::modifyCounter);
        });
        assertThat(counter).isEqualTo(2);
    }
    @Test public void runAfterRollbacking() {
        counter = 0;
        tm.runInTransaction(t -> {
            counter++;
            t.runAfter(this::modifyCounter);
            t.abort();
        });
        assertThat(counter).isEqualTo(0);
    }

    @Test public void transactionListener() {
        counter = 0;
        final TransactionListener<TransactionContext> listener = this::transactionListener;
        tm.addListener(listener);

        // Increment counter
        incrementCounter(listener);
        assertThat(counter).isEqualTo(2);
        tm.removeListener(listener);
        incrementCounter(listener);
        assertThat(counter).isEqualTo(3);
    }

    private void modifyCounter(boolean commited) {
        if (commited) counter++;
        else counter--;
    }

    private TransactionContext transactionListener(final TransactionListener.Operation op, final TransactionContext ctx) {
        if (op == BEGIN) return TransactionContext.EMPTY_CONTEXT;
        if (op == COMMIT) counter++;
        return ctx;
    }
}  // end class TransactionTest
