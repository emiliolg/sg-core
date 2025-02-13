
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect.delta;

import tekgenesis.database.introspect.TableInfo;

/**
 * Deltas in Tables.
 */
public interface TableDeltas extends MdDelta {

    //~ Methods ......................................................................................................................................

    /** Diff the elements of the specified table. */
    MdDelta diff(String tableName, TableInfo.Element element);

    /** Returns true if the primary key has change. */
    boolean primaryKeyChange(String tableName);
}
