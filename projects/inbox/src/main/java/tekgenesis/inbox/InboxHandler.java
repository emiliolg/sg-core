
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.inbox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.OrgUnit;
import tekgenesis.authorization.Role;
import tekgenesis.authorization.shiro.AuthorizationUtils;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.util.Reflection;
import tekgenesis.form.ApplicationContext;
import tekgenesis.form.ApplicationContextImpl;
import tekgenesis.metadata.authorization.User;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.workflow.WorkItemInstance;

import static tekgenesis.authorization.g.UserBase.find;
import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.collections.Colls.sorted;
import static tekgenesis.common.core.Tuple.tuple;

/**
 * User class for Handler: InboxHandler
 */
public class InboxHandler extends InboxHandlerBase {

    //~ Constructors .................................................................................................................................

    InboxHandler(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    /** Invoked for route "/inbox/list". */
    @NotNull @Override public Result<Seq<WorkItem>> list() {
        final ApplicationContext                       context = ApplicationContextImpl.getInstance();
        final User                                     user    = context.getUser();
        final OrgUnit                                  orgUnit = AuthorizationUtils.getCurrentOrgUnit();
        final List<WorkItemInstance<?, ?, ?, ?, ?, ?>> items   = WorkItems.getWorkItems(user, orgUnit);

        final List<WorkItem> workItems = new ArrayList<>();

        for (final WorkItemInstance<?, ?, ?, ?, ?, ?> item : items) {
            final WorkItem wi = new WorkItem();

            final Tuple<String, String> assignee = getName(item.getAssignee());
            final Tuple<String, String> reporter = getName(notNull(item.getReporter()));

            wi.setReporter(reporter.first());
            wi.setAssignee(assignee.first());
            wi.setAvatar_sha(reporter.second());
            wi.setPriority(item.getPriority().getCode());
            wi.setTitle(notNull(item.getTitle()));
            wi.setDescription(item.getDescription());

            final int id = ensureNotNull(Reflection.getPrivateField(item, "id"));
            // noinspection DuplicateStringLiteralInspection
            final DateTime updateTime = ensureNotNull(Reflection.getPrivateField(item, "updateTime"));

            wi.setId(id);
            wi.setUpdateTime(updateTime.toMilliseconds());
            // noinspection DuplicateStringLiteralInspection
            wi.setType("NOTIFICATION");

            workItems.add(wi);
        }

        return ok(sorted(seq(workItems), Comparator.comparingInt(WorkItem::getPriority)));
    }  // end method list

    //~ Methods ......................................................................................................................................

    private static Tuple<String, String> getName(String code) {
        final Tuple<String, String> parsed = tuple(code.substring(0, 1), code.substring(2));
        if ("r".equals(parsed.first())) {
            // it s
            final Role role = Role.find(parsed.second());
            return role == null ? EMPTY_TUPLE : tuple(role.getName(), role.getImage());
        }
        else {
            final tekgenesis.authorization.User user = find(parsed.second());
            return user == null ? EMPTY_TUPLE : tuple(user.getName(), user.getImage());
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Tuple<String, String> EMPTY_TUPLE = tuple("", "");
}  // end class InboxHandler
