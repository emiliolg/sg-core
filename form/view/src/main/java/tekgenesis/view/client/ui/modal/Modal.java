
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.modal;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.FooterType;
import tekgenesis.view.client.FormBox;
import tekgenesis.view.client.ui.FormUI;
import tekgenesis.view.client.ui.WidgetUI;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.modal.ModalListener.ModalButtonType;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_ESCAPE;
import static com.google.gwt.user.client.History.addValueChangeHandler;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.metadata.form.model.FormConstants.*;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;
import static tekgenesis.view.client.ui.modal.ModalListener.ModalButtonType.VALIDATE_OK;

/**
 * Application Modal.
 */
public class Modal extends SimplePanel implements HasCloseHandlers<Modal> {

    //~ Instance Fields ..............................................................................................................................

    final FlowPanel body;

    final FlowPanel           footer;
    final FlowPanel           header;
    private ModalSubscription active;
    private boolean           allowClosing;
    private ModalButtonType   buttonClicked;
    private boolean           closeByClickEnabled;
    private final FlowPanel   container;
    private ModalListener     currentListener;
    private FormBox           formBox     = null;
    private final FlowPanel   glass;
    private boolean           hasFormBox;
    private HTML              headerLabel = null;
    private boolean           showing;

    //~ Constructors .................................................................................................................................

    /** Default modal constructor. */
    public Modal() {
        header = div();
        body   = div();
        footer = div();

        active              = null;
        buttonClicked       = null;
        currentListener     = null;
        hasFormBox          = false;
        allowClosing        = true;
        showing             = false;
        closeByClickEnabled = true;

        header.addStyleName("modal-header");
        body.addStyleName(MODAL_BODY);
        footer.addStyleName("modal-footer");

        container = div();
        final FlowPanel dialog = div();
        dialog.addStyleName(MODAL_DIALOG);
        dialog.addStyleName("auto-width-modal");

        container.addStyleName("modal-content");
        container.add(header);
        container.add(body);
        container.add(footer);

        addValueChangeHandler(event -> hide());

        glass = new FlowPanel();
        glass.addStyleName("glass fade");
        RootPanel.get().add(this);
        RootPanel.get().add(glass);

        addCloseHandler(event -> {
            if (currentListener != null) {
                currentListener.onHide(buttonClicked);
                currentListener = null;
            }
        });

        dialog.add(container);
        add(dialog);
        setVisible(showing);
    }  // end ctor Modal

    //~ Methods ......................................................................................................................................

    @Override public HandlerRegistration addCloseHandler(CloseHandler<Modal> handler) {
        return addHandler(handler, CloseEvent.getType());
    }

    /** Adds footer close button. */
    public void addFooterCloseButton() {
        final Button closeButton = HtmlWidgetFactory.button(MSGS.close());
        closeButton.addStyleName("btn");
        closeButton.addClickHandler(event -> hide());

        addInFooter(closeButton);
    }

    /** Adds a widget to the modal footer. */
    public void addInFooter(Widget w) {
        // - 1 for the tab limit
        // footer.insert(w, footer.getWidgetCount() - 1);
        footer.add(w);
    }

    @Override public void clear() {
        header.clear();
        body.clear();
        footer.clear();
    }

    /** Hides Modal. */
    public void hide() {
        hide(false);
    }

    /**
     * Hides Modal, indicating if its because the user clicked outside the modal or with any other
     * way.
     */
    public void hide(boolean byClick) {
        if (showing && allowClosing && (!byClick || closeByClickEnabled)) {
            glass.removeStyleName(MODAL_BACKDROP_IN);
            CloseEvent.fire(this, this, false);
            showing = false;
            toggleBodyElementScroll();
            setVisible(false);
        }
    }

    /** Show modal. */
    public ModalSubscription show(final ModalContent content) {
        setOnlyBody(content.isBodyOnly());

        final String modalClassName = content.getModalClassName();
        if (!isEmpty(modalClassName)) addStyleName(modalClassName);
        setStyleName("modal modals " + (!isEmpty(modalClassName) ? modalClassName : ""));

        if (!content.isTransparent()) glass.addStyleName(MODAL_BACKDROP_IN);

        formBox    = content.getFormBox();
        hasFormBox = formBox != null;
        if (hasFormBox) {
            setContent(content.getTitle(), content.getFormBox());
            addModalButtonsInFooter(content.getFooterType());
        }
        else {
            setContent(content.getTitle(), content.getBody());
            setFooter(content.getFooter());
            setHeader(content.getHeader());
        }

        if (content.isCloseButton() || content.getFooterType() == FooterType.NONE) addCloseButton();

        currentListener = content.getListener();
        if (currentListener != null) currentListener.onShow();
        addTabLimits();
        addStyleName(MODAL_VISIBLE);
        addStyleName("modal-box");
        addStyleName("form-horizontal");
        show();
        setFirstElementFocus();

        allowClosing        = !content.isFixed();
        closeByClickEnabled = !content.isClickOutsideDisabled();

        active = new ModalSubscription() {
                @Override public void hide() {
                    if (this == active) {
                        allowClosing = true;
                        removeStyleName(MODAL_VISIBLE);
                        Modal.this.hide();
                    }
                }

                @Override public void updateLabel(String label) {
                    updateHeader(label);
                }
            };

        return active;
    }  // end method show

    public FlowPanel getContainer() {
        return container;
    }

