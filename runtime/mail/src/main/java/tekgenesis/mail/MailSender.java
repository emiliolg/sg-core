
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mail;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.MessageHandler;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Resource;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.Colls.mkString;
import static tekgenesis.common.env.context.Context.getEnvironment;
import static tekgenesis.common.env.context.Context.getSingleton;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Mail Provider.
 */
public class MailSender {

    //~ Constructors .................................................................................................................................

    private MailSender() {}

    //~ Methods ......................................................................................................................................

    /** Send all mails in queue. */
    public static void flushQueue() {
        try {
            getSingleton(ClusterManager.class).sendMessage(MessageHandler.SEND_MAILS, "");
        }
        catch (final Exception e) {
            // logger.error(e);
        }
    }

    /** Push Mail in Queue. The queue is not flushed and the transaction is not committed */
    public static void queue(@NotNull Mail mail)
        throws MailException
    {
        final String defaultPropScope = notEmpty(getEnvironment().get(MailProps.class).defaultPropScope, "");
        queue(defaultPropScope, mail);
    }
    /** Push Mail in Queue. The queue is not flushed and the transaction is not committed */
    public static void queue(@NotNull String config, @NotNull Mail mail)
        throws MailException
    {
        if (mail.getTos().isEmpty() && mail.getBcc().isEmpty() && mail.getCc().isEmpty())
            throw new MailException("No mail destination(to,bcc or cc) set.");

        checkEmailAddresses(mail.getTos());
        checkEmailAddresses(mail.getBcc());
        checkEmailAddresses(mail.getCc());

        mail.generate();

        final MailQueue mailQueue = MailQueue.create();
        mailQueue.setReplyTo(mail.getReplyTo().mkString("", ",", ""));
        mailQueue.setFrom(mail.getFrom());
        mailQueue.setTo(mail.getTos().mkString("", ",", ""));
        mailQueue.setCc(mail.getCc().mkString("", ",", ""));
        mailQueue.setBcc(mail.getBcc().mkString("", ",", ""));
        mailQueue.setSubject(mail.getSubject());
        mailQueue.setBody(mail.getBody());
        mailQueue.setSent(false);
        mailQueue.setDate(mail.getDate());
        mailQueue.setMainProp(config);
        final List<Resource> resources = mail.getResources();
        for (final Resource resource : resources) {
            final Attachment add = mailQueue.getAttachments().add();
            add.setResourceId(resource);
        }

        mailQueue.insert();
    }

    /** Push Mail in Mail Queue and send the mail with defualt config. */
    public static void send(@NotNull Mail mail)
        throws MailException
    {
        final String defaultPropScope = notEmpty(getEnvironment().get(MailProps.class).defaultPropScope, "");
        send(defaultPropScope, mail);
    }
    /** Push Mail in Mail Queue. */
    public static void send(@NotNull String config, @NotNull Mail mail)
        throws MailException
    {
        try {
            runInTransaction(() -> queue(config, mail));
        }
        finally {
            flushQueue();
        }
    }  // end method send

    static void checkEmailAddresses(Seq<String> tos)
        throws MailException
    {
        final List<String> invalidEmails = new ArrayList<>();
        for (final String s : tos) {
            try {
                new InternetAddress(s).validate();
            }
            catch (final AddressException ex) {
                invalidEmails.add(s);
            }
        }
        if (!invalidEmails.isEmpty()) throw new MailException(format("Invalid email addresses: %s", mkString(invalidEmails)));
    }
}  // end class MailSender
