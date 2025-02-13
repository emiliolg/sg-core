package tekgenesis.sales.basic.service;

import tekgenesis.service.Factory;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;

/** User class for Handler: ExternalServicesHandler */
public class ExternalServicesHandler
    extends ExternalServicesHandlerBase
{

    //~ Constructors .............................................................................................................

    ExternalServicesHandler(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/invocations/compumundo/services" */
    @Override @NotNull public Result<String> compumundo() { return notImplemented(); }

    /** Invoked for route "/invocations/garbarino/services" */
    @Override @NotNull public Result<String> garbarino() { return notImplemented(); }

}
