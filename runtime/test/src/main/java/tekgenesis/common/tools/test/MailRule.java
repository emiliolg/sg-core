
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.tools.test;

import javax.mail.internet.MimeMessage;

import com.icegreen.greenmail.util.GreenMail;

import org.jetbrains.annotations.NotNull;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import tekgenesis.common.core.Times;
import tekgenesis.common.env.context.Context;
import tekgenesis.mail.MailProcessor;
import tekgenesis.mail.MailProps;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.env.context.Context.getEnvironment;

/**
 * Mail Rule to mock Mail Server.
 */
@SuppressWarnings({ "MagicNumber", "DuplicateStringLiteralInspection" })
public class MailRule implements TestRule {

    //~ Instance Fields ..............................................................................................................................

    private MailProcessor mailProcessor = null;

    private final GreenMail mailServer = new GreenMail();

    //~ Methods ......................................................................................................................................

    @Override public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override public void evaluate()
                throws Throwable
            {
                before();
                mailProcessor = new MailProcessor();

                Context.getContext().setSingleton(MailProcessor.class, mailProcessor);
                mailServer.start();
                mailProcessor.start();
                try {
                    base.evaluate();
                }
                finally {
                    after();
                    mailProcessor.shutdown();
                    mailServer.stop();
                }
            }
        };
    }

    /** Return inbox messages.* */
    public MimeMessage[] inbox() {
        return mailServer.getReceivedMessages();
    }

    /** Wait for messages.* */
    public void waitForReceiveMessages(int nro)
        throws InterruptedException
    {
        final long now = System.currentTimeMillis();
        while (inbox().length < nro) {
            Thread.sleep(100);
            assertThat(System.currentTimeMillis() - now).isLessThan(Times.MILLIS_SECOND * 20);
        }
    }

    protected void after() {
        if (mailProcessor != null) mailProcessor.shutdown();
    }

    protected void before() {
        final MailProps mailProps = defaultMailProps();
        setupProps(mailProps);
        setupProps(MAIL_TEST_PROP, mailProps);
    }

    @NotNull protected MailProps defaultMailProps() {
        final MailProps mailProps = new MailProps();
        mailProps.hostname = "localhost";
        mailProps.port     = 3025;
        mailProps.username = "suigeneris@tekgenesis.com";
        mailProps.password = "password";
        return mailProps;
    }

    protected void setupProps(@NotNull MailProps mailProps) {
        setupProps("", mailProps);
    }
    protected void setupProps(@NotNull String scope, @NotNull MailProps mailProps) {
        getEnvironment().put(scope, mailProps);
        mailServer.setUser(mailProps.username, mailProps.password);
    }

    //~ Static Fields ................................................................................................................................

    public static final String MAIL_TEST_PROP = "mailTest";
}  // end class MailRule
