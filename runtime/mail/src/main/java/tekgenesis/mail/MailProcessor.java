
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mail;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.IntIntTuple;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.media.Mime;
import tekgenesis.common.util.LruCache;
import tekgenesis.common.util.ProgressMeter;
import tekgenesis.database.exception.DatabaseConcurrencyException;
import tekgenesis.metric.core.StatusMeterMetric;
import tekgenesis.persistence.Criteria;

import static java.lang.String.format;

import static javax.mail.Message.RecipientType.*;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.mkString;
import static tekgenesis.common.collections.Colls.set;
import static tekgenesis.common.core.Constants.UTF8;
import static tekgenesis.common.core.Strings.split;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.mail.MailStatus.*;
import static tekgenesis.mail.g.MailQueueTable.MAIL_QUEUE;
import static tekgenesis.persistence.Sql.*;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Email Processor.
 */
public class MailProcessor {

    //~ Instance Fields ..............................................................................................................................

    private final Environment env;

    private boolean isRunning;

    private final LruCache<DateTime, IntIntTuple> stats;

    //~ Constructors .................................................................................................................................

    /** .* */
    public MailProcessor() {
        env   = Context.getEnvironment();
        stats = LruCache.createLruCache(env.get(MailProps.class).statsCache);
    }

    //~ Methods ......................................................................................................................................

    /**
     * clean mail queue. By default only clean Success mails sent.
     *
     * @param  status  Array of status to clean.
     */
    public void cleanQueue(MailStatus... status) {
        // if (isRunning) throw new IllegalStateException("Queue can not be clean if the processor is running");
        runInTransaction(() ->
                deleteFrom(MAIL_QUEUE)                                                         //
                .where(MAIL_QUEUE.STATUS.in(isNotEmpty(status) ? set(status) : set(SUCCESS)))  //
                .execute());
    }

    /** clear stats.* */
    public void clearStats() {
        stats.evictAll();
    }

    /** Send email to retry.* */
    public void retryEmails(@NotNull MailStatus status, @Nullable ProgressMeter meter) {
        retry(meter, MAIL_QUEUE.STATUS.eq(status));
    }

    /** .* */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void sendMails() {
        if (!isRunning) throw new IllegalStateException("Mail processor is already running");
        final ClusterManager<?> clusterManager = Context.getSingleton(ClusterManager.class);
        if (!clusterManager.isMaster()) {
            logger.debug(format("Node %s is not master. Not sending mail from this node", clusterManager.getCurrentMember()));
            return;
        }

        logger.info("Processing mails to send ");
        boolean                 processMails   = true;
        final StatusMeterMetric metric         = new StatusMeterMetric("Mail", "sent");
        int                     mailsProcessed = 0;
        int                     mailsFails     = 0;
        while (isRunning && processMails) {
            processMails = false;
            for (final MailEntry entry : pendingMails()) {
                processMails = true;

                final boolean ret = entry.processEmail();
                metric.mark(ret);
                if (ret) mailsProcessed++;
                else mailsFails++;
            }

            if (processMails) stats.put(DateTime.current(), tuple(mailsProcessed, mailsFails));
        }
        logger.info(format("Mail Processor result: Ok %d, Fails %d", mailsProcessed, mailsFails));
    }  // end method sendMails

    /** Send pending emails not sent 5 minute ago. */
    public void sendPendingEmails(@Nullable ProgressMeter meter) {
        final DateTime timeFrame = DateTime.current().addMinutes(-5);
        final Criteria criteria  = MAIL_QUEUE.STATUS.eq(PENDING).and(MAIL_QUEUE.UPDATE_TIME.lt(timeFrame));
        retry(meter, criteria);
    }

    /** .* */
    public void shutdown() {
        isRunning = false;
    }

    /** Start Processor.* */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void start() {
        if (isRunning) throw new IllegalStateException("Mail processor is already running");
        isRunning = true;
    }

    /** Return stats about mails sends.* */
    public Map<DateTime, IntIntTuple> stats() {
        return stats.snapshot();
    }

    /** Running. */
    public boolean isRunning() {
        return isRunning;
    }

    private ImmutableList<MailEntry> listMails(Criteria criteria) {  //
        return invokeInTransaction(() -> selectFrom(MAIL_QUEUE).where(criteria).map(m -> new MailEntry(m, env)).toList());
    }

    private ImmutableList<MailEntry> pendingMails() {
        return listMails(MAIL_QUEUE.DATE.le(DateTime.current()).and(MAIL_QUEUE.STATUS.eq(PENDING)));
    }

    private void retry(@Nullable ProgressMeter meter, Criteria criteria) {
        final ImmutableList<MailEntry> mailQueues = listMails(criteria);

        if (meter != null) meter.setItemsToProcess(mailQueues.size());

        for (final MailEntry mailQueue : mailQueues) {
            mailQueue.processEmail();
            if (meter != null) meter.advance();
        }
    }

    //~ Methods ......................................................................................................................................

