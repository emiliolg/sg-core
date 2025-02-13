
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.StepResult;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.tools.test.*;
import tekgenesis.database.*;
import tekgenesis.database.annotations.DbColumn;
import tekgenesis.database.exception.BadGrammarException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.Predefined.hashCodeAll;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.database.DatabaseType.ORACLE;
import static tekgenesis.database.DbMacro.SeqNextVal;
import static tekgenesis.database.test.StatementTest.Entry.entry;
import static tekgenesis.database.test.StatementTest.Entry2.entry2;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Test Statement execution;
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class StatementTest {

    //~ Instance Fields ..............................................................................................................................

    // private Database db = null;

    @NotNull @Parameter public String dbName = "";

    private boolean nextVal;

    private final DatabaseRule db = new DatabaseRule() {
            @Override protected void before() {
                runInTransaction(() -> {
                    createDatabase(dbName);
                    final DatabaseType t = db.getDatabaseType();

                    nextVal = t.has(SeqNextVal);

                    sqlStatement(
                        "create table TEST (" +
                        "  CODE int primary key, NAME varchar(50), COLOR int," +
                        "  X_CODE varchar(20), X_NAME varchar(50), " +
                        "  BIG    clob" +
                        ")").execute();
                    sqlStatement("create sequence ID_SEQ start with %s increment by 1", t == ORACLE ? "0 minvalue 0" : "1").execute();
                    sqlStatement("create table ID (ID Serial(1,ID_SEQ) not null, N int)").execute();
                });
            }

            @Override protected void after() {
                runInTransaction(() -> {
                    sqlStatement("drop table TEST").execute();
                    sqlStatement("drop table ID").execute();
                    sqlStatement("drop sequence ID_SEQ").execute();
                });
            }
        };

    private final MuteRule mute  = new MuteRule();
    @Rule public TestRule  chain = TransactionalRule.into(db).around(mute);

    // private DatabaseChangeRegistration dcr  = null;

    //~ Methods ......................................................................................................................................

    @Test public void basic() {
        assertThat(db.sqlStatement("select count(*) from TEST").getInt()).isZero();

        assertThat(db.sqlStatement("select 1 from TEST").getInt()).isEqualTo(null);

        final int n = db.sqlStatement("insert into TEST values (?,?,?,?,?,?)").onArgs(1, "Hello", Color.RED, "1", "Hello", "Big one").executeDml();
        assertThat(n).isEqualTo(1);

        final Integer c = db.sqlStatement("select max(CODE) from TEST").getInt();
        assertThat(c).isEqualTo(1);

        assertThat(db.sqlStatement("select count(*) from TEST").getInt()).isEqualTo(1);

        assertThat(db.sqlStatement("select NAME from TEST").get(String.class)).isEqualTo("Hello");

        assertThat(db.sqlStatement("select * from TEST").get(Entry.rowMapper())).isEqualTo(entry(1, "Hello"));

        // Use Reflective Object mapping;
        assertThat(db.sqlStatement("select * from TEST").get(Entry2.class)).isEqualTo(entry2(1, "Hello", Color.RED));

        // Use HasRowMapper;
        assertThat(db.sqlStatement("select * from TEST").get(Entry.class)).isEqualTo(entry(1, "Hello"));
    }

    @Test public void currentTime() {
        final long     expected = DateTime.currentTimeMillis();
        final DateTime current  = db.getDatabase().currentTime();
        System.out.println("timestamp = " + current + ", current = " + DateTime.current());
        assertThat(current.toMilliseconds()).isBetween(expected - 10_000, expected + 10_000);
    }

    @Test public void generatedId() {
        assertCount(0);

        try(final SqlStatement.Prepared ins = db.sqlStatement(
                                                    nextVal ? "insert into ID(ID, N) values(SeqNextVal(ID_SEQ), ?)" : "insert into ID(N) values (?)")
                                                .returnKeys("ID")
                                                .prepare())
        {
            final List<Long> returnedIds = new ArrayList<>();
            for (int i = 0; i < 10; i++)
                returnedIds.add(ins.onArgs(i + 100).insertWithKey());
            validateGeneration(returnedIds);
        }
    }

    @Test public void generatedIdBatch() {
        assertCount(0);
        try(final SqlStatement.Prepared ins = db.sqlStatement(
                                                    nextVal ? "insert into ID(ID, N) values(SeqNextVal(ID_SEQ), ?)" : "insert into ID(N) values (?)")
                                                .returnKeys("ID")
                                                .prepare())
        {
            for (int i = 0; i < 10; i++)
                ins.onArgs(i + 100).batch();
            validateGeneration(ins.executeBatchWithKeys());
        }
    }

    @Test public void list() {
        assertThat(db.sqlStatement("select count(*) from TEST").getInt()).isZero();

        db.sqlStatement("insert into TEST values (?,?,?,?,?,?)").onArgs(1, "Hello", 0, 11, "World", "of my dreams").executeDml();
        db.sqlStatement("insert into TEST values (?,?,?,?,?,?)").onArgs(2, "Good Bye", 1, 12, "Cruel World", "").executeDml();

        assertThat(db.sqlStatement("select count(*) from TEST").getInt()).isEqualTo(2);

        assertThat(db.sqlStatement("select NAME from TEST").list(String.class)).containsExactly("Hello", "Good Bye");
        assertThat(db.sqlStatement("select NAME from TEST").limit(1).list(String.class)).containsExactly("Hello");

        final int    code = 1;
        final String name = "Hello";
        assertThat(db.sqlStatement("select * from TEST").get(Entry.rowMapper())).isEqualTo(entry(code, name));
        assertThat(db.sqlStatement("select * from TEST").list(Entry.rowMapper())).containsExactly(entry(1, "Hello"), entry(2, "Good Bye"));

        // Use Reflective Object mapping;
        assertThat(db.sqlStatement("select * from TEST").list(Entry2.class)).containsExactly(entry2(1, "Hello", Color.RED),
            entry2(2, "Good Bye", Color.BLUE));

        // Use HasRowMapper;
        assertThat(db.sqlStatement("select * from TEST").list(Entry.class)).containsExactly(entry(1, "Hello"), entry(2, "Good Bye"));

        // Use ForEach;
        db.sqlStatement("select * from TEST where CODE = 1").forEach(rs -> {
            assertThat(rs.getInt(1)).isEqualTo(1);
            assertThat(rs.getString(2)).isEqualTo("Hello");
            // final InputStream clob = JdbcUtils.getColValue(rs, 6, InputStream.class);
            // StringWriter  sw = new StringWriter();
            // if (clob != null)
            // Files.copy(new InputStreamReader(clob), sw);
            // assertThat(sw.toString()).isEqualTo("of my dreams");
            return StepResult.next();
        });

        // Use ForEach for update
        db.sqlStatement("select CODE, NAME from TEST where CODE = 1").withUpdatableResultSet().forEach(rs -> {
            rs.updateString(2, rs.getString(2) + " World");
            rs.updateRow();
            return StepResult.next();
        });
        assertThat(db.sqlStatement("select NAME from TEST where CODE = 1").get(String.class)).isEqualTo("Hello World");

        // Update more than 1
        db.sqlStatement("insert into TEST values (?,?,?,?,?,?)").onArgs(3, "Good Bye", 1, 12, "Cruel World", "").executeDml();
        final int n = db.sqlStatement("update TEST set NAME = NAME || '!' where NAME = 'Good Bye'").executeDml();
        assertThat(n).isEqualTo(2);
    }  // end method list

    @Test public void multiInsert() {
        try(final SqlStatement.Prepared count = db.sqlStatement("select count(*) from TEST").prepare();
            final SqlStatement.Prepared insert = db.sqlStatement("insert into TEST values (?,?,?,?,?,?)").prepare())
        {
            assertCount(count, 0);
            final int n = 100;
            for (int i = 0; i < n; i++)
                insertOne(insert, i).execute();
            assertCount(count, n);
        }
    }

    @Test public void multiInsertBatch() {
        multiInsertBatch(100);
    }

    @Test public void multiMergeBatch() {
        try(final SqlStatement.Prepared count = db.sqlStatement("select count(*) from TEST").prepare();
            final SqlStatement.Prepared insert = db.sqlStatement(
                                                       "merge into TEST " +
                                                       " using Values(V, ? CODE, ? NAME, ? COLOR, ? X_CODE, ? X_NAME, ? BIG) " +
                                                       " on (V.CODE = TEST.CODE) " +
                                                       "when matched then " +
                                                       " update set NAME = V.NAME " +
                                                       "when not matched then " +
                                                       " insert (CODE, NAME, COLOR, X_CODE, X_NAME, BIG) values (V.CODE, V.NAME, V.COLOR, V.X_CODE, V.X_NAME, V.BIG)")
                                                   .prepare())
        {
            assertCount(count, 0);
            final int n = 100;
            for (int i = 0; i < n; i++)
                insertOne(insert, i).batch();
            insert.executeBatch();
            assertCount(count, n);

            for (int i = n + 10; i > n - 10; i--)
                insertOne(insert, i).batch();
            insertOne(insert, 200).batch();

            insert.executeBatch();
            assertCount(count, 112);
        }
    }

    @Test public void multiUpdateBatch() {
        multiInsertBatch(100);

        try(final SqlStatement.Prepared count = db.sqlStatement("select count(*) from TEST where NAME = 'UPDATE_TEST'").prepare();
            final SqlStatement.Prepared update = db.sqlStatement("update TEST set NAME = ? where CODE = ?").prepare())
        {
            assertCount(count, 0);
            final int n = 100;
            for (int i = 0; i < n; i++)
                update.onArgs("UPDATE_TEST", i).batch();
            update.executeBatch();
            assertCount(count, n);
        }
    }

    @Test public void prepare() {
        try(final SqlStatement.Prepared count = db.sqlStatement("select count(*) from TEST").prepare();
            final SqlStatement.Prepared insert = db.sqlStatement("insert into TEST values (?,?,?,?,?,?)").prepare())
        {
            assertCount(count, 0);

            final int n = insert.onArgs(1, "Hello", 0, "1", "Hello", "").executeDml();
            final int m = insert.onArgs(2, "Good Bye", 1, "2", "Good Bye", "").executeDml();
            assertCount(count, 2);
            assertThat(n).isEqualTo(1);
            assertThat(m).isEqualTo(1);
        }
    }

    @Test public void procedureTest() {
        if (db.getDatabaseType() != ORACLE) return;

        db.sqlStatement(
                "create function FACTORIAL(N integer) return integer as\n" +
                "begin \n" +
                "  if N = 1 then return 1; else return N * factorial(N-1); end if;\n" +
                "end;\n").execute();

        assertThat(db.getDatabase().invokeFunction("FACTORIAL", Integer.class, 4)).isEqualTo(24);

        db.sqlStatement(
                "create or replace\n" +
                "procedure LIST_TEST (NAME_PREFIX in varchar2, LEN out integer, ITEMS out SYS_REFCURSOR, BIG_ITEMS out SYS_REFCURSOR) is\n" +
                "begin\n" +
                "LEN := length(NAME_PREFIX);\n" +
                "\t open ITEMS for\n" +
                "         select CODE, NAME from TEST where NAME like NAME_PREFIX || '%'; \n" +
                "\t open BIG_ITEMS for\n" +
                "         select CODE, NAME from TEST where NAME like NAME_PREFIX || '%' and CODE > 3; \n" +
                "end;").execute();

        multiInsertBatch(6);

        try(final Procedure.Prepared prepare = db.getDatabase().procedure("", "LIST_TEST", "He").out(Integer.class).cursor().cursor().prepare()) {
            final Procedure.Result r1 = prepare.invoke();
            assertThat(r1.get(1, Integer.class)).isEqualTo(2);
            assertThat(r1.list(2, rs1 -> rs1.getInt(1))).containsExactly(0, 1, 2, 3, 4, 5);
            assertThat(r1.list(3, Integer.class)).containsExactly(4, 5);

            final Procedure.Result r2 = prepare.onArgs("Pepe").invoke();
            assertThat(r2.get(1, Integer.class)).isEqualTo(4);
            assertThat(r2.list(3, rs1 -> rs1.getInt(1))).isEmpty();
        }
    }  // end method procedureTest
    // @Ignore @Test public void notificationTest() {
    // db.getDatabase().runInTransaction(database -> register(oracleConnection(database), new TestListener()));
    // try {
    // Thread.sleep(1);
    // }
    // catch (final InterruptedException ignore) {}
    // db.sqlStatement("insert into TEST values (?,?,?,?,?,?)").onArgs(1, "Hello", 0, 11, "World", "of my dreams").executeDml();
    // db.sqlStatement("insert into TEST values (?,?,?,?,?,?)").onArgs(2, "Good Bye", 1, 12, "Cruel World", "").executeDml();
    // db.commitTransaction();
    // try {
    // Thread.sleep(10);
    // }
    // catch (final InterruptedException ignore) {}
    // if (dcr != null) db.getDatabase().runInTransaction(database -> {
    // try {
    // (oracleConnection(database)).unregisterDatabaseChangeNotification(dcr);}
    // catch (final SQLException ignored) {}
    // });
    // dcr = null;
    // }

    @Test public void sequenceTest() {
        try {
            final long n1 = db.getDatabase().nextSequenceValue(QName.createQName("ID_SEQ"));
            final long n2 = db.getDatabase().nextSequenceValue(QName.createQName("ID_SEQ"));
            assertThat(n2).isEqualTo(n1 + 1);
        }
        catch (final UnsupportedOperationException e) {
            assertThat(db.getDatabaseType()).isEqualTo(DatabaseType.HSQLDB_NOSEQ);
        }
    }

    @Test public void syntaxError() {
        mute.mute(SqlStatement.class);
        try {
            db.sqlStatement("SELECT 1").getInt();
            failBecauseExceptionWasNotThrown(BadGrammarException.class);
        }
        catch (final BadGrammarException ignored) {}
    }

    @Test public void update() {
        multiInsertBatch(100);

        try(final SqlStatement.Prepared count = db.sqlStatement("select count(*) from TEST where NAME = 'UPDATE_TEST'").prepare();
            final SqlStatement.Prepared update = db.sqlStatement("update TEST set NAME = ? where CODE = ?").prepare())
        {
            assertCount(count, 0);
            final int n = 100;
            for (int i = -1; i < n; i++)
                update.onArgs("UPDATE_TEST", i).execute();
            assertCount(count, n);
        }
    }

    void assertCount(int expected) {
        assertThat(countId()).isEqualTo(expected);
    }

    void assertCount(SqlStatement.Prepared count, int expected) {
        assertThat(count.getInt()).isEqualTo(expected);
    }

    @Nullable private Integer countId() {
        return db.sqlStatement("select count(*) from ID").getInt();
    }

    private SqlStatement.Prepared insertOne(final SqlStatement.Prepared insert, final int i) {
        final String name = "Hello " + i;
        return insert.onArgs(i, name, i, String.valueOf(i), name, "");
    }
    private void multiInsertBatch(int n) {
        try(final SqlStatement.Prepared count = db.sqlStatement("select count(*) from TEST").prepare();
            final SqlStatement.Prepared insert = db.sqlStatement("insert into TEST values (?,?,?,?,?,?)").prepare())
        {
            assertCount(count, 0);
            for (int i = 0; i < n; i++)
                insertOne(insert, i).batch();
            insert.executeBatch();
            assertCount(count, n);
        }
    }

    // private OracleConnection oracleConnection(Database database) {
    // final ConnectionHandle connection = (ConnectionHandle) database.getConnectionRef().get();
    // return (OracleConnection) connection.getInternalConnection();
    // }
    //
    // private void register(OracleConnection conn, DatabaseChangeListener listener) {
    // // first step: create a registration on the server:
    // final Properties prop = new Properties();
    //
    // // if connected through the VPN, you need to provide the TCP address of the client.
    // // For example:
    // // prop.setProperty(OracleConnection.NTF_LOCAL_HOST,"14.14.13.12");
    //
    // // Ask the server to send the ROWIDs as part of the DCN events (small performance
    // // cost):
    // prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
    // // Set the DCN_QUERY_CHANGE_NOTIFICATION option for query registration with finer granularity.
    // // prop.setProperty(OracleConnection.DCN_QUERY_CHANGE_NOTIFICATION, "true");
    //
    // try {
    // dcr = conn.registerDatabaseChangeNotification(prop);
    // }
    // catch (final SQLException e) {
    // throw new RuntimeException(e);
    // }
    // try {
    // // add the listenerr:
    // dcr.addListener(listener);
    //
    // // second step: add objects in the registration:
    // final Statement stmt = conn.createStatement();
    // // associate the statement with the registration:
    // ((OracleStatement) stmt).setDatabaseChangeRegistration(dcr);
    // final ResultSet rs = stmt.executeQuery("select 1 from TEST");
    // // noinspection StatementWithEmptyBody
    // while (rs.next())
    // ;
    // final String[] tableNames = dcr.getTables();
    // for (final String t : tableNames)
    // System.out.println(t + " is part of the registration.");
    // rs.close();
    // stmt.close();
    // }
    // catch (final SQLException ex) {
    // // if an exception occurs, we need to close the registration in order
    // // to interrupt the thread otherwise it will be hanging around.
    // try {
    // conn.unregisterDatabaseChangeNotification(dcr);
    // }
    // catch (final SQLException e) {
    // throw new RuntimeException(e);
    // }
    // throw new RuntimeException(ex);
    // }
    // }  // end method register
    //
    private void validateGeneration(final List<Long> returnedIds) {
        assertCount(10);

        assertThat(db.sqlStatement("select sum(ID) from ID").getInt()).isEqualTo(55);

        final ImmutableList<Long> ids = db.sqlStatement("select ID from ID").list(Long.class);
        assertThat(ids).isEqualTo(returnedIds);
        assertThat(ids).hasSize(10);
        assertThat(ids).contains(1L, 10L);
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    //~ Enums ........................................................................................................................................

    enum Color implements Enumeration<Color, Integer> {
        RED, BLUE, GREEN;

        @Override public int index() {
            return key();
        }

        @Override public Integer key() {
            return ordinal();
        }

        @NotNull @Override public String label() {
            return notNull(name());
        }

        /** Returns the Map: key -> value. */
        @NotNull public static Map<Integer, Color> map() {
            return COLOR_MAP;
        }

        @NotNull private static final Map<Integer, Color> COLOR_MAP = Enumerations.buildMap(values());
    }

    //~ Inner Classes ................................................................................................................................

    static class Entry implements HasRowMapper {
        private final int             code;
        @NotNull private final String name;

        private Entry(int code, @Nullable String name) {
            this.code = code;
            this.name = notNull(name);
        }

        @Override public boolean equals(Object obj) {
            return obj instanceof Entry && code == ((Entry) obj).code && name.equals(((Entry) obj).name);
        }

        @Override public int hashCode() {
            return hashCodeAll(code, name);
        }
        @Override public String toString() {
            return code + ":" + name;
        }

        @NotNull public static Entry entry(final ResultSet rs)
            throws SQLException
        {
            return entry(rs.getInt(1), rs.getString(2));
        }

        public static RowMapper<Entry> rowMapper() {
            return Entry::entry;
        }

        static Entry entry(final int code, @Nullable final String name) {
            return new Entry(code, name);
        }
    }

    static class Entry2 {
        private final int             code;
        @NotNull private final Color  color;
        @DbColumn("NAME")
        @NotNull private final String nm;

        @SuppressWarnings("UnusedDeclaration")
        private Entry2() {
            this(0, "", Color.BLUE);
        }
        private Entry2(int code, @NotNull String name, @NotNull Color color) {
            this.code  = code;
            nm         = name;
            this.color = color;
        }

        @Override public boolean equals(Object obj) {
            if (obj instanceof Entry2) {
                final Entry2 that = (Entry2) obj;
                return code == that.code && nm.equals(that.nm) && color == that.color;
            }
            return false;
        }

        @Override public int hashCode() {
            return hashCodeAll(code, nm);
        }

        @Override public String toString() {
            return code + ":" + nm + ":" + color;
        }

        static Entry2 entry2(int code, @Nullable final String name, @NotNull Color color) {
            return new Entry2(code, notNull(name), color);
        }
    }
}  // end class StatementTest
