
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.code.Fun;
import tekgenesis.code.FunctionRegistry;
import tekgenesis.common.annotation.GwtIncompatible;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.type.EnumType;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.code.PredefinedFunctions.SCALE_FN;
import static tekgenesis.common.core.Constants.DEFINED;
import static tekgenesis.expr.BinaryExpression.Operator.*;
import static tekgenesis.expr.UnaryExpression.Operator.*;
import static tekgenesis.type.Types.stringType;

/**
 * Factory for {@link ExpressionAST}s. This should be the only way to create an expressions.
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "WeakerAccess" })
public class ExpressionFactory {

    //~ Constructors .................................................................................................................................

    private ExpressionFactory() {}

    //~ Methods ......................................................................................................................................

    /** ADD + operation. sum Numbers, cat Strings Can handle Numbers, Strings and Dates. */
    public static BinaryExpression add(ExpressionAST left, ExpressionAST right) {
        return new BinaryExpression(ADD, left, right);
    }

    /** AND && operation. Can handle two Boolean. */
    public static BinaryExpression and(ExpressionAST left, ExpressionAST right) {
        return new BinaryExpression(AND, left, right);
    }

    /** Average operation. Can handle Arrays of Numbers. */
    public static UnaryExpression avg(ExpressionAST expr) {
        return new UnaryExpression(AVG, expr);
    }

    /** Boolean constant. */
    public static Value bool(Boolean value) {
        return value ? TRUE_VALUE_EXPR : FALSE_VALUE_EXPR;
    }

    /** Boolean Array constant. */
    public static Value boolArray(Boolean... bool) {
        return new Value(list(bool), Types.arrayType(Types.booleanType()));
    }

    /**
     * Conversion/Cast operation. Converts the expression to a desired type, wrapping the passed
     * expression If the expression type is equivalent to the desired type then it just returns the
     * same passed expression.
     */
    public static ExpressionAST conversion(ExpressionAST e, Type t) {
        return t.equivalent(e.getType()) ? e : new ConversionOp(t, e);
    }

    /** Count operation. Counts the number of not null elements in a column. */
    public static UnaryExpression count(ExpressionAST expr) {
        return new UnaryExpression(COUNT, expr);
    }

    /** Date Array constant. */
    public static Value dateArray(Date... date) {
        return new Value(list(date), Types.arrayType(Types.dateType()));
    }

    /** Decimal constant. */
    public static Value decimal(BigDecimal value) {
        return new Value(value, Types.decimalType(value.precision()));
    }

    /** Decimal Array constant. */
    public static Value decimalArray(BigDecimal... decimals) {
        // todo (Agus) undefined Decimal precision
        return new Value(list(decimals), Types.arrayType(Types.decimalType(Integer.MAX_VALUE)));
    }

    /**
     * DEFINED operation. Checks that the Array or String is not empty Can handle any Array or a
     * String.
     */
    public static FunctionCallNode defined(ExpressionAST expr) {
        return new FunctionCallNode(DEFINED, expr);
    }

    /** DIV / operation. Can handle Numbers. */
    public static BinaryExpression div(ExpressionAST left, ExpressionAST right) {
        return new BinaryExpression(DIV, left, right);
    }

    /**
     * EMPTY operation. Checks that the Array or String is not empty Can handle any Array or a
     * String.
     */
    public static FunctionCallNode empty(ExpressionAST expr) {
        return new FunctionCallNode("empty", expr);
    }

    /** EQ == operation. Can handle ANY object. */
    public static BinaryExpression eq(ExpressionAST left, ExpressionAST right) {
        return new BinaryExpression(EQ, left, right);
    }

    /** Custom fn method. */
    public static FunctionCallNode fn(String name, ExpressionAST... args) {
        return new FunctionCallNode(name, args);
    }

    /** Forbidden expression. */
    public static ExpressionAST forbidden(String permission) {
        return new ForbiddenExpression(permission);
    }

    /** GE >= operation. Can handle ANY object. */
    public static BinaryExpression ge(ExpressionAST left, ExpressionAST right) {
        return new BinaryExpression(GE, left, right);
    }

    /** GT > operation. Can handle ANY object. */
    public static BinaryExpression gt(ExpressionAST left, ExpressionAST right) {
        return new BinaryExpression(GT, left, right);
    }

    /** Integer Array constant. */
    public static Value intArray(Integer... integers) {
        return new Value(list(integers), Types.arrayType(Types.intType()));
    }

    /** Integer constant. */
    public static Value integer(int value) {
        return new Value(value, Types.intType());
    }

    /** le <= operation. Can handle ANY object. */
    public static BinaryExpression le(ExpressionAST left, ExpressionAST right) {
        return new BinaryExpression(LE, left, right);
    }

    /** LT < operation. Can handle ANY object. */
    public static BinaryExpression lt(ExpressionAST left, ExpressionAST right) {
        return new BinaryExpression(LT, left, right);
    }

    /** MAX operation. Return the maximum value of the array Can handle any Array. */
    public static UnaryExpression max(ExpressionAST expr) {
        return new UnaryExpression(MAX, expr);
    }

    /** MIN operation. Return the minimum value of the array Can handle any Array. */
    public static UnaryExpression min(ExpressionAST expr) {
        return new UnaryExpression(MIN, expr);
    }

    /** MOD % operation. Can handle Integers. */
    public static BinaryExpression mod(ExpressionAST left, ExpressionAST right) {
        return new BinaryExpression(MOD, left, right);
    }

    /** MUL * operation. Can handle Numbers. */
    public static BinaryExpression mul(ExpressionAST left, ExpressionAST right) {
        return new BinaryExpression(MUL, left, right);
    }

    /** NE != operation. Can handle ANY object. */
    public static BinaryExpression ne(ExpressionAST left, ExpressionAST right) {
        return new BinaryExpression(NE, left, right);
    }

    /** NOT ! operation. Can handle a Boolean */
    public static UnaryExpression not(ExpressionAST expr) {
        return new UnaryExpression(NOT, expr);
    }

    /** null constant. */
    public static Value nullValue() {
        return NULL_VALUE_EXPR;
    }

    /** OR || operation. Can handle two Boolean. */
    public static BinaryExpression or(ExpressionAST left, ExpressionAST right) {
        return new BinaryExpression(OR, left, right);
    }

    /** Real constant. */
    public static Value real(Double value) {
        return new Value(value, Types.realType());
    }

    /** Real Array constant. */
    public static Value realArray(Double... doubles) {
        return new Value(list(doubles), Types.arrayType(Types.realType()));
    }

    /** RefType operand. */
    public static Ref ref(String reference) {
        return new Ref(reference);
    }

    /** Registers a Custom fn method. */
    public static void registerFn(Fun<?> fun) {
        FunctionRegistry.getInstance().register(fun);
    }

    /** Rows operation. Counts the number of row elements in a column. */
    public static UnaryExpression rows(ExpressionAST expr) {
        return new UnaryExpression(ROWS, expr);
    }

    /** Scale method. */
    public static FunctionCallNode scale(ExpressionAST arg, int scale) {
        return fn(SCALE_FN, arg, integer(scale));
    }

    /** Size operation. Returns the size of an array field. */
    public static UnaryExpression size(ExpressionAST expr) {
        return new UnaryExpression(SIZE, expr);
    }

    /** String interpolation. */
    @GwtIncompatible public static ExpressionAST str(String value) {
        if (!isInterpolation(value)) return value(replaceDuplicates(value));

        final Seq<ExpressionAST> references = ImmutableList.build(b -> {
                final Matcher matcher = INTERPOLATION_PATTERN.matcher(value);
                int           start   = 0;
                while (matcher.find()) {
                    b.add(value(replaceDuplicates(value.substring(start, matcher.start()))));
                    b.add(ref(matcher.group().substring(1)));
                    start = matcher.end();
                }
                b.add(value(replaceDuplicates(value.substring(start))));
            });

        return references.reduce(ExpressionFactory::add);
    }

    /** String Array constant. */
    public static Value strArray(String... str) {
        return new Value(list(str), Types.arrayType(stringType()));
    }

    /** SUB - operation. Can handle Numbers and Dates. */
    public static BinaryExpression sub(ExpressionAST left, ExpressionAST right) {
        return new BinaryExpression(SUB, left, right);
    }

    /** SUM operation. Can handle Arrays of Numbers. */
    public static UnaryExpression sum(ExpressionAST expr) {
        return new UnaryExpression(SUM, expr);
    }

    /** String constant. */
    public static Value value(String value) {
        return new Value(value, Types.stringType(value.length()));
    }

    public static Value value(Type type, Object value) {
        return new Value(value, type);
    }

    /** IF - operation. */
    public static IfExpression wildcard(ExpressionAST condition, ExpressionAST trueExpr, ExpressionAST falseExpr) {
        return new IfExpression(condition, trueExpr, falseExpr);
    }

    @GwtIncompatible public static boolean isInterpolation(@NotNull String str) {
        return INTERPOLATION_PATTERN.matcher(str).find();
    }

    /** Returns a Serializable List. */
    private static <T> ArrayList<T> list(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    private static String replaceDuplicates(String value) {
        return value.replaceAll("\\$\\$", "\\$");
    }

    //~ Static Fields ................................................................................................................................

    @GwtIncompatible private static final Pattern INTERPOLATION_PATTERN = Pattern.compile("(?<!\\$)\\$[A-Za-z][A-Za-z0-9_]*");
    private static final Value                    NULL_VALUE_EXPR       = new ImmutableValue(null, Types.nullType());
    private static final Value                    TRUE_VALUE_EXPR       = new ImmutableValue(true, Types.booleanType());
    private static final Value                    FALSE_VALUE_EXPR      = new ImmutableValue(false, Types.booleanType());

    //~ Inner Classes ................................................................................................................................

    private static class ImmutableValue extends Value {
        public ImmutableValue(Object o, Type type) {
            super(o, type);
        }

        @NotNull @Override ExpressionAST solveEnumRef(EnumType enumType) {
            return this;
        }

        @Override void setTargetType(@Nullable Type t) {
            throw new RuntimeException("Should not be called");
        }
    }
}  // end class ExpressionFactory
