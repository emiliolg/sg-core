
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
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.datepicker.DateTimePicker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.model.FormConstants;

import static java.lang.Integer.parseInt;

import static com.google.gwt.user.datepicker.client.CalendarUtil.addDaysToDate;
import static com.google.gwt.user.datepicker.client.CalendarUtil.copyDate;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.metadata.form.model.FormConstants.STYLE_ATTR;

/**
 * A text box that shows a {@link DateTimePicker} when the user focuses on it.
 *
 * <h3>CSS Style Rules</h3>
 *
 * <dl>
 *   <dt>.gwt-DateTimeBox</dt>
 *   <dd>default style name</dd>
 *
 *   <dt>.dateBoxPopup</dt>
 *   <dd>Applied to the popup around the DateTimePicker</dd>
 *
 *   <dt>.dateBoxFormatError</dt>
 *   <dd>Default style for when the date box has bad input. Applied by
 *     {@link DateTimeBox.DefaultFormat} when the text does not represent a date that can be
 *     parsed</dd>
 * </dl>
 */
public class DateTimeBox extends Composite implements HasValue<Date>, IsEditor<LeafValueEditor<Date>>, HasEnabled {

    //~ Instance Fields ..............................................................................................................................

    private boolean               allowDPShow  = true;
    private final TextBox         box          = new TextBox();
    private DateTimeFormat        dateFormat   = null;
    private LeafValueEditor<Date> editor       = null;
    private Format                format;
    private boolean               midnightAs24 = false;
    private final DateTimePicker  picker;
    private final PopupPanel      popup;
    private String                resetTime    = null;

    //~ Constructors .................................................................................................................................

    /** Create a date box with a new {@link DateTimePicker}. */
    @SuppressWarnings("WeakerAccess")  // component
    public DateTimeBox(boolean use24Hours, boolean midnightAs24, @NotNull DefaultFormat format, @NotNull DateTimeFormat dateFormat) {
        this(new DateTimePicker(use24Hours, midnightAs24), null, format, dateFormat);
        this.midnightAs24 = midnightAs24;
    }

    /**
     * Create a new date box.
     *
     * @param  date    the default date.
     * @param  picker  the picker to drop down from the date box
     * @param  format  to use to parse and format dates
     */
    private DateTimeBox(final DateTimePicker picker, @Nullable final Date date, @NotNull Format format, @NotNull DateTimeFormat dateFormat) {
        this.picker     = picker;
        popup           = new PopupPanel(true);
        this.format     = format;
        this.dateFormat = dateFormat;

        popup.addAutoHidePartner(box.getElement());
        popup.setWidget(picker);
        // noinspection SpellCheckingInspection
        popup.getElement().setAttribute(STYLE_ATTR, "background-color: white; border: 1px solid lightgray; border-top:0;");
        // noinspection GWTStyleCheck
        popup.setStyleName("dateTimeBoxPopup");
        popup.addStyleName(FormConstants.FLOATING_MODAL);

        initWidget(box);
        setStyleName(DEFAULT_STYLE_NAME);

        final DateBoxHandler handler = new DateBoxHandler();
        picker.getDatePicker().addValueChangeHandler(handler);
        picker.getTimePicker().addValueChangeHandler(event -> {
            setValue(parseDate(false), event.getValue(), false);
            picker.getTimePicker().setValue(event.getValue());
        });

        box.addFocusHandler(handler);
        box.addBlurHandler(handler);
        box.addClickHandler(handler);
        box.addKeyDownHandler(handler);
        popup.addCloseHandler(handler);
        setValue(date);
    }

    //~ Methods ......................................................................................................................................

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Date> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /** Returns a {@link TakesValueEditor} backed by the DateTimeBox. */
    public LeafValueEditor<Date> asEditor() {
        if (editor == null) editor = TakesValueEditor.of(this);
        return editor;
    }

    /** Hide the date picker. */
    @SuppressWarnings("WeakerAccess")  // component
    public void hideDatePicker() {
        popup.hide();
    }

