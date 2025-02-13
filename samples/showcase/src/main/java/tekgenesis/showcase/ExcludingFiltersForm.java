
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.form.FormTable;
import tekgenesis.form.filter.Accepts;
import tekgenesis.form.filter.Filter;
import tekgenesis.form.filter.Options;

/**
 * User class for Form: ExcludingFiltersForm
 */
@Generated(value = "tekgenesis/showcase/ListShowcase.mm", date = "1372944985394")
public class ExcludingFiltersForm extends ExcludingFiltersFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void load() {
        populateItems();

        forms.filter(createFilters());
    }

    private Seq<Filter> createFilters() {
        final List<Filter> result = new ArrayList<Filter>();

        result.add(ITEMS_ROW_FILTERS.createFilter("Capuleti Number", new Options<ItemsRow, Integer>() {
                                            @Override public Seq<Integer> values(@NotNull Seq<ItemsRow> elements) {
                                                return ImmutableList.of(18, 21, 32, 48);
                                            }
                                        }).accepts(new Accepts<ItemsRow, Integer>() {
                                            @Override public boolean apply(@NotNull ItemsRow element, @NotNull Integer number) {
                                                final String label = element.getLabel();
                                                return label.contains("Capuleti") && label.contains(String.valueOf(number));
                                            }
                                        }));

        result.add(
            ITEMS_ROW_FILTERS.createFilter("Montecchi Letter", new Options<ItemsRow, Character>() {
                                     @Override public Seq<Character> values(@NotNull Seq<ItemsRow> elements) {
                                         return ImmutableList.of('A', 'B', 'C');
                                     }
                                 }).accepts(new Accepts<ItemsRow, Character>() {
                                     @Override public boolean apply(@NotNull ItemsRow element, @NotNull Character character) {
                                         final String label = element.getLabel();
                                         return label.contains("Montecchi") && label.contains(String.valueOf(character));
                                     }
                                 }));

        return Colls.seq(result);
    }

    private void populateItems() {
        final FormTable<ItemsRow> items = getItems();
        items.add().setLabel("Capuleti 18");
        items.add().setLabel("Capuleti 21");
        items.add().setLabel("Capuleti 32");
        items.add().setLabel("Montecchi A");
        items.add().setLabel("Montecchi B");
    }

    //~ Inner Classes ................................................................................................................................

    public class FiltersRow extends FiltersRowBase {}

    public class ItemsRow extends ItemsRowBase {}
}  // end class ExcludingFiltersForm
