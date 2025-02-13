# Dashboard

_tekgenesis.admin_

## Overview

The dashboard section allows visualization of the general status of the cluster, node per node.
The main view shows each node and their current status.

For each node:

- Memory Heap Use: The use of the JVM memory heap. The memory heap is divided in three sections: Eden, Survivor and Old Gen.
- Uptime: The node uptime 
- Components: Installed components with versions

![DashBoard](/img/dashboard.png)

![ThreadDump](/img/threadDump.png) Thread Dump: Allows downloading the nodes Thread Dump.  (Text file containing all JVM available thread dumps and current state for each one of them)
![Restart](/img/restart.png) Restart: Allows restarting the node

When a single node is selected, the details will be shown. In this view, Services, Configuration, Logging and Properties options will be modifiable.
Each modification made will only affect the selected node, and not the complete cluster.

![Summary](/img/dashSummary.png) 

## Services
The services section allows the user to Start/Stop Enable/Disable the available services for the selected node.

![Services](/img/dashServices.png) 

Starting/Stopping a service is a TEMPORARY state change. When the node is restarted the service will initialize again.
Enabling/Disabling a services is a PERMANENT state change. When the node is restarted the service will maintain the same state.

- ElasticSearchService: Responsible for communication with the ElasticSearch server 
- EmailService: Responsible for sending mails to the Email Server.
- ImporterService: Responsible for importers.
- JmxService: Responsible for initializing the JMX.
- MetricService: Responsible for sending Metrics.
- TaskService: Responsible for monitoring and executing tasks.
- WebProxyService: Responsible for initializing the communication between the web proxy and the HA Proxy
