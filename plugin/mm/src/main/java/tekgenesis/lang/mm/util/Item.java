
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.util;

import org.jetbrains.annotations.NotNull;

/**
 * Item Task.
 */
public interface Item {

    //~ Methods ......................................................................................................................................

    /**
     * Process Item.
     *
     * @param   retryable  retryable
     *
     * @return  process result
     */
    String process(@NotNull Retryable retryable);
}
