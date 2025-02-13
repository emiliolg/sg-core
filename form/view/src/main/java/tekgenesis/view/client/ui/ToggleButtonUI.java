
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.metadata.form.widget.ToggleButtonType;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.metadata.form.model.FormConstants.ACTIVE_STYLE;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * Checkbox UI widget.
 */
public class ToggleButtonUI extends BaseButtonUI implements HasScalarValueUI, ClickHandler {

    //~ Instance Fields ..............................................................................................................................

    private boolean                    active;
    private ValueChangeHandler<Object> changeHandler;

    //~ Constructors .................................................................................................................................

    /** Create a Checkbox UI widget. */
    public ToggleButtonUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        changeHandler = null;

        if (isDeprecate()) button.addStyleName("btn-warning");
        else {
            setActive(false);
            setText(model.getLabel());
        }

        addClickHandler(this);
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(ValueChangeHandler<Object> handler) {
        changeHandler = handler;
    }

    @Override public void onClick(ClickEvent event) {
        if (!isDeprecate()) setActive(!active);
        changeHandler.onValueChange(null);
    }

    /** Returns true if this toggle button is active or not. */
    public boolean isActive() {
        return active;
    }

    /** Returns true whether or not this toggle button is a deprecate one. */
    public boolean isDeprecate() {
        return getModel().getToggleButtonType() == ToggleButtonType.DEPRECATE;
    }

    @Override public void setReadOnly(boolean readOnly) {
        if (isDeprecate()) return;
        super.setReadOnly(readOnly);
    }

    @Override public Object getValue() {
        return active;
    }

    @Override public void setValue(@Nullable Object modelValue) {
        setActive(modelValue != null && (Boolean) modelValue);
    }

    @Override public void setValue(@Nullable Object modelValue, boolean fireEvents) {
        setValue(modelValue);
        if (fireEvents) throw new UnsupportedOperationException(Constants.TO_BE_IMPLEMENTED);
    }

    @Override protected void addButtonStyle() {
        addStyleName("toggleButton");
    }

    private void setActive(boolean active) {
        this.active = active;

        if (active) {
            button.addStyleName(ACTIVE_STYLE);
            if (isDeprecate()) button.setText(MSGS.undeprecate());
            Icon.replaceInWidget(button, getModel().getIconSelectedStyle());
        }
        else {
            button.removeStyleName(ACTIVE_STYLE);
            if (isDeprecate()) button.setText(MSGS.deprecate());
            Icon.replaceInWidget(button, getModel().getIconStyle());
        }
    }
}  // end class ToggleButtonUI
