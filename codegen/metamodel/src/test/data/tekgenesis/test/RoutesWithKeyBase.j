package tekgenesis.test;

import tekgenesis.common.service.Call;
import tekgenesis.service.Factory;
import tekgenesis.service.HandlerInstance;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;
import tekgenesis.common.core.Strings;
import static tekgenesis.common.service.Method.GET;
import static tekgenesis.common.service.Method.POST;

/** 
 * Generated base class for handler: RoutesWithKey.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class RoutesWithKeyBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    RoutesWithKeyBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/a/$id" */
    @NotNull public abstract Result<Void> first(@NotNull String id);

    /** Invoked for route "/a/$id" */
    @NotNull public abstract Result<Void> second(@NotNull String id);

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(RoutesWithKey.class);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/a/$id" {@link RoutesWithKey#first(String)} */
        @NotNull public static Call first(@NotNull String id) {
            return new Call(GET, String.format("/a/%s", Strings.truncate(id, 255)), "RouteWithKey");
        }

        /** Reverse route for "/a/$id" {@link RoutesWithKey#second(String)} */
        @NotNull public static Call second(@NotNull String id) {
            return new Call(POST, String.format("/a/%s", Strings.truncate(id, 255)), "AId");
        }

    }
}
