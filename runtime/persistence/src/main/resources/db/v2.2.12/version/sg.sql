-- Pretty SQL File --
create table TableName(SG,_METADATA) (
    VERSION                  nvarchar(24)    not null,
    SHA                      nvarchar(128)   not null,
    SHA_OVL                  nvarchar(128),
    SCHEMA                   clob,
    OVERLAY                  clob,
    constraint PK__METADATA
        primary key(VERSION)
);;

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

  constraint PK_RESOURCE_INDEX primary key (UUID, VARIANT)
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

create index IndexName(SG, EVENT_IMPL_AS_F0B874_IDXT)
    on TableName(SG, EVENT_IMPL)(TIMESTAMP) IndexTableSpace;;

create index IndexName(SG, RESOURCE_INDEX_IDXT)
    on TableName(SG, RESOURCE_INDEX)(URL) IndexTableSpace;;

create index IndexName(SG, RESOURCE_INDEX_IDXT2)
    on TableName(SG, RESOURCE_INDEX)(UUID) IndexTableSpace;;

create index IndexName(SG, TASK_INFO_INDEX_IDXT)
    on TableName(SG, TASK_INFO)(TIME,TASK_ID) IndexTableSpace;;

create index IndexName(SG, TASK_INFO_INDEX_IDXT_INV)
    on TableName(SG, TASK_INFO)(TASK_ID,TIME) IndexTableSpace;;

