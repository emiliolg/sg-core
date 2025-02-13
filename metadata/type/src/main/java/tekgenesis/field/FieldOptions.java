
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.field;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import tekgenesis.aggregate.AggregateList;
import tekgenesis.check.Check;
import tekgenesis.common.Predefined;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.expr.Expression;
import tekgenesis.expr.RefTypeSolver;
import tekgenesis.type.Type;
import tekgenesis.type.exception.IllegalReferenceException;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.collections.Colls.isInstanceOf;
import static tekgenesis.field.FieldOptionType.*;

/**
 * A Map of FieldOptions.
 */
public class FieldOptions extends EnumMap<FieldOption, Object> implements Iterable<FieldOption> {

    //~ Constructors .................................................................................................................................

    /** Create an instance of the Map. */
    public FieldOptions() {
        super(FieldOption.class);
    }

    /** Copy constructor. */
    public FieldOptions(FieldOptions other) {
        super(FieldOption.class);
        putAll(other);
    }

    //~ Methods ......................................................................................................................................

    /** Compile expression field references. */
    public void compile(RefTypeSolver solver) {
        compile(solver, false);
    }
    /** Compile expression field references. */
    public void compile(RefTypeSolver solver, boolean composite) {
        for (final FieldOption option : this) {
            final FieldOptionType type = option.getType();
            if (type.isExpression()) {
                final Expression expression = getExpression(option);
                final Expression value      = expression.compile(solver);
                if (composite && option == FieldOption.DEFAULT && !value.isConstant())
                    throw new IllegalReferenceException("Only constants are allowed in 'default' option");
                else put(option, value);
            }
            else if (type == CHECK_T) put(option, getCheck(option).compile(solver));
            else if (type == AGGREGATE_T) put(option, getAggregate(option).compile(solver));
        }
    }

    /** Returns true if the value for the options is equal to the specified value. */
    public boolean equal(FieldOption o, @Nullable Object value) {
        if (o.getType() == IDENTIFIER_T || o.getType() == STRING_T) return Predefined.equal(getString(o), value);
        return Predefined.equal(g(o), value);
    }

    @Override public Object get(Object o) {
        throw new UnsupportedOperationException("Use the typed get Methods");
    }

    /** Returns true if the Map has the specified Option. */
    public boolean hasOption(FieldOption o) {
        return containsKey(o);
    }  // && !o.getType().isEmpty(g(o)); }

    @Override public Iterator<FieldOption> iterator() {
        return keySet().iterator();
    }

    @Override public Object put(FieldOption o, Object value) {
        final FieldOptionType type = o.getType();
        if (type.isEmpty(value)) return remove(o);
        final Object convertedValue = type.convert(value);
        if (type.isValid(convertedValue)) return super.put(o, convertedValue);
        throw new IllegalArgumentException("Invalid value '" + value + "' for option " + o);
    }

    /** Serialize to a Stream. */
    public void serialize(StreamWriter writer) {
        writer.writeInt(size());
        for (final FieldOption o : this) {
            o.serialize(writer);
            o.getType().writeValue(writer, g(o));
        }
    }

    /** Get the Option as an String. Return "" if the options does not exists or is empty */
    public String toString(FieldOption option) {
        final Object          value = g(option);
        final FieldOptionType type  = option.getType();
        return type.isEmpty(value) ? "" : type.asString(value);
    }

    /** Get multiple Aggregates. */
    public AggregateList getAggregate(FieldOption option) {
        assert option.getType() == AGGREGATE_T;
        final Object o = g(option);
        return o instanceof AggregateList ? (AggregateList) o : new AggregateList();
    }

    /** Get a Boolean value option, or false. */
    public boolean getBoolean(FieldOption option) {
        assert option.getType() == BOOLEAN_T;
        return g(option) == Boolean.TRUE;
    }

    /** Get multiple Checks. */
    public Check.List getCheck(FieldOption option) {
        assert option.getType() == CHECK_T;
        final Object o = g(option);
        return o instanceof Check.List ? (Check.List) o : Check.List.EMPTY;
    }

