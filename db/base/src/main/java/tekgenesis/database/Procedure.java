
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.StepResult;
import tekgenesis.database.exception.CallableException;

import static java.lang.String.format;

import static tekgenesis.common.collections.Seq.repeat;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.database.Argument.inputArguments;

/**
 * To Manage invocation of Sql Procedures.
 */
public class Procedure {

    //~ Instance Fields ..............................................................................................................................

    private final List<Argument> arguments;
    private final Database       database;
    private final String         procedure;
    private final Class<?>       returning;

    //~ Constructors .................................................................................................................................

    Procedure(@NotNull Database database, @NotNull QName qName, @Nullable Class<?> returning, Object[] inputArguments) {
        this.database = database;
        procedure     = database.qualifyAndQuote(qName);
        arguments     = new ArrayList<>();
        if (returning != null) arguments.add(new Argument(returning));
        for (final Object o : inputArguments)
            arguments.add(new Argument(o));
        this.returning = returning;
    }

    Procedure(@NotNull Database database, String schemaName, String procedureName, @Nullable Class<?> returning, Object[] inputArguments) {
        this.database = database;
        procedure     = schemaName.isEmpty() ? quoted(procedureName) : format("QName(%s,%s)", schemaName, procedureName);
        arguments     = new ArrayList<>();
        if (returning != null) arguments.add(new Argument(returning));
        for (final Object o : inputArguments)
            arguments.add(new Argument(o));
        this.returning = returning;
    }

    //~ Methods ......................................................................................................................................

    /** Add a Cursor argument. */
    public Procedure cursor() {
        arguments.add(new Argument());
        return this;
    }

    /** Add Input Parameter. */
    public Procedure in(Object value) {
        arguments.add(new Argument(value));
        return this;
    }
    /** Add Input Parameters. */
    public Procedure in(Object... values) {
        for (final Object value : values)
            arguments.add(new Argument(value));
        return this;
    }

    /** Add Input Array Parameter. */
    public Procedure inArray(List<?> value, String typeName) {
        arguments.add(new Argument.Array(value, typeName));
        return this;
    }
    /** Add Input-Output Parameter. */
    public <T> Procedure inOut(T value, Class<T> clazz) {
        arguments.add(new Argument(value, clazz));
        return this;
    }
    /** Invoke a Procedure. */
    public void invoke() {
        try(final Prepared p = prepare()) {
            p.invoke();
        }
    }
    /** Add Output Parameter. */
    public Procedure out(Class<?> clazz) {
        arguments.add(new Argument(clazz));
        return this;
    }
    /** Prepare a Procedure call. */
    public Prepared prepare() {
        final String qs  = repeat("?").take(returning == null ? arguments.size() : arguments.size() - 1).mkString(",");
        final String sql = format(returning == null ? "{ call %s (%s) }" : "{ ? = call %s (%s)", procedure, qs);
        return new Prepared(database.sqlStatement(sql), "", arguments);
    }

    //~ Methods ......................................................................................................................................

    static String invokeStatement(String fn, boolean function, int argc) {
        return format("{ %s call %s (%s) }", function ? "? =" : "", fn, repeat("?").take(function ? argc - 1 : argc).mkString(","));
    }

    //~ Inner Classes ................................................................................................................................

    public static class Prepared implements AutoCloseable {
        private final SqlStatement.Prepared prepared;

        Prepared(SqlStatement sql, String keyColumnName, List<Argument> arguments, SqlStatementBase.Flag... fs) {
            prepared = new SqlStatement.Prepared(sql, keyColumnName, arguments, fs) {
                    CallableStatement createPreparedStatement(String key, Connection c)
                        throws SQLException
                    {
                        return c.prepareCall(nativeSql);
                    }
                };
        }

        @Override public void close() {
            prepared.close();
        }

        /** Invoke a Procedure. */
        public Result invoke() {
            final SqlStatement.Runner runner = prepared.createRunner();
            runner.ensureTransactionStarted();
            try(final StatementProxy s = runner.createStatement(false)) {
                s.executeStatement();
                return new Result((CallableStatement) s.getStatement(), prepared.getArguments(), s.getSqlStatement());
            }
        }
        /** Replace value of input arguments. */
        @NotNull public Prepared onArgs(Object... values) {
            final List<Argument> as = prepared.getArguments();
            if (as.isEmpty()) as.addAll(inputArguments(values));
            else {
                int j = 0;
                for (int i = 0; i < as.size() && j < values.length; i++) {
                    final Argument a = as.get(i);
                    if (a.isIn()) as.set(i, new Argument(values[j++]));
                }
            }

            return this;
        }
    }  // end class Prepared

    public static class Result {
        private final List<Argument>    arguments;
        private final SqlStatement      sqlStatement;
        private final CallableStatement statement;

        Result(CallableStatement statement, List<Argument> arguments, SqlStatement sqlStatement) {
            this.statement    = statement;
            this.arguments    = arguments;
            this.sqlStatement = sqlStatement;
        }

        /** Run {@link RowMapper} over the specified Argument (That must be a cursor). */
        public final <R> Option<R> forEach(int i, final RowHandler<R> handler) {
            try(final ResultSet rs = getCursor(i)) {
                while (rs != null && rs.next()) {
                    final StepResult<R> result = handler.accept(rs);
                    if (result.isDone()) return result.getValue();
                }
            }
            catch (final SQLException e) {
                sqlStatement.handleException(e);
            }
            return Option.empty();
        }
        /** Get the nth parameter index (starting from 0). */
        @Nullable public <T> T get(int i, Class<T> type) {
            try {
                return type.cast(getOutputArgument(i).getOutputValue(statement, i));
            }
            catch (final SQLException e) {
                return null;
            }
        }

        /**
         * Execute the statement over the specified Argument (That must be a cursor), and return a
         * list of results of the specified type.
         */
        @NotNull public final <T> ImmutableList<T> list(int i, final Class<T> resultType) {
            return list(i, RowMapper.reflectiveMapper(resultType));
        }
        /**
         * Execute the statement over the specified Argument (That must be a cursor), and return a
         * list of the items based on a Mapper. The {@link RowMapper} is used to map rows from the
         * result set to the desired result
         */
        @NotNull public final <T> ImmutableList<T> list(int i, final RowMapper<T> mapper) {
            final ImmutableList.Builder<T> result = ImmutableList.builder();
            forEach(i, rs -> {
                    result.add(mapper.mapRow(rs));
                    return StepResult.next();
                });
            return result.build();
        }

        private Argument getArgument(int i) {
            if (i < 0 || i >= arguments.size()) throw new IndexOutOfBoundsException(String.valueOf(i));
            return arguments.get(i);
        }
        @Nullable private ResultSet getCursor(int i)
            throws SQLException
        {
            final Argument argument = getArgument(i);
            if (!argument.isCursor()) throw new CallableException("Not a cursor argument " + i);
            return (ResultSet) argument.getOutputValue(statement, i);
        }

        @NotNull private Argument getOutputArgument(int i) {
            final Argument argument = getArgument(i);
            if (!argument.isOut()) throw new CallableException("Not an output argument " + i);
            return argument;
        }
    }  // end class Result
}  // end class Procedure
