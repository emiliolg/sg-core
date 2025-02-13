
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

import tekgenesis.common.core.Times;
import tekgenesis.form.Action;

import static tekgenesis.showcase.TimePickerFormBase.Field.*;

/**
 * User class for Form: TimePickerForm
 */
public class TimePickerForm extends TimePickerFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changed() {
        if (isDefined(TIME_PICKER)) System.out.println("TimePicker = " + getTimePicker());

        if (isDefined(TIME_PICKER_STEP5)) System.out.println("TimePicker5 = " + getTimePickerStep5());

        if (isDefined(TIME_PICKER_STEP15)) System.out.println("TimePicker15 = " + getTimePickerStep15());

        if (isDefined(FROM)) System.out.println("From = " + getFrom());

        if (isDefined(TO)) System.out.println("To = " + getTo());

        return actions.getDefault();
    }

    @NotNull @Override public Action tzAwareValue() {
        if (isDefined(WITH_TZ)) System.out.println(Times.toHoursMinutesString(getWithTz()).asList().mkString(":"));

        return actions.getDefault();
    }
}
