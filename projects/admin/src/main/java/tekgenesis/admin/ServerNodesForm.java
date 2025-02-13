
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import javax.management.MBeanInfo;

import org.jetbrains.annotations.NotNull;
import org.jgroups.Address;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.jmx.util.ClusterInfo;
import tekgenesis.cluster.jmx.util.MemberInfo;
import tekgenesis.cluster.jmx.util.MemoryInfo;
import tekgenesis.cluster.jmx.util.MemoryUsageInfo;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.invoker.exception.InvokerConnectionException;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.common.jmx.JmxUtil;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Action;
import tekgenesis.form.Download;
import tekgenesis.form.FormTable;
import tekgenesis.sg.g.TaskEntryTable;
import tekgenesis.task.TaskStatus;
import tekgenesis.task.jmx.JmxConstants;
import tekgenesis.type.permission.PredefinedPermission;

import static tekgenesis.admin.Message.*;
import static tekgenesis.cluster.jmx.util.Format.toSizeString;
import static tekgenesis.cluster.jmx.util.MemoryUtils.calculateMemoryUsage;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Strings.setterName;
import static tekgenesis.common.env.context.Context.getContext;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.common.media.Mime.APPLICATION_OCTET_STREAM;
import static tekgenesis.common.util.Reflection.invoke;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 * User class for Form: ServerNodesForm
 */
