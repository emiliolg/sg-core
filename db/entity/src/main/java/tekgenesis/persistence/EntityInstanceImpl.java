
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

/**
 * Base Implementation of EntityInstance.
 */
public abstract class EntityInstanceImpl<This extends EntityInstance<This, K>, K> extends EntityInstanceBaseImpl<This, K> {

    //~ Instance Fields ..............................................................................................................................

    private boolean modified = false;

    //~ Methods ......................................................................................................................................

    @Override public final boolean modified() {
        return modified;
    }

    protected void markAsModified() {
        modified = true;
    }

    @Override void resetModified() {
        modified = false;
    }
}  // end class EntityInstanceImpl
