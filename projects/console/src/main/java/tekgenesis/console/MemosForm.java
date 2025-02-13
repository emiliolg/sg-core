
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
import java.util.function.Predicate;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cluster.Clusters;
import tekgenesis.cluster.jmx.Memos;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.cluster.jmx.util.JsonDataSerializer;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.common.util.Memo;
import tekgenesis.common.util.SingletonMemo;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.sg.ClusterConf;

import static tekgenesis.common.util.Reflection.getFieldValue;
import static tekgenesis.common.util.Reflection.getPrivateField;
import static tekgenesis.task.jmx.JmxConstants.MEMOS;
import static tekgenesis.type.permission.PredefinedPermission.UPDATE;

/**
 * User class for Form: MemosForm
 */
public class MemosForm extends MemosFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action closeDialog() {
        setMemoValueDialog(false);
        return actions.getDefault();
    }

    /** Invoked when text_field(searchBox) value changes. */
    @NotNull @Override public Action filterMemos() {
        filter(s -> !isDefined(MemosFormBase.Field.SEARCH_BOX) || (s != null && getSearchBox() != null && s.contains(getSearchBox())));
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
        List<String> memos = JmxInvokerImpl.invoker(remoteMember.getJmxEndpoint()).mbean(MEMOS).getAttribute("Memos");

        if (predicate != null) memos = Colls.filter(memos, entity -> entity != null && predicate.test(entity)).toList();

        final FormTable<MemosRow> memosTable = getMemos();
        memosTable.clear();
        for (final String m : memos)
            memosTable.add().setName(m);
    }

    private RemoteMember getRemoteMember() {
        return Context.getSingleton(Clusters.class).getActiveCluster().get().getMembers().get(0);
    }

    //~ Inner Classes ................................................................................................................................

    public class MemosRow extends MemosRowBase {
        /** Invoked when label(forceMemo) is clicked. */
        @NotNull @Override public Action forceMemo() {
            Memos.forceCluster(getName());
            return actions.getDefault();
        }

        @NotNull @Override public Action seeMemoValue() {
            final Option<Memo<?, ?>> first = Memo.allMemos().filter((p) -> p.getClass().getCanonicalName().equals(getName())).getFirst();
            final Memo<?, ?>         memo  = first.get();
            try {
                final String value;
                if (memo instanceof SingletonMemo) {
                    final Object o = ((SingletonMemo<?, ?>) memo).get();
                    value = JsonDataSerializer.json(o);
                }
                else {
                    final Map<?, ?> map = getFieldValue(memo, getPrivateField(memo, "map"));
                    value = JsonDataSerializer.json(map);
                }
                setMemoFqn(getName());
                setMemoValue(value);
                setMemoValueDialog(true);
                return actions.getDefault();
            }
            catch (final JsonProcessingException e) {
                logger().error("Unable to serialize memo value", e);
                return actions.getError().withMessage("Unable to retrieve memos value");
            }
        }
    }
}  // end class MemosForm
