
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.view.client.ui.modal.ModalListener;

import static tekgenesis.metadata.form.model.FormConstants.*;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;

/**
 * Popup Modal.
 */
public class ImageModal extends FlowPanel implements ClickHandler {

    //~ Instance Fields ..............................................................................................................................

    private Boolean                   active              = null;
    private Button                    closeButton         = null;
    private HandlerRegistration       handlerRegistration = null;
    private Boolean                   isGlassEnabled      = null;
    private KeyUpHandler              keyUpHandler        = null;
    private final List<ModalListener> listeners;
    private FlowPanel                 modalBody           = null;
    private Panel                     modalContainer      = null;

    //~ Constructors .................................................................................................................................

    /** Creates a Modal focusPanel as modalContainer. */
    public ImageModal() {
        this(new FlowPanel());
    }

    /** Creates a Modal with a custom Panel as a container. */
    private ImageModal(Panel panel) {
        modalContainer = panel;

        setStyleName(FLEX_BOX_CONTAINER);
        final FlowPanel flexBox = div();
        flexBox.addStyleName(FLEX_BOX);
        addDomHandler(this, ClickEvent.getType());

        addStyleName(Z_HIDDEN);
        modalContainer.setStyleName("fade");
        modalContainer.addDomHandler(event -> {
                event.preventDefault();
                event.stopPropagation();
            },
            ClickEvent.getType());

        active = false;

        sinkEvents(Event.ONKEYUP);

        modalBody = div();
        modalBody.setStyleName(MODAL_BODY);
        addCloseButton();

        modalContainer.add(modalBody);
        flexBox.add(modalContainer);
        super.add(flexBox);

        keyUpHandler        = null;
        handlerRegistration = null;

        setGlassEnabled(true);
        listeners = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    /** Adds a widget to the modal body. */
    public void add(Widget w) {
        modalBody.add(w);
    }

    /** Add Show Hide Listener. */
    public void addDisplayListener(final ModalListener listener) {
        listeners.add(listener);
    }

    @Override public void clear() {
        modalBody.clear();
    }

    @Override public void onClick(ClickEvent event) {
        hide();
    }

    @Override public boolean remove(Widget w) {
        return modalBody.remove(w);
    }

    /** Shows modal and modalGlass. */
    public void show() {
        removeStyleName(Z_HIDDEN);
        modalContainer.addStyleName("in");

        // ??????
        if (!active && keyUpHandler != null && handlerRegistration == null)
            handlerRegistration = RootPanel.get().addDomHandler(keyUpHandler, KeyUpEvent.getType());

        if (isGlassEnabled && !active) {
            active = true;
            final ModalGlass glass = ModalGlass.show(false);
            glass.setBlurHandler(event -> closeButton.setFocus(true));
            glass.setClickHandler(this);
            glass.setKeyHandler(event -> hide());
        }
        for (final ModalListener listener : listeners)
            listener.onShow();
    }

    /** Set modal content. */
    public void setContent(Widget w) {
        clear();
        modalBody.add(w);
    }

    /** Adds prev-next buttons with its handlers and keyHandler for keyboard support. */
    public void setPrevNextListener(final Listener listener) {
        final Anchor prev = HtmlWidgetFactory.anchor();
        Icon.replaceInElement(prev.getElement(), IconType.CHEVRON_LEFT.getClassName());
        prev.addClickHandler(event -> listener.onPrevious());
        prev.setStyleName("left image-control");

        final Anchor next = HtmlWidgetFactory.anchor();
        Icon.replaceInElement(next.getElement(), IconType.CHEVRON_RIGHT.getClassName());
        next.addClickHandler(event -> listener.onNext());
        next.setStyleName("right image-control");

        final RootPanel rootPanel = RootPanel.get();
        keyUpHandler        = event -> {
                                  final int keyCode = event.getNativeEvent().getKeyCode();
                                  if (keyCode == KeyCodes.KEY_LEFT && isActive()) listener.onPrevious();
                                  if (keyCode == KeyCodes.KEY_RIGHT && isActive()) listener.onNext();
                              };
        handlerRegistration = rootPanel.addDomHandler(keyUpHandler, KeyUpEvent.getType());

        modalContainer.add(prev);
        modalContainer.add(next);
    }

    /** Adds Close button. */
    private void addCloseButton() {
        closeButton = HtmlWidgetFactory.button(CLOSE_ICON);
        closeButton.setStyleName("close-image");
        closeButton.addClickHandler(this);
        modalContainer.add(closeButton);
    }

    /** Hides modal and modalGlass. */
    private void hide() {
        active = false;
        modalContainer.removeStyleName("in");
        if (isGlassEnabled) ModalGlass.hide();
        if (handlerRegistration != null) {
            handlerRegistration.removeHandler();
            handlerRegistration = null;
        }
        addStyleName(Z_HIDDEN);
        for (final ModalListener listener : listeners)
            listener.onHide(null);
    }

    /** Returns if modal is active. */
    private Boolean isActive() {
        return active;
    }

    /** Enables modal background. */
    private void setGlassEnabled(boolean enabled) {
        isGlassEnabled = enabled;
    }

    //~ Inner Interfaces .............................................................................................................................

    public interface Listener {
        /** Called to get next image. */
        void onNext();

        /** Called to get previous image. */
        void onPrevious();
    }
}  // end class ImageModal
