package tekgenesis.sales.basic.service;

import tekgenesis.common.service.Call;
import tekgenesis.service.Factory;
import tekgenesis.service.HandlerInstance;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Strings;
import static tekgenesis.common.service.Method.DELETE;
import static tekgenesis.common.service.Method.GET;
import static tekgenesis.common.service.Method.POST;
import static tekgenesis.common.service.Method.PUT;

/** 
 * Generated base class for handler: CountryHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class CountryHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    CountryHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/country" */
    @NotNull public abstract Result<Seq<Country>> list(boolean success);

    /** Invoked for route "/country" */
    @NotNull public abstract Result<Void> post(@NotNull Country body, boolean success);

    /** Invoked for route "/country/$iso2" */
    @NotNull public abstract Result<Void> put(@NotNull String iso2, @NotNull Country body, boolean success);

    /** Invoked for route "/country/$iso2" */
    @NotNull public abstract Result<Void> delete(@NotNull String iso2, boolean success);

    /** Invoked for route "/country/getAndInsert" */
    @NotNull public abstract Result<Country> getAndInsert(boolean success);

    /** Invoked for route "/country/$iso2" */
    @NotNull public abstract Result<Country> get(@NotNull String iso2);

    /** Invoked for route "/country/exception" */
    @NotNull public abstract Result<Country> postWithException(@NotNull Country body);

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(CountryHandler.class);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/country" {@link CountryHandler#list(boolean)} */
        @NotNull public static Call list() { return new Call(GET, "/country", "Country"); }

        /** Reverse route for "/country" {@link CountryHandler#post(Country, boolean)} */
        @NotNull public static Call post() { return new Call(POST, "/country", "Country"); }

        /** Reverse route for "/country/$iso2" {@link CountryHandler#put(String, Country, boolean)} */
        @NotNull public static Call put(@NotNull String iso2) {
            return new Call(PUT, String.format("/country/%s", Strings.truncate(iso2, 2)), "CountryIso2");
        }

        /** Reverse route for "/country/$iso2" {@link CountryHandler#delete(String, boolean)} */
        @NotNull public static Call delete(@NotNull String iso2) {
            return new Call(DELETE, String.format("/country/%s", Strings.truncate(iso2, 2)), "CountryIso2");
        }

        /** Reverse route for "/country/getAndInsert" {@link CountryHandler#getAndInsert(boolean)} */
        @NotNull public static Call getAndInsert() {
            return new Call(GET, "/country/getAndInsert", "CountryGetAndInsert");
        }

        /** Reverse route for "/country/$iso2" {@link CountryHandler#get(String)} */
        @NotNull public static Call get(@NotNull String iso2) {
            return new Call(GET, String.format("/country/%s", Strings.truncate(iso2, 2)), "CountryIso2");
        }

        /** Reverse route for "/country/exception" {@link CountryHandler#postWithException(Country)} */
        @NotNull public static Call postWithException() {
            return new Call(POST, "/country/exception", "CountryException");
        }

    }
}
