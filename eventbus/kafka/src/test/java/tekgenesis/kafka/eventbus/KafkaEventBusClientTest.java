
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.kafka.eventbus;

import org.junit.Rule;

import tekgenesis.eventbus.test.AbstractEventBusClientTest;
import tekgenesis.eventbus.test.EventBusRule;

import static tekgenesis.common.Predefined.isEmpty;

/**
 * KafkaEventConsumer Test.
 */
public abstract class KafkaEventBusClientTest extends AbstractEventBusClientTest {

    //~ Instance Fields ..............................................................................................................................

    @Rule public final EventBusRule eventBusRule = new EventBusRule() {
            public String getImplementation() {
                return KafkaEventBusClient.class.getCanonicalName();
            }

            public KafkaProps getProps() {
                final KafkaProps kp          = new KafkaProps();
                final String     kafkaServer = System.getProperty("kafka.sever");

                if (isEmpty(kafkaServer)) throw new IllegalStateException("Kafka server not set for tests");

                kp.bootstrapServers = kafkaServer + ":9092";
                kp.zookeeperServers = kafkaServer + ":2181";
                return kp;
            }
        };
}  // end class KafkaEventBusClientTest
