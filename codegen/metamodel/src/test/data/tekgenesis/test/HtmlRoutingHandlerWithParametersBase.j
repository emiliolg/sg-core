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
import static tekgenesis.common.service.Method.POST;

/** 
 * Generated base class for handler: HtmlRoutingHandlerWithParameters.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class HtmlRoutingHandlerWithParametersBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    HtmlRoutingHandlerWithParametersBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/nothing" */
    @NotNull public abstract Result<Html> nothing();

    /** Invoked for route "/home" */
    @NotNull public abstract Result<Html> home(@NotNull String from, @NotNull String to);

    /** Invoked for route "/some/$id" */
    @NotNull public abstract Result<Html> someId(@NotNull String id, @NotNull String scheme, @NotNull String type);

    /** Invoked for route "/product/$id" */
    @NotNull public abstract Result<Void> create(@NotNull String id, @NotNull Product body, @NotNull String a, int b, boolean c, int d);

    /** Invoked for route "/returning/$id" */
    @NotNull public abstract Result<Html> create(@NotNull String id, @NotNull Product body, @NotNull String x);

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(HtmlRoutingHandlerWithParameters.class);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/nothing" {@link HtmlRoutingHandlerWithParameters#nothing()} */
        @NotNull public static Call nothing() { return new Call(GET, "/nothing", "Nothing"); }

        /** Reverse route for "/home" {@link HtmlRoutingHandlerWithParameters#home(String, String)} */
        @NotNull public static Call home() { return new Call(GET, "/home", "Home"); }

        /** Reverse route for "/some/$id" {@link HtmlRoutingHandlerWithParameters#someId(String, String, String)} */
        @NotNull public static Call someId(@NotNull String id) {
            return new Call(GET, String.format("/some/%s", Strings.truncate(id, 255)), "SomeId");
        }

        /** Reverse route for "/product/$id" {@link HtmlRoutingHandlerWithParameters#create(String, Product, String, int, boolean, int)} */
        @NotNull public static Call create(@NotNull String id) {
            return new Call(POST, String.format("/product/%s", Strings.truncate(id, 255)), "ProductId");
        }

    }
}
