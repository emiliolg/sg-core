package tekgenesis.sales.basic.service;

import tekgenesis.common.service.Call;
import tekgenesis.service.Factory;
import tekgenesis.service.HandlerInstance;
import tekgenesis.service.html.Html;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;
import static tekgenesis.common.service.Method.GET;

/** 
 * Generated base class for handler: InternalProductHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class InternalProductHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    InternalProductHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/internal/message" */
    @NotNull public abstract Result<Html> internal();

    /** Invoked for route "/internal/timestamp" */
    @NotNull public abstract Result<Html> timestamp();

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(InternalProductHandler.class);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/internal/message" {@link InternalProductHandler#internal()} */
        @NotNull public static Call internal() {
            return new Call(GET, "/internal/message", "InternalMessage");
        }

        /** Reverse route for "/internal/timestamp" {@link InternalProductHandler#timestamp()} */
        @NotNull public static Call timestamp() {
            return new Call(GET, "/internal/timestamp", "InternalTimestamp");
        }

    }
}
