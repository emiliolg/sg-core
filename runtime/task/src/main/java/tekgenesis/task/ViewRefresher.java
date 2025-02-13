
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.core.*;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.*;
import tekgenesis.common.invoker.metric.InvocationKeyGenerator;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.service.Method;
import tekgenesis.common.util.Files;
import tekgenesis.database.Database;
import tekgenesis.database.SqlStatement;
import tekgenesis.database.type.Lob;
import tekgenesis.index.ElasticSearchIndexer;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.View;
import tekgenesis.metadata.entity.ViewAttribute;
import tekgenesis.persistence.*;
import tekgenesis.persistence.expr.Expr;
import tekgenesis.properties.SchemaProps;
import tekgenesis.properties.ViewProps;
import tekgenesis.type.resource.SimpleResourceImpl;

import static java.util.UUID.randomUUID;

import static com.fasterxml.jackson.core.JsonToken.*;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.ImmutableList.fromArray;
import static tekgenesis.common.core.Constants.NULL_TO_STRING;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.core.Tuple.tupleFromList;
import static tekgenesis.database.type.Lob.createClob;
import static tekgenesis.md.MdConstants.UPDATE_TIME;
import static tekgenesis.persistence.Sql.databaseFor;
import static tekgenesis.persistence.Sql.select;
import static tekgenesis.persistence.resource.DbResource.clobToString;
import static tekgenesis.persistence.resource.ResourcesConstants.*;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;
import static tekgenesis.type.resource.AbstractResource.EntryImpl;
import static tekgenesis.type.resource.AbstractResource.MASTER;

/**
 * User class for Task: RefreshRemoteViewTask
 */
public class ViewRefresher<T extends EntityInstance<T, K>, K> {

    //~ Instance Fields ..............................................................................................................................

    private final DbObject            baseEntity;
    private final Database            db;
    private final DbTable<T, K>       dbTable;
    private final Map<String, String> fieldMapping;
    private final List<String>        fields;

    private boolean                   force;
    private DateTime                  lastDeleted;
    private final TableMetadata<T, K> metadata;
    private String                    removedKeys = null;

    private final SchemaProps schemaProps;

    private final View              view;
    private final ViewProps         viewProps;
    private final EntityTable<T, K> viewTable;

    //~ Constructors .................................................................................................................................

    ViewRefresher(View view, DbObject baseEntity) {
        this.view       = view;
        lastDeleted     = DateTime.EPOCH;
        viewTable       = EntityTable.forName(view.getFullName());
        dbTable         = viewTable.getDbTable();
        metadata        = dbTable.metadata();
        db              = databaseFor(dbTable);
        schemaProps     = Context.getProperties(baseEntity.getSchema(), SchemaProps.class);
        viewProps       = Context.getProperties(view.getFullName(), ViewProps.class);
        this.baseEntity = baseEntity;
        fields          = new ArrayList<>();
        fieldMapping    = new HashMap<>();

        for (final Attribute attribute : view.allAttributes())
            collectFields(attribute, "");
    }

    //~ Methods ......................................................................................................................................

    Status refresh(Map<String, RefreshRemoteViewTaskBase.RefreshViewListener.RefreshResult> refreshed) {
        if (isEmpty(schemaProps.remoteUrl)) {
            logger.warning("No remoteUrl property defined for schema " + baseEntity.getSchema());
            return Status.ok();
        }
        logger.info("Refreshing view: " + view.getFullName());

        final Tuple<DateTime, String> t = invokeInTransaction(this::findLastItem);

        final RefreshResult refresh = refresh(t._1(),
                t._2(),
                viewProps.batchSize != null ? viewProps.batchSize : view.getBatchSize(),
                viewProps.ignoreDeletions,
                true);
        if (refresh.syncedData()) {
            refreshed.put(view.getFullName(), RefreshRemoteViewTaskBase.RefreshViewListener.result(t._1(), force, removedKeys));
            runInTransaction(() -> viewTable.updateLastDeleted(lastDeleted));
        }
        return refresh.status();
    }  // end method refresh

    /** Refreshing View. */

