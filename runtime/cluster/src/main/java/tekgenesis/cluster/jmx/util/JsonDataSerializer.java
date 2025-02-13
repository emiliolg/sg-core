
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.json.JsonMapping;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

/**
 * Help task to serialize MM Objects to json.
 */
public final class JsonDataSerializer {

    //~ Constructors .................................................................................................................................

    private JsonDataSerializer() {}

    //~ Methods ......................................................................................................................................

    /** Serialize as Json based only in protected and public fields. */
    public static <T> String json(@Nullable T obj)
        throws JsonProcessingException
    {
        return dataSerializer.writeValueAsString(obj);
    }

    //~ Static Fields ................................................................................................................................

    private static final ObjectMapper dataSerializer = JsonMapping.json().setVisibility(FIELD, PROTECTED_AND_PUBLIC);
}