    private static String asRecipients(Seq<String> list) {
        return mkString(list, "", ",", "");
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(MailProcessor.class);

    //~ Inner Classes ................................................................................................................................

    static class MailEntry {
        final Mail              mail;
        final MailQueue         queueEntry;
        private final MailProps mailProps;
        private final int       maxRetries;

        public MailEntry(MailQueue e, Environment env) {
            queueEntry = e;
            mail       = new Mail().withSubject(e.getSubject())
                         .replyTo(split(e.getReplyTo(), ','))
                         .from(e.getFrom())
                         .to(split(e.getTo(), ','))
                         .cc(split(e.getCc(), ','))
                         .bcc(split(e.getBcc(), ','))
                         .withBody(e.getBody());

            for (final Attachment attachment : e.getAttachments())
                mail.addAttachment(attachment.getResourceId());

            mailProps  = env.get(e.getMainProp(), MailProps.class);
            maxRetries = env.get(MailProps.class).retryNro;
        }

        private Option<Status> lockMailItem() {
            //J-
            try {
                return select(MAIL_QUEUE.STATUS, MAIL_QUEUE.RETRY)
                        .from(MAIL_QUEUE)
                        .where(MAIL_QUEUE.ID.eq(queueEntry.getId()))
                        .forUpdate()
                        .map(qt -> new Status(qt.getOrFail(MAIL_QUEUE.STATUS), qt.getOrFail(MAIL_QUEUE.RETRY)))
                        .getFirst();

            }
            catch (final DatabaseConcurrencyException e) {
                return Option.empty();
            }
            //J+
        }

        private boolean processEmail() {  //
            return invokeInTransaction(() ->
                    lockMailItem().filter(s -> s.status == PENDING || s.status == RETRY)
                                  .map(Status::processMail)
                                  .orElse(false));
        }

        /** Send an Email directly using JavaMail. */
        @SuppressWarnings("SpellCheckingInspection")
        private void sendEmail()
            throws MessagingException
        {
            final Properties props = new Properties();
            props.put("mail.smtp.host", mailProps.hostname);
            props.put("mail.smtp.port", mailProps.port);
            props.put("mail.smtp.auth", mailProps.auth);
            props.put("mail.smtp.starttls.required", mailProps.requiredTls);
            props.put("mail.smtp.starttls.enable", mailProps.tls);
            props.put("mail.transport.protocol", mailProps.protocol);

            final Session session = Session.getDefaultInstance(props);

            final Transport transport = session.getTransport();
            try {
                transport.connect(mailProps.hostname, mailProps.username, mailProps.password);

                final MimeMessage msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(mail.getFrom()));
                msg.addRecipients(TO, asRecipients(mail.getTos()));
                msg.addRecipients(CC, asRecipients(mail.getCc()));
                msg.addRecipients(BCC, asRecipients(mail.getBcc()));
                msg.setSentDate(DateTime.current().toDate());

                if (!mail.getReplyTo().isEmpty()) msg.setReplyTo(InternetAddress.parse(asRecipients(mail.getReplyTo())));

                msg.setSubject(mail.getSubject(), UTF8);

                final List<Resource> resources = mail.getResources();
                if (resources.isEmpty()) msg.setContent(mail.getBody(), Mime.TEXT_HTML.getMime());
                else {
                    final BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent(mail.getBody(), Mime.TEXT_HTML.getMime());

                    final Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(messageBodyPart);

                    for (final Resource resource : resources) {
                        final MimeBodyPart attachmentBP = new MimeBodyPart();

                        final Resource.Entry        master       = resource.getMaster();
                        final String                name         = master.getName();
                        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        master.copyTo(outputStream);

                        final DataSource dataSource = new ByteArrayDataSource(outputStream.toByteArray(), master.getMimeType());
                        attachmentBP.setDataHandler(new DataHandler(dataSource));
                        attachmentBP.setFileName(name);
                        attachmentBP.setContentID(name);
                        if (master.getMimeType().startsWith(Mime.IMAGE.getType()))
                        // noinspection DuplicateStringLiteralInspection
                        attachmentBP.setDisposition("inline");

                        multipart.addBodyPart(attachmentBP);
                    }

                    msg.setContent(multipart);
                }

                transport.sendMessage(msg, msg.getAllRecipients());
            }
            finally {
                transport.close();
            }
        }  // end method sendEmail

        class Status {
            final int retries;

            final MailStatus status;

            public Status(MailStatus status, int retries) {
                this.retries = retries;
                this.status  = status;
            }

            @NotNull private Boolean processMail() {
                if (isEmpty(mailProps.hostname)) {
                    logger.warning(
                        format("Email without hostname. Using property name '%s' for email id '%d'", queueEntry.getMainProp(), queueEntry.getId()));
                    queueEntry.setStatus(FAILED).persist();
                    return false;
                }

                try {
                    if (status == RETRY) queueEntry.setRetry(retries + 1);
                    sendEmail();
                    queueEntry.setSent(true).setStatus(SUCCESS).persist();
                    return true;
                }
                catch (final MessagingException e) {
                    logger.warning(format("Unable to send email: [%s]", mail.toString()), e);
                    queueEntry.setStatus(status == RETRY && retries >= maxRetries ? FAILED : RETRY).persist();
                    return false;
                }
            }
        }
    }  // end class MailEntry
}  // end class MailProcessor
