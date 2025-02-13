
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.management.*;

import tekgenesis.app.service.EmailService;
import tekgenesis.cluster.jmx.notification.RemoteApp;
import tekgenesis.cluster.jmx.util.MemoryUsageInfo;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.IntIntTuple;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.Version;
import tekgenesis.common.logging.Logger;
import tekgenesis.mail.MailStatus;
import tekgenesis.service.Service;
import tekgenesis.service.ServiceManager;
import tekgenesis.service.ServiceStatus;
import tekgenesis.task.TaskService;

import static java.util.Collections.emptyMap;

/**
 * AppObject used to call App methods cluster wide.
 */
public class AppObject implements RemoteApp, Serializable {

    //~ Instance Fields ..............................................................................................................................

    private final ServiceManager serviceManager;
    private final UpgradeUtil    upgradeUtil;

    //~ Constructors .................................................................................................................................

    /** Create AppObject. */
    public AppObject(final ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        upgradeUtil         = new UpgradeUtil(serviceManager);
    }

    //~ Methods ......................................................................................................................................

    /** Clean mail Queue. */
    public void cleanMailQueue(MailStatus[] statuses) {
        serviceManager.getService(EmailService.class).ifPresent(s -> s.cleanMailQueue(statuses));
    }

    /** Starts Main Service Processor.* */
    public void forceSendMails() {
        serviceManager.getService(EmailService.class).ifPresent(EmailService::forceSendMails);
    }

    /** retry send mail for. */
    public void retryMailSendFor(MailStatus statuses) {
        serviceManager.getService(EmailService.class).ifPresent(s -> s.retryMailSendFor(statuses));
    }

    /** Start service. */
    public void startService(String serviceName) {
        serviceManager.start(serviceName);
    }

    /** Stop service. */
    public void stopService(String serviceName) {
        serviceManager.stop(serviceName);
    }

    /** upgrade app version.* */
    public Tuple<Boolean, String> upgrade(Integer buildNumber, String branchName, Boolean healthCheck) {
        return upgradeUtil.upgrade(buildNumber, branchName, healthCheck);
    }

    /** Return component info list. */
    public List<Version.ComponentInfo> getComponents() {
        return Version.getInstance().getComponents().into(new ArrayList<>());
    }

    /** Return MemoryUsage. */
    public double getCpuUsage() {
        try {
            final MBeanServer   mbs  = ManagementFactory.getPlatformMBeanServer();
            final ObjectName    name = ObjectName.getInstance("java.lang:type=OperatingSystem");
            final AttributeList list = mbs.getAttributes(name, new String[] { "ProcessCpuLoad" });
            if (list.isEmpty()) return 0.0;

            final Double value = (Double) ((Attribute) list.get(0)).getValue();

            return value == -1.0 ? 0.0 : Math.rint(value * 1_000) / 10;
        }
        catch (MalformedObjectNameException | ReflectionException | InstanceNotFoundException e) {
            logger.warning(e);
        }
        return 0.0;
    }

    /** Return if the Email service is running.* */
    public boolean isMailProcessorRunning() {
        return isRunning(EmailService.class);
    }

    /** Return if the task service is running.* */
    public boolean isTaskServiceRunning() {
        return isRunning(TaskService.class);
    }

    /** .* */
    public Map<DateTime, IntIntTuple> getMailProcessorStats() {
        return serviceManager.getService(EmailService.class).map(EmailService::stats).orElse(emptyMap());
    }

    /** Return MemoryUsage. */
    public MemoryUsageInfo getMemoryUsage() {
        return MemoryUsageInfo.getMemoryUsageInfo();
    }

    @Override public Object getObject() {
        return this;
    }

    /** Return the status of each service. */
    public Map<String, ServiceStatus> getServices() {
        return serviceManager.getServicesStatus();
    }

    /** UpTime. */
    public long getUpTime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }

    private boolean isRunning(final Class<? extends Service> service) {
        return serviceManager.getService(service).filter(Service::isRunning).isPresent();
    }

    //~ Static Fields ................................................................................................................................

    private static final long   serialVersionUID = 6157810438226957089L;
    private static final Logger logger           = Logger.getLogger(AppObject.class);
}  // end class AppObject
