
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.core.Tuple3;
import tekgenesis.common.util.Preprocessor;
import tekgenesis.database.exception.translator.*;
import tekgenesis.transaction.ConnectionReference;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.mkString;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.database.DbConstants.EMPTY_CHAR;
import static tekgenesis.database.DbMacro.*;

/**
 * Enum with supported databases.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "SpellCheckingInspection" })
public enum DatabaseType {

    //~ Enum constants ...............................................................................................................................

    POSTGRES("org.postgresql.Driver", new PostgresExceptionTranslator()) {
        @Override public boolean needsCommitOnClose() { return true; }

        @Override public void createSchema(Database db, String name, String tableTablespace) {
            final DatabaseConfig c = db.getConfiguration();
            db.sqlStatement("create schema Schema(%s) authorization $CURRENT_USER", name).executeScript(c.jdbcUrl, c.systemUser, c.systemPassword);
            db.sqlStatement("grant all privileges on schema Schema(%s) to %s;", name, quoted(c.user))
                .executeScript(c.jdbcUrl, c.systemUser, c.systemPassword);
        }

        @Override void addDefines() {
            put(Identity, "serial");
            put(Serial, "serial");
            put(BigSerial, "bigserial");
            put(nvarchar, "varchar($1)");
            put(datetime, "timestamp($1) with time zone");
            put(MinDateTime, "timestamp '-infinity'");
            put(clob, "text");
            put(blob, "bytea");
            put(bitand, "($1) & ($2)");
            put(IndexName, "$2");
            put(DropNotNull, "alter column $1 drop not null");
            put(DbCurrentTime, "clock_timestamp()");
            put(AlterColumnType, "alter column $1 type $2");
            put(Utc, "(($1 at time zone current_setting('TIMEZONE')) at time zone interval '0' minute)");
            put(CommentOnView, "comment on view");
            put(AlterView, CREATE_OR_REPLACE_VIEW);
        }

        @Override void resetIdentity(final Database db, final QName tableName, final String sequenceName, final long nextVal) {
            db.sqlStatement("alter sequence Schema(%s).\"%s_ID_seq\" restart with %d", tableName.getQualification(), tableName.getName(), nextVal)
                .execute();
        }

        @Override long nextSequenceValue(final Database db, final String sequenceName) {
            return notNull(db.sqlStatement("select nextval('%s')", sequenceName).getLong(), 0L);
        }

        @Override public boolean supportsUnicodeInChar() { return true; }

        @Override public boolean supportsLobs() { return false; }

        @Override public void createDatabase(Database db) {
            final DatabaseConfig c    = db.getConfiguration();
            final String         user = c.user;

            final String dbName = quoted(c.jdbcUrl.substring(c.jdbcUrl.lastIndexOf('/') + 1));
            db.sqlStatement("create user %s with password '%s'", user, c.password).executeScript(c.getAdminUrl(), c.systemUser, c.systemPassword);

            db.sqlStatement("create database %s;" +
                "grant connect on database %s to %s", dbName, dbName, user).executeScript(c.getAdminUrl(), c.systemUser, c.systemPassword);
        }

        @Override public void dropSchema(Database db, String name, boolean ignoreErrors) {
            try {
                final DatabaseConfig c = db.getConfiguration();
                db.sqlStatement("drop schema Schema(%s) cascade", name)
                    .ignoreErrors(ignoreErrors)
                    .executeScript(c.jdbcUrl, c.systemUser, c.systemPassword);
            }
            catch (final Exception e) {
                if (!ignoreErrors) throw new RuntimeException(e);
            }
        }

        @Override public boolean defaultsToLowerCase() { return true; }

        @Override public void dropDatabase(Database db, boolean ignoreErrors) {
            final DatabaseConfig c      = db.getConfiguration();
            final String         dbName = quoted(c.jdbcUrl.substring(c.jdbcUrl.lastIndexOf('/') + 1));

            db.sqlStatement("drop owned by %s cascade", c.user)
                .ignoreErrors(ignoreErrors)
                .executeScript(c.getAdminUrl(), c.systemUser, c.systemPassword);
            db.sqlStatement("drop database %s", dbName).ignoreErrors(ignoreErrors).executeScript(c.getAdminUrl(), c.systemUser, c.systemPassword);
            db.sqlStatement("drop user %s", c.user).ignoreErrors(ignoreErrors).executeScript(c.getAdminUrl(), c.systemUser, c.systemPassword);
        }},

    ORACLE("oracle.jdbc.OracleDriver", new OracleExceptionTranslator()) {
        @Override void addDefines() {
            put(boolean_, "number(1)");
            put(nvarchar, "nvarchar2($1)");
            put(datetime, "timestamp($1) with local time zone");
            put(clob, "nclob");
            put(False, "0");
            put(True, "1");
            put(SchemaOrUser, "Schema($1)");
            put(BigSerial, "number");
            put(bigint, "number");
            put(CheckBoolConstraint, "constraint $1 check ($2 in (0,1))");
            put(EmptyString, ORACLE_EMPTY_STRING);
            put(SequenceStartValue, "$1 minvalue $1");
            put(DbCurrentDate, ORACLE_CURRENT_DATE);
            put(SelectCurrentTime, "select CurrentTime from dual");
            put(SeqNextVal, "$1.nextval");
            put(NeedsCreateSequence, "true");
            put(NeedsSerialComment, "true");
            put(SetNotNull, "modify $1 not null");
            put(DropNotNull, "modify $1 null");
            put(SetDefault, "modify $1 default $2");
            put(AlterColumnType, "modify $1 $2");
            put(AddColumn, "add $1");
            put(AlterView, CREATE_OR_REPLACE_VIEW);
            put(Values,
                args -> {
                    final Tuple3<String, ArrayList<String>, ArrayList<String>> t  = DbMacro.parseValues(args);
                    final StrBuilder                                           vs = new StrBuilder();
                    for (int i = 0; i < t._2().size(); i++)
                        vs.appendElement(t._2().get(i) + " " + t._3().get(i));
                    return format("(select %s from dual) %s", vs.toString(), t._1());
                });
            put(UpdateIf, "then update set $2 where $1");
            put(NlsSort, "nlssort($1,'NLS_SORT=$2')");
        }

        @Override public boolean supportsEmptyString() { return false; }

        @Override public String limit(String statement, long offset, long limit) {
            if (offset == 0) return format("select * from (%s) where rownum <= %d", statement, limit);
            if (limit == Long.MAX_VALUE)
                return format("select * from (select V.*, rownum as \"_\" from (%s )V )  where \"_\" > %d", statement, offset);
            return format("select * from (select V.*, rownum as \"_\" from (%s )V )  where \"_\" between %d and %d",
                statement,
                offset + 1,
                offset + limit);
        }

        @Override long nextSequenceValue(final Database db, final String sequenceName) {
            return notNull(db.sqlStatement("select %s.nextval from dual", sequenceName).getLong(), 0L);
        }

        @Override public boolean supportsClientInfo() { return true; }

        @Override void resetIdentity(final Database db, final QName tableName, final String sequenceName, final long nextVal) {
            db.sqlStatement("drop sequence QName(%s,%s)", tableName.getQualification(), sequenceName).execute();
            db.sqlStatement("create sequence QName(%s,%s) start with %d increment by 1", tableName.getQualification(), sequenceName, nextVal)
                .execute();
        }

        @Override public Object getValueFor(Object value, Class<?> type) {
            return type.isAssignableFrom(String.class) && isDefined(value) && isEmpty((String) value) ? EMPTY_CHAR : super.getValueFor(value, type);
        }

        @Override public void dropSchema(Database db, String name, boolean ignoreErrors) {
            final DatabaseConfig c = db.getConfiguration();
            db.sqlStatement("drop user Schema(%s) cascade", name)
                .ignoreErrors(ignoreErrors)
                .executeScript(c.getAdminUrl(), c.systemUser, c.systemPassword);
        }

        @Override public void createDatabase(Database db) {
            final DatabaseConfig c       = db.getConfiguration();
            String               profile = "";
            if (c.maxConnectionTime != 0) {
                db.sqlStatement("create profile %s limit connect_time 1", c.user, c.maxConnectionTime)
                    .executeScript(c.jdbcUrl, c.systemUser, c.systemPassword);
                profile = "profile " + c.user;
            }
            db.sqlStatement("create user %s identified by %s %s", c.user, c.password, profile)
                .executeScript(c.jdbcUrl, c.systemUser, c.systemPassword);
            db.sqlStatement(grantPermissions(c.user)).executeScript(c.jdbcUrl, c.systemUser, c.systemPassword);
        }

        @Override public void createSchema(Database db, String name, String tableSpace) {       //
            final Database dbs = db.asSystem();
            dbs.getTransactionManager().runInTransaction(t -> {
                dbs.sqlStatement("create user Schema(%s) identified by $USER_PASSWORD %s",
                       name,
                       isEmpty(tableSpace) ? "" : " default tablespace " + tableSpace).execute();  //
                dbs.sqlStatement("grant resource to Schema(%s)", name).execute();               //
                dbs.sqlStatement("grant unlimited tablespace to Schema(%s)", name).execute();   //
                dbs.sqlStatement(grantPermissions(db.getConfiguration().user)).execute();
            });
        }

        @NotNull private String grantPermissions(String user) { return ImmutableList.fromArray(GRANTS).mkString("grant ", ", ", " to " + user); }

        private final String[] GRANTS = {
            "create session",
            "grant any object privilege",
            "create any view",
            "drop any view",
            "drop any table",
            "create any table",
            "comment any table",
            "alter any table",
            "select any table",
            "insert any table",
            "update any table",
            "delete any table",
            "create any sequence",
            "select any sequence",
            "alter any sequence",
            "drop any sequence",
            "create any index",
            "drop any index",
            "alter any index",
            "execute any type",
            "unlimited tablespace",
            "connect",
            "create any trigger",
            "create any procedure",
            "drop any procedure",
            "drop any trigger",
            "alter any procedure",
            "alter any trigger",
            "change notification",
            "resource"
        };

        @Override public void dropDatabase(Database db, boolean ignoreErrors) {
            final DatabaseConfig c = db.getConfiguration();
            db.sqlStatement("drop user %s cascade", c.user).ignoreErrors(ignoreErrors).executeScript(c.jdbcUrl, c.systemUser, c.systemPassword);
            if (c.maxConnectionTime != 0)
                db.sqlStatement("drop profile %s", c.user).ignoreErrors(ignoreErrors).executeScript(c.jdbcUrl, c.systemUser, c.systemPassword);
        }},

    SQLSERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver") {
        @Override void addDefines() {
            put(Identity, "int identity(1,1)");
            put(Serial, "int identity($1,1)");
            put(BigSerial, "bigint identity($1,1)");
            put(SetNotNull, "alter column $1 not null");
            put(DropNotNull, "alter column $1 null");
        }

        @Override void resetIdentity(final Database db, final QName tableName, final String sequenceName) {
            db.sqlStatement("dbcc checkident (TableName(%s,%s))", tableName.getQualification(), tableName.getName());
        }

        @Override void resetIdentity(final Database db, final QName tableName, final String sequenceName, final long nextVal) {
            db.sqlStatement("dbcc checkident (TableName(%s,%s), reseed, %d)", tableName.getQualification(), tableName.getName(), nextVal);
        }},

    HSQLDB("org.hsqldb.jdbcDriver", new HSQLExceptionTranslator()) {
        @Override void addDefines() {
            put(SequenceCache, "");
            put(RenameColumn, HSQL_RENAME_COLUMN);
            put(NoWait, "");
            put(SkipLocked, "");
            put(NeedsCreateSequence, "true");
            put(Serial, "int generated by default as sequence $2");
            put(BigSerial, "bigint generated by default as sequence $2");
            put(SelectCurrentTime, "values(CurrentTime)");
            put(MinDateTime, "timestamp '0001-01-01 00:00:00'");
            put(Values,
                args -> {
                    final Tuple3<String, ArrayList<String>, ArrayList<String>> t = DbMacro.parseValues(args);
                    return format("(values %s) as %s (%s)", mkString(t._2(), ","), t._1(), mkString(t._3(), ","));
                });
        }

        @Override public void dropDatabase(Database db, boolean ignoreErrors) {
            final DatabaseConfig c = db.getConfiguration();
            if (c.user.equalsIgnoreCase(HSQL_SA)) return;
            try {
                db.sqlStatement("drop user %s cascade", c.user).ignoreErrors(ignoreErrors).executeScript(c.jdbcUrl, c.systemUser, c.systemPassword);
            }
            catch (final Exception e) {
                // ignore
            }
        }

        @Override long nextSequenceValue(final Database db, final String sequenceName) {
            return notNull(db.sqlStatement("call next value for %s", sequenceName).getLong(), 0L);
        }

        @Override public void shutdown(Database db) {
            final ConnectionReference<Connection> connectionRef = db.getConnectionRef();
            try {
                connectionRef.get().createStatement().execute("SHUTDOWN");
            }
            catch (final SQLException e) {
                // ignore
            }
            finally {
                connectionRef.detach();
            }
        }

        @Override public boolean supportsUnicodeInChar() { return true; }

        @Override public String getDefaultCatalog() { return "PUBLIC"; }

        @Override public boolean isPrimaryKey(final String indexName) { return indexName.startsWith("SYS_IDX_PK_"); }

        @Override public void createDatabase(Database db) {
            final DatabaseConfig c = db.getConfiguration();
            if (c.user.equalsIgnoreCase(HSQL_SA)) return;

            db.sqlStatement("create user %s password %s", c.user, quoted(c.password)).executeScript(c.jdbcUrl, c.systemUser, c.systemPassword);
        }},

    HSQLDB_NOSEQ("org.hsqldb.jdbcDriver", new HSQLExceptionTranslator()) {
        @Override public DatabaseType getType() { return HSQLDB; }

        @Override public boolean supportsUnicodeInChar() { return HSQLDB.supportsUnicodeInChar(); }

        @Override public String getDefaultCatalog() { return HSQLDB.getDefaultCatalog(); }

        @Override public void shutdown(Database db) { HSQLDB.shutdown(db); }

        @Override void addDefines() {
            put(SequenceCache, "");
            put(RenameColumn, HSQL_RENAME_COLUMN);
            put(SelectCurrentTime, "values(CurrentTime)");
            put(NoWait, "");
            put(SkipLocked, "");
            put(MinDateTime, "timestamp '0001-01-01 00:00:00'");
            put(Identity, "int generated by default as identity (start with 1 increment by 1)");
            put(Serial, "int generated by default as identity (start with $1 increment by 1)");
            put(BigSerial, "bigint generated by default as identity (start with $1 increment by 1)");
            put(Values,
                args -> {
                    final Tuple3<String, ArrayList<String>, ArrayList<String>> t = DbMacro.parseValues(args);
                    return format("(values %s) as %s (%s)", mkString(t._2(), ","), t._1(), mkString(t._3(), ","));
                });
        }

        @Override public boolean isPrimaryKey(final String indexName) { return HSQLDB.isPrimaryKey(indexName); }

        @Override public void dropDatabase(Database db, boolean ignoreErrors) { HSQLDB.dropDatabase(db, ignoreErrors); }

        @Override public void createDatabase(Database db) { HSQLDB.createDatabase(db); }

        @Override void resetIdentity(final Database db, final QName tableName, final String sequenceName, final long nextVal) {
            db.sqlStatement("alter table TableName(%s,%s) alter column ID restart with %d",
                    tableName.getQualification(),
                    tableName.getName(),
                    nextVal).execute();
        }},

    MYSQL("com.mysql.jdbc.Driver") {
        @Override void addDefines() {
            put(Identity, "int auto_increment");
            put(Serial, "int auto_increment");
            put(BigSerial, "bigint auto_increment");
        }

        @Override void resetIdentity(final Database db, final QName tableName, final String sequenceName, final long nextVal) {
            db.sqlStatement("alter table TableName(%s,%s) auto increment = %d", tableName.getQualification(), tableName.getName(), nextVal).execute();
        }},

    DB2("com.ibm.db2.jcc.DB2Driver") {};

    //~ Instance Fields ..............................................................................................................................

    private final Map<DbMacro, Preprocessor.Macro> defines;

    private final String                 driverName;
    private final SQLExceptionTranslator sqlExceptionTranslator;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("WeakerAccess")
    DatabaseType(@NotNull String defaultDriverClassName) {
        this(defaultDriverClassName, new DummyExceptionTranslator());
    }

    @SuppressWarnings("WeakerAccess")
    DatabaseType(@NotNull String defaultDriverClassName, @NotNull SQLExceptionTranslator translator) {
        driverName = defaultDriverClassName;
        defines    = defaultValues();
        addDefines();
        sqlExceptionTranslator = translator;
    }

    //~ Methods ......................................................................................................................................

    /** Create the database. */
    public void createDatabase(Database db) {}

    /** Create an Schema. */
    public void createSchema(Database db, String name, String tableTablespace) {  //
        final Database dbs = db.asSystem();
        db.getTransactionManager().runInTransaction(t ->
                dbs.sqlStatement("create schema Schema(%s) authorization $CURRENT_USER", name)
                   .executeScript());
    }

    /** returns true if the database defaults to lowercase ids. */
    public boolean defaultsToLowerCase() {
        return false;
    }

    /** Drop the database. */
    public void dropDatabase(Database db, boolean ignoreErrors) {}

    /** Drop an schema. */
    public void dropSchema(Database db, String name, boolean ignoreErrors) {
        final DatabaseConfig c = db.getConfiguration();
        db.sqlStatement("drop schema Schema(%s) cascade", name)
            .ignoreErrors(ignoreErrors)
            .executeScript(c.getAdminUrl(), c.systemUser, c.systemPassword);
    }

    /** Returns true if the specified macro is defined. */
    public boolean has(final DbMacro macro) {
        return defines.containsKey(macro);
    }

    /** Modify the SQL statement to impose a limit in the number of retrieved results. */
    public String limit(String statement, long offset, long limit) {
        return statement + (limit != Long.MAX_VALUE ? " limit " + limit : "") + (offset != 0 ? " offset " + offset : "");
    }

    /** Usually false, POSTGRES needs it in true because of some bug. */
    public boolean needsCommitOnClose() {
        return false;
    }

    /** The predefined set of macros for this Database. */
    public final HashMap<String, Preprocessor.Macro> retrieveMacros() {
        final HashMap<String, Preprocessor.Macro> map = new HashMap<>();
        for (final Map.Entry<DbMacro, Preprocessor.Macro> e : defines.entrySet())
            map.put(e.getKey().id(), e.getValue());
        return map;
    }

    /** Database shutdown. */
    public void shutdown(Database db) {
        // Do nothing
    }

    /** Returns true if support setClientInfo method. */
    public boolean supportsClientInfo() {
        return false;
    }

    /** Returns true if the database type supportsempty strings (different from null). */
    public boolean supportsEmptyString() {
        return true;
    }

    /** Return true if support NClob. */
    public boolean supportsLobs() {
        return true;
    }

    /** Returns true if supports merge statement. */
    public boolean supportsMerge() {
        return true;
    }

    /** Returns true if the databases supports Unicode in normal Chars. */
    public boolean supportsUnicodeInChar() {
        return false;
    }

    /** Return user string for connection. */
    public String getConnectionUser(String jdbcUser) {
        return jdbcUser;
    }

    /** Get Default Catalog for Database. */
    public String getDefaultCatalog() {
        return "";
    }

    /**
     * Returns the default driver class name.
     *
     * @return  Default driver class name.
     */
    public String getDefaultDriverClassName() {
        return driverName;
    }

    /** Returns the SQLException translator for this database type. */
    @NotNull public SQLExceptionTranslator getSqlExceptionTranslator() {
        return sqlExceptionTranslator;
    }

    /** Return the final database type. */
    public DatabaseType getType() {
        return this;
    }

    /** Converts value if necessary. */
    public Object getValueFor(Object value, Class<?> type) {
        return value;
    }

    /** Returns true if the specified index is a primary key. */
    public boolean isPrimaryKey(final String indexName) {
        return false;
    }

    void addDefines() {}

    @NotNull Timestamp currentTime(final Database db) {
        final Timestamp ts = db.sqlStatement(DbMacro.SelectCurrentTime.toString()).get(rs -> rs.getTimestamp(1));
        if (ts == null) return new Timestamp(0L);
        return ts;
    }

    long nextSequenceValue(final Database db, final String sequenceName) {
        throw new UnsupportedOperationException("Next Sequence Value for: " + sequenceName);
    }

    void put(final DbMacro macro, final String value) {
        defines.put(macro, new Preprocessor.Macro(value));
    }
    void put(final DbMacro macro, final Function<List<String>, String> value) {
        defines.put(macro, new Preprocessor.Macro(value));
    }

    void resetIdentity(final Database db, final QName tableName, final String sequenceName) {
        final String schema = tableName.getQualification();
        final long   max    = notNull(db.sqlStatement("select max(ID) from QName(%s,%s)", schema, tableName.getName()).get(Long.class), 0L);
        resetIdentity(db, tableName, sequenceName, max + 1);
    }

    void resetIdentity(Database db, QName tableName, String sequenceName, long nextVal) {
        db.sqlStatement("alter sequence Schema(%s).\"%s\" restart with %d", tableName.getQualification(), sequenceName, nextVal).execute();
    }

    //~ Methods ......................................................................................................................................

    /** Get database type from JDBC connection. */
    @Nullable public static DatabaseType typeFromConnection(@NotNull final Connection connection) {
        try {
            switch (connection.getMetaData().getDatabaseProductName()) {
            case "Oracle":
                return ORACLE;
            case "PostgreSQL":
                return POSTGRES;
            case "HSQL Database Engine":
                return HSQLDB;
            }
        }
        catch (final SQLException ignored) {}
        return null;
    }

    //~ Static Fields ................................................................................................................................

    private static final String HSQL_SA = "sa";

    private static final String HSQL_RENAME_COLUMN     = "alter column $1 rename to $2";
    public static final String  ORACLE_CURRENT_DATE    = "sysdate";
    public static final String  ORACLE_EMPTY_STRING    = "nchr(160)";
    private static final String CREATE_OR_REPLACE_VIEW = "create or replace view";
}
