
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package com.google.gwt.user.datepicker.client;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.PushButton;

import tekgenesis.common.core.Option;

import static tekgenesis.common.core.Times.MONTHS_YEAR;

/**
 * Enhanced month selector with support for month and year backward and forward. To propagate a
 * change in a partner date picker use the given setter. Useful for DoubleDateBoxes.
 */
public class LimitedMonthSelector extends MonthSelector {

    //~ Instance Fields ..............................................................................................................................

    private final boolean      back;
    private final boolean      forward;
    private Grid               grid          = null;
    private Option<DatePicker> partnerPicker;

    private boolean year;

    //~ Constructors .................................................................................................................................

    /** Creates a Limited Month selector. */
    public LimitedMonthSelector(boolean back, boolean forward) {
        this.back     = back;
        this.forward  = forward;
        partnerPicker = Option.empty();
    }

    //~ Methods ......................................................................................................................................

    @Override protected void refresh() {
        grid.setText(0, 2, getModel().formatCurrentMonthAndYear());
    }

    @Override protected void setup() {
        // Set up grid.
        grid = new Grid(1, 5);
        grid.setStyleName(css().monthSelector());

        if (back) setupBack();
        if (forward) setUpForward();

        final HTMLTable.CellFormatter formatter = grid.getCellFormatter();
        formatter.setStyleName(0, 2, css().month());
        formatter.setWidth(0, 0, "1");
        formatter.setWidth(0, 1, "1");
        formatter.setWidth(0, 2, "100%");
        formatter.setWidth(0, 3, "1");
        formatter.setWidth(0, 4, "1");

        initWidget(grid);
    }  // end method setup

    Option<DatePicker> getPartnerPicker() {
        return partnerPicker;
    }

    void setPartnerPicker(Option<DatePicker> partnerPicker) {
        this.partnerPicker = partnerPicker;
    }

    boolean isYear() {
        return year;
    }

    private void addMonthsYear(int monthsYear) {
        super.addMonths(monthsYear);
        partnerPicker.ifPresent(p -> p.getMonthSelector().addMonths(monthsYear));
    }

    private void setupBack() {
        final PushButton yearBack = new PushButton();
        yearBack.setStyleName(css().previousButton());
        yearBack.addClickHandler(event -> {
            year = true;
            addMonthsYear(-MONTHS_YEAR);
        });
        yearBack.getUpFace().setHTML("<<");
        grid.setWidget(0, 0, yearBack);

        final PushButton monthBack = new PushButton();
        monthBack.setStyleName(css().previousButton());
        monthBack.addClickHandler(event -> {
            year = false;
            addMonthsYear(-1);
        });
        monthBack.getUpFace().setHTML("<");
        grid.setWidget(0, 1, monthBack);
    }

    private void setUpForward() {
        final PushButton monthForward = new PushButton();
        monthForward.setStyleName(css().nextButton());
        monthForward.getUpFace().setHTML(">");
        monthForward.addClickHandler(event -> {
            year = false;
            addMonthsYear(1);
        });
        grid.setWidget(0, 3, monthForward);

        final PushButton yearForward = new PushButton();
        yearForward.setStyleName(css().nextButton());
        yearForward.getUpFace().setHTML(">>");
        yearForward.addClickHandler(event -> {
            year = true;
            addMonthsYear(MONTHS_YEAR);
        });
        grid.setWidget(0, 4, yearForward);
    }
}  // end class LimitedMonthSelector
