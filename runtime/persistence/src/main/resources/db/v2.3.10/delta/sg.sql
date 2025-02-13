alter  table QName(SG, CLUSTER_CONF)
	AddColumn(DESCR                             nvarchar(255)    default EmptyString not null);;

alter  table QName(SG, TASK_EXECUTION_LOG)
	SetDefault(DATA, EmptyString);;

alter  table QName(SG, TASK_ENTRY)
	SetDefault(DATA, EmptyString);;

alter  table QName(SG, CLUSTER_STATS)
	SetDefault(STATS, EmptyString);;

alter  table QName(SG, NODE_ENTRY)
	SetDefault(LOG, EmptyString);;
