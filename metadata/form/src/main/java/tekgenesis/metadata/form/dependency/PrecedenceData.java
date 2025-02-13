
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.dependency;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;

/**
 * Holder for reference to precedence mapping e.g. [J <-- B, C, D, T]
 */
@SuppressWarnings("FieldMayBeFinal")
public class PrecedenceData implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    @SuppressWarnings("GwtInconsistentSerializableClass")  // lie!!!
    private Map<String, Iterable<String>> recomputes;

    //~ Constructors .................................................................................................................................

    /** Creates empty data holder. */
    PrecedenceData() {
        recomputes = new LinkedHashMap<>();
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if this widget has reference precedence. */
    public boolean contains(final String widget) {
        return recomputes.containsKey(widget);
    }

    /** Returns a reference precedence. */
    @NotNull public Iterable<String> getRecomputeList(final String reference) {
        final Iterable<String> result = recomputes.get(reference);
        return result == null ? Colls.emptyIterable() : result;
    }

    /** Add recompute list of widget for a given one. */
    void addRecomputeData(final String widget, final Iterable<String> recompute) {
        recomputes.put(widget, recompute);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 5552990884740821093L;
}
