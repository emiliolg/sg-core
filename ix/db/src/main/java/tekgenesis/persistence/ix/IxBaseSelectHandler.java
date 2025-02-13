
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.ix;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.StepResult;
import tekgenesis.common.env.context.Context;
import tekgenesis.persistence.*;
import tekgenesis.persistence.expr.Expr;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.notImplemented;
import static tekgenesis.common.collections.Colls.mkString;

/**
 * Ideafix base select handler.
 */
abstract class IxBaseSelectHandler<E, T extends EntityInstance<T, K>, K> extends Select.Handler<E> {

    //~ Instance Fields ..............................................................................................................................

    private final Function<T, E> mapper;
    private final String         QUERY_PARAM_REGEXP = "([^=&]+)(?:=?([^&#]*))";
    private final Pattern        PATTERN            = Pattern.compile(QUERY_PARAM_REGEXP);

    //~ Constructors .................................................................................................................................

    IxBaseSelectHandler(final Select<E> select) {
        super(select);
        if (getGroupBy().length > 0) throw notImplemented("Group By not implemented for Ideafix");
        if (getHaving().length > 0) throw notImplemented("Having not implemented for Ideafix");
        if (!getJoins().isEmpty()) throw notImplemented("Join not implemented for Ideafix");
        if (getOrderBy().length > 0) throw notImplemented("Order by not implemented for Ideafix");
        mapper = rowMapper();
    }

    //~ Methods ......................................................................................................................................

    @Override protected long count() {
        return values().size();
    }

    @Override protected boolean exists() {
        return !values().isEmpty();
    }

    @Override protected <R> Option<R> forEachReturning(Function<? super E, StepResult<R>> step, Option<R> finalValue) {
        for (final E value : mappedValues()) {
            final StepResult<R> r = step.apply(value);
            if (r.isDone()) return r.getValue();
        }
        return finalValue;
    }

    @Nullable @Override protected E get() {
        return mappedValues().getFirst().getOrNull();
    }

    @Override protected ImmutableList<E> list() {
        return mappedValues().toList();
    }

    protected abstract Seq<T> values();

    @NotNull Seq<E> mappedValues() {
        return values().map(mapper);
    }

    void validateQueryParameters(@NotNull IxEntityTable<?, ?> entityTable, @NotNull String parameters) {
        final IxProps props = Context.getEnvironment().get(IxService.getDomain(), IxProps.class);
        if (props.validateQueryIndexes) {
            final String[] orParts = parameters.split("%7C%7C");
            for (final String querySection : orParts) {
                final Matcher      matcher     = PATTERN.matcher(querySection);
                final List<String> queryFields = new ArrayList<>();
                while (matcher.find())
                    queryFields.add(matcher.group(1));

                if (!entityTable.applyForIndex(queryFields.toArray(new String[queryFields.size()])))
                    throw new UnsupportedOperationException(
                        String.format("Invalid Query expression %s . Fields doesn't apply with any available indexes (%s).",
                            mkString(queryFields),
                            entityTable.getIxIndexes()));
            }
        }
    }

    private Function<T, E> rowMapper() {
        final Expr<?>[] es = getExpressions();
        if (es.length == 0) return cast(Function.identity());

        validate(es);

        if (es.length == 1 && super.getType().equals(es[0].getType())) {
            final TableField<E> tf = cast(es[0]);
            return tf::getValue;
        }

        if (!getType().equals(QueryTuple.class)) throw new IllegalArgumentException("Invalid target type " + getType());

        return cast(QueryTuple.fromEntityInstance(es));
    }

    //~ Methods ......................................................................................................................................

    /** Validate expressions. Only table fields are allowed */
    private static void validate(final Expr<?>[] expressions) {
        for (final Expr<?> expression : expressions) {
            if (!(expression instanceof TableField<?>)) throw new IllegalArgumentException(expression.toString());
        }
    }
}  // end class IxBaseSelectHandler
