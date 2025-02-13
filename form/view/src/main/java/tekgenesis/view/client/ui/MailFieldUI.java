
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.CheckMsg;
import tekgenesis.check.CheckType;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.MailValidationType;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;
import tekgenesis.type.assignment.AssignmentType;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.formatter.InputHandler;
import tekgenesis.view.client.suggest.ItemSuggestion;
import tekgenesis.view.client.suggest.MailSuggestOracle;
import tekgenesis.view.client.suggest.TekSuggestOracle;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.metadata.form.widget.MailDomain.domainsKeyMap;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.TextFieldUI.CAPS_SIZE_FIX;

public class MailFieldUI extends FieldWidgetUI implements SuggestUI, HasScalarValueUI, HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private TekMailBox mailBox;

    //~ Constructors .................................................................................................................................

    public MailFieldUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);

        initSuggest(model);
        initWidget(mailBox);

        setFocusTarget(of(mailBox.getTextBox()));
    }

    //~ Methods ......................................................................................................................................

    @Override public void addBlurHandler(@NotNull BlurHandler handler) {
        mailBox.getTextBox().addBlurHandler(handler);
    }

    @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {
        mailBox.getTextBox().addChangeHandler(event -> changeHandler.onValueChange(null));
    }

    @Override public void addCreateNewHandler(ValueChangeHandler<String> handler) {
        mailBox.addCreateNewHandler(handler);
    }

    /** Adds a SelectionEvent handler. */
    public void addSelectionHandler(final SelectionHandler<SuggestOracle.Suggestion> handler) {
        mailBox.addSelectionHandler(handler);
    }

    @Override public boolean canSearchDeprecable() {
        return getModel().isBoundedToDeprecable();
    }

    /** Returns true if model has an on_new method defined. */
    public boolean hasOnNewMethod() {
        return false;
    }

    @Override public boolean hasOnSuggest() {
        return !getModel().getOnSuggestMethodName().isEmpty();
    }

    @Override public boolean hasOnSuggestSync() {
        return !getModel().getOnSuggestSyncMethodName().isEmpty();
    }

    public void updateModelValue() {
        setValue(mailBox.getText());
    }

    public boolean validMailSyntax(String mail) {
        return mailBox.validMailSyntax(mail);
    }

    /** Overridden to apply content style to suggest box's text field. */
    @Override public void setContentStyleName(String styleName) {
        mailBox.getTextBox().addStyleName(styleName);
    }

    @Override public boolean isBoundedToDeprecable() {
        return false;
    }

    @Nullable public String getErrorMessage() {
        return mailBox.getErrorMessage();
    }

    /** Just because we implement SuggestUI. */
    @NotNull public Iterable<AssignmentType> getFilterExpression() {
        return emptyIterable();
    }

    /** Necesary implementations, left empty. */
    public void setFilterExpression(@Nullable final Iterable<AssignmentType> expression) {}

    /** Necesary implementations, left empty. */
    @Override public void setHandleDeprecated(boolean handleDeprecated) {}

    @Nullable @Override public InputHandler<?> getInputHandler() {
        return null;
    }

    public void setLabel(String label) {
        mailBox.setText(notNull(label));
    }

    /** Sets the max length and visible length of the text field. */
    public void setLength(final int length, boolean expand) {
        if (length > 0) {
            final TextBox textBox = (TextBox) mailBox.getTextBox();
            textBox.setMaxLength(length);
            textBox.setVisibleLength((int) Math.round(length * CAPS_SIZE_FIX));
        }
    }

    @Override public void setOnSuggestExpression(Object result) {
        ((TekSuggestOracle) getSuggestOracle()).setOnSuggestArg(result);
    }

    /**
     * Set Options for mail_field.
     *
     * @param  options
     */
    public void setOptions(KeyMap options) {
        final MailSuggestOracle oracle = cast(getSuggestOracle());
        oracle.clear();
        for (final Map.Entry<Object, String> domain : options) {
            final String key   = domain.getKey().toString();
            final String value = domain.getValue();
            if (isNotEmpty(value)) oracle.addSuggestion(new ItemSuggestion(value, value));
            else oracle.addSuggestion(new ItemSuggestion(key, key));
        }
    }

    @Override public SuggestOracle getSuggestOracle() {
        return mailBox.getSuggestOracle();
    }

    public Object getValue() {
        return isEmpty(mailBox.getValue()) ? null : mailBox.getValue();
    }

    @Override public void setValue(@Nullable final Object value) {
        setValue(value, false);
    }

    @Override public void setValue(@Nullable final Object value, boolean fireEvents) {
        final String stringValue = cast(value);
        if (fireEvents) {
            // force blur to emulate user input on test
            mailBox.setFocus(true);
            mailBox.setValue(stringValue, true);
            mailBox.setFocus(false);
        }
        else mailBox.setValue(stringValue);
        if (!isEmpty(stringValue)) mailBox.validateEmail(true);
    }

    @NotNull @Override Option<Element> createIcon() {
        return Option.of(mailBox.getElement());
    }

    @Override Option<Icon> iconInWidget(Element icon, String iconStyle) {
        return mailBox.iconInText(iconStyle, getModel().isExpand());
    }

    /** Clears the widget error. */
    private void clearTooltipError() {
        mailBox.resetErrorMessage();
        clearMessages();
    }

    private void initSuggest(Widget model) {
        final MailSuggestOracle suggestOracle = new MailSuggestOracle(this);

        final MailValidationType mailValidationType = model.getMailValidationType();
        mailBox = new TekMailBox(suggestOracle,
                "",
                mailValidationType,
                Application.getInstance().isMailValidatorEnabled(),
                message -> setTooltipError(notNull(message)),
                message -> setTooltipWarning(notNull(message)),
                this::clearTooltipError);

        mailBox.setPlaceholder(MSGS.typeEmail());
        mailBox.setChooseOne(MSGS.chooseOne());
        setOptions(domainsKeyMap());
    }  // end method initSuggest

    /** Adds a CheckMsg Error to the widget. */
    private void setTooltipError(String message) {
        mailBox.setErrorMessage(message);
        final CheckMsg msg = new CheckMsg(false, CheckType.ERROR, message);
        addMessage(msg);
    }

    /** Adds a CheckMsg Warning to the widget. */
    private void setTooltipWarning(String message) {
        final CheckMsg msg = new CheckMsg(false, CheckType.WARNING, message);
        addMessage(msg);
    }
}  // end class MailFieldUI
