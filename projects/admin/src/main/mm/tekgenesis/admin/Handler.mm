package tekgenesis.admin;

handler IndexHandler on_route "/sg" {
    "/index" : Html, index;
    "/index.jade" : Html, indexJade;
    "/index.mustache" : Html, indexMustache;
    "/index.xhtml" : Html, indexXhtml;
}

handler DocumentationHandler on_route "/sg" {
    "/help" : Html, helpIndex;
    "/help/$path*" : Html, help;
    "/view/source/$path*" : String, viewSource;
}

handler SwaggerHandler on_route "/sg" {
    "/restapi" : Html, restApi;
    "/swagger.json" : String, swagger, parameters {
        domain : String, optional;
        html : Boolean, default false;
    };
}

handler AdminHandler on_route "/sg/admin" {
    "/rebuild_index" : String, rebuildIndex, parameters {
        fqn : String, optional;
    };
    "/clear_cache" : String, clearCache, parameters {
        fqn : String, optional;
    };
    "/clear_user_cache" : String, clearUserCache, parameters {
        name : String;
    };
}

handler AdminDevHandler on_route "/sg/admin" unrestricted {
    "/refresh" : String, refresh;
    "/refresh/$resource*" : String, refreshResource;
}

handler SyncHandler on_route "/sg/sync" unrestricted {
    "/$path" : String, sync, parameters {
        sync_from : DateTime;
        key_from : String, optional;
        batch_size : Int, optional;
        field : String*;
        initial : Boolean, optional;
    };
}

handler StatusHandler on_route "/sg/status" {
    "/" : Html, statusHtml;
    "/json" : Dummy, statusJson;
    "/content/$id" : Html, contentHtml;
}
handler HealtchCheckHandler on_route "/sg/health_check" unrestricted {
    "/" : String, healthCheck;
}

handler RestHandler on_route "/sg" unrestricted {
    "/enum/$path" : String, enumJson;
//  "/api/$path" : String, api;
}

type Dummy {
    dummy: String;
}
