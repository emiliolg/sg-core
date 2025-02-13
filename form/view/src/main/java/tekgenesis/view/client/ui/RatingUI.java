
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.metadata.form.widget.RatingType;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.metadata.form.model.FormConstants.COL_12;
import static tekgenesis.metadata.form.model.FormConstants.DISABLED_STYLE;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * The Rating UI.
 */
public class RatingUI extends HasOptionsUI implements MouseMoveHandler, MouseOutHandler {

    //~ Instance Fields ..............................................................................................................................

    private WidgetMessages tooltipMessages = null;

    //~ Constructors .................................................................................................................................

    /** RatingUI constructor. */
    public RatingUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        init(new RatingComponent(model.getRatingType(), !model.isRequired()));

        // When having a tooltip expression, initialize a tooltip object and add mouse handlers
        if (!model.getTooltip().isEmpty()) {
            tooltipMessages = WidgetMessages.create((FlowPanel) div, true);
            component.asWidget().addDomHandler(this, MouseMoveEvent.getType());
            component.asWidget().addDomHandler(this, MouseOutEvent.getType());
        }
        final com.google.gwt.user.client.ui.Widget parent = component.asWidget().getParent();
        if (parent != null) parent.addStyleName(COL_12);
    }

    //~ Methods ......................................................................................................................................

    @Override public void onMouseMove(MouseMoveEvent event) {
        // Move the tooltip along with the mouse position
        tooltipMessages.hide();
        tooltipMessages.show(event.getClientX(), event.getClientY());
    }

    @Override public void onMouseOut(MouseOutEvent event) {
        tooltipMessages.hide();
    }

    @Override public void setDisabled(boolean disabled) {
        super.setDisabled(disabled);
        ((RatingComponent) component).setDisabled(disabled);
        if (tooltipMessages != null) tooltipMessages.hide();
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

    //~ Static Fields ................................................................................................................................

    private static final String FILLED = "filled";

    //~ Inner Classes ................................................................................................................................

    private class RatingComponent extends FlowPanel implements HasOptionsComponent, ClickHandler {
        private final Anchor                clearAnchor;
        private final List<InlineHTML>      elements           = new ArrayList<>();
        private boolean                     fillOpt            = false;
        private final boolean               optional;
        private final RatingType            ratingType;
        private ValueChangeHandler<Boolean> valueChangeHandler = null;

        @SuppressWarnings("DuplicateStringLiteralInspection")
        RatingComponent(RatingType ratingType, final boolean optional) {
            this.ratingType = ratingType;
            this.optional   = optional;
            clearAnchor     = HtmlWidgetFactory.anchor(MSGS.clear());
            clearAnchor.addClickHandler(this);
            clearAnchor.setVisible(false);
            add(clearAnchor);
            addStyleName("rating");
            addStyleName("pull-left");
        }

        @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {
            valueChangeHandler = cast(changeHandler);
        }

        @Override public void addOption(@NotNull String id, @NotNull String label) {
            final InlineHTML span = HtmlWidgetFactory.span();
            if (ratingType == RatingType.STARS) span.addStyleName("star");
            else if (ratingType == RatingType.HEARTS) span.addStyleName("heart");
            else span.addStyleName("numbers");
            span.addClickHandler(this);
            span.getElement().setAttribute("data-key", id);
            span.getElement().setAttribute("id", label);
            final Object value = getValue();
            if (elements.isEmpty() && value != null) fillOpt = true;
            if (fillOpt && value != null) {
                span.addStyleName(FILLED);
                if (label.equals(value.toString())) fillOpt = false;
            }
            elements.add(span);
            insert(span, 0);
        }

        @Override public void clearOptions() {
            while (getWidgetCount() > 0)
                remove(0);
            elements.clear();
            fillOpt = true;
        }

        @Override public void clearSelection() {
            uiSelectionClear();
            setSelectedOptions(Colls.emptyList(), false);
        }

        /** Clear selected items. */
        @Override public void onClick(final ClickEvent event) {
            final String key = getKey(cast(event.getSource()));

            uiSelectionUpdate(key, true);
            valueChangeHandler.onValueChange(null);
        }

        @Override public void setFocus(boolean focus) {
            Widgets.setFocus(this, focus);
        }

        @Override public void setSelectedOptions(@NotNull List<String> ids, boolean e) {
            if (!ids.isEmpty()) {
                final String key  = ids.get(0);
                boolean      fill = true;
                for (final InlineHTML span : elements) {
                    if (fill) {
                        if (getKey(span).equals(key)) fill = false;
                        span.addStyleName(FILLED);
                    }
                    else span.removeStyleName(FILLED);
                }
            }
            updateClearAnchor();

            if (e) DomEvent.fireNativeEvent(Document.get().createChangeEvent(), this);
        }

        private void updateClearAnchor() {
            if (optional) clearAnchor.setVisible(!uiSelectionIsEmpty());
        }

        private void setDisabled(boolean disabled) {
            if (disabled) addStyleName(DISABLED_STYLE);
            else removeStyleName(DISABLED_STYLE);
        }

        @SuppressWarnings("SuspiciousMethodCalls")
        private String getKey(com.google.gwt.user.client.ui.Widget widget) {
            return String.valueOf(elements.indexOf(widget));
        }
    }
}  // end class RatingUI
