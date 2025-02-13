
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.filter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.BitArray;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FilterData;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;
import tekgenesis.model.ValueCount;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.metadata.form.model.FilterData.NONE;

/**
 * Utility client friendly class to deal with form filters.
 */
public class FormFiltersClient {

    //~ Constructors .................................................................................................................................

    private FormFiltersClient() {}

    //~ Methods ......................................................................................................................................

    /** Match flag by filter segment. */
    public static boolean accepts(@NotNull BitArray flag, @NotNull MultipleModel filters) {
        for (final RowModel filter : filters) {
            final BitArray filterBits = filter.getFilterBits();
            if (!filterBits.isEmpty()) {
                final BitArray and = flag.and(filterBits);
                if (isExclusive(filter)) {
                    if (!and.eq(filterBits)) return false;
                }
                else {
                    if (and.isEmpty()) return false;
                }
            }
        }
        return true;
    }

    /** True if all options counts are zero. We assume KeyMap of ValueCount!. */
    public static boolean hasZeroOptionsCount(@NotNull final KeyMap options) {
        for (final Object option : options.keySet()) {
            final ValueCount count = (ValueCount) option;
            if (!count.isZero()) return false;
        }
        return true;
    }

    /** Update widgets count given the triggering filter. Watch out!! O(N*M) */
    public static void updateCount(@NotNull MultipleModel filters, @NotNull MultipleModel results, @NotNull Widget widget,
                                   @NotNull Option<RowModel> current) {
        final Iterable<RowModel> accepted = filterAccepted(results);

        for (final RowModel filter : filters) {
            final int                                offset = filter.getFilterOffset();
            final Set<Map.Entry<ValueCount, String>> opts   = opts(widget, filter);

            final KeyMap updated = current.isPresent() && filter == current.get()
                                   ? updateOptions(results, offset, opts, data -> data != null && accepts(data.getBits(), filters, current.get()))
                                   : updateOptions(accepted, offset, opts, o -> true);

            filter.setOptions(widget, updated);
        }
    }

    /** Update given mask for specified widget. */
    public static void updateMask(@NotNull final RowModel current, @NotNull final Widget widget) {
        final BitArray bits   = current.getFilterBits();
        int            offset = current.getFilterOffset();

        final Set<Object>      options = current.getOptions(widget).keySet();
        final Iterable<Object> values  = current.getArray(widget);

        if (!isEmpty(values)) {
            final List<Object> selections = Colls.toList(values);
            // Flag filter pairing options and selections
            for (final Object option : options) {
                final ValueCount valueCount = (ValueCount) option;
                bits.set(offset++, selections.contains(valueCount.getValue()));
            }
        }
        else {
            // Flag all filter options as false
            final int bound = offset + options.size();
            for (; offset < bound; offset++)
                bits.set(offset, false);
        }
    }

    /** Match flag by filter segment, skips current check. */
    private static boolean accepts(@NotNull BitArray flag, @NotNull MultipleModel filters, @NotNull RowModel current) {
        for (final RowModel filter : filters) {
            final BitArray filterBits = filter.getFilterBits();
            if (filter != current && !filterBits.isEmpty() && flag.and(filterBits).isEmpty()) return false;
        }
        return true;
    }

    private static Iterable<RowModel> filterAccepted(@NotNull final MultipleModel results) {
        return seq(results).filter(FormFiltersClient::isAccepted).toList();
    }

    private static Set<Map.Entry<ValueCount, String>> opts(@NotNull Widget widget, @NotNull RowModel row) {
        return cast(row.getOptions(widget).entrySet());
    }

    private static KeyMap updateOptions(Iterable<RowModel> results, int initial, Set<Map.Entry<ValueCount, String>> opts,
                                        Predicate<FilterData> shouldCount) {
        int          offset  = initial;
        final KeyMap updated = KeyMap.create();

        for (final Map.Entry<ValueCount, String> entry : opts) {
            int count = 0;

            for (final RowModel row : results) {
                final FilterData data = row.getFilterData();
                if (data != NONE && shouldCount.test(data) && data.getBits().get(offset)) count++;
            }

            updated.put(entry.getKey().withCount(count), null);
            offset++;
        }

        return updated;
    }

    private static boolean isAccepted(final RowModel row) {
        if (row == null) return false;

        final FilterData data = row.getFilterData();
        return data != NONE && data.isAccepted();
    }

    private static boolean isExclusive(final RowModel row) {
        if (row == null) return false;

        final FilterData data = row.getFilterData();
        return data != NONE && data.isExclusive();
    }
}  // end class FormFiltersClient
