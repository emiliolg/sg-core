
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import java.util.*;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.View;
import tekgenesis.metadata.task.TransactionMode;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.repository.ModelRepository;
import tekgenesis.transaction.Transaction;

/**
 * RefreshViewtaskBase.
 */
public abstract class RefreshRemoteViewTaskBase extends ScheduledTaskInstance {

    //~ Instance Fields ..............................................................................................................................

    private final String cronExpression;

    //~ Constructors .................................................................................................................................

    /** Construct the task. */
    protected RefreshRemoteViewTaskBase(@NotNull ScheduledTask task) {
        super(task);
        cronExpression = DEFAULT_CRON_EXPRESSION;
    }

    //~ Methods ......................................................................................................................................

    /** Refreshing View. */
    @NotNull @Override public Status run() {
        final String domainName = getDomain();
        return domainName == null ? Status.ok() : doRun(domainName);
    }  // end method run

    @Override public int getBatchSize() {
        return 1;
    }

    @NotNull @Override public String getCronExpression() {
        return cronExpression;
    }

    /**  */
    public abstract String getDomain();

    @NotNull @Override public String getExclusionGroup() {
        return "";
    }

    @Override public int getPurgePolicy() {  // noinspection MagicNumber
        return 15;
    }

    /** Get Cron Expression. */
    @NotNull public String getScheduleAfter() {
        return "";
    }

    @NotNull @Override public TransactionMode getTransactionMode() {
        return TransactionMode.ALL;
    }

    @Override boolean manageTransaction() {
        return true;
    }

    @NotNull private Iterable<View> dependenciesFor(final View databaseObject) {
        final ArrayList<View> result = new ArrayList<>();
        for (final Attribute attribute : databaseObject.allAttributes()) {
            for (final DbObject dbo : attribute.asDatabaseObject()) {
                if (!attribute.isMultiple()) result.add(dbo.asView());
            }
        }
        return result;
    }

    private Status doRun(String domainName) {
        final Set<View> views = Context.getSingleton(ModelRepository.class).getModels(domainName, View.class).topologicalSort(this::dependenciesFor);

        getProgressMeter().setItemsToProcess(views.size() + 1);

        final HashMap<String, RefreshViewListener.RefreshResult> refreshed = new HashMap<>();
        final Status                                             status    = refreshViews(views, refreshed);

        getProgressMeter().advance();

        Transaction.getCurrent().ifPresent(Transaction::commit);

        if (!refreshed.isEmpty()) notifyListeners(domainName, refreshed);
        return status;
    }

    private void notifyListeners(String domainName, HashMap<String, RefreshViewListener.RefreshResult> refreshed) {
        final List<RefreshViewListener> refreshViewListener;
        synchronized (lockObject) {
            refreshViewListener = listeners.get(domainName);
        }
        if (refreshViewListener != null) {
            for (final RefreshViewListener viewListener : refreshViewListener)
                viewListener.viewRefreshed(refreshed);
        }
    }

    private Status refreshViews(Set<View> views, HashMap<String, RefreshViewListener.RefreshResult> refreshed) {
        Status status = Status.ok();
        for (final View view : views) {
            if (view.isRemote()) {
                final Option<DbObject> baseEntity = view.getBaseEntity();
                if (baseEntity.isEmpty()) logWarning("No base entity defined for view " + view.getFullName());
                else {
                    status = refreshView(refreshed, view, baseEntity);
                    if (!status.isSuccess()) {
                        getProgressMeter().advance();
                        break;
                    }
                }
            }
            getProgressMeter().advance();
        }
        return status;
    }  // end method refreshViews

    //~ Methods ......................................................................................................................................

    /** Register listener for domain. */
    public static void registerListener(String domainName, RefreshViewListener listener) {
        synchronized (lockObject) {
            List<RefreshViewListener> refreshViewListeners = listeners.get(domainName);
            if (refreshViewListeners == null) refreshViewListeners = new ArrayList<>();
            refreshViewListeners.add(listener);
            listeners.put(domainName, refreshViewListeners);
        }
    }

    /** Un register refresh task listener. */
    public static void unRegisterListener(String domainName, RefreshViewListener listener) {
        final List<RefreshViewListener> refreshViewListeners;
        synchronized (lockObject) {
            refreshViewListeners = listeners.get(domainName);
        }
        if (refreshViewListeners != null) refreshViewListeners.remove(listener);
    }

    private static <T extends EntityInstance<T, K>, K> Status refreshView(final HashMap<String, RefreshViewListener.RefreshResult> refreshed,
                                                                          final View view, final Option<DbObject> baseEntity) {
        return new ViewRefresher<T, K>(view, baseEntity.get()).refresh(refreshed);
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String DEFAULT_CRON_EXPRESSION = "0 0 0/1";

    private static final Object                                     lockObject = new Object();
    private static final HashMap<String, List<RefreshViewListener>> listeners  = new HashMap<>();

    //~ Inner Interfaces .............................................................................................................................

    public interface RefreshViewListener {
        /**
         * Called after refresh task has run. It returns a Map with entity full name and a tuple
         * with the DateTime of the refreshed entities and a boolean indicating if a clean refresh
         * has been performed
         */
        void viewRefreshed(Map<String, RefreshResult> refreshed);

        /** Create a Refresh result. */
        static RefreshResult result(DateTime since, boolean force, String removedKeys) {
            final RefreshResult result = new RefreshResult();
            result.since       = since;
            result.force       = force;
            result.removedKeys = removedKeys;
            return result;
        }

        class RefreshResult {
            private boolean  force;
            private String   removedKeys = null;
            private DateTime since       = null;

            /** Return true if a full refresh has been run. */
            public boolean force() {
                return force;
            }
            /** Return a Seq of keys that have been removed. */
            public Seq<String> removedKeys() {
                return Predefined.isEmpty(removedKeys) ? Colls.emptyIterable() : Colls.immutable(Colls.set(removedKeys.split(",")));
            }

            /** Return refreshed sync date from. */
            public DateTime since() {
                return since;
            }
        }
    }
}  // end class RefreshRemoteViewTaskBase
