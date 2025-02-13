
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin.util;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.context.Context;
import tekgenesis.index.IndexManager;
import tekgenesis.transaction.Transaction;

/**
 * xCT Entity Index Utilities.
 */
public final class IndexUtils {

    //~ Constructors .................................................................................................................................

    private IndexUtils() {}

    //~ Methods ......................................................................................................................................

    /**
     * Rebuild indexed for the entities.
     *
     * @param  fqn  Entities FQN
     */
    public static void rebuildIndex(@NotNull final String fqn) {
        Transaction.runInTransaction(() -> {
            final IndexManager indexManager = Context.getSingleton(IndexManager.class);
            indexManager.rebuildIndex(fqn);
        });
    }
}
