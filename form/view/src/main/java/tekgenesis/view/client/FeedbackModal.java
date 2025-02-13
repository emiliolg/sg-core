
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.exprs.Expressions;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.view.client.ui.modal.ModalContent;
import tekgenesis.view.client.ui.modal.ModalSubscription;
import tekgenesis.view.shared.response.ResponseError;

class FeedbackModal extends ModalContent {

    //~ Instance Fields ..............................................................................................................................

    private ReportFeedbackPanel feedbackPanel = null;

    private Form form = null;

    //~ Methods ......................................................................................................................................

    /** Modal subscription. */
    public void setModalSubscription(ModalSubscription subscription) {
        if (feedbackPanel != null) feedbackPanel.setModalSubscription(subscription);
    }

    void initialize(@NotNull final Form f) {
        // Bind Form expressions
        Expressions.bind(ClientUiModelContext.getRetriever(), f);
        form = f;
    }

    /** Show feedback form modal for given error. */
    FeedbackModal show(@Nullable final ResponseError error) {
        if (form != null) {
            feedbackPanel = new ReportFeedbackPanel(form, error);
            setBody(feedbackPanel);
            setTitle(FormViewMessages.MSGS.sendFeedback());
        }
        return this;
    }
}
