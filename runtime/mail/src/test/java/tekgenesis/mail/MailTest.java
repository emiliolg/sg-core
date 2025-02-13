
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.LocalClusterManager;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.IntIntTuple;
import tekgenesis.common.core.Resource;
import tekgenesis.common.media.Mime;
import tekgenesis.common.service.HeaderNames;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.DbTimeProviderRule;
import tekgenesis.common.tools.test.MailRule;
import tekgenesis.common.util.Files;
import tekgenesis.persistence.resource.DbResourceHandler;

import static javax.mail.Message.RecipientType.CC;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.cluster.MessageHandler.SEND_MAILS;
import static tekgenesis.common.collections.Colls.*;
import static tekgenesis.common.env.context.Context.*;
import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.mail.g.MailQueueTable.MAIL_QUEUE;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 * Mail.
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "SpellCheckingInspection", "DuplicateStringLiteralInspection", "MagicNumber" })
public class MailTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public String dbName = null;

    @Rule public final DbTimeProviderRule timeProvider = new DbTimeProviderRule();
    private DbResourceHandler             rh           = null;

    @Rule public DbRule db = new DbRule(DbRule.SG, DbRule.MAIL) {
            ClusterManager<org.jgroups.Address> clusterManager = null;

            @Override protected void after() {
                super.after();
                if (clusterManager != null) {
                    clusterManager.deRegisterMessageHandler(SEND_MAILS);
                    clusterManager.stop();
                }
            }

            @Override protected void before() {
                createDatabase(dbName);
                rh = new DbResourceHandler(env, database);

                clusterManager = new LocalClusterManager();
                try {
                    clusterManager.registerMessageHandler(new MailSenderMessageHandler());
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
                getContext().setSingleton(ClusterManager.class, clusterManager);
                try {
                    clusterManager.start();
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

    @Rule public MailRule server = new MailRule();

    //~ Methods ......................................................................................................................................

    @Test public void testAddressChecker()
        throws MailException, InterruptedException
    {
        for (final String address : VALID_ADDRESSES)
            assertThat(Mail.checkEmailAddress(address)).describedAs("Validation failed for " + address).isTrue();

        for (final String address : INVALID_ADDRESSES)
            assertThat(Mail.checkEmailAddress(address)).describedAs("Validation NOT failed for " + address).isFalse();

        assertThat(Mail.checkEmailAddresses(String.join(";", VALID_ADDRESSES_NORMAL))).describedAs("Validation failed list").isTrue();
        assertThat(Mail.checkEmailAddresses(String.join(";", INVALID_ADDRESSES_NORMAL))).describedAs("Validation NOT failed list").isFalse();
    }

    @Test public void testCleanQueue()
        throws MailException, InterruptedException
    {
        // Simple Email
        final Mail simpleMail = createSimpleMail();

        MailSender.send(MailRule.MAIL_TEST_PROP, simpleMail);

        final MailProcessor mailProcessor = getSingleton(MailProcessor.class);

        server.waitForReceiveMessages(3);
        mailProcessor.cleanQueue();

        assertThat(selectFrom(MAIL_QUEUE).count()).isEqualTo(0);
        setWrongMailProvider();

        MailSender.send(WRONG_MAIL_SERVER, simpleMail);
        // noinspection MagicNumber
        Thread.sleep(500);
        assertThat(selectFrom(MAIL_QUEUE).count()).isEqualTo(1);
        mailProcessor.cleanQueue();
        assertThat(selectFrom(MAIL_QUEUE).count()).isEqualTo(1);
    }

    @Test public void testDefaultScope()
        throws MailException, InterruptedException
    {
        final MailProps props = getEnvironment().get(MailProps.class);
        props.defaultPropScope = MailRule.MAIL_TEST_PROP;
        getEnvironment().put(props);

        // Simple Email
        MailSender.send(createSimpleMail());
        server.waitForReceiveMessages(3);
    }

    @Test public void testHtmlSimpleMail()
        throws Exception
    {
        // Simple Email
        final Mail simpleMail = new Mail().from("tekgenesis@tekgenesis.com")
                                .to(listOf("test@testmail.com"))
                                .withSubject("Hello World Mail")
                                .withBody(
                "<html>" +
                "<head>" +
                "<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />" +
                "<title>Tu pedido ha sido procesado!</title>" +
                "</head>" +
                "<body><p>Tu pedido fue exitoso!!</p></body>" +
                "</html>");

        MailSender.send(MailRule.MAIL_TEST_PROP, simpleMail);

        server.waitForReceiveMessages(1);
        final MimeMessage[] inbox = server.inbox();
        assertThat(inbox).isNotNull();
        assertThat(inbox.length).isEqualTo(1);

        final MimeMessage message = server.inbox()[0];

        assertMail(message, simpleMail);

        final String[] header = message.getHeader(HeaderNames.CONTENT_TYPE);
        assertThat(header).isNotNull();
        assertThat(header.length).isEqualTo(1);

        assertThat(header[0]).startsWith(Mime.TEXT_HTML.getMime());

        final MailProcessor              mailProcessor = getSingleton(MailProcessor.class);
        final Map<DateTime, IntIntTuple> stats         = mailProcessor.stats();
        assertThat(stats.size()).isEqualTo(1);

        final DateTime    firstStats = stats.keySet().iterator().next();
        final IntIntTuple tuple      = stats.get(firstStats);
        assertThat(tuple.first()).isEqualTo(1);
        assertThat(tuple.second()).isEqualTo(0);
    }  // end method testHtmlSimpleMail

    @Test(expected = MailException.class)
    public void testInvalidMail()
        throws MailException
    {
        // Simple Email
        final Mail simpleMail = createSimpleMail();
        simpleMail.to(Colls.emptyIterable());
        simpleMail.bcc(Colls.emptyIterable());
        simpleMail.cc(Colls.emptyIterable());

        MailSender.send(MailRule.MAIL_TEST_PROP, simpleMail);
    }

    @Test(expected = MailException.class)
    public void testInvalidMailAddress()
        throws MailException
    {
        // Simple Email
        final Mail simpleMail = createSimpleMail();
        simpleMail.to(seq(set("mkyong", "aaa@", " mkyong@.com.my", "test@test.com")));

        MailSender.send(MailRule.MAIL_TEST_PROP, simpleMail);
    }

    @Test public void testMultipleMail()
        throws MailException, InterruptedException
    {
        final Mail         baseMail = new Mail().from("tekgenesis@tekgenesis.com")
                                      .to(listOf("testA@tekgenesis.com", "testB@tekgenesis.com"))
                                      .cc(listOf("cc@testmail.com"))
                                      .bcc(listOf("bcc@testmail.com"))
                                      .withSubject("Hello World Mail")
                                      .withBody("This is a simple message");
        final MultipleMail mMail    = new MultipleMail(baseMail);

        mMail.send(MailRule.MAIL_TEST_PROP);

        server.waitForReceiveMessages(6);
        final MimeMessage[] inbox = server.inbox();
        assertThat(inbox).isNotNull();
        assertThat(inbox.length).isEqualTo(6);
    }

    @Test public void testReplyTo()
        throws MailException, InterruptedException, MessagingException
    {
        final Mail baseMail = new Mail().from("tekgenesis@tekgenesis.com")
                              .replyTo(listOf("reply@tekgenesis.com"))
                              .to(listOf("testB@tekgenesis.com"))
                              .withSubject("Hello World Mail")
                              .withBody("This is a simple message");

        MailSender.send(MailRule.MAIL_TEST_PROP, baseMail);

        server.waitForReceiveMessages(1);
        final MimeMessage[] inbox = server.inbox();
        assertThat(inbox).isNotNull();
        assertThat(inbox.length).isEqualTo(1);
        final Address address = inbox[0].getReplyTo()[0];
        assertThat(address.toString()).isEqualTo("reply@tekgenesis.com");
    }

    @Test public void testRetryNotSend()
        throws Exception
    {
        // Set Wrong port to force fail send
        setWrongMailProvider();

        // Simple Email
        final Mail simpleMail = createSimpleMail();

        MailSender.send(WRONG_MAIL_SERVER, simpleMail);
        Thread.sleep(500);  // Wait until mail processor mark mails as not sent. If not the test finished before mailProcessor
        final MimeMessage[] inbox = server.inbox();
        assertThat(inbox).isNotNull();
        assertThat(inbox.length).isEqualTo(0);

        final MailProcessor              mailProcessor = getSingleton(MailProcessor.class);
        final Map<DateTime, IntIntTuple> stats         = mailProcessor.stats();
        assertThat(stats.size()).isEqualTo(1);

        final DateTime    firstStats = stats.keySet().iterator().next();
        final IntIntTuple tuple      = stats.get(firstStats);
        assertThat(tuple.first()).isEqualTo(0);
        assertThat(tuple.second()).isEqualTo(1);

        mailProcessor.retryEmails(MailStatus.RETRY, null);
        final MailQueue first = assertNotNull(MailQueue.list().get());
        assertThat(first.getRetry()).isEqualTo(1);
    }

    @Test public void testSimpleMail()
        throws Exception
    {
        // Simple Email
        final Mail simpleMail = createSimpleMail();

        MailSender.send(MailRule.MAIL_TEST_PROP, simpleMail);

        server.waitForReceiveMessages(3);
        final MimeMessage[] inbox = server.inbox();
        assertThat(inbox).isNotNull();
        assertThat(inbox.length).isEqualTo(3);

        assertMail(server.inbox()[0], simpleMail);
    }
    @Test public void testSimpleMailFutureSentDate()
        throws Exception
    {
        final Date currentDate = DateTime.current().toDate();
        timeProvider.setCurrentTime(currentDate.getTime());
        // Simple Email
        final DateTime schedule   = DateTime.current().addHours(2);
        final Mail     simpleMail = createSimpleMail().schedule(schedule);

        MailSender.send(MailRule.MAIL_TEST_PROP, simpleMail);

        server.waitForReceiveMessages(0);
        final MimeMessage[] inbox = server.inbox();
        assertThat(inbox).isNotNull();
        assertThat(inbox.length).isEqualTo(0);

        final DateTime sentDate = simpleMail.getDate().addMinutes(1);
        timeProvider.setCurrentTime(sentDate.toMilliseconds());

        MailSender.flushQueue();

        server.waitForReceiveMessages(3);
        final MimeMessage[] inboxRefreshed = server.inbox();
        assertThat(inboxRefreshed.length).isEqualTo(3);

        final MimeMessage mimeMessage = server.inbox()[0];
        assertMail(mimeMessage, simpleMail);
        assertThat(mimeMessage.getSentDate()).isEqualTo(sentDate.toDate());
    }

    @Test public void testSimpleMailSentDate()
        throws Exception
    {
        final Date currentDate = DateTime.current().toDate();
        timeProvider.setCurrentTime(currentDate.getTime());
        // Simple Email
        final Mail simpleMail = createSimpleMail();

        MailSender.send(MailRule.MAIL_TEST_PROP, simpleMail);

        server.waitForReceiveMessages(3);
        final MimeMessage[] inbox = server.inbox();
        assertThat(inbox).isNotNull();
        assertThat(inbox.length).isEqualTo(3);

        final MimeMessage mimeMessage = server.inbox()[0];
        assertMail(mimeMessage, simpleMail);
        assertThat(mimeMessage.getSentDate()).isEqualTo(currentDate);
    }

    @Test public void testSimpleMailWithAttachment()
        throws Exception
    {
        // Email with Attachment
        final Resource attachment1 = rh.create()
                                     .upload("Attachment1",
                Mime.APPLICATION_OCTET_STREAM.getMime(),
                new ByteArrayInputStream("This is an simple text for attachment".getBytes(Constants.UTF8)));

        final Mail mailWithAttachment = new Mail().from("tekgenesis@tekgenesis.com")
                                        .to(listOf("test@testmail.com"))
                                        .cc(listOf("cc@testmail.com"))
                                        .bcc(listOf("bcc@testmail.com"))
                                        .withSubject("Hello World Mail")
                                        .withBody("This is a simple message.\nWith other lines of \n text")
                                        .addAttachment(attachment1);

        MailSender.send(MailRule.MAIL_TEST_PROP, mailWithAttachment);

        server.waitForReceiveMessages(3);

        final MimeMessage[] mimeMessages = server.inbox();
        assertThat(mimeMessages.length).isEqualTo(3);
        assertMail(mimeMessages[2], mailWithAttachment);
    }

    private String asRecipients(Seq<String> list) {
        return mkString(list, "", ",", "");
    }

    private void assertMail(MimeMessage mimeMessage, Mail simpleMail)
        throws MessagingException, IOException
    {
        assertThat(mimeMessage).isNotNull();

        assertThat(simpleMail.getFrom()).isEqualTo(mimeMessage.getFrom()[0].toString());
        final Address[] recipients = mimeMessage.getRecipients(CC);

        final Seq<String> map = ImmutableList.fromArray(recipients).map(Object::toString);

        assertThat(asRecipients(simpleMail.getCc())).isEqualTo(asRecipients(map));

        assertThat(simpleMail.getSubject()).isEqualTo(mimeMessage.getSubject());
        assertThat(mimeMessage.getSentDate()).isNotNull();

        final Object mailContent = mimeMessage.getContent();
        if (mailContent instanceof Multipart) {
            final Multipart multipart = (Multipart) mailContent;
            final int       count     = multipart.getCount();

            assertThat(count).isEqualTo(simpleMail.getResources().size() + 1);

            final String content = ((String) multipart.getBodyPart(0).getContent()).replaceAll("\r", "");
            assertThat(simpleMail.getBody()).isEqualTo(content);

            for (int i = 1; i < count; i++) {
                final Resource resource = simpleMail.getResources().get(i - 1);
                final BodyPart bodyPart = multipart.getBodyPart(i);
                final String   name     = resource.getMaster().getName();
                assertThat(bodyPart.getFileName()).isEqualTo(name);
                // assertThat(bodyPart.getContentType()).isEqualTo(resource.getMaster().getMimeType());
                assertThat(bodyPart.getHeader("Content-Id")[0]).isEqualTo(name);
            }
        }
        else {
            final String content = Files.readInput(new InputStreamReader(mimeMessage.getRawInputStream()));
            assertThat(simpleMail.getBody()).isEqualTo(content);
        }
    }

    private Mail createSimpleMail() {
        return new Mail().from("tekgenesis@tekgenesis.com")
               .to(listOf("test@testmail.com"))
               .cc(listOf("cc@testmail.com"))
               .bcc(listOf("bcc@testmail.com"))
               .withSubject("Hello World Mail")
               .withBody("This is a simple message");
    }

    private void setWrongMailProvider() {
        // Set Wrong port to force fail send
        final MailProps mailServerProps = new MailProps();
        mailServerProps.hostname = "noValidHost";
        mailServerProps.port     = 123;
        mailServerProps.username = "suigeneris@tekgenesis.com";
        mailServerProps.password = "password";
        getEnvironment().put(WRONG_MAIL_SERVER, mailServerProps);
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    //~ Static Fields ................................................................................................................................

    private static final String[]       VALID_ADDRESSES          = {
        "mkyong@yahoo.com", "mkyong-100@yahoo.com", "mkyong.100@yahoo.com",
        "mkyong111@mkyong.com", "mkyong-100@mkyong.net", "mkyong.100@mkyong.com.au",
        "mkyong@1.com", "mkyong@gmail.com.com",
        "mkyong+100@gmail.com", "mkyong-100@yahoo-test.com",
        "A1@E.CC", "AA+11@E.CC", "mariano@tekgenesis.com", "mariano+A-2223@tekgenesis.com",

        // taken from https://en.wikipedia.org/wiki/Email_address
        "\"much.more unusual\"@example.com",
        "\"very.unusual.@.unusual.com\"@example.com",
        "\"very.(),:;<>[]\\\".VERY.\\\"very@\\\\ \\\"very\\\".unusual\"@strange.example.com",
        "#!$%&'*+-/=?^_`{}|~@example.org",
        "\" \"@example.org",
        // "üñîçøðé@example.com",          @ToDo check if still invalid now
        // "üñîçøðé@üñîçøðé.com",          @ToDo check if still invalid now
        // "Maradó@text.com",              @ToDo check if still invalid now
        // emails with names
        "TekGenesis (JIRA) <admin@tekgenesis.com>",
    };
    private static final String[]       INVALID_ADDRESSES        = {
        "mkyong",
        "mkyong@.com.my",
        // "mkyong123@gmail.a",            @ToDo check if still invalid now
        "mkyong123@.com",
        "mkyong123@.com.com",
        // ".mkyong@mkyong.com",           @ToDo check if still invalid now
        "mkyong()*@gmail.com",
        "mkyong@%*.com",
        // "mkyong..2002@gmail.com",       @ToDo check if still invalid now
        // "mkyong.@gmail.com",            @ToDo check if still invalid now
        "mkyong@mkyong@gmail.com",
        // "mkyong@gmail.com.1a",          @ToDo check if still invalid now
        // "test@example.com,,",           @ToDo check if still invalid now
        "Abc.example.com",
        "A@b@c@example.com",
        "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com",
        "just\"not\"right@example.com",
        "this is\"not\\allowed@example.com",
        "john.doe@example..com",
        " a valid address with a leading space",
        "a valid address with a trailing space ",
    };
    private static final CharSequence[] VALID_ADDRESSES_NORMAL   = { "test@example.com", "Name <text@example.com>", };
    private static final CharSequence[] INVALID_ADDRESSES_NORMAL = { "test.example.com", "Name <text@example.com>", "test@texx.com" };

    private static final String WRONG_MAIL_SERVER = "wrongMailServer";
}  // end class MailTest
