alter  table QName(SG, CLUSTER_CONF)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(SG, CLUSTER_STATS)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(SG, HOST_ADDRESS)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(SG, NODE_ENTRY)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(SG, TASK_ENTRY)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

alter  table QName(SG, TASK_EXECUTION_LOG)
	AddColumn(INSTANCE_VERSION                  bigint           default 0 not null);;

