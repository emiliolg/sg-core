
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.EnumSet;

import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.QName;
import tekgenesis.common.util.Files;
import tekgenesis.common.util.Preprocessor;
import tekgenesis.database.support.JdbcUtils;
import tekgenesis.properties.SchemaProps;
import tekgenesis.transaction.ConnectionReference;
import tekgenesis.transaction.Transaction;
import tekgenesis.transaction.TransactionManager;
import tekgenesis.transaction.TransactionResource;

import static java.lang.String.format;
import static java.util.Collections.singletonList;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Constants.SQL_CURRENT_USER;
import static tekgenesis.common.util.Preprocessor.Escape.QUOTE_UPPER_CASE;
import static tekgenesis.database.DbConstants.SQL_INDEX_TABLE_SPACE;
import static tekgenesis.database.DbConstants.SQL_USER_PASSWORD;
import static tekgenesis.database.DbMacro.Schema;
import static tekgenesis.database.SqlStatementBase.Flag.PRE_PROCESS;

/**
 * This class abstracts a Database.
 */
public class Database {

    //~ Instance Fields ..............................................................................................................................

    @NotNull protected final DatabaseConfig configuration;

    /** The factory that created this database. */
    @NotNull private final DatabaseFactory<?> dbFactory;

    @NotNull private final String name;

    @Nullable private Database systemDatabase;

    @NotNull private final TransactionManager              tm;
    @NotNull private final TransactionResource<Connection> tr;

    //~ Constructors .................................................................................................................................

    /**
     * Create a Database, do not use this method directly, use {@link Databases#open(String)}. or
     * {@link Databases#forSchema(String)}
     */
    Database(@NotNull DatabaseFactory<?> dbFactory, @NotNull DatabaseConfig config, @NotNull TransactionManager tm,
             @NotNull final TransactionResource<Connection> tr) {
        this.dbFactory = dbFactory;
        name           = tr.getName();
        configuration  = config;
        this.tm        = tm;
        this.tr        = tr;
        systemDatabase = config.user.equals(config.systemUser) ? this : null;
    }

    //~ Methods ......................................................................................................................................

    /** Return a new 'dry' (Only log executions) Database. */
    public Database asDry(Writer writer) {
        return new Dry(this, writer);
    }

    /** return no dry Database. */
    public Database asNotDry() {
        return this;
    }

    /** Returns an alias of this database that will use the system user to execute statements. */
    public Database asSystem() {
        if (systemDatabase == null) systemDatabase = dbFactory.createSystemAlias(this);
        return systemDatabase;
    }

    /** Close the database. */
    public void close() {
        tm.getCurrentTransaction().ifPresent(Transaction::close);
        tr.close();
        closeSystemConnection();
    }

    /** Create a configured Preprocessor. */
    public Preprocessor createPreprocessor(@Nullable SchemaProps schemaProps) {
        final DatabaseConfig dbConfig = getConfiguration();
        return createPreprocessor(dbConfig.type, dbConfig.schemaPrefix, dbConfig.user, dbConfig.password, schemaProps);
    }
    /**
     * Get database Current Time. This operation perform an query on the database, opening or
     * enrolling on a transaction.
     */
    public DateTime currentTime() {
        return JdbcUtils.fromTimestamp(getDatabaseType().currentTime(this));
    }

    /** Get the current Transaction, or create a new one. */
    public Transaction get() {
        return tm.getOrCreateTransaction();
    }

    /** Invoke a predefined Function. */
    @Nullable public <T> T invokeFunction(String functionName, Class<T> returning, Object... inputArguments) {
        return invokeFunction("", functionName, returning, inputArguments);
    }

    /** Invoke a predefined Function. */
    @Nullable public <T> T invokeFunction(String schemaName, String functionName, Class<T> returning, Object... inArgs) {
        try(final Procedure.Prepared prepared = new Procedure(this, schemaName, functionName, returning, inArgs).prepare()) {
            return prepared.invoke().get(0, returning);
        }
    }

