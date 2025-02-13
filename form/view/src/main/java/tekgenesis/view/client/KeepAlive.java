
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import tekgenesis.view.client.dialog.LoginDialog;
import tekgenesis.view.shared.response.FormModelResponse;
import tekgenesis.view.shared.response.Response;
import tekgenesis.view.shared.service.FormService;

import static java.lang.System.currentTimeMillis;

import static tekgenesis.view.client.Application.modal;
import static tekgenesis.view.client.FormHistory.getFormHistory;
import static tekgenesis.view.client.controller.FormController.THOUSAND;
import static tekgenesis.view.client.ui.modal.ModalListener.EMPTY;

/**
 * Session Keep-Alive mechanism.
 */
public class KeepAlive {

    //~ Instance Fields ..............................................................................................................................

    private final Timer keepAlive;
    private long        lastInteraction           = 0;
    private long        lastKnownServerConnection = 0;
    private final Timer pingForLogin;

    private int sessionTimeout = -1;

    //~ Constructors .................................................................................................................................

    /** Builds a keep-alive mechanism for a given timeout. */
    public KeepAlive(int timeout) {
        sessionTimeout = timeout;

        keepAlive = new Timer() {
                @Override public void run() {
                    keepAlive();
                }
            };

        pingForLogin = new Timer() {
                @Override public void run() {
                    pingForLogin();
                }
            };

        lastInteraction = currentTimeMillis();
        scheduleKeepAliveInterval();
    }

    //~ Methods ......................................................................................................................................

    /** Updates last user interaction on client side. */
    public void updateLastInteraction() {
        lastInteraction = currentTimeMillis();
    }

    /** Updates last known server connection time. */
    public void updateLastKnownServerConnection() {
        try {
            updateLastInteraction();
            lastKnownServerConnection = currentTimeMillis();
            scheduleKeepAliveInterval();
        }
        catch (final Exception ignored) {
            // If this happens
        }
    }

    /**
     * Pings the server to keep alive the session if the user has been using the client without
     * interaction for a long time.
     */
    private void keepAlive() {
        if (lastInteraction > lastKnownServerConnection) FormService.App.getInstance().keepAlive(new AsyncCallback<Void>() {
                @Override public void onFailure(Throwable ignored) {}
                @Override public void onSuccess(Void result) {
                    lastKnownServerConnection = currentTimeMillis();scheduleKeepAliveInterval();}
            });
    }

    private void pingForLogin() {
        FormService.App.getInstance().ping(new AsyncCallback<Response<FormModelResponse>>() {
                @Override public void onFailure(Throwable ignored) {}
                @Override public void onSuccess(Response<FormModelResponse> result) {
                    if (result != null && !result.isAuthenticated()) {
                        if (!Application.getInstance().isModalShowing()) {
                            final LoginDialog login = LoginDialog.show(!getFormHistory().historyMatchesUrl(), EMPTY);
                            login.setSubscription(modal(login));
                        }
                    }
                }
            });
    }

    /**
     * Schedules keep-alive mechanism wake up, beginning of the danger zone of session expiration.
     */
    private void scheduleKeepAliveInterval() {
        if (sessionTimeout != -1) {
            final int timeout     = (sessionTimeout * THOUSAND) - DANGER_ZONE_START;
            final int delta       = (int) (currentTimeMillis() - lastInteraction);
            final int delayMillis = timeout - delta;
            if (delayMillis > 0) {
                keepAlive.schedule(delayMillis);
                final int delayLogin = ((sessionTimeout + 1) * THOUSAND);
                pingForLogin.schedule(delayLogin);
            }
        }
    }

    //~ Static Fields ................................................................................................................................

    /**
     * Beginning of the session timeout danger-zone. For the default (1800 secs or 30 mins), this is
     * the last 5 seconds.
     */
    private static final int DANGER_ZONE_START = 5000;
}  // end class KeepAlive
