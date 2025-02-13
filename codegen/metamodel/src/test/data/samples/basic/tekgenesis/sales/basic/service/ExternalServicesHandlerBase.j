package tekgenesis.sales.basic.service;

import tekgenesis.common.service.Call;
import tekgenesis.service.Factory;
import tekgenesis.service.HandlerInstance;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;
import static tekgenesis.common.service.Method.GET;

/** 
 * Generated base class for handler: ExternalServicesHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class ExternalServicesHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    ExternalServicesHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/invocations/compumundo/services" */
    @NotNull public abstract Result<String> compumundo();

    /** Invoked for route "/invocations/garbarino/services" */
    @NotNull public abstract Result<String> garbarino();

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(ExternalServicesHandler.class);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/invocations/compumundo/services" {@link ExternalServicesHandler#compumundo()} */
        @NotNull public static Call compumundo() {
            return new Call(GET, "/invocations/compumundo/services", "InvocationsCompumundoServices");
        }

        /** Reverse route for "/invocations/garbarino/services" {@link ExternalServicesHandler#garbarino()} */
        @NotNull public static Call garbarino() {
            return new Call(GET, "/invocations/garbarino/services", "InvocationsGarbarinoServices");
        }

    }
}
