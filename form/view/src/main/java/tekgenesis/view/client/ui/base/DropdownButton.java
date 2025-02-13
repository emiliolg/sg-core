
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;

import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.view.client.RootInputHandler;
import tekgenesis.view.client.ui.LabelUI;
import tekgenesis.view.client.ui.WidgetMessages;
import tekgenesis.view.client.ui.base.HtmlList.Unordered;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.metadata.form.model.FormConstants.DROPDOWN_MENU;

/**
 * Dropdown Button.
 */
@SuppressWarnings("GWTStyleCheck")
public class DropdownButton extends FlowPanel implements ClickHandler, MouseMoveHandler, MouseOutHandler {

    //~ Instance Fields ..............................................................................................................................

    private final ExtButton dropDownButton  = new ExtButton();
    private final Unordered menu            = HtmlWidgetFactory.ul();
    private WidgetMessages  tooltipMessages = null;

    //~ Constructors .................................................................................................................................

    /** Creates a Dropdown Button. */
    public DropdownButton() {
        addStyleName("btn-group button-widget");
        menu.setStyleName(DROPDOWN_MENU);
        dropDownButton.addStyleName("dropdown-toggle");
        dropDownButton.getElement().setAttribute(FormConstants.DATA_TOGGLE, FormConstants.DROPDOWN);

        dropDownButton.addClickHandler(this);
        menu.addDomHandler(this, ClickEvent.getType());

        super.add(dropDownButton);
        super.add(menu);
    }

    //~ Methods ......................................................................................................................................

    public void add(Widget w) {
        final HtmlList.Item li = HtmlWidgetFactory.li();
        li.add(w);
        menu.add(li);
    }

    /** Add tooltip. */
    public void addTooltip() {
        tooltipMessages = WidgetMessages.create(this, true);
        dropDownButton.addDomHandler(this, MouseMoveEvent.getType());
        dropDownButton.addDomHandler(this, MouseOutEvent.getType());
    }

    @Override public void clear() {
        menu.clear();
    }

    @Override public void onClick(ClickEvent event) {
        event.stopPropagation();
        final boolean isActive = getStyleElement().hasClassName("open");
        RootInputHandler.getInstance().handleDropDowns();
        toggleDropdown(isActive);
    }

    @Override public void onMouseMove(MouseMoveEvent event) {
        // Move the tooltip along with the mouse position
        tooltipMessages.hide();
        tooltipMessages.show(event.getClientX(), event.getClientY());
    }

    @Override public void onMouseOut(MouseOutEvent event) {
        tooltipMessages.hide();
    }

    /** Returns dropDownButton Element. */
    public Element getButtonElement() {
        return dropDownButton.getElement();
    }

    /** Adds Caret to button. */
    public void setCaret() {
        dropDownButton.setCaret();
    }

    /** Returns Focus Target. */
    public Focusable getFocusTarget() {
        return dropDownButton;
    }

    /** Set Icon to button. */
    public void setIcon(IconType type) {
        dropDownButton.setIcon(type);
    }

    /** Set main button action. */
    public Anchor setMainButton(LabelUI w) {
        final Anchor anchor = notNull(w.getAnchor(), new Anchor(w.getLabel()));
        anchor.addStyleName("btn btn-default main-dropdown-button");
        super.insert(anchor, 0);
        return anchor;
    }

    /** Set main button text. */
    public void setMainText(String s) {
        dropDownButton.setText(s);
    }

    /** Set button style. */
    public void setStyle(String styleName) {
        dropDownButton.addStyleName(styleName);
    }

    /** Set tooltip text. */
    public void setTooltip(String tip) {
        if (tooltipMessages != null) tooltipMessages.tooltipmsg(tip);
    }

    public boolean isMenuEmpty() {
        return !menu.iterator().hasNext();
    }

    private void toggleDropdown(boolean isActive) {
        if (!isActive) addStyleName("open");
        // if (!isMenuEmpty()) leaveInsideScreen(this::checkLeft, this::checkRight);
    }

    //~ Static Fields ................................................................................................................................

    private static final String TO_LEFT = "to-left";

    private static final String MAIN_DROPDOW_BUTTON = "main-dropdown-button";
}  // end class DropdownButton
