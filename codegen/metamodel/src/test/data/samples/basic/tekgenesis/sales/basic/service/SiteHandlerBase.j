package tekgenesis.sales.basic.service;

import tekgenesis.common.service.Call;
import tekgenesis.common.core.Resource.Entry;
import tekgenesis.service.Factory;
import tekgenesis.service.HandlerInstance;
import tekgenesis.service.html.Html;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.service.Result;
import tekgenesis.common.core.Strings;
import static tekgenesis.common.service.Method.GET;

/** 
 * Generated base class for handler: SiteHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class SiteHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    SiteHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/site" */
    @NotNull public abstract Result<Html> home();

    /** Invoked for route "/site/cache" */
    @NotNull public abstract Result<Html> cache();

    /** Invoked for route "/site/product/$pid" */
    @NotNull public abstract Result<Html> product(@NotNull String pid);

    /** Invoked for route "/site/products" */
    @NotNull public abstract Result<Html> products();

    /** Invoked for route "/site/product/$pid/image" */
    @NotNull public abstract Result<Entry> image(@NotNull String pid);

    /** Invoked for route "/site/failure" */
    @NotNull public abstract Result<Html> failure(int method);

    /** Invoked for route "/site/search" */
    @NotNull public abstract Result<Html> search(@Nullable String q);

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(SiteHandler.class);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/site" {@link SiteHandler#home()} */
        @NotNull public static Call home() { return new Call(GET, "/site", "Site"); }

        /** Reverse route for "/site/cache" {@link SiteHandler#cache()} */
        @NotNull public static Call cache() { return new Call(GET, "/site/cache", "SiteCache"); }

        /** Reverse route for "/site/product/$pid" {@link SiteHandler#product(String)} */
        @NotNull public static Call product(@NotNull String pid) {
            return new Call(GET, String.format("/site/product/%s", Strings.truncate(pid, 255)), "SiteProductPid");
        }

        /** Reverse route for "/site/products" {@link SiteHandler#products()} */
        @NotNull public static Call products() { return new Call(GET, "/site/products", "SiteProducts"); }

        /** Reverse route for "/site/product/$pid/image" {@link SiteHandler#image(String)} */
        @NotNull public static Call image(@NotNull String pid) {
            return new Call(GET, String.format("/site/product/%s/image", Strings.truncate(pid, 255)), "SiteProductPidImage");
        }

        /** Reverse route for "/site/failure" {@link SiteHandler#failure(int)} */
        @NotNull public static Call failure() { return new Call(GET, "/site/failure", "SiteFailure"); }

        /** Reverse route for "/site/search" {@link SiteHandler#search(String)} */
        @NotNull public static Call search() { return new Call(GET, "/site/search", "SiteSearch"); }

    }
}
