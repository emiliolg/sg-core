
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.code;

/**
 * A RefType to a value (Something like a pointer).
 */
public interface ValueRef<T> {

    //~ Methods ......................................................................................................................................

    /** Get the referenced value. */
    T get();
}
