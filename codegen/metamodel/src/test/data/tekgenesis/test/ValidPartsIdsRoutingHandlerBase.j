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
 * Generated base class for handler: ValidPartsIdsRoutingHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class ValidPartsIdsRoutingHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    ValidPartsIdsRoutingHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/a/$id" */
    @NotNull public abstract Result<Html> someA(@NotNull String id);

    /** Invoked for route "/b/$id2" */
    @NotNull public abstract Result<Html> someB(@NotNull String id2);

    /** Invoked for route "/c/$id2and3" */
    @NotNull public abstract Result<Html> someC(@NotNull String id2and3);

    /** Invoked for route "/d/$iso2" */
    @NotNull public abstract Result<Html> someD(@NotNull String iso2);

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(ValidPartsIdsRoutingHandler.class);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/a/$id" {@link ValidPartsIdsRoutingHandler#someA(String)} */
        @NotNull public static Call someA(@NotNull String id) {
            return new Call(GET, String.format("/a/%s", Strings.truncate(id, 255)), "AId");
        }

        /** Reverse route for "/b/$id2" {@link ValidPartsIdsRoutingHandler#someB(String)} */
        @NotNull public static Call someB(@NotNull String id2) {
            return new Call(GET, String.format("/b/%s", Strings.truncate(id2, 255)), "BId2");
        }

        /** Reverse route for "/c/$id2and3" {@link ValidPartsIdsRoutingHandler#someC(String)} */
        @NotNull public static Call someC(@NotNull String id2and3) {
            return new Call(GET, String.format("/c/%s", Strings.truncate(id2and3, 255)), "CId2and3");
        }

        /** Reverse route for "/d/$iso2" {@link ValidPartsIdsRoutingHandler#someD(String)} */
        @NotNull public static Call someD(@NotNull String iso2) {
            return new Call(GET, String.format("/d/%s", Strings.truncate(iso2, 255)), "DIso2");
        }

    }
}
