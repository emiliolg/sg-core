
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

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.type.Type;
import tekgenesis.view.client.ui.tablefilters.Comparison;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;

/**
 * Filterable lens.
 */
public class FilterLens extends MultipleModelLens.CompoundLens {

    //~ Instance Fields ..............................................................................................................................

    private FilterEvent e = null;

    private final List<Integer> rowMapper;

    //~ Constructors .................................................................................................................................

    public FilterLens() {
        rowMapper = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public int size() {
        return rowMapper.size();
    }

    @Override protected void doReact(LensEvent event) {
        if (event instanceof ItemEvent) cardinalityChanged((ItemEvent) event);

        if (event instanceof FilterEvent) e = (FilterEvent) event;

        if (event.refresh()) doFilter();
    }

    @Override protected Option<Integer> itemToSection(int item) {
        final int i = rowMapper.indexOf(item);
        return i != -1 ? of(i) : empty();
    }

    @Override protected int sectionToItem(int section) {
        return rowMapper.get(section);
    }

    private void cardinalityChanged(ItemEvent event) {
        switch (event.getType()) {
        case ITEM_CREATED:
            e = null;
            break;
        case ITEM_DELETED:
            break;
        }
        doFilter();
    }

    private void doFilter() {
        rowMapper.clear();

        if (e != null) {
            final String     column     = e.getColumn();
            final Comparison comparison = e.getComparison();

            for (int section = 0; section < origin.size(); section++) {
                final RowModel row    = origin.getItemModel(origin.mapSectionToItem(section));
                final Widget   widget = row.widgetByName(column);
                final Type     type   = widget.getType();

                final String  value   = e.getValue();
                final boolean accepts = isEmpty(value) || comparison.test(type, row.get(widget), value);
                if (accepts) rowMapper.add(section);
            }
        }
        else {
            for (int section = 0; section < origin.size(); section++)
                rowMapper.add(section);
        }
    }

    //~ Methods ......................................................................................................................................

    public static CompoundLens create() {
        return new FilterLens();
    }
}  // end class FilterLens
