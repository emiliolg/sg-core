package tekgenesis.console;

form DatabaseForm "Database" on_load onLoad
{
    dbUrl "Url": String,display;
    dbDriver "Driver": String,display;
    enforceVersion "Enforce Version": Boolean,display;
    dbType "Type": String,display;
    sessionCacheSize "Session Cache Size": Int,display;
    horizontal {
        hikariPools "Connection Pool" : combo_box,on_change selectPool, col 6, label_col 4;
        refreshPools "": button,on_click selectPool, icon refresh,content_style "btn-mini";
    };

    connectionsTab "Connections": table ,sortable {
            conNode "Server Node" : String,display;
            totalConnections "Total": Int, display;
            totalActiveConnections "Active": Int, display;
            totalIdleConnections "Idle": Int, display;
            totalWaitingConnections "Waiting ": Int, display;

        };
}