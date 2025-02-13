
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
import com.google.gwt.user.datepicker.client.ComboDateBox;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.core.Times.millisToDate;

/**
 * Three-combo date box.
 */
public class ComboDateBoxUI extends BaseDateUI implements HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private final ComboDateBox comboDateBox;

    //~ Constructors .................................................................................................................................

    /** Creates a DateBox UI widget. */
    public ComboDateBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        comboDateBox = new ComboDateBox();
        init(comboDateBox, false);
    }

    //~ Methods ......................................................................................................................................

    /** Sets the 'from' String. */
    @Override public void setFrom(Double from) {
        super.setFrom(from);
        comboDateBox.setFrom(from.longValue());
    }

    /** Sets the 'to' String. */
    @Override public void setTo(Double to) {
        super.setTo(to);
        comboDateBox.setTo(to.longValue());
    }

    public Object getValue() {
        return comboDateBox.getValue() != null ? comboDateBox.getValue().getTime() : null;
    }

    @Override public void setValue(@Nullable final Object modelValue, boolean fireEvents) {
        final Date date = modelValue != null ? millisToDate((Long) modelValue, true) : null;
        comboDateBox.setValue(date, fireEvents);
    }

    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName("comboDateBox");
    }

    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }
}  // end class ComboDateBoxUI
