
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

import org.junit.Test;

import tekgenesis.code.Evaluator;

import static java.util.Arrays.asList;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.expr.ExpressionFactory.*;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class ExpressionTest {

    //~ Methods ......................................................................................................................................

    @Test public void arithmetic() {
        // Arithmetic operations;
        checkExpressions(e("1 + 2", add(integer(1), integer(2)), 3),
            e("2 - 1", sub(integer(2), integer(1)), 1),
            e("4 / 2", div(integer(4), integer(2)), 2),
            e("4 * 2", mul(integer(4), integer(2)), 8),
            e("1 + i", add(integer(1), ref("i")), 3),
            e("1 + r", add(integer(1), ref("r")), 3.0));

        // Decimal operations  we use RoundingMode.HALF_EVEN;
        checkExpressions(e("e / 3.00", div(ref("e"), decimal(new BigDecimal("3.00"))), new BigDecimal("3.33")),
            e("e / 6.00", div(ref("e"), decimal(new BigDecimal("6.00"))), new BigDecimal("1.67")));
    }

    @Test public void conversions() {
        // Arithmetic add operation
        checkAndSolveExpressions(e("2.5 + 2", add(real(2.5), integer(2)), 4.5),
            e("2 + 2.5", add(integer(2), real(2.5)), 4.5),
            e("\"xx\" + 2", add(str("xx"), integer(2)), "xx2"),
            e("2.5 + 2", add(decimal(new BigDecimal(2.5)), integer(2)), new BigDecimal(4.5)),

            // Arithmetic operation
            e("2 - 1.5", sub(integer(2), real(1.5)), 0.5),
            e("4.4 / 2", div(real(4.4), integer(2)), 2.2),
            e("4.4 * 2", mul(real(4.4), integer(2)), 8.8),

            // GT operation
            e("4.4 < 2", lt(real(4.4), integer(2)), false),
            e("4.4 <= 2", le(real(4.4), integer(2)), false));
    }

    // @Test public void functions() {
    // FN Operations;
    // checkExpressions(
    // e(count(str("1234")), "count(\"1234\")", 4),
    // e(empty(str("1234")), "empty(\"1234\")", false),
    // e(empty(str("")), "empty(\"\")", true),
    // e(defined(str("")), "defined(\"\")", false),
    // e(empty(intArray(10, 5, 5)), "empty([10, 5, 5])", false),
    // e(empty(intArray()), "empty([])", true),
    // e(defined(intArray()), "defined([])", false));
    //
    // }

    @Test public void groupExpressions() {
        checkExpressions(e("sum([true, false, true])", sum(boolArray(true, false, true)), 2),
            e("sum([10, 5, 5])", sum(intArray(10, 5, 5)), 20),
            e("sum([10.2, 5.0, 5.0])", sum(realArray(10.2, 5.0, 5.0)), 20.2),
            e("avg([5, 5])", avg(intArray(5, 5)), 5),
            e("max([1, 3, 2])", max(intArray(1, 3, 2)), 3),
            e("min([1, 3, 2])", min(intArray(1, 3, 2)), 1),
            e("max([1, 3, 2]) + count([true, false])", add(max(intArray(1, 3, 2)), count(boolArray(true, false))), 5),
            e("max([1, 3, 2]) + count([true, false])", add(max(intArray(1, 3, 2)), count(boolArray(true, false))), 5),
            e("count([1, 2, 3, null, null, 6])", count(intArray(1, 2, 3, null, null, 6)), 4),
            e("rows([1, 2, 3, null, null, 6])", rows(intArray(1, 2, 3, null, null, 6)), 6),
            e("size([1, 2, 3, null, null, 6])", size(intArray(1, 2, 3, null, null, 6)), 6));
    }
    @Test public void logicalOperations() {
        // Logical operations
        checkExpressions(e("(3 != 2) && true", and(ExpressionFactory.ne(integer(3), integer(2)), bool(true)), true),
            e("(4 == 2) || true", or(ExpressionFactory.eq(integer(4), integer(2)), bool(true)), true),

            e("!(true)", ExpressionFactory.not(bool(true)), false));
    }

    @Test public void questionMark() {
        /* OR */
        final ExpressionAST turnOff = str(" turnOffButtons");
        final Value         int0    = integer(0);
        final Value         int1    = integer(1);
        final ExpressionAST empty   = str("");
        final ExpressionAST noLabel = str("no-label resources");

        checkExpressions(
            e(qTxt("((0 > 1) || (1 > 0))"),
                add(noLabel, wildcard(or(gt(int0, int1), gt(int1, int0)), turnOff, empty)),
                "no-label resources turnOffButtons"),

            e(qTxt("((0 > 1) || (0 > 1))"), add(noLabel, wildcard(or(gt(int0, int1), gt(int0, int1)), turnOff, empty)), "no-label resources"),

            e(qTxt("((1 > 0) || (0 > 1))"),
                add(noLabel, wildcard(or(gt(int1, int0), gt(int0, int1)), turnOff, empty)),
                "no-label resources turnOffButtons"),

            e(qTxt("((1 > 0) || (1 > 0))"),
                add(noLabel, wildcard(or(gt(int1, int0), gt(int1, int0)), turnOff, empty)),
                "no-label resources turnOffButtons"),

            e(qTxt("((1 > 0) && (1 > 0))"),
                add(noLabel, wildcard(and(gt(int1, int0), gt(int1, int0)), turnOff, empty)),
                "no-label resources turnOffButtons"),

            e(qTxt("((0 > 1) && (1 > 0))"), add(noLabel, wildcard(and(gt(int0, int1), gt(int1, int0)), turnOff, empty)), "no-label resources"),

            e(qTxt("((1 > 0) && (0 > 1))"), add(noLabel, wildcard(and(gt(int1, int0), gt(int0, int1)), turnOff, empty)), "no-label resources"),

            e(qTxt("((0 > 1) && (0 > 1))"), add(noLabel, wildcard(and(gt(int0, int1), gt(int0, int1)), turnOff, empty)), "no-label resources"),

            e(qTxt("(1 > 0)"), add(noLabel, wildcard(gt(int1, int0), turnOff, empty)), "no-label resources turnOffButtons"),

            e(qTxt("(0 > 1)"), add(noLabel, wildcard(gt(int0, int1), turnOff, empty)), "no-label resources"));
    }  // end method questionMark

    @Test public void relationalOperations() {
        // Relational operations
        checkExpressions(e("4 > 2", gt(integer(4), integer(2)), true),
            e("4 >= 2", ge(integer(4), integer(2)), true),
            e("2 >= 2", ge(integer(2), integer(2)), true),
            e("\"s\" == \"s\"", ExpressionFactory.eq(str("s"), str("s")), true),
            e("\"s\" != \"s\"", ExpressionFactory.ne(str("s"), str("s")), false),
            e("3 == 2", ExpressionFactory.eq(integer(3), integer(2)), false),
            e("3 != 2", ExpressionFactory.ne(integer(3), integer(2)), true));
    }

    @Test public void stringExtraction() {
        final Expression e = add(str("Hello"), add(ref("i"), str("World"))).createExpression();

        e.compile(env);

        assertThat(e.toString()).isEqualTo("\"Hello\" + (i + \"World\")");

        assertThat(e.extractStrings()).containsExactly("Hello", "World");

        assertThat(e.replaceStrings(asList("Hola", "Mundo")).extractStrings()).containsExactly("Hola", "Mundo");
    }

    private void checkAndSolveExpressions(final E... expressions) {
        for (final E e : expressions) {
            e.expr.solveType(RefTypeSolver.EMPTY);
            final Expression expr = e.expr.createExpression();
            assertThat(expr.toString()).isEqualTo(e.text);
            assertThat(expr.compileBindAndEvaluate(env, env, evaluator)).as(e.text).isEqualTo(e.result);
        }
    }

    private void checkExpressions(final E... expressions) {
        for (final E e : expressions) {
            final Expression expr = e.expr.createExpression();
            assertThat(expr.toString()).isEqualTo(e.text);
            assertThat(expr.compileBindAndEvaluate(env, env, evaluator)).as(e.text).isEqualTo(e.result);
        }
    }

    private String qTxt(final String c) {
        return "\"no-label resources\" + " + c + " ? \" turnOffButtons\" : \"\"";
    }

    //~ Methods ......................................................................................................................................

    private static E e(String text, ExpressionAST expr, Object result) {
        return new E(expr, text, result);
    }

    //~ Static Fields ................................................................................................................................

    private static final SimpleReferenceSolver env       = new SimpleReferenceSolver();
    private static final Evaluator             evaluator = new Evaluator();

    static {
        env.put("i", 2);
        env.put("r", 2.0);
        env.put("str", "hello");
        env.put("b", true);
        env.put("d", new BigDecimal(10.20));
        env.put("e", new BigDecimal("10.00"));
    }

    //~ Inner Classes ................................................................................................................................

    static class E {
        ExpressionAST expr;
        Object        result;
        String        text;

        E(final ExpressionAST expr, final String text, final Object result) {
            this.expr   = expr;
            this.text   = text;
            this.result = result;
        }
    }
}  // end class ExpressionTest
