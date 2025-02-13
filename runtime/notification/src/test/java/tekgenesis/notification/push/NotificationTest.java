
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.notification.push;

import java.util.Arrays;

import org.junit.ClassRule;
import org.junit.Test;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.logging.LogConfig;
import tekgenesis.common.tools.test.server.SgHttpServerRule;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Constants.HTTP_LOCALHOST;
import static tekgenesis.common.media.MediaType.APPLICATION_JSON;
import static tekgenesis.common.service.HeaderNames.AUTHORIZATION;

/**
 * NotificationTest.
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection" })
public class NotificationTest {

    //~ Methods ......................................................................................................................................

    @Test public void testComplexMessage() {
        final Msg msg = new Msg();
        msg.number = 100;
        msg.text   = "Hello World";
        final MsgContent content = new MsgContent();
        content.data = msg;
        final String destinationA = "destinationA";
        content.registration_ids = new String[] { destinationA };

        expectPostAndSend(listOf(destinationA), content, msg);
    }

    @Test public void testMultipleDestinations() {
        final Seq<String>   devicesId = Colls.seq(listOf("destinationA", "destinationB", "destinationC"));
        final StringContent content   = new StringContent();
        content.registration_ids = devicesId.toArray(String[]::new);
        final String msg = "Simple message";
        content.data = msg;

        expectPostAndSend(devicesId, content, msg);
    }

    @Test public void testSimpleTextMessage() {
        final String        destinationA = "destinationA";
        final StringContent content      = new StringContent();
        content.registration_ids = new String[] { destinationA };
        final String msg = "Simple message";
        content.data = msg;

        expectPostAndSend(listOf(destinationA), content, msg);
    }

    private void expectPostAndSend(Seq<String> devicesId, Object content, Object msg) {
        mockServer.expectPost()
            .withContent(content)
            .withContentType(APPLICATION_JSON)
            .withHeader(AUTHORIZATION, "key=" + props.googleAuthKey)
            .respondOk();
        Notification.send(msg, devicesId);
    }

    //~ Static Fields ................................................................................................................................

    private static final NotificationProps props = new NotificationProps();

    @ClassRule public static SgHttpServerRule mockServer = SgHttpServerRule.httpServerRule().onStart(sgHttpServerRule -> {
                LogConfig.start();
                assert sgHttpServerRule != null;
                final String server = HTTP_LOCALHOST + sgHttpServerRule.getServerPort();
                props.googleAuthKey         = "ABC";
                props.googleNotificationUrl = server;
                props.googleResource        = "";
                Context.getEnvironment().put(props);
            }).build();

    //~ Inner Classes ................................................................................................................................

    private static class Msg {
        public int    number;
        public String text = null;

        public Msg() {}

        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            final Msg msg = (Msg) o;
            return number == msg.number && text.equals(msg.text);
        }

        @Override public int hashCode() {
            int result = text.hashCode();
            result = 31 * result + number;
            return result;
        }
    }

    private static class MsgContent {
        public Msg      data             = null;
        public String[] registration_ids = null;

        public MsgContent() {}

        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            final MsgContent content = (MsgContent) o;
            return data.equals(content.data) && Arrays.equals(registration_ids, content.registration_ids);
        }

        @Override public int hashCode() {
            int result = Arrays.hashCode(registration_ids);
            result = 31 * result + data.hashCode();
            return result;
        }
    }

    private static class StringContent {
        public String   data             = null;
        public String[] registration_ids = null;

        public StringContent() {}

        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            final StringContent content = (StringContent) o;
            return data.equals(content.data) && Arrays.equals(registration_ids, content.registration_ids);
        }

        @Override public int hashCode() {
            int result = Arrays.hashCode(registration_ids);
            result = 31 * result + data.hashCode();
            return result;
        }
    }
}  // end class NotificationTest
