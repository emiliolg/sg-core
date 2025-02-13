package tekgenesis.inbox;

form Inbox "Inbox"
    on_load load
    on_schedule search
{
    header { message(title); };

    currentWorkItemClass : String(60), internal, optional;
    currentWorkItemPk : String(60), internal, optional;
    tableView : Boolean, internal;
    noOu : Boolean, internal;

    horizontal, style "filter-affix" {
        filter    : tekgenesis.authorization.OrgUnit, combo_box, on_ui_change search, hide when noOu;
        search    : String, icon search, on_ui_change search, optional;
        toggleView : button, icon_expr tableView ? "th" : "bars", on_click  toggle;
        refresh : button, icon refresh, on_click search;
    };

    inboxTable : table(50), on_click navigate, style "no-thead inbox", row_style "clickable" + (onlyToMeT ? " me" : ""), placeholder "No tasks on Inbox.", hide when !tableView {
        workItemClassT : String(60), internal; // WorkItem class
        workItemPkT : String(60), internal; // WorkItem primary key
        onlyToMeT : Boolean, internal; // Only to me flag
        assigneeT "Assignee" : String, display;
        reporterT "Reporter" : String, display;
        descriptionT "Description" : String(60), display;
        taskT "Task" : String, display;
        creationT "Creation" : DateTime, display, style "text-right";
    };


    inbox : section , hide when tableView, style "inbox", row_style cardStyle{

        vertical, style "thumbnail card"{

            hasReporter : Boolean, internal; // Has Reporter flag
            onlyToMe : Boolean, internal; // Only to me flag
            workItemClass : String(60), internal; // WorkItem class
            workItemPk : String(60), internal; // WorkItem primary key
            cardStyle : String(20), internal, is !hasReporter ? (onlyToMe ? "no-header only-me" : "no-header" ) : (onlyToMe ? "only-me " : "" ) ;

            assignee : display, style "header-label card-assignee", icon_expr "user";
            ouName : display, style "header-label card-ou", icon_expr "user-md";

            horizontal, style "card-header no-label-childs" {
                img      : image, content_style "img-circle", style "header-image";
                vertical {
                    reporter : String, display, style "card-reporter";
                    creation : DateTime, display, style "card-date";
                };
            };

            vertical, style "card-content no-label-childs"{
                task : String, display, style "task-name";
                description : String(60), display, style "task-description";
            };

//            vertical, hide when length(description) < 70, style "card-footer" { moreBtn "More" : label, on_click showMore, icon_expr expanded ? "chevron-up" : "chevron-down"; };
            vertical, style "card-footer" { viewBtn "View" : label, on_click navigate, icon chevron_right; };
        };
    };
}

enum Messages "Messages" {
    TASK_ALREADY_CLOSE : "Task already closed.";
}

final type WorkItem {
    id:Int;
    title:String;
    description:String;
    assignee: String;
    reporter:String;
    priority:Int;
    avatar_sha:String;
    type:String;
    updateTime: Real;
}

final type WorkItemMessage
{
    payload:String;
    action:String ,default "save";
}

remote handler InboxHandler
    on_route "/inbox"
    secure_by applicationToken
{
    "/list"           : WorkItem*, list;
}

remote handler WorkItemHandler
    on_route  "/wi/process"
    secure_by applicationToken
{
    "/$name/$id" :   String, process,body WorkItemMessage,method post;
}

menu InboxMenu "Inbox"
{
    Inbox;
}
