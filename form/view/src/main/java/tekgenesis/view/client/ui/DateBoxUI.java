
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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.LimitedDatePicker;
import com.google.gwt.user.datepicker.client.LimitedMonthSelector;
import com.google.gwt.user.datepicker.client.TekDateBox;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.configuration.DateConfig;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlDomUtils;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Times.millisToDate;
import static tekgenesis.common.core.Times.toMidnight;

/**
 * An input box that popups a DatePicker UI widget.
 */
public class DateBoxUI extends BaseDateUI implements HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private final TekDateBox mainDateBox;

    //~ Constructors .................................................................................................................................

    /** Creates a DateBox UI widget. */
    public DateBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        mainDateBox = createDateBox();
        init(mainDateBox, true);
    }

    //~ Methods ......................................................................................................................................

    @Override public void applyConfig(DateConfig c, boolean refresh) {
        super.applyConfig(c, refresh);

        if (refresh) mainDateBox.getDatePicker().onLoad();
    }

    public Object getValue() {
        return asDateBox().getValue() != null ? toMidnight(asDateBox().getValue().getTime()) : null;
    }

    @Override public void setValue(@Nullable final Object modelValue, boolean fireEvents) {
        asDateBox().setValue(modelValue != null ? millisToDate((Long) modelValue, true) : null, fireEvents);
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName("dateBox");
    }

    @NotNull @Override Option<Element> createIcon() {
        return some(mainDateBox.getElement());
    }

    @Override Option<Icon> iconInWidget(Element icon, String iconStyle) {
        return Icon.inTextBox(mainDateBox, iconStyle, getModel().isExpand());
    }

    private TekDateBox asDateBox() {
        return cast(value);
    }

    private TekDateBox createDateBox() {
        final DatePicker datePicker = new LimitedDatePicker(new LimitedMonthSelector(true, true));
        datePicker.addShowRangeHandler(new PickerShowRangeHandler(datePicker));

        final TekDateBox dateBox = new TekDateBox(datePicker);
        dateBox.setAllowDPShow(false);
        dateBox.setFireNullValues(true);
        HtmlDomUtils.clearStyleName(dateBox);

        final DateTimeFormat format = getDateFormat(getModel().getDateType());

        dateBox.setFormat(new TekDateBox.DefaultFormat(format));
        dateBox.addValueChangeHandler(new FromToValueChangeHandler(dateBox));

        final TextBox textBox = dateBox.getTextBox();
        HtmlDomUtils.setPlaceholder(textBox, format.getPattern());

        final DateHandler handler = new DateBoxHandler(dateBox);
        textBox.addKeyDownHandler(handler);
        textBox.addValueChangeHandler(handler);

        setFocusTarget(Option.option(textBox));

        return dateBox;
    }  // end method createDateBox

    //~ Inner Classes ................................................................................................................................

    private static class DateBoxHandler extends DateHandler {
        DateBoxHandler(TekDateBox dateBox) {
            super(dateBox);
        }

        @Override void showPicker() {
            ((DateBox) dateBox).showDatePicker();
        }
    }
}  // end class DateBoxUI
