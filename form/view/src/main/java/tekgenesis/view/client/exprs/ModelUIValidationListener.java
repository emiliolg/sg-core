
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.exprs;

import tekgenesis.check.CheckMsg;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.form.exprs.ValidationListener;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.exprs.ExpressionEvaluatorFactory.ValidationResult;
import tekgenesis.view.client.ui.*;

import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.widget.WidgetType.SUBFORM;
import static tekgenesis.view.client.Application.getInstance;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * Model Ui validation listener.
 */
class ModelUIValidationListener implements ValidationListener {

    //~ Instance Fields ..............................................................................................................................

    private final ValidationResult errors = new ValidationResult();
    private final WidgetUIFinder   finder;

    //~ Constructors .................................................................................................................................

    ModelUIValidationListener(WidgetUIFinder finder) {
        this.finder = finder;
    }

    //~ Methods ......................................................................................................................................

    @Override public void onValidation(Widget widget, Model model, Option<Integer> item, Option<String> subformPath, Seq<CheckMsg> messages) {
        final Option<WidgetUIFinder> f;

        if (subformPath.isPresent() && !subformPath.get().isEmpty()) {
            final FormUI subformView = getInstance().getSubformView(subformPath.get());

            if (subformView != null) f = some(subformView.finder());
            else {
                logger.warning(MSGS.initSubformsOnLoad(subformPath.get()));
                return;
            }
        }
        else f = some(finder);

        for (final WidgetUIFinder fs : f) {
            final Option<WidgetUI> option = fs.find(widget, item);
            if (messages.exists(ExpressionEvaluatorFactory::isErrorType)) errors.add(messages, widget, item, subformPath);

            for (final WidgetUI ui : option) {
                if (ui instanceof FieldWidgetUI && isDefined(widget, model) && !(ui instanceof InlineSubformUI)) {
                    final FieldWidgetUI input = (FieldWidgetUI) ui;
                    input.clearMessages();
                    for (final CheckMsg message : messages)
                        input.addMessage(message);
                }
            }
        }
    }

    ValidationResult getResult() {
        return errors;
    }

    private boolean isDefined(final Widget widget, Model model) {
        return !(widget.hasValue() || widget.getWidgetType() == SUBFORM) || model.isDefined(widget);
    }

    //~ Static Fields ................................................................................................................................

    public static final Logger logger = Logger.getLogger(ModelUIValidationListener.class);
}  // end class ModelUIValidationListener
