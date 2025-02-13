
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * Car listing form class.
 */
@SuppressWarnings("WeakerAccess")
public class CarListForm extends CarListFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action removeCar() {
        final CarsRow row        = getCars().getCurrent();
        final Car     currentCar = row.isDefined(Field.ID) ? Car.find(row.getId()) : null;
        if (currentCar != null) currentCar.delete();
        getCars().removeCurrent();
        return actions.getDefault();
    }

    //~ Inner Classes ................................................................................................................................

    public class CarsRow extends CarsRowBase {}
}
