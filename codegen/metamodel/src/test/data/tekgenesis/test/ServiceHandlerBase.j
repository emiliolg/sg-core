package tekgenesis.test;

import tekgenesis.common.service.Call;
import tekgenesis.service.Factory;
import tekgenesis.service.HandlerInstance;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Strings;
import static tekgenesis.common.service.Method.GET;
import static tekgenesis.common.service.Method.POST;

/** 
 * Generated base class for handler: ServiceHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class ServiceHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    ServiceHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/" */
    @NotNull public abstract Result<Seq<Product>> all();

    /** Invoked for route "/" */
    @NotNull public abstract Result<Product> create();

    /** Invoked for route "/$id" */
    @NotNull public abstract Result<Product> get(@NotNull String id);

    /** Invoked for route "/$id" */
    @NotNull public abstract Result<Product> update(@NotNull String id);

    /** Invoked for route "/list/$id" */
    @NotNull public abstract Result<ProductList> list(@NotNull String id);

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(ServiceHandler.class);

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
