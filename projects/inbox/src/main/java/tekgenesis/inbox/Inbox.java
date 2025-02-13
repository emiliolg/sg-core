
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.inbox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.*;
import tekgenesis.authorization.shiro.AuthorizationUtils;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Tuple;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.metadata.authorization.Assignee;
import tekgenesis.metadata.authorization.Property;
import tekgenesis.metadata.authorization.User;
import tekgenesis.metadata.form.InstanceReference;
import tekgenesis.workflow.WorkItemInstance;

import static tekgenesis.authorization.shiro.AuthorizationUtils.getCurrentOrgUnit;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.metadata.form.InstanceReference.createInstanceReference;
import static tekgenesis.workflow.WorkItemReferences.createWorkInstance;

/**
 * User class for Inbox form.
 */
@SuppressWarnings({ "WeakerAccess", "DuplicateStringLiteralInspection" })
public class Inbox extends InboxBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the inbox loads. */
    @Override public void load() {
        if (context.isAuthenticated()) {
            final Set<OrgUnit> userOrgUnits   = AuthorizationUtils.getUserOrgUnits();
            final OrgUnit      currentOrgUnit = getCurrentOrgUnit();
            if (userOrgUnits.isEmpty()) setFilterOptions(Colls.listOf(currentOrgUnit));
            else setFilterOptions(userOrgUnits.contains(currentOrgUnit) ? userOrgUnits : Colls.append(userOrgUnits, Colls.listOf(currentOrgUnit)));
            setFilter(currentOrgUnit);
            setNoOu(userOrgUnits.isEmpty());

            final User     user      = context.getUser();
            final Property inboxView = user.getProperty("inboxView");
            if (inboxView != null) setTableView(inboxView.asBoolean());
            search();
        }
    }

    /** Invoked when inbox row is clicked. */
    @NotNull public Action navigate() {
        final InboxTask                          row      = isTableView() ? getInboxTable().getCurrent() : getInbox().getCurrent();
        final InstanceReference                  ref      = createInstanceReference(row.getWorkItemFqn(), row.getWorkItemKey());
        final WorkItemInstance<?, ?, ?, ?, ?, ?> instance = createWorkInstance(ref);

        Action result;

        try {
            result = WorkItems.navigate(instance);
        }
        catch (final WorkItemAlreadyCloseException e) {
            result = actions.getError().withMessage(e.getMessage());
        }

        return result;
    }

    @NotNull @Override public Action search() {
        populateInbox(isTableView());
        return actions.getDefault();
    }

    @NotNull @Override public Action toggle() {
        final boolean cardView = !isTableView();
        populateInbox(cardView);
        setTableView(cardView);
        if (noViewProperty()) createViewProperty();

        if (context.isAuthenticated()) {
            final User     user      = context.getUser();
            final Property inboxView = user.getProperty("inboxView");
            if (inboxView != null) inboxView.setValue(cardView);
            user.updateProperty(inboxView);
        }

        return actions.getDefault();
    }

    private void createViewProperty() {
        final tekgenesis.authorization.Property inboxView = tekgenesis.authorization.Property.create("inboxView",
                "Inbox Table View",
                PropertyType.BOOLEAN);
        inboxView.setScope(PropertyScope.USER);
        inboxView.persist();

        if (context.isAuthenticated()) {
            final tekgenesis.authorization.User user = tekgenesis.authorization.User.find(context.getUser().getId());
            if (user != null) {
                final UserProperties userProperties = user.getProps().add();
                userProperties.setProperty(inboxView);
                userProperties.persist();
            }
        }
    }

    private boolean filterOu(List<String> ous, WorkItemInstance<?, ?, ?, ?, ?, ?> instance) {
        return instance.getOuName() == null || ous.contains(instance.getOuName().toLowerCase());
    }

    private boolean filterSearch(String search, WorkItemInstance<?, ?, ?, ?, ?, ?> instance) {
        return instance.getDescription().toLowerCase().contains(search) || instance.getTask().toLowerCase().contains(search);
    }

    private boolean noViewProperty() {
        final tekgenesis.authorization.Property inboxView = tekgenesis.authorization.Property.find(
                Tuple.tuple("inboxView", "Inbox Table View", PropertyType.BOOLEAN));
        return inboxView == null;
    }

    private void populateInbox(boolean isTable) {
        if (context.isAuthenticated()) {
            final User                                     user   = context.getUser();
            final OrgUnit                                  filter = isDefined(Field.FILTER) ? getFilter() : getCurrentOrgUnit();
            final List<WorkItemInstance<?, ?, ?, ?, ?, ?>> items  = WorkItems.getWorkItems(user, filter);

            final FormTable<? extends InboxTask> table;

            if (isTable) table = getInboxTable();
            else table = getInbox();

            final String search = notNull(getSearch(), "").toLowerCase();

            items.sort((alpha, omega) -> omega.getCreation().compareTo(alpha.getCreation()));
            table.clear();

            final ImmutableList<String> ous = filter.getHierarchy().map(String::toLowerCase).toList();

            final Map<String, Assignee> names = new HashMap<>();
            for (final WorkItemInstance<?, ?, ?, ?, ?, ?> instance : items)
                if (filterOu(ous, instance) && filterSearch(search, instance)) table.add().populate(instance, user, names);
        }
    }

    //~ Static Fields ................................................................................................................................

    public static final String INBOX_FQN = "tekgenesis.inbox.Inbox";

    //~ Inner Interfaces .............................................................................................................................

    private interface InboxTask {
        void populate(@NotNull final WorkItemInstance<?, ?, ?, ?, ?, ?> instance, @NotNull final User me, @NotNull final Map<String, Assignee> names);
        QName getWorkItemFqn();
        String getWorkItemKey();
    }

    //~ Inner Classes ................................................................................................................................

    public class InboxRow extends InboxRowBase implements InboxTask {
        /** Invoked when inbox row is clicked. */
        @NotNull public Action navigate() {
            return Inbox.this.navigate();
        }
        public void populate(@NotNull final WorkItemInstance<?, ?, ?, ?, ?, ?> instance, @NotNull final User me,
                             @NotNull final Map<String, Assignee> names) {
            setWorkItemClass(instance.getClass().getName());
            setWorkItemPk(instance.keyAsString());
            setTask(notNull(instance.getTitle(), instance.getTask()));
            setDescription(instance.toString());
            setCreation(instance.getCreation());

            final Assignee assignee = getAssignee(instance, names);
            setOnlyToMe(assignee.equals(me));
            setAssignee(assignee.getName());
            final String ouName = instance.getOuName();
            if (ouName != null) setOuName(ouName);

            final String reporterString = instance.getReporter();
            if (reporterString != null) {
                final Assignee reporter = Assignees.fromString(reporterString);
                setReporter(reporter.getName());
                setImg(reporter.getImage());
                setHasReporter(true);
            }
        }

        public QName getWorkItemFqn() {
            return createQName(getWorkItemClass());
        }

        @Override public String getWorkItemKey() {
            return getWorkItemPk();
        }

        private Assignee getAssignee(@NotNull final WorkItemInstance<?, ?, ?, ?, ?, ?> instance, @NotNull final Map<String, Assignee> names) {
            return names.computeIfAbsent(instance.getAssignee(), k -> Assignees.fromString(instance.getAssignee()));
        }
    }  // end class InboxRow

    public class InboxTableRow extends InboxTableRowBase implements InboxTask {
        public void populate(@NotNull final WorkItemInstance<?, ?, ?, ?, ?, ?> instance, @NotNull final User me,
                             @NotNull final Map<String, Assignee> names) {
            setWorkItemClassT(instance.getClass().getName());
            setWorkItemPkT(instance.keyAsString());
            setTaskT(notNull(instance.getTitle(), instance.getTask()));
            setDescriptionT(instance.toString());
            setCreationT(instance.getCreation());
            final Assignee assignee = getAssignee(instance, names);
            setAssigneeT(assignee.getName());
            final String reporterString = instance.getReporter();
            if (reporterString != null) {
                final Assignee reporter = Assignees.fromString(reporterString);
                setReporterT(reporter.getName());
            }
        }

        public QName getWorkItemFqn() {
            return createQName(getWorkItemClassT());
        }

        @Override public String getWorkItemKey() {
            return getWorkItemPkT();
        }

        private Assignee getAssignee(@NotNull final WorkItemInstance<?, ?, ?, ?, ?, ?> instance, @NotNull final Map<String, Assignee> names) {
            return names.computeIfAbsent(instance.getAssignee(), k -> Assignees.fromString(instance.getAssignee()));
        }
    }
}  // end class Inbox
