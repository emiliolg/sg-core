
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.js;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.js.JsCodeGenerator;
import tekgenesis.codegen.impl.js.JsItemGenerator;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Strings;
import tekgenesis.metadata.handler.Handler;
import tekgenesis.metadata.handler.Parameter;
import tekgenesis.metadata.handler.Part;
import tekgenesis.metadata.handler.Route;
import tekgenesis.metadata.handler.Route.Signature;

import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.common.core.Strings.deCapitalizeFirst;
import static tekgenesis.common.core.Strings.singleQuoted;
import static tekgenesis.common.service.Method.GET;

/**
 * Angular module.
 */
public class AngularModuleCodeGenerator extends JsItemGenerator<AngularModuleCodeGenerator> implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final Handler handler;

    //~ Constructors .................................................................................................................................

    /** Generator constructor. */
    public AngularModuleCodeGenerator(JsCodeGenerator cg, Handler handler) {
        super(cg, jsFileName(handler), MODULE);
        this.handler = handler;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getSourceName() {
        return handler.getSourceName();
    }

    @Override protected void populate() {
        final Object routes = object();

        generateServiceFunctions(routes);

        final Function body = function().expand();
        body.arg("$http");
        body.return_(routes);

        final Array parameters = array().elem(singleQuoted("$http")).elem(body);

        final InvocationStatement services = insertInvocation(constant(FACTORY)).arg(singleQuoted(SERVICES)).arg(parameters).semicolon();

        final InvocationStatement module = invocation(MODULE).arg(singleQuoted(deCapitalizeFirst(handler.getName())));
        // .arg(array().elem(singleQuoted("config")));
        module.target(ANGULAR);

        services.target(module).newLine();
    }

    private Object createConfig(Seq<Parameter> parameters) {
        final Object params = object();
        parameters.forEach(p -> params.member(p.getName(), p.getName()));
        final Object result = object();
        // noinspection DuplicateStringLiteralInspection
        result.member("parameters", params);
        return result;
    }

    private Function createServiceFunction(Signature signature, Route route) {
        // Define anonymous service function
        final Function function = function().expand();
        signature.forEachArgument(f -> function.arg(f.getName()));
        function.arg(CONFIG);

        final InvocationStatement invoke = invocation(route.getHttpMethod().name().toLowerCase());
        invoke.target("$http");
        invoke.arg(createServicePath(route));
        if (signature.hasBody()) invoke.arg("body");
        manageServiceConfig(function, route, invoke);

        function.return_(invoke);

        return function;
    }

    private String createServicePath(Route route) {
        final StringBuilder result = new StringBuilder();
        result.append("'");
        Part last = null;
        for (final Part part : route.getParts()) {
            result.append(last == null || last.isStatic() ? "" : " + '").append("/");
            if (part.isStatic()) result.append(part.getName());
            else {
                result.append("'");
                result.append(" + ").append(part.getName());
            }
            last = part;
        }
        if (last == null) result.append("/'");
        else if (last.isStatic()) result.append("'");
        return result.toString();
    }  // end method createServicePath

    private void generateServiceFunctions(Object routes) {
        final Set<Integer> calls = new HashSet<>();
        for (final Route route : handler.getRoutes()) {
            final Signature signature = route.getSignature();
            if (calls.add(signature.hashNameAndArgumentTypes())) {
                if (route.getHttpMethod() != GET || !signature.getType().isVoid()) {  // Skip get void methods!
                    final Function function = createServiceFunction(signature, route);
                    routes.member(signature.getName(), function)
                        .withComments(
                            String.format("Promise for %s method invocation on path %s",
                                route.getHttpMethod().name().toLowerCase(),
                                singleQuoted(route.getPath())));
                }
            }
        }
    }

    private void manageServiceConfig(Function function, Route route, InvocationStatement invoke) {
        final Seq<Parameter> parameters = route.getParameters();
        if (!parameters.isEmpty()) {
            // Parameters defined, extend config object
            final InvocationStatement merge = function.invocation(MERGE_METHOD);
            merge.target(ANGULAR);
            merge.arg("config || {}");
            merge.arg(createConfig(parameters));

            function.declare(CONFIG, merge).asPublic();
        }
        invoke.arg(CONFIG);
    }

    //~ Methods ......................................................................................................................................

    /** Js file name. */
    @NotNull public static String jsFileName(Handler handler) {
        return Strings.fromCamelCase(handler.getName() + capitalizeFirst(MODULE)).toLowerCase().replace('_', '-');
    }
}
