
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service.html;

import java.io.IOException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Suppliers;
import tekgenesis.common.core.Tuple;
import tekgenesis.service.Forwarder;

import static tekgenesis.common.collections.Colls.mkString;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.util.Files.copy;
import static tekgenesis.service.html.XHtmlTemplate.process;

/**
 * Represents an html instance with arguments set.
 */
@ParametersAreNonnullByDefault public abstract class HtmlInstance implements Html {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, Map<String, String>> messages;
    private final Map<String, Object>              parameters;
    private final HtmlSourceProvider               provider;
    private final Map<String, Html>                views;

    //~ Constructors .................................................................................................................................

    private HtmlInstance(Map<String, Object> parameters, HtmlSourceProvider provider) {
        this(parameters, Collections.emptyMap(), Collections.emptyMap(), provider);
    }

    /** Create html instance. */
    private HtmlInstance(Map<String, Object> parameters, Map<String, Html> views, Map<String, Map<String, String>> messages,
                         HtmlSourceProvider provider) {
        this.parameters = parameters;
        this.views      = views;
        this.messages   = messages;
        this.provider   = provider;
    }

    //~ Methods ......................................................................................................................................

    /** Return basic key for scope code generation. */
    public String key() {
        return provider.key();
    }

    /** Return a provider that can fetch the actual html source of this instance. */
    public HtmlSourceProvider provider() {
        return provider;
    }

    /** Writes the processed html to the provided Writer. */
    @Override public void render(final Writer writer)
        throws IOException
    {
        render(writer, Suppliers.empty());
    }

    /** Writes the processed html to the provided Writer. */
    public abstract void render(final Writer writer, final Supplier<Forwarder> forwarder)
        throws IOException;

    @Override public String toString() {
        return key();
    }

    /** Return a complete context map with the messages, parameters and views. */
    public Map<String, Object> getContext() {
        final Map<String, Object> context = new HashMap<>();
        context.putAll(getMessages());
        context.putAll(getParameters());
        context.putAll(getViews());
        return context;
    }

    /** Return hash for template. */
    @Override public String getHash() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        }
        catch (final NoSuchAlgorithmException ignore) {
            // ignore
        }

        assert md != null;
        md.update(key().getBytes());  // Update hash using content key bytes
        md.update(mkString(messages.entrySet()).getBytes());
        md.update(mkString(parameters.entrySet()).getBytes());
        for (final Html html : views.values())
            md.update(html.getHash().getBytes());

        completeHash(md);

        final byte[]        digest = md.digest();
        final StringBuilder sb     = new StringBuilder(2 * digest.length);
        for (final byte b : digest)
            sb.append(String.format("%02x", b & FORMAT_BYTE));
        return sb.toString();
    }

    /** Returns the messages classes. */
    public Map<String, Map<String, String>> getMessages() {
        return messages;
    }

    /** Returns the string parameters. */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /** Return view argument with given name. */
    public Html getView(String name) {
        return views.get(name);
    }

    /** Return view argument with given name. */
    public Map<String, Html> getViews() {
        return views;
    }

    protected void completeHash(final MessageDigest md) {}

    //~ Static Fields ................................................................................................................................

    private static final int FORMAT_BYTE = 0xff;

    //~ Inner Classes ................................................................................................................................

    public static class Jade extends HtmlInstance {
        Jade(final Map<String, Object> parameters, final Map<String, Html> views, final Map<String, Map<String, String>> messages,
             HtmlSourceProvider provider) {
            super(parameters, views, messages, provider);
        }

        @Override public void render(final Writer writer, final Supplier<Forwarder> forwarder)
            throws IOException
        {
            Jade4jTemplate.getInstance().render(this, writer);
        }
    }

    public static class Mustache extends HtmlInstance {
        Mustache(final Map<String, Object> parameters, final Map<String, Html> views, final Map<String, Map<String, String>> messages,
                 HtmlSourceProvider provider) {
            super(parameters, views, messages, provider);
        }

        @Override public void render(final Writer writer, final Supplier<Forwarder> forwarder)
            throws IOException
        {
            MustacheTemplate.getInstance().render(this, writer);
        }
    }

    public static class Static extends HtmlInstance {
        Static(final Map<String, Object> parameters, final Map<String, Html> views, final Map<String, Map<String, String>> messages,
               HtmlSourceProvider provider) {
            super(parameters, views, messages, provider);
        }

        @Override public void render(final Writer writer, final Supplier<Forwarder> forwarder)
            throws IOException
        {
            copy(provider().reader(), writer);
        }
    }

    public static class Xhtml extends HtmlInstance implements WithMetadata {
        private final List<Tuple<String, String>> metadata;
        private final Map<String, Object>         structs;

        Xhtml(final Map<String, Object> parameters, final Map<String, Object> structs, final Map<String, Html> views,
              final Map<String, Map<String, String>> messages, final List<Tuple<String, String>> metadata, HtmlSourceProvider provider) {
            super(parameters, views, messages, provider);
            this.metadata = metadata;
            this.structs  = structs;
        }

        /** Add a metadata property. */
        public void metadata(String property, String content) {
            metadata.add(tuple(property, content));
        }

        @Override public void render(final Writer writer, final Supplier<Forwarder> forwarder)
            throws IOException
        {
            process(this, writer, forwarder);
        }

        /** Returns the metadata. */
        public Seq<Tuple<String, String>> getMetadata() {
            return seq(metadata);
        }

        /** Returns the struct parameters. */
        public Map<String, Object> getStructs() {
            return structs;
        }

        /** Return view argument with given name. */
        public Xhtml getView(String name) {
            return (Xhtml) super.getView(name);
        }

        @Override protected void completeHash(final MessageDigest md) {
            md.update(getMetadata().mkString("").getBytes());
            md.update(mkString(structs.entrySet()).getBytes());
        }
    }  // end class Xhtml
}  // end class HtmlInstance
