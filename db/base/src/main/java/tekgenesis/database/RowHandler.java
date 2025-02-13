
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.StepResult;

/**
 * An interface used to process rows handled by the {@link SqlStatement#forEach(RowHandler)}.
 */
@FunctionalInterface public interface RowHandler<T> {

    //~ Methods ......................................................................................................................................

    /** Accept the row and process it. */
    @SuppressWarnings("RedundantThrows")
    StepResult<T> accept(@NotNull ResultSet rs)
        throws SQLException;
}
