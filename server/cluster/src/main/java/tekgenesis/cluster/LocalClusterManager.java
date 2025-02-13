
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jgroups.Address;
import org.jgroups.stack.IpAddress;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Reflection;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.emptyList;

/**
 * LocalClusterManager.
 */
@SuppressWarnings("ExternalizableWithoutPublicNoArgConstructor")
public class LocalClusterManager extends BaseClusterManager<Address> {

    //~ Instance Fields ..............................................................................................................................

    private final Logger logger = Logger.getLogger(LocalClusterManager.class);

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("JavaDoc")
    public LocalClusterManager() {}

    //~ Methods ......................................................................................................................................

    @Nullable @Override public <K> K callRemoteMethod(Class<?> clazz, Address to, String methodName, Object[] args, Class<?>[] types, Object options)
        throws Exception
    {
        final org.jgroups.blocks.RpcDispatcher rpcDispatcher = dispatchers.get(clazz);
        final Object                           serverObject  = rpcDispatcher.getServerObject();
        return Reflection.invoke(serverObject, methodName, args);
    }

    @Override public <K> K callRemoteMethods(Class<?> clazz, List<Address> tos, String methodName, Object[] args, Class<?>[] types, Object options)
        throws Exception
    {
        return cast(emptyList());
    }

    @Override public void registerRpcDispatcher(final Class<?> type, final RpcDispatcher dispatcher) {
        final org.jgroups.blocks.RpcDispatcher rpcDispatcher = new org.jgroups.blocks.RpcDispatcher();
        rpcDispatcher.setServerObject(dispatcher.getObject());
        dispatchers.put(type, rpcDispatcher);
    }

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void sendMessage(int scope, Serializable object)
        throws Exception
    {
        final MessageHandler<Serializable> handler = cast(handlers.get((short) scope));

        if (handler == null) logger.error("Cannot found handler for message scope " + scope);
        else {
            try {
                handler.handle(object);
            }
            catch (final ClassCastException e) {
                logger.error(format("Mismatch message object class %s and handler class %s", object.getClass(), handler.getClass()));
            }
        }
    }  // end method sendMessage

    @Override public void start()
        throws Exception {}

    @Override public void stop() {}

    @Override public String getClusterName() {
        return "LocalCluster";
    }

    @Override public String getCurrentMember() {
        return IP_ADDRESS.toString();
    }

    @Override public String getCurrentMemberId() {
        return "n/a";
    }

    @Override public boolean isAlive(@NotNull String nodeName) {
        return true;
    }

    @Override public Address getMaster() {
        return getMember();
    }

    @Override public Address getMember() {
        return IP_ADDRESS;
    }

    @Override public String getMemberName() {
        return getCurrentMember();
    }

    @Override public String getMemberName(Address address) {
        return address.toString();
    }

    @Override public ImmutableList<Address> getMembersAddresses() {
        return ImmutableList.of(getMember());
    }

    @Override public String getMemberUUID() {
        return getCurrentMember();
    }

    @Nullable @Override public InetAddress getPhysicalAddress(final Address address) {
        try {
            return InetAddress.getLocalHost();
        }
        catch (final UnknownHostException e) {
            return null;
        }
    }

    @Override public boolean isMaster() {
        return true;
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("MagicNumber")
    public static final IpAddress IP_ADDRESS = new LocalIpAddress(9999);
}
