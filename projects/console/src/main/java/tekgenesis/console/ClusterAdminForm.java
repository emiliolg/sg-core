
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.RemoteCluster;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.exception.InvokerConnectionException;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.sg.ClusterConf;
import tekgenesis.type.permission.PredefinedPermission;

import static tekgenesis.cluster.jmx.util.Format.timeAsString;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.common.media.Mime.APPLICATION_OCTET_STREAM;
import static tekgenesis.console.Messages.SHUTDOWN_FAILED;
import static tekgenesis.console.Messages.TASKS_RUNNING;

/**
 * User class for Form: ClusterAdminForm
 */
public class ClusterAdminForm extends ClusterAdminFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action closeResetNodeDialog() {
        setRunningTaskOnNode("");
        setConfirmRestartNode(false);
        return actions.getDefault();
    }

    @NotNull @Override public Action closeRestartClusterDialog() {
        setRunningTask("");
        setConfirmRestartCluster(false);
        return actions.getDefault();
    }

    @Override public void init() {
        setDisable(!forms.hasPermission(PredefinedPermission.UPDATE));

        collectInfo();
    }

    @NotNull @Override public Action openRestartClusterDialog() {
        final RemoteCluster cluster = Context.getSingleton(Clusters.class).getActiveCluster().get();
        if (cluster.areRunningTasks()) setRunningTask(Messages.TASKS_RUNNING.label());
        setConfirmRestartCluster(true);
        return actions.getDefault();
    }

    @NotNull @Override public Action openUpdateDialog() {
        final UpdateClusterForm initialize = forms.initialize(UpdateClusterForm.class);
        return actions.navigate(initialize);
    }

    @NotNull @Override public Object populate() {
        return getClusterName();
    }

    @NotNull @Override public Action restartCluster() {
        try {
            Context.getSingleton(Clusters.class).getActiveCluster().get().restart();
            return actions.getDefault();
        }
        catch (final InvokerConnectionException e) {
            logger.warning(e);
            return actions.getError().withMessage(SHUTDOWN_FAILED.label());
        }
    }

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
        init();
        return ret;
    }

    private boolean collectInfo() {
        final ClusterConf selected = Context.getSingleton(Clusters.class).getSelectedClusterConf();
        if (selected != null) setClusterName(selected.getName());

        final RemoteCluster cluster = Context.getSingleton(Clusters.class).getActiveCluster().get();

        final List<RemoteMember> remoteMembers = cluster.getMembers();

        final FormTable<NodeSummaryRow> nodeSummary = getNodeSummary();
        nodeSummary.clear();
        for (final RemoteMember remoteMember : remoteMembers) {
            final NodeSummaryRow row = nodeSummary.add();
            row.set(remoteMember);
        }

        return false;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(ClusterAdminForm.class);

    //~ Inner Classes ................................................................................................................................

    public class NodeSummaryRow extends NodeSummaryRowBase {
        @NotNull @Override public Action configureServices() {
            return actions.detail(Services.class, getNodeName());
        }

        @NotNull @Override public Action generateThreadDump() {
            setSelectedServerNode(getNodeSummary().indexOf(this));
            final Action result = actions.getDefault();

            final String fileName = String.format(ConsoleConstants.THREAD_DUMP_FILE_FORMAT, DateTime.current().toString(), getNodeName());
            result.withDownload(ClusterDashBoard.ThreadDump.class).withFileName(fileName).withContentType(APPLICATION_OCTET_STREAM);
            return result;
        }
        @NotNull @Override public Action openRestartDialog() {
            final RemoteMember remoteMember = Context.getSingleton(Clusters.class).getMemberAt(getSelectedServerNode());
            if (remoteMember.areTaskRunning()) setRunningTaskOnNode(TASKS_RUNNING.label());
            final int currentIndex = getNodeSummary().indexOf(this);
            setNodeIx(currentIndex);
            final String nodeName = getNodeSummary().getCurrent().getNodeName();
            setNodeNameLabel(nodeName);
            setConfirmRestartNode(true);
            return actions.getDefault();
        }

        /** Set row values. */
        public void set(RemoteMember remoteMember) {
            setUptime(timeAsString(remoteMember.getUptime()));
            setNodeName(remoteMember.getName());

            setAvailable(true);

            setComponents(remoteMember.getComponentVersionList());

            setServices(remoteMember.getAvailableServices());
        }
    }  // end class NodeSummaryRow
}  // end class ClusterAdminForm
