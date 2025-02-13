
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package test;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.logging.Logger;
import tekgenesis.type.*;

@SuppressWarnings("ALL")
public class TypeAdapter {

    //~ Methods ......................................................................................................................................

    @NotNull public static BooleanType booleanType() {
        return Types.booleanType();
    }

    public static final DynamicTypeConverterImpl createDynamicTypeConverter(Type t) {
        return new DynamicTypeConverterImpl(t);
    }
    @NotNull public static DateTimeType dateTimeType() {
        return Types.dateTimeType();
    }
    @NotNull public static DateOnlyType dateType() {
        return Types.dateType();
    }
    @NotNull public static DecimalType decimalType(int n) {
        return Types.decimalType(n);
    }
    @NotNull public static DecimalType decimalType(int n, int m) {
        return Types.decimalType(n, m);
    }
    @NotNull public static IntType intType() {
        return Types.intType();
    }
    @NotNull public static IntType intType(int n) {
        return Types.intType(n);
    }
    @NotNull public static RealType realType() {
        return Types.realType();
    }
    @NotNull public static StringType stringType() {
        return Types.stringType();
    }
    @NotNull public static StringType stringType(int n) {
        return Types.stringType(n);
    }

    public static final Logger getDynamicTypeConverterLogger() {
        return Logger.getLogger(DynamicTypeConverterImpl.class);
    }
}  // end class TypeAdapter
