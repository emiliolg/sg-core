package tekgenesis.admin;

enum Message "Message" {
     SEACH : "Search";
     AND: "And";
     OR: "Or";
     MISSED_FIELDS: "Missing Filter values. Please fill all fields";
     ROW_NOT_SELECTED: "No row selected";
     ENTITY_NOT_FOUND: "Entity '%s' not found";
     SHUTDOWN_FALED:"Failed to execute the shutdown";
     SERVER_UNAVAILABLE: "Server not available. Try reloading the form";
     NODES_RESTARTED : "All nodes are restarted";
     TASKS_RUNNING: "Warning: There are Tasks running on the Cluster (Nro. tasks running '%s' )";
     BRANCH_NOT_SPECIFIED: "Branch not specified";
     FOOTER_TEXT : "%s / Powered by ";
     NO_COMPONENTS_INFO : "No Components Information";
}