    /**
     * Create a Native SQL query over the current DataBase (with optional parameters inside). The
     * Query will be passed to the DatabaseDriver 'as is'.
     */
    public SqlStatement nativeSql(@NotNull String sql) {
        return new SqlStatement(this, singletonList(sql), EnumSet.noneOf(SqlStatementBase.Flag.class));
    }

    /** Return next sequence Value. */
    public long nextSequenceValue(QName sequenceName) {
        return getDatabaseType().nextSequenceValue(this, qualifyAndQuote(sequenceName));
    }

    /** Pre-process an statement. */
    @NotNull public String preProcess(@NotNull String line) {
        return createPreprocessor(null).process(line);
    }

    /** Create a Procedure over predefined Function. */
    public Procedure procedure(@NotNull QName qName, Object... inputArguments) {
        return new Procedure(this, qName, null, inputArguments);
    }

    /** Create a Procedure over predefined Function. */
    public Procedure procedure(String schemaName, String procedureName, Object... inputArguments) {
        return new Procedure(this, schemaName, procedureName, null, inputArguments);
    }

    /** Create a Procedure over predefined Function. */
    public Procedure procedure(@NotNull QName qName, @Nullable Class<?> returning, Object... inputArguments) {
        return new Procedure(this, qName, returning, inputArguments);
    }

    /** Create a Procedure over predefined Function. */
    public Procedure procedure(String schemaName, String procedureName, @Nullable Class<?> returning, Object... inputArguments) {
        return new Procedure(this, schemaName, procedureName, returning, inputArguments);
    }

    /** Reset the specify identity counter. */
    public void resetIdentitySequence(QName tableName, String sequenceName) {
        getDatabaseType().resetIdentity(this, tableName, sequenceName);
    }

    /** Create an SQL query over the current DataBase (with optional parameters inside). */
    public SqlStatement sqlStatement(@NotNull Reader reader) {
        return new SqlStatement(this, Files.readLines(reader), EnumSet.of(PRE_PROCESS));
    }
    /**
     * Create an SQL Statement over the current DataBase (with optional parameters inside). The
     * Query will be preprocessed to:
     *
     * <ol>
     *   <li>Automatically quote identifiers, defined in UPPERCASE.</li>
     *   <li>Apply macros defined for the current database.</li>
     * </ol>
     *
     * <p>Statements MUST be written in lowercase.</p>
     *
     * <p>The query will be used in {@link PreparedStatement}, that's why you can use the same
     * formatting as there. Arguments shall be marked as {@code "?"} (question marks). For
     * example:</p>
     *
     * <pre>
     database.sqlStatement("insert into FOO (ID, NAME) values(?, ?)")
              .onArgs(556677, "John Doe")
              .insert()
     * </pre>
     *
     * @param  sql  The SQL Statement. All statements should be written in lowercase for correct
     *              parsing
     */
    @NotNull public SqlStatement sqlStatement(@NotNull String sql) {
        return new SqlStatement(this, singletonList(sql), EnumSet.of(PRE_PROCESS));
    }

    /**
     * Like {@link #sqlStatement(String)}.
     *
     * <p>But you can also specify parameters using the {@link String#format} function with the
     * specified arguments. Useful to take table and column names from constants defined elsewhere
     * example:</p>
     *
     * <pre>
     database.sqlStatement("insert into %s (%s, %s) values(?, ?)", FOO, ID, NAME)
             .onArgs(556677, "John Doe")
             .insert()
     * </pre>
     *
     * @param  sql  The SQL Statement. All statements should be written in lowercase for correct
     *              parsing
     */
    @NotNull public SqlStatement sqlStatement(@NotNull @PrintFormat String sql, Object... args) {
        return sqlStatement(format(sql, args));
    }

    /** Returns the Database Configuration. */
    @NotNull public DatabaseConfig getConfiguration() {
        return configuration;
    }

    /** Find or create a connection. */
    public ConnectionReference<Connection> getConnectionRef() {
        try {
            return tm.getConnectionRef(tr);
        }
        catch (final SQLException e) {
            throw getDatabaseType().getSqlExceptionTranslator().translate(e);
        }
    }

    /** Returns the Database Type. */
    @NotNull public DatabaseType getDatabaseType() {
        return configuration.type;
    }

