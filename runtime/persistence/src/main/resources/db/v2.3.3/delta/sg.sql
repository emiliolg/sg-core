
alter table TableName(SG,TASK_QUEUE) drop column QUEUE_ORDER;;
alter table TableName(SG,TASK_QUEUE) drop column TIME;;
alter  table QName(SG, TASK_QUEUE)
	AddColumn(CREATION_TIME                               datetime(3));;