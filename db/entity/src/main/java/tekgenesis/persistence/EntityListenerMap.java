
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.*;

import org.jetbrains.annotations.Nullable;

import static java.util.Collections.emptyList;

import static tekgenesis.common.collections.Colls.exists;

class EntityListenerMap<T extends EntityInstance<T, ?>> {

    //~ Instance Fields ..............................................................................................................................

    private final ThreadLocal<Set<T>> activeInstances = ThreadLocal.withInitial(HashSet::new);

    private boolean hasUpdateListener;

    private EnumMap<EntityListenerType, List<EntityListener<T>>> listeners = null;

    //~ Methods ......................................................................................................................................

    /** Register a Listener. */
    synchronized void add(EntityListenerType type, EntityListener<T> listener) {
        if (listeners == null) listeners = new EnumMap<>(EntityListenerType.class);

        List<EntityListener<T>> ls = listeners.get(type);
        if (ls == null) listeners.put(type, ls = new ArrayList<>());
        if (!ls.contains(listener)) ls.add(listener);
        if (listener instanceof EntityListener.Update) hasUpdateListener = true;
    }
    boolean apply(EnumSet<EntityListenerType> types, T instance) {
        return doApply(getAll(types), null, instance);
    }
    boolean apply(EntityListenerType type, T instance) {
        final List<EntityListener<T>> ls;
        return listeners == null || (ls = listeners.get(type)) == null || doApply(ls, null, instance);
    }

    boolean apply(EnumSet<EntityListenerType> types, @Nullable T oldInstance, T newInstance) {
        return doApply(getAll(types), oldInstance, newInstance);
    }

    boolean hasUpdateListener() {
        return hasUpdateListener;
    }

    /** De-Register a Listener. */
    synchronized void remove(EntityListenerType type, EntityListener<T> listener) {
        if (listeners != null) {
            final List<EntityListener<T>> ls = listeners.get(type);
            if (ls != null) {
                ls.remove(listener);
                if (listener instanceof EntityListener.Update) hasUpdateListener = exists(ls, l -> l instanceof EntityListener.Update);
            }
        }
    }
    /** De-Register all Listeners. */
    synchronized void removeAll() {
        listeners = null;
    }

    /** De-Register all Listeners of a given type. */
    synchronized void removeAll(EntityListenerType type) {
        if (listeners != null) listeners.remove(type);
    }

    private boolean doApply(List<EntityListener<T>> ls, @Nullable T oldInstance, T newInstance) {
        if (ls.isEmpty()) return true;

        final Set<T> actives = activeInstances.get();

        if (!actives.add(newInstance)) return false;

        try {
            return invokeAll(ls, oldInstance, newInstance);
        }
        finally {
            actives.remove(newInstance);
        }
    }

    private boolean invokeAll(List<EntityListener<T>> ls, @Nullable T oldInstance, T instance) {
        for (final EntityListener<T> l : ls) {
            final boolean vetted = !(oldInstance != null && l instanceof EntityListener.Update
                                     ? ((EntityListener.Update<T>) l).invoke(oldInstance, instance)
                                     : l.invoke(instance));
            if (vetted) return false;
        }
        return true;
    }

    private List<EntityListener<T>> getAll(EnumSet<EntityListenerType> types) {
        if (listeners == null) return emptyList();
        List<EntityListener<T>> result = emptyList();
        int                     sets   = 0;

        for (final EntityListenerType type : types) {
            final List<EntityListener<T>> ls = listeners.get(type);
            if (ls != null) {
                if (sets == 0) result = ls;
                else {
                    if (sets == 1) result = new ArrayList<>(result);
                    result.addAll(ls);
                }
                ++sets;
            }
        }
        return result;
    }  // end method getAll
}  // end class EntityListenerMap
