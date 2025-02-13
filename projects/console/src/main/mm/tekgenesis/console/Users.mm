package tekgenesis.console;

form UsersForm "Users" on_load loadUsers
{
    horizontal {
        clusterName "Cluster Name": String, display, label_col 4, col 4;
    };
    searchBox "Search" : text_field,optional, on_change filterUsers;
    disable : Boolean, internal, default true;
    usersTable : table(20), sortable {
        userName "Username" : display;
        sessionCount "Sessions":Int, display, on_click sessionDetails,default 0;
    } with {
        sum;
    };
    sessionDialog: dialog {
        sessionsTab "Active Sessions": table(8) {
            sessionId:internal;
            mobile:Boolean, internal;
            lastAccessTime "Last Access Time":DateTime, display;
            horizontal {
                i : display, style "device_icon", content_style mobile ? "device_icon fa fa-mobile-phone" : "device_icon fa fa-laptop";
                host "From": display;
            };
            ou "Ou": display;
            expireV "Close": vertical {
              expire : button, on_click expireUserSession,icon sign_out, hide when disable;
            };
        };
    };
}
