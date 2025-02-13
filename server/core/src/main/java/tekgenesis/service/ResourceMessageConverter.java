
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Resource;
import tekgenesis.common.core.Resource.Entry;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.media.MediaType;
import tekgenesis.common.service.Headers;
import tekgenesis.common.service.etl.AbstractMessageConverter;
import tekgenesis.persistence.ResourceHandler;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.service.HeaderNames.CONTENT_TYPE;
import static tekgenesis.common.service.HeaderNames.ETAG;

/**
 * Message converter for reading and writing {@link Resource resources}.
 */
public class ResourceMessageConverter extends AbstractMessageConverter<Entry> {

    //~ Methods ......................................................................................................................................

    @Override public Entry read(Class<? extends Entry> type, Type genericType, @Nullable MediaType contentType, InputStream stream)
        throws IOException
    {
        final ResourceHandler handler = Context.getSingleton(ResourceHandler.class);
        return handler.create().upload("xyz", contentType != null ? contentType.getMime().getMime() : "", stream).getMaster();
    }

    @Override public void write(Entry resource, MediaType contentType, Headers headers) {
        if (!resource.isExternal()) {
            headers.put(ETAG, resource.getSha());

            final String mimeType = resource.getMimeType();
            if (isNotEmpty(mimeType) && headers.getContentType() == null)
            // No mime validation performed
            headers.set(CONTENT_TYPE, mimeType);
            else super.write(resource, contentType, headers);
        }
    }

    @Override public void write(Entry resource, @Nullable MediaType contentType, OutputStream stream)
        throws IOException
    {
        resource.copyTo(stream);
    }

    @Override protected boolean supports(Class<?> clazz) {
        return Entry.class.isAssignableFrom(clazz);
    }
}
