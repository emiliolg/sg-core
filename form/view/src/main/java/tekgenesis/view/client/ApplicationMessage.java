
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import java.util.List;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.metadata.form.model.FormConstants.CLOSE_BUTTON;
import static tekgenesis.metadata.form.model.FormConstants.TIMES;
import static tekgenesis.view.client.ApplicationMessages.MsgType;
import static tekgenesis.view.client.ui.base.HtmlList.*;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.*;

/**
 * Create a panel used to show feedback messages to the user.
 */
class ApplicationMessage extends FlowPanel {

    //~ Instance Fields ..............................................................................................................................

    private final String  msg;
    private final Anchor  sendFeedback;
    private final MsgType type;
    private final Anchor  x;

    //~ Constructors .................................................................................................................................

    /** Creates the feedback messages panel. */
    ApplicationMessage(MsgType type, String msg, List<String> errorList, boolean rel) {
        this.type = type;
        this.msg  = msg;

        setStyleName("fade");

        final FlowPanel  contentPanel = div();
        final InlineHTML msgPanel     = span();
        msgPanel.setHTML(this.msg);
        contentPanel.add(msgPanel);

        sendFeedback = anchor(FormViewMessages.MSGS.sendFeedback());
        sendFeedback.setVisible(false);
        sendFeedback.setStyleName("margin-left-5");
        contentPanel.add(sendFeedback);

        contentPanel.setStyleName(ALERT_STYLE_PREFIX + type.name().toLowerCase());

        if (rel) {
            final Anchor reload = anchor();
            reload.setText(FormViewMessages.MSGS.reload());
            reload.setStyleName("margin-left-5");
            reload.addClickHandler(click -> FormHistory.getFormHistory().reloadWithoutConfirmChanges());
            contentPanel.add(reload);
        }

        x = anchor();
        x.setHTML(TIMES);
        x.addStyleName(CLOSE_BUTTON);
        contentPanel.add(x);

        if (!errorList.isEmpty()) {
            final Unordered errors = ul();
            for (final String errorString : errorList)
                errors.add(li(errorString));
            contentPanel.add(errors);
        }

        add(contentPanel);
    }  // end ctor ApplicationMessage

    //~ Methods ......................................................................................................................................

    public void addClickHandler(ClickHandler close) {
        x.addClickHandler(close);
    }

    @Override public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        final ApplicationMessage m = (ApplicationMessage) obj;
        return type == m.type && (msg.equals(m.msg));
    }

    @Override public int hashCode() {
        int result = msg.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    public MsgType getType() {
        return type;
    }

    void attachSendFeedback(@NotNull final ClickHandler handler) {
        sendFeedback.addClickHandler(handler);
        sendFeedback.setVisible(true);
    }

    //~ Static Fields ................................................................................................................................

    private static final String ALERT_STYLE_PREFIX = "alert alert-";
}  // end class ApplicationMessage
