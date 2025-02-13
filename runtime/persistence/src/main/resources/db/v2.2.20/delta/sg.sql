-- Pretty SQL File --
-- if ORACLE
truncate table TableName(SG,TASK_LOG) drop storage;;
-- end

-- if !POSTGRES
drop index IndexName(SG, TASK_LOG_INDEX_UUID);;
drop index IndexName(SG, TASK_LOG_INDEX_IDXT);;
drop index IndexName(SG, TASK_LOG_INDEX_IDXT_INV);;
-- end

alter table TableName(SG,TASK_LOG) drop constraint PK_TASK_LOG;;

alter table TableName(SG,TASK_LOG) add constraint PK_TASK_LOG_UUID primary key (TASK_UUID);;

alter table TableName(SG,TASK_LOG) add START_TIME  datetime(3);;
alter table TableName(SG,TASK_LOG) add UPDATE_TIME datetime(3);;

create index IndexName(SG, TASK_LOG_INDEX_TASKIDTIME)
    on TableName(SG, TASK_LOG)(TASK_ID,TIME) IndexTableSpace;;

