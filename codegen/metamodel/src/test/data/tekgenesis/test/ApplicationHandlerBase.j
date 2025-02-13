package tekgenesis.test;

import tekgenesis.common.service.Call;
import tekgenesis.service.Factory;
import tekgenesis.service.html.Html;
import tekgenesis.common.service.Method;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;
import tekgenesis.service.ServiceHandlerInstance;

/**
 * Generated base class for handler: ApplicationHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class ApplicationHandlerBase
    extends ServiceHandlerInstance
{

    //~ Constructors .............................................................................................................

    ApplicationHandlerBase(@NotNull Factory factory) { }

    //~ Methods ..................................................................................................................

    /**
     * Invoked for route "/a/$id"
     * Invoked for route "/b/$id"
     * Invoked for route "/c/$id"
     */
    @NotNull public abstract Result<Html> target(@NotNull String id);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/a/$id" */
        @NotNull public static Call target(@NotNull String id) { return new Call(Method.GET, String.format("/a/%s", id)); }

    }
}