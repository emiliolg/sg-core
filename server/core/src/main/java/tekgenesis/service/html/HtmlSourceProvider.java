
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service.html;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.annotation.ParametersAreNonnullByDefault;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.common.util.Sha;
import tekgenesis.form.WebResourceManager;

import static tekgenesis.common.core.Constants.UTF8;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.service.html.HtmlSourceProvider.HtmlPath.toReader;

/**
 * A provider of html source.
 */
@ParametersAreNonnullByDefault public interface HtmlSourceProvider {

    //~ Methods ......................................................................................................................................

    /** Returns template key. */
    String key();

    /** Returns the source reader. */
    Option<Reader> optionalReader();

    /** Returns the source reader. */
    default Reader reader() {
        final Option<Reader> reader = optionalReader();
        if (reader.isEmpty()) throw new IllegalArgumentException("Html source not found: " + key());
        return reader.get();
    }

    //~ Methods ......................................................................................................................................

    /** Returns a reader for a file path. */
    static HtmlPath fromPath(final String path) {
        return new HtmlPath(path);
    }

    /** Returns a reader for a html source in a Resource. */
    static HtmlResource fromResource(final Resource resource) {
        return new HtmlResource(resource);
    }

    /** Returns a reader for a direct html source. */
    static HtmlString fromString(final String str) {
        return new HtmlString(str);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Represents an html instance with a path reference to the content file.
     */
    class HtmlPath implements HtmlSourceProvider {
        private final String path;

        private HtmlPath(final String path) {
            this.path = path;
        }

        @Override public String key() {
            return path;
        }

        @Override public Option<Reader> optionalReader() {
            return reader(path);
        }

        static Option<Reader> reader(final String path) {
            final byte[] bytes = WebResourceManager.getInstance().readWebResource(path.trim());
            return bytes == null ? Option.empty() : toReader(bytes);
        }

        static Option<Reader> toReader(final byte[] bytes) {
            return some(new InputStreamReader(new ByteArrayInputStream(bytes), DEFAULT_CHARSET));
        }

        private static final Charset DEFAULT_CHARSET = Charset.forName(UTF8);
    }

    class HtmlResource implements HtmlSourceProvider {
        private final Resource resource;

        private HtmlResource(Resource resource) {
            this.resource = resource;
        }

        @Override public String key() {
            return resource.getMaster().getSha();
        }

        @Override public Option<Reader> optionalReader() {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            resource.getMaster().copyTo(out);
            return toReader(out.toByteArray());
        }
        /** Return instance associated resource. */
        public Resource getResource() {
            return resource;
        }
    }

    class HtmlString implements HtmlSourceProvider {
        private final String html;
        private final String shaKey;

        private HtmlString(String html) {
            this.html = html;

            final Sha sha = new Sha();
            sha.process(html.getBytes());
            shaKey = sha.getDigestAsString();
        }

        @Override public String key() {
            return shaKey;
        }

        @Override public Option<Reader> optionalReader() {
            return some(new StringReader(html));
        }

        /** Return instance associated html. */
        public String getHtml() {
            return html;
        }
    }
}  // end interface HtmlSourceProvider
