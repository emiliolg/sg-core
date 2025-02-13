
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.*;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;
import tekgenesis.view.client.formatter.InputHandler;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.common.core.Option.of;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.formatter.InputHandler.none;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;

/**
 * A Range Value UI widget.
 */
public class RangeValueUI extends FieldWidgetUI implements HasInputHandlerUI, HasScalarValueUI, HasRangeOptionsUI, HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private InputHandler<?>   inputHandler;
    private final TextFieldUI textRangeValue;

    //~ Constructors .................................................................................................................................

    /** Creates a TextArea UI widgets. */
    public RangeValueUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        inputHandler   = none();
        textRangeValue = new TextFieldUI(container, model);
        final FlowPanel panel = div();
        panel.add(textRangeValue);
        initWidget(panel);
        setFocusTarget(textRangeValue.getFocusTarget());
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean acceptsFocus() {
        return textRangeValue.acceptsFocus();
    }

    @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {
        textRangeValue.addChangeHandler(changeHandler);
    }

    @Override public void setFocus(boolean focused) {
        textRangeValue.setFocus(focused);
    }

    @Override public void setInputHandler(InputHandler<?> inputHandler) {
        textRangeValue.setInputHandler(inputHandler);
        this.inputHandler = inputHandler;
    }

    /** Set range options. Expect from and to values. */
    @Override public void setRangeOptions(@NotNull KeyMap options) {
        final List<String> values      = getFormattedValues(options.entrySet());
        String             placeholder = "";
        if (!values.isEmpty()) placeholder = MSGS.from() + " " + values.get(0);
        if (values.size() > 1) placeholder += " " + MSGS.to() + " " + values.get(1);
        textRangeValue.setPlaceholder(placeholder);
    }

    @Nullable @Override public Object getValue() {
        return textRangeValue.getValue();
    }

    @Override public void setValue(@Nullable Object value) {
        textRangeValue.setValue(value);
    }

    @Override public void setValue(@Nullable Object value, boolean fireEvents) {
        textRangeValue.setValue(value, fireEvents);
    }

    @NotNull @Override Option<Element> createIcon() {
        return of(textRangeValue.getElement());
    }

    @Override Option<Icon> iconInWidget(Element icon, String iconStyle) {
        return textRangeValue.iconInWidget(icon, iconStyle);
    }

    private List<String> getFormattedValues(@NotNull final Set<Map.Entry<Object, String>> options) {
        final List<String> result = options.isEmpty() ? Collections.emptyList() : new ArrayList<>(options.size());
        for (final Map.Entry<Object, String> option : options)
            result.add(inputHandler.toString(option.getKey()));
        return result;
    }
}  // end class RangeValueUI