    /** Parses the current date box's value and shows that date. */
    @SuppressWarnings("WeakerAccess")  // component
    public void showDatePicker() {
        Date current = parseDate(false);
        if (current == null) current = new Date();
        picker.getDatePicker().setCurrentMonth(current);
        picker.getTimePicker().setValue(current);
        popup.showRelativeTo(this);
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

    @Override public boolean isEnabled() {
        return box.isEnabled();
    }

    /**
     * Gets the date picker.
     *
     * @return  the date picker
     */
    public DateTimePicker getDateTimePicker() {
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
     * Explicitly focus/un-focus this widget. Only one widget can have focus at a time, and the
     * widget that does will receive all keyboard events.
     *
     * @param  focused  whether this widget should take focus or release it
     */
    public void setFocus(boolean focused) {
        box.setFocus(focused);
    }

    /**
     * Gets the format instance used to control formatting and parsing of this {@link DateTimeBox}.
     *
     * @return  the format
     */
    @SuppressWarnings("WeakerAccess")  // component
    public Format getFormat() {
        return format;
    }

    /**
     * Sets the format used to control formatting and parsing of dates in this {@link DateTimeBox}.
     * If this {@link DateTimeBox} is not empty, the contents of date box will be replaced with
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

    /** Returns true if date picker is currently showing, false if not. */
    @SuppressWarnings("WeakerAccess")  // component
    public boolean isDatePickerShowing() {
        return popup.isShowing();
    }

    /** Sets a reset time, to reset the time part to it, when user changes date value. */
    public void setResetTime(@NotNull final String resetTime) {
        this.resetTime = resetTime;
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
        setValue(picker.getDatePicker().getValue(), date, fireEvents);
    }

    private void adjustStyles(Date oldDate, @NotNull Date date, String text) {
        if (oldDate != null) {
            final Date oldDateMinusOne = copyDate(oldDate);
            addDaysToDate(oldDateMinusOne, -1);
            picker.getDatePicker().removeStyleFromDates(DATE_PICKER_DAY_IS_VALUE_CSS, oldDateMinusOne);
        }

        if (text.contains(STR_24_00)) {
            picker.getDatePicker().removeStyleFromDates(DATE_PICKER_DAY_IS_VALUE_CSS, date);
            final Date newDate = copyDate(date);
            addDaysToDate(newDate, -1);
            picker.getDatePicker().addStyleToDates(DATE_PICKER_DAY_IS_VALUE_CSS, newDate);
        }
        else picker.getDatePicker().addStyleToDates(DATE_PICKER_DAY_IS_VALUE_CSS, date);
    }

    private Date parseDate(boolean reportError) {
        if (reportError) getFormat().reset(this, false);
        final String text = box.getText().trim();
        return getFormat().parse(this, text, reportError);
    }

    private String to24(@NotNull final Date date, @NotNull final String text) {
        final Date   d          = new Date(date.getTime() - MINUTE_IN_MILLIS);
        final String dFormatted = getFormat().format(this, d);
        return dFormatted.contains(STR_23_59) ? dFormatted.replace(STR_23_59, STR_24_00) : text;
    }

    private void setValue(Date oldDate, @Nullable Date date, boolean fireEvents) {
        if (date != null) {
            // picker.getDatePicker().setCurrentMonth(date);
            picker.getTimePicker().setDate(date);

            final String text = getFormat().format(this, date);
            if (midnightAs24) box.setText(to24(date, text));
            else box.setText(text);
        }
        picker.getDatePicker().setValue(date, false);

        if (midnightAs24 && date != null) adjustStyles(oldDate, date, box.getText());

        format.reset(this, false);

        if (fireEvents) DateChangeEvent.fireIfNotEqualDates(this, oldDate, date);
    }

    //~ Static Fields ................................................................................................................................

    private static final int    MINUTE_IN_MILLIS = 60000;
    private static final String STR_23_59        = "23:59";
    private static final String STR_24_00        = "24:00";

    /** Css class that is used to highlight a day as a value. */
    private static final String DATE_PICKER_DAY_IS_VALUE_CSS = "datePickerDayIsValue";

    /** Default style name added when the date box has a format error. */
    static final String DATE_BOX_FORMAT_ERROR = "dateBoxFormatError";

    /** Default style name. */
    private static final String DEFAULT_STYLE_NAME = "gwt-DateTimeBox";

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
        String format(DateTimeBox dateBox, Date date);

        /**
         * Parses the provided string as a date.
         *
         * @param   dateBox      the date box
         * @param   text         the string representing a date
         * @param   reportError  should the formatter indicate a parse error to the user?
         *
         * @return  the date created, or null if there was a parse error
         */
        Date parse(DateTimeBox dateBox, String text, boolean reportError);

        /**
         * If the format did any modifications to the date box's styling, reset them now.
         *
         * @param  abandon  true when the current format is being replaced by another
         * @param  dateBox  the date box
         */
        void reset(DateTimeBox dateBox, boolean abandon);
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

        @SuppressWarnings("MagicConstant")  // Its not magic IDEA!
        public void onValueChange(ValueChangeEvent<Date> event) {
            final Date value = event.getValue();

            if (midnightAs24) {
                resetTimeToMidnight(value);
                addDaysToDate(value, 1);
            }
            else resetTime(value, getDateTimePicker().getTimePicker().getValue(), resetTime);

            setValue(parseDate(false), value, true);
            preventDatePickerPopup();
        }

        @SuppressWarnings("UnnecessaryFullyQualifiedName")
        private void preventDatePickerPopup() {
            allowDPShow = false;
            Scheduler.get().scheduleDeferred(() -> allowDPShow = true);
        }

        @SuppressWarnings("deprecation")  // This is how GWT does it anyway...
        private void resetTime(Date date, Date timeDate) {
            date.setHours(timeDate.getHours());
            date.setMinutes(timeDate.getMinutes());
            date.setSeconds(timeDate.getSeconds());
        }

        @SuppressWarnings("deprecation")  // This is how GWT does it anyway...
        private void resetTime(Date date, Date timeDate, String strTime) {
            if (isEmpty(strTime)) resetTime(date, timeDate);
            else {
                final String[] parts = strTime.split(":");
                if (parts.length < 2) resetTime(date, timeDate);
                else {
                    final Date resetDate = new Date(date.getYear(), date.getMonth(), date.getDate(), parseInt(parts[0]), parseInt(parts[1]));
                    if (parts.length > 2) resetDate.setSeconds(parseInt(parts[2]));
                    resetTime(date, resetDate);

                    // only in this case, we should adequate the time picker to the new date.
                    picker.getTimePicker().setValue(date);
                }
            }
        }  // end method resetTime

        @SuppressWarnings("deprecation")
        private void resetTimeToMidnight(Date date) {
            resetTime(date, new Date(date.getYear(), date.getMonth(), 0, 0, 0));
        }

        private void updateDateFromTextBox() {
            final Date parsedDate = parseDate(true);
            if (parsedDate != null) setValue(picker.getDate(), parsedDate, true);
            else {
                // text parsed with dateTime format returned error, let's try with date format.
                Date date = null;
                try {
                    final String text = box.getText().trim();
                    if (!text.isEmpty()) date = dateFormat.parse(text);
                }
                catch (final IllegalArgumentException ignored) {}

                if (date != null) setValue(picker.getDate(), date, true);
                else box.setText("");  // if it's not a valid date, clean the textfield.
            }
        }                              // end method updateDateFromTextBox
    }  // end class DateBoxHandler

    /**
     * Default {@link DateTimeBox.Format} class. The date is first parsed using the
     * {@link DateTimeFormat} supplied by the user, or {@link DateTimeFormat#getMediumDateFormat()}
     * by default.
     *
     * <p>If that fails, we then try to parse again using the default browser date parsing.</p>
     *
     * <p>If that fails, the <code>dateBoxFormatError</code> css style is applied to the
     * {@link DateTimeBox}. The style will be removed when either a successful
     * {@link #parse(DateTimeBox,String, boolean)} is called or {@link #format(DateTimeBox,Date)} is
     * called.</p>
     *
     * <p>Use a different {@link DateTimeBox.Format} instance to change that behavior.</p>
     */
    public static class DefaultFormat implements Format {
        private final DateTimeFormat dateTimeFormat;

        /** Creates a new default format instance. */
        @SuppressWarnings("deprecation")
        public DefaultFormat() {
            dateTimeFormat = DateTimeFormat.getMediumDateTimeFormat();
        }

        /**
         * Creates a new default format instance.
         *
         * @param  dateTimeFormat  the {@link DateTimeFormat} to use with this {@link Format}.
         */
        public DefaultFormat(DateTimeFormat dateTimeFormat) {
            this.dateTimeFormat = dateTimeFormat;
        }

        public String format(DateTimeBox timeBox, Date date) {
            if (date == null) return "";
            else return dateTimeFormat.format(date);
        }

        @Nullable
        @SuppressWarnings("deprecation")
        public Date parse(DateTimeBox dateBox, String dateText, boolean reportError) {
            Date date = null;
            try {
                if (!dateText.isEmpty()) date = dateTimeFormat.parse(dateText);
            }
            catch (final IllegalArgumentException ignored) {
                if (reportError) dateBox.addStyleName(DATE_BOX_FORMAT_ERROR);
                return null;
            }
            return date;
        }

        public void reset(DateTimeBox dateBox, boolean abandon) {
            dateBox.removeStyleName(DATE_BOX_FORMAT_ERROR);
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
}  // end class DateTimeBox
