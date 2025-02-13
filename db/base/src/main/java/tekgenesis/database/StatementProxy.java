
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
import java.sql.*;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.database.exception.CallableException;
import tekgenesis.database.exception.SQLExceptionType;
import tekgenesis.transaction.ConnectionReference;

import static java.sql.ResultSet.*;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.database.Argument.interpolateParameters;
import static tekgenesis.database.SqlExecutionLogger.logExecution;
import static tekgenesis.database.SqlStatementBase.Flag.UPDATABLE_RESULT_SET;

/**
 * Wraps the execution of the java.sql.Statement.
 */
class StatementProxy implements AutoCloseable, SqlStatement.Terminator {

    //~ Instance Fields ..............................................................................................................................

    protected final boolean    dml;
    private boolean            canceled     = false;
    private final String       nativeSql;
    private boolean            nop;
    private final SqlStatement sqlStatement;

    //~ Constructors .................................................................................................................................

    StatementProxy(SqlStatement sqlStatement, String nativeSql, boolean dml, boolean nop) {
        this.sqlStatement = sqlStatement;
        this.nativeSql    = nativeSql;
        this.dml          = dml;
        this.nop          = nop;
    }

    //~ Methods ......................................................................................................................................

    @Override public void cancel() {
        canceled = true;
        try {
            if (nop) return;
            getStatement().cancel();
        }
        catch (final SQLException e) {
            handleException(e);
        }
    }

    @Override public void close() {
        SqlStatement.statementListener.get().forEach(c -> c.accept(null));
        if (nop) return;
        try {
            doClose();
        }
        catch (final SQLException e) {
            handleException(e);
        }
    }

    @Override public String toString() {
        return nativeSql;
    }

    void doClose()
        throws SQLException {}

    int doExecute() {
        try {
            if (nop) return 0;
            return doExecute(nativeSql);
        }
        catch (final SQLException e) {
            handleException(e);
            close();
            sqlStatement.database.resetConnection();
            nop = true;
            return 0;
        }
    }

    int doExecute(String s)
        throws SQLException
    {
        return 0;
    }

    @Nullable <T> T doHandle(ResultHandler<T> handler)
        throws SQLException
    {
        final Statement statement = getStatement();
        return handler.handle(statement::getResultSet);
    }

    int executeStatement() {
        SqlStatement.statementListener.get().forEach(c -> c.accept(this));
        return sqlStatement.database.configuration.logStatementsEnabled || sqlStatement.info ? logExecution(toString(), this::doExecute)
                                                                                             : doExecute();
    }

    @Nullable <T> T handle(ResultHandler<T> handler) {
        try {
            if (nop) return null;
            return doHandle(handler);
        }
        catch (final SQLException e) {
            handleException(e);
        }
        return null;
    }

    void handleException(SQLException e) {
        if (canceled) throw SQLExceptionType.EXECUTION_CANCELED.buildException(e);
        sqlStatement.handleException(e, toString());
    }

    @Nullable ResultSet getResultSet() {
        try {
            if (nop) return null;
            return getStatement().getResultSet();
        }
        catch (final SQLException e) {
            handleException(e);
        }
        return null;
    }

    SqlStatement getSqlStatement() {
        return sqlStatement;
    }
    @NotNull Statement getStatement() {
        throw new IllegalStateException("Get Statement");
    }

    //~ Methods ......................................................................................................................................

    static StatementProxy createCall(SqlStatement sqlStatement, @Nullable CallableStatement stmt, String nativeSql, List<Argument> args,
                                     int cursorArg, @Nullable PrintWriter dryWriter) {
        if (stmt == null) return new Dry(ensureNotNull(dryWriter), sqlStatement, interpolateParameters(nativeSql, args));
        try {
            Argument.setArguments(stmt, args);
            return new Call(sqlStatement, stmt, nativeSql, args, cursorArg);
        }
        catch (final SQLException e) {
            sqlStatement.handleException(e, nativeSql);
            return new StatementProxy(sqlStatement, nativeSql, false, true);
        }
    }

