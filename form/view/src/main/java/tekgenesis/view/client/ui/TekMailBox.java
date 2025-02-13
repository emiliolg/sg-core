
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.function.Consumer;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TekSuggestBox;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.MailValidationType;
import tekgenesis.view.client.ui.base.Icon;
import tekgenesis.view.shared.response.MailValidationResponse;
import tekgenesis.view.shared.response.MailValidationResponse.MailValidationError;
import tekgenesis.view.shared.response.Response;
import tekgenesis.view.shared.service.FormService;

import static com.google.gwt.event.dom.client.KeyCodes.*;
import static com.google.gwt.regexp.shared.RegExp.compile;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.base.Icon.inTextBox;

@SuppressWarnings("ConstructorWithTooManyParameters")
public class TekMailBox extends TekSuggestBox {

    //~ Instance Fields ..............................................................................................................................

    private final Runnable         clearCallback;
    private final Consumer<String> errorCallback;

    private String                   errorMessage         = null;
    private final boolean            mailValidatorEnabled;
    private String                   mailValue            = null;
    private final MailValidationType validationType;
    private final Consumer<String>   warningCallback;

    //~ Constructors .................................................................................................................................

    /**
     * Error, clear and warning callbacks are blocks that are executed when address validation
     * fails, succeds and has connection issues.
     */
    public TekMailBox(SuggestOracle oracle, @Nullable String iconStyle, MailValidationType validationType, boolean mailValidatorEnabled,
                      Consumer<String> errorCallback, Consumer<String> warningCallback, Runnable clearCallback) {
        super(oracle, true, iconStyle, Option.empty(), false, false);
        box.removeStyleName("suggest-icon-place");
        this.errorCallback        = errorCallback;
        this.warningCallback      = warningCallback;
        this.clearCallback        = clearCallback;
        this.validationType       = validationType;
        this.mailValidatorEnabled = mailValidatorEnabled;
    }

    //~ Methods ......................................................................................................................................

    public void addBlurHandler(BlurHandler handler) {
        addHandler(handler, BlurEvent.getType());
    }

    public void resetErrorMessage() {
        errorMessage = null;
    }

    public void validateEmail(boolean forced) {
        final String mail = getText();
        if (!forced && equal(mail, mailValue)) return;
        mailValue = mail;

        if (mail.isEmpty()) {
            clear();
            return;
        }
        if (validMailSyntax(mail)) {
            clear();
            validateAddress(mail);
        }
        else error(MSGS.wrongMailSyntax());
    }

    public boolean validMailSyntax(String mail) {
        return !validationType.mustCheckSyntax() || MAIL_REGEX.test(mail);
    }

    @Nullable public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String message) {
        errorMessage = message;
    }

    public void setIcon(String iconStyle) {
        Icon.replaceInWidget(box, iconStyle);
    }

    /**
     * Set the new suggestion in the text box.
     *
     * @param  suggestion  the new suggestion
     */
    @Override public void setNewSelection(@Nullable final SuggestOracle.Suggestion suggestion) {
        completeEmail(suggestion);
        fireSuggestionEvent(suggestion);
        display.hideSuggestions();
        validateEmail(true);
    }  // end method setNewSelection

    @Override protected void addEventsToTextBox() {
        final MailBoxEvents events = new MailBoxEvents();
        events.addKeyHandlersTo(box);
        box.addValueChangeHandler(events);
        box.addBlurHandler(events);
        display.addCloseHandler(events);
    }

    @Override protected void refreshSuggestions() {
        // Get the raw text.
        final String text = getText();
        if (text.equals(currentText)) return;

        currentText = text;
        showSuggestions(domainSubstring(text), callback);
    }

    Option<Icon> iconInText(String iconStyle, boolean isExpand) {
        return inTextBox(box, iconStyle, isExpand);
    }

    private void clear() {
        clearCallback.run();
    }

    private void completeEmail(@Nullable SuggestOracle.Suggestion suggestion) {
        final String domainPart = suggestion == null ? "" : suggestion.getReplacementString();
        box.setTitle(domainPart);
        final String text = getText();
        final int    idx  = text.indexOf('@');
        if (idx >= 0) setText(text.substring(0, idx + 1) + domainPart);
    }

    @NotNull private String domainSubstring(String text) {
        return text.substring(text.indexOf('@') + 1);
    }

    private void error(String message) {
        errorCallback.accept(message);
    }

    /** GWT service that validates mail domain and address in an asynchronous way. */
    private void validateAddress(final String mail) {
        if (validationType.mustCheckDomain())
            FormService.App.getInstance()
                .validateEmail(mail,
                    mailValidatorEnabled && validationType.mustCheckAddress(),
                    new AsyncCallback<Response<MailValidationResponse>>() {
                        @Override public void onFailure(Throwable caught) {}

                        @Override public void onSuccess(Response<MailValidationResponse> result) {
                            if (result.hasError() || result.hasException()) return;
                            final MailValidationError type = result.getResponse().getValidationError();
                            switch (type) {
                            case DOMAIN:
                                error(MSGS.domainDoesntExist(domainSubstring(mail)));
                                break;
                            case ADDRESS:
                                if (validationType.mustCheckAddress()) warning(MSGS.emailDoesntExist(mail));
                                break;
                            case CONNECTION:
                                warning(MSGS.emailCouldntConnect());
                                break;
                            default:
                                break;
                            }
                        }
                    });
    }  // end method validateAddress

    private void warning(String message) {
        warningCallback.accept(message);
    }

    //~ Static Fields ................................................................................................................................

    private static final RegExp MAIL_REGEX = compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$");

    //~ Inner Classes ................................................................................................................................

    private class MailBoxEvents extends TextBoxEvents {
        @Override public void onBlur(BlurEvent event) {
            validateEmail(false);
        }

        @Override public void onKeyDown(KeyDownEvent event) {
            switch (event.getNativeKeyCode()) {
            case KEY_ESCAPE:
                display.hideSuggestions();
                break;
            case KEY_DOWN:
                if (display.isSuggestionsShowing()) display.moveSelectionDown();
                else if (getText().indexOf('@') > 0) showSuggestions("", callback);
                break;
            case KEY_UP:
                display.moveSelectionUp();
                break;
            case KEY_ENTER:
                if (display.isSuggestionsShowing()) {
                    event.stopPropagation();
                    changeSelection();
                }
                break;
            case KEY_TAB:
                if (display.isSuggestionsShowing()) changeSelection();
                break;
            }
            delegateEvent(TekMailBox.this, event);
        }

        @Override public void onKeyUp(KeyUpEvent event) {
            // After every user key input, refresh the popup suggestions.
            final String text = box.getText();
            if (text.length() >= suggestThreshold) {
                if (suggestDelay > 0) refreshSuggestionsTimer.schedule(suggestDelay);
                else {
                    final int al = text.indexOf('@');
                    if (al > 0 && box.getCursorPos() > al) refreshSuggestions();
                    else {
                        display.hideSuggestions();
                        currentText = getText();
                    }
                }
            }

            delegateEvent(TekMailBox.this, event);
        }  // end method onKeyUp

        @Override public void onValueChange(ValueChangeEvent<String> event) {
            delegateEvent(TekMailBox.this, event);
        }
    }  // end class MailBoxEvents
}  // end class TekMailBox