public class ServerNodesForm extends ServerNodesFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action closeDialog() {
        setRunningTask("");
        setConfirmRestartCluster(false);
        return actions.getDefault();
    }

    @NotNull @Override public Action closeResetNodeDialog() {
        setRunningTaskOnNode("");
        setConfirmRestartNode(false);
        return actions.getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void loadNodesInfo() {
        final FormTable<NodesTabRow> nodesTab = getNodesTab();
        nodesTab.clear();
        final ClusterInfo cluster = ClusterInfo.getInstance().refresh();
        for (final MemberInfo memberInfo : cluster.getMembers())
            createCell(nodesTab.add(), memberInfo);

        final File command = new File(System.getProperty(Constants.BIN_DIR) + File.separator + UPDATE_VERSION_SH);
        setUpdateAvailable(command.exists());
        setDisable(!forms.hasPermission(PredefinedPermission.UPDATE));
    }

    @NotNull @Override public Action openRestartDialog() {
        final long size = selectFrom(TaskEntryTable.TASK_ENTRY).where(TaskEntryTable.TASK_ENTRY.STATUS.eq(TaskStatus.RUNNING)).count();
        if (size > 0) setRunningTask(Message.TASKS_RUNNING.label(size));
        setConfirmRestartCluster(true);
        return actions.getDefault();
    }

    @NotNull @Override public Action openVersionDialog() {
        return actions.navigate(forms.initialize(UpdateVersionForm.class));
    }

    @NotNull @Override public Action refreshForm() {
        loadNodesInfo();
        return actions.getDefault();
    }

    @NotNull @Override public Action restartCluster() {
        final ClusterManager<Address> clusterManager = cast(getContext().getSingleton(ClusterManager.class));
        final Address                 currentMember  = clusterManager.getMember();

        clusterManager.getMembersAddresses().filter(member -> !currentMember.equals(member)).forEach(this::resetNode);
        try {
            // Wait 1 sec before restart me
            Thread.sleep(1_000);
        }
        catch (final InterruptedException e) {
            // ignore
        }
        resetNode(currentMember);
        loadNodesInfo();
        setConfirmRestartCluster(false);
        return actions.getDefault().withMessage(NODES_RESTARTED.label());
    }

    /** Invoked when button(restart) is clicked. */
    @NotNull @Override public Action restartNode() {
        final int                     currentIndex   = getNodeIx();
        final ClusterManager<Address> clusterManager = cast(getContext().getSingleton(ClusterManager.class));

        final List<Address> members = clusterManager.getMembersAddresses();
        final Address       address = members.get(currentIndex);

        final Action action = resetNode(address);
        getNodesTab().removeCurrent();
        return action;
    }

    private boolean canRestart(JmxEndpoint jmxConfiguration) {
        try {
            final MBeanInfo mBean = JmxInvokerImpl.invoker(jmxConfiguration).mbean(JmxConstants.JETTY_NODE).getInfo();
            return mBean != null;
        }
        catch (final InvokerConnectionException e) {
            return false;
        }
    }

    private void createCell(@NotNull NodesTabRow cell, @NotNull MemberInfo member) {
        cell.setNodeName(member.getName());

        final boolean available = member.isJmxEnabled();
        cell.setAvailable(available);

        final JmxEndpoint jmxConfiguration = member.getJmxEndpoint();
        if (jmxConfiguration.isAvailable()) {
            final boolean isRestartable = canRestart(jmxConfiguration);
            if (!isCanRestartCluster()) setCanRestartCluster(isRestartable);
            cell.setIsRestartable(isRestartable);

            if (available) {
                cell.setNodeImg(IMG_NODE_PNG);

                cell.setMasterStyle(member.isMaster());
                final Long uptime = member.getUptime();

                final List<String> buffer = immutable(member.getComponentsVersion())                     //
                                            .map(info -> info.getComponent() + " " + info.getBranch() + "-#" + info.getBuild())  //
                                            .toList();

                cell.setComponentsOptions(buffer);

                cell.setUptime(getTime(uptime));

                final MemoryUsageInfo memoryUsage = member.getMemoryUsage();

                final Map<String, MemoryInfo> details = memoryUsage.getDetails();
                for (final String memoryModelName : details.keySet()) {
                    final MemoryInfo memoryInfo = memoryUsage.get(memoryModelName);
                    final String     setterName = setterName(memoryModelName.replaceAll("\\s", ""));

                    final double mu = memoryInfo.getMax() > 0 ? calculateMemoryUsage(memoryInfo.getMax(), memoryInfo.getUsed()) : 0;
                    invoke(cell, setterName, mu);
                    invoke(cell, setterName + "Tt", getMemorySummary(memoryInfo));
                }
            }
            else cell.setNodeImg(IMG_NODE_FAILED_PNG);
        }
        else {
            cell.setNodeImg(IMG_NODE_FAILED_PNG);
            cell.setAvailable(false);
        }
    }  // end method createCell

    private Action resetNode(@NotNull Address address) {
        final JmxEndpoint endpoint = ClusterInfo.getInstance().getJmxConfiguration(address);

        try {
            JmxInvokerImpl.invoker(endpoint).mbean(JmxConstants.JETTY_NODE).invoke("stop", null, null);
            return actions.getDefault();
        }
        catch (final InvokerConnectionException e) {
            logger.error(e);
            return actions.getError().withMessage(SHUTDOWN_FALED.label());
        }
    }

    private String getMemorySummary(MemoryInfo edenSpace) {
        // noinspection DuplicateStringLiteralInspection
        return String.format("Committed :%s <br> Init : %s <br> Used : %s<br> Max : %s",
            toSizeString(edenSpace.getCommitted()),
            toSizeString(edenSpace.getInit()),
            toSizeString(edenSpace.getUsed()),
            toSizeString(edenSpace.getMax()));
    }

    @SuppressWarnings({ "MagicNumber", "Duplicates" })
    private String getTime(@NotNull Long uptime) {
        final long[] times = new long[4];
        long         tmp   = uptime / 1000;
        times[0] = tmp % 60;
        tmp      /= 60;
        times[1] = tmp % 60;
        tmp      /= 60;
        times[2] = tmp % 24;
        times[3] = tmp / 24;

        return String.format("%d Days, %d hour, %d min, %d sec", times[3], times[2], times[1], times[0]);
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String UPDATE_VERSION_SH = "updateVersion.sh";

    private static final Logger logger = getLogger(ServerNodesForm.class);

    private static final String IMG_NODE_PNG        = "/img/node.png";
    private static final String IMG_NODE_FAILED_PNG = "/img/nodeFailed.png";

    //~ Inner Classes ................................................................................................................................

    public class NodesTabRow extends NodesTabRowBase {
        @NotNull @Override public Action openRestartNodeDialog() {
            final String nodeName = getNodeName();

            final long runningTasks = selectFrom(TaskEntryTable.TASK_ENTRY).where(TaskEntryTable.TASK_ENTRY.MEMBER.eq(nodeName)).count();

            if (runningTasks > 0) setRunningTaskOnNode(Message.TASKS_RUNNING.label(runningTasks));
            final int currentIndex = getNodesTab().indexOf(this);
            setNodeIx(currentIndex);
            setNodeNameLabel(nodeName);
            setConfirmRestartNode(true);
            return actions.getDefault();
        }

        /** Invoked when label(threadDump) is clicked. */
        @NotNull @Override public Action threadDump() {
            setSelectedServerNode(getNodesTab().indexOf(this));
            final Action result = actions.getDefault();
            // noinspection DuplicateStringLiteralInspection
            final String fileName = String.format("%s-%s_dump.txt", DateTime.current().toString(), getNodeName());
            result.withDownload(ThreadDump.class).withFileName(fileName).withContentType(APPLICATION_OCTET_STREAM);
            return result;
        }
    }

    public class ThreadDump implements Download.DownloadWriter {
        @Override public void into(@NotNull OutputStream stream)
            throws IOException
        {
            final ClusterManager<Address> clusterManager = cast(getContext().getSingleton(ClusterManager.class));

            final List<Address> members          = clusterManager.getMembersAddresses();
            final Address       address          = members.get(getSelectedServerNode());
            final JmxEndpoint   jmxConfiguration = ClusterInfo.getInstance().getJmxConfiguration(address);
            if (jmxConfiguration != null) {
                final OutputStreamWriter writer = new OutputStreamWriter(stream);
                JmxUtil.generateThreadDump(jmxConfiguration, writer);
                writer.flush();
            }
            else logger.warning(SERVER_UNAVAILABLE.label());
        }
    }
}  // end class ServerNodesForm
