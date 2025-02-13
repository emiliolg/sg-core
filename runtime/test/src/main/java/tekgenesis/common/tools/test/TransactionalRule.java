
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.tools.test;

import org.jetbrains.annotations.NotNull;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Rule to execute Database tests running inside a transaction.
 */
public class TransactionalRule implements TestRule {

    //~ Methods ......................................................................................................................................

    public final Statement apply(@NotNull final Statement base, Description description) {
        return new Statement() {
            @Override public void evaluate()
                throws Throwable
            {
                try {
                    runInTransaction(() -> {
                        try {
                            base.evaluate();
                        }
                        catch (final Throwable e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                catch (final RuntimeException e) {
                    throw e.getCause();
                }
            }
        };
    }

    //~ Methods ......................................................................................................................................

    /**
     * Create a new {@link RuleChain}, which runs the Transactional Rule into the specified rule.
     */
    public static RuleChain into(TestRule enclosedRule) {
        return RuleChain.outerRule(enclosedRule).around(new TransactionalRule());
    }
}
