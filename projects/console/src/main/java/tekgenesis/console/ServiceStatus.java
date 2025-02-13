
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
import tekgenesis.cluster.jmx.service.RemoteService;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Reflection;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.console.Messages.*;

/**
 * User class for Form: ServiceStatus
 */
public class ServiceStatus extends ServiceStatusBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when button(closeCSSDialog) is clicked. */
    @NotNull @Override public Action closeChangeServiceStatusDialog() {
        setConfirmChangeServiceStatus(false);
        return actions.getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void configure() {
        if (isEmpty(getService()) || isEmpty(getServiceClass())) return;
        final FormTable<ServerStatusByHostRow> statusByHost = getServerStatusByHost();
        statusByHost.clear();
        final Option<RemoteCluster> activeCluster = Context.getSingleton(Clusters.class).getActiveCluster();
        final List<RemoteMember>    remoteMembers = activeCluster.get().getMembers();

        for (final RemoteMember remoteMember : remoteMembers) {
            final ServerStatusByHostRow row = statusByHost.add();
            row.setMemberName(remoteMember.getName());

            final Class<RemoteService> cl      = Reflection.findClass(getServiceClass());
            final RemoteService        service = remoteMember.getService(cl, getService());

            final boolean running = service.isRunning();
            row.setServiceStatus(running);
        }
    }

    /** Invoked when button(done) is clicked. */
    @NotNull @Override public Action toggleTaskServiceStatus() {
        try {
            final RemoteCluster      clusterInstance = Context.getSingleton(Clusters.class).getActiveCluster().get();
            final List<RemoteMember> remoteMembers   = clusterInstance.getMembers();
            for (final RemoteMember remoteMember : remoteMembers) {
                if (remoteMember.getName().equals(getNode())) {
                    final RemoteService service = remoteMember.getService(getService());
                    if (service.isRunning()) service.stop();
                    else service.start();
                }
            }
            return closeChangeServiceStatusDialog();
        }
        catch (final Exception e) {
            logger.error(e);

            return actions.getError().withMessage("Unable to perform the operation");
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(ServiceStatus.class);

    //~ Inner Classes ................................................................................................................................

    public class ServerStatusByHostRow extends ServerStatusByHostRowBase {
        @NotNull @Override public Action openConfirmDialog() {
            final String action = isServiceStatus() ? START_ACTION.label() : STOP_ACTION.label();
            setMsgCSS(SERVICE_STATUS_CHANGE.label(action));
            setNode(getMemberName());
            setConfirmChangeServiceStatus(true);
            return actions.getDefault();
        }
    }
}  // end class ServiceStatus
