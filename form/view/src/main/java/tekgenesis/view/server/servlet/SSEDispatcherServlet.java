
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.servlets.EventSource;
import org.jetbrains.annotations.NotNull;

import tekgenesis.app.sse.SSEProps;
import tekgenesis.app.sse.SSEService;
import tekgenesis.app.sse.SSEType;
import tekgenesis.common.core.UUID;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Sha;

import static tekgenesis.common.Predefined.cast;

public class SSEDispatcherServlet extends SSEServlet {

    //~ Methods ......................................................................................................................................

    EventSource newEventSource(final HttpServletRequest req) {
        final String remoteAddr = req.getRemoteAddr();
        final Sha    sha        = new Sha();
        sha.process((remoteAddr + UUID.generateUUIDString()).getBytes());
        final String token = sha.getDigestAsString();

        return new EventSource() {
            @Override public void onOpen(final Emitter emitter) {
                final SSEProps props = Context.getEnvironment().get(SSEProps.class);
                if (props.type != SSEType.NONE) {
                    send(emitter, "token: " + token);
                    final SSEService<String> service = cast(Context.getContext().getSingleton(SSEService.class));
                    service.subscribe(token, msg -> send(emitter, msg));
                }
                else emitter.close();
            }

            @Override public void onClose() {
                logger.info("Closing communication with channel " + token);
            }

            private void send(@NotNull Emitter emitter, String msg) {
                try {
                    emitter.data(msg);
                }
                catch (final IOException e) {
                    logger.error("Unable to process SSE for channel " + token, e);
                }
            }
        };
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1744205512981820152L;

    private static final Logger logger = Logger.getLogger(SSEDispatcherServlet.class);
}  // end class SSEDispatcherServlet
