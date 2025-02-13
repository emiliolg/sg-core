
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service.html;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.infinispan.Cache;
import org.jetbrains.annotations.NotNull;

import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.common.core.Constants;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.media.MediaType;
import tekgenesis.common.service.etl.HtmlReadMessageConverter;
import tekgenesis.form.properties.XHtmlProps;
import tekgenesis.service.Forwarder;
import tekgenesis.service.ReferenceSupplier;

import static tekgenesis.common.core.Suppliers.empty;
import static tekgenesis.common.util.Files.close;

/**
 * Message converter for reading and writing html.
 */
public class HtmlMessageConverter extends HtmlReadMessageConverter {

    //~ Instance Fields ..............................................................................................................................

    private Supplier<Forwarder> forwarder;

    //~ Constructors .................................................................................................................................

    /** Default constructor that uses {@link #DEFAULT_CHARSET} as default charset. */
    public HtmlMessageConverter() {
        this(DEFAULT_CHARSET);
    }

    /** Constructor accepting a default charset to use if content type does not specifies one. */
    public HtmlMessageConverter(@NotNull Charset charset) {
        super(charset);
        forwarder = empty();
    }

    //~ Methods ......................................................................................................................................

    /** Provide converter forwarder. To be deprecated in conjuction with Sui Generis views. */
    public HtmlMessageConverter withForwarder(@NotNull final ReferenceSupplier<Forwarder> f) {
        forwarder = f;
        return this;
    }

    @Override public void write(Html content, MediaType contentType, OutputStream stream)
        throws IOException
    {
        final HtmlInstance html = (HtmlInstance) content;

        final int cacheExpiration = Context.getProperties(XHtmlProps.class).cacheExpiration;
        try {
            final Charset            c      = getContentTypeCharsetOrDefault(contentType, charset);
            final OutputStreamWriter writer = new OutputStreamWriter(stream, c);
            if (cacheExpiration <= 0) html.render(writer, forwarder);
            else {
                final String cacheKey   = html.getHash();
                final String cachedHtml = getCache().get(cacheKey);

                if (cachedHtml != null) writer.write(cachedHtml);
                else {
                    final StringWriter stringWriter = new StringWriter();
                    html.render(stringWriter, forwarder);

                    final String result = stringWriter.toString();
                    getCache().put(cacheKey, result, cacheExpiration, TimeUnit.SECONDS);
                    writer.write(result);
                }
            }

            close(writer);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override protected boolean isReadOnly() {
        return false;
    }

    //~ Methods ......................................................................................................................................

    private static Cache<String, String> getCache() {
        return Context.getSingleton(InfinispanCacheManager.class).getLocalMap(Constants.SUIGENERIS_XHTML_CACHE);
    }
}
