
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
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SuggestOracle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.CheckMsg;
import tekgenesis.common.Predefined;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.configuration.DynamicConfig;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.model.KeyMap;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.Option.*;
import static tekgenesis.view.client.controller.ViewCreator.Dynamic.createView;
import static tekgenesis.view.client.ui.Widgets.checkArrayAccess;
import static tekgenesis.view.client.ui.Widgets.checkScalarAccess;

/**
 * A widget that dynamically changes how it's render.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class DynamicUI extends HasOptionsUI implements HasWidgetsUI, ValueChangeHandler<Object> {

    //~ Instance Fields ..............................................................................................................................

    private ValueChangeHandler<Object> changeHandler = null;
    private DynamicConfig              config        = DynamicConfig.DEFAULT;
    private WidgetUI                   dynamic;

    private final FlowPanel                                    panel            = HtmlWidgetFactory.div();
    private Option<SelectionHandler<SuggestOracle.Suggestion>> selectionHandler = empty();

    //~ Constructors .................................................................................................................................

    /** Creates a DynamicUI widget component. */
    public DynamicUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        dynamic = new NoneWidgetUI(container, model);
        initWidget(panel);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean acceptsFocus() {
        return dynamic.acceptsFocus();
    }

    @Override public void addChangeHandler(ValueChangeHandler<Object> handler) {
        changeHandler = handler;
    }

    @Override public void addChild(WidgetUI w) {
        panel.add(w);
        dynamic = w;
        if (w instanceof FieldWidgetUI) ((FieldWidgetUI) w).setInputWidth(MAX_COL);
        w.removeStyleName("col-sm-" + w.getModel().getCol());

        for (final HasValueUI ui : asValued())
            ui.addChangeHandler(this);
    }

    @Override public void addMessage(@NotNull CheckMsg msg) {
        for (final FieldWidgetUI ui : asField())
            ui.addMessage(msg);
    }

    public void addSelectionHandler(final SelectionHandler<SuggestOracle.Suggestion> handler) {
        selectionHandler = some(handler);
    }

    /** Apply a new configuration. */
    public void applyConfig(@NotNull final DynamicConfig c) {
        if (!config.equals(c)) {
            config = c;
            redraw();
        }
    }

    public Option<MailFieldUI> asMailField() {
        return dynamic instanceof MailFieldUI ? some(Predefined.cast(dynamic)) : empty();
    }

    @Override public void clearMessages() {
        for (final MailFieldUI ui : asMailField()) {
            if (ui.getErrorMessage() != null) return;
        }
        for (final FieldWidgetUI ui : asField())
            ui.clearMessages();
    }

    @Override public boolean hasErrorMessage() {
        final Option<FieldWidgetUI> field = asField();
        return field.isPresent() && field.get().hasErrorMessage();
    }

    @Override public void onValueChange(ValueChangeEvent<Object> event) {
        if (changeHandler != null) changeHandler.onValueChange(event);
    }

    @Override public CheckMsg getCurrentMessage() {
        for (final FieldWidgetUI ui : asField())
            if (ui.getCurrentMessage() != null) return ui.getCurrentMessage();
        return super.getCurrentMessage();
    }

    @Override public void setDisabled(boolean disabled) {
        super.setDisabled(disabled);
        dynamic.setDisabled(disabled);
    }

    @Override public boolean isErrorMessageVisible() {
        final Option<FieldWidgetUI> field = asField();
        return field.isPresent() && field.get().isErrorMessageVisible();
    }

    @Override public void setFocus(boolean focused) {
        dynamic.setFocus(focused);
    }

    @Override public void setOptions(final KeyMap items) {
        for (final HasOptionsUI ui : asOption())
            ui.setOptions(items);
        for (final HasRangeOptionsUI ui : asRangeOption())
            ui.setRangeOptions(items);
    }

    @Override public void setPlaceholder(String placeholder) {
        for (final HasPlaceholderUI ui : asPlaceholder())
            ui.setPlaceholder(placeholder);
    }

    public Option<SelectionHandler<SuggestOracle.Suggestion>> getSelectionHandler() {
        return selectionHandler;
    }

    @Override public Object getValue() {
        checkScalarAccess(getModel());
        @Nullable final Object result;

        if (!isDefined()) result = null;
        else if (acceptsScalarAccess())
        // Get an scalar value from an scalar widget.
        result = asScalar().getValue();
        else if (acceptsArrayAccess()) {
            // Get an scalar value from an array widget.
            final Iterable<?> values = asArray().getValues();
            result = isEmpty(values) ? null : values.iterator().next();
        }
        else throw unreachable();

        return result;
    }

    @Override public void setValue(@Nullable Object value) {
        checkScalarAccess(getModel());
        // Object expected!
        if (isDefined()) {
            if (acceptsScalarAccess())
            // Set scalar value for an array widget.
            asScalar().setValue(value);
            else if (acceptsArrayAccess())
            // Set scalar value for an array widget.
            asArray().setValues(Option.option(value).toList());
        }
    }

    @Override public void setValue(@Nullable Object value, boolean fireEvents) {
        setValue(value);
    }

    @NotNull @Override public Iterable<?> getValues() {
        checkArrayAccess(getModel());
        if (!isDefined()) return emptyIterable();
        if (acceptsArrayAccess())
        // Get an array value from an array widget.
        return cast(asArray().getValues());
        if (acceptsScalarAccess())
        // Get an array value from an scalar widget.
        return Option.option(asScalar().getValue()).toList();
        throw unreachable();
    }

    @Override public void setValues(@NotNull Iterable<Object> values) {
        checkArrayAccess(getModel());
        // Objects expected!
        if (isDefined()) {
            if (acceptsArrayAccess())
            // Set array value for an array widget.
            asArray().setValues(values);
            else if (acceptsScalarAccess()) {
                // Set array value for an scalar widget.
                final Option<Object> first = seq(values).getFirst();
                asScalar().setValue(first.getOrNull());
            }
        }
    }  // end method setValues

    boolean acceptsArrayAccess() {
        return dynamic instanceof HasArrayValueUI;
    }

    boolean acceptsScalarAccess() {
        return dynamic instanceof HasScalarValueUI;
    }

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    void addStyleNames() {
        super.addStyleNames();
        addStyleName("dynamic");
    }

    @Override void clear() {
        dynamic.clear();
    }

    /** Dynamic's widget will handle the icon! */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }

    private HasArrayValueUI asArray() {
        if (!(dynamic instanceof HasArrayValueUI))
            throw new IllegalStateException("Attempting to fetch an array valued child widget having " + dynamic + ".");
        return (HasArrayValueUI) dynamic;
    }

    private Option<FieldWidgetUI> asField() {
        return dynamic instanceof FieldWidgetUI ? some(Predefined.cast(dynamic)) : empty();
    }

    private Option<HasOptionsUI> asOption() {
        return dynamic instanceof HasOptionsUI ? some(Predefined.cast(dynamic)) : empty();
    }

    private Option<HasPlaceholderUI> asPlaceholder() {
        return dynamic instanceof HasValueUI ? some(Predefined.cast(dynamic)) : empty();
    }

    private Option<HasRangeOptionsUI> asRangeOption() {
        return dynamic instanceof HasRangeOptionsUI ? some(Predefined.cast(dynamic)) : empty();
    }

    private HasScalarValueUI asScalar() {
        if (!(dynamic instanceof HasScalarValueUI))
            throw new IllegalStateException("Attempting to fetch an scalar valued child widget having " + dynamic + ".");
        return (HasScalarValueUI) dynamic;
    }

    private Option<HasValueUI> asValued() {
        return dynamic instanceof HasValueUI ? some(Predefined.cast(dynamic)) : empty();
    }

    private void redraw() {
        panel.clear();
        if (config.getWidget() != WidgetType.NONE) createView(container(), getModel(), this, config);
    }

    /** True if dynamic is defined. */
    private boolean isDefined() {
        return config.hasWidget();
    }

    //~ Static Fields ................................................................................................................................

    private static final int MAX_COL = 12;

    //~ Inner Classes ................................................................................................................................

    private static class NoneWidgetUI extends WidgetUI {
        private NoneWidgetUI(@NotNull final ModelUI container, @NotNull final Widget model) {
            super(container, model);
        }

        @NotNull @Override Option<Element> createIcon() {
            return empty();
        }
    }
}  // end class DynamicUI
