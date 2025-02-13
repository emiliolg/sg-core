
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.expr;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.database.DbMacro;
import tekgenesis.database.SqlConstants;

import static java.lang.String.format;

import static tekgenesis.database.DbMacro.CurrentDate;
import static tekgenesis.database.DbMacro.CurrentTime;

/**
 * Expression Operators.
 */
public enum ExprOperator {

    //~ Enum constants ...............................................................................................................................

    NULL("null", 0),                                                                  //
    AND("%s", 0, " and ") { @Override public boolean isStrict() { return false; } },  //
    OR("(%s)", 0, " or ") { @Override public boolean isStrict() { return false; } },  //
    NOT("not (%s)", 1),                                                               //
    EQ("%s = %s", 2),                                                                 //
    NE("%s != %s", 2),                                                                //
    GE("%s >= %s", 2),                                                                //
    GT("%s > %s", 2),                                                                 //
    LE("%s <= %s", 2),                                                                //
    LT("%s < %s", 2),                                                                 //

    BETWEEN("%s between %s and %s", 3),  //
    LIKE("%s like %s", 2),               //
    NOT_LIKE("%s not like %s", 2),       //
    CASE("case %s", 1),                  //
    IN("%s in (%s)", 1, ","),            //
    IN_VALUES("%s in (%s)", 2) {
        @Override int operandsToVisit(int operands) { return 1; }

        @Override String asSql(final Object[] operands) {
            final String      var    = (String) operands[0];
            final Iterable<?> values = (Iterable<?>) ((Const<?>) operands[1]).getValue();

            final StrBuilder condition = new StrBuilder().startCollection(" or ");
            StrBuilder       clause    = new StrBuilder();
            boolean          split     = false;
            int              i         = 0;
            for (final Object e : values) {
                if (++i >= 1000) {
                    condition.appendFormat(IN.template, var, clause);
                    split  = true;
                    clause = new StrBuilder();
                    i      = 0;
                }
                clause.appendElement(SqlConstants.sqlValue(e));
            }
            condition.appendFormat(IN.template, var, clause);
            return split ? "(" + condition + ")" : condition.toString();
        }},  //

    IS_NOT_NULL("%s is not null", 1),  //
    IS_NULL("%s is null", 1),          //

    // Aggregation functions
    MAX("max(%s)", 1),                        //
    MIN("min(%s)", 1),                        //
    SUM("sum(%s)", 1),                        //
    AVG("avg(%s)", 1),                        //
    COUNT("count(%s)", 1),                    //
    COUNT_DISTINCT("count(distinct %s)", 1),  //
    COUNT_ALL("count(*)", 0),                 //
    CURRENT_TIME(CurrentTime, 0),             //
    CURRENT_DATE(CurrentDate, 0),             //

    // Numeric Functions
    ABS("abs(%s)", 1),          //
    ADD("(%s + %s)", 2),        //
    CEIL("ceil(%s)", 1),        //
    DIV("%s / %s", 2),          //
    EXP("exp(%s)", 1),          //
    FLOOR("floor(%s)", 1),      //
    LN("ln(%s)", 1),            //
    MOD("mod(%s, %s)", 2),      //
    MUL("%s * %s", 2),          //
    NEGATE("-(%s)", 1),         //
    POW("power(%s, %s)", 2),    //
    ROUND("round(%s, %s)", 2),  //
    SUB("(%s - %s)", 2),        //
    TRUNC("trunc(%s, %s)", 2),  //

    // String Functions
    STRING_LENGTH("length(%s)", 1),             //
    LOWER("lower(%s)", 1),                      //
    UPPER("upper(%s)", 1),                      //
    SUBSTR("substr(%s,%s,%s)", 3),              //
    SUBSTR1("substr(%s,%s)", 2),                //
    CONCAT("%s", 0, " || "),                    //
    EMPTY_STRING(DbMacro.EmptyString.id(), 0),  //

    // DateTime operations
    DAY("extract(day from %s)", 1),                                          //
    MONTH("extract(month from %s)", 1),                                      //
    YEAR("extract(year from %s)", 1),                                        //
    HOUR("extract(hour from %s)", 1), MINUTE("extract(minute from %s)", 1),  //
    SECOND("extract(second from %s)", 1),                                    //
    ADD_SECONDS("(%s + interval '%s' second)", 2),                           //
    SUB_SECONDS("(%s - interval '%s' second)", 2),                           //

    // Enum Set Operations
    ENUM_CONTAINS("bitand(%s, %s) != 0", 2),  //
    ENUM_CONTAINS_ALL("bitand(%s, %s) = %s", 3);

    //~ Instance Fields ..............................................................................................................................

    private final int arity;

    // Separator for variable arguments
    private final String          separator;
    @NotNull private final String template;

    //~ Constructors .................................................................................................................................

    ExprOperator(@NotNull final String template, int arity) {
        this(template, arity, null);
    }

    ExprOperator(final DbMacro macro, final int arity) {
        this(macro.name(), arity);
    }

    ExprOperator(@NotNull final String template, final int arity, @Nullable String separator) {
        this.template  = template;
        this.arity     = arity;
        this.separator = separator;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the arity. */
    public boolean hasVarArgs() {
        return separator != null;
    }

    /** Returns true if the operator supports null arguments. */
    public boolean supportsNull() {
        return false;
    }
    /** Check the number of elements in the array vs the arity of the operator. Returns the arity */
    public int validateArity(final Object[] operands) {
        final int n = operands.length;
        if (hasVarArgs()) return n;
        if (arity != n) throw new IllegalArgumentException(format("Invalid number of operands %d vs %d", n, arity));
        return arity;
    }

    /** Returns the arity. */
    public int getArity() {
        return arity;
    }

    /** Returns true if the given operator is Strict (Not lazy). */
    public boolean isStrict() {
        return true;
    }

    String asSql(final Object[] operands) {
        return format(template, processOperands(operands));
    }

    int operandsToVisit(int operands) {
        return operands;
    }

    private Object[] processOperands(final Object[] operands) {
        if (separator == null) return operands;

        final Object[] args = new Object[arity + 1];

        System.arraycopy(operands, 0, args, 0, arity);
        args[arity] = ImmutableList.fromArray(operands).drop(arity).mkString(separator);
        return args;
    }
}
