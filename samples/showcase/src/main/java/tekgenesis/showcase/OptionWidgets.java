
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.form.Action;

import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.showcase.g.MakeTable.MAKE;

/**
 * User class for Form: OptionWidgets
 */
public class OptionWidgets extends OptionWidgetsBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override
    @SuppressWarnings("MagicNumber")
    public void load() {
        // Real
        final Iterable<Double> doubles = ImmutableList.of(3.0, 1.5, .75, .375, .1875);
        setRealComboOptions(doubles);
        setRealTagsComboOptions(doubles);
        setRealCheckBoxGroupOptions(doubles);
        setRealRadiogroupOptions(doubles);
        setRealListboxOptions(doubles);

        // Decimal
        final Iterable<BigDecimal> decimals = ImmutableList.of(new BigDecimal(3),
                new BigDecimal(1.5),
                new BigDecimal(.75),
                new BigDecimal(.375),
                new BigDecimal(.1875));
        setDecimalComboOptions(decimals);
        setDecimalTagsComboOptions(decimals);
        setDecimalCheckBoxGroupOptions(decimals);
        setDecimalRadiogroupOptions(decimals);
        setDecimalListboxOptions(decimals);

        // String
        final Iterable<String> strings = ImmutableList.of("Everest", "K2", "Kangchenjunga", "Aconcagua");
        setStringComboOptions(strings);
        setStringTagsComboOptions(strings);
        setStringCheckBoxGroupOptions(strings);
        setStringRadiogroupOptions(strings);
        setStringListboxOptions(strings);

        // Enum (default populate)

        // Entity
        final ImmutableList<Make> entities = selectFrom(MAKE).list();
        setEntityComboOptions(entities);
        setEntityTagsComboOptions(entities);
        setEntityCheckBoxGroupOptions(entities);
        setEntityRadiogroupOptions(entities);
        setEntityListboxOptions(entities);
    }

    @NotNull @Override public Action some() {
        return actions.getDefault();
    }
}  // end class OptionWidgets
