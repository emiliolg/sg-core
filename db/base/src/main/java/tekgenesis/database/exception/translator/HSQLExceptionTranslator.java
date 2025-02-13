
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
 * SQL Exception translator for HSQL DB.
 */
public class HSQLExceptionTranslator extends SQLExceptionTranslator {

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("MagicNumber")
    protected void populateErrorCodeMapping() {
        // Reference: org.hsqldb.error.ErrorCode

        errorCodeMapping.put("-22", SQLExceptionType.BAD_GRAMMAR);
        errorCodeMapping.put("-28", SQLExceptionType.BAD_GRAMMAR);

        errorCodeMapping.put("-9", SQLExceptionType.INTEGRITY_VIOLATION);
        errorCodeMapping.put("-3500", SQLExceptionType.INTEGRITY_VIOLATION);
        errorCodeMapping.put("-3501", SQLExceptionType.INTEGRITY_VIOLATION);
        errorCodeMapping.put("-104", SQLExceptionType.UNIQUE_VIOLATION);
        errorCodeMapping.put("-105", SQLExceptionType.CHECK_VIOLATION);
        errorCodeMapping.put("-177", SQLExceptionType.FOREIGN_KEY_VIOLATION);
        errorCodeMapping.put("-8", SQLExceptionType.FOREIGN_KEY_VIOLATION);
        errorCodeMapping.put("-10", SQLExceptionType.NOT_NULL_VIOLATION);
        errorCodeMapping.put("-3501", SQLExceptionType.RESTRICT_VIOLATION);

        errorCodeMapping.put("-80", SQLExceptionType.ACCESS_ERROR);
        errorCodeMapping.put("-4000", SQLExceptionType.ACCESS_ERROR);
        errorCodeMapping.put("-5501", SQLExceptionType.INSUFFICIENT_PRIVILEGES);
        errorCodeMapping.put("-5504", SQLExceptionType.DUPLICATE_OBJECT);
        errorCodeMapping.put("-5505", SQLExceptionType.INVALID_NAME);
    }
    @Override DatabaseException buildException(SQLExceptionType type, SQLException exception) {
        switch (type) {
        case SCHEMA_DOES_NOT_EXISTS_ERROR:
            final String msg        = exception.getMessage();
            final int    p          = msg.indexOf(':');
            final int    sp         = p == -1 ? -1 : msg.indexOf(' ', p + 2);
            final String schemaName = sp == -1 ? "" : msg.substring(p + 2, sp);
            return new DatabaseSchemaDoesNotExistsException(exception, schemaName);
        default:
            return super.buildException(type, exception);
        }
    }
}  // end class HSQLExceptionTranslator
