alter  table QName(SG, TASK_ENTRY)
	AddColumn(SCHEDULE_AFTER                    nvarchar(256)    default EmptyString not null);;

