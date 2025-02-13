
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.*;

import static tekgenesis.common.collections.Colls.first;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.type.DecimalType.DEFAULT_PRECISION;

/**
 * Utility methods to manipulate and create {@link Type} objects.
 */
@SuppressWarnings("WeakerAccess")  // Utility class, eventually they will be used from outside.
public final class Types {

    //~ Constructors .................................................................................................................................

    private Types() {}

    //~ Methods ......................................................................................................................................

    /** Return an instance of an {@link AnyType }. */
    @NotNull public static AnyType anyType() {
        return AnyType.INSTANCE;
    }

    /** Return an instance of a {@link ArrayType } specifying the element {@link Type}. */
    @NotNull public static ArrayType arrayType(@NotNull Type elementType) {
        return ArrayType.createArrayType(elementType);
    }

    /** Return the basic set of types. */
    public static ImmutableList<AbstractType> basicTypes() {
        return basicTypes;
    }
    /** Return an instance of a {@link BooleanType }. */
    @NotNull public static BooleanType booleanType() {
        return BooleanType.INSTANCE;
    }
    /** Return an instance of a {@link DateTimeType } . */
    @NotNull public static DateTimeType dateTimeType() {
        return dateTimeType(0);
    }

    /** Return an instance of a {@link DateTimeType } . */
    @NotNull public static DateTimeType dateTimeType(int precision) {
        return precision == 0 ? DateTimeType.DATE_TIME_INSTANCE : new DateTimeType(precision);
    }

    /** Return the date only instance of a {@link DateTimeType } . */
    @NotNull public static DateOnlyType dateType() {
        return DateOnlyType.DATE_ONLY_INSTANCE;
    }
    /** Return an instance of a {@link ArrayType } of Decimal. */
    @NotNull public static ArrayType decimalArrayType() {
        return ArrayType.DECIMAL_ARRAY_INSTANCE;
    }

    /** Return an instance of a {@link DecimalType } with default precision. */
    @NotNull public static DecimalType decimalType() {
        return DecimalType.INSTANCE;
    }

    /** Return an instance of a {@link DecimalType } specifying precision and with no decimals. */
    @NotNull public static DecimalType decimalType(int precision) {
        return new DecimalType(precision, 0);
    }

    /** Return an instance of a {@link DecimalType } specifying precision and number of decimals. */
    @NotNull public static DecimalType decimalType(int precision, int decimals) {
        return precision == DEFAULT_PRECISION && decimals == 0 ? decimalType() : new DecimalType(precision, decimals);
    }

    /**
     * Utility method to get the elementType of a Type It will return null except that the Type is
     * an Array.
     */
    @Nullable public static Type elementType(@NotNull Type t) {
        return t instanceof ArrayType ? ((ArrayType) t).getElementType() : null;
    }

    /** Create a basic or extendend type (including html, void & any) based on an String. */
    @NotNull public static Type extendedFromString(@NotNull String typeName, @NotNull Type type) {
        if (type.isNull()) {
            if (htmlType().toString().equals(typeName)) return htmlType();
            else if (voidType().toString().equals(typeName)) return voidType();
            else if (anyType().toString().equals(typeName)) return anyType();
        }
        return type;
    }

    /** Create a basic type based on an String. */
    @NotNull public static Type fromString(@NotNull String str) {
        for (final Type basicType : basicTypes) {
            if (basicType.toString().equals(str)) return basicType;
        }
        return nullType();
    }

    /** Return an instance of a {@link HtmlType }. */
    @NotNull public static HtmlType htmlType() {
        return HtmlType.INSTANCE;
    }

    /** Return an instance of a {@link ArrayType } of Int. */
    @NotNull public static ArrayType intArrayType() {
        return ArrayType.INT_ARRAY_INSTANCE;
    }

    /** Return an instance of an {@link IntType} (with max-length). */
    @NotNull public static IntType intType() {
        return IntType.INSTANCE;
    }

