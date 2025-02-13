
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.common.core.Resource.Entry;
import tekgenesis.common.service.Status;
import tekgenesis.sales.basic.Product;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.service.html.Html;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.service.HeaderNames.IF_NONE_MATCH;
import static tekgenesis.common.service.Status.NOT_MODIFIED;
import static tekgenesis.sales.basic.service.Products.createProductSequence;
import static tekgenesis.sales.basic.service.Products.tv;

/**
 * User class for Handler: SiteHandler
 */
public class SiteHandler extends SiteHandlerBase {

    //~ Instance Fields ..............................................................................................................................

    private final Views views;

    //~ Constructors .................................................................................................................................

    SiteHandler(@NotNull Factory factory) {
        super(factory);
        views = factory.html(Views.class);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<Html> cache() {
        return ok(views.httpTestCache());
    }

    @NotNull @Override public Result<Html> failure(int method) {
        return failureMethod(method, views.httpTestSite());
    }

    @NotNull @Override public Result<Html> home() {
        return ok(views.httpTestSite());
    }

    @NotNull @Override public Result<Entry> image(@NotNull String pid) {
        final Product product = Product.find(pid);
        if (product == null) return notFound();
        final Option<Resource> resource = product.getImageResources().getFirst();
        return resource.map(image -> {
                final String etag = req.getHeaders().getOrEmpty(IF_NONE_MATCH);
                return !etag.equals(image.getMaster().getSha()) ? ok(image.getMaster()).withCache(100) : this.<Entry>status(NOT_MODIFIED);
            }).orElseGet(this::notFound);
    }

    @NotNull @Override public Result<Html> product(@NotNull String pid) {
        return ok(views.httpTestProduct(tv(pid)));
    }

    @NotNull @Override public Result<Html> products() {
        return ok(views.httpTestProducts(createProductSequence()));
    }

    @NotNull @Override public Result<Html> search(@Nullable String q) {
        final Query query = new Query();
        query.setQ(notEmpty(q, ""));
        return ok(views.httpTestSearch(query));
    }

    @SuppressWarnings("MagicNumber")
    private Result<Html> failureMethod(int method, Html html) {
        switch (method) {
        case 400:
            return badRequest(html);
        case 401:
            return unauthorized(html);
        case 403:
            return forbidden(html);
        case 404:
            return notFound(html);
        case 500:
            return internalServerError(html);
        case 501:
            return notImplemented(html);
        default:
            return status(Status.fromCode(method), html);
        }
    }
}  // end class SiteHandler
