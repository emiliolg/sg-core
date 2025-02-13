
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx.service;

import java.util.EnumMap;

import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.mail.MailStatus;
import tekgenesis.service.ServiceMBean;

import static tekgenesis.task.jmx.JmxConstants.EMPTY_ARGS;
import static tekgenesis.task.jmx.JmxConstants.EMPTY_SIGNATURE;

/**
 * MailServiceInstance.
 */
public class RemoteMailService extends RemoteService implements ServiceMBean {

    //~ Constructors .................................................................................................................................

    /**  */
    public RemoteMailService(String name, JmxEndpoint c) {
        super(name, c);
    }

    //~ Methods ......................................................................................................................................

    /** Clean mail queue by status. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void cleanMailQueue(MailStatus... statuses) {
        JmxInvokerImpl.invoker(conn)
            .mbean(jmxServiceName)
            .invoke("cleanMailQueue", new String[] { MailStatus[].class.getName() }, new Object[] { statuses });
    }

    /** re-send pending emails. */
    public void forceSendMails() {
        JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).invoke("forceSendMails", EMPTY_SIGNATURE, EMPTY_ARGS);
    }

    /** Retry by mail status. */
    public void retryMailSendFor(MailStatus statuses) {
        // noinspection DuplicateStringLiteralInspection
        JmxInvokerImpl.invoker(conn)
            .mbean(jmxServiceName)
            .invoke("retryMailSendFor", new String[] { MailStatus.class.getName() }, new Object[] { statuses });
    }

    /** Mail queue count. */
    public EnumMap<MailStatus, Integer> getMailQueue() {
        return JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).getAttribute("MailQueue");
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -7150025537706176489L;
}  // end class RemoteMailService
