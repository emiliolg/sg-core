package tekgenesis.task;

task ResourceGCTask "Resource GC" schedule "0 0 0 *";

task CleaningTask "Cleaning Task" schedule "0 0 0 *";

task RetryMailSender "Retry Send email" schedule "0 0/1" transaction none;

task SuiGenerisAdvisor "Sui Generis Advisor" schedule "0 0 3 *";

/** SuiGenerisTaskPurge is resposible for delete old/zombie tasks */
task SuiGenerisTaskPurge "Sui Generis Task Purger" schedule "0 0 0/6" transaction isolated;

/** SuigenerisTaskMonitor is resposible for stop task that excedee the running time. This task runs in every node */
task SuiGenerisTaskMonitor "Sui Generis Task Monitor" schedule "0 0/1" transaction none node;

task SuiGenerisIndexMonitor "Sui Generis Index Monitor" schedule "0 0 0 * *";