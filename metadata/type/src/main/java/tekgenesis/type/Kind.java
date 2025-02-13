
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Resource;
import tekgenesis.common.core.Strings;
import tekgenesis.common.serializer.SerializerException;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.type.resource.AbstractResource;

import static tekgenesis.common.Predefined.cast;

/**
 * Kind enum. This {@link Enum} is used to tag similar types.
 */
@SuppressWarnings("WeakerAccess")
public enum Kind {

    //~ Enum constants ...............................................................................................................................

    ANY(Object.class) { @Override public Type readType(final StreamReader r) { return Types.anyType(); } },
    VOID(Void.class) { @Override public Type readType(final StreamReader r) { return Types.voidType(); } },
    NULL() {
        @Override public Type readType(final StreamReader r) { return Types.nullType(); }

        @Override protected void writeValue(StreamWriter w, Object value) { assert value == null; }

        @Nullable @Override public Object readValue(StreamReader r) { return null; }},
    BOOLEAN(Boolean.class) {
        @Override public Type readType(final StreamReader r) { return Types.booleanType(); }

        @Override protected void writeValue(StreamWriter w, Object value) { w.writeBoolean((Boolean) value); }

        @Override public Boolean readValue(StreamReader r) { return r.readBoolean(); }},

    STRING(String.class) {
        @Override public Type readType(final StreamReader r) {
            final int length = r.readInt();
            return length != -1 ? Types.stringType(length) : Types.stringType();
        }

        @Override protected void writeType(final StreamWriter w, final Type t) {
            final StringType string = (StringType) t;
            w.writeInt(string.getLength().orElse(-1));
        }

        @Override protected void writeValue(StreamWriter w, Object value) { w.writeString((String) value); }

        @Override public String readValue(StreamReader r) { return r.readString(); }},

    DATE_TIME(DateTime.class) {
        @Override public Type readType(final StreamReader r) { return Types.dateTimeType(); }

        @Override protected void writeValue(StreamWriter w, Object value) { w.writeLong(((DateTime) value).toMilliseconds()); }

        @Override public DateTime readValue(StreamReader r) { return DateTime.fromMilliseconds(r.readLong()); }},

    DATE(DateOnly.class) {
        @Override public Type readType(final StreamReader r) { return Types.dateType(); }

        @Override protected void writeValue(StreamWriter w, Object value) { w.writeLong(((DateOnly) value).toMilliseconds()); }

        @Override public DateOnly readValue(StreamReader r) { return DateOnly.fromMilliseconds(r.readLong()); }},

    REAL(Double.class) {
        @Override public boolean isNumber() { return true; }

        @Override public Type readType(final StreamReader r) { return Types.realType(); }

        @Override protected void writeValue(StreamWriter w, Object value) { w.writeDouble(((Number) value).doubleValue()); }

        @Override public Double readValue(StreamReader r) { return r.readDouble(); }},

    INT(Integer.class) {
        @Override public boolean isNumber() { return true; }

        @Override public Type readType(final StreamReader r) { return Types.intType(r.readInt()); }

        @Override protected void writeType(StreamWriter w, Type t) { t.getLength().ifPresent(w::writeInt); }

        @Override protected void writeValue(StreamWriter w, Object value) { w.writeInt(((Number) value).intValue()); }

        @Override public Integer readValue(StreamReader r) { return r.readInt(); }},

    DECIMAL(BigDecimal.class) {
        @Override public boolean isNumber() { return true; }

        @Override public Type readType(final StreamReader r) { return Types.decimalType(r.readInt(), r.readInt()); }

        @Override protected void writeType(final StreamWriter w, final Type t) {
            final DecimalType type = (DecimalType) t;
            w.writeInt(type.getPrecision()).writeInt(type.getDecimals());
        }

        @Override protected void writeValue(StreamWriter w, Object value) { w.writeString(value.toString()); }

        @Override public BigDecimal readValue(StreamReader r) { return new BigDecimal(r.readString()); }},

    ARRAY(Seq.class) {
        @Override public Type readType(final StreamReader r) { return Types.arrayType(instantiateType(r)); }

        @Override protected void writeType(final StreamWriter w, final Type t) {
            final ArrayType array = (ArrayType) t;
            serializeType(w, array.getElementType());
        }

        @Override protected void writeValue(StreamWriter w, Object value) {
            final Iterable<Object> iter = cast(value);
            final Seq<Object>      list = Colls.seq(iter);
            w.writeInt(list.size());

            list.getFirst().map(first -> Types.typeOf(first).getKind()).ifPresent(innerKind -> {
                innerKind.serialize(w);
                list.forEach(item -> innerKind.writeValue(w, item));
            });
        }

        @Override public Seq<?> readValue(StreamReader r) {
            final List<Object> list = new ArrayList<>();
            final int          size = r.readInt();
            if (size > 0) {
                final Kind innerKind = instantiate(r);
                for (int i = 0; i < size; i++)
                    list.add(innerKind.readValue(r));
            }
            return Colls.seq(list);
        }},

