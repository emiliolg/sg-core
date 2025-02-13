package tekgenesis.test;

import tekgenesis.common.service.Call;
import tekgenesis.service.Factory;
import tekgenesis.service.HandlerInstance;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;
import tekgenesis.common.collections.Seq;
import static tekgenesis.common.service.Method.POST;

/** 
 * Generated base class for handler: TypeAliasOnHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class TypeAliasOnHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    TypeAliasOnHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/stringAlias" */
    @NotNull public abstract Result<String> stringAlias(@NotNull String body);

    /** Invoked for route "/stringAliasArray" */
    @NotNull public abstract Result<Seq<String>> stringAliasArray(@NotNull Seq<String> body);

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(TypeAliasOnHandler.class);

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
