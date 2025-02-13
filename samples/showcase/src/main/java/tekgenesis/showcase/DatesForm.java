
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.time.DayOfWeek;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateOnly;
import tekgenesis.form.Action;
import tekgenesis.form.configuration.DateConfiguration;

/**
 * User class for Form: DatesForm
 */
@Generated(value = "tekgenesis/showcase/DateShowcase.mm", date = "1377306634092")
public class DatesForm extends DatesFormBase implements DateOutput {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        this.<DateConfiguration>configuration(Field.DATE_FROM)
            .disableWeekends()
            .disable(DayOfWeek.WEDNESDAY)
            .enabledDates(ImmutableList.of(DateOnly.date(2014, 4, 23)))
            .addStyleToDay(DateOnly.current().addDays(1), "tomorrow");

        this.<DateConfiguration>configuration(Field.DATE_PICKER_FROM)
            .disable(DayOfWeek.WEDNESDAY)
            .enabledDates(ImmutableList.of(DateOnly.date(2014, 4, 23)))
            .addStyleToDay(DateOnly.current().addDays(1), "tomorrow");

        this.<DateConfiguration>configuration(Field.TIME_FROM)
            .disableWeekends()
            .disable(DayOfWeek.WEDNESDAY)
            .enabledDates(ImmutableList.of(DateOnly.date(2014, 4, 23)));

        this.<DateConfiguration>configuration(Field.DOUBLE_DATE_FROM)
            .disableWeekends()
            .disable(DayOfWeek.WEDNESDAY)
            .enabledDates(ImmutableList.of(DateOnly.date(2014, 4, 23)));
    }

    /**
     * Invoked when date_box(dateFrom) value changes Invoked when date_box(dateTo) value changes
     * Invoked when date_time_box(timeFrom) value changes Invoked when date_time_box(timeTo) value
     * changes Invoked when double_date_box(doubleDateFrom) value changes Invoked when
     * double_date_box(doubleDateTo) value changes.
     */
    @NotNull @Override public Action show() {
        sout(this);
        return actions.getDefault();
    }

    private void sout(DateOutput date) {
        if (isDefined(Field.DATE_FROM) && isDefined(Field.DATE_TO)) {
            final StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("\nDateFrom: ").append(date.getDateFrom());
            strBuilder.append("\t \tDateTo: ").append(date.getDateTo());
            System.out.println(strBuilder);
        }

        if (isDefined(Field.TIME_FROM) && isDefined(Field.TIME_TO)) {
            final StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("\nTimeFrom: ").append(date.getTimeFrom());
            strBuilder.append("\t\tTimeTo: ").append(date.getTimeTo());
            System.out.println(strBuilder);
        }

        if (isDefined(Field.DOUBLE_DATE_FROM) && isDefined(Field.DOUBLE_DATE_TO)) {
            final StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("\nDoubleDateFrom: ").append(date.getDoubleDateFrom());
            strBuilder.append("\t\tDoubleDateTo: ").append(date.getDoubleDateTo());
            System.out.println(strBuilder);
        }

        if (isDefined(Field.DATE_COMBO) && isDefined(Field.DATE_COMBO1)) {
            final StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("\nDateCombo: ").append(date.getDateCombo());
            strBuilder.append("\t\tDateCombo1: ").append(date.getDateCombo1());
            System.out.println(strBuilder);
        }
    }
}  // end class DatesForm
