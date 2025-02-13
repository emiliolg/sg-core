package tekgenesis.console schema tek_console;

import tekgenesis.sg.ClusterConf;

enum ClusterStatus
{
    ACTIVE;
    DEACTIVE;
}

form ClusterAdminForm "Cluster Admin"
    on_load init
    primary_key clusterName
{
    disable : Boolean, internal,  default true;

    horizontal {
        clusterName "Cluster Name": String, display, label_col 4, col 3;
        restartCluster "Restart Cluster": button, on_click openRestartClusterDialog,  style "pull-right margin-right-10",  icon repeat, hide when disable;
        updateVersion "Update Version" : button,  on_click openUpdateDialog ,  style "pull-right margin-right-10",  hide when disable;
    };

    selectedServerNode:Int, internal;

    summaryGrp "Nodes": vertical  {
        nodeSummary: table(10)
        {
            available: Boolean, internal;
            nodeName "Server Node":display, style "no-label bold", link_form NodeAdminForm, link_pk nodeName;
            uptime "Uptime": String, display;
            components "Components" : display, multiple, disable, content_style "no_resize";
            services "Services" : display, multiple, disable, content_style "no_resize";
            dropdown,  icon cog {
                    editServices "Services" : label ,  on_click configureServices,  icon pencil,  disable when disable;
                    threadDump "Thread Dump" : label,  on_click generateThreadDump,  icon eye,  disable when disable ;
                };
            restart : button ,  on_click openRestartDialog ,  icon repeat, disable when disable;
        };
    };

    confirmRestartNode "Restart Node": dialog
    {
        nodeIx: Int, internal;
        nodeNameLabel : String, display;
        msgNode "Are you sure to restart the node ?": label;
        runningTaskOnNode : String, display, content_style "label label-warning";
        footer {
            restartNode "Restart": button, on_click restartNode;
            closeRestartDialog "Close" : button,  on_click closeResetNodeDialog, content_style "margin-left-10";
        };
    };

    confirmRestartCluster "Restart Cluster": dialog
    {
        msg "Are you sure to restart the cluster ?": label;
        runningTask : String, display, content_style "label label-warning";
        footer {
            restartC "Restart": button, on_click restartCluster;
            close "Close" : button,  on_click closeRestartClusterDialog, content_style "margin-left-10";
        };
    };
}

form UpdateClusterForm "Update Cluster"
    on_load onLoad
    on_schedule refresh 2
{
    horizontal {
        clusterName "Cluster Name": String, display, label_col 4, col 3;
    };

    horizontal {
        branch "Branch name": String(30), col 4;
        version "Build #": Int, col 4;
        update "Update": button,  on_click updateVersion, style "margin-left-5", tooltip "Remember to restart each Server Node in order to make available the new version";
    };
    healthCheck "Enable Health Check": check_box;
    refreshActived: Boolean, internal;
    updateTableLog "Log" : table(8) {
        member:String,internal;
        ip:String , internal;
        updateStatus : Int, internal;
        serverNode: String,display;
        link "Link" : label,link ip, target_blank;
        status "Status" :String, display;
        markAsReady "Ready" : button, hide when status != "Safe Mode", on_click markNodeAsReady;
        rollback "Rollback" : button,on_click rollbackNodeUpdate;
        viewLog "Output" : String, display, on_click showOutputLog,  content_style "fa fa-search";
        log:String,  internal;
    };

    logDetails: text_area, disable, style "no-label",  content_style "logView";
}

form NodeAdminForm "Node Admin"
    primary_key node
    on_load load
{

    horizontal {
        clusterName "Cluster Name": String, display, label_col 4, col 3;
    };

    disable : Boolean, internal,  default true;

    horizontal {
        node "Node": String, display, label_col 4, col 3;
        status : String, display, label_col 4, col 3,style status == "Ready" ? "green_text" : "red_text";
        restartCluster "Restart Node": button, on_click openRestartDialog,  style "pull-right margin-right-10",  icon repeat, hide when disable;
        changeStatus "Safe Mode": button, on_click changeStatus,  style "pull-right margin-right-10",  icon ambulance, hide when disable;
    };

    tab : tabs,  on_change tabChanged {
        t1 "Summary" : vertical,   col 0 {
            uptime "Uptime": String, display;
            components "Components" : display, multiple, disable, content_style "no_resize";
            services "Services" : display, multiple, disable, content_style "no_resize";
        };

        t2 "Services" : vertical,   col 0 {
            serviceForm:subform(Services),   inline;
        };

        t3 "Configuration" : horizontal,  col 0 {

            vTools "Http": vertical
            {
                horizontal ,  style " marginBottom15"{
                    xNodeHeaderLabel "Add X-Id-Ref Header To Responses": label;
                    horizontal, style xNodeHeader ? "marginLeft10  switch-container switch-container-green" : "marginLeft10  switch-container switch-container-gray"
                    {
                        horizontal,  style xNodeHeader ?  "switch switch-right" : "switch switch-left"  { };
                        xNodeHeader:toggle_button,  style "hidden-but",  on_ui_change applyXNodeHeader;
                        xNodeHeaderSwitch :display,  is xNodeHeader ? "On" : "Off" , style "switch-text";
                    };
                };
            };
        };

        vLogging "Logging": vertical
        {
            loggingForm:subform(LoggerConfiguration),   inline;
        };

        vProps "Properties": vertical,  col 0{
            searchPropsBox "Search" : text_field(255), optional,  on_change filterProperties;
            propertyTable : table(10),sortable {
                endpoint : String ,internal;
                attrName : String, internal;
                editProp : Boolean, internal;
                propName "Name": String,display;
                propValue "Value": text_field,disable when !editProp, on_change updatePropertyValue;
                editPropValue "" : button, icon edit, on_click  editPropertyValue;
            };
        };
    };

    confirmRestartNode "Restart Node": dialog
    {
        nodeNameLabel : String, display;
        msgNode "Are you sure to restart the node ?": label;
        runningTaskOnNode : String, display, content_style "label label-warning";
        footer {
            restartNode "Restart": button, on_click restartNode;
            closeRestartDialog "Close" : button,  on_click closeResetNodeDialog, content_style "margin-left-10";
        };
    };
}

