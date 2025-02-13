
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import tekgenesis.common.core.IntIntTuple;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Tuple.tuple;

/**
 * A Kind use to simplify treatment of types.
 */
public enum SqlKind {

    //~ Enum constants ...............................................................................................................................

    OTHER(Types.OTHER), REFERENCE(Types.REF),                                //
    DATA_LINK(Types.DATALINK), XML(Types.SQLXML),                            //
    BINARY(Types.BINARY, Types.LONGVARBINARY, Types.VARBINARY),              //
    BOOLEAN(Types.BIT, Types.BOOLEAN),                                       //
    ROW_ID(Types.ROWID), INT(Types.INTEGER, Types.SMALLINT, Types.TINYINT),  //
    BIGINT(Types.BIGINT),                                                    //
    DOUBLE(Types.DOUBLE, Types.FLOAT, Types.REAL),                           //
    BLOB(Types.BLOB),                                                        //
    CLOB(Types.CLOB, Types.NCLOB),                                           //
    OBJECT(Types.ARRAY, Types.DISTINCT, Types.JAVA_OBJECT, Types.STRUCT),

    VARCHAR(Types.CHAR, Types.VARCHAR, Types.LONGVARCHAR) {
        @Override int fixSize(final int size) { return size; }

        @Override public IntIntTuple fixSizes(int s, int p) { return tuple(s, 0); }},

    NVARCHAR(Types.NCHAR, Types.NVARCHAR, Types.LONGNVARCHAR) {
        @Override int fixSize(final int size) { return size; }

        @Override public IntIntTuple fixSizes(int s, int p) { return tuple(s, 0); }},

    DECIMAL(Types.NUMERIC, Types.DECIMAL) {
        @Override int fixSize(final int size) { return size; }

        @Override int fixPrecision(final int precision) { return precision; }

        @Override IntIntTuple fixSizes(int s, int p) { return tuple(s, p); }},

    DATE(Types.DATE), DATETIME(Types.TIME, Types.TIMESTAMP) { @Override int fixPrecision(final int precision) { return precision; } },;

    //~ Constructors .................................................................................................................................

    SqlKind(final int... sqlTypes) {
        for (final int t : sqlTypes)
            TTK.typeToKind.put(t, this);
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if the specified type needs a parameter. */
    public boolean needsParameter() {
        return this == DATETIME || this == NVARCHAR;
    }

    @Override public String toString() {
        return name().toLowerCase();
    }

    /** Fix the precisionfor the given type. */
    int fixPrecision(final int precision) {
        return 0;
    }

    /** Fix the size for the given type. */
    int fixSize(final int size) {
        return 0;
    }

    /** Fix the size and precision for the given type. */
    IntIntTuple fixSizes(final int size, final int precision) {
        return ZERO_ZERO;
    }

    //~ Methods ......................................................................................................................................

    /** Create a Kind given the specified information. */
    public static SqlKind kindFor(final int sqlType) {
        return notNull(TTK.typeToKind.get(sqlType), OTHER);
    }

    //~ Static Fields ................................................................................................................................

    private static final IntIntTuple ZERO_ZERO = tuple(0, 0);

    //~ Inner Classes ................................................................................................................................

    private static class TTK {
        private static final Map<Integer, SqlKind> typeToKind = new HashMap<>();
    }
}
