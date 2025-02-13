
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mail;

import tekgenesis.cluster.MessageHandler;
import tekgenesis.common.env.context.Context;

/**
 * MailSenderMessageHandler.
 */
public class MailSenderMessageHandler implements MessageHandler<String> {

    //~ Methods ......................................................................................................................................

    @Override public void handle(String message) {
        if (Context.getContext().hasBinding(MailProcessor.class)) {
            final MailProcessor processor = Context.getContext().getSingleton(MailProcessor.class);
            processor.sendMails();
        }
    }

    @Override public Short getScope() {
        return MessageHandler.SEND_MAILS;
    }
}
