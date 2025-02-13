
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.CustomMsgType;
import tekgenesis.view.shared.response.ResponseError;

import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;

/**
 * Feedback messages to the user.
 */
class ApplicationMessages implements Messages {

    //~ Instance Fields ..............................................................................................................................

    private final FlowPanel                              container;
    private final Map<FormBox, List<ApplicationMessage>> messages;
    private boolean                                      reporting;

    //~ Constructors .................................................................................................................................

    ApplicationMessages() {
        messages  = new HashMap<>();
        container = div();
        container.addStyleName("feedBack");
        RootPanel.get().add(container);
    }

    //~ Methods ......................................................................................................................................

    @Override public void custom(@NotNull String msg, @NotNull FormBox formBox, @NotNull CustomMsgType type, boolean autoHide) {
        addMessage(msg, formBox, mapToMsgType(type), autoHide, empty());
    }

    @Override public void customError(@NotNull String msg, @NotNull FormBox formBox, @NotNull List<String> errorDetails, boolean reload,
                                      @NotNull CustomMsgType type, boolean autoHide) {
        addMessage(msg, formBox, mapToMsgType(type), autoHide, empty(), errorDetails, reload);
    }

    @Override public void error(@NotNull String msg, @NotNull FormBox formBox) {
        addMessage(msg, formBox, MsgType.DANGER, false, empty());
    }

    @Override public void error(@NotNull String msg, @NotNull ResponseError exception, @NotNull FormBox formBox) {
        addMessage(msg, formBox, MsgType.DANGER, false, some(exception));
    }

    @Override public boolean hasError(@NotNull FormBox formBox) {
        final List<ApplicationMessage> msgs = messages.get(formBox);
        for (final ApplicationMessage message : msgs) {
            if (message.getType() == MsgType.DANGER) return true;
        }
        return false;
    }

    @Override public void hide(@NotNull FormBox formBox) {
        hide(empty(), formBox);
    }

    @Override public void hideInfoMessages(@NotNull FormBox formBox) {
        removeMessages(INFO_MESSAGES, formBox);
        removeUiMessages();
    }

    @Override public void info(@NotNull String msg, @NotNull FormBox formBox) {
        addMessage(msg, formBox, MsgType.INFO, false, empty());
    }

    @Override public void success(@NotNull String msg, @NotNull FormBox formBox) {
        success(msg, formBox, true);
    }

    @Override public void success(@NotNull String msg, @NotNull FormBox formBox, boolean autoHide) {
        addMessage(msg, formBox, MsgType.SUCCESS, autoHide, empty());
    }

    @Override public void warning(@NotNull String msg, @NotNull FormBox formBox) {
        addMessage(msg, formBox, MsgType.WARNING, false, empty());
    }

    /** Disable 'send feedback' functionality. */
    void allowReporting() {
        reporting = true;
    }

    private void addMessage(String msg, final FormBox formBox, MsgType type, boolean hasTimer, Option<ResponseError> error) {
        addMessage(msg, formBox, type, hasTimer, error, new ArrayList<>(), false);
    }

    private void addMessage(String msg, final FormBox formBox, MsgType type, boolean hasTimer, Option<ResponseError> error, List<String> errorList,
                            boolean reload) {
        final ApplicationMessage message = new ApplicationMessage(type, msg, errorList, reload);

        // first hide, because if the message already exist, it won't hide the info messages
        if (type != MsgType.INFO) hideInfoMessages(formBox);

        if (messages.get(formBox) != null && messages.get(formBox).contains(message)) return;

        final ClickHandler close = event -> removeMessages(new OnlyMePredicate(message), formBox);
        message.addClickHandler(close);

        if (reporting) error.ifPresent(err -> message.attachSendFeedback(getFeedBackHandler(err, formBox)));

        List<ApplicationMessage> applicationMessages = messages.get(formBox);
        if (applicationMessages == null) applicationMessages = new ArrayList<>();
        applicationMessages.add(message);
        messages.put(formBox, applicationMessages);

        showMessage(message);

        if (hasTimer) Scheduler.get().scheduleFixedDelay(new HideSchedule(message, formBox), HIDE_FEEDBACK_DELAY_MS);
    }

    private void hide(@NotNull Option<ApplicationMessage> m, @NotNull FormBox formBox) {
        m.ifPresentOrElse(                                                  //
            am -> removeMessages(new OnlyMeAndInfoPredicate(am), formBox),  //
            () -> removeMessages(ALL_MESSAGES, formBox));
        removeUiMessages();
    }

    @NotNull private MsgType mapToMsgType(@NotNull CustomMsgType type) {
        switch (type) {
        case INFO:
            return MsgType.INFO;
        case SUCCESS:
            return MsgType.SUCCESS;
        case WARNING:
            return MsgType.WARNING;
        case ERROR:
            return MsgType.DANGER;
        }
        return MsgType.SUCCESS;
    }

    private void removeMessages(@NotNull final Predicate<ApplicationMessage> p, @NotNull final FormBox formBox) {
        final List<ApplicationMessage> msgs = messages.get(formBox);

        if (msgs != null) {
            final List<ApplicationMessage> messagesToBeRemoved = new ArrayList<>();
            filter(msgs, p).forEach(message -> {
                messagesToBeRemoved.add(message);
                message.removeStyleName("in");
            });
            msgs.removeAll(messagesToBeRemoved);
        }
    }

    private void removeUiMessages() {
        for (final Widget widget : container) {
            if (!widget.getStyleName().contains("in")) container.remove(widget);
        }
    }

    private void showMessage(ApplicationMessage message) {
        if (!container.isVisible()) container.setVisible(true);
        container.add(message);
        message.addStyleName("in");
    }

    private ClickHandler getFeedBackHandler(final ResponseError error, final FormBox formBox) {
        return event -> {
                   Application.getInstance().feedback(error);
                   hide(formBox);
                   event.stopPropagation();
               };
    }

    //~ Static Fields ................................................................................................................................

    private static final Predicate<ApplicationMessage> INFO_MESSAGES = message -> message != null && message.getType() == MsgType.INFO;

    private static final Predicate<ApplicationMessage> ALL_MESSAGES = message -> true;

    public static final int DELAY = 500;

    private static final int HIDE_FEEDBACK_DELAY_MS = 1500;

    //~ Enums ........................................................................................................................................

    enum MsgType { INFO, SUCCESS, WARNING, DANGER }

    //~ Inner Classes ................................................................................................................................

    private class HideSchedule implements Scheduler.RepeatingCommand {
        private final FormBox            formBox;
        private final ApplicationMessage message;

        HideSchedule(ApplicationMessage message, FormBox formBox) {
            this.message = message;
            this.formBox = formBox;
        }

        @Override public boolean execute() {
            hide(some(message), formBox);
            return false;
        }
    }

    private class OnlyMeAndInfoPredicate extends OnlyMePredicate {
        private OnlyMeAndInfoPredicate(@NotNull ApplicationMessage m) {
            super(m);
        }

        @Override public boolean test(@Nullable ApplicationMessage message) {
            return message != null && (super.test(message) || message.getType() == MsgType.INFO);
        }
    }

    private class OnlyMePredicate implements Predicate<ApplicationMessage> {
        private final ApplicationMessage m;

        private OnlyMePredicate(@NotNull ApplicationMessage m) {
            this.m = m;
        }

        @Override public boolean test(@Nullable ApplicationMessage message) {
            return message != null && message.equals(m);
        }
    }
}  // end class ApplicationMessages
