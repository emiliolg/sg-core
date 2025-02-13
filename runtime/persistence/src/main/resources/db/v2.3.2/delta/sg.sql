alter  table QName(SG, TASK_ENTRY)
	AddColumn(MDC                               nvarchar(40));;

alter  table QName(SG, TASK_ENTRY)
	SetDefault(EXPIRATION_TIME, null);;

alter  table QName(SG, TASK_ENTRY)
	DropNotNull(EXPIRATION_TIME);;

