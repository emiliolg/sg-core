
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.samples.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.service.HeaderNames;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.service.html.Html;

/**
 * User class for Handler: ServiceHandler
 */
public class ServiceHandler extends ServiceHandlerBase {

    //~ Constructors .................................................................................................................................

    ServiceHandler(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<String> all(@NotNull String passPhrase, int range, @Nullable String from, @NotNull Seq<String> currencies) {
        final String result = String.format("Result for parameters: from '%s', range '%s', passPhrase '%s', currencies '%s'",
                from,
                range,
                passPhrase,
                currencies.mkString("(", ",", ")"));
        return ok(result).withCache(1);
    }

    @NotNull @Override public Result<String> arrayCreate(@NotNull Seq<Product> body) {
        return ok(String.format("Created %s products", body.size()));
    }

    @NotNull @Override public Result<String> create(@NotNull String pid, @NotNull Product body) {
        final Product p = new Product(pid);
        p.setModel(body.getModel());
        p.setDescription(body.getDescription());
        p.setPrice(body.getPrice());
        return ok(p.getModel());
    }

    @NotNull @Override public Result<Html> list(@NotNull String lid) {
        return this.<Html>ok().withHeader(HeaderNames.CACHE_CONTROL, "max-age=2628000, public");
    }

    @NotNull @Override public Result<Html> menu(@NotNull String mid) {
        return notImplemented();
    }
}
