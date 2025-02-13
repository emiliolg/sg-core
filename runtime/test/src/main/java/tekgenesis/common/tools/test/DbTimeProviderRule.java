
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.tools.test;

import java.util.function.LongSupplier;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import tekgenesis.common.core.DateTime;
import tekgenesis.database.Database;

/**
 * DB Timer provider Rule.
 */
public class DbTimeProviderRule extends TimeProviderRule {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public DbTimeProviderRule() {}
    /** Constructor with initial DateTime. */
    public DbTimeProviderRule(DateTime current) {
        super(current);
    }

    //~ Methods ......................................................................................................................................

    @Override public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override public void evaluate()
                throws Throwable
            {
                final LongSupplier oldProvider = DateTime.setTimeSupplier(DbTimeProviderRule.this::getCurrentTime);
                Database.setUseClientTime(true);
                try {
                    base.evaluate();
                }
                finally {
                    DateTime.setTimeSupplier(oldProvider);
                    Database.setUseClientTime(false);
                }
            }
        };
    }
}
