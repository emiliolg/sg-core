
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.context.Context;
import tekgenesis.form.Action;
import tekgenesis.mail.Mail;
import tekgenesis.mail.MailException;
import tekgenesis.mail.MailProps;
import tekgenesis.mail.MailSender;

import static tekgenesis.common.core.Strings.split;

/**
 * User class for Form: EmailForm
 */
public class EmailForm extends EmailFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action sendEmail() {
        final MailProps props = new MailProps();

        props.hostname = getHost();
        props.port     = getPort();
        props.username = getUsername();
        props.password = getPassword();
        props.auth     = true;
        props.tls      = true;

        Context.getEnvironment().put(EMAIL_NOTIFICATION_MAIL, props);

        final Mail mail = new Mail().from(getFrom())
                          .to(split(getTo(), ','))
                          .cc(split(getCc(), ','))
                          .bcc(split(getBcc(), ','))
                          .withSubject(getSubject())
                          .withBody(getBody());

        try {
            MailSender.send(EMAIL_NOTIFICATION_MAIL, mail);
            return actions.getDefault();
        }
        catch (final MailException e) {
            return actions.getError().withMessage(e.getMessage());
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final String EMAIL_NOTIFICATION_MAIL = "emailNotificationMail";
}
