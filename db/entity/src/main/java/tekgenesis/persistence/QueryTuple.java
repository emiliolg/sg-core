
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.database.Database;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.expr.Expr;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.collections.Colls.forAll;
import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.common.util.Reflection.*;

/**
 * An untyped Query result.
 */
public abstract class QueryTuple {

    //~ Instance Fields ..............................................................................................................................

    private final Expr<?>[] expressions;

    //~ Constructors .................................................................................................................................

    private QueryTuple(final Expr<?>[] es) {
        expressions = es;
    }

    //~ Methods ......................................................................................................................................

    /** Return the value for the specified Column. */
    @Nullable public <T> T get(Expr<T> expr) {
        for (int i = 0; i < expressions.length; i++) {
            if (expressions[i].equals(expr)) return get(i + 1);
        }
        throw new IllegalArgumentException("Invalid Expression " + expr);
    }

    /** Return the value for the specified Column number (1 based). */
    @Nullable public abstract <T> T get(int columnNumber);

    /** Return the value for the specified Entity. */
    @NotNull public <I extends EntityInstance<I, K>, K> Option<I> get(DbTable<I, K> table) {
        final EntityTable<I, K>               et       = table.entityTable();
        final Map<TableField<Object>, Object> valueMap = new HashMap<>();
        for (int i = 0; i < expressions.length; i++) {
            final Expr<?> e = expressions[i];
            if (e instanceof TableField) {
                final TableField<Object> tf = cast(e);

                if (table.alias().equals(tf.alias()) && tf.entityTable().equals(et)) valueMap.put(tf, get(i + 1));
            }
        }
        if (forAll(valueMap.values(), Objects::isNull)) return Option.empty();
        try {
            final I result = table.metadata().createInstance();
            valueMap.forEach((tf, v) -> tf.setValue(result, v));
            return Option.of(result);
        }
        catch (final IllegalArgumentException e) {
            return Option.empty();
        }
    }

    /** Return the number of elements in the tuple. */
    public int size() {
        return expressions.length;
    }

    /** Return the Tuple as an array of objects. */
    public Object[] toArray() {
        final Object[] a = new Object[expressions.length];
        for (int i = 0; i < a.length; i++)
            a[i] = get(i + 1);
        return a;
    }

    /** Transform the tuple into a list of objects. */
    public ImmutableList<Object> toList() {
        final ImmutableList.Builder<Object> b = ImmutableList.builder(expressions.length);
        for (int i = 0; i < expressions.length; i++)
            b.add(get(i + 1));
        return b.build();
    }

    @Override public String toString() {
        final StrBuilder b = new StrBuilder();
        for (int i = 0; i < expressions.length; i++)
            b.appendElement(exprOrFieldName(expressions[i], i) + "=" + get(i + 1));
        return b.toString();
    }
    /** Return the value for the specified integer Column, or fail if it is null. */
    public int getInt(Expr.Int expr) {
        return getOrFail(expr);
    }

    /** Return an Option for the value of the specified Column. */
    @NotNull public <T> Option<T> getOption(Expr<T> expr) {
        return Option.ofNullable(get(expr));
    }

    /** Return the value for the specified Column number (1 based). */
    @NotNull public <T> T getOrFail(int columnNumber) {
        return ensureNotNull(get(columnNumber), () -> new IllegalArgumentException("Null value for column number: " + columnNumber));
    }

    /** Return the value for the specified Column, or fail if it is null. */
    @NotNull public <T> T getOrFail(Expr<T> expr) {
        return ensureNotNull(get(expr), () -> new IllegalArgumentException("Null value for: " + expr));
    }

    //~ Methods ......................................................................................................................................

    /** A Function to convert from an EntityInstance to a QueryTuple. */
    public static <I extends EntityInstance<I, K>, K> Function<I, QueryTuple> fromEntityInstance(final Expr<?>[] expressions) {
        return new Function<I, QueryTuple>() {
            @Override public QueryTuple apply(final I instance) {
                return new ArrayQueryTuple(expressions, getObjects(instance));
            }
            private Object[] getObjects(I instance) {
                final Object[] vs = new Object[expressions.length];
                for (int i = 0; i < expressions.length; i++) {
                    final Expr<?> e = expressions[i];
                    if (e instanceof TableField) {
                        final TableField<?> tf = cast(e);
                        vs[i] = tf.getValue(instance);
                    }
                }
                return vs;
            }
        };
    }

