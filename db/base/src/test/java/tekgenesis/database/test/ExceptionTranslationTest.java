
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.test;

import org.assertj.core.api.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.*;
import tekgenesis.database.SqlStatement;
import tekgenesis.database.SqlStatement.Prepared;
import tekgenesis.database.exception.BadGrammarException;
import tekgenesis.database.exception.NotNullViolationException;
import tekgenesis.database.exception.UniqueViolationException;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Test Exception Translation;
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class ExceptionTranslationTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    @Rule public MuteRule mute = new MuteRule(SqlStatement.class);

    private final DatabaseRule db = new DatabaseRule() {
            @Override protected void before() {
                runInTransaction(() -> {
                    createDatabase(dbName);

                    sqlStatement(
                        "create table ERRORSTEST (\n" +
                        "FIRST_NAME nvarchar(20) not null,\n" +
                        "LAST_NAME nvarchar(256) not null,\n" +
                        "DOCUMENT_NUMBER int not null,\n" +
                        "EMAIL nvarchar(256),\n" +
                        "constraint PK_ERRORSTEST primary key(DOCUMENT_NUMBER),\n" +
                        "constraint ERRORSTEST_UNQT unique(LAST_NAME))\n").execute();
                });
            }

            @Override protected void after() {
                runInTransaction(() -> sqlStatement("drop table ERRORSTEST").execute());
            }
        };

    @Rule public TestRule chain = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    @Test public void badGrammar() {
        try(final Prepared wrongInsert = db.sqlStatement("insert into ERRORSTEST valques (?,?,?,?)").prepare()) {
            wrongInsert.onArgs("Pepe", "Castro", 123123123, "pepe@mail.com").executeDml();
            failBecauseExceptionWasNotThrown(BadGrammarException.class);
        }
        catch (final BadGrammarException ignored) {}
    }

    @Test public void constraintViolationDuplicatePK() {
        final Prepared insert = insertData();

        try {
            insert.onArgs("Rea", "Sanka", 123, "sanka@mail.com").executeDml();
            Assertions.failBecauseExceptionWasNotThrown(UniqueViolationException.class);
        }
        catch (final UniqueViolationException ignored) {}
        insert.close();
    }

    @Test public void constraintViolationNotNull() {
        try(final Prepared insert = insertData()) {
            insert.onArgs("", null, 2342, "john@dou.com").executeDml();
            Assertions.failBecauseExceptionWasNotThrown(NotNullViolationException.class);
        }
        catch (final NotNullViolationException ignored) {}
    }

    @Test public void constraintViolationUnique() {
        final Prepared insert = insertData();

        try {
            insert.onArgs("Rea", "Castro", 1234, "sanka@mail.com").executeDml();
            Assertions.failBecauseExceptionWasNotThrown(UniqueViolationException.class);
        }
        catch (final UniqueViolationException ignored) {}
        insert.close();
    }

    @Test public void invalidTableName() {
        try(final Prepared wrongInsert = db.sqlStatement("insert into ZOMGWTFBBQ values (?,?,?,?)").prepare()) {
            wrongInsert.onArgs("Pepe", "Castro", 4321, "pepe@mail.com").executeDml();
            failBecauseExceptionWasNotThrown(BadGrammarException.class);
        }
        catch (final BadGrammarException ignored) {}
    }

    private Prepared insertData() {
        final Prepared insert = db.sqlStatement("insert into ERRORSTEST values (?,?,?,?)").prepare();
        insert.onArgs("Pepe", "Castro", 123, "pepe@mail.com").executeDml();

        final Prepared count = db.sqlStatement("select count(*) from ERRORSTEST").prepare();
        assertThat(count.getInt()).isEqualTo(1);
        count.close();
        return insert;
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "{0}")
    public static Seq<Object[]> listFiles() {
        return DbTests.listDatabases();
    }
}  // end class ExceptionTranslationTest
