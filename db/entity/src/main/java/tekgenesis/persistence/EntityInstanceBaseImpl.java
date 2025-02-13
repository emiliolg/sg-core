
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
 * Base Implementation of EntityInstance.
 */
public abstract class EntityInstanceBaseImpl<This extends EntityInstance<This, K>, K> implements EntityInstance<This, K> {

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object o) {
        if (o == this) return true;
        if (!getClass().isInstance(o) || hasEmptyKey()) return false;

        final This that = cast(o);
        return keyObject().equals(that.keyObject());
    }

    @Override public int hashCode() {
        return keyObject().hashCode();
    }

    @Override public String toString() {
        return keyAsString();
    }

    void resetModified() {}
}