form ClusterForm "Cluster"
    entity ClusterConf
{
    header {
        message(title);
        search_box,  style "pull-right";
    };

    clusterName "Name"       : name;
    discoveryEntryPoints "Nodes" : entryPoints,  table {
        ip "Address" : address;
    };

    horizontal,  style "margin-top-20" {
        addRowBottom "Add": button(add_row,  discoveryEntryPoints),  style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row,  discoveryEntryPoints),  on_click removeNode;
    };

    footer {
        button(save);
        button(cancel);
        button(delete),  style "pull-right";
    };
}

form ClusterListForm "Cluster List"
    on_load init
{
    newC "New" : button, on_click createNew;
    loading: Boolean,internal;
    clusters: ClusterConf, table(8)
    {
        name, display, link_form ClusterForm, link_pk name;
        selectCluster "Select": check_box, on_change selectCurrentCluster;
    };
}


form ClusterDashBoard on_load  configure on_schedule refresh 10
{
    disable : Boolean, internal,  default true;
    selectedServerNode:Int, internal;

    vertical , style "margin-top-40" {

        horizontal {
            clusterName "Cluster Name": String, display, link_form ClusterAdminForm, link_pk clusterName, label_col 5, col 3;

        };

        summaryGrp "Summary": vertical , collapsible {
            nodeSummary: table(10)
            {
                available: Boolean, internal;
                nodeName "Server Node": display, style "no-label bold", link_form NodeAdminForm, link_pk nodeName,width 10;
                heapMemoryUsage "Heap Memory Usage": horizontal,width 20
                {
                    vertical {
                        pSEdenSpaceTt:internal;
                        pSEdenSpaceL "Eden":label;
                        pSEdenSpace "Eden": progress, 
                                    content_style pSEdenSpace > 80? "progress-bar-danger" : (pSEdenSpace < 50 ? "progress-bar-success" : "progress-bar-warning" ), 
                                    placeholder pSEdenSpace + "%", 
                                    tooltip pSEdenSpaceTt, 
                                    no_label;
                    };
                    vertical {
                        pSSurvivorSpaceTt:internal;
                        pSSurvivorSpaceL "Survivor":label;
                        pSSurvivorSpace "Survivor": progress, 
                                    content_style pSSurvivorSpace > 80? "progress-bar-danger" : (pSSurvivorSpace < 50 ? "progress-bar-success" : "progress-bar-warning" ) , 
                                    placeholder pSSurvivorSpace + "%", 
                                    tooltip pSSurvivorSpaceTt, 
                                    no_label;
                    };
                    vertical {
                        pSOldGenTt:internal;
                        pSOldGenL "Old Gen":label;
                        pSOldGen : progress, 
                                    content_style pSOldGen > 80? "progress-bar-danger" : (pSOldGen < 50 ? "progress-bar-success" : "progress-bar-warning" ) , 
                                    placeholder pSOldGen + "%" , 
                                    tooltip pSOldGenTt, 
                                    no_label;
                    };

                };
                uptime "Uptime": String, display;

                components "Components" : display, multiple, disable, content_style "no_resize";

                td "Thread Dump":vertical {
                    threadDump "" : button,  on_click generateThreadDump,no_label,  icon eye , disable when  !available || disable;
                };
                rs "Restart": vertical {
                    restart "": button ,  on_click openRestartNodeDialog ,no_label,  icon repeat, disable when disable;
                };
            };
        };
    };

    confirmRestartNode "Restart Node": dialog
    {
        nodeIx: Int, internal;
        nodeNameLabel : String, display;
        msgNode "Are you sure to restart the node ?": label;
        runningTaskOnNode : String, display, content_style "label label-warning";
        footer {
            restartN "Restart": button, on_click restartNode;
            closeN "Close" : button,  on_click closeResetNodeDialog, content_style "margin-left-10";
        };
    };
}