
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

import javax.management.Attribute;
import javax.management.AttributeList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.AttributeChangeJmxOperation;
import tekgenesis.cluster.jmx.MethodInvokeJmxOperation;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.cluster.jmx.util.Format;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.exception.InvokerConnectionException;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.sg.ClusterConf;
import tekgenesis.sg.MemberStatus;
import tekgenesis.type.permission.PredefinedPermission;

import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.console.Messages.SHUTDOWN_FAILED;
import static tekgenesis.console.Messages.TASKS_RUNNING;

/**
 * User class for Form: NodeAdminForm
 */
public class NodeAdminForm extends NodeAdminFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when toggle_button(xNodeHeader) value ui changes. */
    @NotNull @Override public Action applyXNodeHeader() {
        return actions.getDefault();
    }

    @NotNull @Override public Action changeStatus() {
        final RemoteMember member = getMember();
        if (member != null) {
            final MemberStatus status = member.getStatus().getStatus();
            if ((status != MemberStatus.SAFE_MODE && status != MemberStatus.READY))
                return actions.getError().withMessage(String.format("Illegal state %s", status.label()));

            member.safeMode(status != MemberStatus.SAFE_MODE);
            setStatus(member.getStatus().getStatus().label());
            return actions.getDefault();
        }
        // noinspection DuplicateStringLiteralInspection
        return actions.getError().withMessage("Member not found");
    }

    @NotNull @Override public Action closeResetNodeDialog() {
        setRunningTaskOnNode("");
        setConfirmRestartNode(false);
        return actions.getDefault();
    }

    /** Invoked when text_field(searchPropsBox) value changes. */
    @NotNull @Override public Action filterProperties() {
        loadProperties(getSearchPropsBox());
        return actions.getDefault();
    }

    @Override public void load() {
        createServiceForm();
        createLoggingForm();
    }

    @NotNull @Override public Action openRestartDialog() {
        final RemoteMember remoteMember = getMember();
        if (remoteMember != null) {
            if (remoteMember.areTaskRunning()) setRunningTaskOnNode(TASKS_RUNNING.label());
        }
        setNodeNameLabel(getNode());
        setConfirmRestartNode(true);
        return actions.getDefault();
    }

    @NotNull @Override public Object populate() {
        final String node = getNode();
        tabChanged();
        return node;
    }

    @NotNull @Override public Action restartNode() {
        Action ret;

        try {
            final RemoteMember remoteMember = getMember();
            if (remoteMember != null) remoteMember.restart();

            ret = actions.getDefault();
        }
        catch (final InvokerConnectionException e) {
            logger.warning(e);
            ret = actions.getError().withMessage(SHUTDOWN_FAILED.label());
        }
        setConfirmRestartNode(false);

        return ret;
    }

    /** Invoked when tabs(tab1) value changes. */
    @NotNull @Override public Action tabChanged() {
        setDisable(!forms.hasPermission(PredefinedPermission.UPDATE));

        final ClusterConf selected = Context.getSingleton(Clusters.class).getSelectedClusterConf();
        if (selected != null) setClusterName(selected.getName());

        final RemoteMember remoteMember = getMember();

        if (remoteMember != null) setStatus(remoteMember.getStatus().getStatus().label());

        switch (getTab()) {
        case 0:
            if (remoteMember != null) {
                setUptime(Format.timeAsString(remoteMember.getUptime()));
                setComponents(remoteMember.getComponentVersionList());
                setServices(remoteMember.getAvailableServices());
            }
            break;
        case 1:
            final Services serviceForm = getServiceForm();
            if (serviceForm != null) {
                serviceForm.setCurrentNode(getNode());
                serviceForm.configure();
            }

            break;
        case 2:

            break;
        case 3:
            final LoggerConfiguration loggingForm = getLoggingForm();
            if (loggingForm != null) {
                loggingForm.setCurrentNode(getNode());
                loggingForm.loadConfiguration();
            }
            break;
        case 4:
            loadProperties(getSearchPropsBox());
            break;
        }

        return actions.getDefault();
    }  // end method tabChanged

    private void loadProperties(@Nullable String filter) {
        final RemoteMember remoteMember = getMember();
        if (remoteMember == null) return;

        final Map<String, String>         properties    = remoteMember.getProperties();
        final FormTable<PropertyTableRow> propertyTable = getPropertyTable();
        propertyTable.clear();

        for (final String key : properties.keySet()) {
            final boolean mustAdd = filter == null || key.contains(filter);

            if (mustAdd) {
                final String jmxEndpoint;

                final String[] keyParts = key.split("\\.");

                final String fieldName;
                if (keyParts.length > 2) {
                    jmxEndpoint = String.format(ConsoleConstants.CONFIGURATION_MBEAN, keyParts[0], keyParts[1]);
                    fieldName   = keyParts[2];
                }
                else {
                    jmxEndpoint = String.format(ConsoleConstants.DEFAULT_CONFIGURATION_MBEAN, keyParts[0]);
                    fieldName   = keyParts[1];
                }
                final PropertyTableRow row = getPropertyTable().add();

                row.setAttrName(fieldName);
                row.setEndpoint(jmxEndpoint);
                row.setPropName(key);
                if (properties.get(key) != null) row.setPropValue(properties.get(key));
            }
        }
    }

    @Nullable private RemoteMember getMember() {
        final List<RemoteMember> remoteMembers = Context.getSingleton(Clusters.class).getActiveCluster().get().getMembers();
        for (final RemoteMember remoteMember : remoteMembers)
            if (remoteMember.getName().equals(getNode())) return remoteMember;

        return null;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(NodeAdminForm.class);

    //~ Inner Classes ................................................................................................................................

    public class PropertyTableRow extends PropertyTableRowBase {
        @NotNull @Override public Action editPropertyValue() {
            final Option<PropertyTableRow> current = getPropertyTable().current();
            if (current.isPresent()) setEditProp(true);

            return actions.getDefault();
        }
        @NotNull @Override public Action updatePropertyValue() {
            if (isEditProp()) {
                setEditProp(false);

                final PropertyTableRow current = getPropertyTable().getCurrent();

                final AttributeList attrs = new AttributeList();

                attrs.add(new Attribute(current.getAttrName(), current.getPropValue()));

                final AttributeChangeJmxOperation operation = new AttributeChangeJmxOperation(current.getEndpoint(), attrs);
                final RemoteMember                member    = getMember();
                if (member == null) return actions.getError().withMessage("Unable to connect");
                operation.setEndpoint(member.getJmxEndpoint());
                operation.execute();

                final MethodInvokeJmxOperation apply = new MethodInvokeJmxOperation(current.getEndpoint(), ConsoleConstants.APPLY, null, null);
                apply.setEndpoint(member.getJmxEndpoint());
                apply.execute();
            }
            return actions.getDefault();
        }
    }
}  // end class NodeAdminForm
