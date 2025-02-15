
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package com.google.gwt.user.client.ui.datepicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;

import org.jetbrains.annotations.Nullable;

/**
 * {@link TimePicker} widget to enter the time part of a date using spinners.
 */
@SuppressWarnings("ConstructorWithTooManyParameters")
public class TimePicker extends Composite implements HasValueChangeHandlers<Date>, HasValue<Date> {

    //~ Instance Fields ..............................................................................................................................

    private long    dateInMillis;
    private boolean enabled = true;

    private final List<TimeSpinner> timeSpinners = new ArrayList<>();

    //~ Constructors .................................................................................................................................

    /** @param  use24Hours  if set to true the {@link TimePicker} will use 24h format */
    @SuppressWarnings("WeakerAccess")  // component
    public TimePicker(boolean use24Hours, boolean midnightAs24) {
        this(new Date(), use24Hours, midnightAs24);
    }

    /**
     * @param  date        the date providing the initial time to display
     * @param  use24Hours  if set to true the {@link TimePicker} will use 24h format
     */
    @SuppressWarnings("WeakerAccess")  // component
    public TimePicker(Date date, boolean use24Hours, boolean midnightAs24) {
        this(date,
            use24Hours ? null : DateTimeFormat.getFormat("aa"),
            use24Hours ? midnightAs24 ? DateTimeFormat.getFormat("kk") : DateTimeFormat.getFormat("HH")
                       : midnightAs24 ? DateTimeFormat.getFormat("KK") : DateTimeFormat.getFormat("hh"),
            DateTimeFormat.getFormat("mm"));
    }

    /**
     * @param  date           the date providing the initial time to display
     * @param  amPmFormat     the format to display AM/PM. Can be null to hide AM/PM field
     * @param  hoursFormat    the format to display the hours. Can be null to hide hours field
     * @param  minutesFormat  the format to display the minutes. Can be null to hide minutes field
     */
    @SuppressWarnings("WeakerAccess")  // component
    public TimePicker(Date date, @Nullable DateTimeFormat amPmFormat, DateTimeFormat hoursFormat, DateTimeFormat minutesFormat) {
        this(date, amPmFormat, hoursFormat, minutesFormat, null, null, null);
    }

    /**
     * @param  date           the date providing the initial time to display
     * @param  amPmFormat     the format to display AM/PM. Can be null to hide AM/PM field
     * @param  hoursFormat    the format to display the hours. Can be null to hide hours field
     * @param  minutesFormat  the format to display the minutes. Can be null to hide minutes field
     * @param  secondsFormat  the format to display the seconds. Can be null to seconds field
     */
    @SuppressWarnings("WeakerAccess")  // component
    public TimePicker(Date date, DateTimeFormat amPmFormat, DateTimeFormat hoursFormat, DateTimeFormat minutesFormat, DateTimeFormat secondsFormat) {
        this(date, amPmFormat, hoursFormat, minutesFormat, secondsFormat, null, null);
    }

