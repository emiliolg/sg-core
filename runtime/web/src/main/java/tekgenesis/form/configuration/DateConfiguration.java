
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

/**
 * Configuration for Date widgets.
 */
@SuppressWarnings("UnusedReturnValue")
public interface DateConfiguration extends WidgetConfiguration {

    //~ Methods ......................................................................................................................................

    /** Define a style class for a day. */
    @NotNull DateConfiguration addStyleToDay(DateOnly date, String styleClass);

    /** Disables a day of the week. */
    @NotNull DateConfiguration disable(@NotNull final DayOfWeek day);

    /** Disables days. */
    @NotNull DateConfiguration disableDates(List<DateOnly> dates);

    /** Disables Saturdays. */
    @NotNull DateConfiguration disableSaturdays();

    /** Disables Sundays. */
    @NotNull DateConfiguration disableSundays();

    /** Disables Weekends. */
    @NotNull DateConfiguration disableWeekends();

    /** Enable days. */
    @NotNull DateConfiguration enabledDates(List<DateOnly> dates);

    /** Resets this configuration. */
    @NotNull DateConfiguration reset();
}
