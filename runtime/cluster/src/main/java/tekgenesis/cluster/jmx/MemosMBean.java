
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx;

import java.io.Serializable;

import tekgenesis.common.collections.ImmutableList;

/**
 * Memos Bean interface.
 */
public interface MemosMBean extends Serializable {

    //~ Methods ......................................................................................................................................

    /** Clear memo. */
    void force(String memo);
    /** Clear memo for key. */
    void force(String memo, Object key);

    /** Return registered memos. */
    ImmutableList<String> getMemos();
}
