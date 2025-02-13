
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.eventbus.test;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.common.core.DateTime;
import tekgenesis.eventbus.test.msg.CustomEvent;
import tekgenesis.eventbus.test.msg.EnumMessage;
import tekgenesis.eventbus.test.msg.TypeMessage;

import static org.assertj.core.api.Java6Assertions.assertThat;

import static tekgenesis.eventbus.EventBus.publish;
import static tekgenesis.eventbus.EventBus.subscribe;
import static tekgenesis.eventbus.test.msg.EnumMessage.NAME;

/**
 * Abstract based class for Eventbus clients tests.
 */
public abstract class AbstractEventBusClientTest {

    //~ Instance Fields ..............................................................................................................................

    protected final String TOPIC = "eventbus-dev";

    //~ Methods ......................................................................................................................................

    @Test public void checkEnumMessages() {
        assertPublish(TOPIC, NAME, EnumMessage.class);
    }

    @Test public void checkPOJOMessages() {
        final CustomEvent object = new CustomEvent();
        object.msg = "Value";
        object.nro = 2003;
        assertPublish(TOPIC, object, CustomEvent.class);
    }

    @Test public void checkTypeMessages() {
        final TypeMessage object = new TypeMessage();
        object.setName("A").setNumber(1981);
        assertPublish(TOPIC, object, TypeMessage.class);
    }

    private <T> void assertPublish(@NotNull String topic, T object, Class<T> clazz) {
        final AtomicBoolean msgReceived = new AtomicBoolean(false);
        // EventBus
        subscribe(topic, (m) -> msgReceived.set(true), clazz);

        publish(topic, object);

        waitForMsg(msgReceived);

        assertThat(msgReceived.get()).isTrue();
    }

    private void waitForMsg(AtomicBoolean msgReceived) {
        final long start = DateTime.currentTimeMillis();
        // Wait until messge arrive or timeout (1sec)
        while (!msgReceived.get() || (DateTime.currentTimeMillis() - start < 10000)) {
            try {
                Thread.sleep(200);
            }
            catch (final InterruptedException e) {
                // ignore
            }
        }
    }
}  // end class AbstractEventBusClientTest
