
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.configuration;

import java.time.DayOfWeek;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.DateOnly;
import tekgenesis.metadata.form.configuration.DateConfig;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;

class DateConfigurationImpl extends AbstractWidgetConfiguration<DateConfig> implements DateConfiguration {

    //~ Constructors .................................................................................................................................

    DateConfigurationImpl(@NotNull final Model model, @NotNull final Widget widget) {
        super(model, widget);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public DateConfiguration addStyleToDay(DateOnly date, String styleClass) {
        config().addStyleToDay(date, styleClass);
        return this;
    }

    @NotNull @Override public DateConfiguration disable(@NotNull final DayOfWeek day) {
        config().disable(day);
        return this;
    }

    @NotNull @Override public DateConfiguration disableDates(List<DateOnly> dates) {
        config().disableDates(dates);
        return this;
    }

    @NotNull @Override public DateConfiguration disableSaturdays() {
        config().disableSaturdays();
        return this;
    }

    @NotNull @Override public DateConfiguration disableSundays() {
        config().disableSundays();
        return this;
    }

    @NotNull @Override public DateConfiguration disableWeekends() {
        config().disableWeekends();
        return this;
    }

    @NotNull @Override public DateConfiguration enabledDates(List<DateOnly> dates) {
        config().enabledDates(dates);
        return this;
    }

    @NotNull @Override public DateConfiguration reset() {
        config().reset();
        return this;
    }

    @NotNull @Override DateConfig createConfig() {
        return new DateConfig();
    }
}  // end class DateConfigurationImpl
