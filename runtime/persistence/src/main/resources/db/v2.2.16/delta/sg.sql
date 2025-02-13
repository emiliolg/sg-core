create table TableName(SG, TASK_RUNNING) (
    TASK_ID                  nvarchar(256)   not null,
    TASK_NAME                nvarchar(256)    not null,
    STATUS                   nvarchar(50)    not null,
    MEMBER                   nvarchar(256)   not null,
    CREATION_TIME            datetime(3),
    GROUP_NAME               nvarchar(256),
    CRON_EXECUTION           datetime(3),
    TASK_DATA                clob,

    constraint PK_TASK_RUNNING
        primary key(TASK_ID,TASK_NAME)
);;

create table TableName(SG, TASK_DATA) (
    TASK_ID                  nvarchar(256)   not null,
    TASK_NAME                nvarchar(256)    not null,
    TASK_DATA                clob,

    constraint PK_TASK_DATA
        primary key(TASK_ID)
);;

alter table TableName(SG,TASK) drop column TIME;;
alter table TableName(SG,TASK) drop column RUNNING_AT;;
alter table TableName(SG,TASK) drop column SCHEDULE_BY;;
alter table TableName(SG,TASK) drop column TASK_DATA;;

alter table TableName(SG,TASK_LOG) add MEMBER nvarchar(256);;

update TableName(SG,TASK_LOG) set MEMBER = RUNNING_AT;;

alter table TableName(SG,TASK_LOG) drop column RUNNING_AT;;
alter table TableName(SG,TASK_LOG) drop column SCHEDULE_BY;;