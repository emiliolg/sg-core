
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.util.EnumSet;
import java.util.List;

import static tekgenesis.common.core.Strings.asLines;

/**
 * Base Sql Statement.
 */
class SqlStatementBase {

    //~ Instance Fields ..............................................................................................................................

    protected final EnumSet<SqlStatement.Flag> flags;
    protected boolean                          info;
    /** The sql to use. */
    protected final List<String> sql;
    protected int                timeout;
    final Database               database;

    //~ Constructors .................................................................................................................................

    SqlStatementBase(Database database, List<String> sql) {
        this(database, sql, EnumSet.noneOf(Flag.class));
    }
    SqlStatementBase(Database database, List<String> sql, EnumSet<Flag> flags) {
        this.sql      = sql;
        this.flags    = flags.clone();
        this.database = database;
        timeout       = database.configuration.statementTimeout;
    }

    //~ Methods ......................................................................................................................................

    @Override public String toString() {
        return asString();
    }

    protected String asString() {
        return sql.size() == 1 ? sql.get(0) : asLines(sql);
    }

    //~ Enums ........................................................................................................................................

    enum Flag { RETURN_KEYS, CLOSE_AFTER_RUN, TRANSACTIONAL, IGNORE_ERRORS, UPDATABLE_RESULT_SET, PRE_PROCESS }
}
