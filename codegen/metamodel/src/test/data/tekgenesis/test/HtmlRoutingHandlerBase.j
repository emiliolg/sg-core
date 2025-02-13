package tekgenesis.test;

import tekgenesis.common.service.Call;
import tekgenesis.service.Factory;
import tekgenesis.service.HandlerInstance;
import tekgenesis.service.html.Html;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;
import tekgenesis.common.core.Strings;
import static tekgenesis.common.service.Method.GET;

/** 
 * Generated base class for handler: HtmlRoutingHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class HtmlRoutingHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    HtmlRoutingHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/home" */
    @NotNull public abstract Result<Html> home();

    /** Invoked for route "/some" */
    @NotNull public abstract Result<Html> some();

    /** Invoked for route "/some/$id" */
    @NotNull public abstract Result<Html> someId(@NotNull String id);

    /** Invoked for route "/some/$path*" */
    @NotNull public abstract Result<Html> somePath(@NotNull String path);

    /** Invoked for route "/example/$with/multiple/$params" */
    @NotNull public abstract Result<Html> multiple(@NotNull String with, @NotNull String params);

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(HtmlRoutingHandler.class);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/home" {@link HtmlRoutingHandler#home()} */
        @NotNull public static Call home() { return new Call(GET, "/home", "Home"); }

        /** Reverse route for "/some" {@link HtmlRoutingHandler#some()} */
        @NotNull public static Call some() { return new Call(GET, "/some", "Some"); }

        /** Reverse route for "/some/$id" {@link HtmlRoutingHandler#someId(String)} */
        @NotNull public static Call someId(@NotNull String id) {
            return new Call(GET, String.format("/some/%s", Strings.truncate(id, 255)), "SomeId");
        }

        /** Reverse route for "/some/$path*" {@link HtmlRoutingHandler#somePath(String)} */
        @NotNull public static Call somePath(@NotNull String path) {
            return new Call(GET, String.format("/some/%s", Strings.truncate(path, 255)), "SomePath");
        }

        /** Reverse route for "/example/$with/multiple/$params" {@link HtmlRoutingHandler#multiple(String, String)} */
        @NotNull public static Call multiple(@NotNull String with, @NotNull String params) {
            return new Call(GET, String.format("/example/%s/multiple/%s", Strings.truncate(with, 255), Strings.truncate(params, 255)), "ExampleWithMultipleParams");
        }

    }
}
