
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.jgroups.Address;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.jmx.JmxHelper;
import tekgenesis.common.logging.Logger;

import static java.lang.String.format;

import static tekgenesis.common.core.Strings.deCapitalizeFirst;
import static tekgenesis.service.ServiceStatus.*;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Service.
 */
public abstract class Service implements Comparable<Service>, ServiceMBean {

    //~ Instance Fields ..............................................................................................................................

    protected ClusterManager<Address> clusterManager    = null;
    protected final Logger            logger;
    private String                    currentMemberName = SINGLETON;
    private final ServiceManager      serviceManager;
    private final String              serviceName;
    private final ServiceProps        serviceProps;
    private final int                 serviceStartOrder;
    private ServiceStatus             status;

    //~ Constructors .................................................................................................................................

    protected Service(ServiceManager sm, String serviceName, int order) {
        this(sm, serviceName, order, ServiceProps.class);
    }

    protected Service(ServiceManager sm, String serviceName, int order, Class<? extends ServiceProps> p) {
        status            = DISABLED;
        serviceManager    = sm;
        this.serviceName  = serviceName;
        serviceProps      = sm.getEnvironment().get(deCapitalizeFirst(serviceName), p);
        serviceStartOrder = order;
        serviceManager.register(this);
        logger = Logger.getLogger(getClass());
    }

    //~ Methods ......................................................................................................................................

    @Override public int compareTo(@NotNull Service o2) {
        return Integer.compare(getStartLevel(), o2.getStartLevel());
    }

    @Override public void disable() {
        runInTransaction(() -> {
            if (isRunning()) shutdown();
            enable(false);
        });
    }

    @Override public void enable() {
        runInTransaction(() -> enable(true));
        if (!isRunning()) start();
    }

    public boolean equals(Object o) {
        return o instanceof Service && (o == this || getName().equals(((Service) o).getName()));
    }

    public int hashCode() {
        return getName().hashCode();
    }

    /** Initialize service. */
    public void init(final ClusterManager<Address> cm) {
        clusterManager    = cm;
        currentMemberName = clusterManager == null ? SINGLETON : clusterManager.getCurrentMember();
        if (isEnabled()) status = ENABLED;
    }

    /**  */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void registerMBean() {
        JmxHelper.registerMBean(getName(), JmxHelper.CLUSTER_DOMAIN, "service", this);
    }

    /** Shutdown service. */
    public void shutdown() {
        final String name = getName();
        if (isRunning() && isEnabled()) {
            logger.info(format("Shutting down '%s' starts.", name));
            doShutdown();
            logger.info(format("Shutting down '%s' ends.", name));
            status = ENABLED;
        }
    }

    /** Start service. */
    public final void start() {
        if (isRunning()) throw new IllegalStateException(format("Service %s already running.", getName()));

        final String name = getName();
        if (isEnabled()) {
            logger.info(format("Service '%s' starting...", name));
            try {
                doStart();
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
            logger.info(format("Service '%s' started.", name));
            status = RUNNING;
        }
        else logger.info(format("Service '%s' disable by properties", name));
    }

    @Override public void stop() {
        if (isRunning()) shutdown();
        else throw new IllegalStateException(format("Service %s already stop.", getName()));
    }

    /**  */
    public boolean isEnabled() {
        return serviceProps.enabled;
    }

    /** Verify service running. */
    public boolean isRunning() {
        return status == RUNNING;
    }

    /** Service Name. */
    public String getName() {
        return serviceName;
    }

    /** Return the service properties. */
    public ServiceProps getProperties() {
        return serviceProps;
    }

    /** Get Service Manager. */
    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    /** Return service status. */
    public ServiceStatus getStatus() {
        return status;
    }
    protected abstract void doShutdown();
    protected abstract void doStart()
        throws IOException;

    @NotNull protected String getCurrentMember() {
        return currentMemberName;
    }

    /** Returns the Environment. */
    protected Environment getEnv() {
        return serviceManager.getEnvironment();
    }

    private void enable(final boolean b) {
        if (b != serviceProps.enabled) {
            serviceProps.enabled = b;
            serviceManager.getEnvironment().put(deCapitalizeFirst(serviceName), serviceProps);
        }
        status = b ? ENABLED : DISABLED;
    }
    private int getStartLevel() {
        return serviceStartOrder;
    }

    //~ Methods ......................................................................................................................................

    /** Get the Specific Service... Remove this method once DI is implemented */
    @NotNull public static <T extends Service> Option<T> getService(final Class<T> serviceClass) {
        return Context.getSingleton(ServiceManager.class).getService(serviceClass);
    }

    //~ Static Fields ................................................................................................................................

    private static final String SINGLETON = "Singleton";
}  // end class Service
