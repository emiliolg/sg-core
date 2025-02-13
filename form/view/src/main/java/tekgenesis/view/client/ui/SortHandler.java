
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Tuple;
import tekgenesis.view.client.ui.multiple.SorterLens;

import static tekgenesis.common.core.Tuple.tuple;

/**
 * A table sort handler that accumulates sort criteria.
 */
public abstract class SortHandler {

    //~ Instance Fields ..............................................................................................................................

    private final List<Tuple<Integer, SorterLens.SortType>> criteria = new ArrayList<>();

    //~ Methods ......................................................................................................................................

    abstract void notifySort(@NotNull final List<Tuple<Integer, SorterLens.SortType>> criteria);

    /** Called when user chooses to sort a column. */
    void sorted(boolean add, int column, SorterLens.SortType type) {
        if (add) {
            final int lastIndex = criteria.size() - 1;
            if (criteria.get(lastIndex).first().equals(column)) criteria.remove(lastIndex);
        }
        else criteria.clear();

        criteria.add(tuple(column, type));

        notifySort(criteria);
    }
}
