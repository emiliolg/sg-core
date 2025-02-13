
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin;

import org.jetbrains.annotations.NotNull;

import tekgenesis.admin.sg.Views;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.service.html.Html;

/**
 * User class for Handler: IndexHandler
 */
public class IndexHandler extends IndexHandlerBase {

    //~ Instance Fields ..............................................................................................................................

    private final Views views;

    //~ Constructors .................................................................................................................................

    IndexHandler(@NotNull Factory factory) {
        super(factory);
        views = factory.html(Views.class);
    }

    //~ Methods ......................................................................................................................................

    /** Invoked for route "/sg/index". */
    @NotNull @Override public Result<Html> index() {
        return indexMustache();
    }

    /** Invoked for route "/sg/index.jade". */
    @NotNull @Override public Result<Html> indexJade() {
        return ok(views.sgIndexJade(TITLE, true));
    }

    /** Invoked for route "/sg/index.mustache". */
    @NotNull @Override public Result<Html> indexMustache() {
        return ok(views.sgIndexMustache(TITLE, true));
    }

    /** Invoked for route "/sg/index.xhtml". */
    @NotNull @Override public Result<Html> indexXhtml() {
        return ok(views.sgIndexXhtml(TITLE));
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String TITLE = "TekGenesis";
}  // end class IndexHandler