    /** Get an Enum value option, or the specified default value. */
    public <T extends Enum<T>> T getEnum(FieldOption option, Class<T> enumClass, T defaultValue) {
        assert option.getType() == ENUM_T;
        final Object g = g(option);
        if (g != null) {
            if (g instanceof Enum) return cast(g);
            if (g instanceof String) {
                final T t = Enum.valueOf(enumClass, (String) g);
                super.put(option, t);
                return t;
            }
        }
        return defaultValue;
    }

    /** return an Expression. */
    public Expression getExpression(FieldOption option) {
        final FieldOptionType type = option.getType();
        assert type.isExpression();
        final Object e = g(option);
        return (Expression) (e instanceof Expression ? e : type.getDefault());
    }

    /** Get a list of fields value option, or default empty list. */
    public Seq<?> getFields(FieldOption option) {
        final FieldOptionType type = option.getType();
        assert type == FIELDS_T;
        final Object o = g(option);
        return (ImmutableList<?>) (o instanceof ImmutableList ? o : type.getDefault());
    }

    /** Get an Integer value option, or 0. */
    public int getInt(FieldOption option) {
        return getInt(option, 0);
    }

    /** Get an Integer value option, or the specified default. */
    public int getInt(final FieldOption option, final int defaultValue) {
        assert option.getType() == UNSIGNED_T;
        final Object g = g(option);
        return g instanceof Integer ? (Integer) g : defaultValue;
    }

    /** Get a MetaModel reference value option, or default empty. */
    public MetaModelReference getMetaModelReference(FieldOption option) {
        final FieldOptionType type = option.getType();
        assert type == METAMODEL_REFERENCE_T;
        final Object o = g(option);
        return (MetaModelReference) (o instanceof MetaModelReference ? o : type.getDefault());
    }

    /** Get a Method option or the empty String otherwise.. */
    public String getMethod(FieldOption option) {
        assert option.getType() == METHOD_T;
        final Object o = g(option);
        return o instanceof String ? (String) o : "";
    }

    /** Get a String value option or the empty String otherwise. */
    @Nullable
    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    public Function<?, ?> getMethodRef(FieldOption option) {
        assert option.getType() == METHOD_REF_T;
        final Object o = g(option);
        return o instanceof Function ? (Function<?, ?>) o : null;
    }

    /** Get a String value option or the empty String otherwise. */
    public String getString(FieldOption option) {
        assert option.getType() == STRING_T || option.getType() == IDENTIFIER_T;
        final Object o = g(option);
        return o instanceof String ? (String) o : "";
    }

    /** Get a List of strings value option or the empty list otherwise. */
    public List<String> getStrings(FieldOption option) {
        assert option.getType() == IDENTIFIERS_T;
        final Object o = g(option);
        if (isInstanceOf(o, List.class, String.class)) return cast(o);
        return emptyList();
    }

    /** Get a type value option, or default nullType. */
    public Type getType(FieldOption option) {
        final FieldOptionType type = option.getType();
        assert type == TYPE_T;
        final Object o = g(option);
        return (Type) (o instanceof Type ? o : type.getDefault());
    }

    private FieldOptions deserialize(StreamReader r, int n) {
        for (int i = 0; i < n; i++) {
            final FieldOption o = FieldOption.instantiate(r);
            super.put(o, o.getType().readValue(r));
        }
        return this;
    }

    /**
     * The get is private so it is only called from inside the implementation For public invocations
     * used the typed gets.
     *
     * @see  #getBoolean(FieldOption)
     * @see  #getString(FieldOption)
     * @see  #getExpression(FieldOption)
     * @see  #getCheck(FieldOption)
     * @see  #getAggregate(FieldOption)
     */
    private Object g(FieldOption o) {
        return super.get(o);
    }

    //~ Methods ......................................................................................................................................

    /** Instantiate from serialized data. */
    public static FieldOptions instantiate(StreamReader r) {
        final int n = r.readInt();
        return n == 0 ? EMPTY : new FieldOptions().deserialize(r, n);
    }

    //~ Static Fields ................................................................................................................................

    public static final FieldOptions EMPTY = new FieldOptions();

    private static final long serialVersionUID = 4920620189603092473L;
}  // end class FieldOptions
