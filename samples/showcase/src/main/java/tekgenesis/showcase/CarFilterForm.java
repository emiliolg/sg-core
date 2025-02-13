
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
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Range;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.form.SwipeLoader;
import tekgenesis.form.filter.Filter;
import tekgenesis.form.filter.Rounder;

import static tekgenesis.common.collections.Colls.listOf;

/**
 * User class for Form: CarFilterForm
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class CarFilterForm extends CarFilterFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action filter() {
        // Apply filter
        forms.filter(generateFilters());

        return actions.getDefault().withMessage("Filtering enabled");
    }

    @Override public void load() {
        final FormTable<CarsRow> cars = getCars();
        Car.forEach(car -> cars.add().populate(car));

        forms.filter(generateFilters());
    }

    @NotNull @Override public Action navigate() {
        return actions.navigate(ClassroomForm.class);
    }

    @NotNull @Override public Action rowClicked() {
        return actions.swipe(CarSwipper.class, getCars()).fetchSize(3).cyclic();
    }

    //~ Methods ......................................................................................................................................

    @SuppressWarnings({ "MagicNumber", "DuplicateStringLiteralInspection", "Duplicates" })
    static Seq<Filter> generateFilters() {
        final CarsFilters filters = CARS_ROW_FILTERS;

        final Filter origin = filters.createFilter("Origin",
                    elements -> {
                        final EnumSet<Country> countries = EnumSet.noneOf(Country.class);
                        for (final VehicleRow row : elements)
                            countries.add(row.getMake().getOrigin());
                        return Colls.seq(countries);
                    }).accepts((element, option) ->
                    element.getMake().getOrigin() == option)
                              .labels(Country::label);

        final Filter engine = filters.engine.labels(option -> option.label() + (option == Engine.GAS ? " Unleaded" : ""));

        final Filter price = filters.price.ranges(6, Rounder.module(new BigDecimal(10000)));

        final Filter publication = filters.publication.custom(elements -> {
                    final DateOnly              current = DateOnly.current();
                    final List<Range<DateOnly>> result  = new ArrayList<>();
                    result.add(Range.atLeast(current.addWeeks(-1)));
                    result.add(Range.closed(current.addMonths(-1), current.addWeeks(-1)));
                    result.add(Range.atMost(current.addMonths(-1)));
                    return Colls.seq(result);
                }).accepts((element, range) -> range.contains(element)).labels(CarFilterForm::label);

        final Filter transmission = filters.createFilter("Transmission", elements ->
                        Colls.seq(EnumSet.allOf(Transmission.class)))
                                    .accepts((element, option) ->
                        element.getTransmission() == option ||
                        (option == Transmission.MANUMATIC && element.getTransmission() == Transmission.AUTOMATIC &&
                            "Audi".equals(element.getMake().getName())))
                                    .labels(Transmission::label);

        // noinspection unchecked
        return listOf(origin, filters.make.single(), filters.model, publication, engine, filters.year.steps(2), price, transmission.exclusive());
    }  // end method generateFilters

    @NotNull private static String label(final Range<DateOnly> option) {
        return option.contains(DateOnly.current()) ? "Last Week"
                                                   : option.contains(DateOnly.current().addWeeks(-1)) ? "Last Month" : "More than a month ago";
    }

    //~ Inner Classes ................................................................................................................................

    public class CarsRow extends CarsRowBase implements VehicleRow {
        @Override
        @SuppressWarnings("MagicNumber")
        public void populate(@NotNull Car car) {
            super.populate(car);
            setPublication(DateOnly.current().addDays(-new Random(car.getMileage()).nextInt(80)));
            setExpiration(DateTime.current().addHours(1));
        }
    }  // end class CarsRow

    public class CarSwipper implements SwipeLoader<CarForm> {
        @Override public CarForm load(int index) {
            return forms.initialize(CarForm.class, getCars().get(index).getId() + "");
        }
    }

    public class FiltersRow extends FiltersRowBase {}
}  // end class CarFilterForm
