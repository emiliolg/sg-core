
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.List;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.service.etl.JsonMessageConverter;

import static tekgenesis.common.Predefined.option;
import static tekgenesis.common.collections.Colls.seq;

/**
 * Custom IX Json message converter.
 */
public class IxJsonMessageConverter<T extends IxInstance<T, K>, K> extends JsonMessageConverter {

    //~ Constructors .................................................................................................................................

    /** Create default Json converter specifying supported Mime. */
    public IxJsonMessageConverter(@Nullable Class<T> clazz) {
        super(register(option(clazz).toList()));
    }

    /**  */
    public IxJsonMessageConverter(@NotNull List<Class<T>> clazz) {
        super(register(seq(clazz)));
    }

    //~ Methods ......................................................................................................................................

    private static <T extends IxInstance<T, K>, K> ObjectMapper register(@Nullable Seq<Class<T>> clazz) {
        final ObjectMapper objectMapper = JsonMapping.json();
        final SimpleModule module       = new SimpleModule("restInvokerCustomModule", new Version(1, 0, 0, null, null, null));

        if (clazz != null) {
            for (final Class<T> c : clazz) {
                module.addDeserializer(c, new IxInstanceJsonDeserializer<>(c));
                module.addSerializer(c, new IxInstanceJsonSerializer<>());
            }
        }

        module.addSerializer(int.class, new IxNumericTypeJsonSerializer.IntegerSerializer());
        module.addSerializer(double.class, new IxNumericTypeJsonSerializer.DoubleSerializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
