
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

/*
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.gwt.user.datepicker.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.model.FormConstants;

import static tekgenesis.common.core.PrettyDateParser.prettyParse;

/**
 * A text box that shows a {@link DatePicker} when the user focuses on it.
 *
 * <h3>CSS Style Rules</h3>
 *
 * <dl>
 *   <dt>.gwt-DateBox</dt>
 *   <dd>default style name</dd>
 *
 *   <dt>.dateBoxPopup</dt>
 *   <dd>Applied to the popup around the DatePicker</dd>
 *
 *   <dt>.dateBoxFormatError</dt>
 *   <dd>Default style for when the date box has bad input. Applied by
 *     {@link TekDateBox.DefaultFormat} when the text does not represent a date that can be
 *     parsed</dd>
 * </dl>
 */
public class TekDateBox extends Composite implements HasEnabled, HasValue<Date>, IsEditor<LeafValueEditor<Date>> {

    //~ Instance Fields ..............................................................................................................................

    private boolean               allowDPShow    = true;
    private final TextBox         box            = new TextBox();
    private LeafValueEditor<Date> editor         = null;
    private boolean               fireNullValues = false;
    private Format                format;
    private final DatePicker      picker;
    private final PopupPanel      popup;

    //~ Constructors .................................................................................................................................

    /** Create a date box with a new {@link DatePicker}. */
    public TekDateBox() {
        this(new DatePicker(), null, DEFAULT_FORMAT);
    }

    /** Create a date box with a new {@link DatePicker}. */
    public TekDateBox(@NotNull final DatePicker picker) {
        this(picker, null, DEFAULT_FORMAT);
    }

    /**
     * Create a new date box.
     *
     * @param  date    the default date.
     * @param  picker  the picker to drop down from the date box
     * @param  format  to use to parse and format dates
     */
    private TekDateBox(DatePicker picker, @Nullable Date date, Format format) {
        this.picker = picker;
        picker.setStyleName("date-picker");

        popup = new PopupPanel(true);
        // noinspection DuplicateStringLiteralInspection
        assert format != null : "You may not construct a date box with a null format";
        this.format = format;

        popup.addAutoHidePartner(box.getElement());
        popup.setWidget(picker);
        // noinspection GWTStyleCheck
        popup.setStyleName("dateBoxPopup");
        popup.addStyleName(FormConstants.FLOATING_MODAL);

        initWidget(box);
        setStyleName(DEFAULT_STYLE_NAME);

        final DateBoxHandler handler = new DateBoxHandler();
        picker.addValueChangeHandler(handler);
        box.addFocusHandler(handler);
        box.addBlurHandler(handler);
        box.addClickHandler(handler);
        box.addKeyDownHandler(handler);
        box.setDirectionEstimator(false);
        popup.addCloseHandler(handler);
        setValue(date);
    }

    //~ Methods ......................................................................................................................................

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Date> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /** Returns a {@link TakesValueEditor} backed by the DateBox. */
    public LeafValueEditor<Date> asEditor() {
        if (editor == null) editor = TakesValueEditor.of(this);
        return editor;
    }

    /**
     * Sets the date box's 'access key'. This key is used (in conjunction with a browser-specific
     * modifier key) to automatically focus the widget.
     *
     * @param  key  the date box's access key
     */
    public void setAccessKey(char key) {
        box.setAccessKey(key);
    }

    /** Set to true to prevent popup from showing. */
    public void setAllowDPShow(boolean allowDPShow) {
        this.allowDPShow = allowDPShow;
    }

    /**
     * Gets the current cursor position in the date box.
     *
     * @return  the cursor position
     */
    public int getCursorPos() {
        return box.getCursorPos();
    }

    /** Returns true if the date box is enabled, false if not. */
    public boolean isEnabled() {
        return box.isEnabled();
    }

    /**
     * Gets the date picker.
     *
     * @return  the date picker
     */
    public DatePicker getDatePicker() {
        return picker;
    }

    /**
     * Sets whether the date box is enabled.
     *
     * @param  enabled  is the box enabled
     */
    public void setEnabled(boolean enabled) {
        box.setEnabled(enabled);
    }

    /**
     * Returns true iff the date box will fire {@code ValueChangeEvents} with a date value of
     * {@code null} for invalid or empty string values.
     */
    public boolean getFireNullValues() {
        return fireNullValues;
    }

    /**
     * Sets whether or not the date box will fire {@code ValueChangeEvents} with a date value of
     * {@code null} for invalid or empty string values.
     */
    public void setFireNullValues(boolean fireNullValues) {
        this.fireNullValues = fireNullValues;
    }

