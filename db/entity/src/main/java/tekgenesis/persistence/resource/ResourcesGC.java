
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.resource;

import java.util.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Resource;
import tekgenesis.common.env.Environment;
import tekgenesis.common.logging.Logger;
import tekgenesis.database.Database;
import tekgenesis.database.SqlStatement;
import tekgenesis.persistence.*;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.database.DbConstants.SCHEMA_SG;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Garbage Collector for Resources.
 */
public class ResourcesGC {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Database db;

    private final Environment env;
    private final Logger      logger    = Logger.getLogger(ResourcesGC.class);
    private List<String>      resources = Collections.emptyList();

    //~ Constructors .................................................................................................................................

    /** Create a ResourcesGC. */
    public ResourcesGC(final Environment env, @NotNull Database db) {
        this.env = env;
        this.db  = db;
    }

    //~ Methods ......................................................................................................................................

    /** Purges non referenced resources. */
    public void purge() {
        final List<EntityTable<?, ?>> entitiesWithResources = getEntitiesWithResources();

        readResources(" ");
        if (!resources.isEmpty() && entitiesWithResources.isEmpty())
            logger.error("There are resources but no entities with resources found. This may be an error an will destroy all resources. Aborting");
        else {
            while (!resources.isEmpty()) {
                purge(entitiesWithResources);
                readResources(resources.get(resources.size() - 1));
            }
        }
        logger.info("Resources purged.");
    }

    private void purge(List<EntityTable<?, ?>> entitiesWithResources) {
        final Set<String> strings = removeUsed(resources, entitiesWithResources);
        if (resources.size() == strings.size())
            logger.warning("ResourcesGCTask - Erasing all resources (" + strings.size() + "). This might be an issue");

        db.getTransactionManager().runInTransaction(t -> {
            try(final SqlStatement.Prepared deleteIndex = db.sqlStatement("delete from %s.RESOURCE_INDEX where UUID = ?", SCHEMA_SG).prepare()) {
                for (final String resource : strings)
                    deleteIndex.onArgs(resource).batch();
                if (!strings.isEmpty()) {
                    deleteIndex.executeBatch();
                    db.sqlStatement("delete from %s.RESOURCE_CONTENT where SHA not in (select URL from %s.RESOURCE_INDEX)", SCHEMA_SG, SCHEMA_SG)
                      .execute();
                }
            }
        });
    }

    private void readResources(@NotNull String lastUuid) {
        //J-
        resources = db.getTransactionManager().invokeInTransaction(t ->
            db.sqlStatement("select distinct UUID from %s.RESOURCE_INDEX where UUID > ? order by UUID", SCHEMA_SG)
            .limit(BATCH_SIZE)
            .onArgs(lastUuid)
            .list(String.class)
        );
        //J+
    }

    private List<EntityTable<?, ?>> getEntitiesWithResources() {
        final List<EntityTable<?, ?>> entities = new ArrayList<>();

        for (final String entityName : TableMetadata.localEntities(env)) {
            final EntityTable<? extends EntityInstance<?, ?>, ?> t = EntityTable.forName(entityName);
            for (final TableField<?> field : t.getFields()) {
                if (field.getType().equals(Resource.class)) {
                    entities.add(t);
                    break;
                }
            }
        }
        return entities;
    }

    //~ Methods ......................................................................................................................................

    private static <T extends EntityInstance<T, K>, K> void removedUsed(final EntityTable<?, ?> entityTable, final Set<String> result) {
        final EntityTable<T, K> entity = cast(entityTable);

        final Seq<TableField.Res> resFields = entity.getFields().filter(TableField.Res.class);
        final TableField.Res      first     = resFields.getFirst().get();
        final Criteria            criteria  = resFields.foldLeft(Criteria.EMPTY, (c, res) -> c.or(res.isNotNull()));
        final TableField.Res[]    fields    = resFields.size() > 1 ? resFields.drop(1).toArray(TableField.Res[]::new) : EMPTY_RES;
        // noinspection ConfusingArgumentToVarargsMethod
        runInTransaction(() ->
                Sql.select(first, fields).from(entity.getDbTable()).distinct().where(criteria).list().forEach(instance -> {
                    for (int i = 0; i < instance.size(); i++) {
                        final Object resource = instance.get(i + 1);
                        if (resource != null) result.remove(resource.toString());
                    }
                }));
    }

    private static Set<String> removeUsed(List<String> resources, List<EntityTable<?, ?>> entityList) {
        final Set<String> result = new HashSet<>(resources);
        for (final EntityTable<?, ?> entity : entityList)
            removedUsed(entity, result);
        return result;
    }

    //~ Static Fields ................................................................................................................................

    private static final TableField.Res[] EMPTY_RES = new TableField.Res[0];

    private static final int BATCH_SIZE = 50000;
}  // end class ResourcesGC
