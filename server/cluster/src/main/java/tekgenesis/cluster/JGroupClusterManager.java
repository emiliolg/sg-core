
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
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jgroups.*;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.conf.ConfiguratorFactory;
import org.jgroups.conf.ProtocolConfiguration;
import org.jgroups.conf.ProtocolStackConfigurator;
import org.jgroups.stack.IpAddress;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.env.Environment;
import tekgenesis.common.logging.Logger;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.collections.Colls.immutable;

/**
 * Cluster Manager class.
 */
public class JGroupClusterManager extends BaseClusterManager<Address> {

    //~ Instance Fields ..............................................................................................................................

    private JChannel channel = null;

    private final Logger logger = Logger.getLogger(JGroupClusterManager.class);

    private final ClusterProps properties;
    private JChannel           rpcChannel = null;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public JGroupClusterManager(Environment env) {
        properties = env.get(ClusterProps.class);
    }

    //~ Methods ......................................................................................................................................

    public <K> K callRemoteMethod(Class<?> clazz, Address to, String methodName, Object[] args, Class<?>[] types, Object options)
        throws Exception
    {
        return getRpcDispatcher(clazz).callRemoteMethod(to, methodName, args, types, (RequestOptions) options);
    }

    @SuppressWarnings("unchecked")
    public <K> K callRemoteMethods(Class<?> clazz, List<Address> tos, String methodName, Object[] args, Class<?>[] types, Object options)
        throws Exception
    {
        return cast(getRpcDispatcher(clazz).callRemoteMethods(tos, methodName, args, types, (RequestOptions) options).getResults());
    }

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void receive(Message msg) {
        final short                        scope   = msg.getScope();
        final MessageHandler<Serializable> handler = cast(handlers.get(scope));

        if (handler == null) logger.debug("Cannot found handler for message scope " + scope);
        else {
            try {
                handler.handle((Serializable) msg.getObject());
            }
            catch (final ClassCastException e) {
                logger.error("Mismatch message object class " + msg.getObject().getClass() + " and handler class " + handler.getClass());
            }
        }
    }  // end method receive

