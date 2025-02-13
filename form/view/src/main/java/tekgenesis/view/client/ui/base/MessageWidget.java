
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
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;

import org.jetbrains.annotations.NotNull;

import tekgenesis.check.CheckType;

import static tekgenesis.metadata.form.model.FormConstants.CLOSE_BUTTON;
import static tekgenesis.metadata.form.model.FormConstants.STRONG_TAG;
import static tekgenesis.view.client.ui.WidgetMessageUI.ALERT_STYLE_CLASS_NAME;

/**
 * Renders a message.
 */
public class MessageWidget extends FlowPanel implements IsWidget {

    //~ Constructors .................................................................................................................................

    /** Creates a Message widget. */
    public MessageWidget(@NotNull String title, @NotNull String msg, CheckType type) {
        addStyleName(ALERT_STYLE_CLASS_NAME);
        addStyleName("alert-" + type.getDecorationClass(false));
        final Anchor close = new Anchor("×");
        close.addStyleName(CLOSE_BUTTON);
        close.addClickHandler(event -> removeFromParent());
        add(close);
        final SpanElement spanElement = Document.get().createSpanElement();
        spanElement.setInnerHTML(STRONG_TAG + title + " </strong>" + msg);
        getElement().appendChild(spanElement);
    }
}
