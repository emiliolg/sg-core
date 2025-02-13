-- SQL for Schema SG --


create table TableName(SG,EVENT_IMPL) (
  UUID nvarchar(128) not null,
  TYPE nvarchar(128) not null,
  USER nvarchar(64),
  TIMESTAMP datetime(3) not null,
  DESCRIPTION nvarchar(128),
  DATA clob not null,
  constraint PK_EVENT_IMPL primary key (UUID)
);;

create table TableName(SG, STORED_CONSUMER) (
  ID nvarchar(128) not null,
  DESCRIPTION nvarchar(128),
  TIMESTAMP datetime(3) not null,
  constraint PK_STORED_CONSUMER primary key (ID)
);;


create table TableName(SG, RESOURCE_INDEX) (
  UUID      nvarchar(128) not null,
  VARIANT   nvarchar(64)  not null,
  EXTERNAL  boolean       not null,
  URL       nvarchar(512) not null, -- if is not external this will be the sha
  NAME      nvarchar(256),
  INFO      clob,

  constraint PK_RESOURCE_INDEX primary key (UUID, VARIANT)
);;

create table QName(SG, INDEX_LOCK) (
	INDEX_NAME                        nvarchar(255)    default EmptyString      not null,
	UPDATING_NODE                     nvarchar(255)    default EmptyString      not null,

	constraint PK_INDEX_LOCK          primary key (INDEX_NAME)
);;

create table TableName(SG, RESOURCE_CONTENT) (
  SHA         nvarchar(128) not null,
  SIZE        int           not null,
  MIME_TYPE   nvarchar(128) not null,
  BINARY_DATA blob,
  TEXT_DATA   clob,

  constraint PK_RESOURCE_CONTENT primary key (SHA)
);;

create table TableName(SG, LAST_DELETED) (
  ENTITY    nvarchar(128) not null,
  TS   timestamp(3)  not null,

  constraint PK_LAST_DELETED_INDEX primary key (ENTITY)
);;

create table TableName(SG, DELETED_INSTANCES) (
  ENTITY    nvarchar(128) not null,
  DELETED_KEY nvarchar(1000) not null,
  TS   timestamp(3)  not null,

  constraint PK_DELETED_INSTANCES_INDEX primary key (ENTITY,DELETED_KEY,TS)
);;

create table TableName(SG, TASK_INFO) (
    TASK_UUID                nvarchar(256)   not null,
    TASK_ID                  nvarchar(256)   not null,
    TIME                     double          not null,
    RUNNING_AT_MEMBER        nvarchar(256)   not null,
    RUNNING_MEMBER_UUID      nvarchar(256)   not null,
    SCHEDULER_MEMBER_UUID    nvarchar(256)   not null,
    SCHEDULE_BY_MEMBER       nvarchar(256)   not null,
    CRON_EXECUTION           datetime(3),
    STATUS                   nvarchar(50)    not null,
    CREATION_TIME            datetime(3),
    TASK_NAME                nvarchar(256),
    CRON_EXP                 nvarchar(256),
    GROUP_NAME               nvarchar(256),
    TASK_DATA                clob,

    constraint PK_TASK_INFO
        primary key(TASK_UUID,TASK_ID,TIME)
);;

create table TableName(SG, TASK_SUSPENDED) (
    TASK_ID                  nvarchar(256)   not null,
    CREATION_TIME            datetime(3),

    constraint PK_TASK_SUSPENDED
        primary key(TASK_ID)
);;

create table TableName(SG, LIFECYCLE_TASK) (
    TASK_ID                  nvarchar(256)   not null,
    STATUS                   nvarchar(20),
    HOST                     nvarchar(256),
    constraint PK_LIFECYCLE_TASK
        primary key(TASK_ID,HOST)
);;

create table TableName(SG, TASK_LOCK) (
    LOCK_NAME                  nvarchar(256)   not null,

    constraint PK_TASK_LOCK
        primary key(LOCK_NAME)
);;

create table TableName(SG,TASK_QUEUE) (
    CRON                     boolean not null,
    TASK_FQN                 nvarchar(256)   not null,
    TASK_UUID                nvarchar(256)   not null,
    TASK_ID                  nvarchar(256)   not null,
    CREATION_TIME            datetime(3),
    SCHEDULE_BY_MEMBER       nvarchar(256),
    TASK_NAME                nvarchar(256),
    CRON_EXP                 nvarchar(256),
    GROUP_NAME               nvarchar(256),

    constraint PK_TASK_QUEUE
        primary key(TASK_FQN,TASK_NAME,GROUP_NAME)
);;

