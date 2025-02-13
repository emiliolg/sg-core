
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app.service;

import java.util.EnumMap;
import java.util.Map;

import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.IntIntTuple;
import tekgenesis.mail.MailProcessor;
import tekgenesis.mail.MailSenderMessageHandler;
import tekgenesis.mail.MailStatus;
import tekgenesis.persistence.expr.Expr;
import tekgenesis.service.Service;
import tekgenesis.service.ServiceManager;

import static java.util.Collections.emptyMap;

import static tekgenesis.cluster.MessageHandler.SEND_MAILS;
import static tekgenesis.common.env.context.Context.getContext;
import static tekgenesis.mail.g.MailQueueTable.MAIL_QUEUE;
import static tekgenesis.persistence.Sql.select;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * EmailService.
 */
public class EmailService extends Service implements EmailServiceMBean {

    //~ Instance Fields ..............................................................................................................................

    private MailProcessor mailProcessor = null;

    //~ Constructors .................................................................................................................................

    /** Create the EmailService. */
    public EmailService(final ServiceManager serviceManager) {
        super(serviceManager, SERVICE_NAME, SERVICE_START_ORDER);
    }

    //~ Methods ......................................................................................................................................

    @Override public void cleanMailQueue(MailStatus... statuses) {
        getContext().getSingleton(MailProcessor.class).cleanQueue(statuses);
    }

    @Override public void forceSendMails() {
        getContext().getSingleton(MailProcessor.class).sendMails();
    }

    @Override public void retryMailSendFor(MailStatus statuses) {
        getContext().getSingleton(MailProcessor.class).retryEmails(statuses, null);
    }

    /** Return stats. */
    public Map<DateTime, IntIntTuple> stats() {
        return mailProcessor == null ? emptyMap() : mailProcessor.stats();
    }

    @Override public boolean isRunning() {
        return mailProcessor != null && mailProcessor.isRunning();
    }

    //J-
    @Override public EnumMap<MailStatus, Integer> getMailQueue() {
        final EnumMap<MailStatus, Integer> ret = new EnumMap<>(MailStatus.class);
        final Expr.Long                    C   = MAIL_QUEUE.STATUS.count();
        runInTransaction(() ->
                select(MAIL_QUEUE.STATUS, C) //
                        .from(MAIL_QUEUE) //
                        .groupBy(MAIL_QUEUE.STATUS)
                        .forEach(t -> ret.put(t.getOrFail(MAIL_QUEUE.STATUS), t.getOrFail(C).intValue())));
        return ret;
    }
    //J+

    @Override protected void doShutdown() {
        if (getContext().hasBinding(MailProcessor.class)) {
            clusterManager.deRegisterMessageHandler(SEND_MAILS);
            if (mailProcessor.isRunning()) mailProcessor.shutdown();
        }
    }
    @Override protected void doStart() {
        mailProcessor = new MailProcessor();
        getContext().setSingleton(MailProcessor.class, mailProcessor);
        mailProcessor.start();
        clusterManager.registerMessageHandler(new MailSenderMessageHandler());
    }

    //~ Static Fields ................................................................................................................................

    public static final String SERVICE_NAME        = EmailService.class.getSimpleName();
    private static final int   SERVICE_START_ORDER = 4;
}  // end class EmailService
