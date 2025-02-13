
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.test;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.MuteRule;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.database.SqlStatement;
import tekgenesis.database.exception.ForeignKeyViolationException;
import tekgenesis.database.exception.NotNullViolationException;
import tekgenesis.database.exception.UniqueViolationException;
import tekgenesis.test.basic.*;

import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.test.basic.g.PreferencesTable.PREFERENCES;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class ExceptionsTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public String dbName = null;

    @Rule public MuteRule mute = new MuteRule(SqlStatement.class);
    private final DbRule  db   = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule tr = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    @Test public void duplicatedKeyInsert() {
        insertData();

        final Customer customer = Customer.create(DocType.PASS, BigDecimal.valueOf(2314), Sex.F);
        customer.setFirstName("Juana");
        customer.setLastName("Repetida");
        customer.setNickname("Lol");
        try {
            customer.insert();
            Assertions.failBecauseExceptionWasNotThrown(UniqueViolationException.class);
        }
        catch (final UniqueViolationException ignore) {}
    }

    @Test public void foreignKeyMissing() {
        insertData();

        final Customer customer = Customer.create(DocType.DNI, BigDecimal.valueOf(678), Sex.F);
        customer.setFirstName("Chris");
        customer.setLastName("Bathtub");
        customer.setNickname("Latina");

        final Preferences preferences = Preferences.create();
        preferences.setCustomer(customer);
        preferences.setDigest(MailDigest.DAILY);
        preferences.setMail("test@mail.com");
        preferences.setTwitter("@queen");
        try {
            preferences.insert();
            Assertions.failBecauseExceptionWasNotThrown(ForeignKeyViolationException.class);
        }
        catch (final ForeignKeyViolationException ignore) {}
    }

    @Test public void missingRequired() {
        insertData();

        final Preferences preferences = Preferences.create();
        final Customer    customer    = assertNotNull(Customer.find(DocType.PASS, BigDecimal.valueOf(2314), Sex.F));
        preferences.setCustomer(customer);
        PREFERENCES.MAIL.setValue(preferences, null);

        try {
            preferences.insert();
            Assertions.failBecauseExceptionWasNotThrown(NotNullViolationException.class);
        }
        catch (final NotNullViolationException ignore) {}
    }

    @Test public void uniqueUpdate() {
        insertData();

        final Customer customer = assertNotNull(Customer.find(DocType.DNI, BigDecimal.valueOf(2345), Sex.F));
        customer.setNickname("Lau");
        try {
            customer.update();
            Assertions.failBecauseExceptionWasNotThrown(UniqueViolationException.class);
        }
        catch (final UniqueViolationException ignore) {}
    }

    private void insertData() {
        final Customer customer = Customer.create(DocType.PASS, BigDecimal.valueOf(2314), Sex.F);
        customer.setFirstName("Laura");
        customer.setLastName("Sarasa");
        customer.setNickname("Lau");
        customer.insert();

        final Customer customer2 = Customer.create(DocType.DNI, BigDecimal.valueOf(2345), Sex.F);
        customer2.setFirstName("Mary");
        customer2.setLastName("Poppins");
        customer2.setNickname("Mary");
        customer2.insert();
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class ExceptionsTest
