
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
 * Base Class for Ideafix Records.
 */
public interface IxInstance<T extends PersistableInstance<T, K>, K> extends PersistableInstance<T, K> {

    //~ Methods ......................................................................................................................................

    /** Check if a lock is present in ix for the current instance. */
    boolean checkLock();

    /** Increment field by #value. */
    void incr(TableField<?> field, double value);
}
