
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx.service;

import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.service.ServiceMBean;

import static java.lang.String.format;

import static tekgenesis.task.jmx.JmxConstants.*;

/**
 * Wrapper over ServiceMBean.
 */
public class RemoteService implements ServiceMBean {

    //~ Instance Fields ..............................................................................................................................

    protected final JmxEndpoint conn;
    protected final String      jmxServiceName;
    private final String        serviceName;

    //~ Constructors .................................................................................................................................

    /**  */
    public RemoteService(String name, JmxEndpoint c) {
        conn           = c;
        serviceName    = name;
        jmxServiceName = format("tekgenesis.cluster:Name=%s,type=service", serviceName);
    }

    //~ Methods ......................................................................................................................................

    @Override public void disable() {  // noinspection DuplicateStringLiteralInspection
        JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).invoke("disable", EMPTY_SIGNATURE, EMPTY_ARGS);
    }

    @Override public void enable() {  // noinspection DuplicateStringLiteralInspection
        JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).invoke("enable", EMPTY_SIGNATURE, EMPTY_ARGS);
    }

    @Override public void start() {
        JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).invoke(START, EMPTY_SIGNATURE, EMPTY_ARGS);
    }

    @Override public void stop() {
        JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).invoke(STOP, EMPTY_SIGNATURE, EMPTY_ARGS);
    }

    @Override public boolean isEnabled() {
        return JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).getAttribute("Enabled");
    }

    @Override public boolean isRunning() {  // noinspection DuplicateStringLiteralInspection
        return JmxInvokerImpl.invoker(conn).mbean(jmxServiceName).getAttribute("Running");
    }

    @Override public String getName() {
        return serviceName;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2488602276024919165L;
}  // end class RemoteService