    /**
     * @param  date           the date providing the initial time to display
     * @param  amPmFormat     the format to display AM/PM. Can be null to hide AM/PM field
     * @param  hoursFormat    the format to display the hours. Can be null to hide hours field
     * @param  minutesFormat  the format to display the minutes. Can be null to hide minutes field
     * @param  secondsFormat  the format to display the seconds. Can be null to seconds field
     * @param  styles         styles to be used by this TimePicker instance
     * @param  images         images to be used by all nested Spinner widgets
     */
    @SuppressWarnings("WeakerAccess")  // component
    public TimePicker(Date date, @Nullable DateTimeFormat amPmFormat, DateTimeFormat hoursFormat, DateTimeFormat minutesFormat,
                      @Nullable DateTimeFormat secondsFormat, @Nullable ValueSpinner.ValueSpinnerResources styles,
                      @Nullable Spinner.SpinnerResources images) {
        dateInMillis = date.getTime();
        final HorizontalPanel horizontalPanel = new HorizontalPanel();
        // noinspection GWTStyleCheck
        horizontalPanel.setStylePrimaryName("gwt-TimePicker");
        if (amPmFormat != null) {
            final TimeSpinner amPmSpinner = new TimeSpinner(date, amPmFormat, HALF_DAY_IN_MS, styles, images);
            timeSpinners.add(amPmSpinner);
            horizontalPanel.add(amPmSpinner);
        }
        if (hoursFormat != null) {
            final TimeSpinner hoursSpinner = new TimeSpinner(date, hoursFormat, HOUR_IN_MILLIS, styles, images);
            timeSpinners.add(hoursSpinner);
            horizontalPanel.add(hoursSpinner);
        }
        if (minutesFormat != null) {
            final TimeSpinner minutesSpinner = new TimeSpinner(date, minutesFormat, MINUTE_IN_MILLIS, styles, images);
            timeSpinners.add(minutesSpinner);
            horizontalPanel.add(minutesSpinner);
        }
        if (secondsFormat != null) {
            final TimeSpinner secondsSpinner = new TimeSpinner(date, secondsFormat, SECOND_IN_MILLIS, styles, images);
            timeSpinners.add(secondsSpinner);
            horizontalPanel.add(secondsSpinner);
        }
        for (final TimeSpinner timeSpinner : timeSpinners) {
            for (final TimeSpinner nestedSpinner : timeSpinners) {
                if (nestedSpinner != timeSpinner) timeSpinner.getSpinner().addSpinnerListener(nestedSpinner.getSpinnerListener());
            }
            timeSpinner.getSpinner().addSpinnerListener(value -> ValueChangeEvent.fireIfNotEqual(this, new Date(dateInMillis), new Date(value)));
        }
        initWidget(horizontalPanel);
    }  // end ctor TimePicker

    //~ Methods ......................................................................................................................................

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Date> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /** @return  Gets whether this widget is enabled */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param  date  the date to be set. Only the date part will be set, the time part will not be
     *               affected
     */
    public void setDate(Date date) {
        // Only change the date part, leave time part untouched
        dateInMillis = (long) ((Math.floor(date.getTime() / DAY_IN_MS)) * DAY_IN_MS) + dateInMillis % DAY_IN_MS;
        for (final TimeSpinner spinner : timeSpinners)
            spinner.getSpinner().setValue(dateInMillis, false);
    }

    /**
     * Sets whether this widget is enabled.
     *
     * @param  enabled  true to enable the widget, false to disable it
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        for (final TimeSpinner spinner : timeSpinners)
            spinner.setEnabled(enabled);
    }

    /** @return  the date specified by this {@link TimePicker} */
    public Date getValue() {
        return new Date(dateInMillis);
    }

    /** @param  date  the date to be set. Both date and time part will be set */
    public void setValue(Date date) {
        setValue(date, true);
    }

    @Override public void setValue(Date date, boolean fireEvents) {
        dateInMillis = date == null ? new Date().getTime() : date.getTime();
        for (final TimeSpinner spinner : timeSpinners)
            spinner.getSpinner().setValue(dateInMillis, fireEvents);
    }

    //~ Static Fields ................................................................................................................................

    private static final int SECOND_IN_MILLIS = 1000;
    private static final int MINUTE_IN_MILLIS = 60000;
    private static final int HOUR_IN_MILLIS   = 3600000;
    private static final int HALF_DAY_IN_MS   = 43200000;
    private static final int DAY_IN_MS        = 86400000;

    //~ Inner Classes ................................................................................................................................

    private class TimeSpinner extends ValueSpinner {
        private final DateTimeFormat dateTimeFormat;

        public TimeSpinner(Date date, DateTimeFormat dateTimeFormat, int step, @Nullable ValueSpinnerResources styles,
                           @Nullable Spinner.SpinnerResources images) {
            super(date.getTime(), styles, images);
            this.dateTimeFormat = dateTimeFormat;
            getSpinner().setMinStep(step);
            getSpinner().setMaxStep(step);
            // Refresh value after dateTimeFormat is set
            getSpinner().setValue(date.getTime(), true);
        }

        protected String formatValue(long value) {
            if (dateTimeFormat != null) return dateTimeFormat.format(new Date(value));
            return "";
        }

        protected long parseValue(String value) {
            final Date parsedDate = new Date(dateInMillis);
            dateTimeFormat.parse(value, 0, parsedDate);
            return parsedDate.getTime();
        }
    }
}  // end class TimePicker
