
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.datepicker.client.LimitedDatePicker;
import com.google.gwt.user.datepicker.client.LimitedMonthSelector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.CheckMsg;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.configuration.DateConfig;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Times.millisToDate;
import static tekgenesis.common.core.Times.toMidnight;

/**
 * Inline/permanent DatePicker UI widget.
 */
public class DatePickerUI extends BaseDateUI implements HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private final LimitedDatePicker datePicker;

    //~ Constructors .................................................................................................................................

    /** Creates Inline/permanent DatePicker UI widget. */
    public DatePickerUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);

        datePicker = new LimitedDatePicker(new LimitedMonthSelector(true, true));
        datePicker.addShowRangeHandler(new PickerShowRangeHandler(datePicker));
        datePicker.addValueChangeHandler(new FromToValueChangeHandler(datePicker));

        init(datePicker, false);
    }

    //~ Methods ......................................................................................................................................

    @Override public void addMessage(@NotNull CheckMsg msg) {
        setCurrentMsg(msg);
        setInlineMessage(msg.getType(), msg.getText());
    }

    @Override public void applyConfig(DateConfig c, boolean refresh) {
        super.applyConfig(c, refresh);

        if (refresh) datePicker.setCurrentMonth(datePicker.getCurrentMonth());  // Other do onLoad() but datePickers needs to refresh view as well.
    }

    public Object getValue() {
        return datePicker.getValue() != null ? toMidnight(datePicker.getValue().getTime()) : null;
    }

    @Override public void setValue(@Nullable final Object modelValue, boolean fireEvents) {
        datePicker.setValue(modelValue != null ? millisToDate((Long) modelValue, true) : null, fireEvents);
        if (modelValue != null) datePicker.setCurrentMonth(millisToDate((Long) modelValue, true));
    }

    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName("datePicker");
    }

    @NotNull @Override Option<Element> createIcon() {
        return empty();
    }
}  // end class DatePickerUI
