
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.transaction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A Transaction Listener The listener will be invoked at the beginning of the transaction with an
 * {@link #EMPTY_CONTEXT} You can return your own implementation of a Context. This context is
 * afterward accessible via {@link Transaction#getContext()} and in the COMMIT and ROLLBACK
 * invocations.
 */
@FunctionalInterface public interface TransactionListener<T extends TransactionContext> {

    //~ Methods ......................................................................................................................................

    /** Invoke the Listener. */
    @NotNull T invoke(Operation operation, @Nullable T ctx);

    //~ Enums ........................................................................................................................................

    enum Operation { BEGIN, COMMIT, ROLLBACK, AFTER_COMMIT, AFTER_ROLLBACK }
}
