
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.ix;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.logging.Logger;
import tekgenesis.persistence.*;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.persistence.ix.IxStoreHandlerType.MOCKED;

/**
 * Ix Batch Operation Handler.
 */
public final class IxBatchHandler {

    //~ Instance Fields ..............................................................................................................................

    private final List<BatchOperation<? extends IxInstance<?, ?>>> list   = new ArrayList<>(MAX_BATCH_SIZE);
    private final Logger                                           logger = Logger.getLogger(IxBatchHandler.class);

    //~ Constructors .................................................................................................................................

    private IxBatchHandler() {}

    //~ Methods ......................................................................................................................................

    /**
     * add a new operation in the Batch Stack.
     *
     * @param  batchOperation  BatchOperation
     */
    public <T extends IxInstance<T, ?>> void add(@NotNull BatchOperation<T> batchOperation) {
        logger.info(
            format("Add to batch (Thread: %s, Operation: %s,content: %s)",
                Thread.currentThread().getName(),
                batchOperation.getOperation(),
                batchOperation.getJson().toString()));
        if (getIxStoreHandlerType() == MOCKED) return;

        if (list.size() == MAX_BATCH_SIZE) throw new OperationNotAllowedException("Maximum operations in batch exceeded.");
        list.add(batchOperation);
    }

    /** Close the batch. */
    public void close() {
        logger.info(() -> format("Close IX Batch (Thread: %s)", Thread.currentThread().getName()));
        list.clear();
    }

    /** @return  the amount if item in the batch */
    public int size() {
        return list.size();
    }

    private void doBegin() {
        logger.info(() -> format("Begin IX Batch (Thread: %s)", Thread.currentThread().getName()));
        list.clear();
    }

    private <T extends IxInstance<T, K>, K> void doCommit() {
        logger.info(() -> format("Commit IX Batch (Thread: %s)", Thread.currentThread().getName()));
        if (getIxStoreHandlerType() == MOCKED) return;

        final ObjectMapper mapper    = JsonMapping.json();
        final ArrayNode    arrayNode = mapper.createArrayNode();

        // If there is nothing to send...
        if (list.isEmpty()) return;
        final List<Class<T>> classes = new ArrayList<>();
        // Create Message
        while (!list.isEmpty()) {
            final BatchOperation<T> pop   = cast(list.remove(0));
            final Class<T>          clazz = pop.getObjectClass();
            if (clazz != null) classes.add(clazz);
            final JsonNode json = pop.getJson();
            arrayNode.add(json);
        }

        final IxJsonMessageConverter<T, K> converter = new IxJsonMessageConverter<>(classes);
        // Post message
        final HttpInvoker restInvoker = IxUtil.createInvoker().withConverter(converter);
        restInvoker.resource("/").post(arrayNode);
    }

    private IxStoreHandlerType getIxStoreHandlerType() {
        final IxProps ixProps = Context.getEnvironment().get(IxService.getDomain(), IxProps.class);
        return IxStoreHandlerType.get(ixProps.url);
    }

    //~ Methods ......................................................................................................................................

    /** Abort current batch. */
    public static void abortBatch() {
        synchronized (batchLock) {
            // noinspection DuplicateStringLiteralInspection
            option(batchHandler.get()).getOrFail("Batch not started").close();
            batchHandler.remove();
        }
    }

    /** Begin the batch. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static void begin() {
        if (isBatchEnabled()) throw new OperationNotAllowedException("Batch operation already started.");

        synchronized (batchLock) {
            if (isBatchEnabled()) throw new OperationNotAllowedException("Batch operation already started.");
            final IxBatchHandler handler = create();
            batchHandler.set(handler);
            handler.doBegin();
        }
    }

    /** Commit the batch. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static void commit() {
        synchronized (batchLock) {
            option(batchHandler.get()).getOrFail("Batch not started").doCommit();
            batchHandler.remove();
        }
    }

    /** @return  current IxBatchHandler for the current thread execution, if it is exists. */
    public static IxBatchHandler current() {
        return batchHandler.get();
    }

    /** @return  true if the batch is started */
    public static boolean isBatchEnabled() {
        return current() != null;
    }

    /** @return  IxBatchHandler */
    private static IxBatchHandler create() {
        return new IxBatchHandler();
    }

    //~ Static Fields ................................................................................................................................

    private static final ThreadLocal<IxBatchHandler> batchHandler   = new ThreadLocal<>();
    private static final Object                      batchLock      = new Object();
    private static final int                         MAX_BATCH_SIZE = 4192;
}  // end class IxBatchHandler
