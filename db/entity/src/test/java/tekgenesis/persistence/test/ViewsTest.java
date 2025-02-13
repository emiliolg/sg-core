
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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.test.basic.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.test.basic.g.CustomerSqlViewTable.CUSTOMER_SQL_VIEW;
import static tekgenesis.test.basic.g.CustomerTable.CUSTOMER;
import static tekgenesis.test.basic.g.MiniCustomerTable.MINI_CUSTOMER;
import static tekgenesis.test.basic.g.MiniCustomerUpdatableTable.MINI_CUSTOMER_UPDATABLE;
import static tekgenesis.test.basic.g.MiniPreferencesTable.MINI_PREFERENCES;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class ViewsTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;
    private final DbRule     db     = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule t = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    @Test public void testSqlTests() {
        insertData();
        assertThat(selectFrom(CUSTOMER).count()).isEqualTo(3);
        assertThat(selectFrom(CUSTOMER_SQL_VIEW).count()).isEqualTo(1);
        final CustomerSqlView customerSqlView = assertNotNull(selectFrom(CUSTOMER_SQL_VIEW).get());

        assertThat(customerSqlView.getFirstName()).isEqualTo("Johnny");
        assertThat(customerSqlView.getLastName()).isEqualTo("Bravo");
        assertThat(customerSqlView.getMail()).isEqualTo("johnny@mail.com");
    }

    @Test public void testViewsCompositeKeyAttribute() {
        insertData();

        assertThat(selectFrom(CUSTOMER).count()).isEqualTo(3);

        final Customer vilma = assertNotNull(Customer.find(DocType.DNI, new BigDecimal(2345), Sex.F));

        final Preferences pref = Preferences.create();
        pref.setCustomer(vilma);
        pref.setMail("sarasa@sarasategui.com");
        pref.setTwitter("@vilmaf");
        pref.setDigest(MailDigest.WEEKLY);
        pref.insert();

        final ImmutableList<MiniPreferences> list = selectFrom(MINI_PREFERENCES).list();
        assertThat(list).hasSize(2);

        assertThat(list).extracting("correo").contains("sarasa@sarasategui.com");
    }
    @Test public void testViewsReadOnly() {
        insertData();

        assertThat(selectFrom(CUSTOMER).count()).isEqualTo(3);

        assertThat(selectFrom(MINI_CUSTOMER).count()).isEqualTo(3);

        final MiniCustomer vilma = assertNotNull(MiniCustomer.find(DocType.DNI, new BigDecimal(2345), Sex.F));

        assertThat(vilma.getApellido()).isEqualTo("Flintstone");
        assertThat(vilma.getNombre()).isEqualTo("Vilma");
    }

    @Test public void testViewsUpdatable() {
        insertData();

        assertThat(selectFrom(CUSTOMER).count()).isEqualTo(3);

        assertThat(selectFrom(MINI_CUSTOMER_UPDATABLE).count()).isEqualTo(3);

        final MiniCustomerUpdatable vilma = assertNotNull(MiniCustomerUpdatable.find(DocType.DNI, new BigDecimal(2345), Sex.F));
        assertThat(vilma.getApellido()).isEqualTo("Flintstone");
        assertThat(vilma.getNombre()).isEqualTo("Vilma");

        vilma.setApellido("Palma");
        vilma.update();

        final Customer vilmaUpdated = assertNotNull(Customer.find(DocType.DNI, new BigDecimal(2345), Sex.F));
        assertThat(vilmaUpdated.getLastName()).isEqualTo("Palma");
    }

    private void insertData() {
        final Customer customer = Customer.create(DocType.PASS, new BigDecimal(2314), Sex.M);
        customer.setFirstName("Johnny");
        customer.setLastName("Bravo");
        customer.setNickname("JB");
        customer.insert();

        final Preferences pref = Preferences.create();
        pref.setCustomer(customer);
        pref.setMail("johnny@mail.com");
        pref.setTwitter("@johnny");
        pref.setDigest(MailDigest.WEEKLY);
        pref.insert();

        final Customer customer2 = Customer.create(DocType.DNI, new BigDecimal(2345), Sex.F);
        customer2.setFirstName("Vilma");
        customer2.setLastName("Flintstone");
        customer2.setNickname("VF");
        customer2.insert();

        final Customer customer3 = Customer.create(DocType.DNI, new BigDecimal(1231), Sex.F);
        customer3.setFirstName("Emma");
        customer3.setLastName("Peel");
        customer3.setNickname("EP");
        customer3.insert();
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class ViewsTest
