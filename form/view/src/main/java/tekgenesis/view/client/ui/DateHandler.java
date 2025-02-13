
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.Date;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;

import static com.google.gwt.event.dom.client.KeyCodes.*;
import static com.google.gwt.user.datepicker.client.CalendarUtil.addDaysToDate;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.view.client.RootInputHandler.isTriggerKeyDown;

/**
 * Event handler for Date UI widgets.
 */
abstract class DateHandler implements KeyDownHandler, ValueChangeHandler<String> {

    //~ Instance Fields ..............................................................................................................................

    final HasValue<Date> dateBox;

    //~ Constructors .................................................................................................................................

    /** Creates a Date Handler. */
    DateHandler(final HasValue<Date> dateBox) {
        this.dateBox = dateBox;
    }

    //~ Methods ......................................................................................................................................

    @Override public void onKeyDown(KeyDownEvent event) {
        final Date selected = dateBox.getValue();
        if (isTriggerKeyDown(event)) {
            final int daysDiff;
            switch (event.getNativeKeyCode()) {
            case KEY_RIGHT:
                daysDiff = event.isShiftKeyDown() ? YEAR : 1;
                break;
            case KEY_LEFT:
                daysDiff = -(event.isShiftKeyDown() ? YEAR : 1);
                break;
            case KEY_DOWN:
                daysDiff = 7;
                break;
            case KEY_UP:
                daysDiff = -7;
                showPicker();  // to prevent GWT from hiding the datePicker.
                break;
            default:
                return;        // don't move.
            }
            dateBox.setValue(getDate(daysDiff, selected), true);
            event.preventDefault();
        }
        else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) dateBox.setValue(getDate(0, selected), true);
    }

    @Override public void onValueChange(ValueChangeEvent<String> e) {
        // fix date box null value not being fired
        if (isEmpty(e.getValue())) dateBox.setValue(null, true);
    }

    abstract void showPicker();

    private Date getDate(int days, Date selected) {
        final Date date = selected == null ? new Date() : selected;  // Today instead of null.
        addDaysToDate(date, days);
        return date;
    }

    //~ Static Fields ................................................................................................................................

    private static final int YEAR = 365;
}  // end class DateHandler