    private void collectFields(Attribute value, String prefix) {
        if (value.getType().isDatabaseObject()) {
            final DbObject dbObject = value.asDatabaseObject().get();
            for (final Attribute attribute : dbObject.getPrimaryKey()) {
                if (attribute.getType().isDatabaseObject()) {
                    final String baseName = dbObject.asView().getBaseEntity().get().getName();
                    collectFields(attribute, prefix + (!prefix.isEmpty() ? capitalizeFirst(baseName) : deCapitalizeFirst(baseName)));
                }
                else {
                    final String attName  = attribute instanceof ViewAttribute ? ((ViewAttribute) attribute).getBaseAttributeModelField().getName()
                                                                               : attribute.getName();
                    final String baseName = ((ViewAttribute) value).getBaseAttributeModelField().getName();
                    final String name     = prefix + (!prefix.isEmpty() ? capitalizeFirst(baseName) : baseName) + capitalizeFirst(attName);

                    fields.add(name);
                    fieldMapping.put(name,
                        prefix + (!prefix.isEmpty() ? capitalizeFirst(value.getName()) : value.getName()) + capitalizeFirst(attribute.getName()));
                }
            }
        }
        else {
            final String name = value instanceof ViewAttribute ? ((ViewAttribute) value).getBaseAttribute().getName() : value.getName();
            fieldMapping.put(name, value.getName());
            fields.add(name);
        }
    }  // end method collectFields

    private HttpInvoker createInvoker(String[] urls, long readTimeout) {
        if (urls.length == 0) throw new IllegalArgumentException("Missing Schema Properties Remote Url");
        final HttpInvoker result = HttpInvokers.invoker(Strategy.RANDOM, urls).withMetrics(SYNC_VIEW_KEY_GENERATOR).withGzipDecompression();
        return result.withConnectTimeout(server_timeout).withReadTimeout((int) readTimeout);
    }

    private Tuple<DateTime, String> doRefresh(String toBeRemoved, String server, Map<String, String> syncData) {
        return invokeInTransaction(t -> {
            try {
                t.beginBatch();

                ElasticSearchIndexer.refreshingView(true);
                if (isNotEmpty(toBeRemoved)) {
                    viewTable.deleteKeys(ImmutableList.fromArray(toBeRemoved.split(",")).map(metadata::keyFromString));
                    removedKeys = toBeRemoved;
                }

                final HashMap<String, Map<String, String>> resources = insertResources(server, syncData);

                DateTime last    = DateTime.EPOCH;
                String   lastKey = "";
                for (final Map.Entry<String, String> entry : syncData.entrySet()) {
                    final String   key      = entry.getKey();
                    final T        instance = metadata.createInstance(metadata.keyFromString(key));
                    final DateTime dateTime = persistEntry(instance, entry.getValue(), resources.get(key));
                    if (dateTime.isGreaterOrEqualTo(last)) {
                        last    = dateTime;
                        lastKey = instance.keyAsString();
                    }
                }
                return tuple(last, lastKey);
            }
            finally {
                ElasticSearchIndexer.refreshingView(false);
            }
        });
    }  // end method doRefresh

    @NotNull private Tuple<DateTime, String> findLastItem() {
        if (force) return tuple(DateTime.EPOCH, "");
        try {
            final Tuple<DateTime, String> maxUpdateTime = getMaxUpdateTimeKey();

            final DateTime lastRemoved = EntityTable.lastDeletedTime(view.getFullName());
            if (lastRemoved != null && lastRemoved.compareTo(maxUpdateTime.first()) > 0) return Tuple.tuple(lastRemoved, "");
            return maxUpdateTime;
        }
        catch (final IllegalArgumentException ignore) {
            force = true;
            return tuple(DateTime.EPOCH, "");
        }
    }

