package tekgenesis.console;

enum Messages
{
	SERVICE_STATUS_CHANGE :"Are you sure to '%s' %s Service ?";
	STOP_ACTION: "Stop";
	START_ACTION: "Start";
	SERVER_UNAVAILABLE: "Server not available. Try reloading the form";
	HEAP_DUMP_DOWNLOAD: "File downloaded";
	TASKS_RUNNING:"Warning: There are Tasks running on member";
	SHUTDOWN_FAILED: "Failed to execute the shutdown";
	BRANCH_NOT_SPECIFIED: "Branch not specified";
	UPDATE_VERSION_OK:"Ok";
    UPDATE_VERSION_PROCESSING:"Processing...";
    UPDATE_VERSION_FAILED:"Failed";
    UPDATE_RESTART: "Restaring...";
    SERVICE_STATUS_CHANGE:"Are you sure to %s the Service ?";
    TASK_ALREADY_RUNNING:"The task '%s' is already running.";
    TASK_INSTANCE_FAILED:"Unable to create Task Instance";
	TASK_NOT_FOUND:"The task '%s' not found.";
	TASK_ALREADY_SUSPENDED:"Task Already suspended";
	TASK_ACTION:"Executing action '%s'";
	ERROR_APPLYING_CHANGES:"An error ocurrs trying to apply the changes (%s)";
}