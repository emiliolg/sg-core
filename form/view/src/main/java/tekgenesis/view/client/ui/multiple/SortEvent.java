
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.multiple;

import java.util.ArrayList;
import java.util.List;

import tekgenesis.common.core.Tuple;
import tekgenesis.view.client.ui.multiple.SorterLens.SortType;

import static tekgenesis.common.core.Tuple.tuple;

/**
 * Sorter lens event.
 */
public class SortEvent implements LensEvent {

    //~ Instance Fields ..............................................................................................................................

    private final List<Tuple<Integer, SortType>> criteria;

    //~ Constructors .................................................................................................................................

    /** Creates a Sort Lens event. */
    public SortEvent(List<Tuple<Integer, SortType>> criteria) {
        this.criteria = criteria;
    }

    /** Creates a Sort Lens for a column and a sortType. */
    public SortEvent(int c, SortType sortType) {
        criteria = new ArrayList<>();
        criteria.add(tuple(c, sortType));
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean refresh() {
        return true;
    }

    /** Return sort criteria. */
    public List<Tuple<Integer, SortType>> getCriteria() {
        return criteria;
    }
}
