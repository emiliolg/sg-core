alter table TableName(SG,TASK) add NEXT_CRON_EXECUTION      datetime(3);;
alter table TableName(SG,TASK) add MEMBER                   nvarchar(256);;
alter table TableName(SG,TASK) add TASK_UUID                nvarchar(256);;

alter table TableName(SG,TASK_RUNNING) drop column TASK_DATA;;

create index IndexName(SG, TASK_CRON_INDEX)
    on TableName(SG, TASK)(CRON_EXECUTION) IndexTableSpace;;

create index IndexName(SG, TASK_NEXT_CRON_INDEX)
    on TableName(SG, TASK)(NEXT_CRON_EXECUTION) IndexTableSpace;;