    ENUM {
        @Override public Type readType(final StreamReader r) { return EnumType.instantiate(r); }

        @Override protected void writeType(final StreamWriter w, final Type t) { ((EnumType) t).serialize(w); }

        @Override protected void writeValue(StreamWriter w, Object value) { w.writeString((String) value); }

        @Override public String readValue(StreamReader r) { return r.readString(); }},
    RESOURCE(Resource.class) {
        @Override protected Type readType(StreamReader r) { return Types.resourceType(); }

        @Override public Object readValue(StreamReader r) { return AbstractResource.instantiate(r); }

        @Override protected void writeValue(StreamWriter w, Object value) { ((Resource) value).serialize(w); }},
    REFERENCE {
        @Override public Type readType(final StreamReader r) {
            final String domain = r.readString();
            final String name   = r.readString();
            return new EntityReference(domain, name);
        }

        @Override protected void writeType(StreamWriter w, Type t) {
            final EntityReference type = (EntityReference) t;
            w.writeString(type.getDomain());
            w.writeString(type.getName());
        }

        @Override protected void writeValue(StreamWriter w, Object key) { w.writeString((String) key); }

        @Override public String readValue(StreamReader r) { return r.readString(); }},
    HTML { @Override public Type readType(final StreamReader r) { return Types.htmlType(); } },

    TYPE { @Override protected Type readType(StreamReader r) { return Types.anyType(); } };

    //~ Instance Fields ..............................................................................................................................

    @Nullable private final transient Class<?> implementationClass;
    @NotNull private final String              text;

    //~ Constructors .................................................................................................................................

    Kind() {
        this(null);
    }

    Kind(@Nullable Class<?> ic) {
        implementationClass = ic;
        text                = Strings.toCamelCase(name());
    }

    //~ Methods ......................................................................................................................................

    /** Read aon Object of this Kind from the gwt serialization Stream. */
    public Object readValue(StreamReader r) {
        throw new SerializerException("Cannot Instantiate Object  of kind: " + this);
    }

    /** Serialize an Object of this Kind to a stream. */
    public final void serializeValue(StreamWriter w, Object value) {
        serialize(w);
        writeValue(w, value);
    }

    @Override public String toString() {
        return text;
    }

    /**
     * Returns the {@link Class} used to represent this Kind or <code>null</code> if its not
     * defined.
     */
    @Nullable public Class<?> getImplementationClass() {
        return implementationClass;
    }

    /** Returns true if the types is considered a number. */
    public boolean isNumber() {
        return false;
    }

    /** A textual representation of the Kind. */
    @NotNull public String getText() {
        return text;
    }

    protected Type readType(StreamReader r) {
        throw new SerializerException("Cannot Instantiate Type of kind: " + this);
    }

    protected void writeType(StreamWriter w, Type t) {
        // serializing the Kind ordinal may be enough
    }

    protected void writeValue(StreamWriter w, Object value) {
        throw new SerializerException("Cannot Serialize Object : " + value + " of kind: " + this);
    }

    /** Serialize this Kind in a gwt stream. */
    private void serialize(StreamWriter w) {
        w.writeInt(ordinal());
    }

    //~ Methods ......................................................................................................................................

    /** Returns the Kind to this ordinal or NULL if not found. */
    public static Kind fromString(String str) {
        for (final Kind value : VALUES) {
            if (value.getText().equals(str)) return value;
        }
        return NULL;
    }

    /** Instantiate a Kind from the Serialization Stream. */
    public static Kind instantiate(StreamReader r) {
        return valueOf(r.readInt());
    }

    /** Instantiate a Type from the stream. */
    public static Type instantiateType(StreamReader r) {
        return instantiate(r).readType(r);
    }

    /** Serialize an Type in a Serialization stream. */
    public static void serializeType(StreamWriter w, Type type) {
        final Kind kind = type.getKind();
        kind.serialize(w);
        kind.writeType(w, type);
    }

    /** Returns the Kind to this ordinal. */
    public static Kind valueOf(int ordinal) {
        return VALUES[ordinal];
    }

    //~ Static Fields ................................................................................................................................

    private static final Kind[] VALUES = values();
}
