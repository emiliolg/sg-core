# Task

_tekgenesis.admin_

Shows the current progress of the defined Tasks.

Task are the way to execute periodic and asynchronous operations.

![Task Sample](/img/tasks.png)

Tasks can be visualized here, sorted by their state:

- Executing: Task is running
- Scheduled: Task is not currently running but has a cron expression assigned.
- Inactive: Task is not currently running and does not have a cron expression assigned.

###Executing
 
![task executing](/img/tasksExecuting.png)

###Inactive
 
![task inactive](/img/tasksInactive.png)

###Scheduled
 
![task scheduled](/img/tasksScheduled.png)

Clicking ![task icon](/img/tasksIcon.png), the user can see the task options. 

- Execute Now: Execute the task immediately, unless it is currently running.
- Suspend Cron: Suspend the task. 
- Resume Cron: Remove the suspend mark
- Edit Data: Allows data edition for the task.
- Schedule: Configure the cron expression
- Stop Scheduled: Remove the cron expression.

**Cron Expression rules**
Cron expressions are similar to unix ones

``` 
┌───────────── seconds (0 - 59)
│ ┌───────────── minutes (0 - 59)
│ │ ┌───────────── hours (0 - 23)
│ │ │ ┌───────────── day of month (1 - 31)
│ │ │ │ ┌───────────── month (1 - 12)
│ │ │ │ │ ┌───────────── Day of week (SUN-SAT)
│ │ │ │ │ |
│ │ │ │ │ |
* * * * * *

```