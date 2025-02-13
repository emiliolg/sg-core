
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.exception.translator;

import java.sql.SQLException;

import tekgenesis.database.exception.DatabaseException;
import tekgenesis.database.exception.DatabaseSchemaDoesNotExistsException;
import tekgenesis.database.exception.SQLExceptionType;

/**
 * SQL Exception translator for Oracle DB.
 */
public class OracleExceptionTranslator extends SQLExceptionTranslator {

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("MagicNumber")
    protected void populateErrorCodeMapping() {
        // Reference: http://www.techonthenet.com/oracle/errors/index.php

        errorCodeMapping.put("900", SQLExceptionType.BAD_GRAMMAR);
        errorCodeMapping.put("903", SQLExceptionType.INVALID_NAME);
        errorCodeMapping.put("904", SQLExceptionType.INVALID_NAME);
        errorCodeMapping.put("911", SQLExceptionType.BAD_GRAMMAR);  // Oracle: Bad character
        errorCodeMapping.put("917", SQLExceptionType.BAD_GRAMMAR);
        errorCodeMapping.put("936", SQLExceptionType.BAD_GRAMMAR);
        errorCodeMapping.put("942", SQLExceptionType.INVALID_NAME);
        errorCodeMapping.put("955", SQLExceptionType.DUPLICATE_OBJECT);

        errorCodeMapping.put("17006", SQLExceptionType.BAD_GRAMMAR);

        errorCodeMapping.put("17003", SQLExceptionType.UNSPECIFIED);  // invalidResultSetAccessCodes

        errorCodeMapping.put("1", SQLExceptionType.UNIQUE_VIOLATION);
        errorCodeMapping.put("1400", SQLExceptionType.NOT_NULL_VIOLATION);
        errorCodeMapping.put("1407", SQLExceptionType.NOT_NULL_VIOLATION);
        errorCodeMapping.put("1402", SQLExceptionType.CHECK_VIOLATION);
        errorCodeMapping.put("1722", SQLExceptionType.INTEGRITY_VIOLATION);
        errorCodeMapping.put("2291", SQLExceptionType.FOREIGN_KEY_VIOLATION);
        errorCodeMapping.put("2292", SQLExceptionType.FOREIGN_KEY_VIOLATION);

        errorCodeMapping.put("17002", SQLExceptionType.ACCESS_ERROR);
        errorCodeMapping.put("17447", SQLExceptionType.ACCESS_ERROR);

        errorCodeMapping.put("54", SQLExceptionType.CONCURRENCY_ERROR);

        errorCodeMapping.put("8177", SQLExceptionType.UNSPECIFIED);  // serialization_failure

        errorCodeMapping.put("60", SQLExceptionType.DEADLOCK);  // deadlock_detected

        errorCodeMapping.put("1017", SQLExceptionType.SCHEMA_DOES_NOT_EXISTS_ERROR);
        errorCodeMapping.put("1013", SQLExceptionType.EXECUTION_CANCELED);
        errorCodeMapping.put("1000", SQLExceptionType.LIMIT_EXCEEDED);
        errorCodeMapping.put("1037", SQLExceptionType.LIMIT_EXCEEDED);
        errorCodeMapping.put("2020", SQLExceptionType.LIMIT_EXCEEDED);
        errorCodeMapping.put("1574", SQLExceptionType.LIMIT_EXCEEDED);
        errorCodeMapping.put("18", SQLExceptionType.LIMIT_EXCEEDED);
        errorCodeMapping.put("55", SQLExceptionType.LIMIT_EXCEEDED);
    }  // end method populateErrorCodeMapping

    @Override DatabaseException buildException(SQLExceptionType type, SQLException exception) {
        switch (type) {
        case SCHEMA_DOES_NOT_EXISTS_ERROR:
            final String msg = exception.getMessage();
            final int    p   = msg.lastIndexOf(' ');
            return new DatabaseSchemaDoesNotExistsException(exception, p == -1 ? msg : msg.substring(p + 1));
        default:
            return super.buildException(type, exception);
        }
    }
}  // end class OracleExceptionTranslator
