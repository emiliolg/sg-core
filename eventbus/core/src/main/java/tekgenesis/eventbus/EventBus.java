
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.eventbus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.Environment;
import tekgenesis.common.logging.Logger;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.env.context.Context.getEnvironment;

/**
 * EventBus.
 */
public final class EventBus {

    //~ Constructors .................................................................................................................................

    private EventBus() {}

    //~ Methods ......................................................................................................................................

    /**
     * Publish events on a topic.
     *
     * @param  topic   The topic name
     * @param  object  The value object to publish
     */
    public static <T> void publish(@NotNull String topic, T object) {
        EventBus.<T>invoke((client) -> {
            try {
                client.publish(topic, object);
            }
            catch (final IOException e) {
                logger.error(e);
            }
        });
    }

    /**
     * Publish events on a topic.
     *
     * @param  topic   The topic name
     * @param  key     The key used to publish the object
     * @param  object  The value object to publish
     */
    public static <T> void publish(@NotNull String topic, String key, T object) {
        EventBus.<T>invoke((client) -> {
            try {
                client.publish(topic, key, object);
            }
            catch (final IOException e) {
                logger.error(e);
            }
        });
    }

    /** Shutdown all consumer and producers. */
    public static void shutdown() {
        clients.forEach((s, eventBusClient) -> {
            try {
                eventBusClient.close();
            }
            catch (final IOException e) {
                logger.error(e);
            }
        });
        clients.clear();
    }

    /**
     * Consume events from a topic.*
     *
     * @param  topic     the topic name
     * @param  consumer  a @Consumer to consumer the event
     * @param  class     The Event class
     */
    public static <T> void subscribe(@NotNull final String topic, Consumer<T> consume, Class<T> clazz) {
        EventBus.<T>invoke((client) -> client.subscribe(topic, consume, clazz));
    }

    private static <T> void invoke(Consumer<EventBusClient<T>> c) {
        final EventBusProps props = getEnvironment().get(EventBusProps.class);

        if (props.enabled && isNotEmpty(props.implementation)) {
            try {
                final EventBusClient<T> eventBusConsumer = getEventBusClient(props);
                c.accept(eventBusConsumer);
            }
            catch (final Throwable e) {
                logger.error("Unable to instantiate EventBusClient: " + props.implementation, e);
            }
        }
        else logger.warning("EventBus is disable");
    }

    private static <T> EventBusClient<T> getEventBusClient(EventBusProps props)
        throws Exception
    {
        EventBusClient<T> eventBusClient = cast(clients.get(props.implementation));
        if (eventBusClient == null) {
            final Class<EventBusClient<T>> eventBusConsumerClass = cast(Class.forName(props.implementation));
            if (eventBusConsumerClass == null) throw new IllegalStateException("Unable to found implementation:" + props.implementation);
            eventBusClient = eventBusConsumerClass.getConstructor(Environment.class).newInstance(getEnvironment());
            clients.put(props.implementation, eventBusClient);
        }
        return eventBusClient;
    }

    //~ Static Fields ................................................................................................................................

    private static final Map<String, EventBusClient<?>> clients = new HashMap<>();

    @NotNull private static final Logger logger = Logger.getLogger(EventBus.class);
}
