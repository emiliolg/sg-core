
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Tuple;
import tekgenesis.form.configuration.MapConfiguration;
import tekgenesis.form.filter.Filter;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Tuple.tuple;

/**
 * User class for form: CarFilterMapForm
 */
public class CarFilterMapForm extends CarFilterMapFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void load() {
        this.<MapConfiguration>configuration(Field.CARS).width(0).height(MAP_HEIGHT);

        Car.list().forEach(c -> {
            final Tuple<Double, Double> rndCoords = getRndCoords();
            getCars().add().setMake(c.getMake()).setModel(c.getModel()).setLat(rndCoords.first()).setLng(rndCoords.second());
        });

        forms.filter(generateFilters());
    }

    private Seq<? extends Filter> generateFilters() {
        return listOf(CARS_ROW_FILTERS.make.single(), CARS_ROW_FILTERS.model);
    }

    private Tuple<Double, Double> getRndCoords() {
        return tuple(MIN_LAT + Math.random() * ((MAX_LAT - MIN_LAT) + 1), Math.random() * (MAX_LON + 1));
    }

    //~ Static Fields ................................................................................................................................

    private static final int    MAP_HEIGHT = 600;
    private static final double MIN_LAT    = -90.00;
    private static final double MAX_LAT    = 90.00;
    private static final double MAX_LON    = 180.00;

    //~ Inner Classes ................................................................................................................................

    public class CarsRow extends CarsRowBase {}

    public class FiltersRow extends FiltersRowBase {}
}
