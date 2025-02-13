
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.ix;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.persistence.*;
import tekgenesis.persistence.expr.EntityCriteriaPredicate;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.immutable;

/**
 * Ideafix Store Handler supported by an in memory map.
 */
public class IxMockHandler<T extends IxInstance<T, K>, K> extends IxNullStoreHandler<T, K> {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, Map<K, T>> mapByDomain;

    //~ Constructors .................................................................................................................................

    /** Create an IxEntityTable. */
    IxMockHandler(@NotNull DbTable<T, K> dbTable) {
        super(dbTable);
        mapByDomain = new HashMap<>();

        mockHandlers.add(new WeakReference<>(this));
    }

    //~ Methods ......................................................................................................................................

    /** Delete instance. */
    public void delete(T instance) {
        getMap().remove(instance.keyObject());
    }

    @Override public void deleteWhere(final Criteria... condition) {
        if (condition.length != 0) super.deleteWhere(condition);
        else getMap().clear();
    }

    @Nullable @Override public T find(@NotNull K key) {
        return getMap().get(key);
    }

    @Override public T findPersisted(K key) {
        return find(key);
    }

    /** Increment field operation.* */
    public void incr(@NotNull T instance, @NotNull TableField<?> field, double value, String mDateFieldName) {
        final K key = instance.keyObject();
        T       t   = getMap().get(key);

        if (t == null) {
            getMap().put(key, instance);
            t = instance;
        }

        if (field instanceof TableField.Int) {
            final TableField.Int f = (TableField.Int) field;
            f.setValue(t, notNull(f.getValue(t)) + (int) value);
        }
        else if (field instanceof TableField.Decimal) {
            final TableField.Decimal f = (TableField.Decimal) field;
            f.setValue(t, notNull(f.getValue(t), BigDecimal.ZERO).add(BigDecimal.valueOf(value)));
        }
        else if (field instanceof TableField.Real) {
            final TableField.Real f = (TableField.Real) field;
            f.setValue(t, notNull(f.getValue(t), 0.0) + value);
        }
        else throw new IllegalArgumentException(field.getFieldName());
    }

    @Override public void insert(T instance, boolean generateKey) {
        final K key = instance.keyObject();
        getMap().put(key, instance);
    }

    @Override public <E> Select.Handler<E> select(final Select<E> select) {
        return new IxBaseSelectHandler<E, T, K>(select) {
            @Override protected Seq<T> values() {
                final String parameters = new IxCriteriaSerializer().accept(getWhere());

                validateQueryParameters(getIxEntityTable(), parameters);
                final Predicate<T> p = new EntityCriteriaPredicate<>(getWhere());
                return immutable(getMap().values()).filter(p);
            }
        };
    }

    @Override public void update(T instance) {
        insert(instance, false);
    }

    @NotNull private IxEntityTable<T, K> getIxEntityTable() {
        return cast(getEntityTable());
    }

    private Map<K, T> getMap() {
        final String domain = IxService.getDomain();
        if (domain.isEmpty()) throw new IllegalArgumentException("The domain cannot be null when accessing the IX Mock Server");

        return mapByDomain.computeIfAbsent(domain, k -> new LinkedHashMap<>());
    }

    //~ Methods ......................................................................................................................................

    /** Clear all mock handlers. */
    public static void cleanAll() {
        for (final WeakReference<IxMockHandler<?, ?>> mockHandler : mockHandlers) {
            final IxMockHandler<?, ?> mh = mockHandler.get();
            if (mh != null) mh.mapByDomain.clear();
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final List<WeakReference<IxMockHandler<?, ?>>> mockHandlers = new ArrayList<>();
}  // end class IxMockHandler
