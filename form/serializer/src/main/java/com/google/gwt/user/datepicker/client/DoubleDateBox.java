
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package com.google.gwt.user.datepicker.client;

import java.util.Date;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.metadata.form.model.FormConstants;

import static tekgenesis.common.core.DateOnly.fromDate;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.PrettyDateParser.prettyParse;

/**
 * Widget that displays two calendars.
 */
public class DoubleDateBox extends Composite implements HasEnabled, HasValue<Date> {

    //~ Instance Fields ..............................................................................................................................

    private final TextBox box;

    private final DateTimeFormat       format;
    private final DoubleDateBoxHandler handler;

    private final DatePicker leftPicker;
    private final PopupPanel popup;
    private final DatePicker rightPicker;
    private Date             value = null;

    //~ Constructors .................................................................................................................................

    /** Creates a double date box widget using the given DateTimeFormat. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public DoubleDateBox(@NotNull final DateTimeFormat format) {
        this.format = format;
        box         = new TextBox();

        popup = new PopupPanel(true);
        // noinspection GWTStyleCheck
        popup.setStyleName("doubleDateBox");
        popup.addStyleName(FormConstants.FLOATING_MODAL);
        popup.addAutoHidePartner(box.getElement());

        final LimitedMonthSelector leftMonthSelector = new LimitedMonthSelector(true, false);
        leftPicker = new LimitedDatePicker(leftMonthSelector);
        // noinspection GWTStyleCheck
        leftPicker.setStyleName("doubleDateBoxLeftPicker");

        final LimitedMonthSelector rightMonthSelector = new LimitedMonthSelector(false, true);
        rightPicker = new LimitedDatePicker(rightMonthSelector);
        // noinspection GWTStyleCheck
        rightPicker.setStyleName("doubleDateBoxRightPicker");

        leftMonthSelector.setPartnerPicker(some(rightPicker));
        rightMonthSelector.setPartnerPicker(some(leftPicker));

        final FlowPanel container = new FlowPanel();
        container.add(leftPicker);
        container.add(rightPicker);
        popup.add(container);

        initWidget(box);

        handler = new DoubleDateBoxHandler();
        leftPicker.addValueChangeHandler(handler);
        rightPicker.addValueChangeHandler(handler);
        box.addFocusHandler(handler);
        box.addBlurHandler(handler);
        box.addClickHandler(handler);
        box.addKeyDownHandler(handler);
        box.setDirectionEstimator(false);
        popup.addCloseHandler(handler);
        setValue(new Date());
    }  // end ctor DoubleDateBox

    //~ Methods ......................................................................................................................................

    @Override public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Date> valueHandler) {
        return addHandler(valueHandler, ValueChangeEvent.getType());
    }

    /** Set to true to prevent popup from showing. */
    public void setAllowDPShow(boolean allowDPShow) {
        handler.setShowPickers(allowDPShow);
    }

    @Override public boolean isEnabled() {
        return box.isEnabled();
    }

    @Override public void setEnabled(boolean enabled) {
        box.setEnabled(enabled);
    }

    /** Returns the left date picker. */
    public DatePicker getLeftPicker() {
        return leftPicker;
    }

    /** Returns the right date picker. */
    public DatePicker getRightPicker() {
        return rightPicker;
    }

    /** Returns text box. */
    public TextBox getTextBox() {
        return box;
    }

    @Override public Date getValue() {
        return value;
    }

    @Override public void setValue(Date newValue) {
        setValue(newValue, false);
    }

    @Override public void setValue(@Nullable Date newValue, boolean fireEvents) {
        setValue(value, newValue, fireEvents);
    }

    @Nullable private Date parse(@NotNull final String dateText) {
        return prettyParse(dateText, format.getPattern());
    }

    /** Parses date and show pickers. */
    private void showDatePickers() {
        Date current = parse(box.getText().trim());
        if (current == null) current = new Date();

        leftPicker.setValue(current);
        setCurrentMonth(current);

        popup.showRelativeTo(box);
    }

    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private void updateDateFromTextBox() {
        final Date parsedDate = parse(box.getText().trim());
        setValue(value, parsedDate, true);
    }

    private void setCurrentMonth(Date current) {
        leftPicker.setCurrentMonth(current);
        final DateOnly monthDate = fromDate(current);
        rightPicker.setCurrentMonth(DateOnly.date(monthDate.getYear(), monthDate.getMonth() + 1, 1).toDate());
    }

    private void setValue(@Nullable final Date oldDate, @Nullable final Date date, boolean fireEvents) {
        value = date;
        if (date != null) setCurrentMonth(date);

        box.setText(value == null ? "" : format.format(value));
        if (fireEvents) DateChangeEvent.fireIfNotEqualDates(this, oldDate, value);
    }

    //~ Inner Classes ................................................................................................................................

    private class DoubleDateBoxHandler
        implements ValueChangeHandler<Date>, FocusHandler, BlurHandler, ClickHandler, KeyDownHandler, CloseHandler<PopupPanel>
    {
        private boolean showPickers = true;

        @Override public void onBlur(BlurEvent event) {
            if (!popup.isShowing()) updateDateFromTextBox();
        }

        @Override public void onClick(ClickEvent event) {
            showDatePickers();
        }

        @Override public void onClose(CloseEvent<PopupPanel> event) {
            if (showPickers) updateDateFromTextBox();
        }

        @Override public void onFocus(FocusEvent event) {
            if (showPickers && !popup.isShowing()) showDatePickers();
        }

        @Override public void onKeyDown(KeyDownEvent event) {
            switch (event.getNativeKeyCode()) {
            case KeyCodes.KEY_ENTER:
            case KeyCodes.KEY_TAB:
                updateDateFromTextBox();
                popup.hide();
                break;
            case KeyCodes.KEY_ESCAPE:
            case KeyCodes.KEY_UP:
                popup.hide();
                event.preventDefault();
                break;
            case KeyCodes.KEY_DOWN:
                showDatePickers();
                event.preventDefault();
                break;
            }
        }

        @Override public void onValueChange(ValueChangeEvent<Date> event) {
            resetPartnerDatePicker(event);
            setValue(parse(box.getText().trim()), event.getValue(), true);
            popup.hide();
            preventDatePickerPopup();
            box.setFocus(true);
        }

        void setShowPickers(boolean showPickers) {
            this.showPickers = showPickers;
        }

        private void preventDatePickerPopup() {
            showPickers = false;
            Scheduler.get().scheduleDeferred(() -> showPickers = true);
        }

        private void resetPartnerDatePicker(ValueChangeEvent<Date> event) {
            final LimitedDatePicker    datePicker    = (LimitedDatePicker) event.getSource();
            final LimitedMonthSelector monthSelector = (LimitedMonthSelector) datePicker.getMonthSelector();
            monthSelector.getPartnerPicker().ifPresent(p -> p.setValue(null, false));
        }
    }  // end class DoubleDateBoxHandler
}  // end class DoubleDateBox
