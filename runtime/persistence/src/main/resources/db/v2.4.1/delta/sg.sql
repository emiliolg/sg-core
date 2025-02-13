create table TableName(SG, DELETED_INSTANCES) (
  ENTITY    nvarchar(128) not null,
  DELETED_KEY nvarchar(1000) not null,
  TS   timestamp(3)  not null,

  constraint PK_DELETED_INSTANCES_INDEX primary key (ENTITY,DELETED_KEY,TS)
);;

create index IndexName(SG, DELETED_INDEX)
    on TableName(SG, DELETED_INSTANCES)(TS) IndexTableSpace;;
