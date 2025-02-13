
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.exprs;

import org.jetbrains.annotations.NotNull;

import tekgenesis.check.CheckMsg;
import tekgenesis.code.Evaluator;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.exprs.ValidationListener;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.FormViewMessages;
import tekgenesis.view.client.ui.*;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.metadata.form.MetadataFormMessages.MSGS;
import static tekgenesis.metadata.form.exprs.Expressions.evaluateNumber;

/**
 * Ui related validations.
 */
class UiValidations {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Evaluator evaluator;

    @NotNull private final WidgetUIFinder finder;

    @NotNull private final ValidationListener listener;

    //~ Constructors .................................................................................................................................

    UiValidations(@NotNull final ValidationListener listener, @NotNull final WidgetUIFinder finder) {
        this.listener = listener;
        this.finder   = finder;
        evaluator     = new Evaluator();
    }

    //~ Methods ......................................................................................................................................

    public void evaluateExpressions(@NotNull MultipleWidget widget, @NotNull Model model) {
        final MultipleModel table = model.getMultiple(widget);
        int                 i     = 0;
        for (final RowModel rowModel : table) {
            for (final Widget column : widget.getTableElements())
                finder.byItemIndex(column, i).ifPresent(ui -> evaluateExpressions(ui, rowModel));
            i++;
        }
    }

    public void evaluateExpressions(WidgetUI widgetUI, @NotNull Model model) {
        if (widgetUI instanceof PasswordTextFieldUI) {
            final PasswordTextFieldUI password = (PasswordTextFieldUI) widgetUI;
            final Widget              widget   = password.getModel();

            if (widget.hasMetering() && model.hasValue(widget)) {
                final Integer metering  = notNull((Integer) evaluateNumber(evaluator, model, widget.getMetering()), 1);
                final int     lastScore = password.getLastScore();

                // Metering field option is 1-based and Zxcvbn password scores are 0-based
                if (lastScore < (metering - 1)) onValidation(widgetUI, model, widget, MSGS.lowerPasswordStrength());
            }
        }
        // If its a mailfield or a dynamic with mailfield, the address will be validated
        if (widgetUI instanceof MailFieldUI) validateMailField((MailFieldUI) widgetUI, model);
        else if (widgetUI instanceof DynamicUI) {
            for (final MailFieldUI ui : ((DynamicUI) widgetUI).asMailField())
                validateMailField(ui, model);
        }
    }  // end method evaluateExpressions

    private void onValidation(WidgetUI widgetUI, @NotNull Model model, Widget widget, String errorMessage) {
        listener.onValidation(widget, model, widgetUI.getContext().getRow(), Option.empty(), listOf(new CheckMsg(errorMessage)));
    }

    /**
     * Validates the mail syntax or if it already has an error message because domain validation is
     * asynchronous.
     */
    private void validateMailField(MailFieldUI mailField, @NotNull Model model) {
        final Widget widget = mailField.getModel();
        if (model.hasValue(widget)) {
            final String value        = (String) mailField.getValue();
            final String errorMessage = mailField.getErrorMessage() != null
                                        ? mailField.getErrorMessage()
                                        : !isEmpty(value) && !mailField.validMailSyntax(value) ? FormViewMessages.MSGS.wrongMailSyntax() : null;
            if (errorMessage != null) onValidation(mailField, model, widget, errorMessage);
        }
    }
}  // end class UiValidations
