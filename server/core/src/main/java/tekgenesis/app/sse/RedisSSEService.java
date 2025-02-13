
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app.sse;

import java.net.URI;
import java.util.function.Consumer;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.jetbrains.annotations.NotNull;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

/**
 * Redis Notification Service.
 */
public class RedisSSEService implements SSEService<String> {

    //~ Instance Fields ..............................................................................................................................

    private final JedisPool jedisPool;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public RedisSSEService(SSEProps props) {
        final GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(1000);
        jedisPool = new JedisPool(config, URI.create(props.host));
    }

    //~ Methods ......................................................................................................................................

    @Override public void publish(@NotNull String channel, @NotNull String msg) {
        jedisPool.getResource().publish(channel, msg);
    }

    @Override public void subscribe(@NotNull String chn, final Consumer<String> consumer) {
        jedisPool.getResource().subscribe(new JedisPubSub() {
                @Override public void onMessage(String channel, String message) {
                    super.onMessage(channel, message);
                    consumer.accept(message);
                }

                @Override public void onSubscribe(String channel, int subscribedChannels) {}

                @Override public void onUnsubscribe(String channel, int subscribedChannels) {}

                @Override public void onPMessage(String pattern, String channel, String message) {}

                @Override public void onPUnsubscribe(String pattern, int subscribedChannels) {}

                @Override public void onPSubscribe(String pattern, int subscribedChannels) {}
            }, chn);
    }
}  // end class RedisSSEService
