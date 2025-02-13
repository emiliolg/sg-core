
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
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.Metric;
import tekgenesis.cluster.jmx.RemoteCluster;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.cluster.jmx.util.Format;
import tekgenesis.cluster.jmx.util.MemoryInfo;
import tekgenesis.cluster.jmx.util.MemoryUsageInfo;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.exception.InvokerConnectionException;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxException;
import tekgenesis.common.jmx.JmxUtil;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Action;
import tekgenesis.form.Download;
import tekgenesis.form.FormTable;
import tekgenesis.sg.ClusterConf;
import tekgenesis.type.permission.PredefinedPermission;

import static java.lang.String.format;

import static tekgenesis.cluster.jmx.util.Format.toSizeString;
import static tekgenesis.cluster.jmx.util.MemoryUtils.calculateMemoryUsage;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Strings.setterName;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.common.media.Mime.APPLICATION_OCTET_STREAM;
import static tekgenesis.common.util.Reflection.findMethod;
import static tekgenesis.common.util.Reflection.invoke;
import static tekgenesis.console.Messages.SHUTDOWN_FAILED;

/**
 * User class for Form: ClusterDashBoard
 */
public class ClusterDashBoard extends ClusterDashBoardBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action closeResetNodeDialog() {
        setRunningTaskOnNode("");
        setConfirmRestartNode(false);
        return actions.getDefault();
    }

    /**  */
    public void configure() {
        setDisable(!forms.hasPermission(PredefinedPermission.UPDATE));

        final ClusterConf selected = Context.getSingleton(Clusters.class).getSelectedClusterConf();
        if (selected != null) setClusterName(selected.getName());

        refresh();
    }  // end method configure

    /**  */
    @NotNull public Action refresh() {
        final Option<RemoteCluster> activeCluster = Context.getSingleton(Clusters.class).getActiveCluster();
        if (activeCluster.isPresent()) {
            final RemoteCluster cluster = activeCluster.get();
            refreshSummaryTable(cluster);
            return actions.getDefault();
        }
        return actions.getError().withMessage("There is no active cluster.");
    }  // end method refresh

    @NotNull @Override
    @SuppressWarnings("Duplicates")
    public Action restartNode() {
        Action ret;

        try {
            final RemoteMember remoteMember = Context.getSingleton(Clusters.class).getMemberAt(getNodeIx());
            remoteMember.restart();
            ret = actions.getDefault();
        }
        catch (final InvokerConnectionException e) {
            logger.warning(e);
            ret = actions.getError().withMessage(SHUTDOWN_FAILED.label());
        }
        setConfirmRestartNode(false);
        refresh();
        return ret;
    }

    private void refreshSummaryTable(RemoteCluster cluster) {
        final FormTable<NodeSummaryRow> nodeSummary = getNodeSummary();
        nodeSummary.clear();

        try {
            final List<RemoteMember> remoteMembers = cluster.getMembers();

            for (final RemoteMember remoteMember : remoteMembers) {
                final NodeSummaryRow row = nodeSummary.add();
                row.set(remoteMember);
            }
        }
        catch (final JmxException e) {
            logger.warning(e);
        }
    }

    //~ Methods ......................................................................................................................................

    private static String memorySummary(MemoryInfo edenSpace) {
        return format(ConsoleConstants.MEMORY_SUMMARY,
            toSizeString(edenSpace.getCommitted()),
            toSizeString(edenSpace.getInit()),
            toSizeString(edenSpace.getUsed()),
            toSizeString(edenSpace.getMax()));
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(ClusterDashBoard.class);

    //~ Inner Classes ................................................................................................................................

    public class NodeSummaryRow extends NodeSummaryRowBase {
        /**  */
        @NotNull public Action generateThreadDump() {
            setSelectedServerNode(getNodeSummary().indexOf(this));
            final Action result = actions.getDefault();

            final String fileName = String.format(ConsoleConstants.THREAD_DUMP_FILE_FORMAT, DateTime.current().toString(), getNodeName());
            result.withDownload(ThreadDump.class).withFileName(fileName).withContentType(APPLICATION_OCTET_STREAM);
            return result;
        }

        /**  */
        @NotNull public Action openRestartNodeDialog() {
            final String nodeName     = getNodeSummary().getCurrent().getNodeName();
            final int    currentIndex = getNodeSummary().indexOf(this);
            setNodeIx(currentIndex);
            setNodeNameLabel(nodeName);
            setConfirmRestartNode(true);

            return actions.getDefault();
        }

        /**  */
        public void set(RemoteMember remoteMember) {
            setUptime(Format.timeAsString(remoteMember.getUptime()));
            setNodeName(remoteMember.getName());

            setAvailable(true);

            setComponents(remoteMember.getComponentVersionList());

            final Metric metrics = remoteMember.getMetrics();

            final Map<String, Object> values      = cast(metrics.getValue(ConsoleConstants.MEMORY_USAGE));
            final MemoryUsageInfo     memoryUsage = MemoryUsageInfo.from(values);

            final Map<String, MemoryInfo> details = memoryUsage.getDetails();
            for (final String memoryModelName : details.keySet()) {
                final MemoryInfo memoryInfo = memoryUsage.get(memoryModelName);
                final String     setterName = setterName(memoryModelName.replaceAll("\\s", ""));

                final double mu = memoryInfo.getMax() > 0 ? calculateMemoryUsage(memoryInfo.getMax(), memoryInfo.getUsed()) : 0;

                if (findMethod(getClass(), setterName, double.class).isPresent()) {
                    invoke(this, setterName, mu);
                    invoke(this, setterName + "Tt", memorySummary(memoryInfo));
                }
            }
        }
    }  // end class NodeSummaryRow

    public class ThreadDump implements Download.DownloadWriter {
        @Override public void into(@NotNull OutputStream stream)
            throws IOException
        {
            final RemoteMember remoteMember = Context.getSingleton(Clusters.class).getMemberAt(getSelectedServerNode());

            final JmxEndpoint conn = remoteMember.getJmxEndpoint();

            final OutputStreamWriter writer = new OutputStreamWriter(stream);
            JmxUtil.generateThreadDump(conn, writer);

            writer.flush();
        }
    }
}  // end class ClusterDashBoard
