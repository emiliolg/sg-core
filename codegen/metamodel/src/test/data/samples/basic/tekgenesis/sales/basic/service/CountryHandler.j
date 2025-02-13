package tekgenesis.sales.basic.service;

import tekgenesis.service.Factory;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;

/** User class for Handler: CountryHandler */
public class CountryHandler
    extends CountryHandlerBase
{

    //~ Constructors .............................................................................................................

    CountryHandler(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/country" */
    @Override @NotNull public Result<Seq<Country>> list(boolean success) { return notImplemented(); }

    /** Invoked for route "/country" */
    @Override @NotNull public Result<Void> post(@NotNull Country body, boolean success) { return notImplemented(); }

    /** Invoked for route "/country/$iso2" */
    @Override @NotNull public Result<Void> put(@NotNull String iso2, @NotNull Country body, boolean success) { return notImplemented(); }

    /** Invoked for route "/country/$iso2" */
    @Override @NotNull public Result<Void> delete(@NotNull String iso2, boolean success) { return notImplemented(); }

    /** Invoked for route "/country/getAndInsert" */
    @Override @NotNull public Result<Country> getAndInsert(boolean success) { return notImplemented(); }

    /** Invoked for route "/country/$iso2" */
    @Override @NotNull public Result<Country> get(@NotNull String iso2) { return notImplemented(); }

    /** Invoked for route "/country/exception" */
    @Override @NotNull public Result<Country> postWithException(@NotNull Country body) { return notImplemented(); }

}
