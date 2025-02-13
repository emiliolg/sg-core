
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
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.RootPanel;

import tekgenesis.check.CheckType;
import tekgenesis.view.client.ui.ExtendedHtmlSanitizer;

import static tekgenesis.view.client.ui.WidgetMessageUI.ALERT_HEADING;
import static tekgenesis.view.client.ui.WidgetMessageUI.ALERT_STYLE_CLASS_NAME;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.span;

/**
 * A fancy Alert msg.
 */
public class ModalAlert extends ClickablePanel implements ClickHandler, KeyUpHandler {

    //~ Instance Fields ..............................................................................................................................

    final FlowPanel              container = div();
    private final HeadingElement header;
    private final InlineHTML     label     = span();

    //~ Constructors .................................................................................................................................

    ModalAlert(boolean closeOnClick) {
        setVisible(false);
        setStyleName("modal-alert");
        if (closeOnClick) addClickHandler(this);

        container.addStyleName(ALERT_STYLE_CLASS_NAME);
        container.addStyleName("alert-block modalContainer");
        container.setStylePrimaryName(ALERT_STYLE_CLASS_NAME);
        add(container);

        header = Document.get().createHElement(4);
        header.addClassName(ALERT_HEADING);
        container.getElement().insertFirst(header);
        container.add(label);

        final RootPanel rootPanel = RootPanel.get();
        rootPanel.add(this);
    }

    //~ Methods ......................................................................................................................................

    @Override public void onClick(final ClickEvent event) {
        event.stopPropagation();
        hide();
    }

    @Override public void onKeyUp(final KeyUpEvent keyUpEvent) {
        hide();
    }

    void hide() {
        setVisible(false);
        header.setInnerText(null);
        label.setText(null);
        ModalGlass.hide();
    }

    void showAlert(final String headerMsg, final String msg, final CheckType type) {
        header.setInnerText(headerMsg);
        label.setHTML(ExtendedHtmlSanitizer.sanitizeHtml(msg));
        container.setStyleDependentName(type.getDecorationClass(false), true);

        final ModalGlass glass = ModalGlass.show(false);
        glass.setClickHandler(this);
        glass.setKeyHandler(this);

        setVisible(true);
    }

    //~ Methods ......................................................................................................................................

    /** Shows a fancy Alert message. */
    @SuppressWarnings("WeakerAccess")
    public static void show(final String headerMsg, final String msg) {
        show(headerMsg, msg, CheckType.ERROR);
    }

    /** Shows a fancy Alert message. */
    @SuppressWarnings({ "WeakerAccess", "NonThreadSafeLazyInitialization" })
    public static void show(final String headerMsg, final String msg, final CheckType type) {
        if (instance == null) instance = new ModalAlert(true);
        instance.showAlert(headerMsg, msg, type);
    }

    //~ Static Fields ................................................................................................................................

    private static ModalAlert instance = null;
}  // end class ModalAlert
