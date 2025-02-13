
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.aws.eventbus;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.command.AbstractCommand;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.env.Environment;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.logging.Logger;
import tekgenesis.eventbus.EventBusClient;

/**
 * Kinesis EventBus Consumer.
 */
public class KinesisEventBusClient<T> extends EventBusClient<T> {

    //~ Instance Fields ..............................................................................................................................

    Kinesis       kinesis       = null;
    AmazonKinesis kinesisClient = null;

    private final KinesisProducer kinesisProducer;

    private final List<Worker> workers = new ArrayList<>();

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    @SuppressWarnings("WeakerAccess")
    public KinesisEventBusClient(@NotNull Environment env) {
        final KinesisProps props = env.get(KinesisProps.class);
        kinesis         = new Kinesis(props);
        kinesisProducer = createProducer();
        kinesisClient   = createConsumer();
    }

    //~ Methods ......................................................................................................................................

    @Override public void close()
        throws IOException
    {
        super.close();
        workers.forEach((worker) -> {
            if (worker != null) worker.shutdown();
        });
        workers.clear();
        if (kinesisClient != null) kinesisClient.shutdown();
    }

    @Override public void publish(@NotNull String topic, @NotNull T event)
        throws IOException
    {
        final String partitionKey = Long.toString(DateTime.current().toMilliseconds());
        publish(topic, partitionKey, event);
    }

    @Override public void publish(String topic, @NotNull String key, @NotNull T event)
        throws IOException
    {
        try {
            kinesisProducer.addUserRecord(topic, key, ByteBuffer.wrap(JsonMapping.shared().writeValueAsBytes(event)));
        }
        catch (final JsonProcessingException e) {
            throw new IOException(e);
        }
    }

    @Override public void subscribe(String topic, Consumer<T> consumer, Class<T> clazz) {
        new AbstractCommand<Void>() {
                protected String getThreadPoolKey() {
                    return "kinesis-consumer[" + topic + "]";
                }

                @Override protected Void run() {
                    try {
                        final ConsumerContext<T> context = new ConsumerContext<>(consumer, clazz);
                        final Worker             worker  = createWorker(topic, context);
                        workers.add(worker);
                        worker.run();
                    }
                    catch (final Throwable e) {
                        logger.error("Unable to process Kinesis Consumer", e);
                    }
                    return null;
                }
            }.observe();
    }

    AmazonKinesis createConsumer() {
        return kinesis.createConsumer();
    }
    KinesisProducer createProducer() {
        return kinesis.createProducer();
    }

    Worker createWorker(@NotNull String topic, ConsumerContext<T> consumerContext)
        throws UnknownHostException
    {
        return kinesis.createWorker(topic, consumerContext);
    }

    //~ Static Fields ................................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(KinesisEventBusClient.class);
}  // end class KinesisEventBusClient
