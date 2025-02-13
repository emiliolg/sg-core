
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
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.ParametersAreNonnullByDefault;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.MustacheException;
import com.samskivert.mustache.Template;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pegdown.PegDownProcessor;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateTimeBase;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.Version;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.security.SecurityUtils;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.util.LruCache;
import tekgenesis.form.WebResourceManager;

import static java.lang.Boolean.getBoolean;
import static java.lang.String.format;
import static java.text.DateFormat.SHORT;
import static java.text.DateFormat.getDateInstance;
import static java.text.DecimalFormat.getCurrencyInstance;
import static java.text.DecimalFormat.getNumberInstance;

import static org.pegdown.Extensions.ALL;
import static org.pegdown.Extensions.TASKLISTITEMS;

import static tekgenesis.codegen.common.MMCodeGenConstants.RESOURCE;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.core.Constants.HTML_DIR;
import static tekgenesis.common.core.Constants.MUSTACHE_EXT;
import static tekgenesis.common.core.Constants.SUIGEN_DEVMODE;
import static tekgenesis.common.core.Strings.split;
import static tekgenesis.common.env.context.Context.getProperties;
import static tekgenesis.common.util.JavaReservedWords.PUBLIC;
import static tekgenesis.security.shiro.web.URLConstants.*;

/**
 * Mustache html template processor.
 */
@ParametersAreNonnullByDefault class MustacheTemplate {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, Mustache.Lambda> baseContext;
    private final Mustache.Compiler            compiler;

    private final LruCache<String, Template> templates;

    //~ Constructors .................................................................................................................................

    private MustacheTemplate() {
        templates   = LruCache.createLruCache(1000);
        baseContext = Lambdas.create();

        compiler = Mustache.compiler()
                   .withLoader(MustacheTemplate::readHtml)
                   .withFormatter(this::formatValue)
                   .emptyStringIsFalse(true)
                   .zeroIsFalse(true)
                   .nullValue("");
    }

    //~ Methods ......................................................................................................................................

    /** Process and write the template to the provided writer. */
    public void render(final HtmlInstance.Mustache html, final Writer out) {
        try {
            final Template template = getTemplate(html);
            template.execute(html.getContext(), baseContext, out);
        }
        catch (final MustacheException e) {
            throw new HtmlTemplateException(html, e);
        }
    }

    private synchronized Template compileTemplate(@NotNull final HtmlInstance.Mustache html) {
        Template template = templates.get(html.key());
        if (template == null) {
            template = compiler.compile(readHtml(html));
            if (!getBoolean(SUIGEN_DEVMODE)) templates.put(html.key(), template);
        }
        return template;
    }

    private String formatValue(Object v) {
        if (v instanceof String) return (String) v;
        if (v instanceof Number) return getNumberInstance(locale()).format(v);
        if (v instanceof Enumeration) return ((Enumeration<?, ?>) v).label();
        if (v instanceof DateTimeBase) return ((DateTimeBase<?>) v).format(getDateInstance(SHORT, locale()));
        if (v instanceof Iterable) return Colls.mkString((Iterable<?>) v, ", ");
        // if (v instanceof EntityInstance) return ((EntityInstance<?, ?>) v).describe().mkString(" ");
        return String.valueOf(v);
    }

    private Template getTemplate(@NotNull final HtmlInstance.Mustache html) {
        final Template template = templates.get(html.key());
        return template == null ? compileTemplate(html) : template;
    }

    //~ Methods ......................................................................................................................................

    /** Return singleton thread-safe instance. */
    public static MustacheTemplate getInstance() {
        return Holder.instance;
    }

    static String formBootstrap() {
        final StringBuilder result = new StringBuilder("\n");

        for (final String url : ImmutableList.of(sha(SUIGENERIS_CSS), sha(FORMS_CSS)))
            result.append(format(LINK_STYLE, url));

        final ObjectNode conf = JsonMapping.json().createObjectNode();
        conf.put(USER_ID, SecurityUtils.getSession().isAuthenticated() ? SecurityUtils.getSession().getPrincipal().getId() : "");  // ShiroUtils.currentUserId();
        conf.put(LOCALE_PARAM, locale().toString());
        conf.put(VERSION, Version.getInstance().getComponents().mkString(", "));

        final String jsPath = sha(SG_FORMS_JS);
        result.append(format(SG_BOOTSTRAP_SCRIPT, jsPath, conf.toString()));

        return result.toString();
    }

    static String sgComponentsVersions() {
        return Version.getInstance().getComponents().mkString(" / ");
    }

    private static Locale locale() {
        return Context.getContext().getLocale();
    }

    private static Reader readHtml(@NotNull HtmlInstance.Mustache html) {
        return html.provider().reader();
    }

    private static Reader readHtml(final String path) {
        String fixedPath = path.trim();
        if (!path.endsWith(MUSTACHE_EXT)) fixedPath += "." + MUSTACHE_EXT;
        if (!path.startsWith(HTML_DIR)) fixedPath = HTML_DIR + fixedPath;

        final Option<Reader> reader = HtmlSourceProvider.HtmlPath.reader(fixedPath);
        if (reader.isEmpty()) throw new IllegalArgumentException("Html Mustache include source not found: " + fixedPath);
        return reader.get();
    }

