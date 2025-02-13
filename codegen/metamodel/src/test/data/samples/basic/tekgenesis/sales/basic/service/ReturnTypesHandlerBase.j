package tekgenesis.sales.basic.service;

import java.math.BigDecimal;
import tekgenesis.common.service.Call;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Resource.Entry;
import tekgenesis.service.Factory;
import tekgenesis.service.HandlerInstance;
import tekgenesis.service.html.Html;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;
import static tekgenesis.common.service.Method.GET;

/** 
 * Generated base class for handler: ReturnTypesHandler.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "UtilityClassWithoutPrivateConstructor", "UnusedParameters"})
public abstract class ReturnTypesHandlerBase
    extends HandlerInstance
    implements LoggableInstance
{

    //~ Constructors .............................................................................................................

    ReturnTypesHandlerBase(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/return/Boolean" */
    @NotNull public abstract Result<Boolean> returnBoolean();

    /** Invoked for route "/return/Real" */
    @NotNull public abstract Result<Double> returnReal();

    /** Invoked for route "/return/Decimal" */
    @NotNull public abstract Result<BigDecimal> returnDecimal();

    /** Invoked for route "/return/Int" */
    @NotNull public abstract Result<Integer> returnInt();

    /** Invoked for route "/return/DateTime" */
    @NotNull public abstract Result<DateTime> returnDateTime();

    /** Invoked for route "/return/Date" */
    @NotNull public abstract Result<DateOnly> returnDate();

    /** Invoked for route "/return/Type" */
    @NotNull public abstract Result<Product> returnType();

    /** Invoked for route "/return/Enum" */
    @NotNull public abstract Result<State> returnEnum();

    /** Invoked for route "/return/Html" */
    @NotNull public abstract Result<Html> returnHtml();

    /** Invoked for route "/return/Resource" */
    @NotNull public abstract Result<Entry> returnResource();

    /** Invoked for route "/return/Void" */
    @NotNull public abstract Result<Void> returnVoid();

    /** Invoked for route "/return/Any" */
    @NotNull public abstract Result<byte[]> returnAny();

    @Override @NotNull public Logger logger() { return logger; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(ReturnTypesHandler.class);

    //~ Inner Classes ............................................................................................................

    public static final class Routes
    {

        //~ Methods ..................................................................................................................

        /** Reverse route for "/return/Boolean" {@link ReturnTypesHandler#returnBoolean()} */
        @NotNull public static Call returnBoolean() { return new Call(GET, "/return/Boolean", "ReturnBoolean"); }

        /** Reverse route for "/return/Real" {@link ReturnTypesHandler#returnReal()} */
        @NotNull public static Call returnReal() { return new Call(GET, "/return/Real", "ReturnReal"); }

        /** Reverse route for "/return/Decimal" {@link ReturnTypesHandler#returnDecimal()} */
        @NotNull public static Call returnDecimal() { return new Call(GET, "/return/Decimal", "ReturnDecimal"); }

        /** Reverse route for "/return/Int" {@link ReturnTypesHandler#returnInt()} */
        @NotNull public static Call returnInt() { return new Call(GET, "/return/Int", "ReturnInt"); }

        /** Reverse route for "/return/DateTime" {@link ReturnTypesHandler#returnDateTime()} */
        @NotNull public static Call returnDateTime() { return new Call(GET, "/return/DateTime", "ReturnDateTime"); }

        /** Reverse route for "/return/Date" {@link ReturnTypesHandler#returnDate()} */
        @NotNull public static Call returnDate() { return new Call(GET, "/return/Date", "ReturnDate"); }

        /** Reverse route for "/return/Type" {@link ReturnTypesHandler#returnType()} */
        @NotNull public static Call returnType() { return new Call(GET, "/return/Type", "ReturnType"); }

        /** Reverse route for "/return/Enum" {@link ReturnTypesHandler#returnEnum()} */
        @NotNull public static Call returnEnum() { return new Call(GET, "/return/Enum", "ReturnEnum"); }

        /** Reverse route for "/return/Html" {@link ReturnTypesHandler#returnHtml()} */
        @NotNull public static Call returnHtml() { return new Call(GET, "/return/Html", "ReturnHtml"); }

        /** Reverse route for "/return/Resource" {@link ReturnTypesHandler#returnResource()} */
        @NotNull public static Call returnResource() { return new Call(GET, "/return/Resource", "ReturnResource"); }

        /** Reverse route for "/return/Void" {@link ReturnTypesHandler#returnVoid()} */
        @NotNull public static Call returnVoid() { return new Call(GET, "/return/Void", "ReturnVoid"); }

        /** Reverse route for "/return/Any" {@link ReturnTypesHandler#returnAny()} */
        @NotNull public static Call returnAny() { return new Call(GET, "/return/Any", "ReturnAny"); }

    }
}
