
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package com.google.gwt.user.datepicker.client;

import org.jetbrains.annotations.NotNull;

/**
 * Limited date picker.
 */
public class LimitedDatePicker extends DatePicker {

    //~ Constructors .................................................................................................................................

    /** Creates a Limited Date Picker. */
    public LimitedDatePicker(@NotNull final MonthSelector selector) {
        super(selector, new DefaultCalendarView(), new CalendarModel());
        setStyleName("date-picker");
    }
}
