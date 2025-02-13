
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

import tekgenesis.check.CheckType;
import tekgenesis.view.client.FormViewMessages;
import tekgenesis.view.client.ui.WidgetUI;

import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * A fancy Ok/Cancel dialog message.
 */
public class ModalOkCancelAlert extends ModalAlert {

    //~ Instance Fields ..............................................................................................................................

    private OkCancelCallback callback = null;

    //~ Constructors .................................................................................................................................

    private ModalOkCancelAlert() {
        super(false);

        final FlowPanel modalFooter = new FlowPanel();
        modalFooter.setStyleName("okCancelModalFooter");

        final Button cancelButton = HtmlWidgetFactory.button(MSGS.cancel());
        cancelButton.addClickHandler(this);

        final Button okButton = HtmlWidgetFactory.button(FormViewMessages.MSGS.ok());
        okButton.addClickHandler(event -> {
            hide();
            callback.ok();
        });

        modalFooter.add(okButton);
        modalFooter.add(cancelButton);

        container.add(modalFooter);
    }

    //~ Methods ......................................................................................................................................

    @Override public void onClick(final ClickEvent event) {
        super.onClick(event);
        callback.cancel();
    }

    @Override public void onKeyUp(final KeyUpEvent event) {
        super.onKeyUp(event);
        callback.cancel();
    }

    private ModalOkCancelAlert withCallback(OkCancelCallback c) {
        callback = c;
        return this;
    }

    //~ Methods ......................................................................................................................................

    /** Shows a fancy Ok/Cancel dialog message. */
    @SuppressWarnings({ "WeakerAccess", "NonThreadSafeLazyInitialization" })
    public static void show(final String headerMsg, final String msg, final CheckType type, final OkCancelCallback callback) {
        if (instance == null) instance = new ModalOkCancelAlert();
        instance.withCallback(callback).showAlert(headerMsg, msg, type);
    }

    public static void showOkCancelAlert(final WidgetUI ui, final String confirmationText, final ClickHandler handler) {
        ui.setFocus(false);
        show("", confirmationText, CheckType.INFO, new OkCancelCallback() {
                @Override public void cancel() {}
                @Override public void ok() {
                    handler.onClick(null);
                }
            });
    }

    //~ Static Fields ................................................................................................................................

    private static ModalOkCancelAlert instance = null;

    //~ Inner Interfaces .............................................................................................................................

    public interface OkCancelCallback {
        /** Method to be called when the user clicks 'Cancel' button. */
        void cancel();

        /** Method to be called when the user clicks 'Ok' button. */
        void ok();
    }
}  // end class ModalOkCancelAlert
