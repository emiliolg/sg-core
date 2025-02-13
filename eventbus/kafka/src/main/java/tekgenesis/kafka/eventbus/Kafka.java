
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.kafka.eventbus;

import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.jetbrains.annotations.NotNull;

/**
 */
final class Kafka {

    //~ Constructors .................................................................................................................................

    private Kafka() {}

    //~ Methods ......................................................................................................................................

    @NotNull static KafkaConsumer<String, byte[]> createConsumer(KafkaProps kafkaProps) {
        final Properties consumerProp = new Properties();
        // noinspection DuplicateStringLiteralInspection
        consumerProp.put("bootstrap.servers", kafkaProps.bootstrapServers);
        consumerProp.put("group.id", "test");
        // consumerProp.put("heartbeat.interval.ms", "100");
        // consumerProp.put("max.poll.interval.ms", "100");
        consumerProp.put("enable.auto.commit", kafkaProps.enableAutoCommit);
        consumerProp.put("auto.commit.interval.ms", Long.toString(kafkaProps.autoCommitInterval));
        consumerProp.put("session.timeout.ms", Long.toString(kafkaProps.sessionTimeout));
        consumerProp.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProp.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");

        return new KafkaConsumer<>(consumerProp);
    }

    static KafkaProducer<String, byte[]> createProducer(KafkaProps kafkaProps) {
        final Properties producerProp = new Properties();
        // noinspection DuplicateStringLiteralInspection
        producerProp.put("bootstrap.servers", kafkaProps.bootstrapServers);
        producerProp.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProp.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        producerProp.put("acks", "all");
        producerProp.put("retries", 0);
        producerProp.put("batch.size", kafkaProps.batchSize);
        producerProp.put("linger.ms", 1);
        producerProp.put("buffer.memory", kafkaProps.memoryBuffer);

        return new KafkaProducer<>(producerProp);
    }
}  // end class Kafka
