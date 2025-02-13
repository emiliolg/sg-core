
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
import java.util.*;
import java.util.function.Supplier;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.WebResourceManager;
import tekgenesis.service.Forwarder;
import tekgenesis.service.HtmlResultHandler;
import tekgenesis.type.Struct;

import static java.lang.String.format;
import static java.util.Map.Entry;

import static javax.xml.namespace.QName.valueOf;

import static tekgenesis.codegen.html.HtmlFactoryCodeGenerator.*;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Constants.*;
import static tekgenesis.common.core.Strings.toCamelCase;
import static tekgenesis.common.env.context.Context.getProperties;
import static tekgenesis.common.json.JsonMapping.jsonHtmlSafe;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.common.util.JavaReservedWords.CLASS;
import static tekgenesis.service.html.MustacheTemplate.CDN_HOST;
import static tekgenesis.service.html.MustacheTemplate.CONCAT_SERVLET;

/**
 * Xhtml template rendering.
 */
class XHtmlTemplate {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Supplier<Forwarder> forwarder;
    private Scope                              scope;
    private final List<String>                 templateSources;
    private final XMLEventWriter               writer;

    //~ Constructors .................................................................................................................................

    private XHtmlTemplate(@NotNull Writer writer, @NotNull Supplier<Forwarder> forwarder)
        throws XMLStreamException
    {
        this(XMLOutputFactory.newInstance().createXMLEventWriter(writer), forwarder);
    }

    private XHtmlTemplate(@NotNull final XMLEventWriter writer, @NotNull final Supplier<Forwarder> forwarder) {
        this.writer     = writer;
        this.forwarder  = forwarder;
        templateSources = new ArrayList<>();
        scope           = new Scope();
    }

    //~ Methods ......................................................................................................................................

    private void add(XMLEvent event)
        throws XMLStreamException
    {
        writer.add(event);
    }

    private boolean attemptToWriteContent(HtmlInstance.Xhtml view)
        throws XMLStreamException, IOException
    {
        final byte[] stream = getXHtml(view);
        if (stream == null) return false;
        writeContent(view, stream);
        return true;
    }

    private void attribute(String name, String value)
        throws XMLStreamException
    {
        add(XML_EVENT_FACTORY.createAttribute(name, value));
    }

    private void cdata(String content)
        throws XMLStreamException
    {
        add(XML_EVENT_FACTORY.createCData(content));
    }

    private void characters(String content)
        throws XMLStreamException
    {
        add(XML_EVENT_FACTORY.createCharacters(content));
    }

    private void close()
        throws XMLStreamException
    {
        writeSuiCache();  // write if document was not closed

        writer.flush();
        writer.close();
    }

    private void end(String name)
        throws XMLStreamException
    {
        add(XML_EVENT_FACTORY.createEndElement("", null, name));
    }

    /** Terminates the current line by writing the line separator string. */
    private void line()
        throws XMLStreamException
    {
        add(BREAK_LINE);
    }

    private void putStringValueOrSequence(ObjectNode scopeNode, Entry<String, Object> entry) {
        final Object value = entry.getValue();
        if (value instanceof Seq) {
            final ArrayNode array = scopeNode.putArray(entry.getKey());
            for (final Object element : (Seq<?>) value) {
                if (element instanceof String) array.add((String) element);
            }
        }
        else if (value instanceof String) scopeNode.put(entry.getKey(), (String) value);
    }

    private void putStructValueOrSequence(ObjectNode scopeNode, Entry<String, Object> entry) {
        final Object value = entry.getValue();
        if (value instanceof Seq) {
            final ArrayNode array = scopeNode.putArray(entry.getKey());
            for (final Object element : (Seq<?>) value) {
                if (element instanceof Struct) array.addPOJO(element);
            }
        }
        else if (value instanceof Struct) scopeNode.putPOJO(entry.getKey(), value);
    }

