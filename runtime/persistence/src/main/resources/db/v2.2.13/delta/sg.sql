-- Pretty SQL File --
create table TableName(SG,TASK_QUEUE) (
    QUEUE_ORDER              double,
    CRON                     boolean not null,
    TASK_FQN                 nvarchar(256)   not null,
    TASK_UUID                nvarchar(256)   not null,
    TASK_ID                  nvarchar(256)   not null,
    TIME                     double          not null,
    SCHEDULER_MEMBER_UUID    nvarchar(256)   ,
    SCHEDULE_BY_MEMBER       nvarchar(256)   ,
    TASK_NAME                nvarchar(256),
    CRON_EXP                 nvarchar(256),
    GROUP_NAME               nvarchar(256),

    constraint PK_TASK_QUEUE
        primary key(TASK_FQN,TASK_NAME,GROUP_NAME)
);;

create index IndexName(SG, TASK_QUEUE_INDEX_GROUP_NAME)
    on TableName(SG, TASK_QUEUE)(GROUP_NAME) IndexTableSpace;;
