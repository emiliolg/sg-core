
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx.notification;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jgroups.Address;
import org.jgroups.blocks.RequestOptions;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.jmx.util.ClusterInfo;
import tekgenesis.cluster.jmx.util.MemberInfo;
import tekgenesis.common.core.Times;
import tekgenesis.common.jmx.JmxEndpoint;
import tekgenesis.common.jmx.JmxOperation;
import tekgenesis.common.logging.Logger;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.env.context.Context.getContext;

/**
 * Send Events or Task to be executed by all the members.
 */
@SuppressWarnings("WeakerAccess")
public final class Notifier {

    //~ Constructors .................................................................................................................................

    private Notifier() {}

    //~ Methods ......................................................................................................................................

    /**
     * Send broadcast jmx operation.
     *
     * @param  operation  jmxoperation
     */
    public static void broadcast(@NotNull JmxOperation operation) {
        final List<MemberInfo> members = ClusterInfo.getInstance().getMembers();

        for (final MemberInfo member : members) {
            final JmxEndpoint jmxConfiguration = member.getJmxEndpoint();
            operation.setEndpoint(jmxConfiguration);
            try {
                operation.execute();
            }
            catch (final Exception e) {
                logger.warning(e);
            }
        }
    }

    /**
     * Send a broadcast msg.
     *
     * @param  callableTask  The callback task
     */
    public static void broadcastCall(@NotNull Operation callableTask, Object... args)
        throws Exception
    {
        final ClusterManager<Address> clusterManager = cast(getContext().getSingleton(ClusterManager.class));
        final List<Address>           members        = clusterManager.getMembersAddresses();
        remoteExecute(callableTask, members, args);
    }

    /** .* */
    public static <T> T call(Address address, @NotNull Operation callableTask, T defaultValue, Object... args) {
        try {
            return remoteExecute(callableTask, address, args);
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }

    /** .* */
    public static <T> T callWith(Address address, @NotNull Operation callableTask, Object... args)
        throws Exception
    {
        return remoteExecute(callableTask, address, args);
    }

    private static <T> List<T> remoteExecute(@NotNull Operation callable, List<Address> addresses, Object[] args)
        throws Exception
    {
        final ClusterManager<Address> clusterManager = cast(getContext().getSingleton(ClusterManager.class));
        final RequestOptions          requestOptions = RequestOptions.SYNC();

        final Class<?>[] classTypes = getTypes(args);

        return clusterManager.callRemoteMethods(RemoteApp.class, addresses, callable.getMethodName(), args, classTypes, requestOptions);
    }

    private static <T> T remoteExecute(@NotNull Operation callable, Address address, Object[] args)
        throws Exception
    {
        final ClusterManager<Address> clusterManager = cast(getContext().getSingleton(ClusterManager.class));

        final RequestOptions requestOptions = RequestOptions.SYNC();
        requestOptions.setTimeout(Times.MILLIS_MINUTE);
        final Class<?>[] classTypes = getTypes(args);

        return clusterManager.callRemoteMethod(RemoteApp.class, address, callable.getMethodName(), args, classTypes, requestOptions);
    }

    @Nullable private static Class<?>[] getTypes(Object[] args) {
        Class<?>[] classTypes = null;
        if (isNotEmpty(args)) {
            classTypes = new Class<?>[args.length];
            for (int i = 0, argsLength = args.length; i < argsLength; i++)
                classTypes[i] = args[i].getClass();
        }
        return classTypes;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(Notifier.class);
}  // end class BroadcastNotifier
