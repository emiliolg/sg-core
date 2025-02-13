
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import org.jetbrains.annotations.NotNull;

import tekgenesis.aggregate.AggregateFn;
import tekgenesis.common.Predefined;
import tekgenesis.expr.Expression;
import tekgenesis.expr.ExpressionAST;

import static tekgenesis.expr.ExpressionFactory.*;

/**
 * Aggregator utility class.
 */
class Aggregators {

    //~ Constructors .................................................................................................................................

    private Aggregators() {}

    //~ Methods ......................................................................................................................................

    static Expression createExpression(@NotNull final AggregateFn fn, @NotNull final String column) {
        final ExpressionAST expression;
        switch (fn) {
        case AVG:
            expression = avg(ref(column));
            break;
        case SUM:
            expression = sum(ref(column));
            break;
        case MAX:
            expression = max(ref(column));
            break;
        case MIN:
            expression = min(ref(column));
            break;
        case COUNT:
            expression = count(ref(column));
            break;
        case ROWS:
            expression = rows(ref(column));
            break;
        case FIRST:
        case LAST:
        default:
            throw Predefined.notImplemented(fn.name());
        }
        return expression.createExpression();
    }
}
