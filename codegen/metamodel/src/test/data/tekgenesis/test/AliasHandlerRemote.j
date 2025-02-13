package tekgenesis.test;

import tekgenesis.common.service.Call;
import tekgenesis.common.env.context.Context;
import tekgenesis.service.html.Html;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.InvokerCommand;
import java.util.Locale;
import tekgenesis.common.media.MediaType;
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
 * Generated remote service class for handler: AliasHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public class AliasHandlerRemote
{

    //~ Fields ...................................................................................................................

    @NotNull private final HttpInvoker invoker;
    @NotNull private Option<Locale> locale;

    //~ Constructors .............................................................................................................

    /** Default service constructor specifying {@link HttpInvoker} */
    public AliasHandlerRemote(@NotNull HttpInvoker invoker) {
        this.invoker = invoker;
        locale = of(Context.getContext().getLocale());
    }

    //~ Methods ..................................................................................................................

    @NotNull protected PathResource<?> resource(@NotNull String path) {
        final PathResource<?> result = invoker.resource(path);
        if (locale.isPresent()) result.acceptLanguage(locale.get());
        return result;
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/a/$id" */
    @NotNull public InvokerCommand<Html> target(@NotNull String id) {
        final Call call = Routes.target(id);
        return resource(call.getUrl()).invoke(GET, Html.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'post' invocation on path "/c/$id" */
    @NotNull public InvokerCommand<Html> target(@NotNull String id, @NotNull String body) {
        final Call call = Routes.target(id);
        final PathResource<?> resource = resource(call.getUrl());
        resource.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        return resource.invoke(POST, Html.class, body).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/failure" */
    @NotNull public InvokerCommand<String> failure() {
        final Call call = Routes.failure();
        return resource(call.getUrl()).invoke(GET, String.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'post' invocation on path "/failure" */
    @NotNull public InvokerCommand<String> failure(@NotNull String body) {
        final Call call = Routes.failure();
        final PathResource<?> resource = resource(call.getUrl());
        resource.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        return resource.invoke(POST, String.class, body).withInvocationKey(call.getKey());
    }

    /** Set locale to be used on invocations. */
    public void setLocale(@Nullable Locale l) { locale = ofNullable(l); }

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/a/$id" {@link AliasHandler#target(String)} */
        @NotNull public static Call target(@NotNull String id) {
            return new Call(GET, String.format("/a/%s", Strings.truncate(id, 255)), "AId");
        }

        /** Reverse route for "/failure" {@link AliasHandler#failure()} */
        @NotNull public static Call failure() { return new Call(GET, "/failure", "Failure"); }

    }
}
