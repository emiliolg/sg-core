
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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DoubleDateBox;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.configuration.DateConfig;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlDomUtils;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Times.millisToDate;
import static tekgenesis.common.core.Times.toMidnight;

/**
 * An input box that popups a two month Date Picker UI widget.
 */
public class DoubleDateBoxUI extends BaseDateUI implements HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private final DoubleDateBox doubleDateBox;

    //~ Constructors .................................................................................................................................

    /** Creates a DoubleDateBox UI widget. */
    public DoubleDateBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        doubleDateBox = createDateBox();
        init(doubleDateBox, true);
    }

    //~ Methods ......................................................................................................................................

    @Override public void applyConfig(DateConfig c, boolean refresh) {
        super.applyConfig(c, refresh);

        if (refresh) {
            doubleDateBox.getLeftPicker().onLoad();
            doubleDateBox.getRightPicker().onLoad();
        }
    }

    @Override public Object getValue() {
        return doubleDateBox.getValue() != null ? toMidnight(doubleDateBox.getValue()) : null;
    }

    @Override public void setValue(@Nullable Object modelValue, boolean fireEvents) {
        final Date date = modelValue != null ? millisToDate((Long) modelValue, true) : null;
        doubleDateBox.setValue(date, fireEvents);
    }

    @NotNull @Override Option<Element> createIcon() {
        return some(doubleDateBox.getElement());
    }

    @Override Option<Icon> iconInWidget(Element icon, String iconStyle) {
        return Icon.inTextBox(doubleDateBox, iconStyle, getModel().isExpand());
    }

    private DoubleDateBox createDateBox() {
        final DateTimeFormat format = getDateFormat(getModel().getDateType());
        final DoubleDateBox  box    = new DoubleDateBox(format);
        box.setStyleName("");
        box.setAllowDPShow(false);

        box.addValueChangeHandler(new FromToValueChangeHandler(box));

        final TextBox textBox = box.getTextBox();
        HtmlDomUtils.setPlaceholder(textBox, format.getPattern());
        textBox.addValueChangeHandler(e -> {
            // fix date box null value not being fired
            if (isEmpty(e.getValue())) doubleDateBox.setValue(null, true);
        });

        box.getLeftPicker().addShowRangeHandler(new PickerShowRangeHandler(box.getLeftPicker()));
        box.getRightPicker().addShowRangeHandler(new PickerShowRangeHandler(box.getRightPicker()));

        return box;
    }
}  // end class DoubleDateBoxUI
