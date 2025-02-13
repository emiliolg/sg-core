package tekgenesis.admin;

form UpdateVersionForm
    on_load onLoad
    on_schedule refresh 2
{
    horizontal {
        branch "Branch name": String(30);
        version "Build #": Int;
        update "Update": button, on_click updateVersion,style "margin-left-5",tooltip "Remember to restart each Server Node in order to make available the new version";
    };
    refreshActived: Boolean,internal;
    updateTableLog "Log" : table(8) {
        serverNode: String, display;
        updateStatus : Int,internal;
        style :String,internal;
        status :String,display;
        viewLog "Output" : String,display,on_click showOutputLog, content_style "fa fa-search";
        log:String, internal;
    };

    logDetails: text_area,disable,style "no-label", content_style "logView";
}

form ServerNodesForm "Server Nodes" on_load loadNodesInfo
{
    updateAvailable:Boolean,internal;
    disable : Boolean,internal, default true;
    canRestartCluster:Boolean,internal,default false;
    selectedServerNode:Int,internal;

    horizontal {
        refresh : button, icon refresh,on_click refreshForm, hide when disable;
        restartCluster "Restart Cluster": button,on_click openRestartDialog, icon repeat,disable when !canRestartCluster,hide when disable;
        updateVersion "Update Version" : button, on_click openVersionDialog, style "pull-right margin-right-10" , disable when !updateAvailable,hide when disable;
    };

    confirmRestartCluster "Restart Cluster": dialog
    {
        msg "Are you sure to restart the cluster ?": label;
        runningTask : String,display,content_style "label label-warning";
        footer {
            restartC "Restart": button,on_click restartCluster;
            close "Close" : button, on_click closeDialog,content_style "margin-left-10";
        };
    };

    confirmRestartNode "Restart Node": dialog
    {
        nodeIx: Int,internal;
        nodeNameLabel : String,display;
        msgNode "Are you sure to restart the node ?": label;
        runningTaskOnNode : String,display,content_style "label label-warning";
        footer {
            restartN "Restart": button,on_click restartNode;
            closeN "Close" : button, on_click closeResetNodeDialog,content_style "margin-left-10";
        };
    };

    nodesTab : table  {
        available: Boolean,internal;
        isRestartable: Boolean,internal;
        masterStyle: Boolean,internal;
        isMaster : label , icon_expr masterStyle ? "check" : "" ;
        nodeImg : image , style  "no-label";
        nodeName "Server Node":display,style "no-label bold";

        heapMemoryUsage "Heap Memory Usage": vertical
        {
            vertical,col 4 {
                pSEdenSpaceTt:internal;
                pSEdenSpaceL "Eden":label;
                pSEdenSpace "Eden": progress,  content_style pSEdenSpace > 80? "progress-bar-danger" : (pSEdenSpace < 50 ? "progress-bar-success" : "progress-bar-warning" ) ,placeholder pSEdenSpace + "%",tooltip pSEdenSpaceTt,no_label;
            };
            vertical,col 4 {
                pSSurvivorSpaceTt:internal;
                pSSurvivorSpaceL "Survivor":label;
                pSSurvivorSpace "Survivor": progress,  content_style pSSurvivorSpace > 80? "progress-bar-danger" : (pSSurvivorSpace < 50 ? "progress-bar-success" : "progress-bar-warning" ) ,placeholder pSSurvivorSpace + "%",tooltip pSSurvivorSpaceTt,no_label;
            };
            vertical,col 4 {
                pSOldGenTt:internal;
                pSOldGenL "Old Gen":label;
                pSOldGen : progress,  content_style pSOldGen > 80? "progress-bar-danger" : (pSOldGen < 50 ? "progress-bar-success" : "progress-bar-warning" ) ,placeholder pSOldGen + "%" ,tooltip pSOldGenTt,no_label;
            };

        };

        uptime "Uptime": String,display;
        components "Components" : list_box,disable,style "componentList";

        threadDump " Thread Dump" : button, on_click threadDump, icon eye ,disable when !available || disable;
        horizontal,hide when disable {
            restart "Restart": button , on_click openRestartNodeDialog , icon repeat, disable when !isRestartable;
        };
     };
}


