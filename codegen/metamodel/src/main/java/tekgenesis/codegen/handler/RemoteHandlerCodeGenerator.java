
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

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.Generators;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.GenericType;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.InvokerCommand;
import tekgenesis.common.invoker.PathResource;
import tekgenesis.common.media.MediaType;
import tekgenesis.common.service.Call;
import tekgenesis.common.util.Conversions;
import tekgenesis.metadata.handler.Handler;
import tekgenesis.metadata.handler.Parameter;
import tekgenesis.metadata.handler.Route;
import tekgenesis.type.ArrayType;
import tekgenesis.type.Type;

import static tekgenesis.codegen.CodeGeneratorConstants.*;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.common.core.Constants.INVOKE;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.media.Mime.TEXT_PLAIN;
import static tekgenesis.common.service.Method.GET;

/**
 * Generate the Code for the xxFormBase class.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class RemoteHandlerCodeGenerator extends HandlerCommonCodeGenerator {

    //~ Constructors .................................................................................................................................

    /** Create a FormBaseCodeGenerator. */
    public RemoteHandlerCodeGenerator(JavaCodeGenerator codeGenerator, @NotNull Handler handler) {
        super(codeGenerator, handler.getName() + REMOTE, handler);
    }

    //~ Methods ......................................................................................................................................

    public void populate() {
        // generate code for form base class
        withComments("Generated remote service class for handler: " + handler.getName() + ".");
        withComments(MODIFICATION_WARNING_LINE_1);
        withComments(MODIFICATION_WARNING_LINE_2_WITHOUT_SUBCLASS);
        suppressWarnings(EXTENDED_SUPPRESSED_WARNINGS);

        final Field invoker = field(INVOKER, HttpInvoker.class).notNull().asFinal();
        final Field locale  = field(LOCALE, generic(Option.class, Locale.class)).notNull();

        generateConstructor(invoker, locale);

        generateResourceMethod(invoker, locale);

        generateRemoteMethods(invoker);

        generateLocaleSetter(locale);

        createRoutesInner();

        super.populate();
    }

    private String createCallLocalVariable(Method m, String value) {
        final String variable = CALL;
        m.declare(Call.class, variable, value);
        return variable;
    }

    private Method createRemoteMethod(Route.Signature signature, Route route) {
        final Type    returnType = signature.getType();
        final boolean isVoid     = returnType.isVoid();

        final String commandReturnType = generic(InvokerCommand.class, isVoid ? WILDCARD : classForType(returnType));
        final Method m                 = method(signature.getName(), commandReturnType).notNull();

        m.asPublic();

        addArgumentsToMethod(signature, m, true);

        final String call = createCallLocalVariable(m, resolvedPathFromRoute(route.getPath(), signature));

        final Seq<Parameter> parameters = route.getParameters();

        final String target;

        final String r = invoke("", RESOURCE, invoke(call, "getUrl"));

        if (parameters.isEmpty() && !route.getBodyType().isString()) target = r;
        else {
            m.declare(generic(PathResource.class, WILDCARD), RESOURCE, r);
            for (final Parameter parameter : parameters) {
                final String statement = invoke(RESOURCE, PARAM_METHOD, quoted(parameter.getName()), parameterToString(parameter));
                if (parameter.isRequired()) m.statement(statement);
                else m.ifInlineStatement(parameter.getName() + NOT_EQ_NULL, statement);
            }
            target = RESOURCE;

            if (route.getBodyType().isString()) {
                final String textPlain = extractImport(MediaType.class) + "." + TEXT_PLAIN.name();
                m.statement(invoke(invoke(target, "getHeaders"), "setContentType", textPlain));
            }
        }

        final List<String> args   = getResourceInvokeArguments(signature, route, returnType, isVoid);
        final String       invoke = invoke(target, INVOKE, args);
        return m.return_(invoke(invoke, "withInvocationKey", invoke(call, "getKey")));
    }  // end method createRemoteMethod

    private void generateConstructor(@NotNull Field invoker, @NotNull Field locale) {
        final Constructor constructor = constructor().asPublic();
        constructor.arg(invoker);
        constructor.withComments("Default service constructor specifying " + link(extractImport(HttpInvoker.class)));

        // Generate: locale = some(Context.getContext().getLocale())
        final String assignation = invoke(invokeStatic(Context.class, "getContext"), "getLocale");
        final String option      = invokeStatic(createQName(Option.class.getName(), "of"), assignation);
        constructor.assign(locale.getName(), option);
    }

    private void generateLocaleSetter(Field locale) {
        final Method   setter = setter(locale.getName());
        final Argument arg    = setter.arg("l", Locale.class);
        final String   option = invokeStatic(createQName(Option.class.getName(), OF_NULLABLE), arg.getName());
        setter.assign(locale.getName(), option);
        setter.withComments("Set locale to be used on invocations.");
    }

    private void generateRemoteMethods(Field invoker) {
        final Set<Integer> calls = new HashSet<>();
        for (final Route route : handler.getRoutes()) {
            final Route.Signature signature = route.getSignature();
            if (calls.add(signature.hashNameAndArgumentTypes())) {
                if ((route.getHttpMethod() != GET || !signature.getType().isVoid()) && !signature.getType().isResource()) {  // Skip get void
                                                                                                                             // methods and
                                                                                                                             // resources!
                    final Method method = createRemoteMethod(signature, route);
                    method.withComments(
                        "Return {@link InvokerCommand command} for remote '" + route.getHttpMethod().name().toLowerCase() + "' invocation on path " +
                        quoted(route.getPath()));
                }
            }
        }
    }

    private void generateResourceMethod(Field invoker, Field locale) {
        final String   type = generic(PathResource.class, "?");
        final Method   m    = method(RESOURCE, type).asProtected().notNull();
        final Argument arg  = m.arg(PATH, String.class).notNull();
        m.declare(type, RESULT, invoke(invoker.getName(), RESOURCE, arg.getName()));
        final String accept = invoke(RESULT, "acceptLanguage", invoke(locale.getName(), "get"));
        m.ifInlineStatement(invoke(locale.getName(), IS_PRESENT), accept);
        m.return_(RESULT);
    }

    private String parameterToString(Parameter parameter) {
        final Type   type = parameter.getFinalType();
        final String var  = Generators.verifyField(this, parameter.getName(), parameter);
        return type.isString() ? var : invokeStatic(Conversions.class, TO_STRING, var);
    }

    private String resolveClassOrGenericType(Type returnType) {
        if (returnType.isArray()) {
            final ArrayType arrayType   = (ArrayType) returnType;
            final String    elementType = extractImport(arrayType.getElementType().getImplementationClassName());
            final String    generic     = generic(GenericType.class, generic(Seq.class, elementType));
            return new_(generic) + "{}";
        }
        return classOf(extractImport(classForType(returnType)));
    }

    private List<String> getResourceInvokeArguments(Route.Signature signature, Route route, Type returnType, boolean isVoid) {
        final List<String> invokeArguments = new ArrayList<>();
        invokeArguments.add(getRouteHttpMethod(route.getHttpMethod()));
        if (!isVoid) invokeArguments.add(resolveClassOrGenericType(returnType));
        if (signature.hasBody()) invokeArguments.add("body");
        return invokeArguments;
    }
}
