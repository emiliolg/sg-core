alter  table QName(SG, TASK_ENTRY)
	AddColumn(START_TIME                        datetime(3)      default CurrentTime not null);;