    private String replaceMacros(String data) {
        String result = data;

        for (final Macro macro : Macro.values()) {
            int          from        = 0;
            final String start       = macro.getStart();
            final int    startLength = start.length();
            final String end         = macro.getEnd();
            final int    endLength   = end.length();
            while (from > -1) {
                from = result.indexOf(start, from);
                if (from > -1) {
                    final int to = result.indexOf(end, from);
                    if (to > -1) {
                        final String                value     = result.substring(from + startLength, to).trim();
                        final ImmutableList<String> values    = Strings.split(value, ',');
                        final String                transform = values.size() > 1 ? macro.transform(forwarder, values)
                                                                                  : macro.transform(forwarder, value);
                        result = result.substring(0, from) + transform + result.substring(to + endLength);
                        from   = to;
                    }
                    else from++;
                }
            }
        }
        return result;
    }

    private String replaceParams(String data, HtmlInstance html) {
        String result = data;
        for (final Entry<String, Object> e : html.getParameters().entrySet()) {
            if (e.getValue() instanceof String) result = result.replace(REF_PARAM + e.getKey(), (String) e.getValue());
        }
        return result;
    }

    private String replaceReferences(String data, HtmlInstance html) {
        return replaceMacros(replaceParams(data, html));
    }

    private void start(String name)
        throws XMLStreamException
    {
        add(XML_EVENT_FACTORY.createStartElement("", null, name));
    }

    private void writeContent(HtmlInstance.Xhtml html, byte[] bytes)
        throws XMLStreamException, IOException
    {
        scope = scope.next(html);

        final XMLEventReader reader = XML_INPUT_FACTORY.createXMLEventReader(new ByteArrayInputStream(bytes));

        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();

            if (event.isCharacters()) {
                final Characters content = event.asCharacters();
                if (content.isWhiteSpace()) add(event);
                else characters(replaceReferences(content.getData(), html));
            }
            else if (event.isStartElement()) writeStartElement(event.asStartElement(), html);
            else if (event.isEndElement()) {
                final EndElement endElement = event.asEndElement();
                final QName      tagName    = endElement.getName();
                final boolean    endingHead = HEAD_ELEM.equals(tagName);
                final boolean    endingBody = BODY_ELEM.equals(tagName);

                if (endingHead) writeMetadata(html);
                else if (endingBody || !reader.hasNext()) {
                    writeSuiTemplates(html);
                    writeSuiCache();
                }
                if (!SKIPPED_TAGS.contains(tagName)) add(event);
            }
        }

