-- Pretty SQL File --
create table TableName(SG, TASK) (
    TASK_ID                  nvarchar(256)   not null,
    STATUS                   nvarchar(50)    not null,
    TIME                     double          not null,
    RUNNING_AT               nvarchar(256)   not null,
    SCHEDULE_BY              nvarchar(256)   not null,
    TASK_NAME                nvarchar(256),
    CREATION_TIME            datetime(3),
    GROUP_NAME               nvarchar(256),
    CRON_EXECUTION           datetime(3),
    TASK_DATA                clob,

    constraint PK_TASK
        primary key(TASK_ID,TASK_NAME)
);;

create table TableName(SG, TASK_LOG) (
    TASK_UUID                nvarchar(256)   not null,
    TASK_ID                  nvarchar(256)   not null,
    STATUS                   nvarchar(50)    not null,
    TIME                     double          not null,
    RUNNING_AT               nvarchar(256)   ,
    SCHEDULE_BY              nvarchar(256)   ,
    TASK_NAME                nvarchar(256)   not null,
    CREATION_TIME            datetime(3),
    GROUP_NAME               nvarchar(256),
    CRON_EXECUTION           datetime(3),
    TASK_DATA                clob,

    constraint PK_TASK_LOG
        primary key(TASK_ID,TASK_NAME,TIME,STATUS)
);;

create index IndexName(SG, TASK_LOG_INDEX_IDXT)
    on TableName(SG, TASK_LOG)(TIME,TASK_ID) IndexTableSpace;;

create index IndexName(SG, TASK_LOG_INDEX_UUID)
    on TableName(SG, TASK_LOG)(TASK_UUID) IndexTableSpace;;

create index IndexName(SG, TASK_LOG_INDEX_IDXT_INV)
    on TableName(SG, TASK_LOG)(TASK_ID,TIME) IndexTableSpace;;


-- if !POSTGRES
drop index IndexName(SG, TASK_INFO_INDEX_IDXT);;
drop index IndexName(SG, TASK_INFO_INDEX_IDXT_INV);;
-- end