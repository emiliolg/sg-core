
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package com.google.gwt.user.client.ui.datepicker;

import java.util.Date;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.LimitedDatePicker;
import com.google.gwt.user.datepicker.client.LimitedMonthSelector;

/**
 * The {@link DateTimePicker} combines a {@link DatePicker} and a {@link TimePicker}.
 */
public class DateTimePicker extends Composite {

    //~ Instance Fields ..............................................................................................................................

    private final DatePicker datePicker;
    private final TimePicker timePicker;

    //~ Constructors .................................................................................................................................

    /**
     * Creates a {@link TimePicker} instance using the current date as initial value.
     *
     * @param  use24Hours  use 24 Hrs or not
     */
    public DateTimePicker(boolean use24Hours, boolean midnightAs24) {
        this(new LimitedDatePicker(new LimitedMonthSelector(true, true)), new TimePicker(use24Hours, midnightAs24));
    }

    /**
     * @param  datePicker  the {@link DatePicker} to be used
     * @param  timePicker  the {@link TimePicker} to be used
     */
    @SuppressWarnings("WeakerAccess")  // component
    public DateTimePicker(final DatePicker datePicker, final TimePicker timePicker) {
        this.datePicker = datePicker;
        this.timePicker = timePicker;
        final VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        verticalPanel.add(datePicker);
        datePicker.setWidth("100%");
        verticalPanel.add(timePicker);
        timePicker.addValueChangeHandler(event -> datePicker.setValue(event.getValue()));
        datePicker.addValueChangeHandler(event -> timePicker.setDate(event.getValue()));
        initWidget(verticalPanel);
    }

    //~ Methods ......................................................................................................................................

    /** @return  Gets whether this widget is enabled */
    public boolean isEnabled() {
        return timePicker.isEnabled();
    }

    /** @return  the entered date */
    public Date getDate() {
        return timePicker.getValue();
    }

    /** @return  the {@link DatePicker} */
    public DatePicker getDatePicker() {
        return datePicker;
    }

    /**
     * Sets whether this widget is enabled.
     *
     * @param  enabled  true to enable the widget, false to disable it
     */
    public void setEnabled(boolean enabled) {
        timePicker.setEnabled(enabled);
    }

    /** @return  the {@link TimePicker} */
    public TimePicker getTimePicker() {
        return timePicker;
    }
}  // end class DateTimePicker