    /**
     * Explicitly focus/un-focus this widget. Only one widget can have focus at a time, and the
     * widget that does will receive all keyboard events.
     *
     * @param  focused  whether this widget should take focus or release it
     */
    public void setFocus(boolean focused) {
        box.setFocus(focused);
    }

    /**
     * Sets the format used to control formatting and parsing of dates in this {@link TekDateBox}.
     * If this {@link TekDateBox} is not empty, the contents of date box will be replaced with
     * current contents in the new format.
     *
     * @param  format  the new date format
     */
    public void setFormat(Format format) {
        // noinspection DuplicateStringLiteralInspection
        assert format != null : "A Date box may not have a null format";
        if (this.format != format) {
            final Date date = getValue();

            // This call lets the formatter do whatever other clean up is required to
            // switch formatter.
            //
            this.format.reset(this, true);

            // Now update the format and show the current date using the new format.
            this.format = format;
            setValue(date);
        }
    }

    /**
     * Gets the date box's position in the tab index.
     *
     * @return  the date box's tab index
     */
    public int getTabIndex() {
        return box.getTabIndex();
    }

    /**
     * Sets the date box's position in the tab index. If more than one widget has the same tab
     * index, each such widget will receive focus in an arbitrary order. Setting the tab index to
     * <code>-1</code> will cause this widget to be removed from the tab order.
     *
     * @param  index  the date box's tab index
     */
    public void setTabIndex(int index) {
        box.setTabIndex(index);
    }

    /**
     * Get text box.
     *
     * @return  the text box used to enter the formatted date
     */
    public TextBox getTextBox() {
        return box;
    }

    /**
     * Get the date displayed, or null if the text box is empty, or cannot be interpreted.
     *
     * @return  the current date value
     */
    public Date getValue() {
        return parseDate(true);
    }

    /** Set the date. */
    public void setValue(@Nullable Date date) {
        setValue(date, false);
    }

    public void setValue(@Nullable Date date, boolean fireEvents) {
        setValue(picker.getValue(), date, fireEvents, true);
    }

    /** Hide the date picker. */
    private void hideDatePicker() {
        popup.hide();
    }

    private Date parseDate(boolean reportError) {
        if (reportError) getFormat().reset(this, false);
        final String text = box.getText().trim();
        return getFormat().parse(this, text, reportError);
    }

    private void preventDatePickerPopup() {
        allowDPShow = false;
        Scheduler.get().scheduleDeferred(() -> allowDPShow = true);
    }

    /** Parses the current date box's value and shows that date. */
    private void showDatePicker() {
        Date current = parseDate(false);
        if (current == null) current = new Date();
        picker.setCurrentMonth(current);
        popup.showRelativeTo(this);
    }

    private void updateDateFromTextBox() {
        final Date parsedDate = parseDate(true);
        if (fireNullValues || (parsedDate != null)) setValue(picker.getValue(), parsedDate, true, true);
    }

    /**
     * Gets the format instance used to control formatting and parsing of this {@link TekDateBox}.
     *
     * @return  the format
     */
    private Format getFormat() {
        return format;
    }

    /** Returns true if date picker is currently showing, false if not. */
    private boolean isDatePickerShowing() {
        return popup.isShowing();
    }

    private void setValue(Date oldDate, @Nullable Date date, boolean fireEvents, boolean updateText) {
        if (date != null) picker.setCurrentMonth(date);
        picker.setValue(date, false);

        if (updateText) {
            format.reset(this, false);
            box.setText(getFormat().format(this, date));
        }

        if (fireEvents) DateChangeEvent.fireIfNotEqualDates(this, oldDate, date);
    }

    //~ Static Fields ................................................................................................................................

    /** Default style name. */
    private static final String        DEFAULT_STYLE_NAME = "gwt-DateBox";
    private static final DefaultFormat DEFAULT_FORMAT     = GWT.create(DefaultFormat.class);

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Implemented by a delegate to handle the parsing and formatting of date values. The default
     * {@link Format} uses a new {@link DefaultFormat} instance.
     */
    public interface Format {
        /**
         * Formats the provided date. Note, a null date is a possible input.
         *
         * @param   dateBox  the date box you are formatting
         * @param   date     the date to format
         *
         * @return  the formatted date as a string
         */
        String format(TekDateBox dateBox, @Nullable Date date);

