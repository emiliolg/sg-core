
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.shiro.session.mgt.SimpleSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.shiro.SuiGenerisAuthorizingRealm;
import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.exception.InvokerConnectionException;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Action;
import tekgenesis.sg.ClusterConf;
import tekgenesis.type.permission.PredefinedPermission;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Constants.SHIRO_SESSION_CACHE;
import static tekgenesis.task.jmx.JmxConstants.CACHES;

/**
 * User class for Form: UsersForm
 */
public class UsersForm extends UsersFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when text_field(searchBox) value changes. */
    @NotNull @Override public Action filterUsers() {
        filter(s -> !isDefined(Field.SEARCH_BOX) || (s != null && getSearchBox() != null && s.startsWith(getSearchBox())));

        return actions.getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void loadUsers() {
        filter(null);
        final ClusterConf selected = Context.getSingleton(Clusters.class).getSelectedClusterConf();
        if (selected != null) setClusterName(selected.getName());
        setDisable(!forms.hasPermission(PredefinedPermission.UPDATE));
    }

    private void filter(@Nullable Predicate<String> predicate) {
        final List<SimpleSession> values = getValues();

        getUsersTable().clear();

        final Map<String, Integer> sessionsOpens = new HashMap<>();

        for (final SimpleSession session : values) {
            final Map<Object, Object> attributes = session.getAttributes();
            if (attributes != null) {
                final Object value = attributes.get(USER_ID);
                if (value != null) {
                    final String sessionUser = (String) value;

                    if (!isEmpty(sessionUser) && (predicate == null || predicate.test(sessionUser))) {
                        Integer sessionCount = sessionsOpens.get(sessionUser);
                        sessionCount = sessionCount == null ? 1 : sessionCount + 1;
                        sessionsOpens.put(sessionUser, sessionCount);
                    }
                }
            }
        }

        for (final Map.Entry<String, Integer> session : sessionsOpens.entrySet()) {
            final UsersTableRow row = getUsersTable().add();

            row.setSessionCount(session.getValue());
            row.setUserName(session.getKey());
        }
    }

    @Nullable private SimpleSession getSimpleSession(String shiroSessionId) {
        final RemoteMember remoteMember = Context.getSingleton(Clusters.class).getActiveCluster().get().getMembers().get(0);
        try {
            return JmxInvokerImpl.invoker(remoteMember.getJmxEndpoint())
                   .mbean(CACHES)
                   .invoke("getValue",
                    new String[] { String.class.getName(), Object.class.getName() },
                    new Object[] { SHIRO_SESSION_CACHE, shiroSessionId });
        }
        catch (final InvokerConnectionException e) {
            logger.error(e);
        }
        return null;
    }

    @NotNull private List<SimpleSession> getValues() {
        final RemoteMember remoteMember = Context.getSingleton(Clusters.class).getActiveCluster().get().getMembers().get(0);
        try {
            return JmxInvokerImpl.invoker(remoteMember.getJmxEndpoint())
                   .mbean(CACHES)
                   .invoke("getValues", new String[] { String.class.getName() }, new Object[] { SHIRO_SESSION_CACHE });
        }
        catch (final InvokerConnectionException e) {
            logger.error(e);
        }
        return Colls.emptyList();
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger    = Logger.getLogger(UsersForm.class);
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String MOBILE    = "mobile";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String CLIENT_IP = "client-ip";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String USER_ID = "userId";

    //~ Inner Classes ................................................................................................................................

    public class SessionsTabRow extends SessionsTabRowBase {
        /** Invoked when button(expire) is clicked. */
        @NotNull @Override public Action expireUserSession() {
            final String shiroSessionId = getSessionId();

            final SimpleSession session = getSimpleSession(shiroSessionId);
            if (session != null) {
                session.stop();
                getSessionsTab().removeCurrent();
                return actions.getDefault();
            }
            return actions.getError().withMessage("Unable to remove the session");
        }
    }

    public class UsersTableRow extends UsersTableRowBase {
        /** Invoked when display(sessionCount) is clicked. */
        @NotNull @Override public Action sessionDetails() {
            getSessionsTab().clear();
            final String              userName = getUserName();
            final List<SimpleSession> values   = getValues();

            for (final SimpleSession session : values) {
                final Map<Object, Object> attributes = session.getAttributes();
                if (attributes != null) {
                    final String sessionUser = (String) attributes.get(USER_ID);
                    if (userName.equals(sessionUser)) {
                        final SessionsTabRow row       = getSessionsTab().add();
                        final String         attribute = (String) session.getAttribute(CLIENT_IP);

                        row.setHost(notNull(notNull(attribute, session.getHost()), "Not Available"));
                        row.setOu((String) notNull(session.getAttribute(SuiGenerisAuthorizingRealm.CURRENT_OU), SuiGenerisAuthorizingRealm.ROOT_OU));
                        row.setLastAccessTime(DateTime.fromDate(session.getLastAccessTime()));

                        row.setSessionId((String) session.getId());
                        row.setMobile((Boolean) notNull(session.getAttribute(MOBILE), Boolean.FALSE));
                    }
                }
            }
            setSessionDialog(true);
            return actions.getDefault();
        }
    }
}  // end class UsersForm
