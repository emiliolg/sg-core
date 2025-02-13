
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

import org.jetbrains.annotations.NonNls;

import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.view.client.SwipeFormBox;
import tekgenesis.view.shared.response.DetailResponse;

import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.clickableDiv;
import static tekgenesis.view.client.ui.base.SwipePopup.*;

/**
 * Detail popup panel used to show detail form.
 */
public class DetailPopup extends PopupPanel {

    //~ Instance Fields ..............................................................................................................................

    private SwipeFormBox        box                 = null;
    private HandlerRegistration handlerRegistration = null;

    private final FlowPanel holder;

    //~ Constructors .................................................................................................................................

    /** Creates a DetailPopup. */
    public DetailPopup(DetailResponse response) {
        super(true);

        setStyleName(SWIPE_POPUP_FULL_SCREEN);

        if (response.isFullscreen()) {
            getElement().getStyle().setWidth(100, Style.Unit.PCT);
            getElement().getStyle().setHeight(100, Style.Unit.PCT);
        }
        else {
            setGlassStyleName(SWIPE_POPUP_GLASS);
            setGlassEnabled(true);
            resize(response.getMarginTop(), response.getWidth(), response.getHeight());
        }

        holder = new FlowPanel();

        final ClickablePanel close = clickableDiv();
        close.setStyleName(SWIPE_CLOSE_CONTAINER_CLICKABLE);
        close.addClickHandler(event -> hide());

        final Icon closeIcon = new Icon(IconType.TIMES_CIRCLE);
        closeIcon.addStyleName(SWIPE_CLOSE_ICON_3X);
        close.add(closeIcon);
        holder.add(close);

        handlerRegistration = RootPanel.get().addDomHandler(event -> {
                    if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) hide();
                },
                KeyDownEvent.getType());

        add(holder);
    }

    //~ Methods ......................................................................................................................................

    @Override public void hide(boolean autoClosed) {
        Document.get().getBody().getStyle().clearOverflow();

        if (handlerRegistration != null) handlerRegistration.removeHandler();
        if (box != null) box.detach();
        super.hide(autoClosed);
    }

    /** Empties popup, adds form and opens it. */
    public void show(SwipeFormBox b) {
        Document.get().getBody().getStyle().setOverflow(Style.Overflow.HIDDEN);  // prevent body scroll

        box = b;
        box.attach(holder);
        show();
    }

    /** Resize swipper based on size properties. */
    private void resize(int marginTop, final int width, final int height) {
        addStyleName(SWIPE_POPUP);
        final int w = width != 0 ? width : DEFAULT_WIDTH;
        getElement().getStyle().setMarginLeft(-w / 2, Style.Unit.PX);
        getElement().getStyle().setMarginTop(marginTop, Style.Unit.PX);
        getElement().getStyle().setWidth(w, Style.Unit.PX);
        getElement().getStyle().setHeight(height != 0 ? height : DEFAULT_HEIGHT, Style.Unit.PX);
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String SWIPE_POPUP             = "swipePopup";
    @NonNls public static final String SWIPE_POPUP_GLASS       = "swipePopupGlass";
    @NonNls public static final String SWIPE_POPUP_FULL_SCREEN = "swipePopupFullscreen";
}  // end class DetailPopup
