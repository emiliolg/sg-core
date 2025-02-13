
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.Collection;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.collections.Traversable;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.StepResult;
import tekgenesis.database.DbMacro;
import tekgenesis.persistence.expr.Expr;
import tekgenesis.persistence.sql.SqlBaseSelectHandler;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.database.DbMacro.*;
import static tekgenesis.persistence.EntityTable.forTable;

/**
 * A Query builder.
 */
public class Select<T> extends TableLike<T> implements Traversable<T> {

    //~ Instance Fields ..............................................................................................................................

    private final long cacheTime;  // Cache Time in milliseconds

    private final Expr<?>[]     expressions;
    private final EnumSet<Flag> flags;

    private final TableLike<?> from;
    private final Expr<?>[]    groupBy;

    private final Criteria[] having;

    private final Seq<Join>      joins;
    private final long           limit;
    private final long           offset;
    private final OrderSpec<?>[] orderBy;

    private final boolean    singleTable;
    private final Class<T>   type;
    private final Seq<Union> unions;

    private final Criteria[] where;

    //~ Constructors .................................................................................................................................

    protected Select(final Select<T> s, @Nullable final Join join, @Nullable Union union) {
        type        = s.type;
        from        = s.from;
        expressions = s.expressions;
        having      = s.having;
        orderBy     = s.orderBy;
        groupBy     = s.groupBy;
        where       = s.where;
        offset      = s.offset;
        limit       = s.limit;
        flags       = s.flags;
        cacheTime   = s.cacheTime;
        unions      = union == null ? s.unions : s.unions.append(union);
        joins       = join == null ? s.joins : s.joins.append(join);
        singleTable = s.singleTable && joins.isEmpty();
    }

    private Select(final TableLike<?> from, final Class<T> type, final Expr<?>[] es) {
        this.type   = type;
        this.from   = from;
        singleTable = from.isSingleTable();
        expressions = es;
        offset      = 0;
        limit       = Long.MAX_VALUE;
        cacheTime   = 0;
        flags       = EnumSet.noneOf(Flag.class);
        having      = EMPTY_CRITERIA;
        joins       = emptyIterable();
        unions      = emptyIterable();
        orderBy     = EMPTY_ORDER;
        groupBy     = EMPTY_EXPR;
        where       = EMPTY_CRITERIA;
    }

    private Select(final Select<T> s, final long offset, final long limit, final long cacheTime, final EnumSet<Flag> flags) {
        type           = s.type;
        from           = s.from;
        expressions    = s.expressions;
        having         = s.having;
        joins          = s.joins;
        unions         = s.unions;
        orderBy        = s.orderBy;
        groupBy        = s.groupBy;
        where          = s.where;
        this.offset    = offset;
        this.limit     = limit;
        this.cacheTime = cacheTime;
        singleTable    = s.singleTable;
        this.flags     = flags;
    }
    private Select(final Select<T> s, final Criteria[] where, final Expr<?>[] groupBy, final Criteria[] having, final OrderSpec<?>[] orderBy) {
        type         = s.type;
        from         = s.from;
        expressions  = s.expressions;
        offset       = s.offset;
        limit        = s.limit;
        flags        = s.flags;
        joins        = s.joins;
        unions       = s.unions;
        cacheTime    = s.cacheTime;
        this.where   = where;
        this.groupBy = groupBy;
        this.having  = having;
        this.orderBy = orderBy;
        singleTable  = s.singleTable;
    }

    //~ Methods ......................................................................................................................................

    /** Returns a SubQuery. */
    public SubQuery<T> as(final String aliasName) {
        return new SubQuery<>(this, aliasName);
    }

    /** Returns the statement as an Sql string. */
    public String asSql() {
        return notNull(SqlBaseSelectHandler.asSql(this));
    }

    /** Cache for the specified number of seconds. */
    @NotNull public Select<T> cache(int seconds) {
        return cache(seconds, TimeUnit.SECONDS);
    }

    /** Cache for the specified number of timeUnits. */
    @NotNull public Select<T> cache(int n, TimeUnit timeUnit) {
        return new Select<>(this, offset, limit, timeUnit.toMillis(n), flags);
    }

    /** Return the number of elements that fulfil the query. */
    public long count() {
        return storeHandler().select(this).count();
    }

    /** Make the query return non-duplicate elements. */
    @NotNull public Select<T> distinct() {
        return new Select<>(this, offset, limit, cacheTime, addFlag(flags, Flag.DISTINCT));
    }

    /** Return true if there is at least one element that fulfils the condition. */
    public boolean exists() {
        return storeHandler().select(this).exists();
    }

