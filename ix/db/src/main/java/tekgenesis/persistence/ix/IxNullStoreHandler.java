
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.ix;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.StepResult;
import tekgenesis.common.logging.Logger;
import tekgenesis.database.Database;
import tekgenesis.persistence.*;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.notImplemented;
import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.persistence.Criteria.anyOf;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 * Mock IX Store handler.
 */
class IxNullStoreHandler<T extends IxInstance<T, K>, K> extends StoreHandler<T, K> {

    //~ Constructors .................................................................................................................................

    /** Create the Store Handler. */
    IxNullStoreHandler(DbTable<T, K> dbTable) {
        super(dbTable, null);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean checkAndLock(K key, DateTime updateTime) {
        logWarning();
        return false;
    }

    /** Check if the following key is locked in IX. */
    public boolean checkLock(K key) {
        logWarning();
        return false;
    }

    @Override public void delete(T instance) {
        logWarning();
    }

    @Override public void delete(Iterable<K> keys) {
        logWarning();
    }

    @Override public void deleteWhere(Criteria... condition) {
        throw notImplemented("deleteWhere");
    }

    @Override public T find(K key) {
        logWarning();
        return null;
    }

    @Override public T findPersisted(K key) {
        logWarning();
        return null;
    }

    /** Increment field operation.* */
    public void incr(@NotNull T instance, @NotNull TableField<?> field, double value, String mDateFieldName) {
        logWarning();
    }

    @Override public void insert(T instance, boolean generateKey) {
        logWarning();
    }

    @Override public final ImmutableList<T> list(Iterable<K> keys) {
        return selectFrom(getDbTable()).where(anyOf(map(keys, k -> getMetadata().buildKeyCriteria(k)))).toList();
    }

    @Override public void merge(@NotNull T instance) {
        update(instance);
    }

    @Override public <E> Select.Handler<E> select(final Select<E> select) {
        return new Select.Handler<E>(select) {
            @Override protected long count() {
                return 0;
            }
            @Override protected boolean exists() {
                return false;
            }

            @Override protected <R> Option<R> forEachReturning(Function<? super E, StepResult<R>> step, Option<R> finalValue) {
                return finalValue;
            }

            @Nullable @Override protected E get() {
                return null;
            }
            @Override protected ImmutableList<E> list() {
                return emptyList();
            }
        };
    }

    @Override public void update(T instance) {
        logWarning();
    }

    @Override public void updateLocking(T instance) {
        logWarning();
    }

    @Override public final Database getDatabase() {
        throw new UnsupportedOperationException();
    }

    String getSchemaName() {
        return QName.createQName(getEntityTable().getEntityName().getQualification()).getName();
    }
    String getTableName() {
        return getEntityTable().getEntityName().getName();
    }

    private void logWarning() {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        final StringBuilder sb = new StringBuilder();
        if (stackTrace != null) {
            for (final StackTraceElement stackTraceElement : stackTrace)
                sb.append(stackTraceElement.toString());
        }

        logger.warning(format("Trying to execute an operation over an Ix Null StoreHandler (%s)", sb.toString()));
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(IxNullStoreHandler.class);
}  // end class IxNullStoreHandler
