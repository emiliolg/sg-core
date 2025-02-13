
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.service.html.Html;
import tekgenesis.showcase.sui.Views;

/**
 * User class for Handler: UnrestrictedHtmlHandler
 */
public class UnrestrictedHtmlHandler extends UnrestrictedHtmlHandlerBase {

    //~ Instance Fields ..............................................................................................................................

    private final Views views;

    //~ Constructors .................................................................................................................................

    UnrestrictedHtmlHandler(@NotNull Factory factory) {
        super(factory);
        views = factory.html(Views.class);
    }

    //~ Methods ......................................................................................................................................

    /** Invoked for route "/showcase/main". */
    @NotNull @Override public Result<Html> main() {
        return ok(views.main("Some Main Unrestricted"));
    }

    /** Invoked for route "/showcase/template". */
    @NotNull @Override public Result<Html> template() {
        return ok(views.template("Template Unrestricted", views.fragmentExternal()));
    }
}
