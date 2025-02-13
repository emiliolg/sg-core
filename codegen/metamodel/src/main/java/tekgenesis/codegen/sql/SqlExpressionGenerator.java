
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.sql;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.database.SqlConstants;
import tekgenesis.expr.Expression;
import tekgenesis.field.TypeField;
import tekgenesis.type.EnumType;
import tekgenesis.type.Type;

import static tekgenesis.database.DbMacro.*;

/**
 * A generator of Sql for Expressions.
 */
class SqlExpressionGenerator {

    //~ Constructors .................................................................................................................................

    private SqlExpressionGenerator() {}

    //~ Methods ......................................................................................................................................

    public static String defaultFor(final TypeField field, boolean multiple) {
        final String sql = sqlFor(field.getDefaultValue(), multiple);
        return !sql.isEmpty() || !field.isRequired() ? sql : defaultForType(field.getType(), multiple);
    }

    private static String defaultForType(final Type type, boolean multiple) {
        switch (type.getKind()) {
        case BOOLEAN:
            return False.name();
        case STRING:
            return EmptyString.name();
        case DATE_TIME:
            return CurrentTime.name();
        case DATE:
            return CurrentDate.name();
        case REAL:
        case INT:
        case DECIMAL:
            return "0";
        case ENUM:
            if (multiple) return "0";
            final EnumType e = (EnumType) type;
            return e.sqlIdFor(e.getDefaultValue(), false);
        default:
            return "";
        }
    }

    @NotNull private static String sqlFor(final Expression e, boolean multiple) {
        if (e.isNull()) return "";
        final Option<Object> constantValue = e.getConstantValue();
        if (constantValue.isEmpty()) return "";
        final Object value = constantValue.get();
        if (e.getType().isEnum() && value instanceof String) return ((EnumType) e.getType()).sqlIdFor((String) value, multiple);
        return SqlConstants.sqlValue(value);
    }
}  // end class SqlExpressionGenerator