    /** Return a QueryTuple based on a ResultSet. */
    public static QueryTuple fromResultSet(final ResultSet rs, final Expr<?>[] expressions, @Nullable final Database db) {
        return new QueryTuple(expressions) {
            @Nullable @Override public <T> T get(int columnNumber) {
                try {
                    return cast(expressions[columnNumber - 1].getValueFromResultSet(rs, columnNumber));
                }
                catch (final SQLException e) {
                    if (db == null) throw new RuntimeException(e);
                    throw db.getDatabaseType().getSqlExceptionTranslator().translate(e);
                }
            }
        };
    }

    /** A Row Mapper for result sets. */
    public static <R> RowMapper<R> rowMapper(Class<R> type, final Expr<?>[] expressions) {
        if (type.equals(QueryTuple.class)) return cast(new RowMapper<QueryTuple>() {
                @Override public QueryTuple mapRow(final ResultSet rs)
                    throws SQLException
                {
                    return new ArrayQueryTuple(expressions, getObjects(rs));}
                private Object[] getObjects(ResultSet rs)
                    throws SQLException
                {
                    final Object[] vs = new Object[expressions.length];for (int i = 0; i < expressions.length; i++)
                        vs[i] = expressions[i].getValueFromResultSet(rs, i + 1);return vs;}
            });
        return new ReflectiveMapper<>(type, expressions);
    }

    private static String exprOrFieldName(final Expr<?> expr, final int i) {
        final String name = expr instanceof TableField ? ((TableField<?>) expr).getFieldName() : expr.getName();
        return name.isEmpty() ? String.valueOf(i + 1) : name;
    }

    //~ Inner Classes ................................................................................................................................

    private static class ArrayQueryTuple extends QueryTuple {
        private final Object[] vs;

        ArrayQueryTuple(final Expr<?>[] es, final Object[] vs) {
            super(es);
            this.vs = vs;
        }

        @Nullable public <T> T get(int columnNumber) {
            return cast(vs[columnNumber - 1]);
        }
    }

    private static class ReflectiveMapper<T> implements RowMapper<T> {
        @NotNull private final Class<T>   clazz;
        @NotNull private final Expr<?>[]  expressions;
        @NotNull private final Field[]    mapping;

        private ReflectiveMapper(@NotNull Class<T> clazz, @NotNull Expr<?>[] expressions) {
            this.clazz       = clazz;
            this.expressions = expressions;
            mapping          = fieldMapping(clazz, expressions);
        }

        @Override public T mapRow(@NotNull ResultSet rs)
            throws SQLException
        {
            final T result = construct(clazz);
            for (int i = 0; i < expressions.length; i++) {
                final Field field = mapping[i];
                setFieldValue(result, field, expressions[i].getValueFromResultSet(rs, i + 1));
            }
            return result;
        }

        @NotNull private static Field fieldFor(final Map<String, Field> fields, final Expr<?> e) {
            final Field fc = fields.get(e.getName());
            if (fc != null) return fc;
            if (e instanceof TableField<?>) {
                final Field f = fields.get(((TableField<?>) e).getFieldName());
                if (f != null) return f;
            }
            throw new IllegalArgumentException("Cannot map: " + e);
        }

        private static Field[] fieldMapping(Class<?> clazz, Expr<?>[] es) {
            // Create map  with fields
            final Map<String, Field> fields = new HashMap<>();
            for (final Field fld : getFields(clazz)) {
                final String name = fld.getName();
                fields.put(name, fld);
                fields.put(fromCamelCase(name), fld);
            }
            final Field[] m = new Field[es.length];
            for (int i = 0; i < es.length; i++)
                m[i] = fieldFor(fields, es[i]);
            return m;
        }
    }  // end class ReflectiveMapper
}  // end class QueryTuple
