
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.swagger;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.Version;
import tekgenesis.common.env.Version.ComponentInfo;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.media.Mime;
import tekgenesis.expr.Expression;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.StructType;
import tekgenesis.metadata.handler.Handler;
import tekgenesis.metadata.handler.Part;
import tekgenesis.metadata.handler.Route;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.ArrayType;
import tekgenesis.type.EnumType;
import tekgenesis.type.IntType;
import tekgenesis.type.Type;

import static java.lang.String.format;
import static java.lang.String.valueOf;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.core.Constants.INVALID;
import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.common.core.Strings.toWords;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.service.Status.*;

/**
 * Generates a swagger definition json.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class SwaggerGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final Set<StructType> definitions = new LinkedHashSet<>();
    private final Set<EnumType>   errors      = new LinkedHashSet<>();
    private final Seq<Handler>    handlers;

    private final String           host;
    private final Predicate<Route> routeFilter;
    private final Seq<String>      schemes;

    //~ Constructors .................................................................................................................................

    private SwaggerGenerator(final String host, final Seq<String> schemes, final Seq<Handler> handlers, final Predicate<Route> filter) {
        this.host    = host;
        this.schemes = schemes;
        final Predicate<Route> black = r -> r != null && !r.isInternal();
        routeFilter   = black.and(filter);
        this.handlers = handlers.filter(h -> h != null && !h.getRoutes().filter(routeFilter).isEmpty()).toList();
    }

    //~ Methods ......................................................................................................................................

    /** Writes swagger json properties on container object. */
    public ObjectNode json(final ObjectNode container) {
        meta(container);
        tags(container.putArray("tags"));
        paths(container.putObject("paths"));

        final ObjectNode defsField = container.putObject("definitions");
        definitions(defsField);
        errors(defsField);

        return container;
    }

    private void addBodyParameter(final ArrayNode container, final Type bodyType) {
        final ObjectNode param = container.addObject();
        param.put("name", "body");
        param.put("in", "body");
        param.put("description", "Payload body");
        param.put("required", true);
        addTypeInfo(param.putObject("schema"), bodyType);
    }

    private void addDefinitionRef(final StructType structType) {
        if (definitions.add(structType))
            structType.getReferences().filter(StructType.class).append(structType.getSuperTypes()).forEach(this::addDefinitionRef);
    }

    private void addInnerTypeInfo(final ObjectNode container, final Type type) {
        if (type.isType() && type instanceof StructType) {
            final StructType structType = (StructType) type;
            container.put("$ref", "#/definitions/" + structType.getFullName());
            addDefinitionRef(structType);
        }
        else if (type.isEnum() && type instanceof EnumType) {
            container.put("type", "string");
            final ArrayNode enumOptions = container.putArray("enum");
            final EnumType  enumType    = (EnumType) type;
            enumType.getIds().forEach(enumOptions::add);
            container.put("default", enumType.getDefaultValue());
        }
        else {
            final Tuple<String, String> basicTypeInfo = basicType(type);
            container.put("type", basicTypeInfo.first());
            final String format = basicTypeInfo.second();
            if (!format.isEmpty()) container.put("format", format);
        }
    }

    private void addParameter(final ArrayNode container, final String in, final TypeField field) {
        final Type       type         = field.getType().getFinalType();
        final Expression defaultValue = field.getDefaultValue();
        final boolean    hasDefault   = defaultValue.isConstant() && !defaultValue.isNull();

        final ObjectNode param = container.addObject();
        param.put("name", field.getName());
        param.put("in", in);
        addDescription(param, field.getLabel(), field.getName());
        param.put("required", field.isRequired() && !hasDefault);

        addTypeInfo(param, type, field instanceof Part && ((Part) field).isWildcard());
        if (!field.isSigned() && type.isNumber()) param.put("minimum", 0.0);
        else if (type.isArray()) param.put("collectionFormat", "multi");

        if (hasDefault) param.put("default", defaultValue.toString());
    }

    private void addTypeInfo(final ObjectNode container, final Type type) {
        addTypeInfo(container, type, false);
    }

    private void addTypeInfo(final ObjectNode container, final Type type, boolean wildcard) {
        if (type.isNull() || type.isVoid() || type.isHtml() || type.isAny()) return;

        if (type.isArray() && type instanceof ArrayType) {
            container.put("type", "array");
            addInnerTypeInfo(container.putObject("items"), ((ArrayType) type).getElementType().getFinalType());
        }
        else if (wildcard) {
            container.put("type", "array");
            addInnerTypeInfo(container.putObject("items"), type);
        }
        else addInnerTypeInfo(container, type);
    }

    private void definitions(final ObjectNode container) {
        for (final StructType ref : definitions) {
            final ObjectNode def = container.putObject(ref.getFullName());
            def.put("type", "object");
            addDescription(def, ref.getLabel(), ref.getName());

            final Seq<StructType> superTypes    = ref.getSuperTypes();
            final ObjectNode      propContainer;
            if (superTypes.isEmpty()) propContainer = def;
            else {
                final ArrayNode allOf = def.putArray("allOf");
                for (final StructType superType : superTypes)
                    allOf.addObject().put("$ref", format("#/definitions/%s", superType.getFullName()));
                propContainer = allOf.addObject();
            }

            final List<String> requiredFields = new ArrayList<>();

            final ObjectNode props = propContainer.putObject("properties");
            for (final TypeField field : ref.getChildren()) {
                final ObjectNode attr = props.putObject(field.getName());
                addDescription(attr, field.getLabel(), field.getName());
                final Type type = field.getType().getFinalType();
                addTypeInfo(attr, type);
                if (!field.isSigned() && type.isNumber()) attr.put("minimum", 0.0);
                final Expression defaultValue = field.getDefaultValue();
                final boolean    hasDefault   = defaultValue.isConstant() && !defaultValue.isNull();
                if (hasDefault) attr.put("default", defaultValue.toString());

                if (field.isRequired() && !hasDefault) requiredFields.add(field.getName());
            }

            if (!requiredFields.isEmpty()) {
                final ArrayNode required = propContainer.putArray("required");
                for (final String requiredField : requiredFields)
                    required.add(requiredField);
            }
        }
    }  // end method definitions

    private void errors(final ObjectNode container) {
        for (final EnumType error : errors) {
            final String enumFqn = error.getFullName();

            final ObjectNode def = container.putObject(enumFqn);
            def.put("type", "object");
            addDescription(def, error.getLabel(), error.getName());

            final ObjectNode props = def.putObject("properties");

            final ObjectNode code = props.putObject("code");
            code.put("type", "string");
            final ArrayNode codeOptions = code.putArray("enum");
            error.getIds().forEach(id -> codeOptions.add(enumFqn + "." + id));

            final ObjectNode msg = props.putObject("msg");
            msg.put("type", "string");
            final ArrayNode msgOptions = msg.putArray("enum");
            error.getIds().forEach(id -> msgOptions.add(error.getLabel(id)));

            final ObjectNode enumName = props.putObject("enumName");
            enumName.put("type", "string");
            final ArrayNode enumNameOptions = enumName.putArray("enum");
            error.getIds().forEach(enumNameOptions::add);

            final ObjectNode enumClass = props.putObject("enumClass");
            enumClass.put("type", "string");
            enumClass.put("default", enumFqn);
        }
    }

    private void meta(final ObjectNode container) {
        container.put("swagger", "2.0");
        final ObjectNode         info       = container.putObject("info");
        final Seq<ComponentInfo> components = Version.getInstance().getComponents().revert();
        info.put("title", components.map(ComponentInfo::getComponent).mkString(" & "));
        info.put("version", components.map(SwaggerGenerator::versionStr).mkString(" & "));
        container.put("host", host);
        container.put("basePath", "/");
        final ArrayNode schemesArray = container.putArray("schemes");
        schemes.forEach(schemesArray::add);
    }

    private void pathMethod(final ObjectNode method, final Handler handler, final Route route) {
        method.putArray("tags").add(handler.getName());
        method.put("summary", route.getSummary());
        // no long description yet... method.put("description", route.getDescription());
        method.put("operationId", route.getMethodName());

        final Type bodyType   = route.getBodyType().getFinalType();
        final Type returnType = route.getType().getFinalType();
        if (!bodyType.isNull()) method.putArray("consumes").add(mimeType(bodyType));
        if (!returnType.isVoid()) method.putArray("produces").add(mimeType(returnType));

        final ArrayNode parameters = method.putArray("parameters");
        route.getParts().filter(p -> p != null && !p.isStatic()).forEach(p -> addParameter(parameters, "path", p));
        route.getParameters().forEach(p -> addParameter(parameters, "query", p));
        if (!bodyType.isNull()) addBodyParameter(parameters, bodyType);

        final ObjectNode responses   = method.putObject("responses");
        final ObjectNode response200 = responses.putObject(valueOf(OK.code()));
        response200.put("description", "Successful operation (ok)");
        if (!returnType.isVoid() && !returnType.isHtml() && !returnType.isAny()) addTypeInfo(response200.putObject("schema"), returnType);

        switch (route.getSecureMethod()) {
        case UNRESTRICTED:
            break;
        case USERNAMEPASSWORD:
            if (route.getType().isHtml()) {
                final ObjectNode response301 = responses.putObject(valueOf(FOUND.code()));
                response301.put("description", "Redirect to login page if unauthorized (found)");
            }
            else {
                final ObjectNode response401 = responses.putObject(valueOf(UNAUTHORIZED.code()));
                response401.put("description", "Unauthorized error (unauthorized)");
            }
            break;
        case APPLICATIONTOKEN:
            final ObjectNode response401 = responses.putObject(valueOf(UNAUTHORIZED.code()));
            response401.put("description", "Unauthorized error (unauthorized)");
            break;
        }

        final Option<EnumType> errorRaiseEnum = handler.getErrorRaiseEnum();
        if (errorRaiseEnum.isPresent()) {
            final EnumType   enumType    = errorRaiseEnum.get();
            final ObjectNode response400 = responses.putObject(valueOf(BAD_REQUEST.code()));
            response400.put("description", "Application error (bad request)");
            response400.putObject("schema").put("$ref", format("#/definitions/%s", enumType.getFullName()));
            errors.add(enumType);
        }
    }  // end method pathMethod

    private void paths(final ObjectNode container) {
        for (final Handler handler : handlers) {
            for (final Route route : handler.getRoutes().filter(routeFilter)) {
                final String     pathStr      = toPath(route.getParts());
                final ObjectNode existingPath = (ObjectNode) container.get(pathStr);
                final ObjectNode path         = existingPath != null ? existingPath : container.putObject(pathStr);
                final ObjectNode method       = path.putObject(route.getHttpMethod().name().toLowerCase());

                pathMethod(method, handler, route);
            }
        }
    }

    private void tags(final ArrayNode container) {
        for (final Handler handler : handlers) {
            final ObjectNode tag = container.addObject();
            tag.put("name", handler.getName());
            addDescription(tag, handler.getLabel(), handler.getName().replace("Handler", ""));
        }
    }

    //~ Methods ......................................................................................................................................

    /** Create a Swagger generator using the handlers on the ModelRepository. */
    public static SwaggerGenerator create(final String host, final Seq<String> schemes, final Predicate<Handler> handlerFilter,
                                          final Predicate<Route> routeFilter) {
        final Seq<Handler> handlers = Context.getSingleton(ModelRepository.class).getModels().filter(Handler.class).filter(handlerFilter);
        return new SwaggerGenerator(host, schemes, handlers, routeFilter);
    }

    /** Create a Swagger generator using the provided handlers for testing purposes. */
    public static SwaggerGenerator createLocal(final Seq<Handler> handlers) {
        return new SwaggerGenerator("localhost:8080", ImmutableList.of("http"), handlers, o -> true);
    }

    private static void addDescription(final ObjectNode param, final String label, final String id) {
        param.put("description", notEmpty(label, toWords(fromCamelCase(id))));
    }

    @SuppressWarnings("MethodWithMultipleReturnPoints")
    private static Tuple<String, String> basicType(final Type type) {
        switch (type.getKind()) {
        case BOOLEAN:
            return tuple("boolean", "");
        case STRING:
            return tuple("string", "");
        case REAL:
        case DECIMAL:
            return tuple("number", "double");
        case INT:
            final boolean isLong = type instanceof IntType && ((IntType) type).isLong();
            return tuple("integer", isLong ? "int64" : "int32");
        case DATE_TIME:
            return tuple("string", "date-time");
        case DATE:
            return tuple("string", "date");
        case RESOURCE:
            return tuple("file", "");
        default:
            return tuple(INVALID, format("%s-%s", type.getKind(), type));
        }
    }

    private static String mimeType(final Type type) {
        if (type.isType() || type.isArray()) return Mime.APPLICATION_JSON.getMime();
        if (type.isHtml()) return Mime.TEXT_HTML.getMime();
        return Mime.TEXT_PLAIN.getMime();
    }

    private static String toPath(final Seq<Part> parts) {
        return parts.map(p -> p.isStatic() ? p.getName() : "{" + p.getName() + "}").mkString("/", "/", "");
    }

    private static String versionStr(final ComponentInfo c) {
        return isEmpty(c.getBuild()) ? "Dev" : format("%s %s-%s", c.getVersion(), c.getBranch(), c.getBuild());
    }
}  // end class SwaggerGenerator
