
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;
import tekgenesis.type.permission.PredefinedPermission;
import tekgenesis.view.client.controller.FormController;
import tekgenesis.view.client.event.CancelEvent;
import tekgenesis.view.client.event.FormBus;
import tekgenesis.view.client.event.SubmitEvent;
import tekgenesis.view.client.service.Feedback;
import tekgenesis.view.client.ui.modal.ModalSubscription;
import tekgenesis.view.shared.response.ResponseError;
import tekgenesis.view.shared.service.FormService;

import static tekgenesis.common.core.Functions.mkString;
import static tekgenesis.view.client.Application.messages;
import static tekgenesis.view.client.FormHistory.HistoryFormEvent;
import static tekgenesis.view.client.service.Feedback.TicketType;
import static tekgenesis.view.client.service.Feedback.TicketType.ERROR;
import static tekgenesis.view.client.service.Feedback.TicketType.EXCEPTION;
import static tekgenesis.view.client.service.Feedback.TicketType.SUGGESTION;

/**
 * Report feedback panel.
 */
class ReportFeedbackPanel extends FlowPanel {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private final ResponseError error;
    @NotNull private final FormModel      model;
    private ModalSubscription             subscription = null;

    //~ Constructors .................................................................................................................................

    /** Report feedback panel constructor. */
    ReportFeedbackPanel(@NotNull final Form feedback, @Nullable final ResponseError error) {
        this.error = error;
        getElement().setClassName("feedbackReport");
        model = new FormModel(feedback);
        prepareModel(error != null);
        final FormBus        bus        = createBus();
        final FormController controller = createController(bus);
        add(controller.getView());
    }

    //~ Methods ......................................................................................................................................

    public void setModalSubscription(ModalSubscription subscription) {
        this.subscription = subscription;
    }

    private FormBus createBus() {
        return new FormBus() {
            @Override protected void onSubmit(SubmitEvent e) {
                final Feedback feedback = createFeedback();

                // Server feedback submit
                FormService.App.getInstance().postFeedback(feedback, voidAsync());
            }

            @Override protected void onCancel(CancelEvent e) {
                if (subscription != null) subscription.hide();
            }

            private AsyncCallback<Void> voidAsync() {
                return new AsyncCallback<Void>() {
                    @Override public void onFailure(Throwable caught) {
                        messages().error(FormViewMessages.MSGS.feedbackError(), Application.getInstance().getActiveOrMain());
                    }

                    @Override public void onSuccess(Void result) {
                        messages().success(FormViewMessages.MSGS.feedbackSuccess(), Application.getInstance().getActiveOrMain());
                        if (subscription != null) subscription.hide();
                    }
                };
            }
        };
    }

    private FormController createController(FormBus bus) {
        final FormController c = new FormController(model, bus, null, null);
        c.init();
        return c;
    }

    private Feedback createFeedback() {
        final String           summary     = (String) model.get(Constants.SUMMARY);
        final String           description = (String) model.get(Constants.DESCRIPTION);
        final String           type        = (String) model.get(Constants.TYPE);
        final HistoryFormEvent event       = FormHistory.getFormHistory().current();
        return new Feedback(summary,
            description,
            TicketType.valueOf(type),
            Window.Location.getHref(),
            topHistory(),
            event.toString(),
            error != null ? error.toString() : "");
    }

    private void prepareModel(boolean exception) {
        model.setPermission(PredefinedPermission.CREATE.getName(), true);

        model.set(model.widgetByName(Constants.HIDE_TYPE), exception);

        final Widget typeWidget = model.widgetByName(Constants.TYPE);
        if (exception) model.set(typeWidget, EXCEPTION.name());
        else {
            model.set(typeWidget, ERROR.name());
            model.setOptions(typeWidget, ticketTypeOptions());
        }
    }  // end method prepareModel

    private KeyMap ticketTypeOptions() {
        final KeyMap result = KeyMap.create();
        result.put(ERROR.name(), FormViewMessages.MSGS.error());
        result.put(SUGGESTION.name(), FormViewMessages.MSGS.suggestion());
        return result;
    }

    /** Return history top ten items. */
    private List<String> topHistory() {
        final List<String> history = new ArrayList<>(10);
        Application.getInstance().getHistory().map(mkString()).take(10).into(history);
        return history;
    }
}  // end class ReportFeedbackPanel
