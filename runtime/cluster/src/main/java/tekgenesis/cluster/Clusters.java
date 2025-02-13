
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jgroups.Address;

import tekgenesis.cluster.jmx.RemoteCluster;
import tekgenesis.cluster.jmx.RemoteMember;
import tekgenesis.cluster.jmx.service.RemoteService;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.sg.ClusterConf;
import tekgenesis.task.jmx.EndpointResolver;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Constants.LOCALHOST;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.sg.ClusterStatus.ACTIVE;
import static tekgenesis.sg.g.ClusterConfTable.CLUSTER_CONF;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * Cluster Util.
 */
public final class Clusters {

    //~ Constructors .................................................................................................................................

    /** Default Constructor. */
    public Clusters() {}

    //~ Methods ......................................................................................................................................

    /**  */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Optional<RemoteMember> findMember(String name) {
        final Option<RemoteCluster> activeCluster = getActiveCluster();

        if (activeCluster.isEmpty()) throw new IllegalStateException("No cluster activated");

        return activeCluster.get().getMembers().stream().filter((f) -> name.equals(f.getName())).findFirst();
    }

    /** Get Active cluster m bean. */
    public Option<RemoteCluster> getActiveCluster() {
        return invokeInTransaction(this::getCluster);
    }

    /**  */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public RemoteMember getMemberAt(int idx) {
        final Option<RemoteCluster> activeCluster = getActiveCluster();

        if (activeCluster.isEmpty()) throw new IllegalStateException("No cluster activated");

        return activeCluster.get().getMembers().get(idx);
    }

    /**  */
    @Nullable public ClusterConf getSelectedClusterConf() {
        final ClusterConf clusterDef = selectFrom(CLUSTER_CONF).where(CLUSTER_CONF.STATUS.eq(ACTIVE)).cache(60).get();
        if (clusterDef == null) return setLocalClusterConf();
        return clusterDef;
    }

    /** returns an Instance Service enabled in some node. null if the service is not enabled */
    public <T extends RemoteService> Option<T> getService(@NotNull Class<T> impl, @NotNull String serviceName) {
        final Option<RemoteCluster> activeCluster = getActiveCluster();
        final List<RemoteMember>    remoteMembers = activeCluster.get().getMembers();

        for (final RemoteMember remoteMember : remoteMembers) {
            final T service = remoteMember.getService(impl, serviceName);
            if (service.isEnabled()) return Option.some(service);
        }

        return Option.empty();
    }

    private ClusterConf createDefaultLocal() {
        return invokeInTransaction(() -> {
            final ClusterConf local = ClusterConf.create(LOCALHOST_NO_CLUSTER).setStatus(ACTIVE);
            local.getEntryPoints().add().setAddress(LOCALHOST);
            return local.persist();
        });
    }

    /**
     * Get Cluster M Bean Object. Commit transaction if there is no cluster defined with the
     * localhost cluster created.
     */
    private Option<RemoteCluster> getCluster() {
        final ClusterManager<Address> clusterManager = cast(Context.getSingleton(ClusterManager.class));
        return Option.some(new RemoteCluster(EndpointResolver.resolve(clusterManager.getMember())));
    }  // end method getCluster

    @Nullable private ClusterConf setLocalClusterConf() {
        if (selectFrom(CLUSTER_CONF).count() == 0) return createDefaultLocal();
        else {
            // Set localhost as ACTIVE
            final ClusterConf local = selectFrom(CLUSTER_CONF).where(CLUSTER_CONF.NAME.eq(LOCALHOST_NO_CLUSTER)).get();
            if (local == null) createDefaultLocal();
            else {
                local.setStatus(ACTIVE);
                local.persist();
            }
            return local;
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final String LOCALHOST_NO_CLUSTER = "localhost-noCluster";
}
