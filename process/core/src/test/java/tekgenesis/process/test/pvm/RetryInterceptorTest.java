
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.test.pvm;

import org.junit.Rule;
import org.junit.Test;

import tekgenesis.common.tools.test.MuteRule;
import tekgenesis.process.engine.exception.OptimisticLockingException;
import tekgenesis.process.engine.exception.ProcessEngineException;
import tekgenesis.process.interceptor.CommandExecutorImpl;
import tekgenesis.process.interceptor.RetryInterceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

/**
 * RetryInterceptorTest.
 */
@SuppressWarnings("JavaDoc")
public class RetryInterceptorTest {

    //~ Instance Fields ..............................................................................................................................

    @Rule public MuteRule mute = new MuteRule(RetryInterceptor.class);

    //~ Methods ......................................................................................................................................

    @Test public void retryInterceptor() {
        final RetryInterceptor retryInterceptor = new RetryInterceptor();
        retryInterceptor.setNext(new CommandExecutorImpl());

        try {
            retryInterceptor.execute(commandContext -> { throw new OptimisticLockingException(""); });
            failBecauseExceptionWasNotThrown(ProcessEngineException.class);
        }
        catch (final ProcessEngineException e) {
            assertThat(e.getMessage()).contains("3 retries failed");
        }
    }
}
