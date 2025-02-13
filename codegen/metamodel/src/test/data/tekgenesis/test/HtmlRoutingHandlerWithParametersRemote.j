package tekgenesis.test;

import tekgenesis.common.service.Call;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.util.Conversions;
import tekgenesis.service.html.Html;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.core.Integers;
import tekgenesis.common.invoker.InvokerCommand;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.core.Option;
import tekgenesis.common.invoker.PathResource;
import tekgenesis.common.core.Strings;
import static tekgenesis.common.service.Method.GET;
import static tekgenesis.common.service.Method.POST;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Option.ofNullable;

/** 
 * Generated remote service class for handler: HtmlRoutingHandlerWithParameters.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public class HtmlRoutingHandlerWithParametersRemote
{

    //~ Fields ...................................................................................................................

    @NotNull private final HttpInvoker invoker;
    @NotNull private Option<Locale> locale;

    //~ Constructors .............................................................................................................

    /** Default service constructor specifying {@link HttpInvoker} */
    public HtmlRoutingHandlerWithParametersRemote(@NotNull HttpInvoker invoker) {
        this.invoker = invoker;
        locale = of(Context.getContext().getLocale());
    }

    //~ Methods ..................................................................................................................

    @NotNull protected PathResource<?> resource(@NotNull String path) {
        final PathResource<?> result = invoker.resource(path);
        if (locale.isPresent()) result.acceptLanguage(locale.get());
        return result;
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/nothing" */
    @NotNull public InvokerCommand<Html> nothing() {
        final Call call = Routes.nothing();
        return resource(call.getUrl()).invoke(GET, Html.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/home" */
    @NotNull public InvokerCommand<Html> home(@NotNull String from, @NotNull String to) {
        final Call call = Routes.home();
        final PathResource<?> resource = resource(call.getUrl());
        resource.param("from", Strings.truncate(from, 255));
        resource.param("to", Strings.truncate(to, 255));
        return resource.invoke(GET, Html.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/some/$id" */
    @NotNull public InvokerCommand<Html> someId(@NotNull String id, @NotNull String scheme, @NotNull String type) {
        final Call call = Routes.someId(id);
        final PathResource<?> resource = resource(call.getUrl());
        resource.param("scheme", Strings.truncate(scheme, 255));
        resource.param("type", Strings.truncate(type, 255));
        return resource.invoke(GET, Html.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'post' invocation on path "/product/$id" */
    @NotNull public InvokerCommand<?> create(@NotNull String id, @NotNull Product body, @NotNull String a, int b, boolean c, int d) {
        final Call call = Routes.create(id);
        final PathResource<?> resource = resource(call.getUrl());
        resource.param("a", Strings.truncate(a, 255));
        resource.param("b", Conversions.toString(Integers.checkSignedLength("b", b, false, 9)));
        resource.param("c", Conversions.toString(c));
        resource.param("d", Conversions.toString(Integers.checkSignedLength("d", d, false, 3)));
        return resource.invoke(POST, body).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'post' invocation on path "/returning/$id" */
    @NotNull public InvokerCommand<Html> create(@NotNull String id, @NotNull Product body, @NotNull String x) {
        final Call call = Routes.create(id);
        final PathResource<?> resource = resource(call.getUrl());
        resource.param("x", Strings.truncate(x, 255));
        return resource.invoke(POST, Html.class, body).withInvocationKey(call.getKey());
    }

    /** Set locale to be used on invocations. */
    public void setLocale(@Nullable Locale l) { locale = ofNullable(l); }

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