    /** Performs an action for each element of this query. */
    public void forEach(@NotNull Consumer<? super T> consumer) {
        Traversable.super.forEach(consumer);
    }

    /** Performs an action for each element of this query. And returns a result when done */
    public <R> Option<R> forEachReturning(@NotNull Function<? super T, StepResult<R>> step, Option<R> finalValue) {
        return storeHandler().select(this).forEachReturning(step, finalValue);
    }

    /** Make the query lock for update. */
    @NotNull public Select<T> forUpdate(ForUpdateFlag... forUpdateFlags) {
        final EnumSet<Flag> fs = addFlag(flags, Flag.FOR_UPDATE);
        for (final ForUpdateFlag forUpdateFlag : forUpdateFlags)
            fs.add(forUpdateFlag.selectFlag);
        return new Select<>(this, offset, limit, cacheTime, fs);
    }

    /** get the first row. */
    @Nullable public T get() {
        return storeHandler().select(this).get();
    }

    /** Group by the specified expressions. */
    @NotNull public Select<T> groupBy(Expr<?>... groupByExpressions) {
        return new Select<>(this, where, groupByExpressions, having, orderBy);
    }

    /** Defines the filters for aggregation. */
    @NotNull public Select<T> having(Criteria... havingCriteria) {
        return new Select<>(this, where, groupBy, havingCriteria, orderBy);
    }

    /** Join with the specified Table using the given Criteria. */
    @NotNull public Select<T> join(TableLike<?> table, Criteria... joinCondition) {
        return new Select<>(this, new Join(table, joinCondition, false), null);
    }

    /** Left Outer Join with the specified Table using the given Criteria. */
    @NotNull public Select<T> leftOuterJoin(TableLike<?> table, Criteria... joinCondition) {
        return new Select<>(this, new Join(table, joinCondition, true), null);
    }
    /** Limit the number of entries retrieved. */
    @NotNull public Select<T> limit(long size) {
        return new Select<>(this, offset, size, cacheTime, flags);
    }

    /** List the specified rows. */
    @NotNull public ImmutableList<T> list() {
        return limit == 0 ? emptyList() : storeHandler().select(this).list();
    }

    /** Start reading from the specified offset (discard n entries). */
    @NotNull public Select<T> offset(long l) {
        return new Select<>(this, l, limit, cacheTime, flags);
    }

    /** Order the query according to the given {@link OrderSpec}. */
    @NotNull public Select<T> orderBy(OrderSpec<?>... orderByExpressions) {
        return new Select<>(this, where, groupBy, having, orderByExpressions);
    }

    @NotNull @Override public ImmutableList<T> toList() {
        return list();
    }

    /**
     * Union of this select clause with a target select clause. Removes duplicate rows from result.
     */
    @NotNull public Select<T> union(Select<?> target) {
        return new Select<>(this, null, new Union(target, false));
    }

    /**
     * Union of this select clause with a target select clause. Includes all rows (even duplicate
     * ones).
     */
    @NotNull public Select<T> unionAll(Select<?> target) {
        return new Select<>(this, null, new Union(target, true));
    }

    /** Add a where clause to the query that implements the <b>and</b> over all conditions. */
    @NotNull public Select<T> where(final Criteria... allOff) {
        return new Select<>(this, allOff, groupBy, having, orderBy);
    }

    /** get the first value or the specified one if none exists. */
    @NotNull public T getOrElse(T defaultValue) {
        return notNull(get(), defaultValue);
    }

    public Class<T> getType() {
        return type;
    }

    String alias() {
        return from.getDbTable().metadata().getTableName();
    }

    @Override String asTableExpression() {
        return "(" + asSql() + ") " + alias();
    }

    @Override Collection<TableField<?>> fields() {
        return from.fields();
    }

    @Override <I extends EntityInstance<I, K>, K> DbTable<I, K> getDbTable() {
        return from.getDbTable();
    }

    @Override boolean isSingleTable() {
        return singleTable;
    }

    @Override Expr<?>[] getExpressions() {
        return expressions;
    }

    private boolean cacheEnabled() {
        return storeHandler().getDatabase().getConfiguration().selectCacheEnabled;
    }

    private <I extends EntityInstance<I, K>, K> StoreHandler<I, K> storeHandler() {
        final DbTable<I, K> f = from.getDbTable();
        return forTable(f).getStoreHandler();
    }

    //~ Methods ......................................................................................................................................

    @NotNull private static EnumSet<Flag> addFlag(final EnumSet<Flag> flags, final Flag flag) {
        final EnumSet<Flag> c = EnumSet.copyOf(flags);
        c.add(flag);
        return c;
    }

