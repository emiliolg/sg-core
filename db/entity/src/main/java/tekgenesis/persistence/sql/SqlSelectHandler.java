
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.sql;

import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.StepResult;
import tekgenesis.common.util.LruCache;
import tekgenesis.database.Database;
import tekgenesis.database.RowHandler;
import tekgenesis.database.RowMapper;
import tekgenesis.database.SqlStatement;
import tekgenesis.persistence.QueryTuple;
import tekgenesis.persistence.Select;
import tekgenesis.persistence.expr.Expr;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.core.Constants.HASH_SALT;
import static tekgenesis.persistence.expr.Expr.COUNT_ALL;
import static tekgenesis.persistence.sql.StatementBuilder.buildNestedSelect;
import static tekgenesis.persistence.sql.StatementBuilder.convertToSql;

/**
 * Implementation of a {@link Select.Handler} in Sql.
 */
class SqlSelectHandler<R> extends SqlBaseSelectHandler<R> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Database     db;
    @NotNull private final RowMapper<R> entityRowMapper;

    //~ Constructors .................................................................................................................................

    SqlSelectHandler(@NotNull Select<R> select, @NotNull Database db, @NotNull RowMapper<?> entityRowMapper) {
        super(select);
        this.db              = db;
        this.entityRowMapper = cast(entityRowMapper);
    }

    //~ Methods ......................................................................................................................................

    @Override protected long count() {
        String sql = asSql(COUNT_ALL);
        if (sql == null) return 0L;

        if (getGroupBy().length > 0) sql = buildNestedSelect(convertToSql(false, false, COUNT_ALL), "(" + sql + ")");

        final ImmutableList<Long> rs = list(sql, 0, 1, r -> r.getLong(1));
        return rs.isEmpty() ? 0L : rs.get(0);
    }

    @Override protected boolean exists() {
        final String sql = asSql(Expr.constant(1));
        return sql != null && !list(sql, 0, 1, rs -> rs.getInt(1)).isEmpty();
    }

    @Override protected <S> Option<S> forEachReturning(Function<? super R, StepResult<S>> step, Option<S> finalValue) {
        final SqlStatement sqlStatement = selectStatement();
        if (sqlStatement == null) return Option.empty();
        return sqlStatement.forEach(rowHandler(step, getExpressions()), finalValue, true);
    }

    @Override protected R get() {
        final Expr<?>[] expressions = getExpressions();
        final List<R>   result      = selectStatement(0, 1, expressions);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override protected ImmutableList<R> list() {
        final long limit = getLimit();
        if (limit <= 0) return emptyList();
        final Expr<?>[] expressions = getExpressions();
        return selectStatement(getOffset(), limit, expressions);
    }

    @NotNull private <T> ImmutableList<T> list(final String sql, final long offset, final long limit, final RowMapper<T> rowMapper) {
        if (limit == 0) return emptyList();
        final long cacheTime = getCacheTime();
        if (cacheTime <= 0) return listFromDb(sql, offset, limit, rowMapper);

        final CacheEntry e = new CacheEntry(sql, offset, limit);
        final CacheEntry v = cache.get(e);
        return v != null ? v.getResult() : e.cache(listFromDb(sql, offset, limit, rowMapper), cacheTime);
    }

    @NotNull private <T> ImmutableList<T> listFromDb(final String sql, final long offset, final long limit, final RowMapper<T> rowMapper) {
        return db.sqlStatement(sql).limit(offset, limit).list(rowMapper);
    }

    private <S> RowHandler<S> rowHandler(final Function<? super R, StepResult<S>> step, final Expr<?>[] expressions) {
        if (super.getType().equals(QueryTuple.class))
            return rs -> {
                       final R o = cast(QueryTuple.fromResultSet(rs, expressions, db));
                       return step.apply(o);
                   };

        final RowMapper<R> m = rowMapper(expressions);
        return rs -> step.apply(m.mapRow(rs));
    }

    private RowMapper<R> rowMapper(final Expr<?>[] expressions) {
        if (expressions.length == 0) return entityRowMapper;
        if (expressions.length == 1 && super.getType().equals(expressions[0].getType())) {
            final Expr<R> e = cast(expressions[0]);
            return rs -> e.getValueFromResultSet(rs, 1);
        }

        return QueryTuple.rowMapper(super.getType(), expressions);
    }

    @Nullable private SqlStatement selectStatement() {
        final Expr<?>[] expressions = getExpressions();
        final String    sql         = asSql(expressions);
        if (sql == null) return null;
        return db.sqlStatement(sql).limit(getOffset(), getLimit());
    }

    @NotNull private ImmutableList<R> selectStatement(final long offset, final long limit, final Expr<?>... expressions) {
        final String sql = asSql(expressions);
        return sql == null ? emptyList() : list(sql, offset, limit, rowMapper(expressions));
    }

    //~ Static Fields ................................................................................................................................

    private static final int                              MAX_CACHE_WEIGHT = 100_000;
    private static final LruCache<CacheEntry, CacheEntry> cache            = new LruCache.Builder<CacheEntry, CacheEntry>().weigher(
                CacheEntry::weight).maxWeight(MAX_CACHE_WEIGHT).withExpiration(CacheEntry::getExpiration)
                                                                             .build();

    //~ Inner Classes ................................................................................................................................

    private static final class CacheEntry {
        private long             expiration;
        private final int        hash;
        private final long       limit;
        private final long       offset;
        private ImmutableList<?> result;
        private final String     sql;

        CacheEntry(final String sql, final long offset, final long limit) {
            this.sql    = sql;
            this.offset = offset;
            this.limit  = limit;
            result      = emptyList();
            hash        = sql.hashCode() + HASH_SALT * (Long.hashCode(offset) + HASH_SALT * Long.hashCode(limit));
        }

        @Override public boolean equals(final Object o) {
            if (this == o) return true;
            if (o instanceof CacheEntry) {
                final CacheEntry cacheEntry = (CacheEntry) o;
                return sql.equals(cacheEntry.sql) && offset == cacheEntry.offset && limit == cacheEntry.limit;
            }
            return false;
        }

        @Override public int hashCode() {
            return hash;
        }

        int weight() {
            return result.size();
        }
        long getExpiration() {
            return expiration;
        }

        <R> ImmutableList<R> getResult() {
            return cast(result);
        }

        private <R> ImmutableList<R> cache(final ImmutableList<R> rs, final long cacheTime) {
            result     = rs;
            expiration = DateTime.currentTimeMillis() + cacheTime;
            cache.put(this, this);
            return cast(result);
        }
    }  // end class CacheEntry
}  // end class SqlSelectHandler
