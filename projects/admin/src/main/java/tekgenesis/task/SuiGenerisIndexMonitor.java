
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import org.jetbrains.annotations.NotNull;

import tekgenesis.admin.notice.Advice;
import tekgenesis.admin.notice.AdviceType;
import tekgenesis.admin.notice.Level;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.index.IndexManager;
import tekgenesis.index.IndexSearcher;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.Sql;

import static java.lang.String.format;

import static tekgenesis.common.env.context.Context.getEnvironment;
import static tekgenesis.common.env.context.Context.getSingleton;
import static tekgenesis.persistence.DbTable.forName;
import static tekgenesis.persistence.TableMetadata.searchableEntities;
import static tekgenesis.task.Status.ok;

/**
 * User class for Task: SuiGenerisIndexMonitor
 */
public class SuiGenerisIndexMonitor extends SuiGenerisIndexMonitorBase {

    //~ Constructors .................................................................................................................................

    private SuiGenerisIndexMonitor(@NotNull ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Status run() {
        final IndexManager manager = getSingleton(IndexManager.class);

        final ImmutableList<IndexSearcher> searchers = searchableEntities(getEnvironment()).map(e ->
                    forName(e).metadata()
                              .getIndexSearcher()).filter(Option::isPresent)
                                                       .map(Option::get)
                                                       .toList();

        // Check for deprecated indexes (with older mappings) and delete them.
        manager.deleteDeprecated(searchers);

        // Check for inconsistencies in indexes and log them
        searchers.forEach(searcher -> logInconsistencies(manager, searcher));

        return ok();
    }

    private void logInconsistencies(@NotNull final IndexManager manager, @NotNull final IndexSearcher searcher) {
        final double        indexCount = manager.getIndexCount(searcher);
        final DbTable<?, ?> dbTable    = forName(searcher.getEntityFqn());

        final long dbCount = Sql.selectFrom(dbTable).where(searcher.allow()).count();

        if (indexCount != dbCount) {
            final String format = format("Inconsistent index %s for entity %s. DbCount: %d, IndexCount: %s",
                    searcher.getAlias(),
                    searcher.getEntityFqn(),
                    dbCount,
                    indexCount);
            logger().error(format);
            Advice.create(format, Level.WARNING, AdviceType.INCONSISTENT_INDEX).insert();
        }
    }
}  // end class SuiGenerisIndexMonitor