    /** Set modal container. */
    public Modal setContent(String label, Widget contentDiv) {
        clear();
        if (contentDiv != null) body.add(contentDiv);
        if (label != null) setHeader(label);
        return this;
    }

    /** Set modal content. */
    public void setContent(String title, final FormBox w) {
        clear();
        setHeader(title);
        w.attach(body);
    }

    /** Determines whether or not this popup is showing. */
    public boolean isShowing() {
        return showing;
    }

    /** Returns true if modal has no FormBox. */
    public boolean isTransient() {
        return !hasFormBox;
    }

    private void addCloseButton() {
        final Anchor x = anchor();
        x.setHTML(TIMES);
        x.addStyleName(CLOSE_BUTTON);
        x.addClickHandler(event -> hide());

        if (header.isVisible()) header.add(x);
        else body.insert(x, 0);
    }

    private void addFooterButton(ModalButtonType... types) {
        for (final ModalButtonType type : types)
            addFooterButton(type.getButtonClass(),
                type.getText(),
                event -> {
                    buttonClicked = type;
                    if (type != VALIDATE_OK || formBox.getCurrent().validate(Option.empty()).isValid()) hide();
                });
    }

    private void addFooterButton(String buttonClass, String buttonText, ClickHandler clickHandler) {
        final Button button = HtmlWidgetFactory.button(buttonText);
        button.addStyleName("btn");
        button.addStyleName(buttonClass);
        button.addClickHandler(clickHandler);

        addInFooter(button);
    }

    private void addKeyHandler() {
        addDomHandler(event -> {
                if (event.getNativeKeyCode() == KEY_ESCAPE) hide();
            },
            KeyDownEvent.getType());
    }

    private void addModalButtonsInFooter(@NotNull final FooterType type) {
        switch (type) {
        case OK_CANCEL:
            addFooterButton(ModalButtonType.OK, ModalButtonType.CANCEL);
            break;
        case CLOSE:
            addFooterButton(ModalButtonType.CLOSE);
            break;
        case OK_CANCEL_VALIDATE:
            addFooterButton(VALIDATE_OK, ModalButtonType.CANCEL);
            break;
        case NONE:
            break;
        }
    }

    private void addTabLimits() {
        final Anchor bottomTabLimit = anchor("l");
        final Anchor topTabLimit    = anchor("l");
        bottomTabLimit.addStyleName(TAB_LIMIT);
        topTabLimit.addStyleName(TAB_LIMIT);

        final FocusHandler handler = event -> {
                                         event.preventDefault();
                                         setFirstElementFocus();
                                     };
        bottomTabLimit.addFocusHandler(handler);
        topTabLimit.addFocusHandler(handler);

        if (header.isVisible()) header.insert(topTabLimit, 0);
        else body.insert(topTabLimit, 0);

        if (footer.isVisible()) footer.add(bottomTabLimit);
        else body.add(bottomTabLimit);
    }

    private void show() {
        showing = true;
        setVisible(true);
        toggleBodyElementScroll();
        // start showing
    }

    private void toggleBodyElementScroll() {
        if (showing) Document.get().getBody().addClassName(NO_SCROLL);
        else Document.get().getBody().removeClassName(NO_SCROLL);
    }

    private void updateHeader(String title) {
        final int widgetIndex = header.getWidgetIndex(headerLabel);
        if (widgetIndex < 0) {
            headerLabel = new HTML("<h3>" + title + "</h3>");
            header.add(headerLabel);
        }
        else {
            header.remove(widgetIndex);
            headerLabel = new HTML("<h3>" + title + "</h3>");
            header.insert(headerLabel, widgetIndex);
        }
    }

    /** Sets focus on the first Focusable widget in ModalBody. */
    private void setFirstElementFocus() {
        setFirstElementFocus(container);
    }

    private boolean setFirstElementFocus(Iterable<Widget> wContainer) {
        for (final Widget ui : wContainer) {
            // subforms
            if (ui instanceof FormUI) {
                ((FormUI) ui).focusFirst();
                return true;
            }

            // recursive
            if (ui instanceof Iterable) {
                final Iterable<Widget> hasWidgets = cast(ui);
                if (setFirstElementFocus(hasWidgets)) return true;
            }

            // found focusable
            if (ui instanceof Focusable && !ui.getStyleName().contains(TAB_LIMIT)) {
                if (!(ui instanceof WidgetUI) || (((WidgetUI) ui).acceptsFocus())) {
                    ((Focusable) ui).setFocus(true);
                    return true;
                }
            }
        }
        return false;
    }

    private void setFooter(Widget footerDiv) {
        if (footerDiv != null) {
            footer.setVisible(true);
            footer.add(footerDiv);
        }
        else footer.setVisible(false);
    }

    private void setHeader(String title) {
        headerLabel = new HTML("<h3>" + title + "</h3>");
        header.add(headerLabel);
    }

    private void setHeader(Widget headerDiv) {
        if (headerDiv != null) {
            header.setVisible(true);
            header.add(headerDiv);
        }
    }

    /** Hides header and footer. */
    private void setOnlyBody(boolean bodyOnly) {
        header.setVisible(!bodyOnly);
        footer.setVisible(!bodyOnly);
    }

    //~ Static Fields ................................................................................................................................

    public static final String  MODAL_BACKDROP_IN = "modal-backdrop in";
    private static final String NO_SCROLL         = "no-scroll";

    private static final String MODAL_VISIBLE = "modal-visible";
}
