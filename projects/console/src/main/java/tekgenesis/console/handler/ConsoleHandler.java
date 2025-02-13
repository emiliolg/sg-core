
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console.handler;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.jetbrains.annotations.NotNull;

import tekgenesis.admin.notice.Advice;
import tekgenesis.admin.notice.State;
import tekgenesis.admin.notice.g.AdviceTable;
import tekgenesis.app.sse.SSEService;
import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.console.*;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.service.html.Html;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.service.HeaderNames.TEK_APP_TOKEN;
import static tekgenesis.console.handler.ConsoleMenuItem.build;

/**
 * User class for Handler: ConsoleHandler
 */
public class ConsoleHandler extends ConsoleHandlerBase {

    //~ Instance Fields ..............................................................................................................................

    private final ViewFactory views;

    //~ Constructors .................................................................................................................................

    ConsoleHandler(@NotNull Factory factory) {
        super(factory);
        views = factory.html(ViewFactory.class);
    }

    //~ Methods ......................................................................................................................................

    /** Invoked for route "/sg/cluster". */
    @NotNull @Override public Result<Html> console() {
        return ok(views.console(buildMenu()));
    }

    @NotNull @Override public Result<Void> transfer(@NotNull SessionTransfer sessionTransfer) {
        final SSEService<String> service = Predefined.cast(Context.getContext().getSingleton(SSEService.class));

        try {
            final String value = JsonMapping.shared().writeValueAsString(sessionTransfer);
            service.publish(sessionTransfer.getMachine(), value);
            return ok();
        }
        catch (final JsonProcessingException e) {
            return internalServerError();
        }
    }

    @NotNull @Override public Result<Void> transferLogin() {
        final Result<Void> ok = ok();

        req.getHeaders().getKeys().filter((k) -> k.toLowerCase().equals(TEK_APP_TOKEN.toLowerCase())).getFirst().ifPresent((k) ->
                ok.withCookie(TEK_APP_TOKEN, req.getHeaders().getOrEmpty(k)).withPath("/"));

        return ok;
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private Seq<ConsoleMenuItem> buildMenu() {
        return listOf(build(ClusterDashBoard.class.getName(), "Dashboard", "dashboard"),
            build(UsersForm.class.getName(), "Users", "group"),
            build(CacheForm.class.getName(), "Cache", "cloud"),
            build(DatabaseForm.class.getName(), "Database", "table"),
            build(EntitiesForm.class.getName(), "Entities", "columns"),
            build(TaskDashboard.class.getName(), "Tasks", "tasks"),
            build(MemosForm.class.getName(), "Memos", "plus-square"),
            build(EmailForm.class.getName(), "Email", "envelope-o"),
            build(Metrics.class.getName(), "Metrics", "bar-chart-o"),
            build(WebProxiesForm.class.getName(), "Proxies", "arrows"),
            build(EndpointsForm.class.getName(), "Endpoints", "globe"),
            build(ConfigurationForm.class.getName(), "Configuration", "gears"),
            build(AdviceForm.class.getName(), "Notices", "flag", getNotices()));
    }

    private int getNotices() {
        return (int) Advice.listWhere(AdviceTable.ADVICE.STATE.ne(State.DISMISSED)).count();
    }
}  // end class ConsoleHandler
