
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.field;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import tekgenesis.aggregate.AggregateList;
import tekgenesis.check.Check;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.expr.Expression;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.QName.createQName;

/**
 * The Type of Field Option.
 */
public enum FieldOptionType {

    //~ Enum constants ...............................................................................................................................

    /** A special Type for the check option. */
    CHECK_T(true) {
        @Override public boolean isValid(Object value) { return value instanceof Check.List; }

        @Override public Check.List getDefault() { return Check.List.EMPTY; }

        @Override public boolean isExpression() { return false; }

        @Override Check.List readValue(StreamReader r) { return Check.List.instantiate(r); }

        @Override void writeValue(StreamWriter w, Object value) { ((Check.List) value).serialize(w); }},

    /** A special Type for the aggregate option. */
    AGGREGATE_T(true) {
        @Override public boolean isValid(Object value) { return value instanceof AggregateList; }

        @Override public AggregateList getDefault() { return AggregateList.EMPTY; }

        @Override public boolean isExpression() { return false; }

        @Override AggregateList readValue(StreamReader r) { return AggregateList.instantiate(r); }

        @Override void writeValue(StreamWriter w, Object value) { ((AggregateList) value).serialize(w); }},

    /** A Constant String. */
    STRING_T(true) {
        @Override public boolean isValid(Object value) { return value instanceof String; }

        @Override public String getDefault() { return ""; }

        @Override public boolean isExpression() { return false; }

        @Override String readValue(StreamReader r) { return r.readString(); }

        @Override void writeValue(StreamWriter w, Object value) { w.writeString((String) value); }},

    /** A Unsigned Integer. */
    UNSIGNED_T(false) {
        @Override public boolean isValid(Object value) { return value instanceof Integer && ((Integer) value) >= 0; }

        @Override public Integer getDefault() { return 0; }

        @Override public boolean isExpression() { return false; }

        @Override public boolean isEmpty(Object value) { return false; }

        @Override Integer readValue(StreamReader r) { return r.readInt(); }

        @Override void writeValue(StreamWriter w, Object value) { w.writeInt((Integer) value); }},

    /** An Identifier. */
    IDENTIFIER_T(false) {
        @Override public boolean isValid(Object value) { return value instanceof String; }

        @Override public String getDefault() { return ""; }

        @Override public boolean isExpression() { return false; }

        @Override String readValue(StreamReader r) { return r.readString(); }

        @Override void writeValue(StreamWriter w, Object value) { w.writeString((String) value); }},

    /** A set of Identifiers. */
    IDENTIFIERS_T(false) {
        @Override public boolean isValid(Object value) { return Colls.isInstanceOf(value, List.class, String.class); }

        @Override public List<String> getDefault() { return Collections.emptyList(); }

        @Override public boolean isExpression() { return false; }

        @Override List<String> readValue(StreamReader r) { return r.readStrings(); }

        @Override String asString(final Object value) {
            final List<String> list = cast(value);
            return list.isEmpty() ? "" : list.size() == 1 ? list.get(0) : Colls.mkString(list);
        }

        @Override void writeValue(StreamWriter w, Object value) {
            final List<String> list = cast(value);
            w.writeStrings(list);
        }},

    /** A fully qualified metamodel reference. */
    METAMODEL_REFERENCE_T(false) {
        @Override public boolean isValid(Object value) { return value instanceof MetaModelReference; }

        @Override public MetaModelReference getDefault() { return MetaModelReference.EMPTY; }

        @Override public boolean isExpression() { return false; }

        @Override MetaModelReference readValue(StreamReader r) { return new MetaModelReference(createQName(r.readString())); }

        @Override void writeValue(StreamWriter w, Object value) { w.writeString(value.toString()); }},

    /** A Boolean Option (A Modifier). */
    BOOLEAN_T(false) {
        @Override public boolean isValid(Object value) { return value instanceof Boolean; }

        @Override public Boolean getDefault() { return false; }

        @Override public boolean isExpression() { return false; }

        @Override Boolean readValue(StreamReader r) { return r.readBoolean(); }

        @Override void writeValue(StreamWriter w, Object value) { w.writeBoolean(value == Boolean.TRUE); }},

    /** An Option with an Expression with the type of the field. */
    VALUE_EXPR_T(false) { @Override public Expression getDefault() { return Expression.NULL; } },

    /** An Option with an invokable method identifier and a when expression. */
    METHOD_T(false) {
        @Override public boolean isValid(Object value) { return value instanceof String; }

        @Override public String getDefault() { return ""; }

        @Override public boolean isExpression() { return false; }

        @Override String readValue(StreamReader r) { return r.readString(); }

        @Override void writeValue(StreamWriter w, Object value) { w.writeString((String) value); }},

