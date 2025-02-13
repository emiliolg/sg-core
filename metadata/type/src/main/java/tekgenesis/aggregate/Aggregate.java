
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.aggregate;

import java.util.function.Consumer;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Tuple;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.expr.Expression;
import tekgenesis.expr.RefTypeSolver;

/**
 * Aggregate expression.
 */
public class Aggregate {

    //~ Instance Fields ..............................................................................................................................

    private final Expression  expr;
    private final AggregateFn fn;
    private final String      ref;

    //~ Constructors .................................................................................................................................

    Aggregate(@NotNull final Expression expr, @NotNull final AggregateFn fn, @NotNull final String ref) {
        this.expr = expr;
        this.fn   = fn;
        this.ref  = ref;
    }

    //~ Methods ......................................................................................................................................

    /** Returns aggregate expression. */
    public Expression getExpr() {
        return expr;
    }

    /** Returns aggregate function. */
    public AggregateFn getFn() {
        return fn;
    }

    /** Returns aggregate reference. */
    public String getRef() {
        return ref;
    }

    /** Compile aggregate expression. */
    void compile(@NotNull final RefTypeSolver solver) {
        expr.compile(solver);
    }

    //~ Static Fields ................................................................................................................................

    public static final Function<StreamReader, Aggregate> READER = r ->
                                                                   new Aggregate(Expression.instantiate(r),
                AggregateFn.instantiate(r),
                r.readString());

    public static final Consumer<Tuple<StreamWriter, Aggregate>> WRITER = params -> {
                                                                              assert params != null;
                                                                              final Aggregate    aggregate = params.second();
                                                                              final StreamWriter writer    = params.first();
                                                                              aggregate.expr.serialize(writer);
                                                                              aggregate.fn.serialize(writer);
                                                                              writer.writeString(aggregate.ref);
                                                                          };
}  // end class Aggregate
