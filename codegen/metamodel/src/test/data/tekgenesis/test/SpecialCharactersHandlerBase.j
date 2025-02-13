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
 * Generated base class for handler: SpecialCharactersHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class SpecialCharactersHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    SpecialCharactersHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/$i/some-path_with.special/characters/$spanning*" */
    @NotNull public abstract Result<Html> target(@NotNull String i, @NotNull String spanning);

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(SpecialCharactersHandler.class);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/$i/some-path_with.special/characters/$spanning*" {@link SpecialCharactersHandler#target(String, String)} */
        @NotNull public static Call target(@NotNull String i, @NotNull String spanning) {
            return new Call(GET, String.format("/%s/some-path_with.special/characters/%s", Strings.truncate(i, 255), Strings.truncate(spanning, 255)), "ISome-path_with.specialCharactersSpanning");
        }

    }
}
