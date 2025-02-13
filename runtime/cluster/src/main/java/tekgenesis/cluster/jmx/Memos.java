
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx;

import java.io.Serializable;

import tekgenesis.cluster.jmx.notification.Notifier;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.util.Memo;
import tekgenesis.common.util.MemoMap;

import static tekgenesis.task.jmx.JmxConstants.MEMOS;

/**
 * Memos MBean.
 */
public class Memos implements MemosMBean {

    //~ Methods ......................................................................................................................................

    @Override public void force(String memo) {
        Memo.forceRecalculation(memo);
    }
    @Override public void force(String memo, Object key) {
        MemoMap.forceRecalculation(memo, key);
    }
    @Override public ImmutableList<String> getMemos() {
        return Memo.allMemoNames();
    }

    //~ Methods ......................................................................................................................................

    /** force memo recalculation in all nodes of the cluster. */
    public static void forceCluster(String memo) {
        // noinspection DuplicateStringLiteralInspection
        Notifier.broadcast(new MethodInvokeJmxOperation(MEMOS, "force", new String[] { String.class.getName() }, new Object[] { memo }));
    }

    /** force memo with key recalculation in all nodes of the cluster. */
    public static void forceCluster(String memo, Serializable key) {
        // noinspection DuplicateStringLiteralInspection
        Notifier.broadcast(
            new MethodInvokeJmxOperation(MEMOS,
                "force",
                new String[] { String.class.getName(), Serializable.class.getName() },
                new Object[] { memo, key }));
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1929356918663071416L;
}
