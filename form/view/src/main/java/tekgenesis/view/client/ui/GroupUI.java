
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.UIObject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.view.client.ui.base.ExtButton;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.metadata.form.model.FormConstants.COL_12;
import static tekgenesis.metadata.form.model.FormConstants.PAGE_HEADER;
import static tekgenesis.metadata.form.widget.WidgetType.*;

/**
 * Group UI widget.
 */
public class GroupUI extends ContainerUI implements HasScalarValueUI, MouseMoveHandler, MouseOutHandler, HasTooltipUI {

    //~ Instance Fields ..............................................................................................................................

    private ExtButton                  arrow         = null;
    private ValueChangeHandler<Object> changeHandler;

    private final Panel    childPanelsDiv;
    private boolean        collapsed                 = false;
    private boolean        heightAuto                = true;
    private WidgetMessages tooltipMessages           = null;

    //~ Constructors .................................................................................................................................

    /** Creates a Group UI widget. */
    public GroupUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        if (isEmpty(model.getLabel()) && model.isCollapsible()) addStyleName("no-label-collapsible");
        changeHandler  = null;
        childPanelsDiv = HtmlWidgetFactory.div();
        childPanelsDiv.addStyleName(GROUP_CONTAINER);
        if (getModel().getWidgetType() == FOOTER) childPanelsDiv.addStyleName("col-md-offset-2");
        div.add(childPanelsDiv);
        if (getModel().isCollapsible()) addCollapseButton();
        final String affix = model.getAffix();
        if (!isEmpty(affix)) addAffix(affix);

        // When having a tooltip expression, initialize a tooltip object and add mouse handlers
        if (!model.getTooltip().isEmpty()) {
            tooltipMessages = WidgetMessages.create((FlowPanel) childPanelsDiv, true);
            div.addDomHandler(this, MouseMoveEvent.getType());
            div.addDomHandler(this, MouseOutEvent.getType());
        }
        if (getModel().isTopLabel()) addStyleName("top-label");
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean acceptsFocus() {
        return !(getModel().isCollapsible() && collapsed) && super.acceptsFocus();
    }

    @Override public void addChangeHandler(ValueChangeHandler<Object> handler) {
        changeHandler = handler;
    }

    @Override public void onMouseMove(MouseMoveEvent event) {
        // Move the tooltip along with the mouse position
        tooltipMessages.hide();
        tooltipMessages.show(event.getClientX(), event.getClientY());
    }

    @Override public void onMouseOut(MouseOutEvent event) {
        tooltipMessages.hide();
    }

    @Override public void setFocus(boolean focused) {
        immutable(this).getFirst(WidgetUI::acceptsFocus).ifPresent(ui -> ui.setFocus(focused));
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

    @Nullable @Override public Object getValue() {
        return collapsed;
    }

    @Override public void setValue(@Nullable Object value) {
        final Boolean modelValue = value != null ? (Boolean) value : collapsed;
        if (modelValue != collapsed) toggleCollapse();
    }

    @Override public void setValue(@Nullable Object value, boolean fireEvents) {
        setValue(value);
        if (fireEvents) throw new UnsupportedOperationException(Constants.TO_BE_IMPLEMENTED);
    }

    void addChildPanel(final WidgetUI w) {
        childPanelsDiv.add(w);
        if (isMessageAndGroupIsVertical(w)) w.setStyleName(COL_12);
    }

    @Override void addStyleNames() {
        super.addStyleNames();
        addStyles(getModel().getWidgetType(), this);
    }

    private void addCollapseButton() {
        arrow = new ExtButton();
        arrow.setIcon(IconType.CHEVRON_DOWN);
        arrow.removeStyleName("btn");
        arrow.setStyleName(FormConstants.COLLAPSE_BTN);
        arrow.setTitle(FormConstants.COLLAPSE);
        arrow.addClickHandler(event -> toggleCollapse());
        div.add(arrow);
        addStyleName("collapsible");
    }

    private void toggleCollapse() {
        if (collapsed) {
            removeStyleName(FormConstants.COLLAPSED);
            if (arrow != null) arrow.removeStyleName(FormConstants.ROTATE_90);
            getElement().getStyle().clearHeight();
        }
        else {
            final int height = getOffsetHeight();
            if (heightAuto && height != 0) {
                getElement().getStyle().setHeight(height, Style.Unit.PX);
                heightAuto = false;
            }
            addStyleName(FormConstants.COLLAPSED);
            if (arrow != null) arrow.addStyleName(FormConstants.ROTATE_90);
        }
        collapsed = !collapsed;
        childPanelsDiv.setVisible(!collapsed);
        changeHandler.onValueChange(null);
    }

    /** Method to correct widgetMessages inside a vertical group. */
    private boolean isMessageAndGroupIsVertical(WidgetUI w) {
        return w instanceof WidgetMessageUI && getModel().getWidgetType() == VERTICAL && !w.getStyleName().contains("col-");
    }

    //~ Methods ......................................................................................................................................

    static FlowPanel createGroupDiv(WidgetType type) {
        final FlowPanel div = HtmlWidgetFactory.div();
        addStyles(type, div);
        return div;
    }

    @SuppressWarnings("IfStatementWithTooManyBranches")
    private static void addStyles(WidgetType widgetType, UIObject ui) {
        if (widgetType == VERTICAL) ui.addStyleName("verti row");
        else if (widgetType == HORIZONTAL) ui.addStyleName("hori row");
        else if (widgetType == HEADER)
        // ui.addStyleName(FORM_INLINE);
        ui.addStyleName(PAGE_HEADER + " form-group");
        else if (widgetType == FOOTER) {
            ui.addStyleName(FORM_INLINE);
            ui.addStyleName(FORM_FOOTER_CLASS_NAME);
            ui.addStyleName("row");
        }
        else if (widgetType == DROPDOWN) ui.addStyleName("dropdown-group");
    }  // end method addStyles

    //~ Static Fields ................................................................................................................................

    private static final String FORM_INLINE            = "form-inline";
    static final String         FORM_FOOTER_CLASS_NAME = "form-actions";  // Shouldn't be page-footer? :)
}                                                                 // end class GroupUI
