
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.util.*;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jgroups.Address;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.common.collections.ImmutableSet;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.Environment;

import static tekgenesis.common.collections.Colls.*;
import static tekgenesis.common.core.Option.option;

/**
 * ServicesManager.
 */
public final class ServiceManager {

    //~ Instance Fields ..............................................................................................................................

    private final Environment                            env;
    private final SortedSet<Service>                     services;
    private final Map<Class<? extends Service>, Service> servicesByClazz;
    private final Map<String, Service>                   servicesByName;

    //~ Constructors .................................................................................................................................

    /**  */
    public ServiceManager(Environment e) {
        env             = e;
        servicesByName  = new TreeMap<>();
        services        = new TreeSet<>();
        servicesByClazz = new HashMap<>();
    }

    //~ Methods ......................................................................................................................................

    /** Clean services. */
    public void clean() {
        services.clear();
        servicesByName.clear();
        servicesByClazz.clear();
    }

    /** Execute on service. */
    public <T extends Service> void onService(Class<T> clazz, Consumer<T> c) {
        filter(services, clazz).forEach(c);
    }

    /** Register a service. */
    public void register(Service s) {
        services.add(s);
        servicesByName.put(s.getName(), s);
        servicesByClazz.put(s.getClass(), s);
    }

    /** Stop all registered service. */
    public void shutdown() {
        final ListIterator<Service> li = toList(services).listIterator(services.size());

        while (li.hasPrevious())
            li.previous().shutdown();
    }

    /** Start all registered services. */
    public void start(ClusterManager<Address> clusterManager) {
        for (final Service service : services) {
            service.init(clusterManager);
            service.start();
        }
    }

    /**  */
    public void start(String serviceName) {
        final Service s = getService(serviceName);
        if (s != null) s.start();
    }

    /** start All services. */
    public void startAll() {
        services.forEach(Service::start);
    }

    /**  */
    public void stop(String serviceName) {
        final Service s = getService(serviceName);
        if (s != null) s.stop();
    }

    /** Stop all services. */
    public void stopAll(@Nullable Consumer<Service> listen) {
        for (final Service service : services) {
            service.shutdown();
            if (listen != null) listen.accept(service);
        }
    }

    /** Returns the environment. */
    public Environment getEnvironment() {
        return env;
    }

    /** Get Service. */
    @Nullable public Service getService(String name) {
        return servicesByName.get(name);
    }

    /** Get Service. */
    @NotNull public <T extends Service> Option<T> getService(Class<T> clazz) {
        return option(servicesByClazz.get(clazz)).castTo(clazz);
    }

    /**  */
    public ImmutableSet<Service> getServices() {
        return immutable(services);
    }

    /** Return services and their status. */
    public Map<String, ServiceStatus> getServicesStatus() {
        final Map<String, ServiceStatus> list = new TreeMap<>();
        for (final Service service : services)
            list.put(service.getName(), service.getStatus());
        return list;
    }
}  // end class ServiceManager