    private static String sha(String resourcePath) {
        return WebResourceManager.getInstance().shaPath(resourcePath);
    }

    //~ Static Fields ................................................................................................................................

    private static final String LINK_STYLE          = "<link rel='stylesheet' href='%s'/>\n";
    private static final String SG_BOOTSTRAP_SCRIPT = "<script id='sg-forms' src='%s' data-sg-type='bootstrap' data-sg-config='%s'></script>";
    private static final String SG_FORMS_JS         = "/public/sg/js/sg-forms.js";
    static final String         CONCAT_SERVLET      = "/sg/concat?";
    static final String         CDN_HOST            = notEmpty(getProperties(ApplicationProps.class).cdnHost, "");

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String VERSION = "version";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String USER_ID = "userId";

    //~ Inner Classes ................................................................................................................................

    private static class Holder {
        private static final MustacheTemplate instance = new MustacheTemplate();
    }

    private static class Lambdas {
        @NotNull private static String addPrefixToPath(String prefix, String path) {
            final String trimmed = path.trim();
            return trimmed.startsWith("/") ? prefix + trimmed : prefix + "/" + trimmed;
        }
        @Nullable private static <T> T contextVal(final Template.Fragment frag, Class<T> clazz) {
            final Object context = frag.context();
            if (!(context instanceof Map)) return null;
            final Map<String, Object> map = cast(context);
            final Object              s   = map.get(frag.execute());
            if (!clazz.isInstance(s)) return null;
            return clazz.cast(s);
        }
        private static Map<String, Mustache.Lambda> create() {
            final Map<String, Mustache.Lambda> result = new HashMap<>();

            result.put("sha", (frag, out) -> resource(PUBLIC, frag, out));  // transform public resource path into sha
            result.put(RESOURCE, (frag, out) -> resource("", frag, out));   // deprecate (usa sha)
            result.put("img", (frag, out) -> resource("img", frag, out));   // deprecate (usa sha)
            result.put("js", (frag, out) -> resource("js", frag, out));     // deprecate (usa sha)
            result.put("css", (frag, out) -> resource("css", frag, out));   // deprecate (usa sha)
            result.put("favicon", customOrDefault(DEFAULT_FAVICON, DEFAULT_TEKGENESIS_FAVICON));
            result.put("css-theme", customOrDefault(DEFAULT_THEME, DEFAULT_TEKGENESIS_THEME));

            final PegDownProcessor pegDownProcessor = new PegDownProcessor(ALL | TASKLISTITEMS);
            result.put("md", str(str -> unescape(pegDownProcessor.markdownToHtml(str))));
            result.put("json", (frag, out) -> out.write(frag.execute().replace("&quot;", "\"")));
            result.put("$", str((v) -> getCurrencyInstance(locale()).format(new BigDecimal(v))));

            result.put("sg-include",
                (frag, out) -> {
                    final HtmlInstance html = contextVal(frag, HtmlInstance.class);
                    if (html != null) html.render(out);
                });

            result.put("sg-forms-bootstrap", (frag, out) -> out.write(formBootstrap()));
            result.put("sg-versions", (frag, out) -> out.write(sgComponentsVersions()));

            return result;
        }  // end method create

        private static Mustache.Lambda customOrDefault(final String def, final String tekDef) {
            return (frag, out) -> {
                       if (!resource("", frag, out)) {
                           final String sha = sha(def);
                           out.write(def.equals(sha) ? sha(tekDef) : sha);
                       }
                   };
        }

        private static boolean resource(final String prefix, final Template.Fragment frag, Writer out)
            throws IOException
        {
            final String path = frag.execute();
            if (isNotEmpty(path)) {
                out.write(CDN_HOST);

                if (!path.contains(",")) out.write(sha(addPrefixToPath(prefix, path)));
                else out.write(split(path, ',').map(p -> addPrefixToPath(prefix, p)).map(MustacheTemplate::sha).mkString(CONCAT_SERVLET, "&", ""));
                return true;
            }
            return false;
        }

        private static Mustache.Lambda str(Function<String, String> fn) {
            return (frag, out) -> {
                       final String value = frag.execute();
                       if (isNotEmpty(value)) out.write(fn.apply(value));
                   };
        }

        private static String unescape(final String text) {
            String result = text;
            for (final String[] escape : HTML_UNSCAPES)
                result = result.replace(escape[0], escape[1]);
            return result;
        }

        /** Escapes HTML entities. */
        private static final String[][] HTML_UNSCAPES = {
            { "&amp;", "&" },
            { "&#39;", "'" },
            { "&quot;", "\"" },
            { "&lt;", "<" },
            { "&gt;", ">" },
            { "&#x60;", "`" },
            { "&#x3D;", "=" }
        };
    }  // end class Lambdas
}  // end class MustacheTemplate
