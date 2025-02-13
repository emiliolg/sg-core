
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
import java.util.EnumSet;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.form.Action;
import tekgenesis.form.filter.Filter;

import static java.util.EnumSet.allOf;
import static java.util.EnumSet.noneOf;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.form.filter.Rounder.module;
import static tekgenesis.showcase.Engine.GAS;
import static tekgenesis.showcase.Transmission.AUTOMATIC;
import static tekgenesis.showcase.Transmission.MANUMATIC;

/** User class for form: ListFilterCars */
//J-
public class ListFilterCars
    extends ListFilterCarsBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the form is loaded */
    @Override public void load() {
        lazyLoadCars(0, 15);

        forms.filter(generateFilters());
    }

    @Nullable @Override public Action lazyLoadCars(int offset, int limit) {
        super.lazyLoadCars(offset, limit);

        forms.filter(generateFilters());
        return actions().getDefault();
    }

    @SuppressWarnings({"MagicNumber", "DuplicateStringLiteralInspection", "Duplicates"})
    private static Seq<Filter> generateFilters() {
        final ListFilterCarsBase.CarsFilters filters = CARS_ROW_FILTERS;

        final Filter origin = filters.createFilter("Origin",
                elements -> {
                    final EnumSet<Country> countries = noneOf(Country.class);
                    for (final VehicleRow row : elements)
                        countries.add(row.getMake().getOrigin());
                    return seq(countries);
                }).accepts((element, option) ->
                element.getMake().getOrigin() == option)
                .labels(Country::label);

        final Filter engine = filters.engine.labels(option -> option.label() + (option == GAS ? " Unleaded" : ""));

        final Filter price = filters.price.ranges(6, module(new BigDecimal(10000)));

        final Filter transmission = filters.createFilter("Transmission", elements ->
                seq(allOf(Transmission.class)))
                .accepts((element, option) ->
                        element.getTransmission() == option ||
                                (option == MANUMATIC && element.getTransmission() == AUTOMATIC
                                        && "Audi".equals(element.getMake().getName())))
                .labels(Transmission::label);

        // noinspection unchecked
        return listOf(origin, filters.make.single(), filters.model, engine, filters.year.steps(2), price, transmission.exclusive());
    }  // end method generateFilters

    //~ Inner Classes ............................................................................................................

    public class FiltersRow extends FiltersRowBase {
    }

    public class CarsRow extends CarsRowBase implements VehicleRow {
    }
}
//J+
