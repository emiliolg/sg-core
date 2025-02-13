
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
import java.util.TimeZone;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Strings;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.common.util.CalendarUtils;
import tekgenesis.persistence.QueryTuple;
import tekgenesis.test.basic.BasicTypes;
import tekgenesis.test.basic.g.BasicTypesTable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.DateOnly.date;
import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.persistence.Sql.select;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.test.basic.g.BasicTypesBase.create;
import static tekgenesis.test.basic.g.BasicTypesTable.BASIC_TYPES;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class BasicTypesTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;
    private final DbRule     db     = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule t = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    @Test public void basicTypes() {
        final String   name            = "basic";
        final DateOnly currentDateOnly = DateOnly.current();
        final DateTime currentDate     = DateTime.fromMilliseconds(1234567032);

        final String veryLongName = Strings.nChars('a', 8000);

        // Create a record;
        final BasicTypes basic = BasicTypes.create(1);
        basic.setName(name);
        basic.setBool(true);
        basic.setDate(currentDateOnly);
        basic.setReal(10d);
        final BigDecimal decimal = BigDecimal.valueOf(20.4231);
        basic.setDecimal(decimal);
        basic.setDateTime(currentDate);
        basic.setLongName(veryLongName);
        basic.insert();

        // Read and validate;
        final BasicTypes b = assertNotNull(BasicTypes.find(1));
        assertThat(b.getName()).isEqualTo(name);
        assertThat(b.isBool()).isTrue();
        assertThat(b.getDate()).isEqualTo(currentDateOnly);
        assertThat(b.getReal()).isEqualTo(10d);
        assertThat(b.getDecimal()).isEqualTo(decimal);
        assertThat(b.getDateTime()).isEqualTo(currentDate);
        assertThat(b.getLongName()).isEqualTo(veryLongName);
        assertThat(b.getLongNullableName()).isNull();

        final BasicTypesTable BT = BASIC_TYPES;
        assertThat(select(BT.LONG_NAME.count()).from(BASIC_TYPES).get()).isEqualTo(1);
        assertThat(selectFrom(BASIC_TYPES).where(BT.REAL.le(20)).list().size()).isEqualTo(1);

        assertThat(select(BT.DATE_TIME).from(BASIC_TYPES).get()).isEqualTo(currentDate);
        final QueryTuple t = assertNotNull(select(BT.DATE_TIME, BT.LONG_NAME).from(BASIC_TYPES).get());
        assertThat(assertNotNull(t.get(BT.DATE_TIME))).hasSameClassAs(currentDate);
        assertThat(selectFrom(BASIC_TYPES).where(BT.DATE_TIME.eq(currentDate)).count()).isEqualTo(1);
    }  // end method basicTypes

    @Test public void basicTypesDateOnly() {
        listOf(date(1960, 6, 29), date(1963, 10, 1)).forEach(this::testFor);
    }

    @Test public void basicTypesMerge() {
        final String     name            = "basic";
        final DateOnly   currentDateOnly = DateOnly.current();
        final BigDecimal decimal         = BigDecimal.valueOf(20.4231);
        final DateTime   currentDate     = DateTime.fromMilliseconds(1234567032);

        final String veryLongName = Strings.nChars('a', 5000);

        // Create a record;
        final BasicTypes basic = BasicTypes.create(1);
        basic.setName(name);
        basic.setBool(true);
        basic.setDate(currentDateOnly);
        basic.setReal(10d);
        basic.setDecimal(decimal);
        basic.setDateTime(currentDate);
        basic.setLongName(veryLongName);
        basic.merge();

        // Read and validate;
        final BasicTypes b = assertNotNull(BasicTypes.find(1));
        assertThat(b.getName()).isEqualTo(name);
        assertThat(b.isBool()).isTrue();
        assertThat(b.getDate()).isEqualTo(currentDateOnly);
        assertThat(b.getReal()).isEqualTo(10d);
        assertThat(b.getDecimal()).isEqualTo(decimal);
        assertThat(b.getDateTime()).isEqualTo(currentDate);
        assertThat(b.getLongName()).isEqualTo(veryLongName);

        final BasicTypesTable BT = BASIC_TYPES;
        assertThat(select(BT.LONG_NAME.count()).from(BASIC_TYPES).get()).isEqualTo(1);

        assertThat(select(BT.DATE_TIME).from(BASIC_TYPES).get()).isEqualTo(currentDate);
        final QueryTuple t = assertNotNull(select(BT.DATE_TIME, BT.LONG_NAME).from(BASIC_TYPES).get());
        assertThat(assertNotNull(t.get(BT.DATE_TIME))).hasSameClassAs(currentDate);
        assertThat(selectFrom(BASIC_TYPES).where(BT.DATE_TIME.eq(currentDate)).count()).isEqualTo(1);
    }

    @Test public void dateOnlyFromParts() {
        final DateOnly currentDateOnly = date(1960, 6, 29);

        // Create a record;
        final BasicTypes basic = BasicTypes.create(1);
        basic.setDate(currentDateOnly);
        basic.insert();

        // Read and validate;
        final BasicTypes actual = assertNotNull(BasicTypes.find(1));
        assertThat(actual.getDate()).isEqualTo(currentDateOnly);

        assertThat(select(BASIC_TYPES.LONG_NAME.count()).from(BASIC_TYPES).get()).isEqualTo(1);
    }

    @Test public void differentTimezones() {
        final DateTime currentDate = DateTime.dateTime(1977, 9, 27, 11, 0);
        CalendarUtils.setSessionTimeZoneOffset(TimeZone.getDefault().getOffset(currentDate.toMilliseconds()) / 3600000 + 10);

        // Create a record;
        final BasicTypes basic = BasicTypes.create(1);
        basic.setDateTime(currentDate);
        basic.insert();

        // Read and validate;
        final DateTime time = assertNotNull(BasicTypes.find(1)).getDateTime();
        assertThat(time).isEqualTo(currentDate);
        assertThat(time.getDay()).isEqualTo(27);
        assertThat(time.getMonth()).isEqualTo(9);
        assertThat(time.getHours()).isEqualTo(21);
        assertThat(time.getMinutes()).isEqualTo(0);
    }

    public void testFor(DateOnly currentDateOnly) {
        // Create a record;
        runInTransaction(() -> {
            final BasicTypes basic = create(1);
            basic.setDate(currentDateOnly);
            basic.insert();
        });

        // Read and validate;
        runInTransaction(() -> {
            final BasicTypes basic = assertNotNull(BasicTypes.find(1));
            assertThat(basic.getDate()).isEqualTo(currentDateOnly);
            basic.delete();
        });
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class BasicTypesTest
