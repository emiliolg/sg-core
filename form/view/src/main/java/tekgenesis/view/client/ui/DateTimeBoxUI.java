
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

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DateTimeBox;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.configuration.DateConfig;
import tekgenesis.metadata.form.widget.DateType;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlDomUtils;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Times.millisToDate;

/**
 * An input box that popups a DateTimePicker UI widget.
 */
public class DateTimeBoxUI extends BaseDateUI implements HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private final DateTimeBox dateTimeBox;
    private final TextBox     textBox;

    //~ Constructors .................................................................................................................................

    /** Creates a DateTimeBox UI widget. */
    public DateTimeBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);

        final DateType       dateType = model.getDateType();
        final DateTimeFormat format   = getDateTimeFormat(dateType);

        dateTimeBox = new DateTimeBox(true, model.isMidnightAs24(), new DateTimeBox.DefaultFormat(format), getDateFormat(dateType));
        dateTimeBox.setResetTime(model.getResetTime());
        dateTimeBox.setAllowDPShow(false);
        final DateTimeFromToValueChangeHandler valueChangeHandler = new DateTimeFromToValueChangeHandler(dateTimeBox);
        final DatePicker                       datePicker         = dateTimeBox.getDateTimePicker().getDatePicker();

        dateTimeBox.addValueChangeHandler(valueChangeHandler);
        datePicker.addValueChangeHandler(valueChangeHandler);
        dateTimeBox.getDateTimePicker().getTimePicker().addValueChangeHandler(valueChangeHandler);

        datePicker.addShowRangeHandler(new PickerShowRangeHandler(datePicker));

        textBox = dateTimeBox.getTextBox();
        HtmlDomUtils.setPlaceholder(textBox, format.getPattern());
        final DateTimeBoxHandler handler = new DateTimeBoxHandler(dateTimeBox);
        textBox.addKeyDownHandler(handler);
        textBox.addValueChangeHandler(handler);

        setFocusTarget(Option.option(textBox));

        init(dateTimeBox, true);
    }  // end ctor DateTimeBoxUI

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(final ValueChangeHandler<Object> changeHandler) {
        super.addChangeHandler(changeHandler);

        // Time pickers are not notifying og their individual changes. We must register there.
        // As always, we are not using the value from the event, so using null instead.
        asDateTimeBox().getDateTimePicker().getTimePicker().addValueChangeHandler(event -> changeHandler.onValueChange(null));
    }

    @Override public void applyConfig(DateConfig c, boolean refresh) {
        super.applyConfig(c, refresh);

        if (refresh) dateTimeBox.getDateTimePicker().getDatePicker().onLoad();
    }

    public Object getValue() {
        return asDateTimeBox().getValue() != null ? asDateTimeBox().getValue().getTime() : null;
    }

    @Override public void setValue(@Nullable final Object modelValue, boolean fireEvents) {
        final Date date = modelValue != null ? millisToDate((Long) modelValue, false) : null;
        asDateTimeBox().setValue(date, fireEvents);
        if (modelValue == null) textBox.setValue(null);
    }

    @NotNull @Override Option<Element> createIcon() {
        return Option.some(dateTimeBox.getElement());
    }

    @Override Option<Icon> iconInWidget(Element icon, String iconStyle) {
        return Icon.inTextBox(dateTimeBox, iconStyle, getModel().isExpand());
    }

    private DateTimeBox asDateTimeBox() {
        return cast(value);
    }

    //~ Inner Classes ................................................................................................................................

    private class DateTimeBoxHandler extends DateHandler {
        DateTimeBoxHandler(DateTimeBox dateBox) {
            super(dateBox);
        }

        @Override void showPicker() {
            ((DateTimeBox) dateBox).showDatePicker();
        }
    }

    private class DateTimeFromToValueChangeHandler implements ValueChangeHandler<Date> {
        private final HasValue<Date> box;

        DateTimeFromToValueChangeHandler(HasValue<Date> box) {
            this.box = box;
        }

        @Override public void onValueChange(ValueChangeEvent<Date> event) {
            final Date date = event.getValue();
            if (date != null) {
                if (from != null && date.getTime() < from) box.setValue(new Date(from), false);

                if (to != null && date.getTime() > to) box.setValue(new Date(to), false);
            }
        }
    }
}  // end class DateTimeBoxUI
