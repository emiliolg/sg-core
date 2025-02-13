
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;
import org.jgroups.Address;

import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.ClusterProps;
import tekgenesis.cluster.jmx.notification.Notifier;
import tekgenesis.cluster.jmx.notification.Operation;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Memo;
import tekgenesis.common.util.SingletonMemo;
import tekgenesis.task.jmx.EndpointResolver;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.env.context.Context.getContext;
import static tekgenesis.common.env.context.Context.getSingleton;
import static tekgenesis.common.logging.Logger.getLogger;

/**
 * Cluster summary.
 */
public final class ClusterInfo {

    //~ Instance Fields ..............................................................................................................................

    private final InfinispanCacheManager cacheManager;

    private final ClusterManager<Address> clusterManager;
    private final String                  clusterName;

    //~ Constructors .................................................................................................................................

    private ClusterInfo() {
        clusterManager = cast(getContext().getSingleton(ClusterManager.class));
        cacheManager   = getSingleton(InfinispanCacheManager.class);
        clusterName    = Context.getEnvironment().get(ClusterProps.class).clusterName;
    }

    //~ Methods ......................................................................................................................................

    /** Find member.* */
    public Option<MemberInfo> findMember(final String name) {
        Option<MemberInfo>     member  = Option.empty();
        final List<MemberInfo> members = getMembers();
        for (final MemberInfo memberInfo : members) {
            if (memberInfo.getName().equals(name)) {
                member = Option.option(memberInfo);
                break;
            }
        }
        return member;
    }

    /** Refresh cluster info. */
    public ClusterInfo refresh() {
        memo.force();
        return this;
    }

    /** @return  MemorySummary */
    public MemorySummary getHeapMemory() {
        return memo.getHeapMemory();
    }

    /** get JmxConfiguration. */
    public JmxEndpoint getJmxConfiguration(@NotNull Address address) {
        return EndpointResolver.resolve(address);
    }

    /** Get Local Member.* */
    public MemberInfo getLocal() {
        return new MemberInfo(cacheManager, clusterManager, clusterManager.getMember());
    }

    /** returns master memberInfo.* */
    public Option<MemberInfo> getMaster() {
        final List<MemberInfo> members = getMembers();
        for (final MemberInfo member : members) {
            if (member.isMaster()) return Option.some(member);
        }
        return Option.empty();
    }

    /** @return  The List of MemberInfo */
    public List<MemberInfo> getMembers() {
        return memo.getMembers();
    }

    /** Cluster Name. */
    public String getName() {
        return clusterName;
    }

    //~ Methods ......................................................................................................................................

    /** @return  Create an instance of ClusterInfo */
    public static ClusterInfo getInstance() {
        return INSTANCE;
    }

    //~ Static Fields ................................................................................................................................

    private static final ClusterInfoMemo memo     = Memo.getInstance(ClusterInfoMemo.class);
    private static final ClusterInfo     INSTANCE = new ClusterInfo();

    private static final Logger logger = getLogger(ClusterInfo.class);

    //~ Inner Classes ................................................................................................................................

    public static class ClusterInfoMemo extends SingletonMemo<ClusterInfoMemo, ClusterInfoMemo> {
        private MemorySummary heapMemory = null;

        private List<MemberInfo> memberInfoList = null;

        /** Create SingletonMemo. */
        ClusterInfoMemo() {
            super(5, TimeUnit.MINUTES);
        }

        @Override protected ClusterInfoMemo calculate(long lastRefreshTime, ClusterInfoMemo oldValue) {
            return new ClusterInfoMemo().init();
        }

        MemorySummary getHeapMemory() {
            return get().heapMemory;
        }

        List<MemberInfo> getMembers() {
            return get().memberInfoList;
        }

        private ClusterInfoMemo init() {
            final ClusterManager<Address> manager  = cast(getContext().getSingleton(ClusterManager.class));
            final InfinispanCacheManager  cacheMgr = getSingleton(InfinispanCacheManager.class);
            memberInfoList = new ArrayList<>();

            final List<Address> members = manager.getMembersAddresses();

            Long heapAvailableMemory = 0L;
            Long heapUsedMemory      = 0L;
            for (final Address member : members) {
                final MemoryUsageInfo memoryUsageInfo = Notifier.call(member, Operation.MEMORY_USAGE, new MemoryUsageInfo());
                final MemoryInfo      oldGen          = memoryUsageInfo.getOldGen();

                heapAvailableMemory += oldGen.getMax();
                heapUsedMemory      += oldGen.getUsed();

                final MemberInfo memberInfo = new MemberInfo(cacheMgr, manager, member);
                memberInfoList.add(memberInfo);
            }

            heapMemory = new MemorySummary(heapAvailableMemory, heapUsedMemory);
            return this;
        }  // end method init
    }  // end class ClusterInfoMemo
}  // end class ClusterInfo
