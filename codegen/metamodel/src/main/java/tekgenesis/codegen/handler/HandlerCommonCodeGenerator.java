
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.handler;

import java.util.*;
import java.util.function.Consumer;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.ImmutableList.Builder;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Resource.Entry;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.handler.Handler;
import tekgenesis.metadata.handler.Route;
import tekgenesis.type.Type;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import static tekgenesis.codegen.CodeGeneratorConstants.FORMAT;
import static tekgenesis.codegen.common.Generators.verifyField;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.ImmutableList.builder;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.util.Primitives.primitiveFor;

/**
 * Common class for handler code generators.
 */
class HandlerCommonCodeGenerator extends ClassGenerator implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull protected final Handler handler;

    //~ Constructors .................................................................................................................................

    HandlerCommonCodeGenerator(JavaCodeGenerator cg, String name, @NotNull Handler handler) {
        super(cg, name);
        this.handler = handler;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getSourceName() {
        return handler.getSourceName();
    }

    void addArgumentsToMethod(Route.Signature signature, final Method result, final boolean includeBodyAndParameters) {
        final Consumer<TypeField> visitor = arg -> {
                                                final Type     type     = arg.getFinalType();
                                                final Argument argument = result.arg(arg.getName(), type.getImplementationClassName());
                                                if (arg.isRequired() || type.isArray()) argument.notNull();
                                            };

        if (includeBodyAndParameters) signature.forEachArgument(visitor);
        else signature.forEachPart(visitor);
    }

    String classForType(@NotNull final Type type) {
        if (type.isResource()) return Entry.class.getCanonicalName();
        if (type.isAny()) return byte[].class.getCanonicalName();
        return type.getImplementationClassName();
    }

    ClassGenerator createRoutesInner() {
        final ClassGenerator g = innerClass(ROUTES);
        g.asStatic().asPublic().asFinal();

        final Set<Integer> calls = new HashSet<>();
        for (final Route route : handler.getRoutes()) {
            final Route.Signature signature = route.getSignature();
            if (calls.add(signature.hashNameAndDynamicParts())) {
                final Method call = g.method(signature.getName(), CALL_CLASS).asPublic().asStatic().notNull();
                call.withComments(
                    "Reverse route for " + quoted(route.getPath()) + " " + commentLink(handler.getName(), signature.getName(), signature));
                addArgumentsToMethod(route.getSignature(), call, false);
                call.return_(
                    new_(CALL_CLASS, getRouteHttpMethod(route.getHttpMethod()), resolvedPath(route.getPath(), signature), resolveKey(route)));
            }
        }
        return g;
    }

    /** Return path from inner Routes (eg. 'Routes.product(category, id).getUrl()') */
    String resolvedPathFromRoute(String absolutePath, Route.Signature signature) {
        return invokeStatic(ROUTES, signature.getName(), collectPaths(signature, false));
    }

    String getRouteHttpMethod(tekgenesis.common.service.Method m) {
        return extractStaticImport(createQName(tekgenesis.common.service.Method.class.getCanonicalName(), m.name()));
    }

    private List<String> appendReplaceablePathToArgs(String absolutePath, List<String> parts) {
        final String sPath = absolutePath.replaceAll("\\$[a-zA-Z0-9]+\\*?", "%s");
        parts.add(0, quoted(sPath));
        return parts;
    }

    private List<String> collectPaths(Route.Signature signature, final boolean verify) {
        final List<String> result = new ArrayList<>();
        signature.forEachPart(a -> result.add(verify ? verifyField(this, a.getName(), a) : a.getName()));
        return result;
    }

    /** Build javadoc comment link. */
    private String commentLink(@NotNull final String clazz, @NotNull final String method, @NotNull final Route.Signature signature) {
        // Instead of generating links such as Some#metho(Integer, String, String>) -> generate Some#method(int, String, Seq) ;)
        final Builder<String> args = builder();
        signature.forEachArgument(a -> {
            final Type type = a.getFinalType();
            if (type.isArray()) args.add(Seq.class.getSimpleName());
            else {
                final String t = extractImport(type.getImplementationClassName());
                if (a.isRequired()) args.add(notEmpty(primitiveFor(t), t));
                else args.add(t);
            }
        });
        return format("{@link %s#%s(%s)}", clazz, method, args.build().mkString(", "));
    }  // end method commentLink

    /**
     * Return format (eg. 'String.format("/products/%s/%s", category, id)') or straight path if no
     * arguments.
     */
    private String resolvedPath(String absolutePath, Route.Signature signature) {
        final List<String> args = collectPaths(signature, true);
        if (args.isEmpty()) return quoted(absolutePath);
        final List<String> formatArgs = appendReplaceablePathToArgs(absolutePath, args);
        return invokeStatic(String.class, FORMAT, formatArgs);
    }

    private String resolveKey(Route route) {
        return quoted(route.getKey());
    }

    //~ Static Fields ................................................................................................................................

    @NonNls static final String[] EXTENDED_SUPPRESSED_WARNINGS = ImmutableList.<String>build(builder -> {
                builder.addAll(asList(COMMON_SUPPRESSED_WARNINGS));
                builder.add(UTILITY_CLASS_WITHOUT_CONSTRUCTOR);
                builder.add(UNUSED_PARAMETERS);
            }).toArray(new String[] {});
}  // end class HandlerCommonCodeGenerator
