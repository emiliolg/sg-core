-- Sql

create table TableName(TESTER,TEST) (
  UUID nvarchar(128) not null,
  TYPE nvarchar(128) not null,
  NAME nvarchar(64),

  constraint PK_TEST primary key (UUID)
);;

create table TableName(TESTER,_METADATA) (
  VERSION nvarchar(24)   not null,
  SHA     nvarchar(128)  not null,
  SHA_OVL nvarchar(128),
  UPDATE_TIME datetime(0),
  SCHEMA  clob,
  OVERLAY clob,

  constraint PK__METADATA primary key (VERSION)
);;


