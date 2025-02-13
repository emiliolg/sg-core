
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.RemoteCluster;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.exception.InvokerConnectionException;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Action;
import tekgenesis.form.Download;
import tekgenesis.form.FormTable;
import tekgenesis.form.configuration.ChartConfiguration;
import tekgenesis.sg.ClusterConf;
import tekgenesis.task.jmx.JmxConstants;
import tekgenesis.type.permission.PredefinedPermission;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.media.Mime.APPLICATION_OCTET_STREAM;

/**
 * User class for Form: CacheForm
 */
public class CacheForm extends CacheFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action dumpCache() {
        final String cacheName = getVCacheName();

        final Action result   = actions.getDefault();
        final String fileName = String.format(ConsoleConstants.CACHE_DUMP_FILE_FORMAT, DateTime.current().toString(), cacheName, getSelectedNode());
        result.withDownload(CacheCSVDump.class).withFileName(fileName).withContentType(APPLICATION_OCTET_STREAM);
        return result;
    }

    @NotNull @Override public Action filterEntities() {
        fillEntityTable(getSearchEntityBox());
        return actions.getDefault();
    }

    @NotNull @Override public Action nodeSelected(@NotNull Field field) {
        final EntityChartRow current    = getEntityChart().getCurrent();
        final String         serverNode = current.getServerNode();
        setSelectedNode(serverNode);

        return actions().getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void onLoad() {
        // noinspection MagicNumber
        this.<ChartConfiguration>configuration(Field.ENTITY_CHART).dimension(400, 400).positiveQuadrant();
        fillEntityTable(null);
        final ClusterConf selected = Context.getSingleton(Clusters.class).getSelectedClusterConf();
        if (selected != null) setClusterName(selected.getName());
        setDisable(!forms.hasPermission(PredefinedPermission.UPDATE));
    }

    @NotNull @Override public Action selectEntity() {
        final FormTable<EntityChartRow> entityChart = getEntityChart();
        setSelectedNode("");

        entityChart.clear();
        final CacheTabRow current   = getCacheTab().getCurrent();
        final String      cacheName = current.getCacheName();
        setVCacheName(cacheName);
        final Option<RemoteCluster> activeCluster = Context.getSingleton(Clusters.class).getActiveCluster();

        final RemoteCluster      cluster       = activeCluster.get();
        final List<RemoteMember> remoteMembers = cluster.getMembers();

        for (final RemoteMember remoteMember : remoteMembers) {
            final EntityChartRow add = entityChart.add();
            add.setCacheEntries(getCacheEntriesCount(remoteMember, cacheName, current.getCacheMode()));
            add.setServerNode(remoteMember.getName());
        }

        return actions.getDefault();
    }

    private void fillEntityTable(@Nullable final String filter) {
        final FormTable<CacheTabRow> entitiesTab = getCacheTab();
        entitiesTab.clear();

        final Option<RemoteCluster> activeCluster = Context.getSingleton(Clusters.class).getActiveCluster();

        if (activeCluster.isPresent()) {
            final RemoteCluster clusterWrapper = activeCluster.get();
            final RemoteMember  remoteMember   = clusterWrapper.getMembers().get(0);

            final CacheType             searchCacheType = getSearchCacheType();
            final String                op              = searchCacheType == CacheType.USER_CACHE ? "UserCaches" : "EntityCaches";
            List<Tuple<String, String>> caches          = JmxInvokerImpl.invoker(remoteMember.getJmxEndpoint())
                                                          .mbean(JmxConstants.CACHES)
                                                          .getAttribute(op);

            if (isNotEmpty(filter)) caches = Colls.filter(caches, s -> s != null && s.first().contains(filter)).toList();

            for (final Tuple<String, String> c : caches) {
                final CacheTabRow row = entitiesTab.add();
                row.setCacheName(c.first());
                row.setCacheMode(c.second());
                row.setCacheType(Strings.toWords(c.second()));
            }
        }
    }  // end method fillEntityTable

    private int getCacheEntriesCount(RemoteMember remoteMember, String cacheName, String cacheMode) {
        try {
            final String objName = String.format("org.infinispan:type=Cache,name=\"%s(%s)\",manager=\"DefaultCacheManager\",component=Statistics",
                    cacheName,
                    cacheMode.toLowerCase());

            return JmxInvokerImpl.invoker(remoteMember.getJmxEndpoint()).mbean(objName).getAttribute("numberOfEntries");
        }
        catch (final InvokerConnectionException e) {
            logger.error(e);
            return 0;
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(CacheForm.class);

    //~ Inner Classes ................................................................................................................................

    class CacheCSVDump implements Download.DownloadWriter {
        @Override public void into(@NotNull OutputStream stream)
            throws IOException
        {
            final OutputStreamWriter     writer         = new OutputStreamWriter(stream);
            final Optional<RemoteMember> memberOptional = Context.getSingleton(Clusters.class).findMember(getSelectedNode());

            if (memberOptional.isPresent()) {
                final RemoteMember                remoteMember = memberOptional.get();
                final List<Tuple<String, String>> tuples       = remoteMember.dumpCacheContent(getVCacheName());

                tuples.forEach((t) -> {
                    try {
                        writer.write(t.first());
                        writer.write(",");
                        writer.write(t.second());
                        writer.write("\n");
                    }
                    catch (final IOException e) {
                        logger().warning("Unable to write Cache dump file.");
                    }
                });
            }

            writer.flush();
        }
    }

    public class CacheTabRow extends CacheTabRowBase {
        @NotNull @Override public Action clearCache() {
            final Option<RemoteCluster> activeCluster = Context.getSingleton(Clusters.class).getActiveCluster();
            final RemoteCluster         cluster       = activeCluster.get();

            final RemoteMember remoteMember = cluster.getMembers().get(0);
            final CacheType    cacheType    = getSearchCacheType();
            final String       objectName   = cacheType == CacheType.USER_CACHE ? JmxConstants.CACHES : JmxConstants.ENTITIES;

            JmxInvokerImpl.invoker(remoteMember.getJmxEndpoint())
                .mbean(objectName)
                .invoke(ConsoleConstants.CLEAR_CACHE, new String[] { String.class.getName() }, new Object[] { getCacheName() });

            return actions.getDefault();
        }
    }

    public class EntityChartRow extends EntityChartRowBase {}
}  // end class CacheForm
