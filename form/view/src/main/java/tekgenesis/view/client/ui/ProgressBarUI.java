
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
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.Widget;

import static java.lang.Math.max;
import static java.lang.Math.min;

import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.metadata.form.model.FormConstants.NUMBER_STYLE;
import static tekgenesis.metadata.form.model.FormConstants.PROGRESS;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.span;

/**
 * ProgressBar UI widget.
 */
public class ProgressBarUI extends FieldWidgetUI implements HasScalarValueUI, MouseMoveHandler, MouseOutHandler, HasFromTo {

    //~ Instance Fields ..............................................................................................................................

    private final FlowPanel  bar;
    private final InlineHTML span;
    private WidgetMessages   tooltipMessages = null;
    private double           value;

    //~ Constructors .................................................................................................................................

    /** Creates ProgressBar UI widget. */
    public ProgressBarUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        final FlowPanel progressPanel = div();
        progressPanel.addStyleName(PROGRESS);
        bar = div();
        bar.addStyleName(FormConstants.PROGRESS_BAR);
        span = span();
        bar.add(span);
        progressPanel.add(bar);
        initWidget(progressPanel);
        value = 0;
        if (!model.getTooltip().isEmpty()) {
            tooltipMessages = WidgetMessages.create((FlowPanel) div, true);
            progressPanel.addDomHandler(this, MouseMoveEvent.getType());
            progressPanel.addDomHandler(this, MouseOutEvent.getType());
        }
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {
        // Read Only !
    }

    @Override public void onMouseMove(MouseMoveEvent event) {
        // Move the tooltip along with the mouse position
        tooltipMessages.hide();
        tooltipMessages.show(event.getClientX(), event.getClientY());
    }

    @Override public void onMouseOut(MouseOutEvent event) {
        tooltipMessages.hide();
    }

    /** Default add style to component. */
    public void setContentStyleName(String styleName) {
        bar.addStyleName(styleName);
    }

    @Override public void setFrom(Double from) {
        bar.getElement().getStyle().setMarginLeft(from, Style.Unit.PCT);
    }

    /** Sets Placeholder property to the DOM element. */
    public void setPlaceholder(String placeholder) {
        span.setText(placeholder);
    }

    @Override public void setTo(Double to) {
        throw unreachable();
    }

    /**
     * Override the default tooltip behavior so it is displayed when hovering the image and avoid
     * creating an extra icon.
     *
     * @param  tip  the string message to be displayed
     */
    @Override public void setTooltip(String tip) {
        if (tooltipMessages != null) tooltipMessages.tooltipmsg(tip);
    }

    @Override public Object getValue() {
        return value;
    }

    @Override public void setValue(@Nullable Object modelValue) {
        if (modelValue != null) {
            value = max(0, min(((Number) modelValue).doubleValue(), 100));
            bar.setWidth(value + "%");
        }
    }

    @Override public void setValue(@Nullable Object modelValue, boolean fireEvents) {
        setValue(modelValue);
        if (fireEvents) throw new UnsupportedOperationException(Constants.TO_BE_IMPLEMENTED);
    }

    @Override void addStyleNames() {
        super.addStyleNames();
        removeStyleName(NUMBER_STYLE);
    }

    // ** This widget doesn't support icon */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }
}  // end class ProgressBarUI
