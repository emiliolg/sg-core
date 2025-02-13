
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet.concurrent;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.infinispan.Cache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.MDC;

import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.ExecutionFeedback;
import tekgenesis.transaction.Transaction;
import tekgenesis.view.server.servlet.gwtservices.ServerMethod;
import tekgenesis.view.shared.feedback.FeedbackEventData;
import tekgenesis.view.shared.response.FormModelResponse;

import static java.util.Map.Entry;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.view.server.servlet.concurrent.FutureExecution.FeedbackEventType.*;
import static tekgenesis.view.shared.feedback.FeedbackEventData.progress;
import static tekgenesis.view.shared.feedback.FeedbackEventData.started;

/**
 * Allow long running forms future executions.
 */
public class FutureExecution {

    //~ Constructors .................................................................................................................................

    private FutureExecution() {}

    //~ Methods ......................................................................................................................................

    /** Check for a termination or return an await response with joined events. */
    public static FeedbackEventData attempt(@NotNull String uuid) {
        return createFeedbackResponse(uuid, false);
    }

    /**
     * Check for long execution if defined, await a while for completion, or send execution to
     * background.
     */
    public static ServerMethod await(@NotNull ServerMethod m, Option<ExecutionFeedback> e) {
        return e.map(f -> (ServerMethod) new LongServerMethod(m, (ExecutionFeedbackImpl) f)).orElse(m);
    }

    /** Submit cancel directive for execution. */
    public static FeedbackEventData cancel(@NotNull String uuid) {
        final FeedbackEventData cancellation = FeedbackEventData.cancellation();
        getFeedbackEventsMap(uuid).put(CANCELATION.label(), cancellation);
        return cancellation;
    }

    /** Construct optional execution feedback. */
    public static Option<ExecutionFeedback> feedback(boolean isLongExecution) {
        return isLongExecution ? of(new ExecutionFeedbackImpl()) : empty();
    }

    private static FeedbackEventData createFeedbackResponse(String uuid, boolean started) {
        final Cache<String, FeedbackEventData> map = getFeedbackEventsMap(uuid);
        if (map.containsKey(TERMINATION.label())) return map.get(TERMINATION.label());
        else if (map.containsKey(CANCELATION.label())) return map.get(CANCELATION.label());
        else if (map.containsKey(EXCEPTION.label())) return map.get(EXCEPTION.label());
        else {
            final List<Entry<String, FeedbackEventData>> events = new ArrayList<>();
            events.addAll(map.entrySet());
            final FeedbackEventData result = started ? started(uuid) : FeedbackEventData.join(uuid);
            for (final Entry<String, FeedbackEventData> event : events) {
                result.join(event.getValue());
                map.remove(event.getKey());
            }
            return result;
        }
    }  // end method createFeedbackResponse

    private static FormModelResponse createResponse(@NotNull LongServerMethod method) {
        try {
            final FormModelResponse                response = method.future.get(wait_time, TimeUnit.MILLISECONDS);
            final String                           uuid     = method.getExecutionUUID();
            final Cache<String, FeedbackEventData> map      = getFeedbackEventsMap(uuid);

            if (map.containsKey(TERMINATION.label())) map.remove(TERMINATION.label());
            if (map.containsKey(CANCELATION.label())) map.remove(CANCELATION.label());
            if (map.containsKey(EXCEPTION.label())) map.remove(EXCEPTION.label());

            return response;
        }
        catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        catch (final TimeoutException ignored) {}

        final FormModelResponse result = new FormModelResponse();
        result.await(createFeedbackResponse(method.feedback.uuid, true));

        return result;
    }

    private static Cache<String, FeedbackEventData> getFeedbackEventsMap(String uuid) {
        return Context.getSingleton(InfinispanCacheManager.class).getCache(uuid);
    }

    //~ Static Fields ................................................................................................................................

    private static final int wait_time = 200;

    private static final ExecutorService executor = Executors.newFixedThreadPool(5, new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(0);

                @Override public Thread newThread(@NotNull Runnable r) {
                    final Thread t = new Thread(r, "feedback-" + threadNumber.incrementAndGet());
                    t.setDaemon(true);
                    return t;
                }
            });

    private static final Logger logger = getLogger(FutureExecution.class);

    //~ Enums ........................................................................................................................................

    enum FeedbackEventType {
        TERMINATION("termination-"), FUTURE("future-"), CANCELATION("cancelation-"), EXCEPTION("exception-");

        @NotNull private final String label;

        FeedbackEventType(@NotNull String label) {
            this.label = label;
        }

        @NotNull public String label() {
            return label;
        }
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Execution feedback.
     */
    public static class ExecutionFeedbackImpl implements ExecutionFeedback {
        private int          count = 0;
        private final String uuid;

        ExecutionFeedbackImpl() {
            uuid = UUID.randomUUID().toString();
        }

        ExecutionFeedbackImpl(@NotNull String uuid) {
            this.uuid = uuid;
        }

        @Override public ExecutionFeedback step(@Nullable String msg) {
            return step(NO_PROGRESS, msg);
        }

        @Override public ExecutionFeedback step(int percentage, @Nullable String msg) {
            getFeedbackEventsMap(uuid).put(FeedbackEventType.FUTURE.label() + (++count), progress(percentage, msg));
            return this;
        }

        @Override public boolean isCanceled() {
            return getFeedbackEventsMap(uuid).containsKey(CANCELATION.label());
        }

        public static final int NO_PROGRESS = -1;
    }

    /**
     * Long method to be performed on server after form request.
     */
    private static class LongServerMethod implements ServerMethod {
        @NotNull private final ExecutionFeedbackImpl feedback;
        private Future<FormModelResponse>            future;

        @NotNull private final ServerMethod original;

        private LongServerMethod(@NotNull ServerMethod original, @NotNull ExecutionFeedbackImpl feedback) {
            this.original = original;
            this.feedback = feedback;
            future        = null;
        }

        @Override public FormModelResponse exec() {
            final Locale              locale     = Context.getContext().getLocale();
            final Map<String, String> contextMap = MDC.getCopyOfContextMap();

            final Subject subject = ThreadContext.getSubject();
            future = executor.submit(() -> {
                    threadContext(subject, locale, contextMap);

                    FormModelResponse                      result;
                    final Cache<String, FeedbackEventData> queue = getFeedbackEventsMap(getExecutionUUID());
                    try {
                        Transaction.getCurrent().ifPresent(t -> {
                            t.rollback();
                            logger.error("Rollback previous transaction on feedback thread");
                        });
                        result = invokeInTransaction(original::exec);
                        queue.put(TERMINATION.label(), FeedbackEventData.termination(result));
                    }
                    catch (final Exception e) {
                        logger.error(e);
                        final Throwable cause = e.getCause();
                        result = new FormModelResponse().error(cause.getLocalizedMessage(), new ArrayList<>(), false);
                        queue.put(EXCEPTION.label(), FeedbackEventData.exception(result));
                    }
                    SecurityUtils.unbindContext();
                    return result;
                });
            return createResponse(this);
        }

        private void threadContext(Subject subject, Locale locale, Map<String, String> contextMap) {
            // Shiro thread subject context
            ThreadContext.bind(subject);
            // Locale
            Context.getContext().setLocale(locale);
            // Log
            MDC.setContextMap(contextMap);
        }

        @NotNull private String getExecutionUUID() {
            return feedback.uuid;
        }
    }  // end class LongServerMethod
}  // end class FutureExecution
