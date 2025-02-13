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
import static tekgenesis.common.service.Method.POST;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Option.ofNullable;

/** 
 * Generated remote service class for handler: TypeAliasOnHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public class TypeAliasOnHandlerRemote
{

    //~ Fields ...................................................................................................................

    @NotNull private final HttpInvoker invoker;
    @NotNull private Option<Locale> locale;

    //~ Constructors .............................................................................................................

    /** Default service constructor specifying {@link HttpInvoker} */
    public TypeAliasOnHandlerRemote(@NotNull HttpInvoker invoker) {
        this.invoker = invoker;
        locale = of(Context.getContext().getLocale());
    }

    //~ Methods ..................................................................................................................

    @NotNull protected PathResource<?> resource(@NotNull String path) {
        final PathResource<?> result = invoker.resource(path);
        if (locale.isPresent()) result.acceptLanguage(locale.get());
        return result;
    }

    /** Return {@link InvokerCommand command} for remote 'post' invocation on path "/stringAlias" */
    @NotNull public InvokerCommand<String> stringAlias(@NotNull String body) {
        final Call call = Routes.stringAlias();
        return resource(call.getUrl()).invoke(POST, String.class, body).withInvocationKey(call.getKey());
    }

    /** Return {@link InvokerCommand command} for remote 'post' invocation on path "/stringAliasArray" */
    @NotNull public InvokerCommand<Seq<String>> stringAliasArray(@NotNull Seq<String> body) {
        final Call call = Routes.stringAliasArray();
        return resource(call.getUrl()).invoke(POST, new GenericType<Seq<String>>(){}, body).withInvocationKey(call.getKey());
    }

    /** Set locale to be used on invocations. */
    public void setLocale(@Nullable Locale l) { locale = ofNullable(l); }

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/stringAlias" {@link TypeAliasOnHandler#stringAlias(String)} */
        @NotNull public static Call stringAlias() { return new Call(POST, "/stringAlias", "StringAlias"); }

        /** Reverse route for "/stringAliasArray" {@link TypeAliasOnHandler#stringAliasArray(Seq)} */
        @NotNull public static Call stringAliasArray() {
            return new Call(POST, "/stringAliasArray", "StringAliasArray");
        }

    }
}
