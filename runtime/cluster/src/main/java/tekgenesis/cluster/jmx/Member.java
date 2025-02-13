
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.infinispan.Cache;
import org.infinispan.commons.util.CloseableIteratorSet;
import org.jetbrains.annotations.NotNull;
import org.jgroups.Address;

import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cluster.ServerProps;
import tekgenesis.cluster.jmx.notification.Operation;
import tekgenesis.cluster.jmx.util.JsonDataSerializer;
import tekgenesis.cluster.jmx.util.MemoryUsageInfo;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.Version;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.impl.BaseEnvironment;
import tekgenesis.common.env.impl.PropertiesEnvironment;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxInvokerImpl;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Conversions;
import tekgenesis.common.util.Reflection;
import tekgenesis.service.Service;
import tekgenesis.service.ServiceManager;
import tekgenesis.service.ServiceStatus;
import tekgenesis.sg.NodeEntry;
import tekgenesis.task.TaskStatus;
import tekgenesis.task.jmx.JmxConstants;

import static tekgenesis.cluster.jmx.notification.Notifier.call;
import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Constants.SHIRO_SESSION_CACHE;
import static tekgenesis.common.env.context.Context.getSingleton;
import static tekgenesis.common.json.JsonMapping.shared;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.sg.MemberStatus.READY;
import static tekgenesis.sg.MemberStatus.SAFE_MODE;
import static tekgenesis.sg.g.TaskEntryTable.TASK_ENTRY;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Cluster's Node.
 */
public class Member implements MemberMBean {

    //~ Instance Fields ..............................................................................................................................

    private final Address     address;
    private final JmxEndpoint conn;

    //~ Constructors .................................................................................................................................

