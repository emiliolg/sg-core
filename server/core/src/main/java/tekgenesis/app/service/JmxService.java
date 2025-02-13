
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app.service;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.RemoteException;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.infinispan.Cache;

import tekgenesis.app.properties.JmxServiceProps;
import tekgenesis.cache.CacheType;
import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cluster.jmx.Caches;
import tekgenesis.cluster.jmx.Cluster;
import tekgenesis.cluster.jmx.Entities;
import tekgenesis.cluster.jmx.Member;
import tekgenesis.cluster.jmx.Memos;
import tekgenesis.cluster.jmx.util.ClusterInfo;
import tekgenesis.common.core.Constants;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.jmx.JmxHelper;
import tekgenesis.service.Service;
import tekgenesis.service.ServiceManager;
import tekgenesis.task.jmx.EndpointResolver;
import tekgenesis.task.jmx.JmxConstants;

import static java.rmi.registry.LocateRegistry.createRegistry;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Constants.JMX_INSTANCE_MAP;
import static tekgenesis.common.core.Constants.SG_SERVICENAME;
import static tekgenesis.common.env.context.Context.getContext;
import static tekgenesis.task.jmx.JmxConstants.RUNTIME;

/**
 * JmxService.
 */
public class JmxService extends Service {

    //~ Instance Fields ..............................................................................................................................

    private JMXConnectorServer jmxConnectorServer = null;

    //~ Constructors .................................................................................................................................

    /** Create a JMX Service. */
    public JmxService(final ServiceManager serviceManager) {
        super(serviceManager, SERVICE_NAME, SERVICE_START_ORDER, JmxServiceProps.class);
    }

    //~ Methods ......................................................................................................................................

    @Override public final JmxServiceProps getProperties() {
        return (JmxServiceProps) super.getProperties();
    }

    @Override protected void doShutdown() {
        if (jmxConnectorServer != null) {
            try {
                // noinspection NestedTryStatement
                try {
                    Memos.forceCluster(ClusterInfo.ClusterInfoMemo.class.getName());
                }
                catch (final Exception ignore) {}
                jmxConnectorServer.stop();
            }
            catch (final IOException e) {
                logger.warning("IO Exception trying to stop JMX Connector Server", e);
            }
        }
    }

    @Override protected void doStart()
        throws IOException
    {
        final String jmxEnableByProp = System.getProperty(JMX_REMOTE);

        if (isNotEmpty(jmxEnableByProp)) {
            final int jmxPort = Integer.valueOf(System.getProperty("com.sun.management.jmxremote.port"));
            registerJmxNode(jmxPort);
        }
        else {
            System.setProperty(JMX_REMOTE, "");
            System.setProperty("com.sun.management.jmxremote.ssl", String.valueOf(false));
            System.setProperty("com.sun.management.jmxremote.authenticate", String.valueOf(false));

            final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            try {
                final int           jmxPort = bindJmxPort();
                final JMXServiceURL url     = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + jmxPort + "/" + SG_SERVICENAME);

                jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mBeanServer);
                jmxConnectorServer.start();
                getContext().setSingleton(MBeanServer.class, mBeanServer);
                registerJmxNode(jmxPort);
            }
            catch (final IOException e) {
                logger.error("IO Exception trying to start jmx service. Jmx is not enabled", e);
                throw e;
            }
        }

        registerAppMBeans();
        Memos.forceCluster(ClusterInfo.ClusterInfoMemo.class.getName());
    }

    private int bindJmxPort()
        throws RemoteException
    {
        int jmxPort = getProperties().port;

        if (jmxPort < JmxConstants.DEFAULT_PORT) throw new IllegalStateException("JMX port number not allowed. Bellow " + JmxConstants.DEFAULT_PORT);
        try {
            createRegistry(jmxPort);
        }
        catch (final RemoteException e) {
            // noinspection MagicNumber
            jmxPort = Integer.parseInt((String) System.getProperties().get(Constants.PORT_OPT)) + 1500;
            createRegistry(jmxPort);
            final JmxServiceProps properties = getProperties();
            properties.port = jmxPort;
            getEnv().put(properties);
        }

        return jmxPort;
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private void registerAppMBeans() {
        // Cluster
        final Cluster cluster = new Cluster(clusterManager);
        JmxHelper.registerMBean("Cluster", JmxHelper.CLUSTER_DOMAIN, RUNTIME, cluster);

        // Node
        final Member member = new Member(clusterManager.getMember(), EndpointResolver.resolve(clusterManager.getMember()));
        JmxHelper.registerMBean("Node", JmxHelper.CLUSTER_DOMAIN, RUNTIME, member);

        // Services
        getServiceManager().getServices().forEach(Service::registerMBean);

        // Cache
        final Caches cache = new Caches();
        JmxHelper.registerMBean("Caches", JmxHelper.CLUSTER_DOMAIN, RUNTIME, cache);

        // Memos
        final Memos memos = new Memos();
        // noinspection DuplicateStringLiteralInspection
        JmxHelper.registerMBean("Memos", JmxHelper.CLUSTER_DOMAIN, RUNTIME, memos);

        // Entities
        final Entities entities = new Entities();
        // noinspection DuplicateStringLiteralInspection
        JmxHelper.registerMBean("Entities", JmxHelper.CLUSTER_DOMAIN, RUNTIME, entities);
    }

    private void registerJmxNode(int jmxPort) {
        final InfinispanCacheManager cacheManager = Context.getContext().getSingleton(InfinispanCacheManager.class);
        final Cache<Object, Integer> cache        = cacheManager.getCache(JMX_INSTANCE_MAP, CacheType.FULL.withSyncReplication());
        cache.put(clusterManager.getMember(), jmxPort);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1455426808156745986L;

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String JMX_REMOTE = "com.sun.management.jmxremote";

    public static final String SERVICE_NAME        = JmxService.class.getSimpleName();
    private static final int   SERVICE_START_ORDER = 2;
}  // end class JmxService