    /** Return an instance of an {@link IntType} specifying its length. */
    @NotNull public static IntType intType(int length) {
        return length == IntType.DEFAULT_INT_LENGTH ? intType() : new IntType(length);
    }

    /** Return an instance of an {@link IntType} (with max-length). */
    @NotNull public static IntType longType() {
        return IntType.LONG_INSTANCE;
    }

    /** Return an instance of an {@link NullType }. */
    @NotNull public static NullType nullType() {
        return NullType.INSTANCE;
    }

    /** Return an instance of a {@link ArrayType } of Real. */
    @NotNull public static ArrayType realArrayType() {
        return ArrayType.REAL_ARRAY_INSTANCE;
    }

    /** Return an instance of a {@link RealType }. */
    @NotNull public static RealType realType() {
        return RealType.INSTANCE;
    }
    /** Returns an instance of a UnresolvedReference Type. */
    @NotNull public static UnresolvedTypeReference referenceType(String fqn) {
        return new UnresolvedTypeReference("", fqn);
    }

    /** Returns an instance of a UnresolvedReference Type. */
    @NotNull public static UnresolvedTypeReference referenceType(String defaultDomain, String target) {
        return new UnresolvedTypeReference(defaultDomain, target);
    }
    /** Return an instance of a {@link ResourceType }. */
    @NotNull public static ResourceType resourceType() {
        return ResourceType.INSTANCE;
    }

    /** Return an instance of a {@link StringType }. */
    @NotNull public static StringType stringType() {
        return StringType.INSTANCE;
    }
    /** Return an instance of a {@link StringType } with the specified length. */
    @NotNull public static StringType stringType(int length) {
        return length <= 0 ? StringType.INSTANCE : new StringType(length);
    }

    /** Return an instance of a {@link StringType } if type blob. */
    @NotNull public static StringType textType() {
        return new StringType(Constants.MEGA);
    }

    /** Return the {@link Type} of a given {@link Object}. */
    @NotNull public static Type typeOf(@Nullable Object o) {
        if (o == null) return nullType();
        if (o instanceof Integer) return intType();
        if (o instanceof Long) return longType();
        if (o instanceof Double || o instanceof Float) return realType();
        if (o instanceof BigDecimal) return decimalType(((BigDecimal) o).precision(), ((BigDecimal) o).scale());
        if (o instanceof String) return stringType(((String) o).length());
        if (o instanceof Boolean) return booleanType();
        if (o instanceof DateOnly) return dateType();
        if (o instanceof DateTime) return dateTimeType();
        if (o instanceof Resource) return resourceType();
        if (o instanceof Iterable) return first((Iterable<?>) o)                                    //
                                          .map(first -> first instanceof Resource.Entry ? resourceType() : arrayType(typeOf(first)))  //
                                          .orElseGet(() -> arrayType(nullType()));

        throw new IllegalArgumentException("Cannot find the type for '" + o + "' : " + o.getClass());
    }  // end method typeOf

    /** Return an instance of a {@link VoidType }. */
    @NotNull public static VoidType voidType() {
        return VoidType.INSTANCE;
    }
    /** Returns a name with a maximum length of 30, used for database constraints. */
    public static String getConstrainedName(String name) {
        return Strings.truncate(name, "_", MAX_CONSTRAINED_NAME_LENGTH).toUpperCase();
    }

    /** Returns a name with a maximum length of 30, used for database constraints. */
    public static String getConstrainedName(String name, String suffix) {
        return Strings.truncate(name, suffix, "_", MAX_CONSTRAINED_NAME_LENGTH).toUpperCase();
    }

    //~ Static Fields ................................................................................................................................

    public static final int MAX_CONSTRAINED_NAME_LENGTH = 30;

    private static final ImmutableList<AbstractType> basicTypes = listOf(stringType(),
            decimalType(),
            dateType(),
            dateTimeType(),
            booleanType(),
            intType(),
            realType(),
            resourceType());
}  // end class Types
