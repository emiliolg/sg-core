
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

import tekgenesis.mail.MailStatus;
import tekgenesis.service.ServiceMBean;

/**
 * MailServiceMBean.
 */
public interface EmailServiceMBean extends ServiceMBean {

    //~ Methods ......................................................................................................................................

    /** Clean mail queue by status. */
    void cleanMailQueue(MailStatus... statuses);

    /** re-send pending emails. */
    void forceSendMails();

    /** Retry by mail status. */
    void retryMailSendFor(MailStatus statuses);

    /** Mail queue count. */
    EnumMap<MailStatus, Integer> getMailQueue();
}
