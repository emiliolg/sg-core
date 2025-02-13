package tekgenesis.console;

form EntitiesForm "Entities and Remote Views" on_load onLoad
{
    horizontal {
        clusterName "Cluster Name": String, display, label_col 4, col 4;
    };
    disable : Boolean, internal, default true;
    searchBox "Search" : text_field(255),optional, on_change filterEntities;

    entitiesTable : table(8),sortable {
        entityName "Name " : display;
        rebuildEntityIndex : label, icon repeat, tooltip "Rebuild the index", on_click rebuildEntityIndex, hide when disable;
        clearEntityCache : label, icon trash_o, tooltip "Clear the cache", on_click clearEntityCache, hide when disable;
        cookEntities : label, icon magic, tooltip "Fix entity data", on_click fixEntities, hide when disable;
    };
}

form ViewResource
primary_key resource
{
    resource: String, internal;
    resources : gallery;
}