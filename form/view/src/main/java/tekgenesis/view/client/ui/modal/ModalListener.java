
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.modal;

import org.jetbrains.annotations.Nullable;

import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * Listener for Modal Dialogs.
 */
public interface ModalListener {

    //~ Instance Fields ..............................................................................................................................

    ModalListener EMPTY = new ModalListener() {
            @Override public void onHide(ModalButtonType buttonClicked) {}

            @Override public void onShow() {}
        };

    String OK_SUBFORM = "ok-subform";

    //~ Methods ......................................................................................................................................

    /**
     * Called after executing {hide()}.
     *
     * @param  buttonClicked:  button clicked to hide modal, null if no button clicked
     */
    void onHide(@Nullable ModalButtonType buttonClicked);

    /** Called after executing {show()}. */
    void onShow();

    //~ Enums ........................................................................................................................................

    enum ModalButtonType {
        OK(MSGS.ok(), OK_SUBFORM), CANCEL(MSGS.cancel(), "cancel-subform"), CLOSE(MSGS.close(), "close-subform"), VALIDATE_OK(MSGS.ok(), OK_SUBFORM),;
        private final String buttonClass;

        private final String text;

        ModalButtonType(String text, String buttonClass) {
            this.text        = text;
            this.buttonClass = buttonClass;
        }

        /** Get button class. */
        public String getButtonClass() {
            return buttonClass;
        }

        /** Get button text. */
        public String getText() {
            return text;
        }

        /** array with ok and cancel buttons. */
        public static ModalButtonType[] okCancel() {
            return new ModalButtonType[] { CANCEL, OK };
        }
    }
}  // end interface ModalListener
