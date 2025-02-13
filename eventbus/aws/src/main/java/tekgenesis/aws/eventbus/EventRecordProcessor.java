
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.aws.eventbus;

import java.io.IOException;
import java.util.List;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import com.amazonaws.services.kinesis.model.Record;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.logging.Logger;

/**
 * AWS Kinesis Event Record Processor.
 */
public class EventRecordProcessor<T> implements IRecordProcessor {

    //~ Instance Fields ..............................................................................................................................

    private ConsumerContext<T> conf = null;

    private String shardId = null;

    //~ Constructors .................................................................................................................................

    private EventRecordProcessor(ConsumerContext<T> conf) {
        this.conf = conf;
    }

    //~ Methods ......................................................................................................................................

    @Override public void initialize(InitializationInput initializationInput) {
        shardId = initializationInput.getShardId();
        logger.info("Initializing record processor for shard: %s", initializationInput.getShardId());
    }

    @Override public void processRecords(ProcessRecordsInput recordsInput) {
        final List<Record> records = recordsInput.getRecords();
        logger.info("Processing " + records.size() + " records from " + shardId);

        // Process records and perform all exception handling.
        try {
            processRecordsWithRetries(records);
            checkpoint(recordsInput.getCheckpointer());
        }
        catch (final IOException e) {
            logger.error(e);
        }
    }

    @Override public void shutdown(ShutdownInput shutdownInput) {
        logger.info("Shutting down record processor for shard: %s", shardId);
        // Important to checkpoint after reaching end of shard, so we can start processing data from child shards.
        if (shutdownInput.getShutdownReason() == ShutdownReason.TERMINATE) checkpoint(shutdownInput.getCheckpointer());
    }

    private void checkpoint(IRecordProcessorCheckpointer checkpointer) {
        try {
            checkpointer.checkpoint();
        }
        catch (final Exception e) {
            logger.error("Checkpoint failed.", e);
        }
    }

    private void processRecordsWithRetries(List<Record> records)
        throws IOException
    {
        for (final Record record : records) {
            final byte[] array = record.getData().array();
            conf.consume(array);
        }
    }

    //~ Methods ......................................................................................................................................

    static <T> IRecordProcessor create(ConsumerContext<T> conf) {
        return new EventRecordProcessor<>(conf);
    }

    //~ Static Fields ................................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(EventRecordProcessor.class);
}  // end class EventRecordProcessor
