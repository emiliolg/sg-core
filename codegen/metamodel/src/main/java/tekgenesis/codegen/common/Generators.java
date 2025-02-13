
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.common;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;

import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaElement;
import tekgenesis.codegen.impl.java.JavaItemGenerator;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.*;
import tekgenesis.expr.Expression;
import tekgenesis.field.ModelField;
import tekgenesis.field.Signed;
import tekgenesis.field.TypeField;
import tekgenesis.type.*;

import static tekgenesis.codegen.CodeGeneratorConstants.*;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.util.JavaReservedWords.NULL;

/**
 * Utility methods to help with the generation.
 */
public class Generators {

    //~ Constructors .................................................................................................................................

    private Generators() {}

    //~ Methods ......................................................................................................................................

    /** Get the default value for a given Field. */
    public static String defaultFor(final ClassGenerator cg, final TypeField field, boolean required) {
        final Expression expression = field.getDefaultValue();
        if (!expression.isNull() && expression.isConstant()) return defaultFor(cg, field, expression);
        if (required) return defaultFor(cg, field);
        return NULL;
    }

    /** Return an EnumSet.noneOf(class). */
    public static String enumsetNoneOf(JavaItemGenerator<?> cg, String enumType) {
        return cg.invokeStatic(EnumSet.class, "noneOf", cg.classOf(enumType));
    }

    /** Create arguments from Attributes. */
    public static Seq<JavaElement.Argument> makeArguments(List<JavaElement.Field> fields) {
        return seq(fields).map(f -> new JavaElement.Argument(f.getName(), f.getType()).required(f.isNotNull()));
    }

    /** Create arguments from Attributes. */
    public static Seq<JavaElement.Argument> makeArguments(Seq<? extends TypeField> attributes, final boolean required) {
        // noinspection UnnecessaryLocalVariable
        final Seq<JavaElement.Argument> r = attributes.map(f ->
                    new JavaElement.Argument(f.getName(), f.getImplementationClassName()).required(required || f.isRequired()));
        return r;
    }

    /** Invoke verifications over the value to be set. */
    public static <M extends ModelField & Signed> String verifyField(ClassGenerator g, String name, M field) {
        final Type t    = field.getFinalType();
        final Type type = t.isArray() ? ((ArrayType) t).getElementType() : t;
        return verifyField(g, name, field, type);
    }

    /** The names of the fields. */
    static Seq<String> fieldNames(Seq<? extends ModelField> fields) {
        return fields.map(ModelField::getName);
    }

    @SuppressWarnings("MethodWithMultipleReturnPoints")
    private static String defaultFor(final JavaItemGenerator<?> cg, final TypeField typeField) {
        final Type type = typeField.getFinalType();
        switch (type.getKind()) {
        case BOOLEAN:
            return String.valueOf(false);
        case STRING:
            return quoted("");
        case REAL:
            return "0.0";
        case INT:
            return "0";
        case DECIMAL:
            return cg.refStatic(BigDecimal.class, ZERO);
        case DATE_TIME:
            return cg.refStatic(DateTime.class, EPOCH);
        case DATE:
            return cg.refStatic(DateOnly.class, EPOCH);
        case ENUM:
            final EnumType et       = (EnumType) type;
            final String   enumType = et.getImplementationClassName();
            if (typeField.isMultiple()) return enumsetNoneOf(cg, enumType);
            return cg.refStatic(et.getImplementationClassName(), et.getDefaultValue());
        case ANY:
        case NULL:
        case ARRAY:
        case RESOURCE:
        case REFERENCE:
        case TYPE:
        case HTML:
        case VOID:
            break;
        }
        return NULL;
    }

    private static String defaultFor(final JavaItemGenerator<?> cg, final TypeField field, final Expression constant) {
        final String defaultValue = constant.toString();
        // todo entities needs to compile its expressions, since the result of the expression may be a constant
        // (ex: today() + 1) see forms implementation (CodeGeneratorVisitor.compile)
        switch (field.getFinalType().getKind()) {
        case ENUM:
            final String enumValue = cg.extractImport(field.getImplementationClassName()) + "." + defaultValue;
            return field.isMultiple() ? cg.invokeStatic(EnumSet.class, "of", enumValue) : enumValue;
        case DECIMAL:
            return "0".equals(defaultValue) || "0.0".equals(defaultValue) ? cg.refStatic(BigDecimal.class, ZERO)
                                                                          : cg.new_(BigDecimal.class, quoted(defaultValue));
        case DATE_TIME:
        case DATE:
            // todo should evaluate expressions (see the todo above) and create the Date from the resulting milliseconds
            // cg.refStatic(DateTime.class, "fromMilliseconds", millis);
            // cg.refStatic(DateOnly.class, "fromMilliseconds", millis);
            return NULL;
        default:
            return defaultValue;
        }
    }

    private static String str(int n) {
        return String.valueOf(n);
    }

    private static <M extends ModelField & Signed> String verifyField(ClassGenerator g, String name, M field, Type type) {
        switch (type.getKind()) {
        case STRING:
            final StringType s = (StringType) type;
            return g.invokeStatic(Strings.class, TRUNCATE, name, str(s.getLength().get()));
        case DECIMAL:
            final DecimalType decimal       = (DecimalType) type;
            final Boolean     signedDecimal = field.isSigned();
            return g.invokeStatic(Decimals.class,
                SCALE_AND_CHECK,
                quoted(name),
                name,
                signedDecimal.toString(),
                str(decimal.getPrecision()),
                str(decimal.getDecimals()));
        case INT:
            final IntType integer   = (IntType) type;
            final Boolean signedInt = field.isSigned();
            return g.invokeStatic(Integers.class,
                CHECK_SIGNED_LENGTH,
                quoted(name),
                name,
                signedInt.toString(),
                integer.getLength().get().toString());
        case REAL:
            final Boolean signedReal = field.isSigned();
            return g.invokeStatic(Reals.class, CHECK_SIGNED, quoted(name), name, signedReal.toString());

        default:
            return name;
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final String EPOCH = "EPOCH";
    private static final String ZERO  = "ZERO";
}  // end class Generators
