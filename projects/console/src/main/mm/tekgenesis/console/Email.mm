package tekgenesis.console;

enum MailStatusItem
{
	PENDING;
	SUCCESS;
	RETRY;
	FAILED;
}

form EmailForm "Email" on_load init
{
    disable : Boolean, internal, default true;

    horizontal {
        clusterName "Cluster Name": String, display, label_col 4, col 4;
    };

    statusForm :subform(ServiceStatus),inline;

    queueSize "Email Queue" : vertical {

        cleanQueue "Clean queue" : button, on_click openCleanQueueDialog;

        mailStatusTable "":table {
            mailStatusLabel: String, display;
            mailItems : Int,display;
        };

    };

    cleanQueueDialog "Clean Queue": dialog
    {
        vertical {
            mailStatus "Email Status": MailStatusItem, tags_combo_box , hint "Select which status do you want to clean";
        };
        footer {
            doneCleanQueue "Accept": button,on_click cleanQueue;
            closeCleanQueueDialog "Cancel" : button, on_click closeCleanQueueDialog,content_style "margin-left-10";
        };
    };
}