    @NotNull private HashMap<String, Map<String, String>> insertResources(String server, Map<String, String> syncData) {
        final HashMap<String, Map<String, String>> resources = new HashMap<>();
        if (viewTable.getFields().exists(tableField -> tableField instanceof TableField.Res)) {
            try(final SqlStatement.Prepared stmt = db.sqlStatement(
                                                         "insert into Schema(SG)." + INDEX_TABLE + "(" + UUID + "," + VARIANT + "," + EXTERNAL + "," +
                                                         NAME + "," + URL + "," + INFO + ") values (?,?,?,?,?,?)").prepare())
            {
                boolean resourcesAdded = false;
                for (final Map.Entry<String, String> entry : syncData.entrySet()) {
                    final Map<String, String> map = uploadResources(server, entry.getValue(), stmt);

                    resources.put(entry.getKey(), map);
                    resourcesAdded = resourcesAdded || !map.isEmpty();
                }
                if (resourcesAdded) stmt.executeBatch();
            }
        }
        return resources;
    }

    private DateTime persistEntry(T instance, String values, Map<String, String> resources) {
        DateTime result = DateTime.EPOCH;
        for (final String field : values.split(FIELD_SEPARATOR)) {
            final Tuple<String, String> t         = splitToTuple(field, "=");
            final String                fieldName = fieldMapping.get(t.first().trim());
            final String                v         = t.second();

            if (fieldName.equals(UPDATE_TIME)) result = DateTime.fromString(v);
            metadata.getField(fieldName).ifPresent(tf ->
                    tf.setValue(instance, NULL_TO_STRING.equals(v) ? null : tf instanceof TableField.Res ? resources.get(fieldName) : v));
        }

        viewTable.merge(instance);
        return result;
    }

    @SuppressWarnings("MethodWithMultipleReturnPoints")
    private RefreshResult refresh(@NotNull DateTime from, @Nullable String keyFrom, @Nullable Integer batchSize, boolean ignoreDeletions,
                                  boolean initial) {
        final Map<String, String> syncData = getRemoteSyncData(from, keyFrom, batchSize, initial);
        if (syncData == null) return RefreshResult.noData();

        final DateTime dateTime = DateTime.fromString(syncData.get(LAST_DELETED));
        lastDeleted = dateTime;
        syncData.remove(LAST_DELETED);
        final boolean more = cast(syncData.get(MORE));
        syncData.remove(MORE);
        final String server = syncData.get(SERVER);
        syncData.remove(SERVER);
        final String toBeRemoved = syncData.get(DELETED_INSTANCES);
        syncData.remove(DELETED_INSTANCES);

        if ("ALL".equals(toBeRemoved) && !ignoreDeletions) return invokeInTransaction(() -> {
            db.sqlStatement(DELETE_ALL, view.getSchema(), view.getTableName().getName()).execute();force = true;      // will rebuild
            final RefreshResult refresh = refresh(DateTime.EPOCH, null, batchSize, true, false);lastDeleted = dateTime;return refresh;});

        final Tuple<DateTime, String> last;
        try {
            last = doRefresh(toBeRemoved, server, syncData);
        }
        catch (final UncheckedIOException e) {
            final String msg = ERROR_REFRESHING_VIEW + view.getFullName();
            logger.error(msg);
            logger.error(e);
            return RefreshResult.error(e.getMessage());
        }
        if (more) return refresh(last.first(), last.second(), batchSize, ignoreDeletions, false);
        return syncData.isEmpty() && isEmpty(toBeRemoved) ? RefreshResult.noData() : RefreshResult.synced();
    }  // end method refresh

    private String uploadResource(final String remoteUrl, String value, SqlStatement.Prepared stmt) {
        final Resource       resource     = Files.asUnchecked(() -> mapper.readValue(value, SimpleResourceImpl.class));
        final Resource.Entry masterOrigin = resource.getMaster();
        final String         uuid         = randomUUID().toString();

        stmt.onArgs(uuid,
                MASTER,
                true,
                masterOrigin.getName(),
                masterOrigin.isExternal() ? masterOrigin.getUrl() : remoteUrl + RESOURCE_SHA_PATH + masterOrigin.getSha(),
                getMetadataClob(masterOrigin)).batch();

        for (final Resource.Entry e : resource.getEntries()) {
            if (!MASTER.equals(e.getVariant()))
                stmt.onArgs(uuid,
                        e.getVariant(),
                        true,
                        e.getName(),
                        e.isExternal() ? e.getUrl() : remoteUrl + RESOURCE_SHA_PATH + e.getSha(),
                        getMetadataClob(e)).batch();
        }
        return uuid;
    }

