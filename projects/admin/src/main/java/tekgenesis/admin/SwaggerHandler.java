
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin;

import java.util.function.Predicate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.admin.sg.Views;
import tekgenesis.codegen.swagger.SwaggerGenerator;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.metadata.handler.Handler;
import tekgenesis.metadata.handler.Route;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.service.html.Html;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.media.MediaType.APPLICATION_JSON;
import static tekgenesis.common.service.HeaderNames.X_FORWARDED_HOST;
import static tekgenesis.common.service.HeaderNames.X_FORWARDED_PROTO;

/**
 * User class for Handler: RestApiHandler
 */
public class SwaggerHandler extends SwaggerHandlerBase {

    //~ Instance Fields ..............................................................................................................................

    private final Views views;

    //~ Constructors .................................................................................................................................

    SwaggerHandler(@NotNull Factory factory) {
        super(factory);
        views = factory.html(Views.class);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<Html> restApi() {
        if (!AdminHandler.hasDeveloperRole()) return unauthorized();
        return ok(views.sgRestapi());
    }

    @NotNull @Override public Result<String> swagger(@Nullable final String domain, final boolean html) {
        if (!AdminHandler.hasDeveloperRole()) return unauthorized();

        final String host     = req.getHeaders().getFirst(X_FORWARDED_HOST).orElse(req.getHost());
        final String protocol = req.getHeaders().getFirst(X_FORWARDED_PROTO).orElse(req.getScheme());

        final Predicate<Handler> handlerPredicate = isEmpty(domain) ? (o -> true) : h -> (h != null && h.getDomain().startsWith(domain));
        final Predicate<Route>   routePredicate   = html ? (o -> true) : (r -> r != null && !r.getType().isHtml());
        final SwaggerGenerator   swagger          = SwaggerGenerator.create(host, Colls.listOf(protocol), handlerPredicate, routePredicate);

        final ObjectMapper mapper = JsonMapping.json();
        final ObjectNode   result = swagger.json(mapper.createObjectNode());
        return castResult(result).withContentType(APPLICATION_JSON);
    }

    private Result<String> castResult(ObjectNode result) {
        // Hack! Until SG support Json type :S
        return cast(ok(result));
    }
}  // end class SwaggerHandler
