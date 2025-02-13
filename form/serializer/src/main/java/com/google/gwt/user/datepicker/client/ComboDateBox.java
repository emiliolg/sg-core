
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

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.*;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.metadata.form.model.FormConstants;

import static java.lang.Integer.parseInt;

import static com.google.gwt.user.datepicker.client.CalendarUtil.addDaysToDate;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.DateOnly.*;
import static tekgenesis.common.core.Times.MONTHS_YEAR;
import static tekgenesis.metadata.form.MetadataFormMessages.MSGS;

/**
 * Combo DateBox.
 */
public class ComboDateBox extends Composite implements HasEnabled, HasValue<Date> {

    //~ Instance Fields ..............................................................................................................................

    private final ListBox days;
    private Long          from   = null;
    private final ListBox months;
    private Long          to     = null;

    private Date          value = null;
    private final ListBox years;

    //~ Constructors .................................................................................................................................

    /** Creates a Combo DateBox. */
    public ComboDateBox() {
        final FlowPanel container = new FlowPanel();

        days = new ListBox();
        days.setMultipleSelect(false);
        days.setVisibleItemCount(1);
        days.setStyleName(COMBO_CLASS);

        months = new ListBox();
        months.setMultipleSelect(false);
        months.setVisibleItemCount(1);
        months.setStyleName(COMBO_CLASS);

        years = new ListBox();
        years.setMultipleSelect(false);
        years.setVisibleItemCount(1);
        years.setStyleName(COMBO_CLASS);

        if ("es".equals(LocaleInfo.getCurrentLocale().getLocaleName())) {
            container.add(days);
            container.add(months);
        }
        else {
            container.add(months);
            container.add(days);
        }

        container.add(years);

        initWidget(container);

        attachChangeHandlers();
        updateOptions();
        initComboValues();
        updateDayOptions();
    }

    //~ Methods ......................................................................................................................................

    @Override public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Date> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    // They move in tandem...
    @Override public boolean isEnabled() {
        return days.isEnabled();
    }

    @Override public void setEnabled(boolean enabled) {
        days.setEnabled(enabled);
        months.setEnabled(enabled);
        years.setEnabled(enabled);
    }

    /** Sets lower date limit. */
    public void setFrom(Long from) {
        this.from = from;
        refreshCombos();
    }

    /** Sets upper date limit. */
    public void setTo(Long to) {
        this.to = to;
        refreshCombos();
    }

    @Override public Date getValue() {
        return value;
    }

    @Override public void setValue(Date value) {
        setValue(value, false);
    }

    @Override public void setValue(@Nullable Date dateValue, boolean fireEvents) {
        final Date oldValue = value;
        value = dateValue;

        if (oldValue != dateValue) {
            final DateOnly newValue = fromDate(dateValue);

            final int dayIndex   = dateValue != null ? newValue.getDay() : 0;
            final int monthIndex = dateValue != null ? newValue.getMonth() - 1 : DateOnly.current().getMonth() - 1;
            final int yearIndex  = dateValue != null ? newValue.getYear() - getLowerLimit().getYear()
                                                     : DateOnly.current().getYear() - getLowerLimit().getYear();

            days.setSelectedIndex(dayIndex);
            months.setSelectedIndex(monthIndex);
            years.setSelectedIndex(yearIndex);

            updateDayOptions();
        }

        if (fireEvents) DateChangeEvent.fireIfNotEqualDates(this, oldValue, dateValue);
    }

    private void attachChangeHandlers() {
        final ChangeHandler optionHandler = event -> {
                                                updateDayOptions();
                                                setValue(getValueFromCombos(), true);
                                            };
        months.addChangeHandler(optionHandler);
        years.addChangeHandler(optionHandler);

        days.addChangeHandler(event -> setValue(getValueFromCombos(), true));
    }

    private void initComboValues() {
        final int monthIndex = DateOnly.current().getMonth() - 1;
        final int yearIndex  = DateOnly.current().getYear() - getLowerLimit().getYear();

        months.setSelectedIndex(monthIndex);
        years.setSelectedIndex(yearIndex);
    }

    private void refreshCombos() {
        final int yearsIndex = years.getSelectedIndex();
        String    yearStr    = "";
        if (yearsIndex != -1) yearStr = years.getValue(yearsIndex);

        years.clear();
        updateYearOptions();

        if (yearsIndex != -1) {
            if (!isEmpty(yearStr)) {
                final int year = parseInt(yearStr);
                if (year > getUpperLimit().getYear() || year < getLowerLimit().getYear()) years.setSelectedIndex(0);
                else years.setSelectedIndex(yearsIndex);
            }
            else years.setSelectedIndex(0);
        }
    }

    private void updateDayOptions() {
        // Keep selected index to check it later when options repopulated.
        final int selectedIndex = days.getSelectedIndex();

        // Clear and repopulate options (clear also clears selected index).
        days.clear();
        days.addItem("", "");
        final int daysOfActualMonth = getDaysOfActualMonth();
        for (int i = 1; i <= daysOfActualMonth; i++)
            days.addItem("" + i, "" + i);

        // Re-apply selection if any.
        if (selectedIndex != -1) {
            final String dayStr = days.getValue(selectedIndex);
            if (!isEmpty(dayStr)) {
                final int day = parseInt(dayStr);
                if (day > daysOfActualMonth) days.setSelectedIndex(0);
                else days.setSelectedIndex(selectedIndex);
            }
            else days.setSelectedIndex(0);
        }
    }

    private void updateMonthOptions() {
        for (int i = 0; i < MONTHS.length; i++)
            months.addItem(MONTHS[i], "" + i);
    }

    private void updateOptions() {
        updateMonthOptions();
        updateYearOptions();
    }

    private void updateYearOptions() {
        for (int i = getLowerLimit().getYear(); i <= getUpperLimit().getYear(); i++)
            years.addItem("" + i, "" + i);
    }

    private int getDaysOfActualMonth() {
        final int year  = parseInt(years.getValue(years.getSelectedIndex()));
        final int month = parseInt(months.getValue(months.getSelectedIndex())) + 1;

        final Date date = date(year, month + 1, 1).toDate();
        addDaysToDate(date, -1);
        return fromDate(date).getDay();
    }

    private DateOnly getLowerLimit() {
        return from != null ? fromMilliseconds(from) : date(BEGINNING_OF_TIME, 1, 1);
    }

    private DateOnly getUpperLimit() {
        return to != null ? fromMilliseconds(to) : date(current().getYear(), MONTHS_YEAR, LAST_DAY_OF_MONTH);
    }

    @Nullable private Date getValueFromCombos() {
        final String day = days.getValue(days.getSelectedIndex());
        if (isEmpty(day)) return null;
        final String month = months.getValue(months.getSelectedIndex());
        final String year  = years.getValue(years.getSelectedIndex());

        return date(parseInt(year), parseInt(month) + 1, parseInt(day)).toDate();
    }

    //~ Static Fields ................................................................................................................................

    private static final int    BEGINNING_OF_TIME = 1900;
    private static final int    LAST_DAY_OF_MONTH = 31;
    private static final String COMBO_CLASS       = "comboDateBox-combo " + FormConstants.FORM_CONTROL;

    private static final String[] MONTHS = {
        MSGS.january(), MSGS.february(), MSGS.march(), MSGS.april(), MSGS.may(), MSGS.june(),
        MSGS.july(), MSGS.august(), MSGS.september(), MSGS.october(), MSGS.november(), MSGS.december()
    };
}  // end class ComboDateBox
