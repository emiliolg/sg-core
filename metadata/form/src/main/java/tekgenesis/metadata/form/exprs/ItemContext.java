
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.exprs;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.UiModel;

import static tekgenesis.common.core.Option.of;

/**
 * Represents an item context.
 */
public class ItemContext {

    //~ Instance Fields ..............................................................................................................................

    private final int                     item;
    @NotNull private final MultipleWidget multiple;

    //~ Constructors .................................................................................................................................

    /** Creates an item context with the given item and multiple. */
    private ItemContext(@NotNull MultipleWidget multiple, int item) {
        this.item     = item;
        this.multiple = multiple;
    }

    //~ Methods ......................................................................................................................................

    /** Map {@link UiModel ui model} to contextual {@link RowModel item model}. */
    @NotNull public RowModel mapping(Model model) {
        return model.getMultiple(multiple).getRow(item);
    }

    @Override public String toString() {
        return multiple.getName() + "#" + item;
    }

    /** Returns the item. */
    public int getItem() {
        return item;
    }

    /** Returns the table. */
    @NotNull public MultipleWidget getMultiple() {
        return multiple;
    }

    /** Checks the given multiple to the one available here. */
    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    boolean belongsTo(@NotNull final MultipleWidget widget) {
        return multiple.equals(widget);
    }

    //~ Methods ......................................................................................................................................

    /** Create {@link ItemContext item context} given a multiple and an item. */
    @NotNull public static ItemContext createItemContext(@NotNull final MultipleWidget m, final int i) {
        return new ItemContext(m, i);
    }

    /** Create {@link ItemContext item context} given a multiple and a item. */
    @NotNull public static Option<ItemContext> item(@NotNull final MultipleWidget m, final int i) {
        return of(createItemContext(m, i));
    }
}  // end class ItemContext
