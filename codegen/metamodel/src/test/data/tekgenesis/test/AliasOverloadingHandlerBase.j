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
 * Generated base class for handler: AliasOverloadingHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class AliasOverloadingHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    AliasOverloadingHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/$a" */
    @NotNull public abstract Result<Html> target(@NotNull String a);

    /** Invoked for route "/$a/$b" */
    @NotNull public abstract Result<Html> target(@NotNull String a, @NotNull String b);

    /** Invoked for route "/$a/$b/$c" */
    @NotNull public abstract Result<Html> target(@NotNull String a, @NotNull String b, @NotNull String c);

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(AliasOverloadingHandler.class);

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
