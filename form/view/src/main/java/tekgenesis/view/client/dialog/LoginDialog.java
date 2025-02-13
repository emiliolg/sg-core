
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.dialog;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.FrameElement;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;

import org.jetbrains.annotations.Nullable;

import tekgenesis.view.client.Application;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.modal.ModalContent;
import tekgenesis.view.client.ui.modal.ModalListener;
import tekgenesis.view.client.ui.modal.ModalSubscription;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Constants.REDIRECTION;
import static tekgenesis.metadata.form.model.FormConstants.MODAL_LOGIN;
import static tekgenesis.security.shiro.web.URLConstants.LOGIN_URI;
import static tekgenesis.security.shiro.web.URLConstants.SUCCESS_URI;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * Login panel.
 */
public class LoginDialog extends ModalContent implements LoadHandler {

    //~ Instance Fields ..............................................................................................................................

    private final Frame frame;

    private boolean           fullReload;
    private ModalSubscription subscription = null;

    //~ Constructors .................................................................................................................................

    /** Creates a login panel. */
    private LoginDialog() {
        frame = HtmlWidgetFactory.frame(LOGIN_URI + "?" + REDIRECTION + "=" + SUCCESS_URI + "&" + MODAL_LOGIN);
        frame.addLoadHandler(this);
        frame.addStyleName("loginFrame");
        setBody(frame);
    }

    //~ Methods ......................................................................................................................................

    @Override public void onLoad(LoadEvent event) {
        final FrameElement frameElement    = cast(frame.getElement());
        final Document     contentDocument = frameElement.getContentDocument();
        if (contentDocument != null) {
            contentDocument.getBody().addClassName(MODAL_LOGIN);
            setPreviousUserIfAny(contentDocument);
            redirectReload(event);
        }
    }  // end method onLoad

    /** Subscription to interact with my father. */
    public void setSubscription(ModalSubscription subscription) {
        this.subscription = subscription;
    }

    private void finish() {
        Application.getInstance().boot();
        Application.getInstance().restartLifeCycles();
        subscription.hide();
    }

    private void locationReload() {
        Window.Location.reload();
    }

    private void redirectReload(LoadEvent event) {
        final IFrameElement iframe = IFrameElement.as(((Frame) event.getSource()).getElement());
        if (iframe != null) {
            final String url = iframe.getContentDocument().getURL();
            if (!url.contains(REDIRECTION + "=" + SUCCESS_URI)) {
                if (fullReload) locationReload();
                else {
                    if (subscription != null) finish();
                }
            }
        }
    }  // end method redirectReload

    private void setPreviousUserIfAny(Document contentDocument) {
        final Element username = contentDocument.getElementById("username");
        if (username != null) {
            username.setAttribute("value", Application.getInstance().getUserid());
            final Element password = contentDocument.getElementById("password");
            if (password != null) password.focus();
        }
    }

    //~ Methods ......................................................................................................................................

    /** Show login dialog. */
    @SuppressWarnings("NonThreadSafeLazyInitialization")  // client side
    public static LoginDialog show(final boolean fullReload, @Nullable ModalListener listener) {
        if (instance == null) instance = new LoginDialog();
        instance.setTitle(fullReload ? MSGS.switchUser() : MSGS.loginTitle());
        instance.fullReload = fullReload;
        instance.setListener(listener);
        return instance;
    }

    /** Returns login dialog instance. */
    public static LoginDialog getInstance() {
        return instance;
    }

    //~ Static Fields ................................................................................................................................

    private static LoginDialog instance = null;
}  // end class LoginDialog
