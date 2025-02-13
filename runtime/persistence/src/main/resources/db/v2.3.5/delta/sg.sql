alter  table QName(SG, TASK_ENTRY)
	drop column ERROR_MESSAGE;;

alter  table QName(SG, TASK_ENTRY)
	AddColumn(SUSPENDED                         boolean          default False CheckBoolConstraint(TASK_ENTRY_SUSPENDED_B, SUSPENDED) not null);;

alter  table QName(SG, TASK_ENTRY)
	SetDefault(MDC, EmptyString);;

