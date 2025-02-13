
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.eclipse.jetty.servlets.EventSource;

import tekgenesis.common.util.Reflection;

/**
 * This servlet is a "EventSourceServlet" with a workaround to avoid asyncchain.
 */
public abstract class SSEServlet extends HttpServlet {

    //~ Instance Fields ..............................................................................................................................

    private int heartBeatPeriod = 10;

    private ScheduledExecutorService scheduler = null;

    //~ Methods ......................................................................................................................................

    @Override public void destroy() {
        if (scheduler != null) scheduler.shutdown();
    }

    @Override public void init()
        throws ServletException
    {
        final String heartBeatPeriodParam = getServletConfig().getInitParameter("heartBeatPeriod");
        if (heartBeatPeriodParam != null) heartBeatPeriod = Integer.parseInt(heartBeatPeriodParam);
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // noinspection DuplicateStringLiteralInspection
        final Enumeration<String> acceptValues = request.getHeaders("Accept");
        while (acceptValues.hasMoreElements()) {
            final String accept = acceptValues.nextElement();
            if ("text/event-stream".equals(accept)) {
                final EventSource eventSource = newEventSource(request);
                if (eventSource == null) response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                else {
                    respond(response);
                    // ------------------------------------------------------------------------------------------------------------------
                    // This is a "workaround" in order to set asyn only the current request and avoid asyncSupport for the whole chain.
                    // ------------------------------------------------------------------------------------------------------------------
                    Reflection.invoke((((ShiroHttpServletRequest) request).getRequest()), "setAsyncSupported", true);
                    final AsyncContext async = request.startAsync();
                    // Infinite timeout because the continuation is never resumed,
                    // but only completed on close
                    async.setTimeout(0);
                    final EventSourceEmitter emitter = new EventSourceEmitter(eventSource, async);
                    emitter.scheduleHeartBeat();
                    open(eventSource, emitter);
                }
                return;
            }
        }
        super.doGet(request, response);
    }  // end method doGet

    protected void open(EventSource eventSource, EventSource.Emitter emitter)
        throws IOException
    {
        eventSource.onOpen(emitter);
    }

    abstract EventSource newEventSource(final HttpServletRequest req);

    private void respond(HttpServletResponse response)
        throws IOException
    {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/event-stream");
        // By adding this header, and not closing the connection,
        // we disable HTTP chunking, and we can use write()+flush()
        // to send data in the text/event-stream protocol
        // noinspection DuplicateStringLiteralInspection
        response.addHeader("Connection", "close");
        response.flushBuffer();
    }

    //~ Static Fields ................................................................................................................................

    private static final byte[] CRLF          = { '\r', '\n' };
    private static final byte[] EVENT_FIELD   = "event: ".getBytes(StandardCharsets.UTF_8);
    private static final byte[] DATA_FIELD    = "data: ".getBytes(StandardCharsets.UTF_8);
    private static final byte[] COMMENT_FIELD = ": ".getBytes(StandardCharsets.UTF_8);

    private static final long serialVersionUID = 1744205512981820152L;

    //~ Inner Classes ................................................................................................................................

    private class EventSourceEmitter implements EventSource.Emitter, Runnable {
        private final AsyncContext        async;
        @SuppressWarnings("BooleanVariableAlwaysNegated")
        private boolean                   closed;
        private final EventSource         eventSource;
        private Future<?>                 heartBeat = null;
        private final ServletOutputStream output;

        EventSourceEmitter(EventSource eventSource, AsyncContext async)
            throws IOException
        {
            this.eventSource = eventSource;
            this.async       = async;
            output           = async.getResponse().getOutputStream();
        }

        @Override public void close() {
            synchronized (this) {
                closed = true;
                heartBeat.cancel(false);
            }
            async.complete();
        }

        @Override public void comment(String comment)
            throws IOException
        {
            synchronized (this) {
                output.write(COMMENT_FIELD);
                output.write(comment.getBytes(StandardCharsets.UTF_8));
                output.write(CRLF);
                output.write(CRLF);
                flush();
            }
        }

        @Override public void data(String data)
            throws IOException
        {
            synchronized (this) {
                final BufferedReader reader = new BufferedReader(new StringReader(data));
                String               line;
                while ((line = reader.readLine()) != null) {
                    output.write(DATA_FIELD);
                    output.write(line.getBytes(StandardCharsets.UTF_8));
                    output.write(CRLF);
                }
                output.write(CRLF);
                flush();
            }
        }

        @Override public void event(String name, String data)
            throws IOException
        {
            synchronized (this) {
                output.write(EVENT_FIELD);
                output.write(name.getBytes(StandardCharsets.UTF_8));
                output.write(CRLF);
                data(data);
            }
        }

        @Override public void run() {
            // If the other peer closes the connection, the first
            // flush() should generate a TCP reset that is detected
            // on the second flush()
            try {
                synchronized (this) {
                    output.write('\r');
                    flush();
                    output.write('\n');
                    flush();
                }
                // We could write, reschedule heartbeat
                scheduleHeartBeat();
            }
            catch (final IOException x) {
                // The other peer closed the connection
                close();
                eventSource.onClose();
            }
        }

        void flush()
            throws IOException
        {
            async.getResponse().flushBuffer();
        }

        private void scheduleHeartBeat() {
            synchronized (this) {
                if (!closed) heartBeat = scheduler.schedule(this, heartBeatPeriod, TimeUnit.SECONDS);
            }
        }
    }  // end class EventSourceEmitter
}
