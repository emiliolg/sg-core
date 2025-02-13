
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.notification.push;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.invoker.HttpInvoker;

import static java.lang.String.format;

import static tekgenesis.common.env.context.Context.getProperties;
import static tekgenesis.common.invoker.HttpInvokers.invoker;
import static tekgenesis.common.media.MediaType.APPLICATION_JSON;
import static tekgenesis.common.service.HeaderNames.AUTHORIZATION;

/**
 * NotificationMessage.
 */
public final class Notification {

    //~ Constructors .................................................................................................................................

    private Notification() {}

    //~ Methods ......................................................................................................................................

    /** Send notification. */
    public static <T> void send(T content, Seq<String> to) {
        final Message<T> message = new Message<>();
        message.tos     = to;
        message.content = content;

        final NotificationProps props   = getProperties(NotificationProps.class);
        final HttpInvoker       invoker = invoker(props.googleNotificationUrl);
        invoker.resource(props.googleResource)
            .header(AUTHORIZATION, format(KEY_STR, props.googleAuthKey))
            .contentType(APPLICATION_JSON)
            .post(message);
    }

    //~ Static Fields ................................................................................................................................

    private static final String KEY_STR = "key=%s";
}
