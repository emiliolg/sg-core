
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.exceptions.JadeException;
import de.neuland.jade4j.parser.node.AttrsNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.collections.*;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.exception.ViewsInvalidArgumentTypeException;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Files;
import tekgenesis.metadata.entity.StructType;
import tekgenesis.metadata.handler.Routes;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.*;

import static java.io.File.separatorChar;
import static java.lang.Character.isJavaIdentifierPart;
import static java.util.Collections.singletonList;

import static javax.xml.namespace.QName.valueOf;
import static javax.xml.stream.XMLStreamConstants.DTD;
import static javax.xml.stream.XMLStreamConstants.START_DOCUMENT;

import static tekgenesis.codegen.common.MMCodeGenConstants.BUILDER;
import static tekgenesis.codegen.common.MMCodeGenConstants.BUILD_METHOD;
import static tekgenesis.codegen.common.MMCodeGenConstants.DUPLICATED_STRING;
import static tekgenesis.codegen.common.MMCodeGenConstants.HTML_BUILDER_CLASS;
import static tekgenesis.codegen.common.MMCodeGenConstants.HTML_FACTORY_CLASS;
import static tekgenesis.codegen.common.MMCodeGenConstants.HTML_METHOD;
import static tekgenesis.codegen.common.MMCodeGenConstants.JADE_METHOD;
import static tekgenesis.codegen.common.MMCodeGenConstants.MSG;
import static tekgenesis.codegen.common.MMCodeGenConstants.MUSTACHE_METHOD;
import static tekgenesis.codegen.common.MMCodeGenConstants.PARAM_METHOD;
import static tekgenesis.codegen.common.MMCodeGenConstants.STRING_METHOD;
import static tekgenesis.codegen.common.MMCodeGenConstants.STRUCT_METHOD;
import static tekgenesis.codegen.common.MMCodeGenConstants.WEAKER_ACCESS;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.collections.MultiMap.createSortedMultiMap;
import static tekgenesis.common.core.Constants.*;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.util.Files.toSystemIndependentName;
import static tekgenesis.type.Types.nullType;
import static tekgenesis.type.Types.referenceType;

/**
 * Html template factory class generator.
 */
