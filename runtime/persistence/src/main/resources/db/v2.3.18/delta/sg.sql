alter  table QName(SG, TASK_EXECUTION_LOG)
	AddColumn(TOTAL_ITEMS                       int              default 0 not null);;

alter  table QName(SG, TASK_EXECUTION_LOG)
	AddColumn(SUCCESS_ITEMS_COUNT               int              default 0 not null);;

alter  table QName(SG, TASK_EXECUTION_LOG)
	AddColumn(ERROR_ITEMS_COUNT                 int              default 0 not null);;

alter  table QName(SG, TASK_EXECUTION_LOG)
	AddColumn(TASK_UPDATE_TIME                  datetime(3)      default CurrentTime not null);;

