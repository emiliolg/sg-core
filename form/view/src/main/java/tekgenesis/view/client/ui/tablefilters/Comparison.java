
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.tablefilters;

import java.util.Set;

import tekgenesis.code.Evaluator;
import tekgenesis.common.core.DateOnly;
import tekgenesis.expr.BinaryExpression;
import tekgenesis.expr.BinaryExpression.Operator;
import tekgenesis.expr.ExpressionFactory;
import tekgenesis.expr.SimpleReferenceSolver;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;

import static tekgenesis.common.collections.Colls.set;
import static tekgenesis.expr.ExpressionFactory.real;
import static tekgenesis.type.Kind.DATE;
import static tekgenesis.type.Kind.DATE_TIME;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * Comparisons used by Filterable table headers.
 */
@SuppressWarnings("NonJREEmulationClassesInClientCode")
public enum Comparison {

    //~ Enum constants ...............................................................................................................................

    GREATER_THAN(MSGS.greaterThan()) {
        @Override public Operator getOperator() { return Operator.GT; }

        @Override public boolean test(Type type, Object against, String testValue) { return evaluateNumber(getOperator(), type, against, testValue); }},
    LESS_THAN(MSGS.lessThan()) {
        @Override public Operator getOperator() { return Operator.LT; }

        @Override public boolean test(Type type, Object against, String testValue) { return evaluateNumber(getOperator(), type, against, testValue); }},
    GREATER_OR_EQUALS(MSGS.greaterOrEquals()) {
        @Override public Operator getOperator() { return Operator.GE; }

        @Override public boolean test(Type type, Object against, String testValue) { return evaluateNumber(getOperator(), type, against, testValue); }},
    LESS_OR_EQUALS(MSGS.lessOrEquals()) {
        @Override public Operator getOperator() { return Operator.LE; }

        @Override public boolean test(Type type, Object against, String testValue) { return evaluateNumber(getOperator(), type, against, testValue); }},
    EQUALS(MSGS.equals()) {
        @Override public Operator getOperator() { return Operator.EQ; }

        @Override public boolean test(Type type, Object against, String testValue) {
            if (type.isNumber()) return evaluateNumber(getOperator(), type, against, testValue);
            if (type.getKind() == DATE || type.getKind() == DATE_TIME) return ((Long) against) == DateOnly.fromString(testValue).toMilliseconds();
            if (type.isString()) return evaluateString(getOperator(), type, against, testValue);
            if (type.isBoolean()) return ((Boolean) against) == parseBoolean(testValue);
            if (type.isEnum() || type.isDatabaseObject()) return against.equals(testValue);

            throw new UnsupportedOperationException("EQUALS on " + type + " is unsupported.");
        }},
    NOT_EQUALS(MSGS.notEquals()) {
        @Override public Operator getOperator() { return Operator.NE; }

        @Override public boolean test(Type type, Object against, String testValue) {
            if (type.isNumber()) return evaluateNumber(getOperator(), type, against, testValue);
            if (type.getKind() == DATE || type.getKind() == DATE_TIME) return ((Long) against) != DateOnly.fromString(testValue).toMilliseconds();
            if (type.isString()) return evaluateString(getOperator(), type, against, testValue);
            if (type.isBoolean()) return ((Boolean) against) != parseBoolean(testValue);
            if (type.isEnum() || type.isDatabaseObject()) return !against.equals(testValue);

            throw new UnsupportedOperationException("NOT EQUALS on " + type + " is unsupported.");
        }},
    BEGINS_WITH(MSGS.beginsWith()) {
        @Override public Operator getOperator() { return Operator.EQ; }

        @Override public boolean test(Type type, Object against, String testValue) {
            return ((String) against).toLowerCase().startsWith(testValue.toLowerCase());
        }},
    NOT_BEGINS_WITH(MSGS.notBeginsWith()) {
        @Override public Operator getOperator() { return Operator.NE; }

        @Override public boolean test(Type type, Object against, String testValue) {
            return !((String) against).toLowerCase().startsWith(testValue.toLowerCase());
        }},
    ENDS_WITH(MSGS.endsWith()) {
        @Override public Operator getOperator() { return Operator.EQ; }

        @Override public boolean test(Type type, Object against, String testValue) {
            return ((String) against).toLowerCase().endsWith(testValue.toLowerCase());
        }},
    NOT_ENDS_WITH(MSGS.notEndsWith()) {
        @Override public Operator getOperator() { return Operator.NE; }

        @Override public boolean test(Type type, Object against, String testValue) {
            return !((String) against).toLowerCase().endsWith(testValue.toLowerCase());
        }},
    CONTAINS(MSGS.contains()) {
        @Override public Operator getOperator() { return Operator.EQ; }

        @Override public boolean test(Type type, Object against, String testValue) {
            return ((String) against).toLowerCase().contains(testValue.toLowerCase());
        }},
    NOT_CONTAINS(MSGS.notContains()) {
        @Override public Operator getOperator() { return Operator.NE; }

        @Override public boolean test(Type type, Object against, String testValue) {
            return !((String) against).toLowerCase().contains(testValue.toLowerCase());
        }},
    AFTER(MSGS.after()) {
        @Override public Operator getOperator() { return Operator.GT; }

        @Override public boolean test(Type type, Object against, String testValue) {
            return ((Long) against) > DateOnly.fromString(testValue).toMilliseconds();
        }},
    AFTER_OR_EQUALS(MSGS.afterOrEquals()) {
        @Override public Operator getOperator() { return Operator.GE; }

        @Override public boolean test(Type type, Object against, String testValue) {
            return ((Long) against) >= DateOnly.fromString(testValue).toMilliseconds();
        }},
    BEFORE(MSGS.before()) {
        @Override public Operator getOperator() { return Operator.LT; }

        @Override public boolean test(Type type, Object against, String testValue) {
            return ((Long) against) < DateOnly.fromString(testValue).toMilliseconds();
        }},
    BEFORE_OR_EQUALS(MSGS.beforeOrEquals()) {
        @Override public Operator getOperator() { return Operator.LE; }

        @Override public boolean test(Type type, Object against, String testValue) {
            return ((Long) against) <= DateOnly.fromString(testValue).toMilliseconds();
        }};

