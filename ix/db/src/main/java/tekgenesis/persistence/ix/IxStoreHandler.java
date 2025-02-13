
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.ix;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.context.Context;
import tekgenesis.persistence.*;
import tekgenesis.persistence.expr.Expr;

import static tekgenesis.common.Predefined.cast;

/**
 * Implementation of an StoreHandler for IxInstances.
 */
public class IxStoreHandler<T extends IxInstance<T, K>, K> extends DelegatingStoreHandler<T, K> {

    //~ Instance Fields ..............................................................................................................................

    private final DbTable<T, K> dbTable;

    private StoreHandler<T, K> delegated;
    private IxStoreHandlerType type;

    //~ Constructors .................................................................................................................................

    /** Create an IxEntityTable. */
    IxStoreHandler(@NotNull DbTable<?, K> dbTable) {
        super(IxStoreHandlerType.NULL.createHandler(dbTable));
        this.dbTable = cast(dbTable);
        delegated    = null;
        type         = null;
    }

    //~ Methods ......................................................................................................................................

    /** Check if the following key is locked in IX. */
    public boolean checkLock(@NotNull K key) {
        final IxNullStoreHandler<T, K> delegate = cast(getDelegate());
        return delegate.checkLock(key);
    }

    /** Increment field operation.* */
    public void incr(@NotNull T instance, @NotNull TableField<?> field, double value) {
        final IxNullStoreHandler<T, K> delegate = cast(getDelegate());
        final IxEntityTable<?, ?>      t        = (IxEntityTable<?, ?>) EntityTable.forTable(dbTable);

        delegate.incr(instance, field, value, t.getMDateFieldName());
    }

    @NotNull protected StoreHandler<T, K> getDelegate() {
        final IxProps            props              = Context.getEnvironment().get(IxService.getDomain(), IxProps.class);
        final IxStoreHandlerType ixStoreHandlerType = IxStoreHandlerType.get(props.url);
        if (delegated == null || type != ixStoreHandlerType) {
            type      = ixStoreHandlerType;
            delegated = ixStoreHandlerType.createHandler(dbTable);
        }
        return delegated;
    }

    //~ Methods ......................................................................................................................................

    static TableField<?>[] retrieveFields(final Expr<?>[] expressions) {  // Only Table fields are allowed
        final TableField<?>[] fields = new TableField<?>[expressions.length];
        for (int i = 0; i < expressions.length; i++) {
            final Expr<?> e = expressions[i];
            if (!(e instanceof TableField<?>)) throw new IllegalArgumentException(e.toString());
            fields[i] = (TableField<?>) e;
        }
        return fields;
    }
}  // end class IxStoreHandler
