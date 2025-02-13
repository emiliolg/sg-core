
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tekgenesis.common.invoker.metric.InvocationMetricsPoller;
import tekgenesis.common.invoker.metric.InvocationMetricsPoller.MetricsAsJsonPollerListener;
import tekgenesis.common.invoker.metric.InvokerMetricsProperties;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.media.MediaType;
import tekgenesis.common.service.Headers;
import tekgenesis.common.service.Status;

import static tekgenesis.common.env.context.Context.getEnvironment;
import static tekgenesis.view.server.servlet.Servlets.NO_CACHE;

/**
 * Streams Invocation metrics in text/event-stream format.
 */
public class InvocationMetricsStreamServlet extends HttpServlet {

    //~ Methods ......................................................................................................................................

    /**
     * Handle servlet being un-deployed by gracefully releasing connections so poller threads stop.
     */
    @Override
    @SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
    public void destroy() {
        /* Set marker so the loops can break out */
        isDestroyed = true;
        super.destroy();
    }

    @Override
    @SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
    public void init()
        throws ServletException
    {
        isDestroyed    = false;
        maxConnections = getEnvironment().get(InvokerMetricsProperties.class).maxStreamConcurrentConnections;
    }

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        handleRequest(request, response);
    }

    /**
     * 1- Maintain an open connection with the client 2- On initial connection send latest data of
     * each requested event type 3- Subsequently send all changes for each requested event type.
     */
    private void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        /* Ensure we aren't allowing more connections than we want */
        final int               connections = concurrentConnections.incrementAndGet();
        InvocationMetricsPoller poller      = null;
        try {
            if (connections > maxConnections)
                response.sendError(Status.SERVICE_UNAVAILABLE.code(), "Maximum Concurrent Connections reached: " + maxConnections);
            else {
                final int delay = resolveDelay(request);

                /* Initialize response */
                response.setHeader(Headers.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM.toString());
                response.setHeader(Headers.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate");
                response.setHeader(Headers.PRAGMA, NO_CACHE);

                final MetricJsonListener listener = new MetricJsonListener();
                poller = new InvocationMetricsPoller(listener, delay);

                /* Start polling and it will write directly to the output stream */
                poller.start();
                logger.info("Starting poller");

                loop(response, poller, delay, listener);
            }
        }
        catch (final Exception e) {
            logger.error("Error initializing servlet for metrics event stream.", e);
        }
        finally {
            concurrentConnections.decrementAndGet();
            if (poller != null) poller.shutdown();
        }
    }  // end method handleRequest

    /**
     * We will use a "single-writer" approach where the Servlet thread does all the writing by
     * fetching JSON messages from the MetricJsonListener to write them to the output.
     */
    private void loop(HttpServletResponse response, InvocationMetricsPoller poller, int delay, MetricJsonListener listener) {
        try {
            while (poller.isRunning() && !isDestroyed) {
                final List<String> messages = listener.getJsonMetrics();
                if (messages.isEmpty()) response.getWriter().println("ping: \n");
                else {
                    for (final String json : messages)
                        response.getWriter().println("data: " + json + "\n");
                }

                /* Shortcut breaking out of loop if we have been destroyed */
                if (isDestroyed) break;

                // After outputting all the messages we will flush the stream
                response.flushBuffer();

                // Explicitly check for client disconnect - PrintWriter does not throw exceptions
                if (response.getWriter().checkError()) throw new IOException("io error");

                // Now wait the 'delay' time
                Thread.sleep(delay);
            }
        }
        catch (final InterruptedException e) {
            poller.shutdown();
            logger.debug("InterruptedException. Will stop polling.");
            Thread.currentThread().interrupt();
        }
        catch (final IOException e) {
            poller.shutdown();
            logger.debug("IOException while trying to write (generally caused by client disconnecting). Will stop polling.", e);
        }
        catch (final Exception e) {
            poller.shutdown();
            logger.error("Failed to write. Will stop polling.", e);
        }
        logger.debug("Stopping stream to connection");
    }

    @SuppressWarnings("MagicNumber")
    private int resolveDelay(HttpServletRequest request) {
        try {
            final String d = request.getParameter("delay");
            if (d != null) return Integer.parseInt(d);
        }
        catch (final Exception ignored) {}

        return 1000;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -7548505095303313237L;

    private static final Logger logger = Logger.getLogger(InvocationMetricsStreamServlet.class);

    /* used to track number of connections and throttle */
    private static final AtomicInteger concurrentConnections = new AtomicInteger(0);
    private static Integer             maxConnections        = 0;

    private static volatile boolean isDestroyed = false;

    //~ Inner Classes ................................................................................................................................

    /**
     * This will be called from another thread so needs to be thread-safe.
     */
    private static class MetricJsonListener implements MetricsAsJsonPollerListener {
        /**
         * Setting limit to 1000. In a healthy system there isn't any reason to hit this limit so if
         * we do it will throw an exception which causes the poller to stop. This is a safety check
         * against a runaway poller causing memory leaks.
         */
        private final LinkedBlockingQueue<String> jsonMetrics = new LinkedBlockingQueue<>(1000);

        /** Store JSON messages in a queue. */
        @Override public void handleJsonMetric(String json) {
            jsonMetrics.add(json);
        }

        /** Get all JSON messages in the queue. */
        private List<String> getJsonMetrics() {
            final List<String> metrics = new ArrayList<>();
            jsonMetrics.drainTo(metrics);
            return metrics;
        }
    }
}
