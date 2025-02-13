alter table TableName(SG, TASK_DATA) drop constraint PK_TASK_DATA;;
alter table TableName(SG, TASK_DATA) add  constraint PK_TASK_DATA primary key (TASK_ID,TASK_NAME);;