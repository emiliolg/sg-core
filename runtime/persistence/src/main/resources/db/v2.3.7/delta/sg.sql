alter  table QName(SG, TASK_ENTRY)
	SetDefault(START_TIME, null);;

alter  table QName(SG, TASK_ENTRY)
	DropNotNull(START_TIME);;

