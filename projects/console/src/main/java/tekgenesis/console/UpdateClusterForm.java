
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import java.net.InetAddress;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.NodeStatus;
import tekgenesis.cluster.jmx.RemoteCluster;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.common.Predefined;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Action;
import tekgenesis.sg.ClusterConf;
import tekgenesis.sg.MemberStatus;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.logging.Logger.getLogger;

/**
 * User class for Form: UpdateClusterForm
 */
public class UpdateClusterForm extends UpdateClusterFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void onLoad() {
        setLogDetails("");
        setVersion(0);
        setRefreshActived(false);

        final ClusterConf selected = Context.getSingleton(Clusters.class).getSelectedClusterConf();
        if (selected != null) setClusterName(selected.getName());

        getUpdateTableLog().clear();
    }

    /** Invoked when scheduled interval is triggered. */
    @NotNull @Override public Action refresh() {
        if (isRefreshActived()) {
            getUpdateTableLog().clear();

            final Option<RemoteCluster> activeCluster = Context.getSingleton(Clusters.class).getActiveCluster();
            if (!activeCluster.isPresent())  // noinspection DuplicateStringLiteralInspection
                return actions.getDefault().withMessage("Cluster not available. Try again");

            boolean allOk = true;

            final List<NodeStatus> updateStatus = activeCluster.get().getNodeStatus();

            for (final NodeStatus statusInfo : updateStatus) {
                final UpdateTableLogRow row = getUpdateTableLog().add();
                final String            ip  = statusInfo.getName();
                row.setIp("http:" + (ip.startsWith("/") ? "/" : "//") + ip);
                row.setServerNode(ip);
                row.setLog(Predefined.notNull(statusInfo.getLog()));
                final MemberStatus status = statusInfo.getStatus();
                row.setStatus(status.label());
                if (status != MemberStatus.UPDATED) allOk = false;
            }

            if (allOk) setRefreshActived(false);
        }

        return actions.getDefault();
    }  // end method refresh

    /** Invoked when button(update) is clicked. */
    @NotNull @Override public Action updateVersion() {
        final int version = getVersion();

        if (!isDefined(Field.BRANCH) || isEmpty(getBranch())) return actions.getError().withMessage(Messages.BRANCH_NOT_SPECIFIED.label());

        try {
            Context.getSingleton(Clusters.class).getActiveCluster().get().upgrade(version, getBranch(), isHealthCheck());
            setRefreshActived(true);
            return actions.getDefault();
        }
        catch (final Exception e) {
            logger.error(e);
            // noinspection DuplicateStringLiteralInspection
            return actions.getError().withMessage("Error trying to execute update version");
        }
    }

    @Nullable private RemoteMember getSelectedMember() {
        final List<RemoteMember> members      = Context.getSingleton(Clusters.class).getActiveCluster().get().getMembers();
        final String             selectedNode = getUpdateTableLog().getCurrent().getIp();
        for (final RemoteMember member : members) {
            final InetAddress address = member.getJmxEndpoint().getAddress();
            if (address != null && address.toString().equals(selectedNode)) return member;
        }
        return null;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(UpdateClusterForm.class);

    //~ Inner Classes ................................................................................................................................

    public class UpdateTableLogRow extends UpdateTableLogRowBase {
        @NotNull @Override public Action markNodeAsReady() {
            final RemoteMember member = getSelectedMember();
            if (member != null) {
                member.safeMode(false);
                return actions.getDefault();
            }
            // noinspection DuplicateStringLiteralInspection
            return actions.getError().withMessage("Member not found");
        }

        @NotNull @Override public Action rollbackNodeUpdate() {
            // final RemoteMember member = getSelectedMember();
            // if (member != null) {
            // final Tuple<Boolean, String> ret = member.rollbackUpdate();
            // if (ret.first()) return actions.getDefault();
            // else return actions.getError().withMessage(ret.second());
            // }
            // noinspection DuplicateStringLiteralInspection
            return actions.getError().withMessage("Member not found");
        }

        /** Invoked when display(viewLog) is clicked. */
        @NotNull @Override public Action showOutputLog() {
            setLogDetails(getLog());
            return actions.getDefault();
        }

        @Nullable private RemoteMember getSelectedMember() {
            final List<RemoteMember> members      = Context.getSingleton(Clusters.class).getActiveCluster().get().getMembers();
            final String             selectedNode = getUpdateTableLog().getCurrent().getIp();
            for (final RemoteMember member : members) {
                final InetAddress address = member.getJmxEndpoint().getAddress();
                if (address != null && address.toString().equals(selectedNode)) return member;
            }
            return null;
        }
    }
}  // end class UpdateClusterForm
