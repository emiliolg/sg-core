
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.StepResult;
import tekgenesis.common.core.Strings;
import tekgenesis.common.logging.Logger;
import tekgenesis.database.exception.DatabaseException;
import tekgenesis.database.support.JdbcUtils;
import tekgenesis.properties.SchemaProps;
import tekgenesis.transaction.ConnectionReference;

import static java.lang.Long.MAX_VALUE;
import static java.sql.ResultSet.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.Collections.addAll;
import static java.util.Collections.singletonList;

import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.core.Strings.asLines;
import static tekgenesis.database.Argument.inputArguments;
import static tekgenesis.database.DatabaseType.ORACLE;
import static tekgenesis.database.ResultHandler.listRowHandler;
import static tekgenesis.database.ResultHandler.singleRowHandler;
import static tekgenesis.database.SqlStatementBase.Flag.*;
import static tekgenesis.database.StatementProxy.createDefault;
import static tekgenesis.database.StatementProxy.createPrepared;

/**
 * A SqlStatement executor.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class SqlStatement extends SqlStatementBase {

    //~ Instance Fields ..............................................................................................................................

    private String keyColumnName;
    private long   limit;
    private long   offset;

    private SchemaProps schemaProps = null;

    //~ Constructors .................................................................................................................................

    SqlStatement(Database database, List<String> sql, EnumSet<Flag> flags) {
        super(database, sql, flags);
        limit         = MAX_VALUE;
        offset        = 0;
        keyColumnName = "";
    }

    //~ Methods ......................................................................................................................................

    /** Execute the Statement. */
    public final void execute() {
        createRunner().ensureTransactionStarted().run(stmt -> null);
    }

    /** Execute the statement as a dml one. */
    public int executeDml() {
        return createRunner().ensureTransactionStarted().runDml();
    }

    /** Execute a script with a set of Statements. Separated by ';;' */
    public final void executeScript() {
        executeScript(DEFAULT_HANDLER);
    }

    /** Execute a script with a set of Statements. Separated by ';;' */
    public void executeScript(Predicate<DatabaseException> h) {
        executeScript(h, database.getConnectionRef());
    }

    /**
     * Create a connection with the specified arguments and execute a a script with a set of
     * Statements. Separated by ';;'
     */
    public void executeScript(final String url, final String user, final String password) {
        final Connection c = JdbcUtils.getDirectConnection(url, user, password);
        try {
            executeScript(DEFAULT_HANDLER, () -> c);
        }
        finally {
            JdbcUtils.closeConnection(c);
        }
    }

    /**
     * Execute the statement. The {@link RowMapper} is used to map rows from the result set to the
     * desired result
     */
    public final <T> Option<T> forEach(final RowHandler<T> handler) {
        return forEach(handler, true);
    }
    /**
     * Execute the statement. The {@link RowMapper} is used to map rows from the result set to the
     * desired result
     */
    public final <T> Option<T> forEach(final RowHandler<T> handler, boolean transactional) {
        return forEach(handler, Option.empty(), transactional);
    }
    /**
     * Execute the statement. The {@link RowMapper} is used to map rows from the result set to the
     * desired result
     */
    public final <T> Option<T> forEach(final RowHandler<T> handler, Option<T> finalValue, boolean transactional) {
        final Runner runner = createRunner();
        if (transactional) runner.ensureTransactionStarted();
        final Option<T> r = runner.run(stmt -> {
                try(final ResultSet rs = stmt.getResultSet()) {
                    while (rs.next()) {
                        final StepResult<T> result = handler.accept(rs);
                        if (result.isDone()) return result.getValue();
                    }
                }
                return finalValue;
            });
        return r == null ? Option.empty() : r;
    }

    /**
     * Execute the statement and return a result based on a Mapper. Or null if not entries are
     * present The {@link RowMapper} is used to map rows from the result set to the desired result
     */
    @Nullable public final <T> T get(final RowMapper<T> mapper) {
        return run(singleRowHandler(mapper));
    }

    /**
     * Execute the statement and return a single result of the specified type. Or null if not
     * entries are present
     */
    @Nullable public final <T> T get(final Class<T> resultType) {
        return run(ResultHandler.reflectiveHandler(resultType));
    }
    /** Ignore execution errors. */
    @SuppressWarnings("WeakerAccess")
    public SqlStatement ignoreErrors(boolean ignoreErrors) {
        if (ignoreErrors) flags.add(IGNORE_ERRORS);
        else flags.remove(IGNORE_ERRORS);
        return this;
    }

    /** Limit the number of result to the specified value. */
    @NotNull public SqlStatement limit(long n) {
        return limit(0, n);
    }

    /** Limit the number of result to the specified value. */
    public SqlStatement limit(final long from, final long n) {
        offset = from;
        limit  = n;
        return this;
    }

    /** Execute the statement and return a list of results of the specified type. */
    @NotNull public final <T> ImmutableList<T> list(final Class<T> resultType) {
        return list(createRunner(), listRowHandler(RowMapper.reflectiveMapper(resultType)));
    }
    /**
     * Execute the statement and return a list of the items based on a Mapper. The {@link RowMapper}
     * is used to map rows from the result set to the desired result
     */
    @NotNull public final <T> ImmutableList<T> list(final RowMapper<T> mapper) {
        return list(createRunner(), listRowHandler(mapper));
    }

    /** Specify argument for the SQL statement. */
    @NotNull public Prepared onArgs(Object... arguments) {
        return new Prepared(this, keyColumnName, inputArguments(arguments), CLOSE_AFTER_RUN);
    }

    /** Create a Prepared statement based on the current one. */
    @NotNull public Prepared prepare() {
        return new Prepared(this, keyColumnName, emptyList());
    }

    /** Return generated keys. */
    @NotNull public SqlStatement returnKeys(String columnName) {
        keyColumnName = columnName;
        return this;
    }

    /** run the statement with the specified result handler. */
    @Nullable public <T> T run(@NotNull ResultHandler<T> handler) {
        return createRunner().run(handler);
    }

    /** run the statement with the specified result handler. */
    @Nullable public <T> T run(@NotNull ResultHandler<T> handler, boolean transactional) {
        return createRunner().run(handler);
    }

    /**
     * The number of seconds the driver will wait for a <code>Statement</code> to execute, 0 means
     * no timeout.
     */
    @NotNull public SqlStatement timeout(int seconds) {
        timeout = seconds;
        return this;
    }

    /** Log info the sql to be executed. */
    @SuppressWarnings("WeakerAccess")
    public SqlStatement withInfo() {
        info = true;
        return this;
    }

    /** Use SchemaProps. */
    @SuppressWarnings("WeakerAccess")
    public SqlStatement withSchemaProps(SchemaProps props) {
        schemaProps = props;
        return this;
    }

    /** Mark the statement as having an updatable result set. */
    public final SqlStatement withUpdatableResultSet() {
        flags.add(UPDATABLE_RESULT_SET);
        return this;
    }

    /** Return the database type. */
    public DatabaseType getDatabaseType() {
        return database.getDatabaseType();
    }

    /** Execute the statement and return an int. Or null if not entries are present. */
    @Nullable public Integer getInt() {
        return run(singleRowHandler(rs -> rs.getInt(1)));
    }

    /** Execute the statement and return a long. Or null if not entries are present. */
    @Nullable public Long getLong() {
        return run(singleRowHandler(rs -> rs.getLong(1)));
    }
    void afterRun() {}

    Runner createRunner() {
        return createRunner(database.getConnectionRef(), null);
    }
    Runner createRunner(ConnectionReference<Connection> cr, @Nullable AutoCloseable resource) {
        return new Runner(cr, dml -> createDefault(this, cr, retrieveSql(), dml, resource));
    }

    void handleException(final SQLException e) {
        handleException(e, "");
    }

    void handleException(final SQLException sqlException, final String statement) {
        if (flags.contains(IGNORE_ERRORS)) return;

        final DatabaseException e = database.getDatabaseType().getSqlExceptionTranslator().translate(sqlException, statement);
        if (e.mustLog()) logger.error(e.getMessage(), e);
        throw e;
    }

    private boolean allComments(String statement) {
        for (final String line : Strings.split(statement, '\n')) {
            if (!line.startsWith("--")) return false;
        }
        return true;
    }

    private void executeScript(Predicate<DatabaseException> handler, ConnectionReference<Connection> connectionReference) {
        final String                    s = retrieveSql();
        ConnectionReference<Connection> c = connectionReference;
        for (final String statement : split(s)) {
            if (!allComments(statement)) {
                final SqlStatement stmt = new SqlStatement(database, singletonList(statement), flags);
                if (!flags.contains(IGNORE_ERRORS)) stmt.ignoreErrors(statement.contains(IGNORE_ERRORS_DIRECTIVE));
                try {
                    executeScriptStatement(c, stmt);
                }
                catch (final DatabaseException e) {
                    if (handler.test(e)) {
                        c = database.getConnectionRef();
                        executeScriptStatement(c, stmt);
                    }
                }
            }
        }
    }

    private void executeScriptStatement(ConnectionReference<Connection> c, SqlStatement stmt) {
        stmt.flags.remove(PRE_PROCESS);
        stmt.createRunner(c, null).ensureTransactionStarted().run(ignore -> null);
    }

    @NotNull private <T> ImmutableList<T> list(Runner runner, ResultHandler<ImmutableList<T>> handler) {
        final ImmutableList<T> result = runner.run(handler);
        return result != null ? result : emptyList();
    }

    private String retrieveSql() {
        final String s         = flags.contains(PRE_PROCESS) ? asLines(database.createPreprocessor(schemaProps).process(sql)) : asString();
        final String nativeSql = s.endsWith("\n") ? s.substring(0, s.length() - 1) : s;

        if (limit == MAX_VALUE && offset == 0) return nativeSql;

        // Check if the 'for update' flag is present. Because it needs to be pulled to the outer scope
        final int    fu   = nativeSql.indexOf(DbMacro.ForUpdate.getStringValue());
        final String head;
        final String tail;
        if (fu != -1) {
            head = nativeSql.substring(0, fu - 1);
            tail = nativeSql.substring(fu - 1);
        }
        else {
            head = nativeSql;
            tail = "";
        }

        return database.getDatabaseType().limit(head, offset, limit) + tail;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Register a listener that will be invoked each time a Statement is being executed in the
     * current thread.
     */
    public static void addCurrentStatementListener(Consumer<SqlStatement.Terminator> currentStatementListener) {
        statementListener.get().add(currentStatementListener);
    }

    /** Remove a listener. */
    public static void removeCurrentStatementListeners() {
        statementListener.get().clear();
    }

    private static List<String> split(String sql) {
        if (sql.trim().isEmpty()) return emptyList();
        final List<String> result = new ArrayList<>();

        final Matcher m = splitPattern.matcher(sql);

        int begin = 0;
        while (m.find()) {
            final int start = m.start();
            if (sql.charAt(start) == ';') {
                final String s = sql.substring(begin, start).trim();
                if (!s.isEmpty()) result.add(s);
                begin = m.end();
            }
        }
        final String s = sql.substring(begin).trim();
        if (!s.isEmpty()) result.add(s);

        return result;
    }

    //~ Static Fields ................................................................................................................................

    static final ThreadLocal<Set<Consumer<SqlStatement.Terminator>>> statementListener = ThreadLocal.withInitial(HashSet::new);

    private static final String IGNORE_ERRORS_DIRECTIVE = "Ignore Errors";

    private static final Logger logger = Logger.getLogger(SqlStatement.class);

    private static final Predicate<DatabaseException> DEFAULT_HANDLER = e -> { throw e; };

    private static final Pattern splitPattern = Pattern.compile("\"([^\"\\\\]|\\\\.)*\"|'([^'\\\\]|\\\\.)*'|;;");

    //~ Inner Interfaces .............................................................................................................................

    /**
     * A handler over the current statement being executed to manage termination.
     */
    public interface Terminator {
        /** Try to cancel the current execution. */
        void cancel();
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * A Prepared SQL Statement.
     */
    public static class Prepared extends SqlStatement implements AutoCloseable {
        final ConnectionReference<Connection> connection;
        final String                          nativeSql;
        private final List<Argument>          arguments;
        private boolean                       batch;
        private final boolean                 doNotReturnKey;
        private final List<Long>              results;
        private final PreparedStatement       stmt;

        Prepared(SqlStatement sql, String keyColumnName, List<Argument> args, Flag... fs) {
            super(sql.database, sql.sql, withFlags(sql.flags, fs));
            arguments = new ArrayList<>();
            arguments.addAll(args);
            connection     = database.getConnectionRef();
            doNotReturnKey = keyColumnName.isEmpty();

            nativeSql = sql.retrieveSql();
            results   = doNotReturnKey || database.getDatabaseType() != ORACLE ? null : new ArrayList<>();
            batch     = false;
            timeout   = sql.timeout;
            stmt      = connection.dryWriter() != null ? null : doPrepareStatement(keyColumnName);
        }

        /** Add to current batch. */
        public void batch() {
            if (results != null) {
                // Hack for Oracle
                results.add(insertWithKey());
                return;
            }

            try {
                connection.ensureTransactionStarted();
                if (stmt != null) {
                    Argument.setArguments(stmt, arguments);
                    batch = true;
                    stmt.addBatch();
                }
            }
            catch (final SQLException e) {
                handleException(e, stmt.toString());
            }
        }

        /** close the statement. */
        public void close() {
            try {
                if (stmt != null) stmt.close();
            }
            catch (final SQLException e) {
                handleException(e);
            }
            finally {
                connection.detach(this);
            }
        }

        /** Execute all batched statements. */
        public void executeBatch() {
            if (batch) {
                try {
                    stmt.setQueryTimeout(timeout);
                    stmt.executeBatch();
                }
                catch (final SQLException e) {
                    handleException(e);
                }
            }
        }

        /** Execute all batched statements and return the generated keys. */
        @NotNull public List<Long> executeBatchWithKeys() {
            if (results != null) {
                // Hack for Oracle
                results.clear();
                return new ArrayList<>(results);
            }
            if (!batch) return emptyList();

            validateReturnKey();
            final List<Long> result = new ArrayList<>();
            try {
                stmt.executeBatch();
                final ResultSet rs = stmt.getGeneratedKeys();
                while (rs.next())
                    result.add(rs.getLong(1));
            }
            catch (final SQLException e) {
                handleException(e, stmt.toString());
            }
            return result;
        }

        /** Execute the statement as an Insert one and return the generated Key. */
        @Nullable public Long insertWithKey() {
            validateReturnKey();
            return createRunner().ensureTransactionStarted().run(s -> getGeneratedKey(stmt));
        }

        @NotNull @Override public Prepared onArgs(final Object... args) {
            arguments.clear();
            arguments.addAll(inputArguments(args));
            return this;
        }

        @NotNull @Override public Prepared timeout(int seconds) {
            super.timeout(seconds);
            try {
                stmt.setQueryTimeout(seconds);
            }
            catch (final SQLException e) {
                handleException(e);
            }
            return this;
        }

        /** Return the prepared statement. */
        public PreparedStatement getPreparedStatement() {
            return stmt;
        }

        @Override void afterRun() {
            if (flags.contains(CLOSE_AFTER_RUN)) close();
        }

        PreparedStatement createPreparedStatement(String key, Connection c)
            throws SQLException
        {
            if (doNotReturnKey)
                return c.prepareStatement(nativeSql, TYPE_FORWARD_ONLY, flags.contains(UPDATABLE_RESULT_SET) ? CONCUR_UPDATABLE : CONCUR_READ_ONLY);
            if (database.getDatabaseType().has(DbMacro.NeedsCreateSequence)) return c.prepareStatement(nativeSql, new String[] { key });

            return c.prepareStatement(nativeSql, RETURN_GENERATED_KEYS);
        }

        @Override Runner createRunner(ConnectionReference<Connection> cr, @Nullable AutoCloseable resource) {
            return new Runner(connection, dml -> createPrepared(this, stmt, nativeSql, arguments, dml, cr.dryWriter()));
        }

        List<Argument> getArguments() {
            return arguments;
        }

        private PreparedStatement doPrepareStatement(final String key) {
            try {
                final Connection        c  = connection.get(this);
                final PreparedStatement ps = createPreparedStatement(key, c);
                ps.setQueryTimeout(timeout);
                return ps;
            }
            catch (final SQLException e) {
                connection.detach(this);
                handleException(e, nativeSql);
                throw new RuntimeException(e);
            }
        }

        private void validateReturnKey() {
            if (doNotReturnKey) throw new IllegalStateException("Invoke 'returnKeys' before preparing the statement");
        }

        @Nullable private Long getGeneratedKey(final Statement s)
            throws SQLException
        {
            final ResultSet rs = s.getGeneratedKeys();
            final boolean   b  = rs.next();
            return b ? rs.getLong(1) : null;
        }

        static EnumSet<Flag> withFlags(EnumSet<Flag> f1, Flag... fs) {
            if (fs.length == 0) return f1;
            final EnumSet<Flag> result = EnumSet.copyOf(f1);
            addAll(result, fs);
            return result;
        }
    }  // end class Prepared

    class Runner {
        private final ConnectionReference<Connection>   connection;
        private final Function<Boolean, StatementProxy> creator;

        Runner(final ConnectionReference<Connection> connection, Function<Boolean, StatementProxy> creator) {
            this.connection = connection;
            this.creator    = creator;
        }

        @NotNull StatementProxy createStatement(boolean dml) {
            return creator.apply(dml);
        }

        Runner ensureTransactionStarted() {
            if (database.configuration.logStatementsEnabled) SqlExecutionLogger.setTransactionListener(database);
            connection.ensureTransactionStarted();
            return this;
        }
        @Nullable <T> T run(@NotNull ResultHandler<T> handler) {
            try(final StatementProxy s = creator.apply(false)) {
                s.executeStatement();
                return s.handle(handler);
            }
            finally {
                afterRun();
            }
        }

        private int runDml() {
            try(StatementProxy s = creator.apply(true)) {
                return s.executeStatement();
            }
            finally {
                afterRun();
            }
        }
    }  // end class Runner
}  // end class SqlStatement
