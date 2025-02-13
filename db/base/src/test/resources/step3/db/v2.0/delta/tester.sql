-- Sql


create table TableName(TESTER,ADD_TABLE) (
  UUID nvarchar(128) not null,
  TYPE nvarchar(128) not null,
  NAME nvarchar(64),

  constraint PK_ADD_TEST primary key (UUID)
);;
