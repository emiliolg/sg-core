package tekgenesis.sales.basic.service;

import java.math.BigDecimal;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Resource.Entry;
import tekgenesis.service.Factory;
import tekgenesis.service.html.Html;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;

/** User class for Handler: ReturnTypesHandler */
public class ReturnTypesHandler
    extends ReturnTypesHandlerBase
{

    //~ Constructors .............................................................................................................

    ReturnTypesHandler(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/return/Boolean" */
    @Override @NotNull public Result<Boolean> returnBoolean() { return notImplemented(); }

    /** Invoked for route "/return/Real" */
    @Override @NotNull public Result<Double> returnReal() { return notImplemented(); }

    /** Invoked for route "/return/Decimal" */
    @Override @NotNull public Result<BigDecimal> returnDecimal() { return notImplemented(); }

    /** Invoked for route "/return/Int" */
    @Override @NotNull public Result<Integer> returnInt() { return notImplemented(); }

    /** Invoked for route "/return/DateTime" */
    @Override @NotNull public Result<DateTime> returnDateTime() { return notImplemented(); }

    /** Invoked for route "/return/Date" */
    @Override @NotNull public Result<DateOnly> returnDate() { return notImplemented(); }

    /** Invoked for route "/return/Type" */
    @Override @NotNull public Result<Product> returnType() { return notImplemented(); }

    /** Invoked for route "/return/Enum" */
    @Override @NotNull public Result<State> returnEnum() { return notImplemented(); }

    /** Invoked for route "/return/Html" */
    @Override @NotNull public Result<Html> returnHtml() { return notImplemented(); }

    /** Invoked for route "/return/Resource" */
    @Override @NotNull public Result<Entry> returnResource() { return notImplemented(); }

    /** Invoked for route "/return/Void" */
    @Override @NotNull public Result<Void> returnVoid() { return notImplemented(); }

    /** Invoked for route "/return/Any" */
    @Override @NotNull public Result<byte[]> returnAny() { return notImplemented(); }

}
