package tekgenesis.sales.basic;

import tekgenesis.service.Factory;
import tekgenesis.service.html.Html;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;

/** User class for Handler: StoreHtmlHandler */
public class StoreHtmlHandler
    extends StoreHtmlHandlerBase
{

    //~ Constructors .............................................................................................................

    StoreHtmlHandler(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/stores" */
    @Override @NotNull public Result<Html> home() { return notImplemented(); }

}
