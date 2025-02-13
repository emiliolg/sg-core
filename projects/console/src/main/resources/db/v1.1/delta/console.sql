alter  table QName(CONSOLE, CLUSTER_DEF)
	SetDefault(NAME, EmptyString);;

alter  table QName(CONSOLE, CLUSTER_DEF)
	SetDefault(STATUS, 'ACTIVE');;

alter  table QName(CONSOLE, CLUSTER_STATS)
	SetDefault(CLUSTER_NAME, EmptyString);;

alter  table QName(CONSOLE, CLUSTER_STATS)
	SetDefault(STATS, EmptyString);;

alter  table QName(CONSOLE, IP)
	SetDefault(CLUSTER_DEF_NAME, EmptyString);;

alter  table QName(CONSOLE, IP)
	SetDefault(SEQ_ID, 0);;

alter  table QName(CONSOLE, IP)
	SetDefault(IP, EmptyString);;

