
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Text;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.view.client.ui.base.ModalOkCancelAlert.showOkCancelAlert;

/**
 * Base Button UI widget class.
 */
abstract class BaseButtonUI extends FieldWidgetUI implements HasConfirmUI, HasTooltipUI, MouseMoveHandler, MouseOutHandler, HasClickUI {

    //~ Instance Fields ..............................................................................................................................

    final Button           button;
    private String         confirmationText;
    private Text           labelElement    = null;
    private WidgetMessages tooltipMessages = null;

    //~ Constructors .................................................................................................................................

    BaseButtonUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model, false);
        confirmationText = "";
        button           = HtmlWidgetFactory.button();
        initWidget(button);
        // setInputWidth(0);
        addStyleName("button-widget");

        // When having a tooltip expression, initialize a tooltip object and add mouse handlers
        if (!model.getTooltip().isEmpty()) {
            tooltipMessages = WidgetMessages.create((FlowPanel) div, true);
            button.addDomHandler(this, MouseMoveEvent.getType());
            button.addDomHandler(this, MouseOutEvent.getType());
        }

        labelElement = Document.get().createTextNode("");
        button.getElement().appendChild(labelElement);
    }

    //~ Methods ......................................................................................................................................

    /** Disables element but not modifies the model. */
    public void disableElement(boolean disable) {
        if (disable) button.setEnabled(false);  // force disable
        else button.setEnabled(!isDisabled());  // previous state
    }

    @Override public void onMouseMove(MouseMoveEvent event) {
        // Move the tooltip along with the mouse position
        tooltipMessages.hide();
        tooltipMessages.show(event.getClientX(), event.getClientY());
    }

    @Override public void onMouseOut(MouseOutEvent event) {
        tooltipMessages.hide();
    }

    /** Adds confirmation text to this Button UI widget. */
    public void setConfirmationText(String s) {
        confirmationText = s;
    }

    /** Is button enabled in ui. Testing purposes */
    public boolean isButtonEnabled() {
        return button.isEnabled();
    }

    @Override public void setDisabled(boolean disabled) {
        super.setDisabled(disabled);
        if (tooltipMessages != null) tooltipMessages.hide();
    }

    @Override public void setInputWidth(int col) {}

    @Override public void setLabelFromExpression(@NotNull final String label) {
        if (isNotEmpty(label)) labelElement.setData(label);
    }

    /** Returns button's text. */
    public String getText() {
        return button.getText();
    }

    /**
     * Override the default tooltip behavior so it is displayed when hovering the image and avoid
     * creating an extra icon.
     *
     * @param  tip  the string message to be displayed
     */
    public void setTooltip(String tip) {
        if (tooltipMessages != null) tooltipMessages.tooltipmsg(tip);
    }

    protected abstract void addButtonStyle();

    protected void addStyleNames() {
        // addStyleName("widget");
        // addStyleName("form-group");
        // final int col    = getModel().getCol();
        // final int offset = getModel().getOffsetCol();
        // if (col > 0) addControlStyleName("col-sm-" + col);
        // if (offset > 0) addControlStyleName("col-sm-offset-" + offset);
        super.addStyleNames();
        addButtonStyle();
    }

    @NotNull @Override Option<Element> createIcon() {
        return some(button.getElement());
    }

    //J-
    /** Adds a click handler to this Button UI widget or to the confirmation dialog if required. */
    public void addClickHandler(final ClickHandler handler) {
        if (getModel().getConfirm().isEmpty()) button.addClickHandler(handler);
        else button.addClickHandler(event -> {
            event.preventDefault();
            event.stopPropagation();
            if (isEmpty(confirmationText)) handler.onClick(null);
            else showOkCancelAlert(this, confirmationText, handler);
        });
    }
    //J+

    /** Sets button's text. */
    void setText(final String text) {
        labelElement.setData(text);
    }
}  // end class BaseButtonUI
