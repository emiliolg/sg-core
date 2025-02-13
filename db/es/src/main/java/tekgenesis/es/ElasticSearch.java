
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.es;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.searchbox.action.BulkableAction;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.Refresh;
import io.searchbox.indices.aliases.AddAliasMapping;
import io.searchbox.indices.aliases.GetAliases;
import io.searchbox.indices.aliases.ModifyAliases;
import io.searchbox.indices.aliases.RemoveAliasMapping;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.indices.mapping.PutMapping;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Strings;
import tekgenesis.common.logging.Logger;

import static java.lang.Math.min;
import static java.util.Arrays.asList;

import static org.elasticsearch.common.settings.Settings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.es.ESConstants.*;

/**
 * An Elastic search client.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class ElasticSearch implements Closeable {

    //~ Instance Fields ..............................................................................................................................

    private JestClient jestClient = null;
    private Listener   listener   = Listener.EMPTY;

    private Node node = null;

    //~ Constructors .................................................................................................................................

    /** Constructor for jest elastic search. */
    private ElasticSearch(JestClient jestClient) {
        this.jestClient = jestClient;
    }

    //~ Methods ......................................................................................................................................

    /** Check if index alias exists. */
    public boolean aliasExists(String indexName) {
        try {
            final JestResult execute = jestClient.execute(new IndicesExists.Builder(indexName).build());
            return execute.isSucceeded();
        }
        catch (final IOException e) {
            return false;
        }
    }

    /** New bulk. */
    public JestBulk bulk() {
        return new JestBulk();
    }

    /** Check if bulk has reached limit. */
    public boolean checkBulk(JestBulk bulk) {
        if (bulk.getActionsSize() >= MAX_BULK_SIZE) {
            execute(bulk);
            return true;
        }
        return false;
    }

    /** Clean elastic search. Delete everything. Use carefully young padawan. */
    public JestResult cleanIndexes() {
        try {
            return jestClient.execute(new DeleteIndex.Builder("_all").build());
        }
        catch (final IOException e) {
            return new JestResult(new Gson());
        }
    }

    @Override public void close() {
        if (jestClient != null) jestClient.shutdownClient();
        if (node != null) node.close();
    }

    /** Index specified elastic search mapping. */
    public void createIndex(@NotNull String indexName, @NotNull String type, @NotNull String mapping, String alias) {
        try {
            jestClient.execute(new CreateIndex.Builder(indexName).settings(mapping).build());
        }
        catch (final IOException e) {
            logger.warning("Couldn't create index " + indexName + " of type " + type, e);
        }
    }

    /** Delete the specified Document. */
    public void deleteDocument(JestBulk bulk, @NotNull String indexName, @NotNull String type, @NotNull String id) {
        bulk.add(new Delete.Builder(id).type(type).index(indexName).build());
    }

    /** Delete the specified index. */
    public JestResult deleteIndex(@NotNull String indexName) {
        try {
            return jestClient.execute(new DeleteIndex.Builder(indexName).build());
        }
        catch (final IOException e) {
            logger.warning("Couldn't delete index " + indexName, e);
            return EMPTY_FAILED_RESULT;
        }
    }

    /** Execute current bulk request. */
    @Nullable public BulkResult execute(JestBulk bulk) {
        if (bulk.getActionsSize() == 0) return null;
        boolean callAfter = true;
        try {
            listener.beforeBulk(bulk);
            final BulkResult response = jestClient.execute(bulk.bulkBuilder.refresh(true).build());
            callAfter = false;
            listener.afterBulk(bulk, response);
            bulk.reset();
            return response;
        }
        catch (final Throwable t) {
            logger.warning("Failed to execute bulk request.", t);
            if (callAfter) listener.afterBulk(bulk, t);
        }
        return null;
    }

    /** Get index names that refer to the given alias. */
    public Set<String> indicesFromAlias(@NotNull final String aliasName) {
        final HashSet<String> indices = new HashSet<>();
        try {
            final JsonObject result = jestClient.execute(new GetAliases.Builder().addIndex(aliasName).build()).getJsonObject();
            result.entrySet().forEach(entrySet -> indices.add(entrySet.getKey()));
        }
        catch (final IOException e) {
            logger.error("Error getting indices behind alias: " + aliasName, e);
        }

        return indices;
    }

    /** Index a document associated with a given index and entityName/type. */
    public void prepareIndex(JestBulk bulk, String indexName, String type, @Nullable String id, String source) {
        bulk.add(new Index.Builder(source).index(indexName).type(type).id(id).build());
    }

    /** Refresh indices. */
    public void refresh() {
        try {
            jestClient.execute(new Refresh.Builder().build());
        }
        catch (final IOException e) {
            logger.error(e);
        }
    }

    /** Search. */
    public SearchResult search(@NotNull String indexName, @NotNull QueryBuilder query, int limit) {
        try {
            return jestClient.execute(
                new Search.Builder(new SearchSourceBuilder().size(min(limit, MAX_RESULTS)).query(query).toString()).addIndex(indexName).build());
        }
        catch (final IOException e) {
            return new SearchResult(new Gson());
        }
    }

    /**
     * Returns switch alias request builder to have control to, for example, add more alias changes
     * to the atomic operation.
     */
    public JestResult switchIndexAlias(String alias, String oldIndex, String newIndex) {
        final ModifyAliases modify = new ModifyAliases.Builder(new RemoveAliasMapping.Builder(oldIndex, alias).build()).addAlias(
                    new AddAliasMapping.Builder(newIndex, alias).build())
                                     .addAlias(new RemoveAliasMapping.Builder(newIndex, alias + REBUILD).build())
                                     .build();
        try {
            return jestClient.execute(modify);
        }
        catch (final IOException e) {
            return EMPTY_FAILED_RESULT;
        }
    }

    /** Updates saved mapping for type. */
    public JestResult updateMapping(String indexName, String type, XContentBuilder mapping) {
        try {
            return jestClient.execute(new PutMapping.Builder(indexName, type, mapping.string()).build());
        }
        catch (final IOException e) {
            return EMPTY_FAILED_RESULT;
        }
    }

    /** Resets bulk processor and attach a custom listener. */
    public ElasticSearch withCustomBulkListener(@NotNull final Listener l) {
        listener = l;
        return this;
    }

    /** Check if entity is updating. */
    public boolean isRebuilding(String indexName) {
        return aliasExists(indexName + REBUILD);
    }

    /** Return the number of documents in the given index. */
    public double getIndexCount(String index) {
        try {
            return jestClient.execute(new Count.Builder().addIndex(index).build()).getCount();
        }
        catch (final IOException e) {
            logger.error("Error getting index count for index " + index, e);
            return 0;
        }
    }

    /** Get index mapping from given type or implode in the process. */
    @NotNull public JsonObject getMappingMetadata(String indexName, String type) {
        try {
            final JsonObject                          jsonObject = jestClient.execute(
                    new GetMapping.Builder().addIndex(indexName).addType(type).build())
                                                                   .getJsonObject();
            final Set<Map.Entry<String, JsonElement>> set        = jsonObject.entrySet();
            for (final Map.Entry<String, JsonElement> first : set)
                return first.getValue()
                       .getAsJsonObject()
                       .get(MAPPINGS)
                       .getAsJsonObject()
                       .get(type)
                       .getAsJsonObject()
                       .get(META_FIELD)
                       .getAsJsonObject();
        }
        catch (final Exception e) {
            logger.error("Error getting index metadata for " + type + " in index " + indexName, e);
        }
        return new JsonObject();
    }

    //~ Methods ......................................................................................................................................

    /** Create an embedded server. Used mainly for tests and development */
    public static ElasticSearch createEmbeddedServer(final File runDir, int port) {
        final Node          node   = nodeBuilder().settings(settings(runDir, port)).local(true).client(false).node();
        final ElasticSearch server = jestRemoteServer(Constants.HTTP_LOCALHOST + port);
        server.node = node;
        return server;
    }

    /** Makes string suitable for index naming (must be lower case and unique). */
    @NotNull public static String indexName(@NotNull String fqn) {
        return Strings.fromCamelCase(fqn).toLowerCase();
    }

    /** Create http connection to elastic search using jest. */
    public static ElasticSearch jestRemoteServer(@NotNull String indexUrl) {
        // Create connection
        final JestClientFactory factory = new JestClientFactory();

        factory.setHttpClientConfig(new HttpClientConfig.Builder(asList(indexUrl.split(";"))).multiThreaded(true).build());

        return new ElasticSearch(factory.getObject());
    }

    private static Settings.Builder settings(File runDir, int port) {
        return settingsBuilder().put("http.enabled", true)
               .put("path.home", runDir)
               .put("path.logs", runDir)
               .put("path.work", runDir)
               .put("path.data", runDir)
               .put("network.host", Constants.LOCALHOST)
               .put("http.port", port)
               .put("discovery.zen.ping.multicast.enabled", false)
               .put("action.auto_create_index", false)
               .put("index.mapper.dynamic", false);
    }

    //~ Static Fields ................................................................................................................................

    private static final JestResult EMPTY_FAILED_RESULT = new JestResult(new Gson());

    public static final Logger logger = Logger.getLogger(ElasticSearch.class);

    public static final int MAX_BULK_SIZE = 500;

    //~ Inner Interfaces .............................................................................................................................

    public interface Listener {
        Listener EMPTY = new Listener() {};

        /** Callback after a successful execution of bulk request. */
        default void afterBulk(JestBulk request, BulkResult response) {}
        /** Callback after a failed execution of bulk request. */
        default void afterBulk(JestBulk request, Throwable failure) {}
        /** Callback before the bulk is executed. */
        default void beforeBulk(JestBulk request) {}
    }

    //~ Inner Classes ................................................................................................................................

    public class JestBulk {
        private int          actionsSize;
        private Bulk.Builder bulkBuilder = new Bulk.Builder();

        /** Add. */
        public JestBulk add(BulkableAction<?> action) {
            bulkBuilder.addAction(action);
            actionsSize++;
            return this;
        }

        /** Execute. */
        public Option<BulkResult> execute() {
            return ofNullable(ElasticSearch.this.execute(this));
        }

        /** Reset. */
        public void reset() {
            bulkBuilder = new Bulk.Builder();
            actionsSize = 0;
        }

        /** Get actions size. */
        public int getActionsSize() {
            return actionsSize;
        }
    }
}  // end class ElasticSearch
