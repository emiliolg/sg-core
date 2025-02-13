
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Ix Numeric serializer. IxInstance pk are always primitive types and ideafix supports null keys.
 * So, this serializer handle this scenario in order to send null when the number is MAX_VALUE
 */
final class IxNumericTypeJsonSerializer {

    //~ Constructors .................................................................................................................................

    private IxNumericTypeJsonSerializer() {}

    //~ Inner Classes ................................................................................................................................

    @SuppressWarnings("ClassNameDiffersFromFileName")
    static final class DoubleSerializer extends JsonSerializer<Double> {
        @Override public void serialize(Double value, JsonGenerator jg, SerializerProvider provider)
            throws IOException
        {
            if (value == Double.MAX_VALUE) jg.writeNull();
            else jg.writeNumber(value);
        }
    }

    @SuppressWarnings("ClassNameDiffersFromFileName")
    static final class IntegerSerializer extends JsonSerializer<Integer> {
        @Override public void serialize(Integer value, JsonGenerator jg, SerializerProvider provider)
            throws IOException
        {
            if (value == Integer.MAX_VALUE) jg.writeNull();
            else jg.writeNumber(value);
        }
    }
}
