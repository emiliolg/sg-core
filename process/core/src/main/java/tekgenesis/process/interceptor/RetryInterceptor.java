
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.interceptor;

import tekgenesis.common.logging.Logger;
import tekgenesis.process.engine.exception.OptimisticLockingException;
import tekgenesis.process.engine.exception.ProcessEngineException;

/**
 * Intercepts {@link OptimisticLockingException} and tries to run the same command again. The number
 * of retries and the time waited between retries is configurable.
 */
@SuppressWarnings("FieldCanBeLocal")
public class RetryInterceptor extends CommandInterceptor {

    //~ Instance Fields ..............................................................................................................................

    // Todo use PropertySet
    private final int numOfRetries       = 3;
    private final int waitIncreaseFactor = 5;
    private final int waitTimeInMs       = DEFAULT_WAIT_TIME;

    //~ Methods ......................................................................................................................................

    public <T> T execute(Command<T> command) {
        long waitTime       = waitTimeInMs;
        int  failedAttempts = 0;

        do {
            if (failedAttempts > 0) {
                log.info("Waiting for " + waitTime + "ms before retrying the command.");
                waitBeforeRetry(waitTime);
                waitTime *= waitIncreaseFactor;
            }

            try {
                // try to execute the command
                return next.execute(command);
            }
            catch (final OptimisticLockingException e) {
                log.info("Caught optimistic locking exception: " + e);
            }

            failedAttempts++;
        }
        while (failedAttempts <= numOfRetries);

        throw new ProcessEngineException(numOfRetries + " retries failed with OptimisticLockingException. Giving up.");
    }

    private void waitBeforeRetry(long waitTime) {
        try {
            Thread.sleep(waitTime);
        }
        catch (final InterruptedException e) {
            log.info("I am interrupted while waiting for a retry.");
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final int DEFAULT_WAIT_TIME = 50;

    private static final Logger log = Logger.getLogger(RetryInterceptor.class);
}  // end class RetryInterceptor