    private Map<String, String> uploadResources(String server, String values, SqlStatement.Prepared stmt) {
        final HashMap<String, String> map = new HashMap<>();

        for (final String field : values.split(FIELD_SEPARATOR)) {
            final Tuple<String, String> t = splitToTuple(field, "=");

            final String v = t.second();

            if (!NULL_TO_STRING.equals(v)) {
                final String fieldName = fieldMapping.get(t.first().trim());
                metadata.getField(fieldName).castTo(TableField.Res.class).ifPresent(tf -> map.put(fieldName, uploadResource(server, v, stmt)));
            }
        }
        return map;
    }

    private HttpInvoker getInvoker(String schema) {
        HttpInvoker invoker;
        synchronized (lockObject) {
            invoker = invokers.get(schema);
            if (invoker == null) {
                final SchemaProps props = Context.getProperties(schema, SchemaProps.class);
                invoker = createInvoker(props.remoteUrl.split(";"), props.remoteTimeout * Times.MILLIS_SECOND);
                invokers.put(schema, invoker);
            }
        }
        return invoker;
    }

    /** Get the maximum update time and key value. */
    private Tuple<DateTime, String> getMaxUpdateTimeKey() {
        final TableField.DTime updateTimeField = metadata.getUpdateTimeField();

        if (updateTimeField != null) {
            //J-
            final ImmutableList<TableField<?>> pk = metadata.getPrimaryKey();
            final Expr<?>[] kfs                   = pk.toArray(Expr<?>[]::new);
            final OrderSpec<?>[] orderBy          = pk.prepend(updateTimeField).map(Expr::descending).toArray(OrderSpec<?>[]::new);

            final QueryTuple qt = select(updateTimeField, kfs)
                    .from(dbTable)
                    .orderBy(orderBy)
                    .get();
            if (qt != null) return tuple(qt.getOrFail(updateTimeField),
                                         metadata.hasGeneratedKey() ? qt.getOrFail(2).toString()
                                                                    : metadata.createInstance(tupleFromList(fromArray(qt.toArray()).drop(1)))
                                                                              .keyAsString());
            //J+
        }
        return tuple(DateTime.EPOCH, "");
    }

    private Lob getMetadataClob(Resource.Entry masterOrigin) {
        return createClob(clobToString(masterOrigin.getMetadata()));
    }

    @Nullable private Map<String, String> getRemoteSyncData(DateTime from, @Nullable String key, @Nullable Integer batchSize, boolean initial) {
        final MultiMap<String, String> parameters = MultiMap.createMultiMap();
        parameters.putAll(FIELD, fields);
        parameters.put(SYNC_FROM, from.toString());
        parameters.put(KEY_FROM, notEmpty(key, ""));
        parameters.put(INITIAL, String.valueOf(initial));
        if (batchSize != null) parameters.put(BATCH_SIZE, String.valueOf(batchSize));

        final HttpInvoker invoker = getInvoker(baseEntity.getSchema());

        final PathResource<?>                        resource = invoker.resource("/sg/sync/" + baseEntity.getFullName()).params(parameters);
        final HttpInvokerResult<Map<String, String>> result   = resource.invoke(Method.GET, MAP_TYPE).execute();

        if (!result.getStatus().isSuccessful()) return null;

        final Map<String, String> syncData = result.get();
        syncData.put(SERVER, result.getInvoker().toString());
        return syncData;
    }

    //~ Methods ......................................................................................................................................

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper m      = JsonMapping.json();
        final SimpleModule module = new SimpleModule("JSonHandler", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(SimpleResourceImpl.class, new ResourcesDeserializer());
        m.registerModule(module);
        return m;
    }

    //~ Static Fields ................................................................................................................................

    private static final HashMap<String, HttpInvoker> invokers = new HashMap<>();

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String RESOURCE_SHA_PATH = "/sg/resource?sha=";

    private static final String ERROR_REFRESHING_VIEW = "Error refreshing view: ";

    @NonNls
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String         FIELD = "field";

