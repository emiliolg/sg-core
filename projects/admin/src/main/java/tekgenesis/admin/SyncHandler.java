
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.*;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.media.MediaType;
import tekgenesis.database.Database;
import tekgenesis.database.Databases;
import tekgenesis.database.support.JdbcUtils;
import tekgenesis.metadata.entity.TypeDef;
import tekgenesis.metadata.entity.View;
import tekgenesis.persistence.*;
import tekgenesis.persistence.resource.DbResource;
import tekgenesis.repository.ModelRepository;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.task.ViewRefresher;
import tekgenesis.type.MetaModel;
import tekgenesis.type.resource.AbstractResource;
import tekgenesis.type.resource.SimpleResourceImpl;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.DateTime.EPOCH;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.database.DbConstants.SCHEMA_SG;
import static tekgenesis.persistence.Criteria.EMPTY;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 * Servlet to handle REST operations for enums: only get is supported writing the key-labels
 */
public class SyncHandler extends SyncHandlerBase {

    //~ Constructors .................................................................................................................................

    SyncHandler(@NotNull final Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<String> sync(@NotNull String path, @NotNull DateTime sync_from, @Nullable String keyFrom,
                                                  @Nullable Integer batchSize, @NotNull Seq<String> fields, @Nullable Boolean initial) {
        if (!enabled()) return forbidden();

        final ObjectNode result = writeInstancesJson(RestHandler.getFqn(path),
                sync_from,
                keyFrom,
                batchSize,
                fields,
                initial != null ? initial : false);
        return castResult(result).withContentType(MediaType.APPLICATION_JSON);
    }

    private Result<String> castResult(ObjectNode result) {
        // Hack! Until SG support Json type :S
        return cast(ok(result));
    }

    private boolean enabled() {
        return Context.getProperties(ApplicationProps.class).syncEnabled;
    }

    private void resolveFqnList(List<String> fqnList, String fqn) {
        final Option<MetaModel> model = Context.getContext().getSingleton(ModelRepository.class).getModel(QName.createQName(fqn));
        if (model.isPresent()) {
            if (model.get() instanceof View) {
                for (final MetaModel metaModel : ((View) model.get()).entities())
                    resolveFqnList(fqnList, metaModel.getFullName());
            }
            else {
                String finalFqn = fqn;
                if (model.get() instanceof TypeDef) finalFqn = ((TypeDef) model.get()).getFinalType().getImplementationClassName();
                fqnList.add(finalFqn);
            }
        }
    }

    private void solveDeletions(String fqn, DateTime from, boolean initial, ObjectNode json) {
        if (!initial || from.equals(EPOCH)) return;
        final List<String> fqnList = new ArrayList<>();
        resolveFqnList(fqnList, fqn);
        int      deletedViews = 0;
        DateTime lastDeleted  = DateTime.current();
        for (final String base : fqnList) {
            if (!base.equals(fqn)) {
                final Tuple<DateTime, Integer> count = Databases.openDefault()
                                                       .sqlStatement(
                            "select max(TS), count(DELETED_KEY) from %s.DELETED_INSTANCES where ENTITY = ? and TS > ?",
                            SCHEMA_SG).onArgs(base, from).get(r ->
                            Tuple.tuple(notNull(JdbcUtils.fromTimestamp(r.getTimestamp(1)), EPOCH), r.getInt(2)));
                if (count != null && count.second() > 0) {
                    deletedViews++;
                    if (count.first().isGreaterThan(lastDeleted)) lastDeleted = count.first();
                }
            }
        }

        if (deletedViews > 0) {
            json.put(ViewRefresher.DELETED_INSTANCES, "ALL");
            json.put(ViewRefresher.LAST_DELETED, lastDeleted.toString());
        }
        else {
            json.put(ViewRefresher.DELETED_INSTANCES,
                Databases.openDefault()
                         .sqlStatement("select DELETED_KEY from %s.DELETED_INSTANCES where ENTITY = ? and TS > ?", SCHEMA_SG)
                         .onArgs(fqn, from)
                         .list(String.class)
                         .mkString(","));
            final DateTime dateTime = Databases.openDefault()
                                      .sqlStatement("select max(TS) from %s.DELETED_INSTANCES where ENTITY = ? and TS > ?", SCHEMA_SG)
                                      .onArgs(fqn, from)
                                      .get(DateTime.class);
            if (dateTime != null) json.put(ViewRefresher.LAST_DELETED, dateTime.toString());
        }
    }

    private <T extends EntityInstance<T, K>, K> ObjectNode writeInstancesJson(String fqn, DateTime from, @Nullable String keyFrom, Integer limit,
                                                                              Seq<String> fields, boolean initial) {
        final ObjectMapper mapper = JsonMapping.shared();
        final ObjectNode   json   = mapper.createObjectNode();

        solveDeletions(fqn, from, initial, json);

        final ResourceResolver        resolver = new ResourceResolver();
        final DbTable<T, K>           table    = DbTable.forName(fqn);
        final TableMetadata<T, K>     md       = table.metadata();
        final Tuple<List<T>, Boolean> records  = getRecords(table,
                from,
                keyFrom == null || keyFrom.isEmpty() ? Option.empty() : Option.of(md.keyFromString(keyFrom)),
                notNull(limit, SYNC_LIMIT),
                keyFrom == null);

        // Add resources to resolver first
        for (final T e : records.first()) {
            for (final String fn : fields)
                md.getField(fn).castTo(TableField.Res.class).ifPresent(field -> resolver.add(field.getValue(e)));
        }

        // Write all fields
        for (final T e : records.first()) {
            final StringBuilder builder = new StringBuilder();
            for (final String fn : fields)
                md.getField(fn).ifPresent(field -> {
                    try {
                        final String valueStr = field instanceof TableField.Res ? resolver.resourceValue(mapper, e, (TableField.Res) field)
                                                                                : String.valueOf(field.getValue(e));
                        builder.append(fn).append("=").append(valueStr).append(ViewRefresher.FIELD_SEPARATOR);
                    }
                    catch (final BadResourceException ignore) {}
                });
            json.put(e.keyAsString(), builder.toString());
        }
        json.put(ViewRefresher.MORE, records.second());
        return json;
    }  // end method writeInstancesJson

    private <T extends EntityInstance<T, K>, K> Tuple<List<T>, Boolean> getRecords(DbTable<T, K> table, DateTime from, Option<K> keyFrom, int limit,
                                                                                   boolean expand) {
        final TableMetadata<T, K> metadata   = table.metadata();
        final TableField.DTime    updateTime = ensureNotNull(metadata.getUpdateTimeField());

        final List<OrderSpec<?>> orderByList = new ArrayList<>();
        orderByList.add(updateTime);
        for (final TableField<?> tableField : metadata.getPrimaryKey())
            orderByList.add(tableField.nlsSort("BINARY"));

        Criteria keyCriteria = updateTime.gt(from);

        if (keyFrom.isPresent()) keyCriteria = keyCriteria.or(updateTime.eq(from).and(metadata.buildKeyCriteriaGT(keyFrom.get())));

        final OrderSpec<?>[] orderBy         = orderByList.toArray(new OrderSpec<?>[metadata.getPrimaryKey().size() + 1]);
        final List<T>        entityInstances = selectFrom(table).limit(limit).where(from.equals(EPOCH) ? EMPTY : keyCriteria).orderBy(orderBy).list();

        if (!expand || entityInstances.size() < limit) return tuple(entityInstances, entityInstances.size() >= limit);

        return getRecordsDeprecated(table, from, limit, entityInstances);
    }

    private <T extends EntityInstance<T, K>, K> Tuple<List<T>, Boolean> getRecordsDeprecated(DbTable<T, K> table, DateTime from, int limit,
                                                                                             List<T> entityInstances) {
        final int  last          = entityInstances.size() - 1;
        final long lastEventTime = entityInstances.get(last).getUpdateTime().toMilliseconds();

        for (int endPoint = last - 1; endPoint >= 0; endPoint--) {
            if (entityInstances.get(endPoint).getUpdateTime().toMilliseconds() != lastEventTime && endPoint > 0)
                return tuple(entityInstances.subList(0, endPoint + 1), true);
        }
        return getRecords(table, from, empty(), limit * LIMIT_INCREMENT, true);
    }

    //~ Static Fields ................................................................................................................................

    private static final int    SYNC_LIMIT      = 30000;
    private static final int    LIMIT_INCREMENT = 3;
    private static final Logger logger          = Logger.getLogger(SyncHandler.class);

    //~ Inner Classes ................................................................................................................................

    private class BadResourceException extends Exception {
        private static final long serialVersionUID = 3431772751551744741L;
    }

    private class ResourceResolver {
        HashMap<String, Map<String, Resource.Entry>> resources = new HashMap<>();
        private boolean                              notLoaded = true;

        public void add(@Nullable Resource value) {
            if (value != null) resources.put(value.getUuid(), new HashMap<>());
        }

        private void loadResources() {
            final Database                  db = Databases.openDefault();
            final Seq<DbResource.EntryInfo> es = DbResource.loadEntries(db, Colls.immutable(resources.keySet()));
            for (final DbResource.EntryInfo e : es) {
                final Map<String, Resource.Entry> map = resources.get(e.getUUID());
                map.put(e.getVariant(),
                    new AbstractResource.EntryImpl(e.getVariant(), e.isExternal(), e.getName(), e.getUrl(), e.getMimeType(), e.getMetadata()));
            }

            notLoaded = false;
        }

        private SimpleResourceImpl resource(Resource resource) {
            return new SimpleResourceImpl(resource.getUuid(), resources.get(resource.getUuid()));
        }

        @Nullable private <I extends EntityInstance<I, K>, K> String resourceValue(ObjectMapper mapper, I e, TableField.Res resourceField)
            throws BadResourceException
        {
            if (notLoaded) loadResources();

            final Resource resource = resourceField.getValue(e);
            if (resource != null) {
                final String valueStr;
                try {
                    valueStr = mapper.writeValueAsString(resource(resource));
                }
                catch (final IOException ex) {
                    logger.warning(ex);
                    throw new BadResourceException();
                }
                return valueStr;
            }
            return null;
        }
    }  // end class ResourceResolver
}  // end class SyncHandler
