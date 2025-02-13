
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.Random;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;

import static tekgenesis.showcase.CarFilterForm.generateFilters;

/**
 * User class for Form: CarFilterSectionForm
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class CarFilterSectionForm extends CarFilterSectionFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when button(filter) is clicked. */
    @NotNull @Override public Action filter() {
        // Apply filter
        forms.filter(generateFilters());

        return actions.getDefault().withMessage("Filtering enabled");
    }

    /** Invoked when the form is loaded. */
    @Override public void load() {
        final FormTable<CarsRow> cars = getCars();
        Car.forEach(car -> cars.add().populate(car));

        forms.filter(generateFilters());
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

    public class FiltersRow extends FiltersRowBase {}
}  // end class CarFilterSectionForm
