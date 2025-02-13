
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.notification.push.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.Role;
import tekgenesis.authorization.User;
import tekgenesis.authorization.g.RoleAssignmentTable;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Tuple;
import tekgenesis.metadata.authorization.Device;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.workflow.CaseInstance;
import tekgenesis.workflow.WorkItemInstance;

import static tekgenesis.authorization.User.find;
import static tekgenesis.authorization.g.RoleAssignmentTable.ROLE_ASSIGNMENT;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.Colls.*;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.notification.push.Notification.send;
import static tekgenesis.persistence.Criteria.EMPTY;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 */
public final class WorkItemNotifier {

    //~ Constructors .................................................................................................................................

    private WorkItemNotifier() {}

    //~ Methods ......................................................................................................................................

    /** Send Push notification to assignees.* */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static  //J-
        <C extends CaseInstance<C, CK, P, PK, W, WK>, CK,
         P extends EntityInstance<P, PK>, PK,
         W extends WorkItemInstance<W, WK, C, CK, P, PK>, WK
        >
    //J+
    void notify(@NotNull W instance, @NotNull Integer id, @NotNull DateTime updateTimestamp) {
        final Seq<String> assignees = getDevicesIds(instance);
        if (!assignees.isEmpty()) {
            final Tuple<String, String> assignee = getName(instance.getAssignee());
            final String                r        = instance.getReporter();
            final Tuple<String, String> reporter = r == null ? null : getName(r);

            final Map<String, String> message = new HashMap<>();
            message.put("id", Integer.toString(id));
            message.put("title", instance.getTitle());
            message.put("description", instance.getDescription());
            message.put("assignee", assignee.first());
            message.put("reporter", reporter == null ? "" : reporter.first());
            message.put("priority", Integer.toString(instance.getPriority().getCode()));
            message.put("updateTime", Long.toString(updateTimestamp.toMilliseconds()));
            message.put("avatar_sha", assignee.second());
            message.put("type", "NOTIFICATION");
            send(message, assignees);
        }
    }

    private static  //J-
        <C extends CaseInstance<C, CK, P, PK, W, WK>, CK,
         P extends EntityInstance<P, PK>, PK,
         W extends WorkItemInstance<W, WK, C, CK, P, PK>, WK
        >
    //J+
    Seq<String> getDevicesIds(@NotNull W instance) {
        final String assignee = instance.getAssignee();

        final String ouName = instance.getOuName();

        final String id   = assignee.substring(2);
        final String type = assignee.substring(0, 1);

        if (!"r".equals(type)) {
            final User user = find(id);
            if (user == null) return emptyIterable();
            return map(user.getEnabledDevices(), Device::getDeviceId);
        }

        final List<String> devicesIds = new ArrayList<>();

        final RoleAssignmentTable RA = ROLE_ASSIGNMENT;
        selectFrom(RA)                                                              //
        .where(RA.ROLE_ID.eq(id), isEmpty(ouName) ? EMPTY : RA.OU_NAME.eq(ouName))  //
        .forEach(ra -> map(ra.getUser().getEnabledDevices(), Device::getDeviceId).into(devicesIds));

        return immutable(devicesIds);
    }

    private static Tuple<String, String> getName(String code) {
        final Tuple<String, String> parsed = tuple(code.substring(0, 1), code.substring(2));
        if ("r".equals(parsed.first())) {
            // it s
            final Role role = Role.find(parsed.second());
            return role == null ? EMPTY_TUPLE : tuple(role.getName(), role.getImage());
        }

        final User user = find(parsed.second());
        return user == null ? EMPTY_TUPLE : tuple(user.getName(), user.getImage());
    }

    //~ Static Fields ................................................................................................................................

    private static final Tuple<String, String> EMPTY_TUPLE = tuple("", "");
}  // end class WorkItemNotifier
