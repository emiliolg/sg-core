
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.NodeStatus;
import tekgenesis.cluster.jmx.RemoteCluster;
import tekgenesis.cluster.jmx.notification.Notifier;
import tekgenesis.cluster.jmx.notification.Operation;
import tekgenesis.common.Predefined;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Action;
import tekgenesis.sg.MemberStatus;

import static tekgenesis.admin.Message.BRANCH_NOT_SPECIFIED;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.logging.Logger.getLogger;

/**
 * User class for Form: UpdateVersionForm
 */
public class UpdateVersionForm extends UpdateVersionFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void onLoad() {
        setLogDetails("");
        setVersion(0);
        setRefreshActived(false);
        getUpdateTableLog().clear();
    }

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
                row.setServerNode(ip);
                row.setLog(Predefined.notNull(statusInfo.getLog()));
                final MemberStatus status = statusInfo.getStatus();
                row.setStatus(status.label());
                if (status != MemberStatus.UPDATED) allOk = false;
            }

            if (allOk) setRefreshActived(false);
        }

        return actions.getDefault();
    }

    @NotNull @Override public Action updateVersion() {
        final int version = getVersion();

        if (!isDefined(Field.BRANCH) || isEmpty(getBranch())) return actions.getError().withMessage(BRANCH_NOT_SPECIFIED.label());

        try {
            Notifier.broadcastCall(Operation.UPDATE_VERSION, version, getBranch());
            setRefreshActived(true);
            return actions.getDefault();
        }
        catch (final Exception e) {
            logger.error(e);
            // noinspection DuplicateStringLiteralInspection
            return actions.getError().withMessage("Error trying to execute update version");
        }
    }  // end method updateVersion

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(UpdateVersionForm.class);

    //~ Inner Classes ................................................................................................................................

    public class UpdateTableLogRow extends UpdateTableLogRowBase {
        @NotNull @Override public Action showOutputLog() {
            setLogDetails(getLog());
            return actions.getDefault();
        }
    }
}  // end class UpdateVersionForm
