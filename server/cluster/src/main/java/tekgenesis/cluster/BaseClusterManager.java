
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster;

import java.util.HashMap;

import org.jgroups.ReceiverAdapter;
import org.jgroups.blocks.RpcDispatcher;

import tekgenesis.common.logging.Logger;

/**
 * BaseClusterManager.
 */
public abstract class BaseClusterManager<T> extends ReceiverAdapter implements ClusterManager<T> {

    //~ Instance Fields ..............................................................................................................................

    protected final HashMap<Class<?>, RpcDispatcher>  dispatchers = new HashMap<>();
    protected final HashMap<Short, MessageHandler<?>> handlers    = new HashMap<>();

    private final Logger logger = Logger.getLogger(BaseClusterManager.class);

    //~ Methods ......................................................................................................................................

    @Override public void deRegisterMessageHandler(int scope) {
        handlers.remove((short) scope);
    }

    @Override public void registerMessageHandler(MessageHandler<?> handler) {
        final Short             scope          = handler.getScope();
        final MessageHandler<?> messageHandler = handlers.get(scope);
        if (messageHandler == null) handlers.put(scope, handler);
        else logger.error("Registering multiple handler for scope " + scope);
    }
}
