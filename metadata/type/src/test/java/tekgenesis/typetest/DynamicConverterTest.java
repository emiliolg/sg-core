
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.typetest;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import test.TypeAdapter;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.logging.Logger.Level;
import tekgenesis.type.DynamicTypeConverter;
import tekgenesis.type.DynamicTypeConverterImpl;
import tekgenesis.type.Type;

import static java.math.BigDecimal.ROUND_UNNECESSARY;
import static java.util.Collections.singletonList;

import static org.assertj.core.api.Assertions.assertThat;

import static test.TypeAdapter.*;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.listOf;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class DynamicConverterTest {

    //~ Methods ......................................................................................................................................

    @Test public void dynamicInvalidConversionTests() {
        final Logger logger = TypeAdapter.getDynamicTypeConverterLogger();
        final Level  level  = logger.getLevel();
        try {
            logger.setLevel(Level.OFF);

            checkConversions(false,
                c(intType(), 0, "false"),
                c(booleanType(), false, "c-+_11!"),
                c(realType(), 0.00, "true"),
                c(decimalType(4, 2), BigDecimal.ZERO.setScale(2, ROUND_UNNECESSARY), "#$%^&*"),
                c(intType(), listOf(0, 3, 0, 5, 0, 0), listOf("_", "3", ";", "5", "b", "c")),
                c(booleanType(), listOf(false, false), listOf("_", "c")));
        }
        finally {
            logger.setLevel(level);
        }
    }

    @Test public void dynamicMultipleTypeConverterTests() {
        checkConversions(true,
            c(intType(), listOf(1, 2, 3, 4, 5, 6), listOf("1", "2", "3", "4", "5", "6")),
            c(stringType(), listOf("1", "2"), listOf("1", "2")),
            c(booleanType(), listOf(true, false), listOf("true", "false")),
            c(realType(), listOf(10.205, 12.3), listOf("10.205", "12.3")),
            c(decimalType(4, 2), listOf(new BigDecimal(4.5), new BigDecimal(2.5)), listOf("4.5", "2.5")),
            c(dateTimeType(),
                listOf(DateTime.dateTime(2013, 5, 28, 3, 55).toMilliseconds(), DateTime.dateTime(2013, 6, 20, 4, 0).toMilliseconds()),
                listOf("1369724100000", "1371711600000")));
    }

    @Test public void dynamicSingleTypeConverterTests() {
        checkConversions(true,
            c(intType(), 1234567, "1234567"),
            c(stringType(), "Some String", "Some String"),
            c(stringType(5), "12345", "12345"),
            c(booleanType(), true, "true"),
            c(realType(), 10.204, "10.204"),
            c(decimalType(4, 2), new BigDecimal(4.5), "4.5"),
            c(dateType(), DateOnly.date(2013, 5, 28).toMilliseconds(), "1369699200000"),
            c(dateTimeType(), DateTime.dateTime(2013, 5, 28, 3, 55).toMilliseconds(), "1369724100000"));
    }

    private void checkConversions(boolean duplex, final C... conversions) {
        for (final C c : conversions) {
            final DynamicTypeConverterImpl converter = createDynamicTypeConverter(c.type);
            if (duplex) c.assertToString(converter);
            c.assertFromString(converter);
        }
    }

    //~ Methods ......................................................................................................................................

    private static C c(Type type, List<?> values, List<String> conversions) {
        return new C(type, values, conversions, false);
    }

    private static C c(Type type, Object value, String conversion) {
        return new C(type, singletonList(value), singletonList(conversion), true);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Represents a scalar/multiple conversion.
     */
    private static class C {
        private final List<String> conversions;
        private final boolean      scalar;
        private final Type         type;
        private final List<Object> values;

        C(Type type, List<?> values, List<String> conversions, boolean scalar) {
            this.type        = type;
            this.scalar      = scalar;
            this.values      = cast(values);
            this.conversions = conversions;
        }

        private void assertFromString(DynamicTypeConverter converter) {
            if (scalar) assertThat(converter.fromString(conversions.get(0))).isEqualTo(values.get(0));
            else assertThat(converter.fromString(conversions)).isEqualTo(values);
        }

        private void assertToString(DynamicTypeConverter converter) {
            if (scalar) assertThat(converter.toString(values.get(0))).isEqualTo(conversions.get(0));
            else assertThat(converter.toString(values)).isEqualTo(conversions);
        }
    }
}  // end class DynamicConverterTest
