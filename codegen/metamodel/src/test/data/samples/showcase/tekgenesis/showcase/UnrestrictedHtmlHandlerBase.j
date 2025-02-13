package tekgenesis.showcase;

import tekgenesis.common.service.Call;
import tekgenesis.service.Factory;
import tekgenesis.service.HandlerInstance;
import tekgenesis.service.html.Html;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;
import static tekgenesis.common.service.Method.GET;

/** 
 * Generated base class for handler: UnrestrictedHtmlHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class UnrestrictedHtmlHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    UnrestrictedHtmlHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/showcase/main/unrestricted" */
    @NotNull public abstract Result<Html> main();

    /** Invoked for route "/showcase/template/unrestricted" */
    @NotNull public abstract Result<Html> template();

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(UnrestrictedHtmlHandler.class);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/showcase/main/unrestricted" {@link UnrestrictedHtmlHandler#main()} */
        @NotNull public static Call main() {
            return new Call(GET, "/showcase/main/unrestricted", "ShowcaseMainUnrestricted");
        }

        /** Reverse route for "/showcase/template/unrestricted" {@link UnrestrictedHtmlHandler#template()} */
        @NotNull public static Call template() {
            return new Call(GET, "/showcase/template/unrestricted", "ShowcaseTemplateUnrestricted");
        }

    }
}
