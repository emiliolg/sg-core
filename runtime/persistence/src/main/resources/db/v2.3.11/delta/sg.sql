alter  table QName(SG, TASK_EXECUTION_LOG)
	SetDefault(ERROR_MESSAGE, null);;

alter  table QName(SG, TASK_EXECUTION_LOG)
	DropNotNull(ERROR_MESSAGE);;

