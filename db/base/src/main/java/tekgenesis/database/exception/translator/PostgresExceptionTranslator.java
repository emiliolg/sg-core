
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.exception.translator;

import tekgenesis.database.exception.SQLExceptionType;

/**
 * SQL Exception translator for Postgres DB.
 */
public class PostgresExceptionTranslator extends SQLExceptionTranslator {

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("MagicNumber")
    protected void populateErrorCodeMapping() {
        // Taken from: http://www.postgresql.org/docs/9.2/static/errcodes-appendix.html

        errorCodeMapping.put("03000", SQLExceptionType.BAD_GRAMMAR);   // sql_statement_not_yet_complete
        errorCodeMapping.put("42000", SQLExceptionType.BAD_GRAMMAR);   // syntax_error_or_access_rule_violation
        errorCodeMapping.put("42601", SQLExceptionType.BAD_GRAMMAR);   // syntax_error
        errorCodeMapping.put("42602", SQLExceptionType.INVALID_NAME);  // invalid_name
        errorCodeMapping.put("42622", SQLExceptionType.BAD_GRAMMAR);   // name_too_long
        errorCodeMapping.put("42804", SQLExceptionType.BAD_GRAMMAR);   // datatype_mismatch
        errorCodeMapping.put("42P01", SQLExceptionType.INVALID_NAME);  // undefined_table
        errorCodeMapping.put("42710", SQLExceptionType.BAD_GRAMMAR);   // undefined_table

        errorCodeMapping.put("23000", SQLExceptionType.INTEGRITY_VIOLATION);
        errorCodeMapping.put("23505", SQLExceptionType.UNIQUE_VIOLATION);
        errorCodeMapping.put("23502", SQLExceptionType.NOT_NULL_VIOLATION);
        errorCodeMapping.put("23503", SQLExceptionType.FOREIGN_KEY_VIOLATION);
        errorCodeMapping.put("23514", SQLExceptionType.CHECK_VIOLATION);
        errorCodeMapping.put("23P01", SQLExceptionType.EXCLUSION_VIOLATION);

        errorCodeMapping.put("53000", SQLExceptionType.ACCESS_ERROR);             // insufficient_resources
        errorCodeMapping.put("53100", SQLExceptionType.ACCESS_ERROR);             // disk_full
        errorCodeMapping.put("53200", SQLExceptionType.ACCESS_ERROR);             // out_of_memory
        errorCodeMapping.put("53300", SQLExceptionType.ACCESS_ERROR);             // too_many_connections
        errorCodeMapping.put("42501", SQLExceptionType.INSUFFICIENT_PRIVILEGES);  // insufficient_privilege

        errorCodeMapping.put("55P03", SQLExceptionType.CONCURRENCY_ERROR);  // lock_not_available

        errorCodeMapping.put("40001", SQLExceptionType.UNSPECIFIED);  // serialization_failure

        errorCodeMapping.put("40P01", SQLExceptionType.DEADLOCK);          // deadlock_detected
        errorCodeMapping.put("42P07", SQLExceptionType.DUPLICATE_OBJECT);  // Object already exists
        errorCodeMapping.put("57014", SQLExceptionType.EXECUTION_CANCELED);
    }

    @Override boolean useSQLStateMapping() {
        return true;
    }
}  // end class PostgresExceptionTranslator
