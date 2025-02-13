
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Action;
import tekgenesis.mail.Mail;
import tekgenesis.mail.MailException;
import tekgenesis.mail.MailProps;

import static java.lang.String.format;

import static tekgenesis.authorization.Messages.*;
import static tekgenesis.authorization.g.UserBase.findWhere;
import static tekgenesis.authorization.g.UserTable.USER;
import static tekgenesis.common.env.context.Context.getEnvironment;
import static tekgenesis.mail.MailSender.send;

/**
 * User class for Form: ForgotPassword
 */
public class ForgotPassword extends ForgotPasswordBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action sendEmail() {
        if (isDefined(Field.ID)) {
            final ShiroProps shiroProps = Context.getProperties(ShiroProps.class);
            final String     id         = getId();
            final boolean    idByEmail  = id.contains("@");

            final User user = findWhere(idByEmail ? USER.EMAIL.eq(id) : USER.ID.eq(id));

            if (user != null && user.getEmail() != null) {
                final String code = user.persistNextConfirmation();
                final String url  = shiroProps.forceLoginSsl ? "https://" : "http://" + format(PASSWORD_RECOVER_URL, context.getHost(), code);

                logger.info(String.format("Reset password url generated: %s", url));

                try {
                    Mails.lostPassword(user.getEmail(), url);
                    setShowError(false);
                    setShowInfo(true);
                    setInfo(SUCCESFULLY_SENT_EMAIL.label());
                }
                catch (final MailException e) {
                    logger.error(String.format("Error trying to send reset password email to %s", user), e);
                    setShowError(true);
                    setShowInfo(false);
                    setError(ERROR_SENDING_EMAIL.label());
                }
            }
            else {
                setShowError(true);
                setShowInfo(false);
                setError(idByEmail ? USER_NOT_FOUND_BY_EMAIL.label() : USER_NOT_FOUND_BY_USERNAME.label());
            }
        }
        return actions.getDefault();
    }

    //~ Static Fields ................................................................................................................................

    private static final String PASSWORD_RECOVER_URL = "%s/sg/password/reset/%s";

    private static final Logger logger                   = Logger.getLogger(ForgotPassword.class);
    private static final String RESET_PASSWORD_MAIL_FROM = "no-reply@" + getEnvironment().get(MailProps.class).domain;

    //~ Inner Classes ................................................................................................................................

    private static class Mails {
        public static void lostPassword(@NotNull final String email, @NotNull final String url)
            throws MailException
        {
            final String subject = RESET_EMAIL_SUBJECT.label();
            final String body    = format(RESET_EMAIL_BODY.label(), url, url);

            logger.info(format("Sending mail to %s: %s:%s", email, subject, body));
            final Mail resetEmail = new Mail().from(RESET_PASSWORD_MAIL_FROM).to(Colls.listOf(email)).withSubject(subject).withBody(body);

            send(resetEmail);
        }
    }
}  // end class ForgotPassword
