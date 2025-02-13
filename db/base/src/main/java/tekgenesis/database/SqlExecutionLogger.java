
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.util.function.Supplier;

import tekgenesis.common.logging.Logger;
import tekgenesis.transaction.TransactionContext;
import tekgenesis.transaction.TransactionListener;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

import static tekgenesis.transaction.TransactionListener.Operation.AFTER_COMMIT;
import static tekgenesis.transaction.TransactionListener.Operation.AFTER_ROLLBACK;

/**
 * Container of the logger for statement execution.
 */
public abstract class SqlExecutionLogger {

    //~ Methods ......................................................................................................................................

    /** Log The execution of an Sql Statement. */
    public static int logExecution(String statement, Supplier<Integer> method) {
        logger.info(EXECUTING + statement);
        final long ts = currentTimeMillis();
        try {
            return method.get();
        }
        finally {
            logger.info(format("Executed in %d milliseconds", currentTimeMillis() - ts));
        }
    }

    /** Set transaction Listener if not set. */
    public static void setTransactionListener(Database database) {
        database.getTransactionManager().addFirstListener(logListener);
    }

    //~ Static Fields ................................................................................................................................

    public static final String                                   EXECUTING   = "Executing: ";
    public static final Logger                                   logger      = Logger.getLogger(SqlExecutionLogger.class);
    private static final TransactionListener<LogListenerContext> logListener = (operation, ctx) -> {
                                                                                   if (ctx == null) return new LogListenerContext();
                                                                                   ctx.logEndTransaction(operation);
                                                                                   return ctx;
                                                                               };

    //~ Inner Classes ................................................................................................................................

    static class LogListenerContext implements TransactionContext {
        private final long startTime;

        LogListenerContext() {
            startTime = System.currentTimeMillis();
            logger.info("Starting transaction");
        }

        void logEndTransaction(TransactionListener.Operation operation) {
            final String op = operation == AFTER_COMMIT ? "Committed" : operation == AFTER_ROLLBACK ? "Aborted" : "";
            if (op.isEmpty()) return;
            logger.info(format("Transaction %s after %d milliseconds", op, currentTimeMillis() - startTime));
        }
    }
}  // end class SqlExecutionLogger
