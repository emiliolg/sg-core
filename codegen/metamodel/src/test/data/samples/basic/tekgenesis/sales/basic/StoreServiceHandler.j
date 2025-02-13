package tekgenesis.sales.basic;

import tekgenesis.service.Factory;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;

/** User class for Handler: StoreServiceHandler */
public class StoreServiceHandler
    extends StoreServiceHandlerBase
{

    //~ Constructors .............................................................................................................

    StoreServiceHandler(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/service/store/$key" */
    @Override @NotNull public Result<StoreType> get(@NotNull String key) { return notImplemented(); }

}
