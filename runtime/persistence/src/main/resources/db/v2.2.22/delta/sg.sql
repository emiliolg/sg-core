-- Pretty SQL File --

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

create index IndexName(SG, STATS_TIME_IDXT)
    on TableName(SG, STATS)(STAT_TIME) IndexTableSpace;;