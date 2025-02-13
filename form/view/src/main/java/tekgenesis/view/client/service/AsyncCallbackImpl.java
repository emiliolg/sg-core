
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.service;

import tekgenesis.metadata.form.widget.CustomMsgType;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.FormBox;
import tekgenesis.view.client.dialog.ErrorPanel;
import tekgenesis.view.client.dialog.LoginDialog;
import tekgenesis.view.client.ui.modal.ModalContent;
import tekgenesis.view.client.ui.modal.ModalListener;
import tekgenesis.view.shared.response.FormModelResponse;
import tekgenesis.view.shared.response.Response;
import tekgenesis.view.shared.response.ResponseError;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.metadata.form.widget.CustomMsgType.ERROR;
import static tekgenesis.metadata.form.widget.CustomMsgType.SUCCESS;
import static tekgenesis.view.client.Application.messages;
import static tekgenesis.view.client.FormHistory.getFormHistory;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * Real Async Callback implementation.
 */
class AsyncCallbackImpl extends BaseCallback<FormModelResponse> {

    //~ Instance Fields ..............................................................................................................................

    private final String defaultMsg;

    //~ Constructors .................................................................................................................................

    AsyncCallbackImpl(final DefaultCallback<FormModelResponse> c, final FormBox fb) {
        this(c, "", fb, true);
    }

    AsyncCallbackImpl(final DefaultCallback<FormModelResponse> c, final String m, final FormBox fb) {
        this(c, m, fb, true);
    }

    AsyncCallbackImpl(final DefaultCallback<FormModelResponse> c, final FormBox fb, boolean update) {
        this(c, "", fb, update);
    }

    private AsyncCallbackImpl(final DefaultCallback<FormModelResponse> c, final String m, final FormBox fb, boolean update) {
        super(c, fb, update);
        defaultMsg = m;
    }

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("IfStatementWithTooManyBranches")
    public void onSuccess(final Response<FormModelResponse> result) {
        if (result.hasError()) {
            if (!result.isAuthenticated()) {
                if (!Application.getInstance().isModalShowing()) {
                    Application.getInstance().setAuthenticated(false);
                    final LoginDialog login = LoginDialog.show(!getFormHistory().historyMatchesUrl(), new ModalListener() {
                                @Override public void onHide(ModalButtonType buttonClicked) {
                                    callback.onLoginClosed();
                                }

                                @Override public void onShow() {}
                            });

                    login.setSubscription(Application.modal(login));

                    // Hide messages.
                    Application.messages().hide(formBox);
                }
            }
            else {
                final ResponseError error = result.getError();
                messages().error(MSGS.oops() + error.getMessage(), error, formBox);
                callback.onError(error);

                if (error.isDevMode()) {
                    final ErrorPanel errorPanel = new ErrorPanel(error);
                    Application.modal(new ModalContent().setBody(errorPanel).setTitle("Developer Mode"));
                }
            }
        }
        else if (result.hasException()) {
            super.onSuccess(result);
            messages().warning(result.getException().getExceptionMessage(), formBox);
            callback.onException();
        }
        else if (result.getResponse().isError()) {
            super.onSuccess(result);

            // Hide messages.
            Application.messages().hideInfoMessages(formBox);

            // User programmatic error.
            final FormModelResponse response = result.getResponse();
            messages().customError(response.getError(),
                formBox,
                response.getErrorDetails(),
                response.isReload(),
                response.getCustomMsgOrElse(ERROR),
                response.isAutoClose());
            callback.onSuccess(response);
        }
        else {
            super.onSuccess(result);
            final FormModelResponse response = result.getResponse();

            final String        message = response.getMessage();
            final CustomMsgType msgType = response.getCustomMsgOrElse(SUCCESS);
            if (!isEmpty(message)) messages().custom(message, formBox, msgType, response.isAutoClose());
            else if (!isEmpty(defaultMsg)) messages().custom(defaultMsg, formBox, msgType, msgType != ERROR);
            else messages().hide(formBox);

            callback.onSuccess(response);
        }
    }  // end method onSuccess
}  // end class AsyncCallbackImpl
