
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
import java.util.function.Consumer;

import tekgenesis.common.json.JsonMapping;

/**
 * Consumer Context.
 */
class ConsumerContext<T> {

    //~ Instance Fields ..............................................................................................................................

    private final Class<T>    clazz;
    private final Consumer<T> consumer;

    //~ Constructors .................................................................................................................................

    ConsumerContext(Consumer<T> consumer, Class<T> clazz) {
        this.consumer = consumer;
        this.clazz    = clazz;
    }

    //~ Methods ......................................................................................................................................

    void consume(byte[] array)
        throws IOException
    {
        final T value = JsonMapping.shared().readValue(array, clazz);
        consumer.accept(value);
    }
}