    //~ Static Fields ................................................................................................................................

    private static final OrderSpec<?>[] EMPTY_ORDER = new OrderSpec<?>[0];

    static final Criteria[] EMPTY_CRITERIA = new Criteria[0];
    static final Expr<?>[]  EMPTY_EXPR     = new Expr<?>[0];

    //~ Enums ........................................................................................................................................

    public enum ForUpdateFlag {
        NO_WAIT(Flag.NO_WAIT), SKIP_LOCKED(Flag.SKIP_LOCKED);

        private final Flag selectFlag;

        ForUpdateFlag(Flag selectFlag) {
            this.selectFlag = selectFlag;
        }
    }

    public enum Flag {
        DISTINCT(Distinct), NO_WAIT(NoWait), SKIP_LOCKED(SkipLocked), FOR_UPDATE(ForUpdate);
        private final String txt;

        Flag(DbMacro m) {
            txt = m.toString();
        }

        /** Return the text Representation if the flag is present. */
        public String asStringIfPresent(EnumSet<Flag> flags) {
            return flags.contains(this) ? txt + " " : "";
        }
    }

    //~ Inner Classes ................................................................................................................................

    public static class Builder<T> {
        private final Expr<?>[] expr;
        private final Class<T>  type;

        Builder(Class<T> clazz, Expr<?>... expr) {
            this.expr = expr;
            type      = clazz;
        }

        /** Add all the columns of the specified table. */
        public Builder<QueryTuple> andAllOf(final TableLike<?> tableLike) {
            final Collection<TableField<?>> fields  = tableLike.fields();
            int                             i       = expr.length;
            final Expr<?>[]                 newExpr = new Expr<?>[i + fields.size()];
            System.arraycopy(expr, 0, newExpr, 0, i);
            for (final TableField<?> field : fields)
                newExpr[i++] = field;
            return new Builder<>(QueryTuple.class, newExpr);
        }

        /** Convert each row of the result to items of the specified class. */
        public <E> Builder<E> as(Class<E> clazz) {
            return new Builder<>(clazz, expr);
        }

        /** Create a Sql Query. */
        public Select<T> from(final TableLike<?> tableLike) {
            return new Select<>(tableLike, type, expr);
        }
    }

    /**
     * The class that really executes the select statement.
     */
    public abstract static class Handler<T> {
        private final Select<T> select;

        protected Handler(Select<T> select) {
            this.select = select;
        }

        protected abstract long count();
        protected abstract boolean exists();
        protected abstract <R> Option<R> forEachReturning(Function<? super T, StepResult<R>> step, Option<R> finalValue);

        protected String from() {
            return select.from.asTableExpression();
        }
        protected Iterable<TableField<?>> fromFields() {
            return select.from.fields();
        }
        @Nullable protected abstract T get();
        protected abstract ImmutableList<T> list();

        protected boolean qualify() {
            return !select.singleTable;
        }
        protected long getCacheTime() {
            return select.cacheEnabled() ? select.cacheTime : -1;
        }
        protected Expr<?>[] getExpressions() {
            return select.expressions;
        }
        protected EnumSet<Flag> getFlags() {
            return select.flags;
        }
        protected Expr<?>[] getGroupBy() {
            return select.groupBy;
        }
        protected Criteria[] getHaving() {
            return select.having;
        }
        protected Seq<Join> getJoins() {
            return select.joins;
        }
        protected long getLimit() {
            return select.limit;
        }
        protected long getOffset() {
            return select.offset;
        }
        protected OrderSpec<?>[] getOrderBy() {
            return select.orderBy;
        }
        protected Class<T> getType() {
            return select.type;
        }
        protected Seq<Union> getUnions() {
            return select.unions;
        }
        protected Criteria[] getWhere() {
            return select.where;
        }
    }  // end class Handler

    public static class Join {
        public final Criteria[]    criteria;
        public final boolean       left;
        private final TableLike<?> target;

        Join(final TableLike<?> target, final Criteria[] criteria, final boolean left) {
            this.target   = target;
            this.left     = left;
            this.criteria = criteria;
        }

        /** Return the target as an Sql table reference. */
        public String getTarget() {
            return target.asTableExpression();
        }
    }

    public static class Union {
        public final boolean    all;
        private final Select<?> target;

        Union(Select<?> target, final boolean all) {
            this.target = target;
            this.all    = all;
        }

        /** Return the target as an Sql. */
        public String getTarget() {
            return "(" + target.asSql() + ")";
        }
    }
}  // end class Select
