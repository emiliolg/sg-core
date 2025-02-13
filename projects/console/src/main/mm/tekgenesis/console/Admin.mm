package tekgenesis.console;

form StatusForm
    on_load load
    on_schedule refresh 10
{
    vertical, style "right-panel-box" {
        sessionsTitle "Sessions" : label, style "right-panel-box-title";
        sessions : chart(line) { sessionsOpened : Int; };
        horizontal, style "center" {
            currentSessionOpenedLabel "Current sessions" : label, col 9;
            currentSessionOpened : Int, display, col 3;
        };
    };

   vertical, style "right-panel-box" {
        usersTitle "Users" : label, style "right-panel-box-title";
        users : chart(line) { usersLog : Int; };
        horizontal, style "center" {
            currentUsersOpenedLabel "Current Users" : label, col 9;
            currentUsersOpened : Int, display, col 3;
        };
    };

    vertical, style "right-panel-box" {
        componentsTitle "Components Version" : label, style "right-panel-box-title";
        components : section {
            horizontal {
                component: String, display;
                version: String, display;
            };
        };
    };

    vertical, style "right-panel-box" {
        generalInfo "General Information" : label, style "right-panel-box-title";

        horizontal {
            loadedTasksLabel " Loaded tasks" : label, icon tasks, col 10;
            loadedTasks : Int, display, content_style "label label-success";
        };

        horizontal {
            runningTasksLabel " Running tasks" : label, icon tasks, col 10;
            runningTasks : Real, display, content_style "label label-info";
        };

        horizontal {
            nodesLabel " Nodes" : label, icon hdd_o, col 10;
            nodes : Int, display, content_style "label label-warning";
        };

        vertical {
            currentNodeNameLabel " Logged on Node" : label, icon sign_in;
            currentNodeName : String, display, col 10, offset_col 2;
        };
    };
}

form ApplicationFooterForm
    unrestricted
    on_load onLoad
{
    footerGroup " " : input_group, flow, no_label {
        footerText : display, optional, style "right-text";
        poweredBy : image, default "/img/logo-TekGenesis-mini.png";
    };
}