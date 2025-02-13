
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import java.util.EnumMap;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.app.service.EmailService;
import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.RemoteCluster;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.cluster.jmx.service.RemoteMailService;
import tekgenesis.cluster.jmx.service.RemoteService;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.mail.MailStatus;
import tekgenesis.sg.ClusterConf;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.logging.Logger.getLogger;

/**
 * User class for Form: EmailForm
 */
public class EmailForm extends EmailFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action cleanQueue() {
        final Seq<MailStatusItem> mailStatusItems = getMailStatus();
        Action                    action;
        try {
            final Seq<MailStatus> map = map(mailStatusItems, value -> MailStatus.valueOf(value.name()));

            final MailStatus[] mailStatuses = map.toList().toArray(new MailStatus[map.size()]);

            final RemoteMember anyRemoteMember = Context.getSingleton(Clusters.class).getActiveCluster().get().getMembers().get(0);

            anyRemoteMember.getService(RemoteMailService.class, EmailService.SERVICE_NAME).cleanMailQueue(mailStatuses);

            action = actions.getDefault();
        }
        catch (final Exception e) {
            logger.error(e);
            action = actions.getError().withMessage(e.getMessage());
        }
        setCleanQueueDialog(false);
        return action;
    }

    @NotNull @Override public Action closeCleanQueueDialog() {
        setCleanQueueDialog(false);
        return actions.getDefault();
    }

    @Override public void init() {
        final ServiceStatus statusForm = createStatusForm();
        statusForm.setService(EmailService.SERVICE_NAME);
        statusForm.setServiceClass(RemoteMailService.class.getName());
        statusForm.configure();

        final Clusters    clusters = Context.getSingleton(Clusters.class);
        final ClusterConf selected = clusters.getSelectedClusterConf();
        if (selected != null) setClusterName(selected.getName());

        final FormTable<MailStatusTableRow> mailStatusTable = getMailStatusTable();

        mailStatusTable.clear();

        final Option<RemoteCluster> activeCluster = clusters.getActiveCluster();
        final List<RemoteMember>    remoteMembers = activeCluster.get().getMembers();
        RemoteMailService           mailService   = null;
        for (final RemoteMember remoteMember : remoteMembers) {
            final RemoteService service = remoteMember.getService(RemoteMailService.class, EmailService.SERVICE_NAME);

            // Fill table only once
            if (mailStatusTable.isEmpty()) {
                mailService = cast(service);
                break;
            }
        }

        if (mailService != null) {
            final EnumMap<MailStatus, Integer> status = mailService.getMailQueue();
            for (final MailStatus mailStatus : status.keySet()) {
                final MailStatusTableRow row = getMailStatusTable().add();
                row.setMailStatusLabel(mailStatus.label());
                row.setMailItems(status.get(mailStatus));
            }
        }
    }

    @NotNull @Override public Action openCleanQueueDialog() {
        setCleanQueueDialog(true);
        return actions.getDefault();
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(EmailForm.class);

    //~ Inner Classes ................................................................................................................................

    public class MailStatusTableRow extends MailStatusTableRowBase {}
}  // end class EmailForm
