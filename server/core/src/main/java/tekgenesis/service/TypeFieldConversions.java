
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.field.TypeField;
import tekgenesis.type.ArrayType;
import tekgenesis.type.DecimalType;
import tekgenesis.type.StringType;
import tekgenesis.type.Type;

import static tekgenesis.common.core.Decimals.scaleAndCheck;
import static tekgenesis.common.core.Integers.checkSignedLength;
import static tekgenesis.common.core.Reals.checkSigned;
import static tekgenesis.common.core.Strings.truncate;
import static tekgenesis.common.util.Conversions.toDecimal;
import static tekgenesis.common.util.Conversions.toDouble;
import static tekgenesis.common.util.Conversions.toInt;

/**
 * Conversions with exception checks (eg. sign, precision) and type adequacy (scaling, truncation).
 */
class TypeFieldConversions {

    //~ Constructors .................................................................................................................................

    private TypeFieldConversions() {}

    //~ Methods ......................................................................................................................................

    /** Convert and adequate from given single string value to typed object for given TypeField. */
    @Nullable static Object fromString(@NotNull TypeField field, @Nullable String value) {
        return fromString(field, field.getFinalType(), value);
    }

    private static Boolean booleanFromString(@Nullable String value) {
        return Boolean.valueOf(value);
    }

    private static DateOnly dateFromString(@Nullable String value) {
        return DateOnly.fromString(value);
    }

    private static DateTime dateTimeFromString(@Nullable String value) {
        return DateTime.fromString(value);
    }

    private static BigDecimal decimalFromString(TypeField field, DecimalType type, @Nullable String value) {
        return scaleAndCheck(field.getName(), toDecimal(value), field.isSigned(), type.getPrecision(), type.getDecimals());
    }

    @Nullable private static Object enumFromString(Type type, @Nullable String value) {
        return value == null ? null : type.valueOf(value);
    }

    @Nullable
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    private static Object fromString(@NotNull TypeField field, @NotNull Type type, @Nullable String value) {
        switch (type.getKind()) {
        case BOOLEAN:
            return booleanFromString(value);
        case STRING:
            return stringFromString((StringType) type, value);
        case DATE_TIME:
            return dateTimeFromString(value);
        case DATE:
            return dateFromString(value);
        case REAL:
            return realFromString(field, value);
        case INT:
            return intFromString(field, type, value);
        case DECIMAL:
            return decimalFromString(field, (DecimalType) type, value);
        case ENUM:
            return enumFromString(type, value);
        case ARRAY:
            return fromString(field, ((ArrayType) type).getElementType(), value);
        default:
            return value;
        }
    }

    private static Integer intFromString(TypeField field, Type type, @Nullable String value) {
        final int length = type.getLength().get();
        return checkSignedLength(field.getName(), toInt(value), field.isSigned(), length);
    }

    private static Double realFromString(TypeField field, @Nullable String value) {
        return checkSigned(field.getName(), toDouble(value), field.isSigned());
    }

    @Nullable private static String stringFromString(StringType type, @Nullable String value) {
        return truncate(value, type.getLength().get());
    }
}  // end class TypeFieldConversions