    /** Register dispatcher. */
    public void registerRpcDispatcher(Class<?> clazz, tekgenesis.cluster.RpcDispatcher dispatcher) {
        if (rpcChannel == null) {
            try {
                rpcChannel = getChannel();
                rpcChannel.connect("rpc-" + properties.getClusterName());
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
        final RpcDispatcher rpcDispatcher = new RpcDispatcher(rpcChannel, dispatcher.getObject());
        dispatchers.put(clazz, rpcDispatcher);
        rpcDispatcher.start();
    }

    /** Send message for scope. */
    @Override public void sendMessage(int scope, Serializable object)
        throws Exception
    {
        final Message message = new Message(null, object);
        message.setScope((short) scope);
        channel.send(message);
    }

    /** Start the ClusterManager. */
    public void start()
        throws Exception
    {
        channel = getChannel();
        channel.connect("mgr-" + properties.getClusterName());
        channel.setReceiver(this);
    }

    /** Stop the ClusterManager. */
    public void stop() {
        if (channel != null) {
            channel.disconnect();
            channel.close();
            channel = null;
        }
        if (rpcChannel != null) {
            dispatchers.values().forEach(org.jgroups.blocks.RpcDispatcher::stop);
            rpcChannel.disconnect();
            rpcChannel.close();
            rpcChannel = null;
        }
    }

    /** Creates a JChannel. */
    public JChannel getChannel()
        throws Exception
    {
        final ProtocolStackConfigurator stackConfigurator = getProtocolStackConfigurator();
        return new JChannel(stackConfigurator);
    }

    /** Return cluster name.* */
    @Nullable public String getClusterName() {
        return channel == null ? null : channel.getClusterName();
    }

    /** Returns the current address member as String.* */
    @Nullable public String getCurrentMember() {
        if (rpcChannel != null && rpcChannel.isConnected()) return rpcChannel.getAddressAsString();
        logger.error(JGROUPS_DISCONNECTED);
        return null;
    }  // end method getCurrentMember

    /** Returns the current address member as String.* */
    public String getCurrentMemberId() {
        final String currentMember = getCurrentMember();
        if (currentMember == null) return "n/a";

        final int sep = currentMember.lastIndexOf("-");
        return sep > 0 ? currentMember.substring(sep + 1) : currentMember;
    }  // end method getCurrentMemberId

    @Override public boolean isAlive(@NotNull String nodeName) {
        return !getMembersAddressNames().filter(nodeName::equals).isEmpty();
    }

    /** Returns the master address member as String.* */
    @Nullable public Address getMaster() {
        if (rpcChannel == null || !rpcChannel.isConnected()) {
            // noinspection DuplicateStringLiteralInspection
            logger.error(JGROUPS_DISCONNECTED);
            return null;
        }

        final View view = rpcChannel.getView();
        if (view == null) {
            logger.error("JGroups view no available");
            return null;
        }

        return view.getCreator();
    }  // end method getMaster

    /** Return member UUID. */
    public Address getMember() {
        return rpcChannel.getAddress();
    }

    public String getMemberName() {
        return rpcChannel.getAddress().toString();
    }

    public String getMemberName(Address address) {
        return address.toString();
    }

    @Override public ImmutableList<Address> getMembersAddresses() {
        if (rpcChannel == null) return emptyList();
        final View view = rpcChannel.getView();
        if (view == null) return emptyList();
        return immutable(view.getMembers());
    }

    /** Return member UUID. */
    public String getMemberUUID() {
        return rpcChannel.getAddressAsUUID();
    }

    /** Que physical Address for Member Address.* */
    @Nullable public InetAddress getPhysicalAddress(Address address) {
        final PhysicalAddress physicalAddr = (PhysicalAddress) rpcChannel.down(new Event(Event.GET_PHYSICAL_ADDRESS, address));
        return physicalAddr == null ? null : ((IpAddress) physicalAddr).getIpAddress();
    }

    /** Returns true if the current member is Master.* */
    public boolean isMaster() {
        return rpcChannel != null && getMember().equals(getMaster());
    }

    /** Get dispatcher for object. */
    public RpcDispatcher getRpcDispatcher(Class<?> objectClass) {
        return dispatchers.get(objectClass);
    }

    private void putProperty(ProtocolStackConfigurator stackConfigurator, String protocol, String key, String value) {
        for (final ProtocolConfiguration protocolConfiguration : stackConfigurator.getProtocolStack()) {
            if (protocol.equalsIgnoreCase(protocolConfiguration.getProtocolName())) protocolConfiguration.getProperties().put(key, value);
        }
    }

    private ProtocolStackConfigurator getProtocolStackConfigurator()
        throws Exception
    {
        final ProtocolStackConfigurator stackConfigurator;
        if (!properties.props.isEmpty()) stackConfigurator = ConfiguratorFactory.getStackConfigurator(properties.props);
        else {
            if (properties.isLocal()) stackConfigurator = ConfiguratorFactory.getStackConfigurator("fast.xml");
            else {
                if (isNotEmpty(properties.members)) {
                    stackConfigurator = ConfiguratorFactory.getStackConfigurator("tcp.xml");
                    putProperty(stackConfigurator, TCPPING, "initial_hosts", properties.members);
                    putProperty(stackConfigurator, "TCP", BIND_PORT, String.valueOf(properties.tcpBindPort));
                }
                else if (isNotEmpty(properties.awsBucket)) {
                    stackConfigurator = ConfiguratorFactory.getStackConfigurator("awsping.xml");
                    putProperty(stackConfigurator, "TCP", BIND_PORT, String.valueOf(properties.tcpBindPort));
                    if (!isEmpty(properties.awsTagName))
                        putProperty(stackConfigurator, Class.forName(AWSPING).getName(), "tagName", properties.awsTagName);
                    putProperty(stackConfigurator, Class.forName(AWSPING).getName(), "port", String.valueOf(properties.tcpBindPort));
                }
                else stackConfigurator = ConfiguratorFactory.getStackConfigurator("udp.xml");
            }
        }
        stackConfigurator.getProtocolStack().get(0).getProperties().put("singleton_name", "suigeneris_singleton");
        return stackConfigurator;
    }

    //~ Static Fields ................................................................................................................................

    private static final String AWSPING = "tekgenesis.cluster.aws.AWSPING";

    private static final String BIND_PORT = "bind_port";

    private static final String TCPPING = "TCPPING";

    public static final String JGROUPS_DISCONNECTED = "JGroups disconnected";
}