    /**  */
    public Member(Address a, JmxEndpoint jmx) {
        address = a;
        conn    = jmx;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean areTaskRunning() {
        return invokeInTransaction(() ->
                selectFrom(TASK_ENTRY).where(TASK_ENTRY.MEMBER.eq(getName()).and(TASK_ENTRY.STATUS.eq(TaskStatus.RUNNING)))
                                      .count() > 0);
    }

    public List<Tuple<String, String>> dumpCacheContent(@NotNull String cacheName) {
        final InfinispanCacheManager                          manager = getSingleton(InfinispanCacheManager.class);
        final Cache<Object, Object>                           cache   = manager.getCache(cacheName);
        final CloseableIteratorSet<Map.Entry<Object, Object>> entries = cache.entrySet();
        return entries.stream().map(entry -> {
                try {
                    final String key   = shared().writeValueAsString(entry.getKey());
                    final Object v     = entry.getValue();
                    final String value = JsonDataSerializer.json(v);

                    return Tuple.tuple(key, value);
                }
                catch (final JsonProcessingException e) {
                    logger.error("Unable to serialize cache item", e);
                }

                return Tuple.tuple("", "");
            }).collect(Collectors.toList());
    }

    @Override public void restart() {
        JmxInvokerImpl.invoker(conn).mbean(JmxConstants.JETTY_NODE).invoke("stop", null, null);
    }

    @Override public void safeMode(boolean enabled) {
        final ServerProps serverProps = Context.getEnvironment().get(ServerProps.class);
        serverProps.safeMode = enabled;
        Context.getEnvironment().put(serverProps);
        final ServiceManager serviceManager = getSingleton(ServiceManager.class);

        // Un Register WebProxyService

        final Option<Service> webProxyService = serviceManager.getServices().getFirst(s -> s != null && "WebProxyService".equals(s.getName()));
        if (webProxyService.isEmpty()) throw new IllegalStateException("Unable to find WebProxyService");

        final Service service = webProxyService.get();

        if (service.isRunning()) service.stop();
        service.start();

        runInTransaction(() -> NodeEntry.create(ensureNotNull(conn.getAddress()).toString()).setStatus(enabled ? SAFE_MODE : READY).persist());
    }

    @Override public void update(int buildNumber, String branchName, boolean healthCheck) {
        throw new UnsupportedOperationException("Implement it !");
    }

    @Override public <T> void updateProperty(String scope, Class<T> clazz, String fieldName, String value) {
        final Environment environment = Context.getEnvironment();

        final T        t     = environment.get(scope, clazz);
        final Field    field = Reflection.findField(clazz, fieldName).getOrFail("Unable to found fieldName");
        final Class<?> type  = field.getType();
        Reflection.setFieldValue(t, field, Conversions.fromString(value, type));
        environment.put(scope, t);
    }

    @Override public Address getAddress() {
        return address;
    }

    @Override public List<Version.ComponentInfo> getComponents() {
        final ImmutableList<Version.ComponentInfo> defaultValue = Colls.emptyList();
        final List<Version.ComponentInfo>          call         = call(getAddress(), Operation.COMPONENT_INFO, defaultValue);
        return call == null ? defaultValue : call;
    }

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Metric getMetrics() {
        final Metric metrics = new Metric(DateTime.current());
        metrics.addValue("cpuUsage", call(getAddress(), Operation.CPU_USAGE, 0d));
        final MemoryUsageInfo call = call(getAddress(), Operation.MEMORY_USAGE, new MemoryUsageInfo());
        metrics.addValue("memoryUsage", call.getDetails());
        final InfinispanCacheManager manager = getSingleton(InfinispanCacheManager.class);
        metrics.addValue("userLogged", getUserLoggedCount(manager));
        metrics.addValue("sessionCount", getSessionCount(manager));
        metrics.addValue("uptime", call(address, Operation.UPTIME, 0L));
        return metrics;
    }

    @Override public String getName() {
        return address.toString();
    }

    /** Returns available properties. */
    @Override public Map<String, String> getProperties() {
        final Environment environment = Context.getEnvironment();

        final Map<String, String> map = new HashMap<>();

        if (environment instanceof PropertiesEnvironment) {
            // noinspection DuplicateStringLiteralInspection
            final Map<String, BaseEnvironment.Entry<?>> values = Reflection.getPrivateField(environment, "values");

            if (values != null) {
                for (final String key : values.keySet()) {
                    final BaseEnvironment.Entry<?> entry     = values.get(key);
                    final Object                   propValue = entry.getValue();
                    if (propValue != null) {
                        final Set<Field> publicFields = Reflection.getPublicFields(entry.getClazz());

                        publicFields.forEach(f -> {
                            final Object fieldValue = Reflection.getFieldValue(propValue, f);
                            String       v          = null;
                            if (fieldValue != null) v = fieldValue.toString();
                            map.put(key + "." + f.getName(), v);
                        });
                    }
                }
            }
        }
        return map;
    }

    @Override public Map<String, ServiceStatus> getServicesStatus() {
        return call(address, Operation.SERVICES, new HashMap<>());
    }

    @Override public NodeStatus getStatus() {
        final InetAddress inetAddress = conn.getAddress();
        ensureNotNull(inetAddress);
        final NodeEntry nodeEntry = invokeInTransaction(() -> NodeEntry.find(inetAddress.toString()));

        ensureNotNull(nodeEntry);
        return NodeStatus.create(nodeEntry);
    }

    @Override public long getUptime() {
        return call(address, Operation.UPTIME, 0L);
    }

    private int getSessionCount(final InfinispanCacheManager cacheManager) {  // noinspection DuplicateStringLiteralInspection
        return cacheManager.getCache(SHIRO_SESSION_CACHE).size();
    }

    private int getUserLoggedCount(final InfinispanCacheManager cacheManager) {
        int total = 0;

        // noinspection DuplicateStringLiteralInspection
        final Cache<String, Object> map = cacheManager.getCache(SHIRO_SESSION_CACHE);
        if (map != null && !map.isEmpty()) {
            final Set<Map.Entry<String, Object>> entries = map.entrySet();

            for (final Map.Entry<String, Object> entry : entries) {
                final Object session = entry.getValue();
                if (session != null) {
                    final Map<Object, Object> attributes = Reflection.invoke(session, "getAttributes");
                    if (attributes != null) {
                        // noinspection DuplicateStringLiteralInspection
                        final String sessionUser = (String) attributes.get("userId");
                        if (!isEmpty(sessionUser)) total++;
                    }
                }
            }
        }
        return total;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger           = Logger.getLogger(Member.class);
    private static final long   serialVersionUID = -1394034113916056506L;
}  // end class Member
