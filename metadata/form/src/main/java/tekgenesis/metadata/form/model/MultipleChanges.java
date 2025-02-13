
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.model;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;

/**
 * Multiple changes to be update on client view.
 */
public class MultipleChanges {

    //~ Instance Fields ..............................................................................................................................

    private final boolean      changed;
    private final Seq<Integer> indexes;

    //~ Constructors .................................................................................................................................

    MultipleChanges(boolean changed, Seq<Integer> indexes) {
        this.changed = changed;
        this.indexes = indexes;
    }

    //~ Methods ......................................................................................................................................

    /** Return true if multiple has changes. */
    public boolean hasChanges() {
        return changed;
    }

    /** Return multiple changed indexes. */
    public Seq<Integer> getIndexes() {
        return indexes;
    }

    //~ Static Fields ................................................................................................................................

    public static MultipleChanges NONE = new MultipleChanges(false, Colls.emptyList());
}
