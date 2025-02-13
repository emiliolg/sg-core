
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.filter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.BitArray;
import tekgenesis.form.FormInstance;
import tekgenesis.form.ReflectedFormInstance;
import tekgenesis.form.ReflectedMultipleInstance;
import tekgenesis.metadata.form.configuration.DynamicConfig;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.model.KeyMap;
import tekgenesis.model.ValueCount;

import static tekgenesis.form.ReflectedMultipleInstance.RowInstance;

/**
 * Utility class to deal with form filters.
 */
public class FormFilters {

    //~ Constructors .................................................................................................................................

    private FormFilters() {}

    //~ Methods ......................................................................................................................................

    /** Populate filter data for given filters and results. */
    public static void populateFilters(@NotNull final FormInstance<?> form, @NotNull final MultipleWidget f, @NotNull final MultipleWidget r,
                                       @NotNull final Seq<Filter> filters) {
        final ReflectedFormInstance instance = ReflectedFormInstance.wrap(form);

        final Seq<RowInstance> results = Colls.toList(instance.getMultipleInstance(r));

        if (!results.isEmpty()) {
            final Map<FormFilter, List<Object>> all = new LinkedHashMap<>();

            int bits = 0;

            for (final FormFilter filter : filters.filter(FormFilter.class)) {
                final Seq<Object>           options = filter.compute(results);
                final ImmutableList<Object> opts    = options.toList();
                bits += opts.size();
                all.put(filter, opts);
            }

            // todo Initialize "filter section metadata" with proper size! (bits)

            final ReflectedMultipleInstance filterSection = instance.getMultipleInstance(f);
            filterSection.clear();

            for (final RowInstance result : results)
                result.getModel().setFilterBits(bits);  // Init results filter data.

            int offset = 0;

            for (final Map.Entry<FormFilter, List<Object>> entry : all.entrySet()) {
                final List<Object> options = entry.getValue();
                final FormFilter   filter  = entry.getKey();

                if (!options.isEmpty()) {
                    final RowModel row = filterSection.add().getModel();
                    row.setByFieldSlot(TITLE_FIELD, filter.getTitle());

                    final KeyMap keyMap = asKeyOptions(filter, options);
                    row.setOptionsByFieldSlot(DYNAMIC_FIELD, keyMap);

                    filter.configurate(getConfiguration(row));

                    row.setFilterData(bits, offset, filter.isExclusive());

                    // Iterate and flag all results.
                    for (final RowInstance result : results)
                        flag(result, offset, filter, options);

                    offset += keyMap.size();
                }
            }
        }
    }  // end method populateFilters

    /** As key option. Options count will be set later. */
    private static KeyMap asKeyOptions(@NotNull final FormFilter filter, @NotNull final List<Object> options) {
        final KeyMap result = KeyMap.create();
        for (final Object option : options)
            result.put(new ValueCount(filter.label(option), 0), null);
        return result;
    }

    /** Flag model filter bits to each option if applies. */
    private static void flag(@NotNull RowInstance row, int offset, @NotNull FormFilter filter, @NotNull List<Object> options) {
        final BitArray bits = row.getModel().getFilterBits();
        int            bit  = offset;
        for (final Object option : options) {
            if (filter.accepts(row, option)) bits.set(bit, true);
            bit++;
        }
    }

    @NotNull private static DynamicConfig getConfiguration(@NotNull final RowModel row) {
        DynamicConfig config = (DynamicConfig) row.getConfigByFieldSlot(DYNAMIC_FIELD);
        if (config == null) {
            config = new DynamicConfig();
            row.setConfigByFieldSlot(DYNAMIC_FIELD, config);
        }
        return config;
    }

    //~ Static Fields ................................................................................................................................

    private static final int TITLE_FIELD   = 0;
    private static final int DYNAMIC_FIELD = 1;
}  // end class FormFilters
