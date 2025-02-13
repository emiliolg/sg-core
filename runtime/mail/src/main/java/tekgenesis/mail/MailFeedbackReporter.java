
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mail;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Strings;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.feedback.FeedbackEvent;
import tekgenesis.form.feedback.FeedbackReporter;

import static tekgenesis.common.Predefined.isNotEmpty;

/**
 * Feedback Reporter implementation that sends mails.
 */
public class MailFeedbackReporter implements FeedbackReporter {

    //~ Methods ......................................................................................................................................

    @Override public void reportFeedback(@NotNull final FeedbackEvent feedbackEvent) {
        final MailFeedbackReporterProps props = Context.getEnvironment().get(MailFeedbackReporterProps.class);

        if (isNotEmpty(props.to) && isNotEmpty(props.from)) {
            final String             subject = buildFeedbackMailSubject(feedbackEvent);
            final String             msg     = buildFeedbackMailMessage(feedbackEvent);
            final Collection<String> to      = Arrays.asList(props.to.split(";"));

            try {
                final Mail mail = new Mail().to(Colls.seq(to)).from(props.from).withSubject(subject).withBody(msg);
                MailSender.send(mail);
            }
            catch (final MailException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Override public boolean isEnabled() {
        final MailFeedbackReporterProps props = Context.getEnvironment().get(MailFeedbackReporterProps.class);
        return isNotEmpty(props.to) && isNotEmpty(props.from);
    }

    private String buildDataLine(@NotNull String key, @Nullable String data) {
        return data == null ? "" : ("<dt>" + key + "</dt><dd>" + data + "</dd>");
    }

    @Nullable private String buildError(@Nullable String error) {
        final List<String> lines = Strings.lines(error);
        if (lines.size() > 1) {
            String result = "<ul>";
            for (final String line : lines)
                result = result + "<li style=\"list-style-type:none;\">" + line + "</li>";
            result = result + "</ul>";
            return result;
        }
        else return error;
    }

    @NotNull
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private String buildFeedbackMailMessage(@NotNull FeedbackEvent feedbackEvent) {
        String result = "";
        result = result + "<article>";
        result = result + "<h2>Reporte de usuario</h2>";
        result = result + "<dl>";
        result = result + buildDataLine(feedbackEvent.getType().toString(), buildError(feedbackEvent.getError()));
        result = result + "</dl>";
        result = result + "<section><h3>Contexto</h3>";
        result = result + "<dl>";
        result = result + buildDataLine("Usuario", feedbackEvent.getUser().getId());
        result = result + buildDataLine("Fecha/Hora", feedbackEvent.getDate().toString());
        result = result + buildDataLine("Applicacion", feedbackEvent.getApplication());
        result = result + buildDataLine("Url", feedbackEvent.getUrl());
        result = result + buildDataLine("Historial", buildHistory(feedbackEvent.getHistory()));
        result = result + "</dl>";
        result = result + "</section>";
        result = result + "<section><h3>Mensaje del usuario</h3>";
        result = result + "<dl>";
        result = result + buildDataLine("Titulo", feedbackEvent.getSummary());
        result = result + buildDataLine("Descripcion", "<pre style=\"font-family:inherit;\">" + feedbackEvent.getDescription() + "</pre>");
        result = result + "</dl>";
        result = result + "</section>";
        result = result + "</article>";

        return result;
    }

    @NotNull private String buildFeedbackMailSubject(@NotNull FeedbackEvent feedbackEvent) {
        return "Reporte de usuario:" + feedbackEvent.getType() + ":" + feedbackEvent.getApplication();
    }

    private String buildHistory(Seq<String> history) {
        String result = "<ul>";
        for (final String line : history.take(MAX_HISTORY_LINES))
            result = result + "<li style=\"list-style-type:none;\">" + line + "</li>";
        if (history.size() > MAX_HISTORY_LINES) result = result + "<li style=\"list-style-type:none;\">...</li>";
        result = result + "</ul>";

        return result;
    }

    //~ Static Fields ................................................................................................................................

    public static final int MAX_HISTORY_LINES = 20;

    private static final Logger logger = Logger.getLogger(MailFeedbackReporter.class);
}  // end class MailFeedbackReporter
