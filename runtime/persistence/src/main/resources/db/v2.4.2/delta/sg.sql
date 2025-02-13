alter  table QName(SG, TASK_EXECUTION_LOG)
	AddColumn(IGNORE_ITEMS_COUNT                int              default 0 not null);;

