
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.common.Predefined.cast;

/**
 * InnerEntityTable.
 */
public class InnerEntityTable<T extends InnerInstance<T, K, P, PK>, K, P extends EntityInstance<P, PK>, PK> extends EntityTable<T, K> {

    //~ Constructors .................................................................................................................................

    /** Create the Table. */
    public InnerEntityTable(final DbTable<T, K> dbTable) {
        super(dbTable);
    }

    //~ Methods ......................................................................................................................................

    // TOdo handle insert and  persist when seqId is not defined
    @Override public T delete(@NotNull T instance) {
        if (instance.parent().isUndefined() || instance.siblings().isUndefined()) super.delete(instance);
        else siblingsImpl(instance).delete(instance);
        return instance;
    }
    //
    @Override public T insert(@NotNull T instance) {
        if (instance.parent().isUndefined() || instance.siblings().isUndefined()) super.insert(instance);
        else siblingsImpl(instance).persist(instance, true);
        return instance;
    }

    /** persist inner. */
    @Override public T persist(@NotNull T instance) {
        if (instance.parent().isUndefined() || instance.siblings().isUndefined()) super.persist(instance);
        else siblingsImpl(instance).persist(instance, false);
        return instance;
    }

    @NotNull private InnerEntitySeqImpl<T, T, K, P, PK> siblingsImpl(@NotNull T instance) {
        return cast(instance.siblings());
    }
}
