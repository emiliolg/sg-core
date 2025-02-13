package tekgenesis.console;

enum REQUEST
{
    ALL:"All";
}

form Metrics
    on_load load
{
    horizontal {
        clusterName "Cluster Name": String, display, label_col 4, col 4;
    };
	metrics : tabs,on_change selectTab
	{
		t1 "Database": vertical {
			db:subform(DatabaseMetrics), inline;
		};
	};
}

form DatabaseMetrics "Database" on_load onLoad
{
    horizontal {
        pools "Connection Pool" : combo_box, on_change selectPool,  col 6,  label_col 4;
        refreshPools : button, on_click selectPool,  icon refresh, content_style "btn-mini",no_label;
    };

    connectionsTab "Connections": table , sortable {
        conNode "Server Node" : String, display;
        totalConnections "Total": Int, display;
        totalActiveConnections "Active": Int, display;
        totalIdleConnections "Idle": Int, display;
        totalWaitingConnections "Waiting ": Int, display;
    };
}