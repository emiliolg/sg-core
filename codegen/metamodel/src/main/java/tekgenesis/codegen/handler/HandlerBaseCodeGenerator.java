
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.handler;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.AbstractMethods;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.service.Status;
import tekgenesis.metadata.handler.Handler;
import tekgenesis.metadata.handler.Route;
import tekgenesis.type.EnumType;

import static java.lang.String.format;

import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.common.Predefined.equalElements;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.core.Strings.quoted;

/**
 * Generate the Code for the xxHandlerBase class.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class HandlerBaseCodeGenerator extends HandlerCommonCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final AbstractMethods ab;

    //~ Constructors .................................................................................................................................

    /** Create a FormBaseCodeGenerator. */
    public HandlerBaseCodeGenerator(JavaCodeGenerator codeGenerator, @NotNull Handler handler) {
        super(codeGenerator, handler.getName() + BASE, handler);
        ab = new AbstractMethods(this);
    }

    //~ Methods ......................................................................................................................................

    public void populate() {
        // generate code for form base class
        withComments("Generated base class for handler: " + handler.getName() + ".");
        withComments(MODIFICATION_WARNING_LINE_1);
        withComments(MODIFICATION_WARNING_LINE_2);
        suppressWarnings(EXTENDED_SUPPRESSED_WARNINGS);

        asAbstract();

        withSuperclass(SERVICE_HANDLER_INSTANCE);

        final Constructor constructor = generateConstructor();

        for (final Route route : handler.getRoutes()) {
            final Method m = findOrCreateMethod(route.getSignature());
            m.withComments("Invoked for route " + quoted(route.getPath()));
        }

        // If handler defines an enum for typed application exception errors
        for (final EnumType errorType : handler.getErrorRaiseEnum()) {
            constructor.assign(RESULTS, invoke(FACTORY, RESULTS));
            field(RESULTS, RESULTS_CLASS).notNull().asPrivate().asFinal();
            addApplicationExceptionMethods(errorType);
        }

        createRoutesInner();

        super.populate();

        // add logger to base
        addLogger(handler.getFullName());
    }

    @NotNull Iterable<Method> getAbstractMethods() {
        return ab.methods();
    }

    /** Add all exception methods. */
    private void addApplicationExceptionMethods(@NotNull EnumType enumType) {
        // Create exception(E error)
        final Method error = createExceptionMethod();
        error.arg(ERROR_ARG, enumType.getFullName()).notNull();
        error.return_(invoke(RESULTS, EXCEPTION, ERROR_ARG));
        error.withComments(
            format(RAISE_AN_EXCEPTION_RESULT_WITH_GIVEN_VALUE + "under default bad request {@link Status status}.", enumType.getName()));

        // Create exception(E error, Object... args)
        final Method errorWithArgs = createExceptionMethod();
        errorWithArgs.arg(ERROR_ARG, enumType.getFullName()).notNull();
        errorWithArgs.arg(ARGS, OBJECT_ARRAY).notNull();
        errorWithArgs.return_(invoke(RESULTS, EXCEPTION, ERROR_ARG, ARGS));
        errorWithArgs.withComments(
            format(RAISE_AN_EXCEPTION_RESULT_WITH_GIVEN_VALUE + "formatted with arguments, under default bad request {@link Status status}.",
                enumType.getName()));

        // Create exception(Status status, E error)
        final Method status = createExceptionMethod();
        status.arg(STATUS_ARG, Status.class).notNull();
        status.arg(ERROR_ARG, enumType.getFullName()).notNull();
        status.return_(invoke(RESULTS, EXCEPTION, STATUS_ARG, ERROR_ARG));
        status.withComments(format(RAISE_AN_EXCEPTION_UNDER_SPECIFIED_STATUS + "with given %s value.", enumType.getName()));

        // Create exception(Status status, E error, Object... args)
        final Method statusWithArgs = createExceptionMethod();
        statusWithArgs.arg(STATUS_ARG, Status.class).notNull();
        statusWithArgs.arg(ERROR_ARG, enumType.getFullName()).notNull();
        statusWithArgs.arg(ARGS, OBJECT_ARRAY).notNull();
        statusWithArgs.return_(invoke(RESULTS, EXCEPTION, STATUS_ARG, ERROR_ARG, ARGS));
        statusWithArgs.withComments(
            String.format(RAISE_AN_EXCEPTION_UNDER_SPECIFIED_STATUS + "with given %s value formatted with arguments.", enumType.getName()));
    }

    private Method createExceptionMethod() {
        return method(EXCEPTION, generic(RESULT_CLASS, "T")).withGenerics("T").asProtected().notNull();
    }

    @NotNull private Method findOrCreateMethod(Route.Signature signature) {
        // Check for existence
        for (final Method m : ab.findAllMethods(signature.getName())) {
            if (matchesSignature(m, signature.getArgumentClassNames())) return m;
        }

        // Create new method if no method matches signature
        final Method result = ab.generateAbstractMethod(signature.getName(), generic(RESULT_CLASS, classForType(signature.getType())));
        addArgumentsToMethod(signature, result, true);
        return result;
    }

    private Constructor generateConstructor() {
        final Constructor constructor = constructor().asPackage();
        constructor.arg(FACTORY, FACTORY_CLASS).notNull();
        constructor.invokeSuper(FACTORY);
        return constructor;
    }

    private boolean matchesSignature(Method m, Iterable<String> arguments) {
        return equalElements(map(m.getArguments(), Argument::getType), arguments);
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String RAISE_AN_EXCEPTION_RESULT_WITH_GIVEN_VALUE = "Raise an application exception result with given %s value, ";
    @NonNls private static final String RAISE_AN_EXCEPTION_UNDER_SPECIFIED_STATUS  =
        "Raise an application exception result under specified {@link Status status} ";
}  // end class NewHandlerBaseCodeGenerator