create table TableName(SG, TASK) (
    TASK_ID                  nvarchar(256)   not null,
    TASK_UUID                nvarchar(256)   ,
    STATUS                   nvarchar(50)    not null,
    TASK_NAME                nvarchar(256),
    CREATION_TIME            datetime(3),
    GROUP_NAME               nvarchar(256),
    CRON_EXECUTION           datetime(3),
    NEXT_CRON_EXECUTION      datetime(3),
    MEMBER               nvarchar(256)   ,
    CRON_EXP        nvarchar(256)   ,
    constraint PK_TASK
        primary key(TASK_ID,TASK_NAME)
);;

create table TableName(SG, TASK_LOG) (
    TASK_UUID                nvarchar(256)   not null,
    TASK_ID                  nvarchar(256)   not null,
    STATUS                   nvarchar(50)    not null,
    TIME                     double          not null,
    MEMBER                   nvarchar(256)   ,
    TASK_NAME                nvarchar(256)   not null,
    CREATION_TIME            datetime(3),
    START_TIME               datetime(3),
    UPDATE_TIME              datetime(3),
    GROUP_NAME               nvarchar(256),
    CRON_EXECUTION           datetime(3),
    TASK_DATA                clob,

    constraint PK_TASK_LOG_UUID primary key(TASK_UUID)
);;


create table TableName(SG, TASK_DATA) (
    TASK_ID                  nvarchar(256)   not null,
    TASK_NAME                nvarchar(256)    not null,
    TASK_DATA                clob,

    constraint PK_TASK_DATA
        primary key(TASK_ID,TASK_NAME)
);;

create table TableName(SG, ENV_PROPERTY) (
    PROP_NODE                  nvarchar(512)   not null,
    PROP_SCOPE                  nvarchar(512)   not null,
    PROP_NAME                  nvarchar(512)   not null,
    PROP_VALUE                 clob            not null,
    PROP_CLASS                 nvarchar(512)   not null,
    constraint PK_ENV_PROPERTY
        primary key(PROP_NODE,PROP_SCOPE,PROP_NAME)
);;

create table TableName(SG, STATS) (
    CLUSTER                  nvarchar(512)   not null,
    STAT_TIME                datetime(3)     not null,
    STAT_VALUE               clob            not null,
    constraint PK_STATS
        primary key(CLUSTER,STAT_TIME)
);;

create index IndexName(SG, EVENT_IMPL_AS_F0B874_IDXT)
    on TableName(SG, EVENT_IMPL)(TIMESTAMP) IndexTableSpace;;

create index IndexName(SG, RESOURCE_INDEX_IDXT)
    on TableName(SG, RESOURCE_INDEX)(URL) IndexTableSpace;;

create index IndexName(SG, RESOURCE_INDEX_IDXT2)
    on TableName(SG, RESOURCE_INDEX)(UUID) IndexTableSpace;;

create index IndexName(SG, TASK_LOG_INDEX_TASKIDTIME)
    on TableName(SG, TASK_LOG)(TASK_ID,TIME) IndexTableSpace;;

create index IndexName(SG, TASK_QUEUE_INDEX_GROUP_NAME)
    on TableName(SG, TASK_QUEUE)(GROUP_NAME) IndexTableSpace;;

create index IndexName(SG, TASK_CRON_INDEX)
    on TableName(SG, TASK)(CRON_EXECUTION) IndexTableSpace;;

create index IndexName(SG, TASK_NEXT_CRON_INDEX)
    on TableName(SG, TASK)(NEXT_CRON_EXECUTION) IndexTableSpace;;

create index IndexName(SG, STATS_TIME_IDXT)
    on TableName(SG, STATS)(STAT_TIME) IndexTableSpace;;

create index IndexName(SG, DELETED_INDEX)
    on TableName(SG, DELETED_INSTANCES)(TS) IndexTableSpace;;

create index IndexName(SG, DELETED_TS_INDEX)
    on TableName(SG, DELETED_INSTANCES)(ENTITY, TS) IndexTableSpace;;


-- Functions

-- if ORACLE

create or replace function QName(SG,BINSET_TO_STR)( BINSET in number, TRANSLATION_TABLE in nvarchar) return nvarchar is
    I        number := BINSET;
    INDX     binary_integer;
    T        nvarchar(32767) := TRANSLATION_TABLE;
    V        nvarchar(200);
    RESULT   nvarchar(32767);
begin

    while I > 0 and T is not null loop
        INDX := INSTR(T, ':');
        if INDX = 0 then
          V := T;
          T := null;
        else
          V := SUBSTR(T, 1, INDX-1);
          T := SUBSTR(T, INDX+1);
        end if;

        if BITAND(I,1) = 1 then
            if RESULT is null then
                RESULT := V;
            else
                RESULT := RESULT || ',' || V;
            end if;
        end if;
        I := FLOOR(I / 2);
    end loop;
    return RESULT;
end BINSET_TO_STR;

-- end