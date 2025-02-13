
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.isEmpty;

/**
 * Base UI class for widget that have a scalar value.
 */
public class BaseHasScalarValueUI extends FieldWidgetUI implements HasScalarValueUI {

    //~ Instance Fields ..............................................................................................................................

    HasValue<Object> value;

    private Object previous;

    //~ Constructors .................................................................................................................................

    BaseHasScalarValueUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        value    = null;
        previous = null;
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(final ValueChangeHandler<Object> changeHandler) {
        value.addValueChangeHandler(changeHandler);
    }

    @Override public void clear() {
        super.clear();
        setValue(null);
    }

    public void initPrev() {
        if (previous == null) initPrev(value.getValue());
    }

    public void initPrev(@Nullable Object prev) {
        previous = prev;
    }

    public void resetUndo() {
        previous = null;
    }

    public void undo() {
        if (previous != null && !equal(previous, value.getValue())) {
            value.setValue(previous, true);
            previous = null;
        }
    }

    @Nullable @Override public Object getValue() {
        final Object val = value.getValue();
        return val instanceof String && isEmpty((String) val) ? null : val;
    }

    @Override public void setValue(@Nullable final Object modelValue) {
        setValue(modelValue, false);
    }

    @Override public void setValue(@Nullable final Object modelValue, boolean fireEvents) {
        value.setValue(modelValue, fireEvents);
    }

    void init(HasValue<?> widget, boolean formControl) {
        final com.google.gwt.user.client.ui.Widget w = (com.google.gwt.user.client.ui.Widget) widget;
        initWidget(w);
        value    = cast(widget);
        previous = null;
        if (formControl) w.addStyleName(FormConstants.FORM_CONTROL);
    }
}  // end class BaseHasScalarValueUI
