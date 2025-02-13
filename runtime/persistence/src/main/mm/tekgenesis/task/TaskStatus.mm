package tekgenesis.task;

enum TaskStatus
    with { finalState : Boolean; }
{
    NOT_SCHEDULED : "Not Scheduled", false;
    SCHEDULED     : "Scheduled", false;
    SUSPENDED     : "Suspended", false;
    RUNNING       : "Running", false;
    RETRYING      : "Retrying", false;
    FINISHED      : "Finished", true;
    CANCELLED     : "Cancelled", true;
    FAILED        : "Failed", true;
}
