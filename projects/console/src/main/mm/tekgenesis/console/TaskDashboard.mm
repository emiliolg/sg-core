package tekgenesis.console;

enum TASK_TYPE
{
    RUNNING;
    SCHEDULE;
    INACTIVE;
}

form TaskTable
    on_schedule onSchedule 10
    on_load loadTasks
{
    disable : Boolean, internal, default true;
    notSearcheable:Boolean,internal;
    tasksType:TASK_TYPE,internal;
    vertical {
        searchBox "Search" : text_field,optional, on_change filterTasks, hide when notSearcheable;
        taskTable: table ,sortable
        {
            taskFqn : String,internal;
            taskName: String,internal;
            taskLabel "Task":String,display;
            cronExp "Cron Expression": String,display;
            hostMember "Host Member":String,display, hide_column when tasksType != RUNNING;
            taskData "Data (Raw)": String,display,optional;
            taskDataDatetime "Data (DateTime)": DateTime,display,optional;
            nextExecution "Next Execution": date_time_box,disable, hide_column when tasksType == INACTIVE;
            runningTime "Elapsed Time": String,display, hide_column when tasksType != RUNNING;
            completion "Completion ": progress,content_style "bar-success progress_align",placeholder completion, hide_column when tasksType != RUNNING, style "progress_align";
            status "Status" :String,display, on_click viewHistory;

            dropdown,hide when disable, icon cog, style "open-left" {
                    sRunNow "Run Now": label, on_click runNow,icon play, hide when tasksType == RUNNING;
                    sStopNow "Stop Now": label, on_click stopNow,icon stop, hide when tasksType != RUNNING;
                    suspend "Suspend Cron": label ,on_click suspendSchedule,icon pause;
                    restartCron "Resume Cron": label ,on_click resumeSchedule, icon repeat;
                    setData "Set Data": label, on_click openSetDataDialog, icon pencil;
                    schedule "Schedule": label ,on_click openScheduleTaskDialog, icon calendar;
                    stopNow "Stop Scheduling": label, on_click stopSchedule, icon stop;
            };
        };
    };

    setDataDialog "Set Data": dialog
    {
         vertical, style "form-horizontal" {
            taskDataFqnLabel "Task" : String, display;
            newTaskData "Data":String(10000), optional;
            newTaskDataTime "Datetime Data":DateTime, optional;
        };
        footer {
            done "Accept": button,on_click saveTaskData;
            closeSetDataDialog "Cancel" : button, on_click closeSetDataDialog,content_style "margin-left-10";
        };
    };

    changeCronTaskDialog "Schedule Task": dialog
    {
        vertical, style "form-horizontal" {
            taskFqnLabel "Task" : String, display;
            cronExpression "Cron Expression":String(40);
        };
        note "WARNING: If the is an existing task with the same Id it will remove it from execution table." : label;
        footer {
            scheduleTaskAction "Schedule": button,on_click changeCronScheduleTask;
            closeScheduleTaskDialog "Close" : button, on_click closeChangeCronTaskDialog,content_style "margin-left-10";
        };
    };
}

form TaskDashboard "Task" on_load initForm
{
    disable : Boolean, internal, default true;
    horizontal {
        clusterName "Cluster Name": String, display, label_col 4, col 4;
    };

    statusForm :subform(ServiceStatus),inline;

    horizontal{
        suspendAll "Suspend All Task": button, on_click openSuspendAllDialog,icon pause ;
        ResumeAll "Resume All Task": button, on_click resumeAll,icon  repeat ,style "margin-left-30";
    };

    taskTab : tabs,on_change tabTaskSelection {

        activeTask "Running": vertical {
            activeTaskForm: subform(TaskTable),inline;
        };

        scheduleTask "Scheduled": vertical {
            scheduleTaskForm: subform(TaskTable),inline;
        };

        deactiveTask "Inactive": vertical {
            inactiveTaskForm: subform(TaskTable),inline;
        };
    };

    confirmSuspendTasks "Suspend All Tasks": dialog
    {
        msgST "Are you sure to suspend ALL the Tasks ?": label;
        footer {
            restartC "Suspend All": button,on_click suspendAll;
            close "Close" : button, on_click closeSuspendTaskDialog,content_style "margin-left-10";
        };
    };
}

form TaskHistoryForm on_load onLoad
    primary_key taskName,taskFqn
{
    taskFqn : String,internal;
    taskName : String , internal;
    taskLabel "Task": String,display;

    historyTable: table(8),sortable
    {
        taskuuid "Task": String, internal;
        startTime "Starts at": DateTime,display;
        updateTime "Updated at": DateTime ,display;
        elapsedTime "duration": String,display;
        memberName "Member": String,display;
        status "Status" : String, display;
        totalItems "Total Items": Int,display;
        successItems "Success Items": Int,display;
        failedItems "Failed Items": Int,display;
        ignoredItems "Ignored Items": Int,display;
    };
}