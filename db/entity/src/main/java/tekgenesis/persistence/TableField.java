
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.core.*;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.database.Database;
import tekgenesis.database.DatabaseType;
import tekgenesis.database.SqlConstants;
import tekgenesis.database.SqlStatement;
import tekgenesis.database.exception.DatabaseException;
import tekgenesis.database.support.JdbcUtils;
import tekgenesis.persistence.expr.Expr;
import tekgenesis.persistence.expr.ExprImpl;
import tekgenesis.persistence.expr.ExprOperator;
import tekgenesis.persistence.expr.ExprVisitor;
import tekgenesis.persistence.resource.DbResource;

import static java.math.BigDecimal.ROUND_HALF_EVEN;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Decimals.scaleAndCheck;
import static tekgenesis.common.core.Integers.checkSignedLength;
import static tekgenesis.common.core.Reals.checkSigned;
import static tekgenesis.common.core.Strings.truncate;
import static tekgenesis.common.util.Conversions.*;
import static tekgenesis.database.DbConstants.EMPTY_CHAR;
import static tekgenesis.database.SqlConstants.asSqlConstant;
import static tekgenesis.persistence.CachedEntityInstanceImpl.dataField;
import static tekgenesis.persistence.expr.ExprOperator.IN;

/**
 * Classes to represents different type of Database Fields.
 */
