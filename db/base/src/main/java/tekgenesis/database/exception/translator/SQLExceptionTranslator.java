
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.exception.translator;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.database.exception.DatabaseException;
import tekgenesis.database.exception.SQLExceptionType;

import static tekgenesis.database.exception.SQLExceptionType.*;

/**
 * Translates an SQLException to a DatabaseException.
 */
@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
public abstract class SQLExceptionTranslator {

    //~ Instance Fields ..............................................................................................................................

    final Map<String, SQLExceptionType>         errorCodeMapping = new HashMap<>();
    private final Map<String, SQLExceptionType> sqlStateMapping  = new HashMap<>();

    //~ Constructors .................................................................................................................................

    SQLExceptionTranslator() {
        populate();
    }

    //~ Methods ......................................................................................................................................

    /**
     * Translates an SQLException to a DatabaseException.
     *
     * @param   e  The SQLException
     *
     * @return  The translated DatabaseException
     */
    public DatabaseException translate(@NotNull SQLException e) {
        final SQLException next      = e.getNextException();
        final SQLException exception = e instanceof BatchUpdateException && next != null && (next.getErrorCode() > 0 || next.getSQLState() != null)
                                       ? next
                                       : e.getCause() instanceof SQLException ? (SQLException) e.getCause() : e;

        SQLExceptionType type = errorCodeMapping.get(useSQLStateMapping() ? exception.getSQLState() : Integer.toString(exception.getErrorCode()));

        if (type == null) {
            final String sqlState = getSqlState(e);
            if (sqlState != null && sqlState.length() >= 2) type = sqlStateMapping.get(sqlState.substring(0, 2));
            if (type == null) type = UNSPECIFIED;
        }

        return buildException(type, exception);
    }

    /**
     * Translates an SQLException to a DatabaseException.
     *
     * @param   e          The SQLException
     * @param   statement  The SQL statement that causes the exception
     *
     * @return  The translated DatabaseException
     */
    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    public DatabaseException translate(@NotNull SQLException e, @NotNull String statement) {
        final DatabaseException exception = translate(e);
        exception.setStatement(statement);
        return exception;
    }

    protected abstract void populateErrorCodeMapping();

    DatabaseException buildException(SQLExceptionType type, SQLException exception) {
        return type.buildException(exception);
    }

    /**
     * Indicates if translation should use Exception SQLState for mapping directly instead of using
     * the error code.
     */
    boolean useSQLStateMapping() {
        return false;
    }

    private void populate() {
        populateSqlStatesMapping();
        populateErrorCodeMapping();
    }

    private void populateSqlStatesMapping() {
        sqlStateMapping.put("07", BAD_GRAMMAR);  // Dynamic SQL error
        sqlStateMapping.put("21", BAD_GRAMMAR);  // Cardinality violation
        sqlStateMapping.put("2A", BAD_GRAMMAR);  // Syntax error direct SQL
        sqlStateMapping.put("37", BAD_GRAMMAR);  // Syntax error dynamic SQL
        sqlStateMapping.put("42", BAD_GRAMMAR);  // General SQL syntax error
        sqlStateMapping.put("65", BAD_GRAMMAR);  // Oracle: unknown identifier
        sqlStateMapping.put("S0", BAD_GRAMMAR);  // MySQL uses this - from ODBC error codes?

        sqlStateMapping.put("01", INTEGRITY_VIOLATION);  // Data truncation
        sqlStateMapping.put("02", INTEGRITY_VIOLATION);  // No data found
        sqlStateMapping.put("22", INTEGRITY_VIOLATION);  // Value out of range
        sqlStateMapping.put("23", INTEGRITY_VIOLATION);  // Integrity constraint violation
        sqlStateMapping.put("27", INTEGRITY_VIOLATION);  // Triggered data change violation
        sqlStateMapping.put("44", INTEGRITY_VIOLATION);  // With check violation

        sqlStateMapping.put("08", ACCESS_ERROR);  // Connection exception
        sqlStateMapping.put("53", ACCESS_ERROR);  // PostgreSQL: insufficient resources (e.g. disk full)
        sqlStateMapping.put("54", ACCESS_ERROR);  // PostgreSQL: program limit exceeded (e.g. statement too
                                                  // complex)
        sqlStateMapping.put("57", ACCESS_ERROR);  // DB2: out-of-memory exception / database not started
        sqlStateMapping.put("58", ACCESS_ERROR);  // DB2: unexpected system error
        sqlStateMapping.put("JW", ACCESS_ERROR);  // Sybase: internal I/O error
        sqlStateMapping.put("JZ", ACCESS_ERROR);  // Sybase: unexpected I/O error
        sqlStateMapping.put("S1", ACCESS_ERROR);  // DB2: communication failure

        sqlStateMapping.put("40", CONCURRENCY_ERROR);  // Transaction rollback
        sqlStateMapping.put("61", CONCURRENCY_ERROR);  // Oracle: deadlock

        sqlStateMapping.put("3F", SCHEMA_DOES_NOT_EXISTS_ERROR);  // HSQL.PostgreSQL: Schema does not exists
    }

    private String getSqlState(SQLException e) {
        String sqlState = e.getSQLState();
        if (sqlState == null) {
            final SQLException nextException = e.getNextException();
            if (nextException != null) sqlState = nextException.getSQLState();
        }
        return sqlState;
    }
}  // end class SQLExceptionTranslator
