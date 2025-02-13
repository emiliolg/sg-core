
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
 * An Entity that can be updated (but not deleted or created).
 */
public interface UpdatableInstance<This extends EntityInstance<This, K>, K> extends EntityInstance<This, K> {

    //~ Methods ......................................................................................................................................

    /** update instance ignoring optimistic lock. */
    default This forceUpdate() {
        return table().entityTable().forceUpdate(cast(this));
    }

    /** update instance. */
    default This update() {
        return table().entityTable().update(cast(this));
    }
}
