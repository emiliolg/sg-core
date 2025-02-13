
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
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

import org.jetbrains.annotations.NonNls;

import tekgenesis.metadata.form.model.FormConstants;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_TAB;

import static tekgenesis.view.client.ui.base.HtmlDomUtils.findParentElement;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.targetElement;

/**
 * Modal Glass Background.
 */
@SuppressWarnings("GWTStyleCheck")
public class ModalGlass extends FlowPanel implements ClickHandler, KeyUpHandler {

    //~ Instance Fields ..............................................................................................................................

    private BlurHandler blurHandler = null;

    private ClickHandler currentClickHandler;
    private KeyUpHandler currentKeyHandler;
    private boolean      isOpen;

    //~ Constructors .................................................................................................................................

    /** Creates a ModalGlass with Bootstrap classNames. */
    private ModalGlass() {
        addStyleName("modal-backdrop glass fade");
        currentClickHandler = null;
        currentKeyHandler   = null;
        setVisible(false);
        sinkEvents(Event.ONCLICK);

        final RootPanel rootPanel = RootPanel.get();
        rootPanel.add(this);
        rootPanel.addDomHandler(this, KeyUpEvent.getType());
        addDomHandler(this, ClickEvent.getType());
    }

    //~ Methods ......................................................................................................................................

    @Override public void onClick(ClickEvent event) {
        // glass clicked
        if (currentClickHandler != null) currentClickHandler.onClick(event);
        hideGlass();
    }

    @Override public void onKeyUp(KeyUpEvent e) {
        if (isOpen) {
            switch (e.getNativeKeyCode()) {
            case KEY_TAB:
                if (blurHandler != null) {
                    final Element parentModal = findParentElement(FormConstants.MODAL_CLASS_NAME, targetElement(e), true);
                    if (parentModal == null) blurHandler.onBlur(null);
                }
                break;
            case KeyCodes.KEY_ESCAPE:
                if (currentKeyHandler != null) currentKeyHandler.onKeyUp(e);
                hideGlass();
                break;
            }
        }
    }

    /** Sets a blur handler. */
    public void setBlurHandler(BlurHandler blurHandler) {
        this.blurHandler = blurHandler;
    }

    /** Sets ClickHandler. */
    public void setClickHandler(ClickHandler handler) {
        currentClickHandler = handler;
    }

    /** Sets KeyHandler. */
    public void setKeyHandler(KeyUpHandler handler) {
        currentKeyHandler = handler;
    }

    /** Hides ModalGlass and clears handler reference. */
    private void hideGlass() {
        currentClickHandler = null;  // remove back reference
        currentKeyHandler   = null;
        if (isOpen) {
            isOpen = false;

            removeStyleName("in");
            new Timer() {
                    @Override public void run() {
                        setVisible(false);
                    }
                }.schedule(HIDE_DELAY_MILLIS);
        }
    }

    /** Displays ModalGlass. */
    private void showGlass(boolean transparent) {
        if (!isOpen) {
            isOpen = true;

            if (transparent) addStyleName(TRANSPARENT);
            else removeStyleName(TRANSPARENT);
            setVisible(true);
            new Timer() {
                    @Override public void run() {
                        addStyleName("in");
                    }
                }.schedule(10);
        }
    }

    //~ Methods ......................................................................................................................................

    /** Hides modalGlass. */
    public static void hide() {
        INSTANCE.hideGlass();
    }

    /** Shows modalGlass. */
    public static ModalGlass show(final boolean transparent) {
        INSTANCE.showGlass(transparent);
        return INSTANCE;
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String TRANSPARENT = "transparent";

    private static final ModalGlass INSTANCE          = new ModalGlass();
    private static final int        HIDE_DELAY_MILLIS = 150;
}  // end class ModalGlass
