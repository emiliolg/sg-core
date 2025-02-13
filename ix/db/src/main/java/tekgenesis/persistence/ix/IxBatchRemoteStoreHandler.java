
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

import tekgenesis.persistence.*;

/**
 * IX Store handler with support for batch operations.
 */
public class IxBatchRemoteStoreHandler<T extends IxInstance<T, K>, K> extends IxRemoteStoreHandler<T, K> {

    //~ Constructors .................................................................................................................................

    /** Create an IxEntityTable. */
    IxBatchRemoteStoreHandler(@NotNull DbTable<T, K> dbTable) {
        super(dbTable);
    }

    //~ Methods ......................................................................................................................................

    @Override public void delete(T instance) {
        if (IxService.isBatchEnabled()) {
            final BatchOperation<T> delete = BatchOperation.delete(getSchemaName(), getIxTableName(), instance.keyAsString());
            IxBatchHandler.current().add(delete);
        }
        else super.delete(instance);
    }

    public void incr(@NotNull T instance, @NotNull TableField<?> field, double value, String mDateFieldName) {
        if (IxService.isBatchEnabled()) {
            final BatchOperation<T> incr = BatchOperation.incr(getSchemaName(), getIxTableName(), instance, field, value, mDateFieldName);
            IxBatchHandler.current().add(incr);
        }
        else super.incr(instance, field, value, mDateFieldName);
    }

    @Override public void insert(T instance, boolean generateKey) {
        if (IxService.isBatchEnabled()) {
            final BatchOperation<T> post = BatchOperation.post(getSchemaName(), getIxTableName(), instance);
            IxBatchHandler.current().add(post);
        }
        else super.insert(instance, generateKey);
    }

    @Override public void update(T instance) {
        if (IxService.isBatchEnabled()) {
            final BatchOperation<T> post = BatchOperation.post(getSchemaName(), getIxTableName(), instance);
            IxBatchHandler.current().add(post);
        }
        else super.update(instance);
    }
}
