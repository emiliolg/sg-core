
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import java.util.Collection;

import org.apache.shiro.session.Session;
import org.infinispan.remoting.transport.Address;
import org.jetbrains.annotations.NotNull;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.jmx.util.ClusterInfo;
import tekgenesis.common.env.Version;
import tekgenesis.common.env.context.Context;
import tekgenesis.console.util.SessionUtils;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.form.configuration.ChartConfiguration;
import tekgenesis.sg.g.TaskEntryTable;
import tekgenesis.task.Task;
import tekgenesis.task.TaskStatus;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * User class for Form: StatusForm
 */
public class StatusForm extends StatusFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void load() {
        // noinspection MagicNumber
        this.<ChartConfiguration>configuration(Field.SESSIONS).dimension(215, 80).mainXAxis().axisVisible(false).positiveQuadrant();

        // noinspection MagicNumber
        this.<ChartConfiguration>configuration(Field.USERS).dimension(215, 80).mainXAxis().axisVisible(false).positiveQuadrant();

        final FormTable<ComponentsRow> components = getComponents();
        for (final Version.ComponentInfo component : Version.getInstance().getComponents()) {
            final ComponentsRow row = components.add();
            row.setComponent(component.getComponent());
            row.setVersion(component.getVersion() + " " + component.getBranch() + "-" + component.getBuild());
        }

        refresh();
    }

    /** Invoked when scheduled interval is triggered. */
    @NotNull @Override public Action refresh() {
        // Sessions chart.

        final int                    sessionsOpened = getSessionsOpened();
        final FormTable<SessionsRow> sessions       = getSessions();

        if (sessions.size() > MAX) sessions.remove(0);
        sessions.add().setSessionsOpened(sessionsOpened);
        setCurrentSessionOpened(sessionsOpened);

        final int                 usersLogged = getUsersLogged();
        final FormTable<UsersRow> users       = getUsers();

        if (users.size() > MAX) users.remove(0);
        users.add().setUsersLog(usersLogged);

        setCurrentUsersOpened(usersLogged);

        // Nodes.
        setNodes(ClusterInfo.getInstance().getMembers().size());

        // Tasks.
        setLoadedTasks(Task.listScheduledTasks().size());

        // Tasks.
        runInTransaction(() ->
                setRunningTasks(selectFrom(TaskEntryTable.TASK_ENTRY).where(TaskEntryTable.TASK_ENTRY.STATUS.eq(TaskStatus.RUNNING)).count()));

        // Current node.
        final ClusterManager<Address> clusterManager = cast(Context.getContext().getSingleton(ClusterManager.class));
        final String                  currentMember  = clusterManager.getCurrentMember();
        if (currentMember != null) setCurrentNodeName(currentMember);

        return actions.getDefault();
    }

    private int getSessionsOpened() {
        return SessionUtils.getSessionDAO().getActiveSessions().size();
    }

    private int getUsersLogged() {
        int                       total          = 0;
        final Collection<Session> activeSessions = SessionUtils.getSessionDAO().getActiveSessions();
        if (activeSessions != null && !activeSessions.isEmpty()) {
            for (final Session session : activeSessions) {
                final Object userId = session.getAttribute(USER_ID);
                if (userId != null && isNotEmpty(userId.toString())) total++;
            }
        }
        return total;
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("MagicNumber")
    private static final int MAX = 30;

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String USER_ID = "userId";

    //~ Inner Classes ................................................................................................................................

    public class ComponentsRow extends ComponentsRowBase {}

    public class SessionsRow extends SessionsRowBase {}

    public class UsersRow extends UsersRowBase {}
}  // end class StatusForm