    //~ Instance Fields ..............................................................................................................................

    private final String label;

    //~ Constructors .................................................................................................................................

    /** Creates a Comparison. */
    Comparison(String label) {
        this.label = label;
    }

    //~ Methods ......................................................................................................................................

    /** Returns Comparison label. */
    public String label() {
        return label;
    }

    public abstract boolean test(Type type, Object against, String testValue);

    /** Returns its value. */
    public String value() {
        return name();
    }

    public abstract Operator getOperator();

    //~ Methods ......................................................................................................................................

    static Set<Comparison> comparisonsFor(Kind k) {
        switch (k) {
        case INT:
        case REAL:
        case DECIMAL:
            return NUMBER_COMPARISONS;
        case STRING:
            return STRING_COMPARISONS;
        case DATE:
        case DATE_TIME:
            return DATE_COMPARISONS;
        case ENUM:
            return ENUM_COMPARISONS;
        default:
            throw new IllegalArgumentException("No comparisons for: " + k);
        }
    }

    private static boolean evaluateNumber(Operator op, Type type, Object against, String testValue) {
        final BinaryExpression      e   = new BinaryExpression(op, ExpressionFactory.value(type, against), real(parseDouble(testValue)));
        final SimpleReferenceSolver env = new SimpleReferenceSolver();
        return (Boolean) e.createExpression().compileBindAndEvaluate(env, env, new Evaluator());
    }

    private static boolean evaluateString(Operator op, Type type, Object against, String testValue) {
        final BinaryExpression      e   = new BinaryExpression(op, ExpressionFactory.value(type, against), ExpressionFactory.value(testValue));
        final SimpleReferenceSolver env = new SimpleReferenceSolver();
        return (Boolean) e.createExpression().compileBindAndEvaluate(env, env, new Evaluator());
    }

    //~ Static Fields ................................................................................................................................

    private static final Set<Comparison> NUMBER_COMPARISONS = set(EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_OR_EQUALS, LESS_THAN, LESS_OR_EQUALS);
    private static final Set<Comparison> DATE_COMPARISONS   = set(EQUALS, NOT_EQUALS, BEFORE, BEFORE_OR_EQUALS, AFTER, AFTER_OR_EQUALS);
    private static final Set<Comparison> STRING_COMPARISONS = set(EQUALS,
            NOT_EQUALS,
            BEGINS_WITH,
            NOT_BEGINS_WITH,
            ENDS_WITH,
            NOT_ENDS_WITH,
            CONTAINS,
            NOT_CONTAINS);
    private static final Set<Comparison> ENUM_COMPARISONS   = set(EQUALS, NOT_EQUALS);
}
