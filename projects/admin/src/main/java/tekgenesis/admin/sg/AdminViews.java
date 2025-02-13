
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin.sg;

import org.jetbrains.annotations.NotNull;

import tekgenesis.admin.StatusService;
import tekgenesis.admin.notice.Advice;
import tekgenesis.admin.status.DatabaseInfo;
import tekgenesis.admin.status.MemoryInfo;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.context.Context;
import tekgenesis.service.html.Html;
import tekgenesis.service.html.HtmlBuilder;

/**
 * Admin mail views.
 */
public class AdminViews {

    //~ Constructors .................................................................................................................................

    private AdminViews() {}

    //~ Methods ......................................................................................................................................

    /** Notices mail. */
    @NotNull public static Html notices(Seq<Advice> notices) {
        return views().sgMailNoticeMail(notices);
    }

    /** Status page. */
    @NotNull public static Html status(Seq<StatusService> services) {
        return views().sgStatusStatus(services.getFirst().get().html(), services.map(StatusService::name).toList());
    }

    /** Sui Generis status html. */
    public static Html statusSuiGeneris(String version, MemoryInfo memory, DatabaseInfo db) {
        return views().sgStatusSuigenerisStatus(version, memory, db);
    }

    @NotNull private static Views views() {
        return new Views(Context.getSingleton(HtmlBuilder.class));
    }
}
