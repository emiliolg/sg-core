
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

import tekgenesis.common.core.DateTime;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.service.html.Html;

/**
 * User class for Handler: InternalProductHandler
 */
public class InternalProductHandler extends InternalProductHandlerBase {

    //~ Instance Fields ..............................................................................................................................

    private final Views views;

    //~ Constructors .................................................................................................................................

    InternalProductHandler(@NotNull Factory factory) {
        super(factory);
        views = factory.html(Views.class);
    }

    //~ Methods ......................................................................................................................................

    /** Invoked for route "/internal/message". */
    @NotNull @Override public Result<Html> internal() {
        // Above route can only be referenced from an internal request
        return ok(views.httpTestInternal());
    }

    @NotNull @Override public Result<Html> timestamp() {
        return ok(views.httpTestTimestamp(DateTime.current().toString()));
    }
}
