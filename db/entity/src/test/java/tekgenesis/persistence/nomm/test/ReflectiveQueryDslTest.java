
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.tools.test.DatabaseRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.persistence.TableField;
import tekgenesis.persistence.TableMetadata;
import tekgenesis.persistence.nomm.model.Address;
import tekgenesis.persistence.nomm.model.AddressForUpdate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.persistence.nomm.model.AddressTable.ADDRESS;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "SpellCheckingInspection" })
public class ReflectiveQueryDslTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    private final DatabaseRule db = new NoMmDbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule tr = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    @Test public void simplePureReflectivePersistenceTest() {
        final TableMetadata<Address, String> base    = TableMetadata.forName("tekgenesis.persistence.nomm.model.Address");
        final AddressForUpdate               address = AddressForUpdate.addressForUpdate(base.createInstance("1234567"));

        final Option<TableField.Str> r = base.getField("room").castTo(TableField.Str.class);
        r.ifPresent(room -> room.setValue(address, "12367"));
        base.getField("street").castTo(TableField.Str.class).ifPresent(street -> street.setValue(address, "12367"));
        address.insert();

        final Address storedAddress = assertNotNull(Address.find("1234567"));
        assertThat(storedAddress.getCode()).isEqualTo("1234567");

        assertThat(storedAddress.getStreet()).isEqualTo("12367");

        r.ifPresent(room -> {
            assertThat(room.getValue(storedAddress)).isEqualTo("12367");
            room.setValue(storedAddress, "123456789.123456789.123456789.12345");
            assertThat(room.getValueAsString(storedAddress)).isEqualTo("123456789.123456789.123456789.");
        });
        assertThat(storedAddress.getRoom()).isEqualTo("123456789.123456789.123456789.");
    }

    @Test public void simpleReflectivePersistenceTest() {
        final TableMetadata<Address, String> table = ADDRESS.metadata();

        final AddressForUpdate address = AddressForUpdate.addressForUpdate(table.createInstance("12345"));
        ADDRESS.ROOM.setValue(address, "123");
        ADDRESS.STREET.setValue(address, "123456789.123456789.123456789.12345");

        address.insert();

        final Address storedAddress = assertNotNull(Address.find("12345"));
        assertThat(storedAddress.getCode()).isEqualTo("12345");
        assertThat(storedAddress.getRoom()).isEqualTo("123");
        assertThat(storedAddress.getStreet()).isEqualTo("123456789.123456789.123456789.");
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class ReflectiveQueryDslTest
