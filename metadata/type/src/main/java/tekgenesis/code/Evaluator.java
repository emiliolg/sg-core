
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.code;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Decimals;
import tekgenesis.common.core.Times;
import tekgenesis.expr.Expression;

import static java.lang.Long.parseLong;
import static java.math.BigDecimal.*;
import static java.math.RoundingMode.HALF_EVEN;
import static java.util.Collections.reverse;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.toList;
import static tekgenesis.common.core.Times.parseDate;
import static tekgenesis.common.core.Times.parseDateTime;
import static tekgenesis.type.assignment.AssignmentType.assignment;

/**
 * A {@link Expression } evaluator.
 */
@SuppressWarnings({ "OverlyComplexClass", "NonJREEmulationClassesInClientCode" })
public class Evaluator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final List<Object> values;

    //~ Constructors .................................................................................................................................

    /** Creates an Evaluator. */
    public Evaluator() {
        values = new ArrayList<>(5);
    }

    //~ Methods ......................................................................................................................................

    @SuppressWarnings({ "OverlyLongMethod", "ConstantConditions", "OverlyComplexMethod" })
    Object evaluate(@NotNull Code[] code, @Nullable Object ctx)
        throws UnsupportedOperationException
    {
        final int n  = code.length;
        int       pc = 0;
        while (pc < n) {
            final Code current = code[pc];
            switch (current.getInstruction()) {
            case LIST:
                push(list(((ListCode) current).getSize()));
                break;
            case ASSIGNMENT:
                pushAssignment();
                break;
            case LT:
                push(compare() < 0);
                break;
            case LE:
                push(compare() <= 0);
                break;
            case GT:
                push(compare() > 0);
                break;
            case GE:
                push(compare() >= 0);
                break;
            case NE:
                push(!equal());
                break;
            case EQ:
                push(equal());
                break;
            case NE_NUM:
                push(compare() != 0);
                break;
            case EQ_NUM:
                push(compare() == 0);
                break;
            case NOT:
                push(!popBoolean());
                break;
            case NOP:
                break;

            case ADD_INT: {
                final Integer d2 = popInt();
                final Integer d1 = popInt();
                push(d1 == null || d2 == null ? null : d1 + d2);
                break;
            }
            case SUB_INT: {
                final Integer d2 = popInt();
                final Integer d1 = popInt();
                push(d1 == null || d2 == null ? null : d1 - d2);
                break;
            }
            case MULT_INT: {
                final Integer d2 = popInt();
                final Integer d1 = popInt();
                push(d1 == null || d2 == null ? null : d1 * d2);
                break;
            }
            case DIV_INT: {
                final Integer d2 = popInt();
                final Integer d1 = popInt();
                push(d1 == null || d2 == null ? null : d1 / d2);
                break;
            }
            case MOD_INT: {
                final Integer d2 = popInt();
                final Integer d1 = popInt();
                push(d1 == null || d2 == null ? null : d1 % d2);
                break;
            }
            case ADD_REAL: {
                final Double d2 = popDouble();
                final Double d1 = popDouble();
                push(d1 == null || d2 == null ? null : d1 + d2);
                break;
            }
            case SUB_REAL: {
                final Double d2 = popDouble();
                final Double d1 = popDouble();
                push(d1 == null || d2 == null ? null : d1 - d2);
                break;
            }

            case ADD_DATE_INT: {
                final Integer i = popInt();
                final Long    d = popLong();
                push(d == null || i == null ? null : Times.addDays(d, i));
                break;
            }

            case SUB_DATE_INT: {
                final Integer i = popInt();
                final Long    d = popLong();
                push(d == null || i == null ? null : Times.addDays(d, -i));
                break;
            }
            case SUB_TIME: {
                final Long d2 = popLong();
                final Long d1 = popLong();
                push(d1 == null || d2 == null ? null : (int) Times.secondsBetween(d1, d2));
                break;
            }
            case SUB_DATE: {
                final Long d2 = popLong();
                final Long d1 = popLong();
                push(d1 == null || d2 == null ? null : Times.daysBetween(d1, d2));
                break;
            }
            case MULT_REAL: {
                final Double d2 = popDouble();
                final Double d1 = popDouble();
                push(d1 == null || d2 == null ? null : d1 * d2);
                break;
            }
            case DIV_REAL: {
                final Double d2 = popDouble();
                final Double d1 = popDouble();
                push(d1 == null || d2 == null ? null : d1 / d2);
                break;
            }
            case ADD_DEC: {
                final BigDecimal d2 = popDecimal();
                final BigDecimal d1 = popDecimal();
                push(d1 == null || d2 == null ? null : d1.add(d2));
                break;
            }
            case SUB_DEC: {
                final BigDecimal d2 = popDecimal();
                final BigDecimal d1 = popDecimal();
                push(d1 == null || d2 == null ? null : d1.subtract(d2));
                break;
            }
            case MULT_DEC: {
                final BigDecimal d2 = popDecimal();
                final BigDecimal d1 = popDecimal();
                push(d1 == null || d2 == null ? null : d1.multiply(d2));
                break;
            }
            case DIV_DEC: {
                final BigDecimal d2 = popDecimal();
                final BigDecimal d1 = popDecimal();
                push(d1 == null || d2 == null ? null : d1.divide(d2, HALF_EVEN));
                break;
            }
            case CAT: {
                final String s2 = popString();
                final String s1 = popString();
                push(s1 == null ? s2 : s2 == null ? s1 : s1 + s2);
                break;
            }
            case SUM_BOOL:
                push(sumBooleans(pop()));
                break;
            case SUM_INT:
                push(sumIntegers(pop()));
                break;
            case SUM_REAL:
                push(sumDoubles(pop()));
                break;
            case SUM_DEC:
                push(sumDecimals(pop()));
                break;
            case AVG_DEC:
                push(avgDecimals(pop()));
                break;
            case AVG_REAL:
                push(avgDoubles(pop()));
                break;
            case AVG_INT:
                push(avgInts(pop()));
                break;
            case MAX:
                push(max(pop()));
                break;
            case MIN:
                push(min(pop()));
                break;
            case COUNT:
                push(count(pop()));
                break;
            case ROWS:
            case SIZE:
                push(Colls.size((Iterable<?>) pop()));
                break;
            case INT_TO_REAL:
                if (peek() != null) push(popLong().doubleValue());
                break;
            case DATE_TIME_TO_DATE:
                if (peek() != null) push(DateTime.fromMilliseconds(popLong()).toDateOnly().toMilliseconds());
                break;
            case DATE_TO_DATE_TIME:
                if (peek() != null) push(DateOnly.fromMilliseconds(popLong()).toDateTime().toMilliseconds());
                break;
            case INT_TO_DEC:
                if (peek() != null) push(valueOf(popLong()));
                break;
            case INT_TO_LONG:
                if (peek() != null) push(popLong());
                break;

            case REAL_TO_INT:
                if (peek() != null) push((popDouble().intValue()));
                break;
            case REAL_TO_LONG:
                if (peek() != null) push((popDouble().longValue()));
                break;
            case REAL_TO_DEC:
                if (peek() != null) push(valueOf(popDouble()));
                break;

            case DEC_TO_INT:
                if (peek() != null) push((popDecimal().intValue()));
                break;
            case DEC_TO_LONG:
                if (peek() != null) push((popDecimal().longValue()));
                break;
            case DEC_TO_REAL:
                if (peek() != null) push(popDecimal().doubleValue());
                break;
            case TO_STR:
                if (peek() != null) push(pop().toString());
                break;
            case TO_DATE_STR:
                if (peek() != null) push(DateOnly.fromMilliseconds(parseLong(pop().toString())).toString());
                break;
            case TO_TIME_STR:
                if (peek() != null) push(DateTime.fromMilliseconds(parseLong(pop().toString())).toString());
                break;
            case STR_TO_INT:
                if (peek() != null) push(Integer.valueOf(popString()));
                break;
            case STR_TO_REAL:
                if (peek() != null) push(Double.valueOf(popString()));
                break;
            case STR_TO_DEC:
                push(decimal(popString()));
                break;
            case STR_TO_TIME:
                push(time(popString()));
                break;
            case STR_TO_DATE:
                push(date(popString()));
                break;
            case STR_TO_BOOL:
                push(Boolean.valueOf(popString()));
                break;
            case MINUS_INT:
                if (peek() != null) push(-popInt());
                break;
            case MINUS_REAL:
                if (peek() != null) push(-popDouble());
                break;
            case MINUS_DEC:
                if (peek() != null) push(popDecimal().negate());
                break;
            case OR:
                if (peekBoolean()) pc = ((Jump) current).getAddress() - 1;
                else popBoolean();
                break;
            case AND:
                if (!peekBoolean()) pc = ((Jump) current).getAddress() - 1;
                else popBoolean();
                break;
            case IF:
                if (!popBoolean()) pc = ((Jump) current).getAddress() - 1;
                break;
            case ELSE:
                pc = ((Jump) current).getAddress() - 1;
                break;
            case PUSH:
                push(((Constant) current).getValue());
                break;
            case REF:
                push(((RefAccess) current).getRef().apply(ctx));
                break;
            case FUN:
                ((FunctionCall) current).getFunction().invokeUsing(this);
                break;
            case FORBIDDEN:
                push(!((ForbiddenAccess) current).getRef().apply(ctx));
                break;
            case IS_UPDATE:
                push(((UpdateAccess) current).getRef().apply(ctx));
                break;
            case IS_READ_ONLY:
                push(((ReadOnlyAccess) current).getRef().apply(ctx));
                break;
            case ADD_TIME_INT: {
                final Integer i = popInt();
                final Long    d = popLong();
                push(d == null || i == null ? null : Times.addSeconds(d, i));
                break;
            }
            case SUB_TIME_INT: {
                final Integer i = popInt();
                final Long    d = popLong();
                push(d == null || i == null ? null : Times.addSeconds(d, -i));
                break;
            }
            }
            pc++;
        }
        return pop();
    }  // end method evaluate

    Object pop() {
        return values.remove(values.size() - 1);
    }

    void push(Object o) {
        values.add(o);
    }

    @Contract("null -> fail")
    @NotNull private List<Object> assignmentValue(Object value) {
        if (value == null) return Colls.listOf("");
        if (value instanceof Iterable) return toList(cast(value));
        else return Colls.listOf(value);
    }

    @Nullable private BigDecimal avgDecimals(Object elements) {
        final BigDecimal s = sumDecimals(elements);
        if (s != null) {
            final Integer c = count(elements);
            if (c != null && c != 0) return s.divide(Decimals.fromInt(c), s.scale(), HALF_EVEN);
        }
        return null;
    }

    @Nullable private Double avgDoubles(Object elements) {
        final Double s = sumDoubles(elements);
        if (s != null) {
            final Integer c = count(elements);
            if (c != null && c != 0) return s / c.doubleValue();
        }
        return null;
    }

    @Nullable private Integer avgInts(Object elements) {
        final Integer s = sumIntegers(elements);
        if (s != null) {
            final Integer c = count(elements);
            if (c != null && c != 0) return s / c;
        }
        return null;
    }

    private int compare() {
        final Comparable<Object> b = cast(pop());
        final Comparable<Object> a = cast(pop());
        return a == b ? 0 : a == null ? -1 : b == null ? 1 : a.compareTo(b);
    }

    private Integer count(Object elements) {
        Integer s = 0;
        for (final Object o : (Iterable<?>) elements) {
            if (o != null) s += 1;
        }
        return s;
    }

    @Nullable private Long date(@Nullable String s) {
        return s == null ? null : parseDate(s);
    }

    @Nullable private BigDecimal decimal(String s) {
        return s == null ? null : s.isEmpty() || "0".equals(s) ? ZERO : "1".equals(s) ? ONE : new BigDecimal(s);
    }
    private boolean equal() {
        final Object a = pop();
        final Object b = pop();
        return a == b || a != null && a.equals(b);
    }

    private List<Object> list(int listSize) {
        final ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < listSize; i++)
            list.add(pop());
        // preserving list order
        reverse(list);
        return list;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private Object max(Object elements) {
        Object m = null;
        for (final Comparable<Object> c : (Iterable<Comparable<Object>>) elements) {
            if (c != null && (m == null || c.compareTo(m) > 0)) m = c;
        }
        return m;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private Object min(Object elements) {
        Object m = null;
        for (final Comparable<Object> c : (Iterable<Comparable<Object>>) elements) {
            if (c != null && (m == null || c.compareTo(m) < 0)) m = c;
        }
        return m;
    }

    private Object peek() {
        return values.get(values.size() - 1);
    }

    private Boolean peekBoolean() {
        return toBoolean(peek());
    }

    private Boolean popBoolean() {
        return toBoolean(pop());
    }

    private BigDecimal popDecimal() {
        return (BigDecimal) pop();
    }

    private Double popDouble() {
        return (Double) pop();
    }

    private Integer popInt() {
        return (Integer) pop();
    }

    @Nullable private Long popLong() {
        final Object v = pop();
        if (!(v instanceof Long) && v instanceof Number) return ((Number) v).longValue();

        return cast(v);
    }

    private String popString() {
        return (String) pop();
    }

    private void pushAssignment() {
        final boolean equals = popBoolean();
        final boolean when   = popBoolean();
        final Object  value  = pop();
        final String  field  = popString();

        push(assignment(field, assignmentValue(value), equals, when));
    }

    @Nullable private Integer sumBooleans(Object elements) {
        Integer                 s  = null;
        final Iterable<Boolean> es = cast(elements);
        for (final Boolean b : es) {
            if (b != null && b) s = s == null ? 1 : s + 1;
        }
        return s;
    }

    @Nullable private BigDecimal sumDecimals(Object elements) {
        BigDecimal                 s  = null;
        final Iterable<BigDecimal> es = cast(elements);
        for (final BigDecimal l : es) {
            if (l != null) s = s == null ? l : s.add(l);
        }
        return s;
    }
    @Nullable private Double sumDoubles(Object elements) {
        Double                 s  = null;
        final Iterable<Double> es = cast(elements);
        for (final Double l : es) {
            if (l != null) s = s == null ? l : s + l;
        }
        return s;
    }

    @Nullable private Integer sumIntegers(Object elements) {
        Integer                 s  = null;
        final Iterable<Integer> es = cast(elements);
        for (final Integer l : es) {
            if (l != null) s = s == null ? l : s + l;
        }
        return s;
    }

    @Nullable private Long time(String s) {
        return s == null ? null : parseDateTime(s);
    }

    //~ Methods ......................................................................................................................................

    private static Boolean toBoolean(Object o) {
        return Boolean.TRUE.equals(o) ? Boolean.TRUE : Boolean.FALSE;
    }
}  // end class Evaluator
