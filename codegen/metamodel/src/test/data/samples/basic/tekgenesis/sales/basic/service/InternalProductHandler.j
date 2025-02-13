package tekgenesis.sales.basic.service;

import tekgenesis.service.Factory;
import tekgenesis.service.html.Html;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;

/** User class for Handler: InternalProductHandler */
public class InternalProductHandler
    extends InternalProductHandlerBase
{

    //~ Constructors .............................................................................................................

    InternalProductHandler(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/internal/message" */
    @Override @NotNull public Result<Html> internal() { return notImplemented(); }

    /** Invoked for route "/internal/timestamp" */
    @Override @NotNull public Result<Html> timestamp() { return notImplemented(); }

}
