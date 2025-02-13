# Configuration

_tekgenesis.admin_

## Overview

Cluster Global changes can be made in the configuration section. All changes will be replicated in each cluster node.

###Http
Allows the user to activate-deactivate, the Http-Header-Id, to trace http responses.

![http](/img/http.png)

###Logging
Application log configuration can be changed globally through the following properties:


| |Description|
|----|--------------------|
|Root Log Level||
|Log Filename||
|Max File Size|Max log size|
|Log Directory||
|Max Days|Max days to keep log|
|Console Output|Activate/Deactivate standard output|
|Xml Output|Activate/Deactivate XML log format|
|File Output|Activate/Deactivate file output|
|Debug All|Quick access to set all logs to DEBUG level|
|Debug Tekgenesis|Quick access to set all Sui Generis logs to DEBUG level|
|Context Name|Logs Context Name|

###Gelf
Gelf is the protocol used to send log information to Gralog


| |Description|
|----|--------------------|
|Server|Gelf server name or ip|
|Port|Gelf server port|
|Facility|Facility name for published logs|
|Output|Activate/Deactivate standard output|

###Loggers
Each different Logger is shown here. The loggers are the sources for each log, usually each logger is associated to a particular class. They can be filtered using the search field.
And their specific log level can be changed from here.

![logging](/img/logging.png)

###Properties
Properties can be visualized and modified in this section. All modifications will affect all cluster nodes.
When a value is modified, it is stored in the database and taken into account on next startup.
![properties](/img/properties.png)


###MBeans
All MBeans exposed by the JVM can be visualized from this section.
![mbeans](/img/mbeans.png)

On an MBean selection, it's attributes and operations are displayed.
![selectedMbean](/img/selectedMbean.png)

