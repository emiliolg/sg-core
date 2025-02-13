
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.context.Context;
import tekgenesis.mail.MailProcessor;

import static tekgenesis.mail.MailStatus.PENDING;

/**
 * User class for Task: RetryMailSender
 */
public class RetryMailSender extends RetryMailSenderBase {

    //~ Constructors .................................................................................................................................

    protected RetryMailSender(@NotNull final ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override protected Status run() {
        if (Context.getContext().hasBinding(MailProcessor.class)) {
            final MailProcessor mailProcessor = Context.getSingleton(MailProcessor.class);
            mailProcessor.retryEmails(PENDING, getProgressMeter());
            mailProcessor.sendPendingEmails(getProgressMeter());
        }

        return Status.ok();
    }
}
