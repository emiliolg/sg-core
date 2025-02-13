
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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasText;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.model.FormConstants.VIEW_DISABLED;
import static tekgenesis.view.client.ui.HasLinkUI.FormLinkUtil.hasLink;
import static tekgenesis.view.client.ui.HasLinkUI.FormLinkUtil.link;
import static tekgenesis.view.client.ui.HasLinkUI.FormLinkUtil.updateLinkPk;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.span;
import static tekgenesis.view.client.ui.base.ModalOkCancelAlert.showOkCancelAlert;

/**
 * A Label UI widget.
 */
public class LabelUI extends WidgetUI implements HasLabelUI, HasLinkUI, HasTooltipUI, MouseMoveHandler, MouseOutHandler, HasConfirmUI, ClickHandler {

    //~ Instance Fields ..............................................................................................................................

    private final com.google.gwt.user.client.ui.Widget component;
    private String                                     confirmationText;
    @Nullable private String                           linkPk          = null;
    private WidgetMessages                             tooltipMessages = null;

    //~ Constructors .................................................................................................................................

    /** Creates a Label UI widget. */
    public LabelUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        component        = hasLink(model) ? link(model) : span();
        confirmationText = "";
        initWidget(component);

        // When having a tooltip expression, initialize a tooltip object and add mouse handlers
        if (!model.getTooltip().isEmpty()) {
            tooltipMessages = WidgetMessages.create((FlowPanel) div, true);
            component.addDomHandler(this, MouseMoveEvent.getType());
            component.addDomHandler(this, MouseOutEvent.getType());
            component.addDomHandler(this, ClickEvent.getType());
        }
    }

    //~ Methods ......................................................................................................................................

    /** Adds a click handler to this widget or to the confirmation dialog if required. */
    public void addClickHandler(final ClickHandler handler) {
        if (getModel().getConfirm().isEmpty()) ((FocusWidget) component).addClickHandler(handler);
        else ((FocusWidget) component).addClickHandler(event -> {
            if (isEmpty(confirmationText)) handler.onClick(null);
            else showOkCancelAlert(this, confirmationText, handler);
        });
    }

    @Override public void disableElement(boolean disable) {
        if (hasLink(getModel())) {
            final Anchor a = (Anchor) component;
            if (disable) setEnabled(a, false);                                      // force disable
            else setEnabled(a, !isDisabled());                                      // previous state
        }
    }

    @Override public void onClick(ClickEvent event) {
        hideTooltipMessage();
    }

    @Override public void onMouseMove(MouseMoveEvent event) {
        // Move the tooltip along with the mouse position
        hideTooltipMessage();
        tooltipMessages.show(event.getClientX(), event.getClientY());
    }

    @Override public void onMouseOut(MouseOutEvent event) {
        hideTooltipMessage();
    }

    /** Sets a custom href to the text. */
    @Nullable public Anchor getAnchor() {
        return (component instanceof Anchor) ? (Anchor) component : null;
    }

    /** Adds confirmation text to this Button UI widget. */
    public void setConfirmationText(String s) {
        confirmationText = s;
    }

    @Override public void setDisabled(boolean disabled) {
        // first call super, this will set disabled state on widget, but not on the UI.
        super.setDisabled(disabled);

        // disable/enable UI element now
        disableElement(disabled);
    }

    @Override public String getLabel() {
        return ((HasText) component).getText();
    }

    @Override public void setLabel(String label) {
        ((HasText) component).setText(label);
        Icon.inWidget(component, getModel().getIconStyle());
    }

    @Override public void setLabelFromExpression(String label) {
        ((HasText) component).setText(label);
        Icon.inWidget(component, getModel().getIconStyle());
    }

    /** Sets a custom href to the text. */
    public void setLink(final String href) {
        ((Anchor) component).setHref(href);
    }

    @Nullable @Override public String getLinkPk() {
        return linkPk;
    }

    @Override public void setLinkPk(@Nullable String pk) {
        updateLinkPk((Anchor) component, getModel(), linkPk = pk);
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

    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName("textLabel");
    }

    @NotNull @Override Option<Element> createIcon() {
        return some(component.getElement());
    }

    private void hideTooltipMessage() {
        tooltipMessages.hide();
    }

    private void setEnabled(Anchor a, boolean enable) {
        if (enable) a.removeStyleName(VIEW_DISABLED);
        else a.addStyleName(VIEW_DISABLED);
    }
}  // end class LabelUI
