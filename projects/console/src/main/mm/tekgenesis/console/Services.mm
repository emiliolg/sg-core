package tekgenesis.console;

form ServiceStatus on_load configure
{
	service:String,internal;
	serviceClass:String,internal;
	ss "Service Status" : horizontal , style " marginBottom15",collapsible{
        serverStatusByHost : table {
            memberName:String,display;
            horizontal,style serviceStatus ? "marginLeft10 alignleft switch-container switch-container-green" : "marginLeft10 alignleft switch-container switch-container-gray"
            {
                horizontal, style serviceStatus ?  "switch switch-right" : "switch switch-left"  { };
                serviceStatus:toggle_button, style "hidden-but", on_ui_change openConfirmDialog;
                serviceStatusSwitch :display, is serviceStatus ? "On" : "Off" ,style "switch-text";
            };
        };
    };

	confirmChangeServiceStatus "Task Service Status": dialog
    {
        msgCSS : String,display;
        node:String,internal;
        footer {
            done "Accept": button,on_click toggleTaskServiceStatus;
            closeCSSDialog "Cancel" : button, on_click closeChangeServiceStatusDialog,content_style "margin-left-10";
        };
    };
}

form Services on_load configure
    primary_key currentNode
{
    currentNode: String , internal;
    currentService:String, internal;
    currentServiceStatus:Boolean, internal;
    loading: Boolean,internal,default false;

    services :table {
        id:String, internal;
        name "Name":String, display;
        status "Status": horizontal, style serviceStatus ? "marginLeft10 alignleft switch-container switch-container-green" : "marginLeft10 alignleft switch-container switch-container-gray"
        {
            horizontal,  style serviceStatus ?  "switch switch-right" : "switch switch-left"  { };
            serviceStatus:toggle_button,  style "hidden-but",  on_ui_change confirmChangeStatus;
            serviceStatusSwitch :display,  is serviceStatus ? "On" : "Off" , style "switch-text";
        };
        isEnabled "Enable": Boolean,on_change updateServiceEnabled;
    };

    confirmChangeServiceStatus "Service Status": dialog
    {
        msgCSS : String, display;
        hasError : Boolean, internal,  default false;
        mgsError: String, hide when !hasError;
        footer {
            done "Accept": button, on_click toggleServiceStatus;
            closeCSSDialog "Cancel" : button,  on_click closeServiceStatusDialog, content_style "margin-left-10";
        };
    };
}