public class HtmlFactoryCodeGenerator extends ClassGenerator implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final Iterable<HtmlTemplate> templates;

    //~ Constructors .................................................................................................................................

    private HtmlFactoryCodeGenerator(JavaCodeGenerator cg, String viewName, Iterable<HtmlTemplate> templates) {
        super(cg, viewName);
        this.templates = templates;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getSourceName() {
        return getName();
    }

    @Override protected void populate() {
        withComments(CLASS_COMMENT);
        suppressWarnings(WEAKER_ACCESS, DUPLICATED_STRING);
        withSuperclass(HTML_FACTORY_CLASS);

        final Field builder = field(BUILDER, HTML_BUILDER_CLASS).notNull().asPrivate().asFinal();

        constructor().asPackage().arg(builder).superArg();

        // Add method for each template
        for (final HtmlTemplate template : templates) {
            final String name   = template.getIdentifier();
            final String path   = quoted(toSystemIndependentName(separatorChar + template.getPath()));
            final Method method = method(name, HTML_CLASS).notNull();
            method.withComments(VIEW_COMMENT + path);

            String fluent = invoke(builder.getName(), template.getBuildMethod(), path);

            for (final Tuple<String, Type> parameter : template.getParameters()) {
                final String param = parameter.first();
                final Type   type  = parameter.second();
                method.arg(param, extractImport(type.getImplementationClassName())).notNull();
                fluent = invoke(fluent, template.getParamMethod(type), quoted(param), param);
            }

            for (final Tuple<String, String> message : template.getMessages())
                fluent = invoke(fluent, MSG, quoted(message.first()), quoted(message.second()));

            method.return_(invoke(fluent, BUILD_METHOD));
        }

        super.populate();
    }

    //~ Methods ......................................................................................................................................

    /** Generates the user classes to use sui-view templates. */
    public static List<File> generate(ModelRepository repository, File generatedSourcesDir, File htmlDir) {
        final MultiMap<String, HtmlTemplate> templates = createSortedMultiMap(HtmlTemplate::compareTo);

        for (final String file : Files.list(htmlDir, f -> TEMPLATE_EXTS.contains(Files.extension(f)))) {
            final HtmlTemplate template = HtmlTemplate.Factory.forExtension(repository, toSystemIndependentName(file));
            for (final String factory : template.getClassName())
                templates.put(factory, template);
        }

        final List<File> result = new ArrayList<>();
        for (final String factory : templates.keys()) {
            final tekgenesis.common.core.QName name = createQName(factory);
            final JavaCodeGenerator            cg   = new JavaCodeGenerator(generatedSourcesDir, name.getQualification());
            final HtmlFactoryCodeGenerator     cvg  = new HtmlFactoryCodeGenerator(cg, name.getName(), templates.get(factory));
            cvg.generate();

            result.add(cvg.getTargetFile());
        }
        return result;
    }

    /** Returns the method identifier for an html path. */
    public static String resolveIdentifier(final String path) {
        final String        route  = path.substring(path.indexOf(Constants.HTML_DIR) + Constants.HTML_DIR.length(), path.lastIndexOf("."));
        final Seq<String>   paths  = Routes.split(route);
        final StringBuilder result = new StringBuilder(toJavaIdentifier(paths.getFirst().get()));
        for (final String section : paths.drop(1))
            result.append(capitalizeFirst(toJavaIdentifier(section)));
        return result.toString();
    }

    /** Prune all non-java identifier characters. */
    private static String toJavaIdentifier(final String input) {
        if (isEmpty(input)) return input;
        String result = "";
        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);
            if (isJavaIdentifierPart(c)) result += c;
        }
        return result;
    }

    //~ Static Fields ................................................................................................................................

    private static final String CLASS_COMMENT = "Generated factory class to build html templates for handlers.";
    private static final String VIEW_COMMENT  = "Returns an Html instance for template located in ";

    public static final String DEFAULT_CLASS_NAME          = "sui.Views";
    public static final QName  SUI_VIEW                    = valueOf("sui-view");
    private static final QName SG_VIEW                     = valueOf("sg-view");
    public static final QName  SUI_PARAMS                  = valueOf("sui-params");
    public static final QName  SG_PARAMS                   = valueOf("sg-params");
    public static final QName  SUI_MESSAGES                = valueOf("sui-messages");
    private static final QName SG_MESSAGES                 = valueOf("sg-messages");
    public static final String VIEWS_INVALID_ARGUMENT_TYPE = "Invalid type '%s' for argument '%s'";

    private static final Logger logger = Logger.getLogger(HtmlFactoryCodeGenerator.class);

    //~ Inner Classes ................................................................................................................................

    private static class DTDFilter implements EventFilter {
        @Override public boolean accept(XMLEvent event) {
            return event.getEventType() != DTD && event.getEventType() != START_DOCUMENT;
        }
    }

    private abstract static class HtmlTemplate implements Comparable<HtmlTemplate> {
        protected final String                    fullPath;
        private Option<String>                    className;
        private final String                      identifier;
        private final List<Tuple<String, String>> messages;
        private final List<Tuple<String, Type>>   parameters;
        private final String                      path;
        private final ModelRepository             repository;

        private HtmlTemplate(ModelRepository repository, String fullPath) {
            this.repository = repository;
            this.fullPath   = fullPath;
            path            = fullPath.substring(fullPath.indexOf(Constants.HTML_DIR));
            parameters      = new ArrayList<>();
            messages        = new ArrayList<>();
            identifier      = resolveIdentifier(path);
            className       = Option.empty();
        }

        @Override public int compareTo(@NotNull final HtmlTemplate o) {
            return getIdentifier().compareTo(o.getIdentifier());
        }

        @Override public boolean equals(final Object obj) {
            return obj instanceof HtmlTemplate && compareTo((HtmlTemplate) obj) == 0;
        }

        @Override public int hashCode() {
            return identifier.hashCode();
        }

        protected ViewsInvalidArgumentTypeException invalidArgumentType(@NotNull List<String> errors, int columnNumber, int lineNumber) {
            final String cause = "Invalid argument types in file " + fullPath;
            return new ViewsInvalidArgumentTypeException(cause, errors, fullPath, columnNumber, lineNumber);
        }

        protected void parseSgMessages(final String msgAttr) {
            for (final String param : split(msgAttr, ',')) {
                final ImmutableList<String> parts     = split(param, ':');
                final Type                  typeValue = resolveMessagesType(parts);
                messages.add(tuple(parts.get(0).trim(), typeValue.toString()));
            }
        }

        protected List<String> parseSgParams(final String paramsValue) {
            final List<String> errors = new ArrayList<>();
            for (final String param : split(paramsValue, ',')) {
                final ImmutableList<String> parts = split(param, ':');
                final Type                  type  = resolveParameterType(parts);

                if (type.isNull()) errors.add(String.format(VIEWS_INVALID_ARGUMENT_TYPE, parts.get(1).trim(), parts.get(0).trim()));
                else parameters.add(tuple(parts.get(0).trim(), type));
            }
            return errors;
        }

        /** Defined option if a valid parse was performed. */
        protected abstract Option<String> parseSgView();

        protected Type resolveMessagesType(ImmutableList<String> parts) {
            if (parts.size() != 2) return nullType();
            return attemptTypeFromRepository(parts.get(1).trim(), EnumType.class);
        }

        @NotNull protected Type resolveParameterType(@NotNull List<String> parts) {
            final String  typePart = parts.size() > 1 ? parts.get(1).trim() : STRING;
            final boolean multiple = typePart.endsWith("*");
            final String  typeName = multiple ? typePart.substring(0, typePart.length() - 1) : typePart;
            Type          type     = Types.fromString(typeName);
            if (type.isNull()) {
                if (Types.htmlType().toString().equals(typeName)) type = Types.htmlType();
                else {
                    type = attemptTypeFromRepository(typeName, StructType.class);
                    if (type.isNull()) type = Types.referenceType(typeName);
                }
            }
            return multiple ? Types.arrayType(type) : type;
        }  // end method resolveParameterType

        protected abstract String getBuildMethod();
        protected abstract String getParamMethod(Type type);

        private <T extends MetaModel> Type attemptTypeFromRepository(String typeName, Class<T> c) {
            return repository.getModel(createQName(typeName), c)
                   .map((Function<MetaModel, Type>) mm ->
                        referenceType(mm.getFullName()))
                   .orElse(Types.nullType());
        }

        private HtmlTemplate parse() {
            className = parseSgView();
            return this;
        }

        private Option<String> getClassName() {
            return className;
        }

        private String getIdentifier() {
            return identifier;
        }

        private List<Tuple<String, String>> getMessages() {
            return messages;
        }

        private List<Tuple<String, Type>> getParameters() {
            return parameters;
        }

        private String getPath() {
            return path;
        }

        private enum Factory {
            MUSTACHE(MUSTACHE_EXT) {
                @Override HtmlTemplate.Mustache create(final ModelRepository repo, final String file) {
                    return new HtmlTemplate.Mustache(repo, file);
                }},
            JADE(JADE_EXT) {
                @Override HtmlTemplate.Jade create(final ModelRepository repo, final String file) { return new HtmlTemplate.Jade(repo, file); }},
            XHTML(XHTML_EXT) {
                @Override HtmlTemplate.Xhtml create(final ModelRepository repo, final String file) { return new HtmlTemplate.Xhtml(repo, file); }};

            private final String extension;

            Factory(String extension) {
                this.extension = extension;
            }

            abstract HtmlTemplate create(final ModelRepository repository, final String fullPath);

            /** Create HtmlTemplate based on file extension. */
            static HtmlTemplate forExtension(final ModelRepository repository, final String fullPath) {
                final String  extension = Files.extension(fullPath);
                final Factory result    = factoryByExtension.get(extension);
                if (result == null) throw new IllegalArgumentException("No template engine for extension: " + extension);
                return result.create(repository, fullPath).parse();
            }

            private static final Map<String, Factory> factoryByExtension = Maps.map(ImmutableList.fromArray(values()), f -> tuple(f.extension, f));
        }

        private static class Jade extends HtmlTemplate {
            private final JadeConfiguration config;

            private Jade(final ModelRepository repository, final String fullPath) {
                super(repository, fullPath);
                config = new JadeConfiguration();
                config.setCaching(false);
            }

            /** Defined option if a valid parse was performed. */
            @Override protected Option<String> parseSgView() {
                try {
                    return filter(config.getTemplate(fullPath).getRootNode().getNodes(), AttrsNode.class).getFirst().map(this::parseAttr);
                }
                catch (final JadeException e) {
                    logger.error(e);
                    final String cause = "Invalid jade template: " + fullPath;
                    throw new ViewsInvalidArgumentTypeException(cause, singletonList(e.getMessage()), fullPath, 0, e.getLineNumber());
                }
                catch (final IOException e) {
                    logger.error(e);
                    final String cause = "Jade template error: " + fullPath;
                    throw new ViewsInvalidArgumentTypeException(cause, singletonList(e.getMessage()), fullPath, 0, 0);
                }
            }

            @Override protected String getBuildMethod() {
                return JADE_METHOD;
            }

            @Override protected String getParamMethod(Type type) {
                return PARAM_METHOD;
            }

            @Nullable private String parseAttr(AttrsNode attr) {
                final String sgView = attr.getAttribute(SG_VIEW.getLocalPart());
                if (!isNotEmpty(sgView)) return null;
                final String sgParams = attr.getAttribute(SG_PARAMS.getLocalPart());
                if (isNotEmpty(sgParams)) {
                    final List<String> errors = parseSgParams(sgParams);
                    if (!errors.isEmpty()) throw invalidArgumentType(errors, 0, attr.getLineNumber());
                }

                final String sgMessages = attr.getAttribute(SG_MESSAGES.getLocalPart());
                if (isNotEmpty(sgMessages)) parseSgMessages(sgMessages);

                return notEmpty(sgView, DEFAULT_CLASS_NAME);
            }
        }  // end class Jade

        private static class Mustache extends HtmlTemplate.Xhtml {
            private Mustache(final ModelRepository repository, final String fullPath) {
                super(repository, fullPath);
            }

            @Override protected String getBuildMethod() {
                return MUSTACHE_METHOD;
            }

            @Override protected String getParamMethod(final Type type) {
                return PARAM_METHOD;
            }
        }

        private static class Xhtml extends HtmlTemplate {
            private final XMLInputFactory xmlFactory;

            private Xhtml(final ModelRepository repository, final String fullPath) {
                super(repository, fullPath);
                xmlFactory = XMLInputFactory.newInstance();
            }

            /** Defined option if a valid parse was performed. */
            @Override protected Option<String> parseSgView() {
                try {
                    final FileInputStream in     = new FileInputStream(new File(fullPath));
                    final XMLEventReader  reader = xmlFactory.createFilteredReader(xmlFactory.createXMLEventReader(in), new DTDFilter());

                    final XMLEvent peek = reader.peek();
                    if (peek != null && peek.isStartElement()) {
                        final StartElement element = reader.nextEvent().asStartElement();
                        Attribute          view    = element.getAttributeByName(SG_VIEW);
                        if (view == null) view = element.getAttributeByName(SUI_VIEW);
                        if (view != null) {
                            Attribute params = element.getAttributeByName(SG_PARAMS);
                            if (params == null) params = element.getAttributeByName(SUI_PARAMS);
                            if (params != null) {
                                final List<String> errors = parseSgParams(params.getValue());

                                if (!errors.isEmpty()) {
                                    final Location location = element.getLocation();
                                    throw invalidArgumentType(errors,
                                        location != null ? location.getColumnNumber() : 0,
                                        location != null ? location.getLineNumber() : 0);
                                }
                            }

                            Attribute as = element.getAttributeByName(SG_MESSAGES);
                            if (as == null) as = element.getAttributeByName(SUI_MESSAGES);
                            if (as != null) parseSgMessages(as.getValue());

                            return some(notEmpty(view.getValue(), DEFAULT_CLASS_NAME));
                        }
                    }
                }
                catch (final XMLStreamException e) {
                    logger.error(e);
                    final String   cause    = "Invalid xml template: " + fullPath;
                    final Location location = e.getLocation();
                    throw new ViewsInvalidArgumentTypeException(cause,
                        singletonList(e.getMessage()),
                        fullPath,
                        location != null ? location.getColumnNumber() : 0,
                        location != null ? location.getLineNumber() : 0);
                }
                catch (final FileNotFoundException e) {
                    logger.error(e);
                    final String cause = "Template not found: " + fullPath;
                    throw new ViewsInvalidArgumentTypeException(cause, singletonList(e.getMessage()), fullPath, 0, 0);
                }

                logger.info(String.format("Html '%s' contains no sg-view attribute. No parsing was performed.", fullPath));
                return Option.empty();
            }  // end method parseSgView

            @Override protected String getBuildMethod() {
                return HTML_METHOD;
            }

            @Override protected String getParamMethod(final Type type) {
                final Type finalType = type.isArray() ? ((ArrayType) type).getElementType() : type;
                return finalType.isString() ? STRING_METHOD : finalType.isHtml() ? HTML_METHOD : STRUCT_METHOD;
            }
        }  // end class Xhtml
    }  // end class HtmlTemplate
}  // end class HtmlFactoryCodeGenerator