    /** Returns the Database name. */
    @NotNull public String getName() {
        return name;
    }

    /** Returns the Schema prefix for this database. */
    @NotNull public String getSchemaPrefix() {
        return configuration.schemaPrefix;
    }

    /** Returns tje current transaction Manager. */
    public TransactionManager getTransactionManager() {
        return tm;
    }

    String qualifyAndQuote(QName qname) {
        final String schemaName = qname.getQualification();
        final String nm         = qname.getName();
        return schemaName.isEmpty() ? '"' + nm + '"' : format("\"%s%s\".\"%s\"", getSchemaPrefix(), schemaName, nm);
    }
    void resetConnection() {
        if (getDatabaseType() == DatabaseType.POSTGRES) {
            tm.getCurrentTransaction().ifPresent(Transaction::rollback);
            tm.getOrCreateTransaction();
        }
    }

    private void closeSystemConnection() {
        if (systemDatabase != null && systemDatabase != this) systemDatabase.close();
    }

    //~ Methods ......................................................................................................................................

    /** Create a configured Preprocessor. */
    public static Preprocessor createPreprocessor(DatabaseType dbType, String schemaPrefix, String user, String password,
                                                  @Nullable SchemaProps props) {
        final Preprocessor preprocessor = new Preprocessor()         //
                                          .withMarker("--").define(dbType.name())  //
                                          .escapeIds(QUOTE_UPPER_CASE)  //
                                          .defineAll(dbType.retrieveMacros())  //
                                          .define(Schema.id(), schemaPrefix.toUpperCase() + "$1")  //
                                          .define(SQL_CURRENT_USER, user)  //
                                          .define(SQL_USER_PASSWORD, password);

        // -- Deprecated
        // noinspection DuplicateStringLiteralInspection
        preprocessor.define("SequenceName", "QName($1,$2)").define("$SCHEMA", "Schema($1)");

        if (props != null && !isEmpty(props.tableTablespace)) preprocessor.define("$TABLE_TABLESPACE", props.tableTablespace);

        final String indexTableSpace = props == null || isEmpty(props.indexTablespace) ? "" : props.indexTablespace;
        preprocessor.define(SQL_INDEX_TABLE_SPACE, indexTableSpace.isEmpty() ? "" : "tablespace " + indexTableSpace);

        // Todo remove
        if (!indexTableSpace.isEmpty()) preprocessor.define("$INDEX_TABLESPACE", indexTableSpace);

        return preprocessor;
    }

    /** Use client time for Database CurrentTime. */
    public static boolean useClientTime() {
        return useClientTime;
    }

    /**
     * Force the expansion of CurrentDate and CurrentTime to client time (as returned by
     * {@link DateTime#current()}).
     */
    public static void setUseClientTime(boolean b) {
        useClientTime = b;
    }

    //~ Static Fields ................................................................................................................................

    private static boolean useClientTime = false;

    //~ Inner Classes ................................................................................................................................

    static class Dry extends Database {
        private final ConnectionReference<Connection> dry;
        private final Database                        parent;
        @NotNull private final PrintWriter            writer;

        private Dry(@NotNull final Database db, @NotNull final Writer w) {
            super(db.dbFactory, db.configuration, db.tm, db.tr);
            parent = db;
            writer = w instanceof PrintWriter ? (PrintWriter) w : new PrintWriter(w);
            dry    = new ConnectionReference<Connection>() {
                    @Override public Connection get() {
                        throw new RuntimeException("Dry");
                    }
                    @Nullable @Override public PrintWriter dryWriter() {
                        return writer;
                    }
                };
        }

        @Override public Database asDry(final Writer dryWriter) {
            return this;
        }

        @Override public Database asNotDry() {
            return parent;
        }

        @NotNull @Override public Database asSystem() {
            return super.asSystem().asDry(writer);
        }

        @NotNull @Override public DateTime currentTime() {
            return DateTime.current();
        }

        @Override public ConnectionReference<Connection> getConnectionRef() {
            return dry;
        }
    }
}  // end interface Database
// end interface Database
