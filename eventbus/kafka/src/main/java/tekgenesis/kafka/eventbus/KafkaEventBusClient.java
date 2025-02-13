
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.kafka.eventbus;

import java.io.IOException;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.NotNull;

import tekgenesis.common.command.AbstractCommand;
import tekgenesis.common.env.Environment;
import tekgenesis.common.logging.Logger;
import tekgenesis.eventbus.EventBusClient;

import static java.util.Collections.singletonList;

import static tekgenesis.common.core.Constants.UTF8;
import static tekgenesis.common.json.JsonMapping.shared;
import static tekgenesis.kafka.eventbus.Kafka.createConsumer;
import static tekgenesis.kafka.eventbus.Kafka.createProducer;

/**
 * Kafka EventBus consumer.
 */
public class KafkaEventBusClient<T> extends EventBusClient<T> {

    //~ Instance Fields ..............................................................................................................................

    private final KafkaProducer<String, byte[]> kafkaProducer;
    private final KafkaProps                    kafkaProps;

    //~ Constructors .................................................................................................................................

    /** Create a Kafka Eventbus Consumer. */
    public KafkaEventBusClient(@NotNull Environment env) {
        kafkaProps    = env.get(KafkaProps.class);
        kafkaProducer = createProducer(kafkaProps);
    }

    //~ Methods ......................................................................................................................................

    @Override public void close()
        throws IOException
    {
        super.close();
        if (kafkaProducer != null) kafkaProducer.close();
    }

    @Override public void publish(@NotNull String topic, @NotNull T event)
        throws IOException
    {
        publish(topic, "", event);
    }

    @Override public void publish(String topic, @NotNull String key, @NotNull T event)
        throws IOException
    {
        try {
            kafkaProducer.send(new ProducerRecord<>(topic, shared().writeValueAsString(event).getBytes(UTF8)));
            kafkaProducer.flush();
        }
        catch (final JsonProcessingException e) {
            throw new IOException(e);
        }
    }

    @Override public void subscribe(String topic, Consumer<T> consumer, Class<T> clazz) {
        new AbstractCommand<Void>() {
                @Override protected String getThreadPoolKey() {
                    return "kafka-consumer[" + topic + "]";
                }

                @Override protected Void run() {
                    try(final KafkaConsumer<String, byte[]> kafkaConsumer = createConsumer(kafkaProps)) {
                        kafkaConsumer.subscribe(singletonList(topic));

                        while (consume) {
                            final ConsumerRecords<String, byte[]> records = kafkaConsumer.poll(100);

                            // for (final TopicPartition partition : records.partitions()) {
                            // final List<ConsumerRecord<String, byte[]>> partitionRecords = records.records(partition);
                            // partitionRecords.forEach((r)->{
                            // try {
                            // final T obj = shared().readValue(r.value(), clazz);
                            // consumer.accept(obj);
                            // final long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                            // kafkaConsumer.commitAsync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)),null);
                            // }
                            // catch (final IOException e) {
                            // final String msg = format("Unable to deserialize Kafka message (Topic:%s, RawMsg: %s, Type:%s)",
                            // topic,
                            // new String(r.value()),
                            // clazz);
                            // logger.error(msg, e);
                            // }
                            // });
                            // }
                            records.forEach((r) -> {
                                try {
                                    final T obj = shared().readValue(r.value(), clazz);
                                    consumer.accept(obj);
                                }
                                catch (final IOException e) {
                                    final String msg = String.format("Unable to deserialize Kafka message (Topic:%s, RawMsg: %s, Type:%s)",
                                            topic,
                                            new String(r.value()),
                                            clazz);
                                    logger.error(msg, e);
                                }
                            });
                        }
                    }

                    return null;
                }  // end method run
            }.queue();  // queue();
    }             // end method subscribe

    //~ Static Fields ................................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(KafkaEventBusClient.class);
}  // end class KafkaEventBusClient
