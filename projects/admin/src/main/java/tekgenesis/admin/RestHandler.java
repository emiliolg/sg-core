
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.media.MediaType;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;

/**
 * User class for Handler: RestHandler
 */
public class RestHandler extends RestHandlerBase {

    //~ Constructors .................................................................................................................................

    RestHandler(@NotNull final Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<String> enumJson(@NotNull final String path) {
        final ObjectMapper mapper      = JsonMapping.shared();
        final ObjectNode   enumeration = mapper.createObjectNode();

        for (final Enumeration<?, ?> e : Enumerations.getValuesFor(getFqn(path)))
            enumeration.put(e.name(), e.label());  // using locale for i18n

        try {
            final StringWriter writer = new StringWriter();
            mapper.writer().writeValue(writer, enumeration);
            return ok(writer.toString()).withContentType(MediaType.APPLICATION_JSON);
        }
        catch (final IOException e) {
            Logger.getLogger(RestHandler.class).error(e);
            return internalServerError(e.getMessage());
        }
    }

    //~ Methods ......................................................................................................................................

    /** Returns form fqn from a path. */
    @NotNull static String getFqn(@NotNull final String path) {
        final int start = path.indexOf("/") + 1;
        final int end   = path.indexOf("/", start);
        return end != -1 ? path.substring(start, end) : path.substring(start);
    }
}