        scope = scope.previous();
    }  // end method writeContent

    private void writeDtd()
        throws XMLStreamException
    {
        add(XML_EVENT_FACTORY.createDTD(HTML_DOC_TYPE));
        line();
    }

    private void writeMetadata(HtmlInstance.Xhtml html)
        throws XMLStreamException
    {
        for (final Tuple<String, String> metadata : html.getMetadata()) {
            line();
            start(META_TAG);
            attribute(PROPERTY_ATTR, metadata.first());
            attribute(CONTENT_ATTR, metadata.second());
            end(META_TAG);
        }
    }

    private void writeScopeContent(@NotNull final HtmlInstance.Xhtml html, @NotNull final ObjectNode scopeNode) {
        for (final Entry<String, Object> entry : html.getParameters().entrySet())
            putStringValueOrSequence(scopeNode, entry);

        for (final Entry<String, Object> entry : html.getStructs().entrySet())
            putStructValueOrSequence(scopeNode, entry);

        for (final Entry<String, Map<String, String>> entry : html.getMessages().entrySet()) {
            final ObjectNode enumeration = scopeNode.putObject(entry.getKey());
            for (final Entry<String, String> e : entry.getValue().entrySet())
                enumeration.put(e.getKey(), e.getValue());
        }
    }

    private void writeScopesIntoCache(@NotNull final ObjectNode cacheNode) {
        for (final Scope s : scope.getScopes()) {
            if (s.isReferenced()) {
                final ObjectNode scopeNode = cacheNode.putObject(s.key());
                writeScopeContent(s.html, scopeNode);
            }
        }
    }

    private void writeStartElement(StartElement elem, HtmlInstance.Xhtml html)
        throws IOException, XMLStreamException
    {
        final QName                 tagName = elem.getName();
        final Map<QName, Attribute> attrMap = new LinkedHashMap<>();

        final Iterator<Attribute> attributes = cast(elem.getAttributes());
        while (attributes.hasNext()) {
            final Attribute attribute = attributes.next();
            final String    value     = replaceReferences(attribute.getValue(), html);
            final QName     name      = attribute.getName();

            // Include html fragments
            if (SUI_INCLUDE_TAG.equals(tagName) && name.equals(SRC_ATTR)) {
                final Iterator<Attribute> include = cast(elem.getAttributes());
                writeSuiInclude(value, html, include);
            }
            // Include angular templates
            else if (SUI_TEMPLATE_TAG.equals(tagName) && name.equals(SRC_ATTR)) templateSources.add(value);

            if (SCOPED_ATTRIBUTES.contains(name)) {
                // Register scope to be included on page cache. Add sui-init directive for injection.
                if (!attrMap.containsKey(SUI_SCOPE_ATTR) && elem.getAttributeByName(SUI_VIEW) != null)
                    attrMap.put(SUI_SCOPE_ATTR, XML_EVENT_FACTORY.createAttribute(SUI_SCOPE_ATTR, scope.register()));
            }
            else if (SUI_VIEW.equals(name)) {
                // Mark as referenced only if referenced from a concrete element, not a disposable sui-view.
                if (!SUI_VIEW.equals(tagName)) scope.referenced();
            }
            else attrMap.put(name, XML_EVENT_FACTORY.createAttribute(name, value));
        }

        if (!SKIPPED_TAGS.contains(tagName)) add(XML_EVENT_FACTORY.createStartElement(tagName, attrMap.values().iterator(), elem.getNamespaces()));
    }

    private void writeSuiCache()
        throws XMLStreamException
    {
        if (scope.isEmpty()) return;

        line();
        start(SCRIPT);
        attribute(CLASS, SUI_SCOPES_CACHE);
        line();
        characters("//");

        final StringBuilder result = new StringBuilder("\n");

        writeSuiScopes(result);

        result.append("\n//");

        cdata(result.toString());
        line();
        end(SCRIPT);
        line();

        scope.clear();
    }

    private void writeSuiInclude(String src, HtmlInstance.Xhtml html, @Nullable Iterator<Attribute> attributes)
        throws IOException, XMLStreamException
    {
        try {
            final HtmlInstance.Xhtml view;
            if (src.startsWith(REF_PARAM)) view = html.getView(src.replace(REF_PARAM, ""));
            else {
                String fixedPath = src.trim();
                if (fixedPath.startsWith("/")) fixedPath = fixedPath.substring(1);
                if (!fixedPath.startsWith(HTML_DIR)) fixedPath = HTML_DIR + fixedPath;
                if (!fixedPath.endsWith(XHTML_EXT)) fixedPath += "." + XHTML_EXT;

                final HtmlInstanceBuilder.Xhtml htmlBuilder = Context.getSingleton(HtmlBuilder.class).html(fixedPath);
                if (attributes != null) {
                    while (attributes.hasNext()) {
                        final Attribute attribute = attributes.next();
                        if (!SRC_ATTR.equals(attribute.getName())) {
                            final String attrValue = replaceReferences(attribute.getValue(), html);
                            htmlBuilder.str(attribute.getName().toString(), attrValue);
                        }
                    }
                }

                view = (HtmlInstance.Xhtml) htmlBuilder.build();
            }

            if (!attemptToWriteContent(view)) {
                final HtmlResultHandler delegate = new HtmlResultHandler();
                forwarder.get().forward(src, delegate, true);
                if (delegate.hasHtmlInstance()) attemptToWriteContent((HtmlInstance.Xhtml) delegate.getHtmlInstance());
            }
        }
        catch (final RuntimeException e) {
            logger.error(String.format("Processing %s an error occurred with this sui-include: %s", html.key(), src), e);
        }
    }  // end method writeSuiInclude

    private void writeSuiScopes(StringBuilder result) {
        result.append("var ").append(SUI_SCOPES_CACHE).append(" = ");
        final ObjectMapper mapper = JSON_HTML_SAFE;
        final ObjectNode   cache  = mapper.createObjectNode();
        writeScopesIntoCache(cache);
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            mapper.writeValue(out, cache);
            result.append(out.toString(UTF8)).append(";");
        }
        catch (final IOException e) {
            logger.error(e);
            throw new UncheckedIOException(e);
        }
    }

    private void writeSuiTemplates(HtmlInstance.Xhtml context)
        throws XMLStreamException, IOException
    {
        for (final String src : templateSources) {
            line();
            start(SCRIPT);
            attribute(TYPE, "text/ng-template");
            attribute(ID, src);
            line();

            writeSuiInclude(src, context, null);

            end(SCRIPT);
            line();
        }
    }

    //~ Methods ......................................................................................................................................

    /** Process InputStream with context outputting the result on the given OutputStream. */
    static void process(@NotNull final HtmlInstance.Xhtml html, @NotNull Writer writer, @NotNull final Supplier<Forwarder> forwarder)
        throws IOException
    {
        final byte[] xHtml = getXHtml(html);
        if (xHtml != null) writer.write(process(html, forwarder, xHtml));
        else writer.write("");
    }

    private static String process(HtmlInstance.Xhtml html, Supplier<Forwarder> forwarder, byte[] bytes) {
        final StringWriter writer = new StringWriter();

        try {
            final XHtmlTemplate output = new XHtmlTemplate(writer, forwarder);
            output.writeDtd();
            output.writeContent(html, bytes);
            output.close();
        }
        catch (final XMLStreamException | IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    /** Returns the xhtml resource content. */
    @Nullable private static byte[] getXHtml(@NotNull HtmlInstance html)
        throws IOException
    {
        final Option<Reader> reader = html.provider().optionalReader();
        if (reader.isEmpty()) return null;

        final char[]        charArray    = new char[8 * 1024];
        final StringBuilder builder      = new StringBuilder();
        int                 numCharsRead;
        while ((numCharsRead = reader.get().read(charArray, 0, charArray.length)) != -1)
            builder.append(charArray, 0, numCharsRead);
        final byte[] targetArray = builder.toString().getBytes();
        reader.get().close();
        return targetArray;
    }

    //~ Static Fields ................................................................................................................................

    private static final ObjectMapper JSON_HTML_SAFE = jsonHtmlSafe();

    private static final XMLEventFactory XML_EVENT_FACTORY = XMLEventFactory.newInstance();
    private static final Characters      BREAK_LINE        = XML_EVENT_FACTORY.createCharacters("\n");
    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newInstance();

    @NonNls private static final String HTML_DOC_TYPE = "<!DOCTYPE html>";
    @NonNls private static final String REF_PARAM     = "@";
    @NonNls private static final String META_TAG      = "meta";
    @NonNls
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String         PROPERTY_ATTR = "property";
    @NonNls
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String         CONTENT_ATTR = "content";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String         PUBLIC = "/public";

    private static final QName BODY_ELEM = valueOf("body");
    private static final QName HEAD_ELEM = valueOf("head");
    private static final QName SRC_ATTR  = valueOf("src");

    private static final QName       SUI_SCOPE_ATTR    = valueOf("sui-scope");
    private static final QName       SUI_TEMPLATE_TAG  = valueOf("sui-template");
    private static final QName       SUI_INCLUDE_TAG   = valueOf("sui-include");
    private static final List<QName> SKIPPED_TAGS      = ImmutableList.of(SUI_INCLUDE_TAG, SUI_TEMPLATE_TAG, SUI_VIEW);
    private static final List<QName> SCOPED_ATTRIBUTES = ImmutableList.of(SUI_MESSAGES, SUI_PARAMS);

    private static final Logger logger = getLogger(XHtmlTemplate.class);

    //~ Enums ........................................................................................................................................

    private enum Macro {
        RESOURCE {
            @NotNull @Override String transform(Supplier<Forwarder> f, @NotNull String resourcePath) { return CDN_HOST + shaPath(resourcePath); }

            @NotNull @Override String transform(final Supplier<Forwarder> f, @NotNull Seq<String> resourcePaths) {
                return CDN_HOST + resourcePaths.map(this::shaPath).mkString(CONCAT_SERVLET, "&", "");
            }

            private String shaPath(String resourcePath) { return WebResourceManager.getInstance().shaPath(resourcePath.trim()); }},
        SHA {
            @NotNull @Override String transform(Supplier<Forwarder> f, @NotNull String resourcePath) { return CDN_HOST + shaPath(resourcePath); }

            @NotNull @Override String transform(final Supplier<Forwarder> f, @NotNull Seq<String> resourcePaths) {
                return CDN_HOST + resourcePaths.map(this::shaPath).mkString(CONCAT_SERVLET, "&", "");
            }

            private String shaPath(String resourcePath) {
                String trim = resourcePath.trim();
                if (!trim.startsWith("/")) trim = "/" + trim;
                return WebResourceManager.getInstance().shaPath(PUBLIC + trim);
            }},
        SOCIAL_LOGIN_URL {
            @NotNull @Override String transform(Supplier<Forwarder> f, @NotNull String redirectUrl) {
                return transform(f, ImmutableList.of(redirectUrl, MacroConstants.EMAIL));
            }

            @NotNull @Override String transform(Supplier<Forwarder> f, @NotNull Seq<String> paramsSeq) {
                final ImmutableList<String> params        = paramsSeq.toList();
                final String                redirectUrl   = f.get().url(format(MacroConstants.FB_LOGIN_SERVLET, params.get(0)));
                final String                s             = params.drop(1).mkString(",");
                final String                facebookAppId = notNull(getProperties(ShiroProps.class).facebookAppId);
                return format(MacroConstants.FACEBOOK_OAUTH, facebookAppId, redirectUrl, s);
            }};

        @NotNull abstract String transform(Supplier<Forwarder> f, @NotNull String value);

        @NotNull abstract String transform(Supplier<Forwarder> f, @NotNull Seq<String> value);

        @NotNull String getEnd() {
            return ")";
        }

        @NotNull String getName() {
            return toCamelCase(name());
        }

        @NotNull String getStart() {
            return REF_PARAM + getName() + "(";
        }

        private static class MacroConstants {
            @SuppressWarnings("DuplicateStringLiteralInspection")
            private static final String EMAIL            = "email";
            private static final String FACEBOOK_OAUTH   = "https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s&scope=%s";
            private static final String FB_LOGIN_SERVLET = "/sg/fb/login/%s";
        }
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Define Xhtml parsing scope. Scopes matches one-on-one with html sui-view tags.
     */
    private static class Scope {
        private final HtmlInstance.Xhtml html;
        private final int                id;
        private final Scope              previous;
        private boolean                  referenced;

        private final List<Scope> scopes;

        private Scope() {
            scopes     = new ArrayList<>();
            previous   = null;
            html       = null;
            id         = 0;
            referenced = false;
        }

        private Scope(Scope previous, HtmlInstance.Xhtml html, List<Scope> scopes) {
            this.scopes   = scopes;
            this.previous = previous;
            this.html     = html;
            id            = scopes.size();
            referenced    = false;
        }

        List<Scope> getScopes() {
            return scopes;
        }

        private void clear() {
            scopes.clear();
        }

        private String key() {
            return html.key() + '/' + id;
        }

        private Scope next(HtmlInstance.Xhtml h) {
            return new Scope(this, h, scopes);
        }

        private Scope previous() {
            return previous;
        }

        /**
         * Mark scope as referenced from sui-view attribute, rather than initialized from a sui-view
         * tag.
         */
        private void referenced() {
            referenced = true;
        }

        /** Only register scope if a service or parameters are found. */
        private String register() {
            scopes.add(this);
            return key();
        }

        /**
         * Return true if scope in referenced from sui-view attribute, rather than initialized from
         * a sui-view tag.
         */
        private boolean isReferenced() {
            return referenced;
        }

        private boolean isEmpty() {
            return scopes.isEmpty();
        }
    }  // end class Scope
}  // end class XHtmlTemplate
