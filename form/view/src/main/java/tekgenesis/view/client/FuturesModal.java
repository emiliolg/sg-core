
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Panel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.view.client.service.BaseAsyncCallback;
import tekgenesis.view.client.ui.base.Icon;
import tekgenesis.view.client.ui.modal.ModalContent;
import tekgenesis.view.client.ui.modal.ModalSubscription;
import tekgenesis.view.shared.feedback.FeedbackEventData;
import tekgenesis.view.shared.response.FormModelResponse;
import tekgenesis.view.shared.response.Response;
import tekgenesis.view.shared.service.FormService;

import static java.lang.Math.max;
import static java.lang.Math.min;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.metadata.form.widget.CustomMsgType.SUCCESS;
import static tekgenesis.view.client.Application.messages;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.*;

class FuturesModal extends ModalContent {

    //~ Instance Fields ..............................................................................................................................

    private ModalSubscription active;
    private Panel             bar;
    private Button            button;
    private InlineHTML        span;
    private String            uuid;

    //~ Constructors .................................................................................................................................

    FuturesModal() {
        setBodyOnly(true);
        setFixed(true);
        bar    = null;
        span   = null;
        button = null;
        active = null;
        uuid   = null;
        init();
    }

    //~ Methods ......................................................................................................................................

    boolean hasActiveSubscription() {
        return active != null;
    }

    FuturesModal update(@NotNull final FeedbackEventData data) {
        uuid = data.getUuid();
        if (!data.isTerminated()) updateProgress(data);
        else reset(data);
        return this;
    }

    void setActive(ModalSubscription active) {
        this.active = active;
    }

    private FlowPanel createBar(@NotNull Panel body) {
        final FlowPanel progressPanel = div();
        progressPanel.addStyleName(FormConstants.PROGRESS);
        final FlowPanel progress = div();
        progress.addStyleName(FormConstants.PROGRESS_BAR);
        progressPanel.add(progress);
        body.add(progressPanel);
        return progress;
    }

    private Button createButton(Panel body) {
        final Button b = button();
        b.setText(FormViewMessages.MSGS.cancel());
        body.add(b);
        b.addClickHandler(event -> {
            resetModal();
            FormService.App.getInstance().onFutureCancellation(uuid, new BaseAsyncCallback<Response<FeedbackEventData>>() {
                    @Override public void onSuccess(Response<FeedbackEventData> result) {
                        Application.getInstance().await(result.getResponse());
                    }
                });
        });
        return b;
    }

    private InlineHTML createStatus(Panel body) {
        final FlowPanel status = div();
        status.addStyleName("futures-status");
        final Icon icon = new Icon(IconType.COG);
        icon.addStyleName("fa-spin");
        final InlineHTML holder = span();
        holder.getElement().insertFirst(icon.getElement());
        status.add(holder);
        final InlineHTML s = span();
        status.add(s);
        body.add(status);
        return s;
    }

    private void init() {
        final Panel body = div();
        body.addStyleName("futures-modal");
        bar    = createBar(body);
        button = createButton(body);
        span   = createStatus(body);
        setBody(body);
    }

    private void reset(FeedbackEventData data) {
        resetModal();
        final FormModelResponse response     = data.getResponse();
        final FormBox           activeOrMain = Application.getInstance().getActiveOrMain();

        if (data.isInterrupted()) messages().error(response.getError(), activeOrMain);
        else {
            final String message = response.getMessage();
            if (!isEmpty(message)) messages().custom(message, activeOrMain, response.getCustomMsgOrElse(SUCCESS), response.isAutoClose());
            else messages().hide(activeOrMain);
        }
    }  // end method reset

    private void resetModal() {
        bar.setWidth("0");
        span.setText("");
        if (active != null) {
            active.hide();
            active = null;
        }
    }

    private void updateProgress(FeedbackEventData data) {
        final int progress = data.getProgress();
        if (progress != -1) bar.setWidth(max(0, min(progress, 100)) + "%");
        data.printOutMessages();
        final String msg = notEmpty(data.getLastMessage(), notEmpty(span.getText(), FormViewMessages.MSGS.executing()));
        span.setText(msg);
    }
}  // end class FuturesModal
