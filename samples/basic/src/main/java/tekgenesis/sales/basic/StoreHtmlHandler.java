
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.service.html.Html;

import static tekgenesis.sales.basic.StoreServiceHandler.stores;

/**
 * User class for Handler: StoreHtmlHandler
 */
public class StoreHtmlHandler extends StoreHtmlHandlerBase {

    //~ Instance Fields ..............................................................................................................................

    private final Views views;

    //~ Constructors .................................................................................................................................

    StoreHtmlHandler(@NotNull Factory factory) {
        super(factory);
        views = factory.html(Views.class);
    }

    //~ Methods ......................................................................................................................................

    /** Invoked for route "/stores". */
    @NotNull @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Result<Html> home() {
        final Seq<StoreType> stores = stores();
        return ok(views.storeTemplate("Stores", views.storeStores(JsonMapping.toJson(stores))));
    }
}
