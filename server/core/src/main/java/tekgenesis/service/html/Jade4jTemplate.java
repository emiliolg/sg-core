
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.filter.CachingFilter;
import de.neuland.jade4j.filter.Filter;
import de.neuland.jade4j.parser.Parser;
import de.neuland.jade4j.parser.node.Attr;
import de.neuland.jade4j.template.JadeTemplate;
import de.neuland.jade4j.template.TemplateLoader;

import org.jetbrains.annotations.NotNull;
import org.pegdown.PegDownProcessor;

import tekgenesis.common.collections.Maps;
import tekgenesis.common.core.DateTimeBase;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.util.LruCache;
import tekgenesis.form.WebResourceManager;

import static java.lang.Boolean.getBoolean;
import static java.text.DateFormat.SHORT;
import static java.text.DateFormat.getDateInstance;
import static java.text.NumberFormat.getCurrencyInstance;
import static java.text.NumberFormat.getNumberInstance;

import static org.pegdown.Extensions.ALL;
import static org.pegdown.Extensions.TASKLISTITEMS;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Constants.HTML_DIR;
import static tekgenesis.common.core.Constants.JADE_EXT;
import static tekgenesis.common.core.Constants.SUIGEN_DEVMODE;
import static tekgenesis.common.core.Strings.split;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.util.JavaReservedWords.PUBLIC;
import static tekgenesis.security.shiro.web.URLConstants.DEFAULT_FAVICON;
import static tekgenesis.security.shiro.web.URLConstants.DEFAULT_TEKGENESIS_FAVICON;
import static tekgenesis.security.shiro.web.URLConstants.DEFAULT_TEKGENESIS_THEME;
import static tekgenesis.security.shiro.web.URLConstants.DEFAULT_THEME;
import static tekgenesis.service.html.MustacheTemplate.CDN_HOST;
import static tekgenesis.service.html.MustacheTemplate.CONCAT_SERVLET;
import static tekgenesis.service.html.MustacheTemplate.sgComponentsVersions;

/**
 * Jade html template processor.
 */
@ParametersAreNonnullByDefault class Jade4jTemplate {

    //~ Instance Fields ..............................................................................................................................

    private final JadeConfiguration config;

    private final LruCache<String, JadeTemplate> templates;

    //~ Constructors .................................................................................................................................

    private Jade4jTemplate() {
        templates = LruCache.createLruCache(1000);

        config = new JadeConfiguration();
        config.setTemplateLoader(new HtmlPathTemplateLoader());
        config.setSharedVariables(Maps.hashMap(tuple("sg", new SgHelper())));
        config.setFilter("md", new MarkdownFilter());
        config.setFilter("json", new JsonFilter());
        config.setFilter("sgFormsBootstrap", new FormBootstrapFilter());
        config.setCaching(false);  // using custom template cache
        config.setPrettyPrint(true);
    }

    //~ Methods ......................................................................................................................................

    /** Process and write the template to the provided writer. */
    public void render(final HtmlInstance html, final Writer out)
        throws IOException
    {
        final JadeTemplate template = getTemplate(html);
        config.renderTemplate(template, html.getContext(), out);
    }

    private synchronized JadeTemplate compileTemplate(@NotNull final HtmlInstance html)
        throws IOException
    {
        JadeTemplate template = templates.get(html.key());
        if (template == null) {
            template = createJadeTemplate(html);
            if (!getBoolean(SUIGEN_DEVMODE)) templates.put(html.key(), template);
        }
        return template;
    }

    private JadeTemplate createJadeTemplate(@NotNull final HtmlInstance html)
        throws IOException
    {
        final TemplateLoader templateLoader = new TemplateLoader() {
                @Override public long getLastModified(final String name) {
                    return 0;
                }
                @Override public Reader getReader(final String name)
                    throws IOException
                {
                    if (name.equals(html.key())) return html.provider().reader();
                    return config.getTemplateLoader().getReader(name);
                }
            };

        final Parser       parser   = new Parser(html.key(), templateLoader, config.getExpressionHandler());
        final JadeTemplate template = new JadeTemplate();
        template.setExpressionHandler(config.getExpressionHandler());
        template.setTemplateLoader(config.getTemplateLoader());
        template.setPrettyPrint(config.isPrettyPrint());
        template.setMode(config.getMode());
        template.setRootNode(parser.parse());
        return template;
    }

    private JadeTemplate getTemplate(@NotNull final HtmlInstance html)
        throws IOException
    {
        final JadeTemplate template = templates.get(html.key());
        return template == null ? compileTemplate(html) : template;
    }

