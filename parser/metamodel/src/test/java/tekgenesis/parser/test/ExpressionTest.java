
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.parser.test;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.junit.Test;

import tekgenesis.code.Evaluator;
import tekgenesis.code.SymbolNotFoundException;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.*;
import tekgenesis.expr.Expression;
import tekgenesis.expr.ReferenceEnvironment;
import tekgenesis.expr.SimpleReferenceSolver;
import tekgenesis.metadata.entity.EnumBuilder;
import tekgenesis.mmcompiler.parser.ExpressionCompiler;
import tekgenesis.parser.ASTNode.Utils;
import tekgenesis.type.EnumType;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static java.util.Arrays.asList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Constants.NOT_FOUND;
import static tekgenesis.common.core.Constants.SYMBOL;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.core.Tuple.tuple2;
import static tekgenesis.type.Types.anyType;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class ExpressionTest {

    //~ Instance Fields ..............................................................................................................................

    private final Evaluator evaluator = new Evaluator();

    //~ Methods ......................................................................................................................................

    @Test public void evaluateExpressions() {
        final ImmutableList<Tuple<String, Object>> expressions = listOf(tuple("a > b ? BLUE  : RED", Color.BLUE),
                tuple("color == RED", true),
                tuple("!(date2 == date1)", true),
                tuple("0 == 0.0)", true),
                tuple("0 != 0.0)", false),
                tuple("a - b - c", 50),
                tuple("date2 - date1", 4018),
                tuple("date1 + 4018", date2),
                tuple("date2 - 4018", date1),
                tuple("dateTime1 + 1", dateTime1.addMilliseconds(Times.MILLIS_SECOND)),
                tuple("dateTime1 - 1", dateTime1.addMilliseconds(-Times.MILLIS_SECOND)),
                tuple("dateTime2 - dateTime1", (int) ((dateTime2.toMilliseconds() - dateTime1.toMilliseconds()) / Times.MILLIS_SECOND)),
                tuple("(now() - dateTime1) > 0", true),
                tuple("date2 > date1", true),
                tuple("date2 > date2", false),
                tuple("today > today()", false),
                tuple("date2 == date1", false),
                tuple("date2 != date1", true),
                tuple("a > b || date2 < date1", true),
                tuple("color == BLUE || color == RED", true),
                tuple("a < b && date2 > date1", false),
                tuple("a < b ? date2 : date1", date1),
                tuple("color + \"-\"", "RED-"),
                tuple("today() > date2", true),
                tuple("today() > time", true),
                // ("scale(d, 2)", BigDecimal.valueOf(100.12)),
                tuple("length(\"hello\")", 5),
                tuple("\"\\\"hello\\\"\")", "\"hello\""));

        for (final Tuple<String, Object> entry : expressions) {
            final String expression = entry.first();
            Object       expected   = entry.second();
            final Type   resultType;
            if (expected instanceof Enumeration<?, ?>) {
                final Enumeration<?, ?> e = ((Enumeration<?, ?>) expected);
                expected   = e.name();
                resultType = EnumType.fromEnum(e);
            }
            else {
                resultType = Types.typeOf(expected);
                if (expected instanceof DateTimeBase) expected = ((DateTimeBase<?>) expected).toMilliseconds();
            }
            final Object value = new ExpressionCompiler(expression).buildExpression(resultType).compileBindAndEvaluate(env, env, new Evaluator());
            assertThat(value).isEqualTo(expected);
        }
    }  // end method evaluateExpressions

    // env.put("b", true);
    // env.put("d", new BigDecimal(10.20));

    @Test public void parseSimpleExpressions() {
        final ImmutableList<? extends Tuple5<String, String, String, ? extends Serializable, ? extends Serializable>> expressions = listOf(
                tuple("10 + 1 * 2 * 3", "('+' 10 ('*' ('*' 1 2) 3))", "10 + ((1 * 2) * 3)", Types.intType(), 16),
                tuple("true || true && true && true",
                    "('||' true ('&&' ('&&' true true) true))",
                    "true || ((true && true) && true)",
                    Types.booleanType(),
                    true),

                tuple("a - b - c",
                    "('-' ('-' ([field_ref] ([reference] a)) ([field_ref] ([reference] b))) ([field_ref] ([reference] c)))",
                    "(a - b) - c",
                    Types.intType(),
                    50),

                tuple("a * b + -10",
                    "('+' ('*' ([field_ref] ([reference] a)) ([field_ref] ([reference] b))) ([unary_minus] 10))",
                    "(a * b) + -(10)",
                    Types.intType(),
                    9990),

                tuple("a * (b + 10)",
                    "('*' ([field_ref] ([reference] a)) ('+' ([field_ref] ([reference] b)) 10))",
                    "a * (b + 10)",
                    Types.intType(),
                    12000),

                tuple("b*1 + 5 <= 30", "('<=' ('+' ('*' ([field_ref] ([reference] b)) 1) 5) 30)", "((b * 1) + 5) <= 30", Types.booleanType(), false),

                tuple("a*b + c*d",
                    "('+' ('*' ([field_ref] ([reference] a)) ([field_ref] ([reference] b))) ('*' ([field_ref] ([reference] c)) ([field_ref] ([reference] d))))",
                    "(a * b) + (c * d)",
                    Types.decimalType(),
                    new BigDecimal("20012")),

                tuple("a < 10 && a*b < 10 || b < c",
                    "('||' ('&&' ('<' ([field_ref] ([reference] a)) 10) ('<' ('*' ([field_ref] ([reference] a)) ([field_ref] ([reference] b))) 10)) ('<' ([field_ref] ([reference] b)) ([field_ref] ([reference] c))))",
                    "((a < 10) && ((a * b) < 10)) || (b < c)",
                    Types.booleanType(),
                    true),

                tuple("\"aaaa\" + \"bbbb\" < \"cc\\tcc\"",
                    "('<' ('+' \"aaaa\" \"bbbb\") \"cc\\tcc\")",
                    "(\"aaaa\" + \"bbbb\") < \"cc\tcc\"",
                    Types.booleanType(),
                    true),

                tuple("substring(\"abcd\", 0, 3) == \"abc\"",
                    "('==' ([invoke] substring \"abcd\" 0 3) \"abc\")",
                    "substring(\"abcd\", 0, 3) == \"abc\"",
                    Types.booleanType(),
                    true),

                tuple("sum(vec) / count(vec) == avg(vec)",
                    "('==' ('/' ([invoke] sum ([field_ref] ([reference] vec))) ([invoke] count ([field_ref] ([reference] vec)))) ([invoke] avg ([field_ref] ([reference] vec))))",
                    "(sum(vec) / count(vec)) == avg(vec)",
                    Types.booleanType(),
                    true),

                tuple("sum(vec)", "([invoke] sum ([field_ref] ([reference] vec)))", "sum(vec)", Types.intType(), 60),

                tuple("sum(vec)", "([invoke] sum ([field_ref] ([reference] vec)))", "sum(vec)", Types.intType(), 60),

                tuple("RED", "([field_ref] ([reference] RED))", "RED", colorType, "RED"));

        for (final Tuple5<String, String, String, ? extends Serializable, ? extends Serializable> entry : expressions) {
            final ExpressionCompiler compiler = new ExpressionCompiler(entry.first());
            assertThat(Utils.asLispList(compiler.getAST())).isEqualTo(entry.second());

            final Expression expression = compiler.buildExpression((Type) entry.fourth());

            final Object r = expression.compileBindAndEvaluate(env, env, evaluator);

            assertThat(expression.toString()).isEqualTo(entry.third());

            assertThat(r).isEqualTo(entry.fifth());
        }
    }  // end method parseSimpleExpressions

    @Test public void testEvaluateMethodWithReferenceEnvironment() {
        final ReferenceEnvironment environment = new ReferenceEnvironment();
        environment.putEnvironment("credit").put("total", 10.00);
        environment.putEnvironment("invoice").put("total", 9.99);
        final Object result = ExpressionCompiler.evaluate("credit.total > invoice.total", environment);
        assertThat(result).isInstanceOf(Boolean.class);
        assertThat((Boolean) result).isTrue();
    }

    @Test public void testStringInterpolation() {
        final ReferenceEnvironment environment = new ReferenceEnvironment();
        environment.put("last", "Baggins");
        environment.put("first", "Bilbo");
        environment.put("first2", "Hobbit");
        environment.put("first_3", "Underscore");
        environment.put("five", 5);
        environment.put("eight", 8);
        final Seq<Tuple<String, Object>> fixture = listOf(  //
                tuple("$last, $first", "Baggins, Bilbo"),
                tuple("$last $first", "Baggins Bilbo"),
                tuple("$last$first", "BagginsBilbo"),
                tuple("$$first $last", "$first Baggins"),
                tuple("$last$", "Baggins$"),
                tuple("$last", "Baggins"),
                tuple("$first", "Bilbo"),
                tuple(" $last $first ", " Baggins Bilbo "),
                tuple("$ first", "$ first"),
                tuple("$five $$last", "5 $last"),
                tuple("$10", "$10"),
                tuple("$five", "5"),
                tuple("$five$eight", "58"),
                tuple("$last $$(lastname), $first (firstname)", "Baggins $(lastname), Bilbo (firstname)"),
                tuple("$first $first2 $first_3 $_asd $123asd", "Bilbo Hobbit Underscore $_asd $123asd"));

        fixture.forEach((Tuple<String, Object> t) -> assertInterpolation(environment, t));
        try {
            assertInterpolation(environment, tuple2("$fiv", "exception should be thrown"));
            fail("symbol fiv doesn't exist and an exception should've been thrown");
        }
        catch (final SymbolNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo(SYMBOL + "fiv" + NOT_FOUND);
        }
    }

    private void assertInterpolation(ReferenceEnvironment environment, Tuple<String, Object> t) {
        final Expression expression = compileExpression(quoted(t.first()), environment);
        final Object     result     = expression.compileBindAndEvaluate(environment, environment, evaluator);
        assertThat(result).isEqualTo(t.second());
    }

    private Expression compileExpression(@NotNull String expression, @NotNull ReferenceEnvironment environment) {
        final ExpressionCompiler compiler = new ExpressionCompiler(expression);
        return compiler.buildExpression(anyType()).compile(environment);
    }

    //~ Static Fields ................................................................................................................................

    private static final SimpleReferenceSolver env = new SimpleReferenceSolver();

    private static final EnumType colorType = EnumBuilder.enumType(Color.class);
    private static final DateOnly date2     = DateOnly.fromString("2010/01/01");
    private static final DateOnly date1     = DateOnly.fromString("1999/01/01");
    private static final DateOnly today     = DateOnly.fromString("2013/02/06");

    private static final DateTime dateTime1 = DateTime.dateTime(1984, 4, 23, 20, 45);
    private static final DateTime dateTime2 = DateTime.dateTime(2014, 4, 23, 20, 45);

    static {
        env.put("a", 200);
        env.put("b", 50);
        env.put("c", 100);
        env.put("d", BigDecimal.valueOf(100.125));

        env.put("date1", date1);
        env.put("date2", date2);
        env.put("dateTime1", dateTime1);
        env.put("dateTime2", dateTime2);
        env.put("today", today);
        env.put("time", DateTime.fromString("1999/01/01T16:16"));

        env.put("vec", asList(10, 20, 30), Types.arrayType(Types.intType()));

        env.put("color", Color.RED.toString(), colorType);
    }
}  // end class ExpressionTest
