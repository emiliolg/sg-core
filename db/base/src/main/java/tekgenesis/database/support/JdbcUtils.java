
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.support;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.Times;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Conversions;
import tekgenesis.database.exception.InvalidValueException;
import tekgenesis.database.exception.TypeMismatchException;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.util.Primitives.isPrimitive;
import static tekgenesis.common.util.Primitives.primitiveFor;
import static tekgenesis.common.util.Primitives.wrapperFor;

/**
 * Handy JBC Utilities.
 */
@SuppressWarnings("WeakerAccess")
public class JdbcUtils {

    //~ Constructors .................................................................................................................................

    private JdbcUtils() {}

    //~ Methods ......................................................................................................................................

    /** Close the connection. */
    public static void closeConnection(Connection c) {
        try {
            c.close();
        }
        catch (final SQLException e) {
            logger.warning("Error closing connection " + e.getMessage());
        }
    }

    /** Creates a DateOnly object from a {@link Date} it correct the timezone to UTC. */
    @Nullable public static DateOnly fromSqlDate(@Nullable Date date) {
        return date == null ? null : DateOnly.fromMilliseconds(Times.toMidnight(date));
    }

    /** Returns a Datetime based on a {@link Timestamp}. */
    @Nullable public static DateTime fromTimestamp(Timestamp timestamp) {
        return timestamp == null ? null : DateTime.fromMilliseconds(timestamp.getTime());
    }
    /** Returns the JDBCType for a given scalar type class. */
    public static JDBCType jdbcTypeFor(@Nullable Class<?> clazz) {
        if (clazz == null) return JDBCType.NULL;
        final Class<?> theClass = wrapperFor(clazz);

        final JDBCType jdbcType = scalarTypes.get(theClass);
        if (jdbcType != null) return jdbcType;

        if (Number.class.isAssignableFrom(theClass)) return JDBCType.NUMERIC;
        if (Enum.class.isAssignableFrom(clazz)) return JDBCType.NVARCHAR;
        return JDBCType.NULL;
    }

    /** Returns the JDBCType for a given array of scalar type class. */
    public static JDBCType[] jdbcTypesFor(Class<?>... classes) {
        final JDBCType[] result = new JDBCType[classes.length];
        for (int i = 0; i < classes.length; i++)
            result[i] = jdbcTypeFor(classes[i]);
        return result;
    }

