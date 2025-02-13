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

/** 
 * Generated base class for handler: AliasHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class AliasHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    AliasHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** 
     * Invoked for route "/a/$id"
     * Invoked for route "/b/$id"
     */
    @NotNull public abstract Result<Html> target(@NotNull String id);

    /** Invoked for route "/c/$id" */
    @NotNull public abstract Result<Html> target(@NotNull String id, @NotNull String body);

    /** Invoked for route "/failure" */
    @NotNull public abstract Result<String> failure();

    /** Invoked for route "/failure" */
    @NotNull public abstract Result<String> failure(@NotNull String body);

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(AliasHandler.class);

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
