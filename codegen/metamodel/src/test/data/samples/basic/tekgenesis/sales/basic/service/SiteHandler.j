package tekgenesis.sales.basic.service;

import tekgenesis.common.core.Resource.Entry;
import tekgenesis.service.Factory;
import tekgenesis.service.html.Html;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.service.Result;

/** User class for Handler: SiteHandler */
public class SiteHandler
    extends SiteHandlerBase
{

    //~ Constructors .............................................................................................................

    SiteHandler(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/site" */
    @Override @NotNull public Result<Html> home() { return notImplemented(); }

    /** Invoked for route "/site/cache" */
    @Override @NotNull public Result<Html> cache() { return notImplemented(); }

    /** Invoked for route "/site/product/$pid" */
    @Override @NotNull public Result<Html> product(@NotNull String pid) { return notImplemented(); }

    /** Invoked for route "/site/products" */
    @Override @NotNull public Result<Html> products() { return notImplemented(); }

    /** Invoked for route "/site/product/$pid/image" */
    @Override @NotNull public Result<Entry> image(@NotNull String pid) { return notImplemented(); }

    /** Invoked for route "/site/failure" */
    @Override @NotNull public Result<Html> failure(int method) { return notImplemented(); }

    /** Invoked for route "/site/search" */
    @Override @NotNull public Result<Html> search(@Nullable String q) { return notImplemented(); }

}