    /** An Option with an invokable method identifier and a when expression. */
    METHOD_REF_T(false) {
        @Override
        @SuppressWarnings("NonJREEmulationClassesInClientCode")
        public boolean isValid(Object value) { return value instanceof Function; }

        @Override public Boolean getDefault() { return false; }

        @Override public boolean isExpression() { return false; }

        @Override Boolean readValue(StreamReader r) { return r.readBoolean(); }

        @Override void writeValue(StreamWriter w, Object value) { w.writeBoolean(true); }},

    /** An Option with a Generic Expression. */
    GENERIC_EXPR_T(false) { @Override public Expression getDefault() { return Expression.NULL; } },

    /** An Option with an array Expression. */
    VALUE_ARRAY_EXPR_T(false) { @Override public Expression getDefault() { return Expression.NULL; } },

    /** An Option with a boolean Expression. */
    BOOLEAN_EXPR_T(false) {
        @Override public Expression getDefault() { return Expression.FALSE; }

        @Override public Object convert(Object value) {
            return value == Boolean.TRUE ? Expression.TRUE : value == Boolean.FALSE ? Expression.FALSE : value;
        }},

    /** An Option with an String Expression. */
    STRING_EXPR_T(true) { @Override public Expression getDefault() { return Expression.EMPTY; } },

    /** An Option with a String Expression List. */
    STRING_EXPRS_T(true) { @Override public Expression getDefault() { return Expression.EMPTY; } },

    /** An Option with a String Expression List. */
    ASSIGNMENT_EXPRS(true) { @Override public Expression getDefault() { return Expression.EMPTY; } },

    /** An Option with an Unsigned Integer Expression. */
    UNSIGNED_EXPR_T(false) { @Override public Expression getDefault() { return Expression.EMPTY; } },

    /** An Option with an array Expression. */
    STRING_ARRAY_EXPR_T(false) { @Override public Expression getDefault() { return Expression.NULL; } },

    /**
     * A special option type for an array of labeled identifiers. (Should never be serialized,
     * virtual?)
     */
    LABELED_IDS_T(true) { @Override public Expression getDefault() { return Expression.NULL; } },

    /**
     * An Option with an Enum value. It can hold an Enum or the String returned by
     * {@link Enum#name()}
     */
    ENUM_T(false) {
        @Override public boolean isValid(Object value) { return value instanceof Enum || value instanceof String; }

        @Override public String getDefault() { return ""; }

        @Override public boolean isExpression() { return false; }

        @Override String readValue(StreamReader r) { return r.readString(); }

        /** It is serialized as a String. */
        @Override void writeValue(StreamWriter w, Object value) {
            final String s = value instanceof String ? (String) value : value instanceof Enum<?> ? ((Enum<?>) value).name() : "";
            w.writeString(s);
        }},

    /** A special option type for a Type value. (Should never be serialized, virtual?) */
    TYPE_T(false) {
        @Override public boolean isValid(Object value) { return value instanceof Type; }

        @Override public Type getDefault() { return Types.nullType(); }

        @Override public boolean isExpression() { return false; }

        @Override Type readValue(StreamReader r) { return Kind.instantiateType(r); }

        @Override void writeValue(StreamWriter w, Object value) { Kind.serializeType(w, (Type) value); }},

    /** A special option type for a list of fields. (Should never be serialized, virtual?) */
    FIELDS_T(false) {
        @Override public boolean isValid(Object value) { return value instanceof ImmutableList; }

        @Override public ImmutableList<?> getDefault() { return Colls.emptyList(); }

        @Override public boolean isExpression() { return false; }};

    //~ Instance Fields ..............................................................................................................................

    private final boolean localizable;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("WeakerAccess")
    FieldOptionType(boolean localizable) {
        this.localizable = localizable;
    }

    //~ Methods ......................................................................................................................................

    /** Try to convert the value to a valid one. */
    public Object convert(Object value) {
        return value;
    }

    /** Check to see if the value is valid for the type. */
    public boolean isValid(Object value) {
        return value instanceof Expression;
    }

    /** Return the Default value for the Option. */
    public abstract Object getDefault();

    /** Returns true if the type is localizable. */
    public boolean isLocalizable() {
        return localizable;
    }

    /** Returns true if the type is an Expression. */
    public boolean isExpression() {
        return true;
    }

    /** returns true if the value is the empty one for the given option. */
    public boolean isEmpty(Object value) {
        return value == null || getDefault().equals(value);
    }

    String asString(final Object value) {
        return value instanceof Enum ? value.toString().toLowerCase() : value.toString();
    }

    /** Read the value from a Stream. */
    Object readValue(StreamReader r) {
        return Expression.instantiate(r);
    }

    /** Write the value to a Stream. */
    void writeValue(StreamWriter w, Object value) {
        ((Expression) value).serialize(w);
    }
}
