
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.eventbus;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

/**
 * EventBus Abstract.
 */
public abstract class EventBusClient<T> implements Closeable {

    //~ Instance Fields ..............................................................................................................................

    protected boolean consume = true;

    //~ Methods ......................................................................................................................................

    @Override public void close()
        throws IOException
    {
        consume = false;
    }

    /** Publish events on a topic It will use as a key a random String value. */
    public abstract void publish(@NotNull String topic, @NotNull T event)
        throws IOException;

    /** Publish events on a topic. */
    public abstract void publish(String topic, @NotNull String key, @NotNull T event)
        throws IOException;

    /**
     * Consume events from a topic.*
     *
     * @param  topic     the topic name
     * @param  consumer  a @Consumer to consumer the event
     * @param  class     The Event class
     */
    public abstract void subscribe(String topic, Consumer<T> consumer, Class<T> clazz);
}
