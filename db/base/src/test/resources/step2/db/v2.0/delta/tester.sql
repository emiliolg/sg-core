-- Sql

create table TableName(TESTER,ADD_TABLE) (
  UUID nvarchar(128) not null,
  TYPE nvarchar(128) not null,
  NAME nvarchar(64),

  constraint PK_ADD_TEST primary key (UUID)
);;



-- if ORACLE
alter table  TableName(TESTER,_METADATA) modify (VERSION  nvarchar(24));;
-- elsif POSTGRES
alter table  TableName(TESTER,_METADATA) alter column VERSION type nvarchar(24);;
-- else
alter table  TableName(TESTER,_METADATA) alter column VERSION nvarchar(24);;
-- end