    //~ Methods ......................................................................................................................................

    /** Return singleton instance. */
    public static Jade4jTemplate getInstance() {
        return Holder.instance;
    }

    private static Locale locale() {
        return Context.getContext().getLocale();
    }

    //~ Inner Classes ................................................................................................................................

    public class FormBootstrapFilter implements Filter {
        @Override public String convert(String source, List<Attr> attributes, Map<String, Object> model) {
            return MustacheTemplate.formBootstrap();
        }
    }

    private static class Holder {
        private static final Jade4jTemplate instance = new Jade4jTemplate();
    }

    private static class HtmlPathTemplateLoader implements TemplateLoader {
        @Override public long getLastModified(final String name) {
            return 0;
        }

        @Override public Reader getReader(final String path) {
            String fixedPath = path.trim();
            if (!path.endsWith(JADE_EXT)) fixedPath += "." + JADE_EXT;
            if (!path.startsWith("/" + HTML_DIR) && !path.startsWith(HTML_DIR)) fixedPath = HTML_DIR + fixedPath;

            final Option<Reader> reader = HtmlSourceProvider.HtmlPath.reader(fixedPath);
            if (reader.isEmpty()) throw new IllegalArgumentException("Html Jade include source not found: " + fixedPath);
            return reader.get();
        }
    }

    public class JsonFilter implements Filter {
        @Override public String convert(String source, List<Attr> attributes, Map<String, Object> model) {
            final Object o = model.get(source);
            return o != null ? JsonMapping.toJson(o) : "";
        }
    }

    public class MarkdownFilter extends CachingFilter {
        private final PegDownProcessor pegdown = new PegDownProcessor(ALL | TASKLISTITEMS);

        @Override protected String convert(String source, List<Attr> attributes) {
            return pegdown.markdownToHtml(source);
        }
    }

    /**
     * Jade helper class, added as a shared variable.
     */
    public static class SgHelper {
        /** Include css (deprecate, usa sha). */
        public String css(String path) {
            return resource("css", path);
        }

        /** Include theme. */
        public String cssTheme() {
            return cssTheme("");
        }

        /** Include theme. */
        public String cssTheme(String path) {
            return customOrDefault(path, DEFAULT_THEME, DEFAULT_TEKGENESIS_THEME);
        }

        /** Format currency. */
        public String currency(Number v) {
            return currency(new BigDecimal(v.doubleValue()));
        }

        /** Format string currency. */
        public String currency(String v) {
            return currency(new BigDecimal(v));
        }

        /** Format BigDecimal currency. */
        public String currency(BigDecimal v) {
            return getCurrencyInstance(locale()).format(v);
        }

        /** Returns path to favicon. */
        public String favicon() {
            return favicon("");
        }

        /** Returns path to favicon. */
        public String favicon(String path) {
            return customOrDefault(path, DEFAULT_FAVICON, DEFAULT_TEKGENESIS_FAVICON);
        }

        /** Format dates. */
        public String format(DateTimeBase<?> v) {
            return v.format(getDateInstance(SHORT, locale()));
        }

        /** Format number. */
        public String format(Number v) {
            return getNumberInstance(locale()).format(v);
        }

        /** Include img (deprecate, usa sha). */
        public String img(String path) {
            return resource("img", path);
        }

        /** Include js (deprecate, usa sha). */
        public String js(String path) {
            return resource("js", path);
        }

        /** Include resource (deprecate, usa sha). */
        public String resource(String path) {
            return resource("", path);
        }

        /** Transform public resource path into sha. */
        public String sha(String path) {
            return resource(PUBLIC, path);
        }

        /** Returns component versions. */
        public String versions() {
            return sgComponentsVersions();
        }

        private String customOrDefault(final String path, final String customPath, final String defaultPath) {
            final String result = resource(path);
            if (!result.isEmpty()) return result;

            final String themeSha = getSha(customPath);
            return customPath.equals(themeSha) ? getSha(defaultPath) : themeSha;
        }

        private String resource(String prefix, String path) {
            if (isEmpty(path)) return "";

            final String realPath = path.startsWith("/") ? prefix + path.trim() : prefix + "/" + path.trim();

            String result = CDN_HOST;
            if (!realPath.contains(",")) result += getSha(realPath);
            else result += split(realPath, ',').map(SgHelper::getSha).mkString(CONCAT_SERVLET, "&", "");

            return result;
        }

        private static String getSha(String path) {
            return WebResourceManager.getInstance().shaPath(path);
        }
    }  // end class SgHelper
}  // end class Jade4jTemplate