public abstract class TableField<T> extends ExprImpl<T> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Field field;

    private final String                 fieldName;
    private boolean                      primaryKey;
    @NotNull private final DbTable<?, ?> table;

    //~ Constructors .................................................................................................................................

    protected TableField(@NotNull DbTable<?, ?> table, @NotNull Field field, String columnName) {
        super(columnName);
        this.table = cast(table);
        this.field = field;
        fieldName  = field.getName();
    }

    //~ Methods ......................................................................................................................................

    @Override public <Q> Q accept(ExprVisitor<Q> visitor) {
        return visitor.visit(this);
    }

    /** Returns the alias's table. */
    public String alias() {
        return table.alias();
    }

    @Override public String asSql(final boolean qualify) {
        if (!qualify) return getName();
        final String alias = table.alias();
        return (alias.isEmpty() ? table.metadata().getTableName() : alias) + "." + getName();
    }

    @Override public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TableField)) return false;
        final TableField<?> tf = (TableField<?>) obj;
        return tf.table.equals(table) && tf.fieldName.equals(fieldName);
    }

    /** Returns a value suited to set the field based on an String value. */
    @Nullable public abstract T fromString(@NotNull String value);

    @Override public int hashCode() {
        return fieldName.hashCode() + Constants.HASH_SALT * table.hashCode();
    }

    /** Get a <code>this in values</code> expression. */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public final Criteria in(T first, T... rest) {
        return in(listOf(first).append(rest));
    }

    /** Get a <code>this in expressions</code> expression. */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public final Criteria in(Expr<T> first, Expr<T>... rest) {
        final int n = rest.length;
        if (n == 0) return eq(first);
        final Expr<?>[] ops = new Expr<?>[n + 1];
        ops[0] = first;
        System.arraycopy(rest, 0, ops, 1, n);
        return bool(IN, ops);
    }

    /** Get a <code>this not in values</code> expression. */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public final Criteria notIn(T first, T... rest) {
        return in(first, rest).not();
    }

    @Override public String toString() {
        return table.toString() + "." + fieldName;
    }

    /** Returns the field Name. */
    public String getFieldName() {
        return fieldName;
    }

    /** Sets the value of the field taking it from the specified result set . */
    public <I extends EntityInstance<I, K>, K> void setFromResultSet(I instance, ResultSet rs, int columnIndex) {
        try {
            final T value = getValueFromResultSet(rs, columnIndex);
            setRawValue(instance, value);
        }
        catch (final SQLException e) {
            throw translateException(e);
        }
    }

    /** Set the specified statement parameter . */
    public void setParameter(SqlStatement.Prepared stmt, int i, @Nullable T value) {
        final PreparedStatement p = stmt.getPreparedStatement();
        try {
            if (value == null) p.setObject(i, null);
            else doSetParameter(p, i, value, getDatabase().getDatabaseType());
        }
        catch (final SQLException e) {
            throw translateException(e);
        }
    }

    /** Set the specified parameter with the value of the field in the instance. */
    public <I extends EntityInstance<I, K>, K> void setParameterFromInstance(final SqlStatement.Prepared stmt, final int i, @NotNull I instance) {
        setParameter(stmt, i, getValue(instance));
    }

    /** Return the {@link EntityTable } for this field. */
    @NotNull
    @SuppressWarnings("WeakerAccess")
    public EntityTable<?, ?> getTable() {
        return table.entityTable();
    }

    /** Get the value of the field in its proper type. */
    @Nullable public final <I extends EntityInstance<I, K>, K> T getValue(@NotNull I instance) {
        try {
            return cast(field.get(field.getDeclaringClass().isInstance(instance) ? instance : dataField(instance)));
        }
        catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /** Sets the value of the field using its proper type. */
    public <I extends EntityInstance<I, K>, K> void setValue(@NotNull I instance, @Nullable T value) {
        setRawValue(instance, value);
    }
    // /** Sets the value of the field using an Object. */
    // void setValueFromObject(@NotNull EntityInstance<?, ?> instance, @Nullable Object value) {
    // setValue(instance, getType().cast(value));
    // }
    //
    /** Sets the value of the field from an String. */
    public <I extends EntityInstance<I, K>, K> void setValue(I instance, @Nullable String value) {
        setRawValue(instance, value == null ? null : fromString(value));
    }

    /** Return the value in a format appropriated for inclusion in an SQL statement. */
    public String getValueAsSqlConstant(T value) {
        return value.toString();
    }

    /** Get the value of the field as an String. */
    @Nullable public <I extends EntityInstance<I, K>, K> String getValueAsString(I instance) {
        return Strings.valueOf(getValue(instance));
    }

    /** Returns true if the field is a primary key. */
    public final boolean isPrimaryKey() {
        return primaryKey;
    }

    protected abstract void doSetParameter(final PreparedStatement p, final int i, final T value, DatabaseType databaseType)
        throws SQLException;

    @NotNull protected <I extends EntityInstance<I, K>, K> EntityTable<I, K> entityTable() {
        return cast(table.entityTable());
    }

    protected Database getDatabase() {
        return entityTable().getDatabase();
    }

    /** Sets the value of the field using its proper type. */
    protected final <I extends EntityInstance<I, K>, K> void setRawValue(@NotNull I instance, @Nullable Object value) {
        try {
            field.set(field.getDeclaringClass().isInstance(instance) ? instance : dataField(instance), value);
        }
        catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /** Sets the field as a primary key (Internal method). */
    void primaryKey() {
        primaryKey = true;
    }

    private DatabaseException translateException(SQLException e) {
        return getDatabase().getDatabaseType().getSqlExceptionTranslator().translate(e);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * A Table Field for Boolean Elements.
     */
    public static class Bool extends TableField<Boolean> implements Criteria {
        Bool(DbTable<?, ?> t, Field field, String columnName) {
            super(t, field, columnName);
        }

        @Override public Boolean fromString(@NotNull String value) {
            return Boolean.valueOf(value);
        }

        @Override public String getValueAsSqlConstant(final Boolean value) {
            return asSqlConstant(value);
        }

        @Override protected void doSetParameter(final PreparedStatement p, final int i, final Boolean value, DatabaseType databaseType)
            throws SQLException
        {
            p.setBoolean(i, value);
        }
    }

    /**
     * /** A Table Field for Strings.
     */
    public static class Clob extends TableField<String> {
        private final int length;

        /**  */
        public Clob(DbTable<?, ?> t, Field field, String columnName, int length) {
            super(t, field, columnName);
            this.length = length;
        }

        @Nullable @Override public String fromString(@NotNull String value) {
            return truncate(value, length);
        }

        @NotNull @Override public Class<String> getType() {
            return String.class;
        }

        @Override public <I extends EntityInstance<I, K>, K> void setValue(@NotNull I instance, @Nullable String value) {
            String finalValue = value;

            if (isNotEmpty(finalValue) && finalValue.charAt(0) == EMPTY_CHAR) finalValue = "";
            setRawValue(instance, truncate(finalValue, length));
        }

        @Nullable @Override public <I extends EntityInstance<I, K>, K> String getValueAsString(I instance) {
            return getValue(instance);
        }

        @Nullable @Override public String getValueFromResultSet(ResultSet rs, int columnIndex)
            throws SQLException
        {
            @Nullable final String string;
            if (getDatabase().getDatabaseType().supportsLobs()) {
                final java.sql.Clob clob = rs.getClob(columnIndex);
                if (clob != null) {
                    final char[] chars = new char[(int) clob.length()];
                    final int    read;
                    try {
                        read = clob.getCharacterStream().read(chars);
                    }
                    catch (final IOException e) {
                        throw new SQLException(e);
                    }
                    string = read > 0 ? String.valueOf(chars, 0, read) : "";
                }
                else string = null;
            }
            else string = rs.getString(columnIndex);
            return isNotEmpty(string) && string.charAt(0) == EMPTY_CHAR ? "" : string;
        }

        @Override protected void doSetParameter(final PreparedStatement p, final int i, final String value, DatabaseType databaseType)
            throws SQLException
        {
            if (!databaseType.supportsEmptyString() && "".equals(value)) p.setString(i, String.valueOf(EMPTY_CHAR));
            else {
                if (databaseType.supportsLobs()) p.setClob(i, new StringReader(value), value.length());
                else p.setString(i, value);
            }
        }  // end method doSetParameter
    }  // end class Clob

    /**
     * A Table Field for DateOnly.
     */
    public static class Date extends TableField<DateOnly> implements Expr.Date {
        /** Constructor. */
        Date(DbTable<?, ?> t, Field field, String columnName) {
            super(t, field, columnName);
        }

        @Override public DateOnly fromString(@NotNull String value) {
            return DateOnly.fromString(value);
        }

        @Override public String getValueAsSqlConstant(final DateOnly value) {
            return asSqlConstant(value);
        }

        @Override protected void doSetParameter(final PreparedStatement p, final int i, final DateOnly value, DatabaseType databaseType)
            throws SQLException
        {
            p.setDate(i, new java.sql.Date(value.toDate().getTime()));
        }
    }

    /**
     * A Table Field for Decimals.
     */
    public static class Decimal extends TableField<BigDecimal> implements Expr.Decimal {
        private final int     decimals;
        private final int     precision;
        private final boolean signed;

        Decimal(DbTable<?, ?> t, Field field, String columnName, boolean signed, int precision, int decimals) {
            super(t, field, columnName);
            this.signed    = signed;
            this.precision = precision;
            this.decimals  = decimals;
        }

        @Nullable @Override public BigDecimal fromString(@NotNull String value) {
            return scaleAndCheck(toString(), toDecimal(value), signed, precision, decimals);
        }

        @Override public int getDecimals() {
            return decimals;
        }

        @Override public <I extends EntityInstance<I, K>, K> void setValue(@NotNull I instance, BigDecimal value) {
            setRawValue(instance, scaleAndCheck(toString(), value, signed, precision, decimals));
        }

        @Nullable @Override public <I extends EntityInstance<I, K>, K> String getValueAsString(I instance) {
            final BigDecimal value = super.getValue(instance);
            return value == null ? null : value.setScale(decimals, ROUND_HALF_EVEN).toPlainString();
        }

        @Override protected void doSetParameter(final PreparedStatement p, final int i, final BigDecimal value, DatabaseType databaseType)
            throws SQLException
        {
            p.setBigDecimal(i, value);
        }
    }  // end class Decimal

    /**
     * A Table Field for DateTime.
     */
    public static class DTime extends TableField<DateTime> implements Expr.DTime {
        /** Constructor. */
        DTime(DbTable<?, ?> t, Field field, String columnName) {
            super(t, field, columnName);
        }

        @Override public DateTime fromString(@NotNull String value) {
            return DateTime.fromString(value);
        }

        @Override public String getValueAsSqlConstant(final DateTime value) {
            return asSqlConstant(value);
        }

        @Override protected void doSetParameter(final PreparedStatement p, final int i, final DateTime value, DatabaseType databaseType)
            throws SQLException
        {
            p.setTimestamp(i, JdbcUtils.toTimestamp(value));
        }
    }

    public static class Enum<T extends java.lang.Enum<T> & Enumeration<T, I>, I> extends TableField<T> implements Expr.Enum<T, I> {
        private final Class<T> enumType;

        Enum(DbTable<?, ?> t, Field field, String columnName, Class<T> enumType) {
            super(t, field, columnName);
            this.enumType = enumType;
        }

        @Override public T fromString(@NotNull String value) {
            final T r = cast(Enumerations.enumerationValueOf(getType(), value));
            if (r != null) return r;

            final Class<T> t = cast(getType());
            return Enumerations.valueOf(t, value);
        }

        @NotNull @Override public Class<T> getType() {
            return enumType;
        }

        @Override public String getValueAsSqlConstant(final T value) {
            return asSqlConstant(value);
        }

        @Override protected void doSetParameter(final PreparedStatement p, final int i, final T value, DatabaseType databaseType)
            throws SQLException
        {
            p.setObject(i, value.key());
        }
    }

    /**
     * A Table field for sets of enumerations.
     */
    public static class EnumerationSet<T extends java.lang.Enum<T> & Enumeration<T, I>, I> extends TableField<EnumSet<T>>
        implements Expr.EnumerationSet<T, I>
    {
        private final Class<T> enumType;

        EnumerationSet(DbTable<?, ?> t, Field field, String columnName, Class<T> enumType) {
            super(t, field, columnName);
            this.enumType = enumType;
        }

        @Override public EnumSet<T> fromString(@NotNull String value) {
            return Enumerations.enumSet(enumType, value);
        }
        @NotNull @Override public Class<T> getEnumType() {
            return enumType;
        }

        @Override public String getValueAsSqlConstant(final EnumSet<T> value) {
            return String.valueOf(Enumerations.asLong(value));
        }

        @Override protected void doSetParameter(final PreparedStatement p, final int i, final EnumSet<T> value, DatabaseType databaseType)
            throws SQLException
        {
            p.setObject(i, Enumerations.asLong(value));
        }
    }

    /**
     * A Table Field for Integers.
     */
    public static class Int extends TableField.Num<Integer, Expr.Int> implements Expr.Int {
        private final int     length;
        private final boolean signed;

        Int(DbTable<?, ?> t, Field field, String name, boolean signed, int length) {
            super(t, field, name);
            this.signed = signed;
            this.length = length;
        }

        @Nullable @Override public Integer fromString(@NotNull String value) {
            return checkSignedLength(toString(), (Integer) toInt(value), signed, length);
        }

        @Override public <I extends EntityInstance<I, K>, K> void setValue(@NotNull I instance, @Nullable Integer value) {
            setRawValue(instance, checkSignedLength(toString(), value, signed, length));
        }

        @Override public <I extends EntityInstance<I, K>, K> void setValue(@NotNull I instance, @Nullable Number number) {
            if (number instanceof Integer || number == null) setValue(instance, (Integer) number);
            else setValue(instance, number.intValue());
        }

        @Override protected void doSetParameter(final PreparedStatement p, final int i, final Integer value, DatabaseType databaseType)
            throws SQLException
        {
            p.setInt(i, value);
        }
    }

    /**
     * A Table Field for Longs.
     */
    public static class LongFld extends TableField.Num<java.lang.Long, Long> implements Long {
        private final int     length;
        private final boolean signed;

        LongFld(DbTable<?, ?> t, Field field, String name, boolean signed, int length) {
            super(t, field, name);
            this.signed = signed;
            this.length = length;
        }

        @Nullable @Override public java.lang.Long fromString(@NotNull String value) {
            return checkSignedLength(toString(), (java.lang.Long) toLong(value), signed, length);
        }

        @Override public <I extends EntityInstance<I, K>, K> void setValue(@NotNull I instance, @Nullable java.lang.Long value) {
            setRawValue(instance, checkSignedLength(toString(), value, signed, length));
        }

        @Override public <I extends EntityInstance<I, K>, K> void setValue(@NotNull I instance, @Nullable Number number) {
            if (number instanceof java.lang.Long || number == null) setValue(instance, (java.lang.Long) number);
            else setValue(instance, number.longValue());
        }

        @Override protected void doSetParameter(final PreparedStatement p, final int i, final java.lang.Long value, DatabaseType databaseType)
            throws SQLException
        {
            p.setLong(i, value);
        }
    }

    /**
     * A Table Field for Integers.
     */
    public abstract static class Num<T extends Number & Comparable<T>, ET extends Comp<T>> extends TableField<T> implements Expr.Num<T, ET> {
        protected Num(@NotNull DbTable<?, ?> table, @NotNull Field field, String columnName) {
            super(table, field, columnName);
        }

        /** Set Value from any Number. */
        public abstract <I extends EntityInstance<I, K>, K> void setValue(@NotNull I instance, @Nullable Number number);
    }

    /**
     * A Table Field for Real Numbers.
     */
    public static class Real extends TableField<Double> implements Expr.Real {
        private final boolean signed;

        Real(DbTable<?, ?> t, Field field, String columnName, boolean signed) {
            super(t, field, columnName);
            this.signed = signed;
        }

        @Nullable @Override public Double fromString(@NotNull String value) {
            return checkSigned(toString(), toDouble(value), signed);
        }

        @Override public <I extends EntityInstance<I, K>, K> void setValue(@NotNull I instance, @Nullable Double value) {
            setRawValue(instance, checkSigned(toString(), value, signed));
        }

        @Override public String getValueAsSqlConstant(Double value) {
            return SqlConstants.asSqlConstant(value);
        }

        @Override protected void doSetParameter(final PreparedStatement p, final int i, final Double value, DatabaseType databaseType)
            throws SQLException
        {
            p.setDouble(i, value);
        }
    }

    /**
     * A {@link TableField} for a Resource.
     */
    public static class Res extends TableField<Resource> {
        /** Create a ResourceField. */
        public Res(DbTable<?, ?> t, Field field, String columnName) {
            super(t, field, columnName);
        }

        @Override public Resource fromString(@NotNull String value) {
            throw new IllegalStateException("Should not be called directly");
        }

        @Override public Expr<Resource> unary(ExprOperator operator) {  // noinspection DuplicateStringLiteralInspection
            throw notImplemented("Not implemented");
        }

        @NotNull @Override public Class<Resource> getType() {
            return Resource.class;
        }

        @Override public <I extends EntityInstance<I, K>, K> void setValue(@NotNull I instance, @Nullable Resource value) {
            setRawValue(instance, value == null || value instanceof DbResource ? value : create(value.toString()));
        }

        @Override public <I extends EntityInstance<I, K>, K> void setValue(I instance, @Nullable String value) {
            setRawValue(instance, isEmpty(value) ? null : create(value));
        }

        @Nullable @Override public Resource getValueFromResultSet(final ResultSet rs, final int columnIndex)
            throws SQLException
        {
            final String str = rs.getString(columnIndex);
            return rs.wasNull() ? null : new DbResource(getDatabase(), str);
        }

        @Override protected void doSetParameter(final PreparedStatement p, final int i, final Resource value, DatabaseType databaseType)
            throws SQLException
        {
            p.setString(i, value.getUuid());
        }

        private DbResource create(String str) {
            return new DbResource(entityTable().getDatabase(), str);
        }
    }  // end class Res

    /**
     * A Table Field for Strings.
     */
    public static class Str extends TableField<String> implements Expr.Str {
        protected final int length;

        /**  */
        public Str(DbTable<?, ?> t, Field field, String columnName, int length) {
            super(t, field, columnName);
            this.length = length;
        }

        @Override public String fromString(@NotNull String value) {
            return truncate(value, length);
        }

        /** Return the maximum size of this Field. */
        public int maxLength() {
            return length;
        }

        @Override public <I extends EntityInstance<I, K>, K> void setValue(@NotNull I instance, @Nullable String value) {
            setRawValue(instance, value == null ? null : !value.isEmpty() && value.charAt(0) == EMPTY_CHAR ? "" : truncate(value, length));
        }

        @Override public String getValueAsSqlConstant(String value) {
            return asSqlConstant(value);
        }

        @Nullable @Override public <I extends EntityInstance<I, K>, K> String getValueAsString(I instance) {
            return getValue(instance);
        }

        @Override protected void doSetParameter(final PreparedStatement p, final int i, final String value, DatabaseType databaseType)
            throws SQLException
        {
            if ("".equals(value)) p.setString(i, databaseType.supportsEmptyString() ? "" : String.valueOf(EMPTY_CHAR));
            else p.setString(i, value);
        }
    }  // end class Str

    public static class StrIntern extends Str {
        /**  */
        public StrIntern(DbTable<?, ?> t, Field field, String columnName, int length) {
            super(t, field, columnName, length);
        }

        @Override public String fromString(@NotNull String value) {
            return truncate(value, length).intern();
        }

        @Override public <I extends EntityInstance<I, K>, K> void setValue(@NotNull I instance, @Nullable String value) {
            setRawValue(instance, value == null ? null : !value.isEmpty() && value.charAt(0) == EMPTY_CHAR ? "" : truncate(value, length).intern());
        }

        @Nullable @Override public String getValueFromResultSet(ResultSet rs, int columnIndex)
            throws SQLException
        {
            final String s = super.getValueFromResultSet(rs, columnIndex);
            return Predefined.isNotEmpty(s) ? s.intern() : s;
        }
    }
}  // end interface TableField
