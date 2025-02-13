package tekgenesis.sg;

import tekgenesis.task.TaskStatus;


entity TaskEntry "The Information related to a Task instance"
    primary_key name
{
    name           : String(256);
    status         : TaskStatus;
    suspended      : Boolean;
    cronExpression : String(256);
    scheduleAfter  : String(256);
    dueTime        : DateTime;
    expirationTime : DateTime, optional;
    member         : String(256);
    mdc            : String(40);
    data           : String(10000);
    dataTime       : DateTime(3), optional;
    startTime      : DateTime(3), optional;
    currentLog     : TaskExecutionLog, optional;
}

entity TaskExecutionLog "Information related with task executions"
    index name, startTime
    index startTime
    index updateTime
{
    name           : String(256);
    mdc            : String(40);
    startTime      : DateTime(3);
    endTime        : DateTime(3), optional;
    expirationTime : DateTime(3), optional;
    member         : String(256);
    data           : String(10000);
    dataTime       : DateTime(3), optional;
    status         : TaskStatus;
    errorMessage   : String(1000), optional;
    totalItems     : Int;
    successItemsCount:Int;
    errorItemsCount: Int;
    ignoreItemsCount: Int;
    taskUpdateTime : DateTime(3);
}

