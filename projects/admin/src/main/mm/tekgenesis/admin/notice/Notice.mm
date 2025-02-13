package tekgenesis.admin.notice schema advice;


entity Advice "Sui Generis Notifications"
 auditable
 index state
 index creationTime
{
    description : String;
    type  : AdviceType;
    level : Level;
    state : State;
}


enum AdviceType {
    INDEX_MISSING : "Index missing";
    LONG_RUNNING_TASK : "Long running task";
    FAILED_TASK : "Failed task";
    USER_NOTICE : "User Notice";
    INCONSISTENT_INDEX : "Inconsistent Index";
}

enum Level {
    INFO : "Info";
    WARNING : "Warning";
    SEVERE : "Severe";
}

enum State {
    NEW : "New";
    DISMISSED : "Dismissed";
}

