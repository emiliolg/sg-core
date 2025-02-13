
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Enumeration;
import tekgenesis.database.support.JdbcUtils;
import tekgenesis.database.type.Lob;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.database.support.JdbcUtils.jdbcTypeFor;

/**
 * Class to manage Arguments in Sql Statements.
 */
class Argument {

    //~ Instance Fields ..............................................................................................................................

    private final boolean in;
    private final boolean out;

    private final Class<?> type;
    private final Object   value;

    //~ Constructors .................................................................................................................................

    Argument() {
        this(null, null, false, false);
    }

    Argument(Object value) {
        this(value, null, true, false);
    }
    Argument(@NotNull Class<?> type) {
        this(null, type, false, true);
    }

    Argument(Object value, @NotNull Class<?> type) {
        this(value, type, true, true);
    }

    private Argument(@Nullable Object value, @Nullable Class<?> type, boolean in, boolean out) {
        this.type  = type;
        this.value = value;
        this.in    = in;
        this.out   = out;
    }

    //~ Methods ......................................................................................................................................

    @Override public String toString() {
        final StringBuilder result = new StringBuilder();
        if (in) result.append('`').append(value).append('`');
        if (out) {
            result.append(':');
            if (type != null) result.append(type.getSimpleName());
        }
        else if (!in) result.append(":cursor");
        return result.toString();
    }

    boolean isIn() {
        return in;
    }

    @Nullable Object getOutputValue(CallableStatement statement, int i)
        throws SQLException
    {
        return isOut() ? type != null ? JdbcUtils.getCallableValue(statement, i + 1, type) : null
                       : isCursor() ? (ResultSet) statement.getObject(i + 1) : null;
    }
    boolean isCursor() {
        return !in && !out;
    }
    boolean isOut() {
        return out;
    }

    void setValue(PreparedStatement stmt, int i)
        throws SQLException
    {
        if (value == null) stmt.setString(i + 1, null);
        else setterFor().setValue(stmt, i + 1, value);
    }

    private Setter<Object> setterFor() {
        if (value == null) return PreparedStatement::setObject;

        final Setter<?> s = setters.get(value.getClass());
        if (s != null) return cast(s);

        for (final Map.Entry<Class<?>, Setter<?>> e : setters.entrySet()) {
            if (e.getKey().isInstance(value)) return cast(e.getValue());
        }
        return PreparedStatement::setObject;
    }

    @Nullable private Integer getVendorType() {
        return isCursor() ? ORACLE_CURSOR : out ? jdbcTypeFor(type).getVendorTypeNumber() : null;
    }

    //~ Methods ......................................................................................................................................

    static List<Argument> inputArguments(Object... values) {
        final List<Argument> result = new ArrayList<>();
        for (final Object value : values)
            result.add(new Argument(value));
        return result;
    }

    @NotNull static String interpolateParameters(String statement, List<Argument> arguments) {
        final StringBuilder result = new StringBuilder();
        int                 n      = 0;
        int                 last   = 0;
        int                 q;
        while ((q = statement.indexOf('?', last)) != -1 && n < arguments.size()) {
            result.append(statement.substring(last, q)).append(arguments.get(n++));
            last = q + 1;
        }
        result.append(statement.substring(last));
        return result.toString();
    }

    /** Set the arguments of the specified Statement. */
    static void setArguments(final PreparedStatement stmt, List<Argument> args)
        throws SQLException
    {
        if (args.isEmpty()) return;
        final CallableStatement callable = stmt instanceof CallableStatement ? (CallableStatement) stmt : null;
        for (int i = 0; i < args.size(); i++) {
            final Argument arg = args.get(i);
            if (arg.isIn()) arg.setValue(stmt, i);
            if (callable != null) {
                final Integer vendorType = arg.getVendorType();
                if (vendorType != null) callable.registerOutParameter(i + 1, vendorType);
            }
        }
    }

    private static <T> void s(Class<T> aClass, Setter<? super T> setter) {
        setters.put(aClass, setter);
    }

    //~ Static Fields ................................................................................................................................

    // Todo make it database independent
    private static final Integer ORACLE_CURSOR = -10;

    private static final Setter<CharSequence> STRING_SETTER = (statement, pos, value) -> statement.setString(pos, value.toString());

    private static final Map<Class<?>, Setter<?>> setters = new LinkedHashMap<>();

    private static final Class<Enumeration<?, Object>> E_CLASS = cast(Enumeration.class);

    static {
        s(CharSequence.class, STRING_SETTER);
        s(String.class, STRING_SETTER);
        s(Date.class, (statement, pos, value) -> statement.setTimestamp(pos, new Timestamp(value.getTime())));
        s(DateTime.class, (statement, pos, value) -> statement.setTimestamp(pos, JdbcUtils.toTimestamp(value)));
        s(DateOnly.class, (statement, pos, value) -> statement.setDate(pos, new java.sql.Date(value.toDate().getTime())));
        s(Boolean.class, PreparedStatement::setBoolean);
        s(Integer.class, PreparedStatement::setInt);
        s(BigDecimal.class, PreparedStatement::setBigDecimal);
        s(Lob.class,
            (statement, pos, lob) -> {
                if (lob.isClob()) statement.setCharacterStream(pos, lob.getReader(), lob.getSize());
                else statement.setBinaryStream(pos, lob.getInputStream(), lob.getSize());
            });
        s(Reader.class, PreparedStatement::setCharacterStream);
        s(InputStream.class, PreparedStatement::setBinaryStream);
        s(E_CLASS, (s, pos, value) -> s.setObject(pos, value.key()));
    }

    //~ Inner Interfaces .............................................................................................................................

    interface Setter<T> {
        void setValue(PreparedStatement statement, int pos, T value)
            throws SQLException;
    }

    //~ Inner Classes ................................................................................................................................

    static class Array extends Argument {
        private final String  typeName;
        private final List<?> values;

        Array(List<?> values, String typeName) {
            super(values, null, true, false);
            this.typeName = typeName;
            this.values   = values;
        }

        @Override void setValue(PreparedStatement stmt, int i)
            throws SQLException
        {
            stmt.setArray(i + 1, stmt.getConnection().createArrayOf(typeName, values.toArray()));
        }
    }
}  // end class Argument
