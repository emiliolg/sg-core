package tekgenesis.test;

import tekgenesis.common.service.Call;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.service.Factory;
import tekgenesis.service.HandlerInstance;
import tekgenesis.service.html.Html;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Reals;
import tekgenesis.service.Result;
import tekgenesis.common.core.Strings;
import static tekgenesis.common.service.Method.GET;

/** 
 * Generated base class for handler: TypedHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class TypedHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    TypedHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/a/$id" */
    @NotNull public abstract Result<Html> str(@NotNull String id);

    /** Invoked for route "/b/$id" */
    @NotNull public abstract Result<Html> real(double id);

    /** Invoked for route "/c/$id" */
    @NotNull public abstract Result<Html> date(@NotNull DateOnly id);

    /** Invoked for route "/d/$id" */
    @NotNull public abstract Result<Html> time(@NotNull DateTime id);

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(TypedHandler.class);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/a/$id" {@link TypedHandler#str(String)} */
        @NotNull public static Call str(@NotNull String id) {
            return new Call(GET, String.format("/a/%s", Strings.truncate(id, 255)), "AId");
        }

        /** Reverse route for "/b/$id" {@link TypedHandler#real(double)} */
        @NotNull public static Call real(double id) {
            return new Call(GET, String.format("/b/%s", Reals.checkSigned("id", id, true)), "BId");
        }

        /** Reverse route for "/c/$id" {@link TypedHandler#date(DateOnly)} */
        @NotNull public static Call date(@NotNull DateOnly id) { return new Call(GET, String.format("/c/%s", id), "CId"); }

        /** Reverse route for "/d/$id" {@link TypedHandler#time(DateTime)} */
        @NotNull public static Call time(@NotNull DateTime id) { return new Call(GET, String.format("/d/%s", id), "DId"); }

    }
}
