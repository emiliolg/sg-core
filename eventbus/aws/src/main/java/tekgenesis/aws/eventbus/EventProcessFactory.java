
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.aws.eventbus;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;

/**
 * AWS Kinesis EventProcessFactory.
 */
public class EventProcessFactory<T> implements IRecordProcessorFactory {

    //~ Instance Fields ..............................................................................................................................

    private final ConsumerContext<T> conf;

    //~ Constructors .................................................................................................................................

    private EventProcessFactory(ConsumerContext<T> conf) {
        this.conf = conf;
    }

    //~ Methods ......................................................................................................................................

    @Override public IRecordProcessor createProcessor() {
        return EventRecordProcessor.create(conf);
    }

    //~ Methods ......................................................................................................................................

    /** Create IRecordProcessorFactory. */
    static <T> IRecordProcessorFactory create(ConsumerContext<T> conf) {
        return new EventProcessFactory<>(conf);
    }
}
