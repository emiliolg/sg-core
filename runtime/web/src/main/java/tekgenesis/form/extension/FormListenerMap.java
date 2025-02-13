
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.extension;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import tekgenesis.form.FormInstance;

/**
 * Form listener map.
 */
public class FormListenerMap<T extends FormInstance<?>> {

    //~ Instance Fields ..............................................................................................................................

    private EnumMap<FormListenerType, List<FormListener<T>>> listeners = null;

    //~ Methods ......................................................................................................................................

    /** Register a Listener. */
    public synchronized void addListener(FormListenerType type, FormListener<T> listener) {
        if (listeners == null) listeners = new EnumMap<>(FormListenerType.class);

        List<FormListener<T>> ls = listeners.get(type);
        if (ls == null) listeners.put(type, ls = new ArrayList<>());
        if (!ls.contains(listener)) ls.add(listener);
    }

    /** Calls all listeners of a certain type. */
    public boolean apply(FormListenerType type, T instance) {
        final List<FormListener<T>> ls;
        return listeners == null || (ls = listeners.get(type)) == null || doApply(ls, instance);
    }
    /** De-Register all Listeners. */
    public synchronized void removeAll() {
        listeners = null;
    }

    /** De-Register all Listeners of a given type. */
    public synchronized void removeAll(FormListenerType type) {
        if (listeners != null) listeners.remove(type);
    }

    /** De-Register a Listener. */
    public synchronized void removeListener(FormListenerType type, FormListener<T> listener) {
        if (listeners != null) {
            final List<FormListener<T>> ls = listeners.get(type);
            if (ls != null) ls.remove(listener);
        }
    }

    private boolean doApply(List<FormListener<T>> ls, T newInstance) {
        return ls.isEmpty() || invokeAll(ls, newInstance);
    }

    private boolean invokeAll(List<FormListener<T>> ls, T instance) {
        for (final FormListener<T> l : ls)
            l.invoke(instance);
        return true;
    }
}  // end class FormListenerMap