    static StatementProxy createDefault(SqlStatement sqlStatement, ConnectionReference<Connection> connection, String nativeSql, boolean dml,
                                        @Nullable AutoCloseable resource) {
        try {
            final PrintWriter dryWriter = connection.dryWriter();
            return dryWriter == null ? new Default(sqlStatement, connection, nativeSql, dml, resource) : new Dry(dryWriter, sqlStatement, nativeSql);
        }
        catch (final SQLException e) {
            sqlStatement.handleException(e, nativeSql);
            return new StatementProxy(sqlStatement, nativeSql, dml, true);
        }
    }
    static StatementProxy createPrepared(SqlStatement sqlStatement, @Nullable PreparedStatement stmt, String nativeSql, List<Argument> args,
                                         boolean dml, @Nullable PrintWriter dryWriter) {
        if (stmt == null) return new Dry(ensureNotNull(dryWriter), sqlStatement, interpolateParameters(nativeSql, args));
        try {
            Argument.setArguments(stmt, args);
            return new Prepared(sqlStatement, stmt, nativeSql, args, dml);
        }
        catch (final SQLException e) {
            sqlStatement.handleException(e, nativeSql);
            return new StatementProxy(sqlStatement, nativeSql, dml, true);
        }
    }

    //~ Inner Classes ................................................................................................................................

    static class Call extends Prepared {
        private final int cursorArg;

        private Call(SqlStatement sqlStatement, @NotNull CallableStatement stmt, String nativeSql, List<Argument> args, int cursorArg) {
            super(sqlStatement, stmt, nativeSql, args, false);
            this.cursorArg = cursorArg;
        }

        @Nullable @Override <T> T doHandle(ResultHandler<T> handler)
            throws SQLException
        {
            if (cursorArg == -1) throw new CallableException("Cannot obtain result set");
            return handler.handle(() -> (ResultSet) getStatement().getObject(cursorArg));
        }

        @NotNull @Override CallableStatement getStatement() {
            return (CallableStatement) super.getStatement();
        }
    }

    static class Default extends StatementProxy {
        private final ConnectionReference<Connection> connection;
        private final AutoCloseable                   resource;
        private final Statement                       statement;

        private Default(SqlStatement sqlStatement, ConnectionReference<Connection> connection, String nativeSql, boolean dml,
                        @Nullable AutoCloseable resource)
            throws SQLException
        {
            super(sqlStatement, nativeSql, dml, false);
            this.connection = connection;
            this.resource   = resource == null ? this : resource;
            try {
                final Connection c = connection.get(this.resource);
                statement = dml
                            ? c.createStatement()
                            : c.createStatement(TYPE_FORWARD_ONLY,
                        sqlStatement.flags.contains(UPDATABLE_RESULT_SET) ? CONCUR_UPDATABLE : CONCUR_READ_ONLY);
                statement.setQueryTimeout(sqlStatement.timeout);
            }
            catch (final SQLException sql) {
                connection.detach(this.resource);
                throw sql;
            }
        }

        @Override public void close() {
            connection.detach(resource);
            super.close();
        }
        void doClose()
            throws SQLException
        {
            statement.close();
        }
        int doExecute(String sql)
            throws SQLException
        {
            if (dml) return statement.executeUpdate(sql);
            statement.execute(sql);
            return 1;
        }
        @NotNull @Override Statement getStatement() {
            return statement;
        }
    }  // end class Default

    static class Dry extends StatementProxy {
        private final PrintWriter pw;

        private Dry(PrintWriter pw, SqlStatement sqlStatement, String nativeSql) {
            super(sqlStatement, nativeSql, false, true);
            this.pw = pw;
        }

        @Override int doExecute() {
            pw.println(toString());
            pw.flush();
            return 1;
        }
    }

    /**
     * Proxy for Prepared Statements.
     */
    static class Prepared extends StatementProxy {
        private final List<Argument>    args;
        private final PreparedStatement statement;

        private Prepared(SqlStatement sqlStatement, @NotNull PreparedStatement stmt, String nativeSql, List<Argument> args, boolean dml) {
            super(sqlStatement, nativeSql, dml, false);
            this.args = args;
            statement = stmt;
        }

        @Override public String toString() {
            return interpolateParameters(super.toString(), args);
        }

        @Override int doExecute(String sql)
            throws SQLException
        {
            if (dml) return statement.executeUpdate();
            statement.execute();
            return 1;
        }

        @NotNull @Override PreparedStatement getStatement() {
            return statement;
        }
    }  // end class Prepared
}  // end class StatementProxy
