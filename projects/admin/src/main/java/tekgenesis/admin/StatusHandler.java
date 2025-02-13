
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin;

import java.util.ServiceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jetbrains.annotations.NotNull;

import tekgenesis.admin.sg.AdminViews;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.service.html.Html;

import static tekgenesis.admin.AdminHandler.hasAdminRole;
import static tekgenesis.common.Predefined.cast;

/**
 * User class for Handler: StatusHandler
 */
public class StatusHandler extends StatusHandlerBase {

    //~ Constructors .................................................................................................................................

    StatusHandler(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<Html> contentHtml(@NotNull String id) {
        final Option<StatusService> statusServices = services.getFirst(s -> s.name().equals(id));
        if (statusServices.isPresent()) return ok(statusServices.get().html());
        return notFound();
    }

    @NotNull @Override public Result<Html> statusHtml() {
        return ok(AdminViews.status(services));
    }

    @NotNull @Override public Result<Dummy> statusJson() {
        if (!hasAdminRole()) return unauthorized();
        final ObjectMapper mapper = JsonMapping.shared();
        final ObjectNode   status = mapper.createObjectNode();

        services.forEach(s -> {
            final ObjectNode node = status.putObject(s.name());

            final ObjectNode map = s.status();
            if (map != null) node.setAll(map);
        });

        return ok(cast(status));
    }

    //~ Static Fields ................................................................................................................................

    static final ImmutableList<StatusService> services = Colls.immutable(ServiceLoader.load(StatusService.class)).toList();
}
