
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import static tekgenesis.common.Predefined.cast;

/**
 * Persistable instance.
 */
public interface PersistableInstance<This extends EntityInstance<This, K>, K> extends UpdatableInstance<This, K> {

    //~ Methods ......................................................................................................................................

    /** delete instance. */
    default This delete() {
        return table().entityTable().delete(cast(this));
    }

    /** persist instance ignoring optimistic lock. */
    default This forcePersist() {
        return table().entityTable().forcePersist(cast(this));
    }

    /** insert instance. */
    default This insert() {
        return table().entityTable().insert(cast(this));
    }

    /** merge instance. */
    default This merge() {
        return table().entityTable().merge(cast(this));
    }

    /** persist instance. */
    default This persist() {
        return table().entityTable().persist(cast(this));
    }
}
