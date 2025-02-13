package tekgenesis.console;

enum CacheType
{
    ENTITIES     : "Entity";
    USER_CACHE     : "User";
}

form CacheForm "Cache" on_load onLoad
{
    disable : Boolean, internal, default true;
    horizontal {
        clusterName "Cluster Name": String, display, label_col 4, col 4;
    };
    v1 "" : horizontal {
        vEntities "": vertical
        {
            searchEntityBox "Search" : text_field(255), optional, on_change filterEntities;
            searchCacheType "Cache Type ":CacheType, radio_group, default ENTITIES, on_change filterEntities;
            cacheTab "": table(10), sortable , on_click selectEntity {
                cacheName "Name" : String, display;
                cacheType "Mode": String, display;
                cacheMode : String, internal;
                clearCache : label,  icon trash_o,  tooltip "Clear the cache for this entity",  on_click clearCache,  hide when disable;

            };

        };
        vChart "": vertical {
            vCacheName "" : String, display;
            entityChart "" : chart(column),on_click nodeSelected {
                serverNode: String;
                cacheEntries : Int;
            };
            vOptions "": horizontal {
                selectedNode "Node: ": String,display,  hide when disable ;
                dumpCache : label,  icon download,  tooltip "Download cache content as CSV",  on_click dumpCache,  hide when disable ;
            };
        };
    };
}