    /** Get the Name and Index of the columns in the result set. */
    public static Map<String, Integer> loadColumns(@NotNull ResultSet rs, final boolean upperCase) {
        try {
            final Map<String, Integer> map = new HashMap<>();
            final ResultSetMetaData    md  = rs.getMetaData();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                final String nm   = md.getColumnLabel(i).toUpperCase();
                final String name = nm.isEmpty() ? md.getColumnName(i) : nm;
                map.put(upperCase ? name.toUpperCase() : name, i);
            }
            return map;
        }
        catch (final SQLException e) {
            logger.error(e);
            return Collections.emptyMap();
        }
    }

    /** Returns a {@link Date}. */
    public static Date toSqlDate(DateOnly date) {
        return new Date(date.toDate().getTime());
    }

    /** Returns a {@link Timestamp}. */
    public static Timestamp toTimestamp(DateTime dateTime) {
        return new Timestamp(dateTime.toMilliseconds());
    }

    /** Get the specified parameter from a callable statement converted to the required type. */
    @Nullable public static <T> T getCallableValue(@NotNull CallableStatement rs, int columnNo, @NotNull Class<T> requiredType)
        throws SQLException
    {
        if (isPrimitive(requiredType)) return cast(getPrimitive(requiredType, rs, columnNo));

        final Object result = getCallableStatementValue(rs, columnNo, requiredType);
        if (result == null || rs.wasNull()) return null;
        if (requiredType.isInstance(result)) return cast(result);
        return convert(result, requiredType);
    }

    /** Return the value converted to the specified type. */
    @Nullable public static <T> T getColValue(ResultSet rs, String columnName, Class<T> requiredType)
        throws SQLException
    {
        return getColValue(rs, rs.findColumn(columnName), requiredType);
    }

    /** Get the specified column from the result set converted to the required type. */
    @Nullable public static <T> T getColValue(@NotNull ResultSet rs, int columnNo, @NotNull Class<T> requiredType)
        throws SQLException
    {
        if (isPrimitive(requiredType)) return cast(getPrimitive(requiredType, rs, columnNo));

        final Object result = getResultSetValue(rs, columnNo, requiredType);
        if (result == null || rs.wasNull()) return null;
        if (requiredType.isInstance(result)) return cast(result);
        return convert(result, requiredType);
    }

    /** Get a Direct connection to the specified Jdbc URL. */
    public static Connection getDirectConnection(String url, String user, String password) {
        try {
            final Connection c = DriverManager.getConnection(url, user, password);
            c.setAutoCommit(true);
            return c;
        }
        catch (final SQLException e) {
            logger.error("Cannot get a connection for: " + url, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns true if <code>clazz</code> is a type that can be considered an scalar and will be
     * handled correctly by {@link #convert(Object, Class)}.
     */
    public static boolean isScalarType(Class<?> clazz) {
        return primitiveFor(clazz) != null || Number.class.isAssignableFrom(clazz) || Enum.class.isAssignableFrom(clazz) ||
               scalarTypes.containsKey(clazz);
    }

    @Nullable static <T> T convert(@Nullable Object value, @NotNull Class<T> requiredType) {
        if (value == null) return null;

        if (String.class.equals(requiredType)) return cast(value.toString());

        if (Number.class.isAssignableFrom(requiredType)) {
            final Class<? extends Number> numberType = cast(requiredType);
            if (value instanceof Number) {
                final Number b = Conversions.numberTo((Number) value, numberType);
                if (b == null) return null;
                return cast(b);
            }
        }

        if (Enumeration.class.isAssignableFrom(requiredType)) {
            final Object o = Enumerations.enumerationValueOf(requiredType, value);
            if (o != null) return cast(o);
            throw new InvalidValueException(1, 1, value, requiredType);
        }
        if (requiredType.isEnum() && value instanceof String)
            return cast(Enumerations.valueOf(Enumerations.asEnumClass(requiredType).getName(), (String) value));

        // // Convert string value to target Number class.
        // return NumberUtils.parseNumber(value.toString(), requiredType);
        // }
        // }
        throw new TypeMismatchException(1, 1, value.getClass(), requiredType);
    }  // end method convert

    @Nullable
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    private static Object getCallableStatementValue(CallableStatement rs, int index, Class<?> requiredType)
        throws SQLException
    {
        // First deal with primitives (Need to check for null)
        final Class<?> primitive = primitiveFor(requiredType);
        if (primitive != null) return getPrimitive(primitive, rs, index);

        // Explicitly extract typed value, as far as possible.
        if (String.class.equals(requiredType)) return rs.getString(index);

        if (BigDecimal.class.equals(requiredType)) return rs.getBigDecimal(index);

        if (DateOnly.class.equals(requiredType)) {
            final Date date = rs.getDate(index);
            return date == null ? null : fromSqlDate(date);
        }

        if (DateTime.class.equals(requiredType)) {
            final Timestamp date = rs.getTimestamp(index);
            return date == null ? null : fromTimestamp(date);
        }

        if (byte[].class.equals(requiredType)) return rs.getBytes(index);

        if (Date.class.equals(requiredType)) return rs.getDate(index);

        if (Time.class.equals(requiredType)) return rs.getTime(index);

        if (Timestamp.class.equals(requiredType) || java.util.Date.class.equals(requiredType)) return rs.getTimestamp(index);

        if (Blob.class.equals(requiredType)) return rs.getBlob(index);

        if (Clob.class.equals(requiredType)) return rs.getClob(index);

        if (InputStream.class.equals(requiredType)) {
            final int columnType = rs.getMetaData().getColumnType(index);
            if (columnType != Types.CLOB) return rs.getBlob(index).getBinaryStream();
            // Do something with HSQLDB
            return rs.getClob(index).getAsciiStream();
        }

        if (Reader.class.equals(requiredType)) return rs.getCharacterStream(index);
        // Some unknown type desired -> rely on getObject.
        return rs.getObject(index);
    }  // end method getCallableStatementValue

    @NotNull private static Object getPrimitive(Class<?> requiredType, ResultSet rs, int index)
        throws SQLException
    {
        if (int.class.equals(requiredType)) return rs.getInt(index);
        if (boolean.class.equals(requiredType)) return rs.getBoolean(index);
        if (long.class.equals(requiredType)) return rs.getLong(index);
        if (double.class.equals(requiredType)) return rs.getDouble(index);

        if (byte.class.equals(requiredType)) return rs.getByte(index);
        if (short.class.equals(requiredType)) return rs.getShort(index);
        if (float.class.equals(requiredType)) return rs.getFloat(index);
        throw Predefined.unreachable();
    }

    @NotNull private static Object getPrimitive(Class<?> requiredType, CallableStatement rs, int index)
        throws SQLException
    {
        if (int.class.equals(requiredType)) return rs.getInt(index);
        if (boolean.class.equals(requiredType)) return rs.getBoolean(index);
        if (double.class.equals(requiredType)) return rs.getDouble(index);
        if (long.class.equals(requiredType)) return rs.getLong(index);

        if (byte.class.equals(requiredType)) return rs.getByte(index);
        if (short.class.equals(requiredType)) return rs.getShort(index);
        if (float.class.equals(requiredType)) return rs.getFloat(index);
        throw Predefined.unreachable();
    }

    @Nullable
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    private static Object getResultSetValue(ResultSet rs, int index, Class<?> requiredType)
        throws SQLException
    {
        // First deal with primitives (Need to check for null)
        final Class<?> primitive = primitiveFor(requiredType);
        if (primitive != null) return getPrimitive(primitive, rs, index);

        // Explicitly extract typed value, as far as possible.
        if (String.class.equals(requiredType)) return rs.getString(index);

        if (BigDecimal.class.equals(requiredType)) return rs.getBigDecimal(index);

        if (DateOnly.class.equals(requiredType)) {
            final Date date = rs.getDate(index);
            return date == null ? null : fromSqlDate(date);
        }

        if (DateTime.class.equals(requiredType)) {
            final Timestamp date = rs.getTimestamp(index);
            return date == null ? null : fromTimestamp(date);
        }

        if (byte[].class.equals(requiredType)) return rs.getBytes(index);

        if (Date.class.equals(requiredType)) return rs.getDate(index);

        if (Time.class.equals(requiredType)) return rs.getTime(index);

        if (Timestamp.class.equals(requiredType) || java.util.Date.class.equals(requiredType)) return rs.getTimestamp(index);

        if (Blob.class.equals(requiredType)) return rs.getBlob(index);

        if (Clob.class.equals(requiredType)) return rs.getClob(index);

        if (InputStream.class.equals(requiredType)) {
            final int columnType = rs.getMetaData().getColumnType(index);
            if (columnType != Types.CLOB) return rs.getBinaryStream(index);
            // Do something with HSQLDB
            return rs.getClob(index).getAsciiStream();
        }

        if (Reader.class.equals(requiredType)) return rs.getCharacterStream(index);
        // Some unknown type desired -> rely on getObject.
        return rs.getObject(index);
    }  // end method getResultSetValue

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(JdbcUtils.class);

    private static final Map<Class<?>, JDBCType> scalarTypes = new HashMap<>();

    static {
        scalarTypes.put(Boolean.class, JDBCType.BOOLEAN);
        scalarTypes.put(Integer.class, JDBCType.INTEGER);
        scalarTypes.put(Long.class, JDBCType.BIGINT);
        scalarTypes.put(BigDecimal.class, JDBCType.DECIMAL);
        scalarTypes.put(String.class, JDBCType.NVARCHAR);
        scalarTypes.put(DateOnly.class, JDBCType.DATE);
        scalarTypes.put(Date.class, JDBCType.DATE);
        scalarTypes.put(Time.class, JDBCType.TIME);
        scalarTypes.put(DateTime.class, JDBCType.TIMESTAMP);
        scalarTypes.put(Timestamp.class, JDBCType.TIMESTAMP);
        scalarTypes.put(Blob.class, JDBCType.BLOB);
        scalarTypes.put(Clob.class, JDBCType.CLOB);
        scalarTypes.put(InputStream.class, JDBCType.BLOB);
        scalarTypes.put(Reader.class, JDBCType.CLOB);
    }
}  // end class JdbcUtils
