
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.aws.eventbus;

import org.junit.Rule;

import tekgenesis.eventbus.test.AbstractEventBusClientTest;
import tekgenesis.eventbus.test.EventBusRule;

/**
 * Kinesis client test.
 */
public abstract class KinesisEventBusClientTest extends AbstractEventBusClientTest {

    //~ Instance Fields ..............................................................................................................................

    @Rule public final EventBusRule eventBusRule = new EventBusRule() {
            public String getImplementation() {
                return KinesisEventBusClient.class.getCanonicalName();
            }
            public KinesisProps getProps() {
                final KinesisProps kp = new KinesisProps();
                kp.appName                       = "test-eventbus";
                kp.profileName                   = "default";
                kp.failoverTimemillis            = 1000;
                kp.idleTimeBetweenReads          = 200;
                kp.shardSyncIntervalMillis       = 1000;
                kp.parentShardPollIntervalMillis = 1000;
                return kp;
            }
        };

    // @Test public void testSimple()
    // throws IOException
    // {
    // final AtomicInteger topicACount = new AtomicInteger(0);
    // final AtomicInteger topicBCount = new AtomicInteger(0);
    //
    // try(final KinesisMockEventBusClient<CustomEvent> busMockClient = new KinesisMockEventBusClient<>(Context.getEnvironment())) {
    // busMockClient.createTopic("eventbus-dev");
    // busMockClient.createTopic("eventbus-dev3");
    //
    // final CustomEvent object = new CustomEvent();
    // object.msg = "Msg" + DateTime.current();
    //
    // final int maxItemsCount = 3;
    // for (int i = 0; i < maxItemsCount; i++) {
    // object.nro = i;
    // EventBus.publish("eventbus-dev3", object);
    // EventBus.publish(TOPIC, object);
    // }
    //
    // EventBus.subscribe("eventbus-dev3", (m) -> topicACount.addAndGet(1), CustomEvent.class);
    // EventBus.subscribe(TOPIC, (m) -> topicBCount.addAndGet(1), CustomEvent.class);
    //
    // final long start = DateTime.currentTimeMillis();
    //
    // // noinspection StatementWithEmptyBody
    // while ((topicACount.get() < maxItemsCount || topicBCount.get() < maxItemsCount) &&
    // (DateTime.currentTimeMillis() - start) < Times.MILLIS_MINUTE) {}
    // assertThat(topicACount.get()).as("Topic A").isEqualTo(maxItemsCount);
    // assertThat(topicBCount.get()).as("Topic B").isEqualTo(maxItemsCount);
    // }
    // }
}  // end class KinesisEventBusClientTest
