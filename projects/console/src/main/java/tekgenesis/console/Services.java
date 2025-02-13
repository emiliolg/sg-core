
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
import java.util.Map;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.RemoteCluster;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.cluster.jmx.service.RemoteService;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.service.ServiceStatus;

import static java.lang.String.format;

import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.console.Messages.SERVICE_STATUS_CHANGE;
import static tekgenesis.console.Messages.START_ACTION;
import static tekgenesis.service.ServiceStatus.DISABLED;
import static tekgenesis.service.ServiceStatus.RUNNING;

/**
 * User class for Form: Services
 */
public class Services extends ServicesBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action closeServiceStatusDialog() {
        refreshServiceTable();
        setConfirmChangeServiceStatus(false);
        return actions.getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void configure() {
        refreshServiceTable();
    }

    @NotNull @Override public Object populate() {
        refreshServiceTable();
        return getCurrentNode();
    }

    @NotNull @Override public Action toggleServiceStatus() {
        if (!isDefined(Field.CURRENT_NODE))  // noinspection DuplicateStringLiteralInspection
            return actions.getError().withMessage("There is no selected Node");

        final Consumer<RemoteMember> block = member ->
                                             {
                                                 if (member == null) return;
                                                 final String serviceId = getCurrentService();

                                                 try {
                                                     final RemoteService service = member.getService(serviceId);
                                                     if (isCurrentServiceStatus()) service.start();
                                                     else service.stop();

                                                     setConfirmChangeServiceStatus(false);
                                                     logger().info(String.format("Toggle Service status (Service: %s)", serviceId));
                                                 }
                                                 catch (final Exception e) {
                                                     final String message = e.getMessage() == null ? e.getCause().getMessage() : e.getMessage();
                                                     setMgsError(message);
                                                     setHasError(true);
                                                 }
                                             };
        return invokeOverMember(getCurrentNode(), block);
    }

    private Action invokeOverMember(final String memberName, final Consumer<RemoteMember> block) {
        final Option<RemoteCluster> cluster = Context.getSingleton(Clusters.class).getActiveCluster();
        if (cluster.isPresent()) {
            final RemoteCluster        clusterMBean  = cluster.get();
            final List<RemoteMember>   remoteMembers = clusterMBean.getMembers();
            final Option<RemoteMember> m             = Colls.first(remoteMembers, member -> member != null && member.getName().equals(memberName));

            if (m.isPresent()) {
                final RemoteMember remoteMember = m.get();

                block.accept(remoteMember);

                return actions.getDefault();
            }
            else return actions.getError().withMessage(format(NODE_NOT_FOUND, getCurrentNode(), clusterMBean.getName()));
        }
        else return actions.getError().withMessage("No active cluster");
    }

    private void refreshServiceTable() {
        setLoading(true);
        //J-
        if (isDefined(Field.CURRENT_NODE)) invokeOverMember(getCurrentNode(), member -> {
            if ( member == null) return;
            final Map<String, tekgenesis.service.ServiceStatus> servicesStatus = member.getServicesStatus();

            final FormTable<ServicesRow> table = getServices();
            table.clear();

            for (final String serviceName :servicesStatus.keySet())
            {
                final ServicesRow row = table.add();
                row.set(serviceName, servicesStatus.get(serviceName));
            }
        });
        //J+
        setLoading(false);
    }

    //~ Static Fields ................................................................................................................................

    private static final String NODE_NOT_FOUND = "Node %s not found in cluster %s";

    //~ Inner Classes ................................................................................................................................

    public class ServicesRow extends ServicesRowBase {
        @NotNull @Override public Action confirmChangeStatus() {
            final String action = isServiceStatus() ? Messages.STOP_ACTION.label() : START_ACTION.label();
            setMsgCSS(SERVICE_STATUS_CHANGE.label(action, getName()));

            setCurrentService(getId());
            setCurrentServiceStatus(isServiceStatus());

            setConfirmChangeServiceStatus(true);
            return actions.getDefault();
        }

        /**  */
        public void set(String service, ServiceStatus status) {
            setId(service);
            setName(extractName(service));

            setServiceStatus(status == RUNNING);
            setIsEnabled(status != DISABLED);
        }
        @NotNull @Override public Action updateServiceEnabled() {
            if (!isDefined(Field.CURRENT_NODE))  // noinspection DuplicateStringLiteralInspection
                return actions.getError().withMessage("There is no selected Node");

            if (!isLoading()) {
                final Consumer<RemoteMember> block = member -> {
                                                         if (member == null) return;
                                                         final String serviceId = getId();

                                                         try {
                                                             final RemoteService service = member.getService(serviceId);

                                                             if (isIsEnabled()) service.enable();
                                                             else service.disable();
                                                         }
                                                         catch (final Exception e) {
                                                             final String message = e.getMessage() == null ? e.getCause().getMessage()
                                                                                                           : e.getMessage();
                                                             setMgsError(message);
                                                             setHasError(true);
                                                         }
                                                     };

                return invokeOverMember(getCurrentNode(), block);
            }

            return actions.getDefault();
        }
    }  // end class ServicesRow
}  // end class Services
