package tekgenesis.test;

import tekgenesis.common.service.Call;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.GenericType;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.InvokerCommand;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.core.Option;
import tekgenesis.common.invoker.PathResource;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Strings;
import static tekgenesis.common.service.Method.GET;
import static tekgenesis.common.service.Method.POST;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Option.ofNullable;

/** 
 * Generated remote service class for handler: ServiceHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public class ServiceHandlerRemote
{

    //~ Fields ...................................................................................................................

    @NotNull private final HttpInvoker invoker;
    @NotNull private Option<Locale> locale;

    //~ Constructors .............................................................................................................

    /** Default service constructor specifying {@link HttpInvoker} */
    public ServiceHandlerRemote(@NotNull HttpInvoker invoker) {
        this.invoker = invoker;
        locale = of(Context.getContext().getLocale());
    }

    //~ Methods ..................................................................................................................

    @NotNull protected PathResource<?> resource(@NotNull String path) {
        final PathResource<?> result = invoker.resource(path);
        if (locale.isPresent()) result.acceptLanguage(locale.get());
        return result;
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/" */
    @NotNull public InvokerCommand<Seq<Product>> all() {
        final Call call = Routes.all();
        return resource(call.getUrl()).invoke(GET, new GenericType<Seq<Product>>(){}).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'post' invocation on path "/" */
    @NotNull public InvokerCommand<Product> create() {
        final Call call = Routes.create();
        return resource(call.getUrl()).invoke(POST, Product.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/$id" */
    @NotNull public InvokerCommand<Product> get(@NotNull String id) {
        final Call call = Routes.get(id);
        return resource(call.getUrl()).invoke(GET, Product.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'post' invocation on path "/$id" */
    @NotNull public InvokerCommand<Product> update(@NotNull String id) {
        final Call call = Routes.update(id);
        return resource(call.getUrl()).invoke(POST, Product.class).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'get' invocation on path "/list/$id" */
    @NotNull public InvokerCommand<ProductList> list(@NotNull String id) {
        final Call call = Routes.list(id);
        return resource(call.getUrl()).invoke(GET, ProductList.class).withInvocationKey(call.getKey());
    }

    /** Set locale to be used on invocations. */
    public void setLocale(@Nullable Locale l) { locale = ofNullable(l); }

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/" {@link ServiceHandler#all()} */
        @NotNull public static Call all() { return new Call(GET, "/", ""); }

        /** Reverse route for "/" {@link ServiceHandler#create()} */
        @NotNull public static Call create() { return new Call(POST, "/", ""); }

        /** Reverse route for "/$id" {@link ServiceHandler#get(String)} */
        @NotNull public static Call get(@NotNull String id) {
            return new Call(GET, String.format("/%s", Strings.truncate(id, 255)), "Id");
        }

        /** Reverse route for "/$id" {@link ServiceHandler#update(String)} */
        @NotNull public static Call update(@NotNull String id) {
            return new Call(POST, String.format("/%s", Strings.truncate(id, 255)), "Id");
        }

        /** Reverse route for "/list/$id" {@link ServiceHandler#list(String)} */
        @NotNull public static Call list(@NotNull String id) {
            return new Call(GET, String.format("/list/%s", Strings.truncate(id, 255)), "ListId");
        }

    }
}
