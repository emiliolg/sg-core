package tekgenesis.test;

import tekgenesis.common.service.Call;
import tekgenesis.common.env.context.Context;
import tekgenesis.service.html.Html;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.InvokerCommand;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.core.Option;
import tekgenesis.common.invoker.PathResource;
import tekgenesis.common.core.Strings;
import static tekgenesis.common.service.Method.GET;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Option.ofNullable;

/** 
 * Generated remote service class for handler: HtmlRoutingHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public class HtmlRoutingHandlerRemote
{

    //~ Fields ...................................................................................................................

    @NotNull private final HttpInvoker invoker;
    @NotNull private Option<Locale> locale;

    //~ Constructors .............................................................................................................

    /** Default service constructor specifying {@link HttpInvoker} */
    public HtmlRoutingHandlerRemote(@NotNull HttpInvoker invoker) {
        this.invoker = invoker;
        locale = of(Context.getContext().getLocale());
    }

    //~ Methods ..................................................................................................................

    @NotNull protected PathResource<?> resource(@NotNull String path) {
        final PathResource<?> result = invoker.resource(path);
        if (locale.isPresent()) result.acceptLanguage(locale.get());
        return result;
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/home" */
    @NotNull public InvokerCommand<Html> home() {
        final Call call = Routes.home();
        return resource(call.getUrl()).invoke(GET, Html.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/some" */
    @NotNull public InvokerCommand<Html> some() {
        final Call call = Routes.some();
        return resource(call.getUrl()).invoke(GET, Html.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/some/$id" */
    @NotNull public InvokerCommand<Html> someId(@NotNull String id) {
        final Call call = Routes.someId(id);
        return resource(call.getUrl()).invoke(GET, Html.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/some/$path*" */
    @NotNull public InvokerCommand<Html> somePath(@NotNull String path) {
        final Call call = Routes.somePath(path);
        return resource(call.getUrl()).invoke(GET, Html.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/example/$with/multiple/$params" */
    @NotNull public InvokerCommand<Html> multiple(@NotNull String with, @NotNull String params) {
        final Call call = Routes.multiple(with, params);
        return resource(call.getUrl()).invoke(GET, Html.class).withInvocationKey(call.getKey());
    }

    /** Set locale to be used on invocations. */
    public void setLocale(@Nullable Locale l) { locale = ofNullable(l); }

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
