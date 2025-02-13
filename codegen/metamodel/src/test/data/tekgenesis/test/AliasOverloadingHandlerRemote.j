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
 * Generated remote service class for handler: AliasOverloadingHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public class AliasOverloadingHandlerRemote
{

    //~ Fields ...................................................................................................................

    @NotNull private final HttpInvoker invoker;
    @NotNull private Option<Locale> locale;

    //~ Constructors .............................................................................................................

    /** Default service constructor specifying {@link HttpInvoker} */
    public AliasOverloadingHandlerRemote(@NotNull HttpInvoker invoker) {
        this.invoker = invoker;
        locale = of(Context.getContext().getLocale());
    }

    //~ Methods ..................................................................................................................

    @NotNull protected PathResource<?> resource(@NotNull String path) {
        final PathResource<?> result = invoker.resource(path);
        if (locale.isPresent()) result.acceptLanguage(locale.get());
        return result;
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/$a" */
    @NotNull public InvokerCommand<Html> target(@NotNull String a) {
        final Call call = Routes.target(a);
        return resource(call.getUrl()).invoke(GET, Html.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/$a/$b" */
    @NotNull public InvokerCommand<Html> target(@NotNull String a, @NotNull String b) {
        final Call call = Routes.target(a, b);
        return resource(call.getUrl()).invoke(GET, Html.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/$a/$b/$c" */
    @NotNull public InvokerCommand<Html> target(@NotNull String a, @NotNull String b, @NotNull String c) {
        final Call call = Routes.target(a, b, c);
        return resource(call.getUrl()).invoke(GET, Html.class).withInvocationKey(call.getKey());
    }

    /** Set locale to be used on invocations. */
    public void setLocale(@Nullable Locale l) { locale = ofNullable(l); }

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/$a" {@link AliasOverloadingHandler#target(String)} */
        @NotNull public static Call target(@NotNull String a) {
            return new Call(GET, String.format("/%s", Strings.truncate(a, 255)), "A");
        }

        /** Reverse route for "/$a/$b" {@link AliasOverloadingHandler#target(String, String)} */
        @NotNull public static Call target(@NotNull String a, @NotNull String b) {
            return new Call(GET, String.format("/%s/%s", Strings.truncate(a, 255), Strings.truncate(b, 255)), "AB");
        }

        /** Reverse route for "/$a/$b/$c" {@link AliasOverloadingHandler#target(String, String, String)} */
        @NotNull public static Call target(@NotNull String a, @NotNull String b, @NotNull String c) {
            return new Call(GET, String.format("/%s/%s/%s", Strings.truncate(a, 255), Strings.truncate(b, 255), Strings.truncate(c, 255)), "ABC");
        }

    }
}