        /**
         * Parses the provided string as a date.
         *
         * @param   dateBox      the date box
         * @param   text         the string representing a date
         * @param   reportError  should the formatter indicate a parse error to the user?
         *
         * @return  the date created, or null if there was a parse error
         */
        Date parse(TekDateBox dateBox, String text, boolean reportError);

        /**
         * If the format did any modifications to the date box's styling, reset them now.
         *
         * @param  abandon  true when the current format is being replaced by another
         * @param  dateBox  the date box
         */
        void reset(TekDateBox dateBox, boolean abandon);
    }

    //~ Inner Classes ................................................................................................................................

    private class DateBoxHandler
        implements ValueChangeHandler<Date>, FocusHandler, BlurHandler, ClickHandler, KeyDownHandler, CloseHandler<PopupPanel>
    {
        public void onBlur(BlurEvent event) {
            if (!isDatePickerShowing()) updateDateFromTextBox();
        }

        public void onClick(ClickEvent event) {
            showDatePicker();
        }

        public void onClose(CloseEvent<PopupPanel> event) {
            // If we are not closing because we have picked a new value, make sure the
            // current value is updated.
            if (allowDPShow) updateDateFromTextBox();
        }

        public void onFocus(FocusEvent event) {
            if (allowDPShow && !isDatePickerShowing()) showDatePicker();
        }

        public void onKeyDown(KeyDownEvent event) {
            switch (event.getNativeKeyCode()) {
            case KeyCodes.KEY_ENTER:
            case KeyCodes.KEY_TAB:
                updateDateFromTextBox();
                hideDatePicker();
                break;
            case KeyCodes.KEY_ESCAPE:
            case KeyCodes.KEY_UP:
                hideDatePicker();
                event.preventDefault();
                break;
            case KeyCodes.KEY_DOWN:
                showDatePicker();
                event.preventDefault();
                break;
            }
        }

        public void onValueChange(ValueChangeEvent<Date> event) {
            setValue(parseDate(false), normalize(event.getValue()), true, true);
            hideDatePicker();
            preventDatePickerPopup();
            box.setFocus(true);
        }

        // Round trips on render & parse to convert the date to our interpretation based on the format
        // See issue https://code.google.com/p/google-web-toolkit/issues/detail?id=4785
        private Date normalize(Date date) {
            final TekDateBox dateBox = TekDateBox.this;
            return getFormat().parse(dateBox, getFormat().format(dateBox, date), false);
        }
    }  // end class DateBoxHandler

    /**
     * Default {@link TekDateBox.Format} class. The date is first parsed using the
     * {@link DateTimeFormat} supplied by the user, or {@link PredefinedFormat#DATE_TIME_MEDIUM} by
     * default.
     *
     * <p>If that fails, we then try to parse again using the default browser date parsing.</p>
     *
     * <p>If that fails, the <code>dateBoxFormatError</code> css style is applied to the
     * {@link TekDateBox}. The style will be removed when either a successful
     * {@link #parse(TekDateBox,String, boolean)} is called or {@link #format(TekDateBox,Date)} is
     * called.</p>
     *
     * <p>Use a different {@link TekDateBox.Format} instance to change that behavior.</p>
     */
    public static class DefaultFormat implements Format {
        private final DateTimeFormat dateTimeFormat;

        /** Creates a new default format instance. */
        @SuppressWarnings("deprecation")
        public DefaultFormat() {
            dateTimeFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM);
        }

        /**
         * Creates a new default format instance.
         *
         * @param  dateTimeFormat  the {@link DateTimeFormat} to use with this {@link Format}.
         */
        public DefaultFormat(DateTimeFormat dateTimeFormat) {
            this.dateTimeFormat = dateTimeFormat;
        }

        public String format(TekDateBox dateBox, Date date) {
            if (date == null) return "";
            else return dateTimeFormat.format(date);
        }

        @Nullable public Date parse(TekDateBox dateBox, String dateText, boolean reportError) {
            final Date date = prettyParse(dateText, dateTimeFormat.getPattern());
            if (reportError && date == null) dateBox.addStyleName(DateTimeBox.DATE_BOX_FORMAT_ERROR);
            return date;
        }

        public void reset(TekDateBox dateBox, boolean abandon) {
            dateBox.removeStyleName(DateTimeBox.DATE_BOX_FORMAT_ERROR);
        }

        /**
         * Gets the date time format.
         *
         * @return  the date time format
         */
        public DateTimeFormat getDateTimeFormat() {
            return dateTimeFormat;
        }
    }  // end class DefaultFormat
}  // end class TekDateBox
