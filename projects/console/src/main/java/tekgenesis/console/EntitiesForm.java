
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
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.form.Action;
import tekgenesis.form.DynamicFormInstance;
import tekgenesis.form.FormTable;
import tekgenesis.sg.ClusterConf;

import static tekgenesis.task.jmx.JmxConstants.ENTITIES;
import static tekgenesis.type.permission.PredefinedPermission.UPDATE;

/**
 * User class for Form: EntitiesForm
 */
public class EntitiesForm extends EntitiesFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when text_field(searchBox) value changes. */
    @NotNull @Override public Action filterEntities() {
        filter(s -> !isDefined(Field.SEARCH_BOX) || (s != null && getSearchBox() != null && s.contains(getSearchBox())));
        return actions.getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void onLoad() {
        filter(null);
        final ClusterConf selected = Context.getSingleton(Clusters.class).getSelectedClusterConf();
        if (selected != null) setClusterName(selected.getName());
        setDisable(!forms.hasPermission(UPDATE));
    }

    private void filter(@Nullable final Predicate<String> predicate) {
        final RemoteMember remoteMember = Context.getSingleton(Clusters.class).getActiveCluster().get().getMembers().get(0);

        // noinspection DuplicateStringLiteralInspection
        List<String> entities = JmxInvokerImpl.invoker(remoteMember.getJmxEndpoint()).mbean(ENTITIES).getAttribute("Entities");

        if (predicate != null) entities = Colls.filter(entities, entity -> entity != null && predicate.test(entity)).toList();

        final FormTable<EntitiesTableRow> entitiesTable = getEntitiesTable();
        entitiesTable.clear();
        for (final String entity : entities)
            entitiesTable.add().setEntityName(entity);
    }

    private RemoteMember getRemoteMember() {
        return Context.getSingleton(Clusters.class).getActiveCluster().get().getMembers().get(0);
    }

    //~ Inner Classes ................................................................................................................................

    public class EntitiesTableRow extends EntitiesTableRowBase {
        /** Invoked when label(clearEntityCache) is clicked. */
        @NotNull @Override public Action clearEntityCache() {
            final RemoteMember remoteMember = getRemoteMember();
            // noinspection DuplicateStringLiteralInspection
            JmxInvokerImpl.invoker(remoteMember.getJmxEndpoint())
                .mbean(ENTITIES)
                .invoke(ConsoleConstants.CLEAR_CACHE, new String[] { String.class.getName() }, new Object[] { getEntityName() });
            return actions.getDefault();
        }

        /** Invoked when label(cookEntities) is clicked. */
        @NotNull @Override public Action fixEntities() {
            final DynamicFormInstance instance = new DynamicFormInstance(EntityFixerDynamicForm.class, getEntityName());
            return actions.navigate(instance);
        }

        /** Invoked when label(rebuildEntityIndex) is clicked. */
        @NotNull @Override public Action rebuildEntityIndex() {
            final RemoteMember remoteMember = getRemoteMember();
            JmxInvokerImpl.invoker(remoteMember.getJmxEndpoint())
                .mbean(ENTITIES)
                .invoke("rebuildIndex", new String[] { String.class.getName() }, new Object[] { getEntityName() });
            return actions.getDefault();
        }
    }
}  // end class EntitiesForm