    @NonNls private static final String SYNC_FROM  = "sync_from";
    @NonNls private static final String KEY_FROM   = "key_from";
    @NonNls private static final String INITIAL    = "initial";
    @NonNls private static final String BATCH_SIZE = "batch_size";

    private static final Object lockObject = new Object();

    public static final String FIELD_SEPARATOR = "\u001E";

    private static final Logger logger = Logger.getLogger(ViewRefresher.class);

    private static final String DELETE_ALL = "delete from TableName(%s, %s)";

    public static final String LAST_DELETED      = "__lastdeleted";
    public static final String DELETED_INSTANCES = "__deleted_instances";

    public static final String SERVER = "__server";

    public static final String MORE = "__more";

    private static final GenericType<Map<String, String>> MAP_TYPE = new GenericType<Map<String, String>>() {};

    private static final int server_timeout = (int) (5 * Times.MILLIS_SECOND);

    private static final ObjectMapper mapper = createObjectMapper();

    private static final InvocationKeyGenerator SYNC_VIEW_KEY_GENERATOR = (server, path, method) -> "ViewRefresher";

    //~ Inner Classes ................................................................................................................................

    private static class RefreshResult {
        private final Status  status;
        private final boolean syncedData;

        private RefreshResult(Status s, boolean synced) {
            status     = s;
            syncedData = synced;
        }

        boolean ok() {
            return status.isSuccess();
        }

        Status status() {
            return status;
        }

        boolean syncedData() {
            return syncedData;
        }

        static RefreshResult error(String msg) {
            return new RefreshResult(Status.error(msg), false);
        }

        static RefreshResult noData() {
            return new RefreshResult(Status.ok(), false);
        }
        static RefreshResult synced() {
            return new RefreshResult(Status.ok(), true);
        }
    }

    private static class ResourcesDeserializer extends JsonDeserializer<SimpleResourceImpl> {
        @Override public SimpleResourceImpl deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException
        {
            String                      uuid    = null;
            Map<String, Resource.Entry> entries = null;
            JsonToken                   token;
            while ((token = jp.nextToken()) != null && token != END_OBJECT) {
                final String fieldName = jp.getCurrentName();
                jp.nextToken();
                switch (fieldName) {
                case "uuid":
                    uuid = jp.getText();
                    break;
                case "entries":
                    entries = readEntries(jp);
                    break;
                default:
                    jp.nextToken();
                }
            }
            return new SimpleResourceImpl(uuid, entries);
        }

        private Map<String, Resource.Entry> readEntries(JsonParser jp)
            throws IOException
        {
            final HashMap<String, Resource.Entry> result = new HashMap<>();
            if (jp.getCurrentToken() == START_ARRAY) {
                jp.nextToken();
                while (jp.getCurrentToken() != END_ARRAY) {
                    final Resource.Entry entry = readEntry(jp);
                    result.put(entry.getVariant(), entry);
                    jp.nextToken();
                }
            }
            return result;
        }

        @SuppressWarnings("DuplicateStringLiteralInspection")
        private Resource.Entry readEntry(JsonParser jp)
            throws IOException
        {
            boolean   external = false;
            String    name     = null;
            String    url      = null;
            String    variant  = null;
            String    mimeType = null;
            Metadata  md       = Metadata.empty();
            JsonToken token;
            while ((token = jp.nextToken()) != null && token != END_OBJECT) {
                final String fieldName = jp.getCurrentName();
                jp.nextToken();
                switch (fieldName) {
                case "external":
                    external = jp.getBooleanValue();
                    break;
                case "name":
                    name = jp.getText();
                    break;
                case "url":
                    url = jp.getText();
                    break;
                case "variant":
                    variant = jp.getText();
                    break;
                case "mimeType":
                    mimeType = jp.getText();
                    break;
                case "metadata":
                    md = JsonMapping.shared().readValue(jp, Metadata.class);
                    break;
                }
            }
            assert variant != null;
            assert name != null;
            assert url != null;
            assert mimeType != null;
            return new EntryImpl(variant, external, name, url, mimeType, md);
        }
    }  // end class ResourcesDeserializer
}  // end class ViewRefresher
