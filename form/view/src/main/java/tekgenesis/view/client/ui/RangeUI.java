
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

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;
import tekgenesis.view.client.formatter.InputHandler;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.metadata.form.model.FormConstants.FOCUSED;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.formatter.InputHandler.none;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;

/**
 * A Range UI widget.
 */
public class RangeUI extends FieldWidgetUI implements HasInputHandlerUI, HasArrayValueUI, HasRangeOptionsUI, HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private InputHandler<?>   inputHandler;
    private TextFieldUI       lastOnFocus;
    private final TextFieldUI textFrom;
    private final TextFieldUI textTo;

    //~ Constructors .................................................................................................................................

    /** Creates a TextArea UI widgets. */
    public RangeUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        inputHandler = none();
        textFrom     = new TextFieldUI(container, model);
        textFrom.setPlaceholder(MSGS.from());
        addTextFieldFocusHandler(textFrom);
        textTo = new TextFieldUI(container, model);
        textTo.setPlaceholder(MSGS.to());
        addTextFieldFocusHandler(textTo);
        lastOnFocus = textFrom;
        final FlowPanel panel = div();
        panel.add(textFrom);
        panel.add(textTo);
        initWidget(panel);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean acceptsFocus() {
        return true;
    }

    @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {
        textFrom.addChangeHandler(changeHandler);
        textTo.addChangeHandler(changeHandler);
    }

    @Override public void setFocus(boolean focused) {
        lastOnFocus.setFocus(focused);
    }

    @Override public void setInputHandler(InputHandler<?> inputHandler) {
        textFrom.setInputHandler(inputHandler);
        textTo.setInputHandler(inputHandler);
        this.inputHandler = inputHandler;
    }

    /** Set range options. Expect from and to values. */
    @Override public void setRangeOptions(@NotNull final KeyMap options) {
        final List<String> values = getFormattedValues(options.entrySet());
        if (!values.isEmpty()) textFrom.setPlaceholder(MSGS.from() + " " + values.get(0));
        if (values.size() > 1) textTo.setPlaceholder(MSGS.to() + " " + values.get(1));
    }

    @NotNull @Override public Iterable<?> getValues() {
        return textFrom.getValue() != null && textTo.getValue() != null ? listOf(textFrom.getValue(), textTo.getValue()) : emptyList();
    }

    @Override public void setValues(@NotNull Iterable<Object> v) {
        final ImmutableList<Object> values = Colls.toList(v);

        if (!values.isEmpty()) {
            textFrom.setValue(values.get(0));
            if (values.size() > 1) textTo.setValue(values.get(1));
        }
    }

    @Override public void setValues(@NotNull Iterable<Object> values, boolean fireEvents) {
        setValues(values);
    }

    @NotNull @Override Option<Element> createIcon() {
        return of(textFrom.getElement());
    }

    @Override Option<Icon> iconInWidget(Element icon, String iconStyle) {
        textTo.iconInWidget(icon, iconStyle);
        return textFrom.iconInWidget(icon, iconStyle);
    }

    private void addTextFieldFocusHandler(final TextFieldUI ui) {
        ui.addFocusHandler(event -> {
            lastOnFocus = ui;
            addStyleName(FOCUSED);
        });
        ui.addBlurHandler(event -> {
            lastOnFocus = ui;
            removeStyleName(FOCUSED);
        });
    }

    private List<String> getFormattedValues(@NotNull final Set<Map.Entry<Object, String>> options) {
        final List<String> result = options.isEmpty() ? Collections.emptyList() : new ArrayList<>(options.size());
        for (final Map.Entry<Object, String> option : options)
            result.add(inputHandler.toString(option.getKey()));
        return result;
    }
}  // end class RangeUI
